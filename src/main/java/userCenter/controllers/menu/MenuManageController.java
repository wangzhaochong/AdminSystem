package userCenter.controllers.menu;


import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.omg.PortableInterceptor.DISCARDING;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import userCenter.Utils.CookiesUtil;
import userCenter.Utils.Response;
import userCenter.controllers.userCenterController;
import userCenter.interceptor.LoginInterceptor;
import userCenter.mapper.DishInfoMapper;
import userCenter.mapper.UserMapper;
import userCenter.model.OrderDishInfo;
import userCenter.model.batis.DishInfo;
import userCenter.model.batis.OrderRecode;
import userCenter.model.batis.User;
import userCenter.model.batis.UserStoreInfo;
import userCenter.model.enumModel.OrderType;
import userCenter.model.enumModel.UserType;
import userCenter.service.MenuService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("menuManage")
public class MenuManageController {

    @Autowired
    MenuService menuService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    DishInfoMapper dishInfoMapper;

    @Transactional
    @ResponseBody
    @RequestMapping(value = "commentPost", method = RequestMethod.POST)
    public Object orderList(HttpServletRequest request,
                            @RequestParam String storeId,
                            @RequestParam String comment,
                            @RequestParam String tableId){

        OrderRecode req = new OrderRecode();

        if(StringUtils.isNotBlank(storeId)
                && StringUtils.isNumeric(storeId)) {
            req.setUid(Long.parseLong(storeId));
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(StringUtils.isNotBlank(tableId)
                && StringUtils.isNumeric(tableId)) {
            req.setTableId(Long.parseLong(tableId));
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //判断是否桌号有效
        if (!menuService.tableNumberAvailable(Long.parseLong(storeId), Long.parseLong(tableId))){
            return Response.fail(Response.RespType.OT_UNIQUE_PHONE);
        }

        req.setStatus(OrderType.ORDER_WAIT.getType());

        OrderRecode recode = null;

        List<OrderRecode> recodes = menuService.getOrderRecode(req);
        if(recodes != null
                && recodes.size() > 0){
            recode = recodes.get(0);
        }

        if(recode != null && recode.getUid() > 0){

            boolean orderIdRight = false;

            //检查cookie，只有orderid正确的才能修改评论
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    try {
                        if (cookie.getName().equals("orderinfo")) {
                            String name = cookie.getValue();
                            String orderId = LoginInterceptor.decodeBase64(name);
                            if (recode.getOrderId() != null
                                    && recode.getOrderId().toString().equals(orderId)) {
                                orderIdRight = true;
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }

            if(!orderIdRight){
                return Response.fail(Response.RespType.NOT_UNIQUE_PHONE);
            }

            if(comment == null){
                comment = "";
            }

            OrderRecode updateRecode = new OrderRecode();
            updateRecode.setId(recode.getId());
            updateRecode.setUid(recode.getUid());
            updateRecode.setCustomComment(comment);
            Integer res = menuService.updateOrderRecode(updateRecode);
            if(res > 0){
                return Response.ok(comment);
            }
        }
        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "orderPost", method = RequestMethod.POST)
    public Object orderPost(HttpServletRequest request,
                            HttpServletResponse response,
                            @RequestParam String idStr,
                            @RequestParam String storeId,
                            @RequestParam String tableId){

        if(StringUtils.isBlank(idStr)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        OrderRecode req = new OrderRecode();

        if(StringUtils.isNotBlank(storeId)
                && StringUtils.isNumeric(storeId)) {
            req.setUid(Long.parseLong(storeId));
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(StringUtils.isNotBlank(tableId)
                && StringUtils.isNumeric(tableId)) {
            req.setTableId(Long.parseLong(tableId));
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //判断是否桌号有效
        if (!menuService.tableNumberAvailable(Long.parseLong(storeId), Long.parseLong(tableId))){
            return Response.fail(Response.RespType.OT_UNIQUE_PHONE);
        }

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(Long.parseLong(storeId));
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        req.setStatus(OrderType.ORDER_WAIT.getType());
        OrderRecode recode = null;

        List<OrderRecode> recodes = menuService.getOrderRecode(req);
        if(recodes != null
                && recodes.size() > 0){
            recode = recodes.get(0);
        }

        //对orderId进行检验
        if(recode != null){
            //检查cookie，只有orderid正确的才能修改菜单
            boolean orderIdRight = false;
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    try {
                        if (cookie.getName().equals("orderinfo")) {
                            String name = cookie.getValue();
                            String orderId = LoginInterceptor.decodeBase64(name);
                            if (recode.getOrderId() != null
                                    && recode.getOrderId().toString().equals(orderId)) {
                                orderIdRight = true;
                                break;
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
            if(!orderIdRight){
                return Response.fail(Response.RespType.NOT_UNIQUE_PHONE);
            }
        }


        Map<Long, Integer> orderMap = menuService.convertOrderStrtoMap(idStr);
        int res = -1;
        if(recode != null && recode.getUid() > 0){
            Map<Long, Integer> oldOrderMap = menuService.convertOrderStrtoMap(recode.getDishListStr());
            for(Long dishId : orderMap.keySet()){
                if(oldOrderMap.containsKey(dishId)){
                    int count = oldOrderMap.get(dishId) + 1;
                    oldOrderMap.put(dishId, count);
                }else{
                    oldOrderMap.put(dishId, orderMap.get(dishId));
                }
            }
            //计算价格
            Float price = menuService.getPriceFromOrderMap(oldOrderMap);
            recode.setTotalPrice(price);
            String dishStr = menuService.convertMaptoOrderStr(oldOrderMap);
            recode.setDishListStr(dishStr);
            recode.setStatus(OrderType.ORDER_WAIT.getType());
            res = menuService.updateOrderRecode(recode);
        }else{
            recode = req;
            Long orderId = menuService.generateOrderId();
            recode.setOrderId(orderId);
            recode.setDishListStr(idStr);
            recode.setStatus(OrderType.ORDER_WAIT.getType());
            recode.setSummitTime(new Date());
            //计算价格
            Float price = menuService.getPriceFromOrderMap(orderMap);
            recode.setTotalPrice(price);
            res = menuService.insertOrderRecode(recode);
            menuService.setOrderIdCookies(orderId,response);
        }
        if(res <= 0){
            return Response.fail(Response.RespType.SERVER_ERROR);
        }

        return  Response.ok();
    }

//    @ResponseBody
//    @RequestMapping(value = "getDishList", method = RequestMethod.GET)
//    public Object orderList(HttpServletRequest request,
//                            @RequestParam String storeId,
//                            @RequestParam String orderId){
//
//        OrderRecode req = new OrderRecode();
//
//        if(StringUtils.isNotBlank(storeId)
//                && StringUtils.isNumeric(storeId)) {
//            req.setUid(Long.parseLong(storeId));
//        }else{
//            return Response.fail(Response.RespType.PARAM_WRONG);
//        }
//
//
//        if(StringUtils.isNotBlank(orderId)
//                && StringUtils.isNumeric(orderId)) {
//            req.setOrderId(Long.parseLong(orderId));
//        }else{
//            return Response.fail(Response.RespType.PARAM_WRONG);
//        }
//
//        OrderRecode recode = null;
//        List<OrderDishInfo> orderDishInfos = Lists.newArrayList();
//        String comment = null;
//
//        List<OrderRecode> recodes = menuService.getOrderRecode(req);
//        if(recodes != null
//                && recodes.size() > 0){
//            recode = recodes.get(0);
//        }
//
//        if(recode != null && recode.getUid() > 0) {
//            Map<Long, Integer> oldOrderMap = menuService.convertOrderStrtoMap(recode.getDishListStr());
//            for(Long dishId : oldOrderMap.keySet()){
//                DishInfo dishReq = new DishInfo();
//                dishReq.setDishId(dishId);
//                List<DishInfo> dishes = menuService.getDishInfo(dishReq);
//                if(dishes != null
//                        && dishes.size() > 0){
//                    DishInfo dishInfo = dishes.get(0);
//                    OrderDishInfo orderDishInfo = new OrderDishInfo();
//                    orderDishInfo.setDishId(dishInfo.getDishId());
//                    orderDishInfo.setDishName(dishInfo.getDishName());
//                    orderDishInfo.setCount(oldOrderMap.get(dishId));
//                    Float dishPrice = menuService.calcPrice(dishInfo.getDishPrice(),orderDishInfo.getCount());
//                    orderDishInfo.setDishPrice(dishPrice);
//                    orderDishInfos.add(orderDishInfo);
//                }
//            }
//            comment = recode.getCustomComment();
//        }
//        Float price = menuService.countPrice(orderDishInfos);
//        Map<String, Object> res = Maps.newHashMap();
//        res.put("price",price.toString());
//        res.put("list",orderDishInfos);
//        res.put("comment",comment);
//        return Response.ok(res);
//    }


}

package userCenter.controllers.menu;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import userCenter.interceptor.LoginInterceptor;
import userCenter.mapper.UserMapper;
import userCenter.model.OrderDishInfo;
import userCenter.model.batis.*;
import userCenter.model.enumModel.CommonTypeEnum;
import userCenter.model.enumModel.OrderType;
import userCenter.model.enumModel.UserType;
import userCenter.service.MenuService;
import userCenter.service.UtilService;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("customMenu")
public class CustomMenuController {

    @Autowired
    MenuService menuService;

    @Autowired
    UserMapper userMapper;

    @RequestMapping(value = "list/{storeId}", method = RequestMethod.GET)
    public String list(HttpServletRequest request,
                        @PathVariable String storeId){
        if(StringUtils.isNotBlank(storeId)
                && StringUtils.isNumeric(storeId)){
            Long id = Long.parseLong(storeId);
            UserStoreInfo storeReq = new UserStoreInfo();
            storeReq.setUid(id);
            List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
            if(stores != null
                    && stores.size() > 0){
                request.setAttribute("store",stores.get(0));
            }else{
                return "/common/wap404";
            }

            //获取分类
            DishCategoryInfo cateReq = new DishCategoryInfo();
            cateReq.setUid(id);
            cateReq.setStatus(CommonTypeEnum.OK.getType());
            List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
            if(cates != null){
                Map<String, List<DishInfo>>dishLists = Maps.newLinkedHashMap();
                List<String>cateNames = Lists.newArrayList();
                for (DishCategoryInfo cate : cates) {
                    List<DishInfo> cateDishList = Lists.newArrayList();
                    dishLists.put(cate.getCateName(), cateDishList);
                }
                DishInfo dishReq = new DishInfo();
                dishReq.setUid(id);
                List<DishInfo> dishes = menuService.getDishInfo(dishReq);
                if(dishes != null){
                    for(DishInfo dish : dishes){
                        for(DishCategoryInfo cate : cates){
                            if(dish.getCateId().equals(cate.getCateId())){
                                if(dishLists.containsKey(cate.getCateName())){
                                    List<DishInfo> cateDishList = dishLists.get(cate.getCateName());
                                    cateDishList.add(dish);
                                }
                            }
                        }
                    }
                }
                //空的分类从map里面删除
                for (DishCategoryInfo cate : cates) {
                    List<DishInfo> cateDishList = dishLists.get(cate.getCateName());
                    if(cateDishList == null || cateDishList.size() == 0){
                        dishLists.remove(cate.getCateName());
                    }
                }
                if(dishLists.size() > 0){
                    cateNames.addAll(dishLists.keySet());
                }
                request.setAttribute("cateWithDishList", dishLists);
                request.setAttribute("cateNames", cateNames);
            }
        }
        return "/customMenu/testlistbasic";
    }

    @RequestMapping(value = "orderList/{storeId}/{tableId}", method = RequestMethod.GET)
    public String orderList(HttpServletRequest request,
                            @PathVariable String storeId,
                            @PathVariable String tableId){

        request.setAttribute("storeId",storeId);
        request.setAttribute("tableId",tableId);

        if(StringUtils.isNotBlank(storeId)
                && StringUtils.isNumeric(storeId)){
        }else{
            return  "/common/wap404";
        }

        if(StringUtils.isNotBlank(tableId)
                && StringUtils.isNumeric(tableId)){
        }else{
            return  "/common/wap404";
        }

        Long id = Long.parseLong(storeId);
        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return  "/common/wap404";
        }

        //是否提交过商店信息
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(id);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            request.setAttribute("store",stores.get(0));
            //menuService.setStoreNameInModle(stores.get(0),request);
        }else{
            return "/common/wap404";
        }

        //获取分类
        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(id);
        cateReq.setStatus(CommonTypeEnum.OK.getType());
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        if(cates != null) {
            Map<String, List<DishInfo>> dishLists = Maps.newLinkedHashMap();
            List<String> cateNames = Lists.newArrayList();
            for (DishCategoryInfo cate : cates) {
                List<DishInfo> cateDishList = Lists.newArrayList();
                dishLists.put(cate.getCateName(), cateDishList);
            }
            DishInfo dishReq = new DishInfo();
            dishReq.setUid(id);
            List<DishInfo> dishes = menuService.getDishInfo(dishReq);
            if (dishes != null) {
                for (DishInfo dish : dishes) {
                    for (DishCategoryInfo cate : cates) {
                        if (dish.getCateId().equals(cate.getCateId())) {
                            if (dishLists.containsKey(cate.getCateName())) {
                                List<DishInfo> cateDishList = dishLists.get(cate.getCateName());
                                cateDishList.add(dish);
                            }
                        }
                    }
                }
            }
            //空的分类从map里面删除
            for (DishCategoryInfo cate : cates) {
                List<DishInfo> cateDishList = dishLists.get(cate.getCateName());
                if(cateDishList == null || cateDishList.size() == 0){
                    dishLists.remove(cate.getCateName());
                }
            }
            if (dishLists.size() > 0) {
                cateNames.addAll(dishLists.keySet());
            }
            request.setAttribute("cateWithDishList", dishLists);
            request.setAttribute("cateNames", cateNames);
        }

        //判断是否有正在进行中的菜单
        OrderRecode req = new OrderRecode();
        req.setUid(Long.parseLong(storeId));
        req.setTableId(Long.parseLong(tableId));
        req.setStatus(OrderType.ORDER_WAIT.getType());
        List<OrderRecode> recodes = menuService.getOrderRecode(req);
        OrderRecode recode = null;
        if(recodes != null
                && recodes.size() > 0){
            recode = recodes.get(0);
        }
        if(recode != null){
            //检查cookie，只有orderid正确的才能展示菜单
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    try {
                        if (cookie.getName().equals("orderinfo")) {
                            String name = cookie.getValue();
                            String orderId = LoginInterceptor.decodeBase64(name);
                            if (recode.getOrderId() != null
                                    && recode.getOrderId().toString().equals(orderId)) {
                                request.setAttribute("orderWait", "true");
                                break;
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }

        return "/customMenu/listbasic";
    }

    @RequestMapping(value = "orderResult/{storeId}/{tableId}", method = RequestMethod.GET)
    public String orderResult(HttpServletRequest request,
                            @PathVariable String storeId,
                            @PathVariable String tableId){

        if(StringUtils.isNotBlank(tableId)
                && StringUtils.isNumeric(tableId)){
        }else{
            return "/common/wap404";
        }

        if(StringUtils.isNotBlank(storeId)
                && StringUtils.isNumeric(storeId)){
        }else{
            return "/common/wap404";
        }

        request.setAttribute("tableId",tableId);
        request.setAttribute("storeId",storeId);

        Long id = Long.parseLong(storeId);
        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return  "/common/wap404";
        }

        //是否提交过商店信息
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(id);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            request.setAttribute("store",stores.get(0));
        }else{
            return "/common/wap404";
        }

        //判断是否桌号有效
        if (!menuService.tableNumberAvailable(Long.parseLong(storeId), Long.parseLong(tableId))){
            return "/common/wap404";
        }

        OrderRecode recode = null;
        List<OrderDishInfo> orderDishInfos = Lists.newArrayList();
        String comment = null;
        Float price = 0f;

        //判断是否有正在进行中的菜单
        OrderRecode req = new OrderRecode();
        req.setUid(Long.parseLong(storeId));
        req.setTableId(Long.parseLong(tableId));
        req.setStatus(OrderType.ORDER_WAIT.getType());
        List<OrderRecode> recodes = menuService.getOrderRecode(req);

        if(recodes != null
                && recodes.size() > 0){
            recode = recodes.get(0);
        }
        if(recode != null){

            //检查cookie，只有orderid正确的才能展示菜单
            boolean orderIdRight = false;
            Cookie[] cookies = request.getCookies();
            if(cookies != null) {
                for (Cookie cookie : cookies) {
                    try {
                        if (cookie.getName().equals("orderinfo")) {
                            String name = cookie.getValue();
                            String orderId = LoginInterceptor.decodeBase64(name);
                            if (recode.getOrderId() != null
                                    && recode.getOrderId().equals(Long.parseLong(orderId))) {
                                orderIdRight = true;
                                break;
                            }
                        }
                    } catch (Exception e) {

                    }
                }
            }
            if(!orderIdRight){
                request.setAttribute("price",price.toString());
                return "/customMenu/orderbasic";
            }

            Map<Long, Integer> oldOrderMap = menuService.convertOrderStrtoMap(recode.getDishListStr());
            List<Long>dishIdList = Lists.newArrayList();
            dishIdList.addAll(oldOrderMap.keySet());
            List<DishInfo> dishes = menuService.getDishInfoByList(dishIdList);
            if(dishes != null){
                for(DishInfo dishInfo : dishes){
                    Long dishId = dishInfo.getDishId();
                    OrderDishInfo orderDishInfo = new OrderDishInfo();
                    orderDishInfo.setDishId(dishInfo.getDishId());
                    orderDishInfo.setDishName(dishInfo.getDishName());
                    orderDishInfo.setDisount(dishInfo.getDishPriceDiscount());
                    orderDishInfo.setOriginPrice(dishInfo.getDishPriceOrigin());
                    orderDishInfo.setCount(oldOrderMap.get(dishId));
                    Float dishPrice = menuService.calcPrice(dishInfo.getDishPriceFinal(),orderDishInfo.getCount());
                    orderDishInfo.setDishPrice(dishPrice);
                    orderDishInfos.add(orderDishInfo);
                }
            }
            comment = recode.getCustomComment();
            request.setAttribute("comment",comment);
            price = recode.getTotalPrice();
        }
        request.setAttribute("list",orderDishInfos);
        request.setAttribute("price",price.toString());

        return "/customMenu/orderbasic";
    }

}

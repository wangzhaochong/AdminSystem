package userCenter.controllers.shopManage;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rometools.utils.Longs;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import userCenter.Utils.CookiesUtil;
import userCenter.Utils.Response;
import userCenter.mapper.UserMapper;
import userCenter.model.*;
import userCenter.model.batis.*;
import userCenter.model.enumModel.CommonTypeEnum;
import userCenter.model.enumModel.OrderType;
import userCenter.model.enumModel.SignTypeEnum;
import userCenter.model.enumModel.UserType;
import userCenter.service.MenuService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SignatureException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("/shopManage")
public class CashierManageController {

    @Autowired
    MenuService menuService;

    @Autowired
    UserMapper userMapper;

    public static String OVER_DATE_MESSAGE = "亲爱的用户，您的葫芦微站账号已逾期，请登录葫芦微站www.huluweihzan.cn申请续期";
    public static String CASHIER_COOK_NAME =  "cashierId";

    @RequestMapping(value = "loginIndex/{storeId}", method = RequestMethod.GET)
    public String loginIndex(HttpServletRequest request,
                             @PathVariable String storeId) {

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
            request.setAttribute("cashier_message","");
            request.setAttribute("storeId",storeId);
        }else{
            String mes = "您访问的店铺地址不正确，请登录葫芦微站www.huluweizhan.cn查询正确地址";
            request.setAttribute("cashier_message",mes);
            request.setAttribute("storeId",-1);
            return "/shopManage/loginIndex/";
        }

        return "/shopManage/loginIndex";
    }

    @ResponseBody
    @RequestMapping(value = "getCashierModifyType/{storeId}", method = RequestMethod.GET)
    public Object getCashierModifyType(@PathVariable String storeId) {
        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        Integer res = menuService.getSignMap(SignTypeEnum.NEW_ORDER_AND_COMMENT,Long.parseLong(storeId));

        return Response.ok(res);
    }

    @RequestMapping(value = "cashierIndex/{storeId}", method = RequestMethod.GET)
    public String cashierIndex(HttpServletRequest request,
                               @PathVariable String storeId) {


        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            String mes = "您访问的店铺地址不正确，请登录葫芦微站www.huluweizhan.cn查询正确地址";
            request.setAttribute("cashier_message",mes);
            request.setAttribute("storeId",-1);
            return "/shopManage/loginIndex";
        }


        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            request.setAttribute("cashier_message",OVER_DATE_MESSAGE);
            request.setAttribute("storeId",storeId);
            return  "/shopManage/loginIndex";
        }

        //是否提交过商店信息
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(id);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            request.setAttribute("store",stores.get(0));
        }else{
            request.setAttribute("cashier_message","请先完善店铺信息");
            request.setAttribute("storeId",storeId);
            return  "/shopManage/loginIndex";
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            request.setAttribute("cashier_message","请先登录收银员账号");
            request.setAttribute("storeId",storeId);
            return "/shopManage/loginIndex";
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(id);
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                request.setAttribute("cashier_message","收银员账号不正确");
                request.setAttribute("storeId",storeId);
                return "/shopManage/loginIndex";
            }
        }


        //查询所有桌号信息
        List<CashierOrderVo> cashierOrderVos = Lists.newArrayList();
        StoreTableNumber tableNumber = menuService.getStoreTableNumber(id);
        if(tableNumber != null){
            Integer count = tableNumber.getTableMaxCount();
            String exclude = tableNumber.getExcludeTableNumber();
            String [] excludes = exclude.split(",");

            for(int index = 1; index <= count; index += 1){
                Integer tableId = index;
                Boolean isExclude = false;

                for(String e : excludes){
                    if(String.valueOf(index).equals(e)){
                        isExclude = true;
                        break;
                    }
                }
                CashierOrderVo cashierOrderVo = new CashierOrderVo();
                cashierOrderVo.setTableId(tableId);
                cashierOrderVo.setExclude(isExclude);
                cashierOrderVos.add(cashierOrderVo);
            }
        }


        OrderRecode req = new OrderRecode();
        req.setUid(id);
        req.setStatus(OrderType.ORDER_WAIT.getType());
        List<OrderRecode> orderRecodes = menuService.getOrderRecode(req);
        //把最新的一条orderrecode放到cashierOrderVo list里面
        for(OrderRecode orderRecode : orderRecodes){
            for(CashierOrderVo cashierOrderVo : cashierOrderVos){
                if(!cashierOrderVo.getExclude() &&
                        cashierOrderVo.getTableId().intValue() == orderRecode.getTableId()) {
                    if (cashierOrderVo.getOrderRecode() == null
                            ||cashierOrderVo.getOrderRecode().getSummitTime()==null) {
                        orderRecode = menuService.shortlizeComment(orderRecode);
                        cashierOrderVo.setOrderRecode(orderRecode);
                    } else if (orderRecode.getSummitTime().after(cashierOrderVo.getOrderRecode().getSummitTime()) ){
                        orderRecode = menuService.shortlizeComment(orderRecode);
                        cashierOrderVo.setOrderRecode(orderRecode);
                    }
                }
            }
        }

        //计算一下当天总收入
        OrderRecode reqTotalCash = new OrderRecode();
        reqTotalCash.setUid(id);
        reqTotalCash.setStatus(OrderType.ORDER_FINISH.getType());
        Date today = menuService.getToday();
        reqTotalCash.setStartTime(today);
        List<OrderRecode> orderRecodesForSum = menuService.getOrderRecode(reqTotalCash);

        //查询所有点菜信息
        //获取分类
        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(id);
        cateReq.setStatus(CommonTypeEnum.OK.getType());
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        if(cates != null
                && cates.size() > 0){
            Map<String, List<DishInfo>>dishLists = Maps.newLinkedHashMap();
            for (DishCategoryInfo cate : cates) {
                List<DishInfo> cateDishList = Lists.newArrayList();
                dishLists.put(cate.getCateName(), cateDishList);
            }
            //取出菜单
            DishInfo dishReq = new DishInfo();
            dishReq.setUid(id);
            dishReq.setStatus(CommonTypeEnum.OK.getType());
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
            request.setAttribute("cateWithDishList", dishLists);
        }

        request.setAttribute("cashierOrderVos",cashierOrderVos);
        request.setAttribute("storeId",storeId);
        CashierStaticInfo cashierStaticInfo = menuService.getStaticInfo(cashierOrderVos, orderRecodesForSum);
        request.setAttribute("cashierStaticInfo",cashierStaticInfo);

        return "/shopManage/cashierIndex";
    }

    @RequestMapping(value = "cashierDateIndex/{storeId}", method = RequestMethod.GET)
    public String cashierDateIndex(HttpServletRequest request,
                                   @PathVariable String storeId,
                                   @RequestParam(required = false) String startTime,
                                   @RequestParam(required = false) String endTime) {

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            String mes = "您访问的店铺地址不正确，请登录葫芦微站www.huluweizhan.cn查询正确地址";
            request.setAttribute("cashier_message",mes);
            request.setAttribute("storeId",-1);
            return "/shopManage/loginIndex";
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            request.setAttribute("cashier_message",OVER_DATE_MESSAGE);
            request.setAttribute("storeId",storeId);
            return  "/shopManage/loginIndex";
        }

        //是否提交过商店信息
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(id);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            request.setAttribute("store",stores.get(0));
        }else{
            request.setAttribute("cashier_message","请先完善店铺信息");
            request.setAttribute("storeId",storeId);
            return  "/shopManage/loginIndex";
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            request.setAttribute("cashier_message","请先登录收银员账号");
            request.setAttribute("storeId",storeId);
            return "/shopManage/loginIndex";
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(id);
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                request.setAttribute("cashier_message","收银员账号不正确");
                request.setAttribute("storeId",storeId);
                return "/shopManage/loginIndex";
            }
        }


        OrderRecode req = new OrderRecode();
        req.setUid(id);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        if(StringUtils.isNotBlank(startTime)){
            try {
                Date start = sdf.parse(startTime);
                req.setStartTime(start);
                request.setAttribute("startTime",startTime);
            } catch (Exception e) {
            }
        }else{
            Date today = menuService.getToday();
            req.setStartTime(today);
            request.setAttribute("startTime",sdf.format(today));
        }
        if(StringUtils.isNotBlank(endTime)){
            try {
                Date end = sdf.parse(endTime);
                end.setTime(end.getTime() + 24*3600*1000);
                req.setEndTime(end);
                request.setAttribute("endTime",endTime);
            } catch (Exception e) {
            }
        }else{
            request.setAttribute("endTime","");
        }
        List<OrderRecode> orderRecodes = menuService.getOrderRecode(req);
        if(orderRecodes != null){
            for(OrderRecode orderRecode : orderRecodes){
                menuService.shortlizeComment(orderRecode);
            }
        }

        //计算一下当天总收入
        OrderRecode reqTotalCash = new OrderRecode();
        reqTotalCash.setUid(id);
        reqTotalCash.setStatus(OrderType.ORDER_FINISH.getType());
        Date today = menuService.getToday();
        reqTotalCash.setStartTime(today);
        List<OrderRecode> orderRecodesForSum = menuService.getOrderRecode(reqTotalCash);

        //计算一下未完成总单数
        OrderRecode reqCount = new OrderRecode();
        reqCount.setUid(id);
        reqCount.setStatus(OrderType.ORDER_WAIT.getType());
        Integer finishCount = menuService.getOrderRecodeCount(reqCount);
        request.setAttribute("finishCount",finishCount);

        //获取分类
        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(id);
        cateReq.setStatus(CommonTypeEnum.OK.getType());
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        if(cates != null
                && cates.size() > 0){
            Map<String, List<DishInfo>>dishLists = Maps.newLinkedHashMap();
            for (DishCategoryInfo cate : cates) {
                List<DishInfo> cateDishList = Lists.newArrayList();
                dishLists.put(cate.getCateName(), cateDishList);
            }
            //取出菜单
            DishInfo dishReq = new DishInfo();
            dishReq.setUid(id);
            dishReq.setStatus(CommonTypeEnum.OK.getType());
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
            request.setAttribute("cateWithDishList", dishLists);
        }

        request.setAttribute("orderRecodes",orderRecodes);
        request.setAttribute("orderRecodeSize",orderRecodes.size());
        request.setAttribute("orderRecodeSumPrice",menuService.countTotalPrice(orderRecodes));
        request.setAttribute("storeId",storeId);
        CashierStaticInfo cashierStaticInfo = menuService.getStaticInfo(null, orderRecodesForSum);
        request.setAttribute("cashierStaticInfo",cashierStaticInfo);

        return "/shopManage/cashierDateIndex";
    }

    @RequestMapping(value = "dishManageIndex/{storeId}", method = RequestMethod.GET)
    public String dishManageIndex(HttpServletRequest request,
                                  @PathVariable String storeId) {

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            String mes = "您访问的店铺地址不正确，请登录葫芦微站www.huluweizhan.cn查询正确地址";
            request.setAttribute("cashier_message",mes);
            request.setAttribute("storeId",-1);
            return "/shopManage/loginIndex";
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            request.setAttribute("cashier_message",OVER_DATE_MESSAGE);
            request.setAttribute("storeId",storeId);
            return  "/shopManage/loginIndex";
        }

        //是否提交过商店信息
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(id);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            request.setAttribute("store",stores.get(0));
        }else{
            request.setAttribute("cashier_message","请先完善店铺信息");
            request.setAttribute("storeId",storeId);
            return  "/shopManage/loginIndex";
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            request.setAttribute("cashier_message","请先登录收银员账号");
            request.setAttribute("storeId",storeId);
            return "/shopManage/loginIndex";
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(id);
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                request.setAttribute("cashier_message","收银员账号不正确");
                request.setAttribute("storeId",storeId);
                return "/shopManage/loginIndex";
            }
        }


        //计算一下当天总收入
        OrderRecode reqTotalCash = new OrderRecode();
        reqTotalCash.setUid(id);
        Date today = menuService.getToday();
        reqTotalCash.setStartTime(today);
        List<OrderRecode> orderRecodesForSum = menuService.getOrderRecode(reqTotalCash);

        //计算一下未完成总单数
        OrderRecode reqCount = new OrderRecode();
        reqCount.setUid(id);
        reqCount.setStatus(OrderType.ORDER_WAIT.getType());
        Integer finishCount = menuService.getOrderRecodeCount(reqCount);
        request.setAttribute("finishCount",finishCount);

        //获取分类
        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(id);
        cateReq.setStatus(CommonTypeEnum.OK.getType());
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        Map<String, List<DishInfo>>dishLists = Maps.newLinkedHashMap();
        if(cates != null
                && cates.size() > 0){
            for (DishCategoryInfo cate : cates) {
                List<DishInfo> cateDishList = Lists.newArrayList();
                dishLists.put(cate.getCateName(), cateDishList);
            }
            //取出菜单
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
            request.setAttribute("cateWithDishList", dishLists);
        }


        Map<Long, ManageDishInfo> manageDishInfoMap = menuService.getManageDishInfo(dishLists);
        menuService.fillConsumeCount(manageDishInfoMap,orderRecodesForSum);
        request.setAttribute("dishList",manageDishInfoMap.values());
        request.setAttribute("totalDishSize",manageDishInfoMap.size());

        //计算暂停的菜品数目
        Integer parseCount = 0;
        for(Long dishId : manageDishInfoMap.keySet()){
            if(manageDishInfoMap.get(dishId).getStatus().equals(CommonTypeEnum.PARSED.getType())){
                parseCount += 1;
            }
        }
        request.setAttribute("parsedDishSize",parseCount);
        request.setAttribute("storeId",storeId);
        CashierStaticInfo cashierStaticInfo = menuService.getStaticInfo(null, orderRecodesForSum);
        request.setAttribute("cashierStaticInfo",cashierStaticInfo);

        return "/shopManage/dishManageIndex";
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "parseTable/{storeId}", method = RequestMethod.POST)
    public Object orderPost(HttpServletRequest request,
                            HttpServletResponse response,
                            @PathVariable String storeId,
                            @RequestParam String tableId){

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)) {
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(tableId)
                && org.apache.commons.lang.StringUtils.isNumeric(tableId)) {
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(Long.parseLong(storeId));
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(Long.parseLong(storeId));
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
            }
        }

        //是否有正在进行中的菜单
        OrderRecode req = new OrderRecode();
        req.setUid(Long.parseLong(storeId));
        req.setTableId(Long.parseLong(tableId));
        req.setStatus(OrderType.ORDER_WAIT.getType());
        OrderRecode recode = null;
        List<OrderRecode> recodes = menuService.getOrderRecode(req);
        if(recodes != null
                && recodes.size() > 0){
            recode = recodes.get(0);
        }
        //存在正在进行的菜单，打死也不能删除桌子
        if(recode != null){
            String msg = "该桌还有未付款的点单正在进行。需要先请点击“付款”，结束正在进行的点单";
            return Response.fail(Response.RespType.OPERATE_FAILED, msg);
        }

        //判断是否桌号有效
        Integer res = -1;
        StoreTableNumber number = menuService.getStoreTableNumber(Long.parseLong(storeId));
        if(number != null && number.getTableMaxCount() >= Integer.parseInt(tableId)){
            res = menuService.excludeTable(number,Integer.parseInt(tableId));
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(res > 0){
            return Response.ok();
        }else {
            return Response.fail(Response.RespType.OPERATE_FAILED);
        }
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "resumeTable/{storeId}", method = RequestMethod.POST)
    public Object resumeTable(HttpServletRequest request,
                              HttpServletResponse response,
                              @PathVariable String storeId,
                              @RequestParam String tableId){

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)) {
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(tableId)
                && org.apache.commons.lang.StringUtils.isNumeric(tableId)) {
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(Long.parseLong(storeId));
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(Long.parseLong(storeId));
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
            }
        }

        //判断是否桌号有效
        Integer res = -1;
        StoreTableNumber number = menuService.getStoreTableNumber(Long.parseLong(storeId));
        if(number != null && number.getTableMaxCount() >= Integer.parseInt(tableId)){
            res = menuService.resumeTable(number,Integer.parseInt(tableId));
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(res > 0){
            return Response.ok();
        }else {
            return Response.fail(Response.RespType.OPERATE_FAILED);
        }
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "loginPost/{storeId}", method = RequestMethod.POST)
    public Object orderPost(HttpServletRequest request,
                            HttpServletResponse response,
                            @PathVariable String storeId,
                            @RequestParam String uname,
                            @RequestParam String passwd){

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)) {
        }else{
            String mes = "您访问的店铺地址不正确，请登录葫芦微站www.huluweizhan.cn查询正确地址";
            return Response.fail(Response.RespType.PARAM_WRONG, mes);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(uname)
                && org.apache.commons.lang.StringUtils.isNotBlank(passwd)
                && uname.length() > 30
                && passwd.length() > 40) {
            return Response.fail(Response.RespType.PARAM_WRONG, "用户名或密码错误");
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG, OVER_DATE_MESSAGE);
        }

        //查询收银信息
        CashierInfo req = new CashierInfo();
        req.setUid(id);
        req.setCashierName(uname);
        req.setPassword(passwd);
        req.setStatus(CommonTypeEnum.OK.getType());
        List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
        if(cashieres != null
                && cashieres.size() == 1){
            CashierInfo cashier = cashieres.get(0);
            if(cashier != null && cashier.getUid() > 0){
                menuService.setCashierCookies(cashier,response);
                return Response.ok();
            }
        }
        CookiesUtil.deleteCookie("cashierId",request,response);
        return Response.fail(Response.RespType.PARAM_WRONG, "店长尚未添加您的收银员账号");

    }

    @ResponseBody
    @RequestMapping(value = "logoutPost", method = RequestMethod.POST)
    public Object logoutPost(HttpServletRequest request,
                             HttpServletResponse response){

        CookiesUtil.deleteCookie("cashierId",request,response);
        return Response.ok("操作成功");

    }

    @ResponseBody
    @RequestMapping(value = "getOrderDetail/{storeId}", method = RequestMethod.POST)
    public Object getOrderDetail(@PathVariable String storeId,
                                 @RequestParam String tableId,
                                 @RequestParam String orderId) {


        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(tableId)
                && org.apache.commons.lang.StringUtils.isNumeric(tableId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(orderId)
                && org.apache.commons.lang.StringUtils.isNumeric(orderId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        Long id = Long.parseLong(storeId);

        OrderRecode req = new OrderRecode();
        OrderRecode recode = null;
        CashierOrderDetailVo cashierOrderDetailVo = new CashierOrderDetailVo();
        List<OrderDishInfo> orderDishInfoList = Lists.newArrayList();
        cashierOrderDetailVo.setOrderDishInfoList(orderDishInfoList);
        req.setUid(id);
        req.setTableId(Long.parseLong(tableId));
        req.setId(Long.parseLong(orderId));
        List<OrderRecode> orderRecodes = menuService.getOrderRecode(req);
        if(orderRecodes != null
                && orderRecodes.size() > 0){
            recode = orderRecodes.get(0);
        }
        if(recode != null){
            Map<Long, Integer> oldOrderMap = menuService.convertOrderStrtoMap(recode.getDishListStr());
            List<Long>dishIdList = Lists.newArrayList();
            dishIdList.addAll(oldOrderMap.keySet());
            List<DishInfo> dishes = menuService.getDishInfoByList(dishIdList);
            if(dishes != null){
                for(DishInfo dishInfo : dishes){
                    Long dishId = dishInfo.getDishId();
                    OrderDishInfo orderDishInfo = new OrderDishInfo();
                    orderDishInfo.setDishId(dishId);
                    orderDishInfo.setDishName(dishInfo.getDishName());
                    orderDishInfo.setDisount(dishInfo.getDishPriceDiscount());
                    orderDishInfo.setCount(oldOrderMap.get(dishId));
                    orderDishInfo.setSinglePrice(dishInfo.getDishPriceFinal());
                    orderDishInfo.setOriginPrice(dishInfo.getDishPriceOrigin());
                    Float dishPrice = menuService.calcPrice(dishInfo.getDishPriceFinal(),orderDishInfo.getCount());
                    orderDishInfo.setDishPrice(dishPrice);
                    orderDishInfoList.add(orderDishInfo);
                }
            }

            cashierOrderDetailVo.setTotalPrice(recode.getTotalPrice());
            String customComment = recode.getCustomComment();
            String manageComment = recode.getManageComment();
            cashierOrderDetailVo.setCustomComment(customComment);
            cashierOrderDetailVo.setManageComment(manageComment);
            cashierOrderDetailVo.setOrderCount(orderDishInfoList.size());
            cashierOrderDetailVo.setStatus(recode.getStatus());
        }

        return Response.ok(cashierOrderDetailVo);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "payPost", method = RequestMethod.POST)
    public Object payPost(HttpServletRequest request,
                          @RequestParam String storeId,
                          @RequestParam String tableId,
                          @RequestParam String orderId,
                          @RequestParam (required = false)String comment,
                          @RequestParam (required = false)String dishStr,
                          @RequestParam (required = false)Float price) {


        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(tableId)
                && org.apache.commons.lang.StringUtils.isNumeric(tableId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(orderId)
                && org.apache.commons.lang.StringUtils.isNumeric(orderId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG,OVER_DATE_MESSAGE);
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(Long.parseLong(storeId));
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
            }
        }


        OrderRecode req = new OrderRecode();
        OrderRecode recode = null;
        req.setUid(id);
        req.setTableId(Long.parseLong(tableId));
        req.setId(Long.parseLong(orderId));
        req.setStatus(OrderType.ORDER_WAIT.getType());
        List<OrderRecode> orderRecodes = menuService.getOrderRecode(req);
        if(orderRecodes != null
                && orderRecodes.size() > 0){
            recode = orderRecodes.get(0);
        }
        if(recode != null){
            OrderRecode updateReq = new OrderRecode();
            updateReq.setId(recode.getId());
            updateReq.setStatus(OrderType.ORDER_FINISH.getType());
            updateReq.setFinishTime(new Date());
            if(StringUtils.isNotBlank(comment)){
                updateReq.setManageComment(comment);
            }
            if(StringUtils.isNotBlank(dishStr)
                    && price != null
                    && price > 0){
                updateReq.setDishListStr(dishStr);
                updateReq.setTotalPrice(price);
            }
            Integer res = menuService.updateOrderRecode(updateReq);
            if(res > 0){
                return Response.ok();
            }else{
                return Response.fail(Response.RespType.NETWORK_ERROR);
            }
        }

        return Response.fail(Response.RespType.PARAM_WRONG);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "cancelPost", method = RequestMethod.POST)
    public Object cancelPost(HttpServletRequest request,
                             @RequestParam String storeId,
                             @RequestParam String tableId,
                             @RequestParam String orderId) {


        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(tableId)
                && org.apache.commons.lang.StringUtils.isNumeric(tableId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(orderId)
                && org.apache.commons.lang.StringUtils.isNumeric(orderId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG,OVER_DATE_MESSAGE);
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(Long.parseLong(storeId));
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
            }
        }


        OrderRecode req = new OrderRecode();
        OrderRecode recode = null;
        req.setUid(id);
        req.setTableId(Long.parseLong(tableId));
        req.setId(Long.parseLong(orderId));
        req.setStatus(OrderType.ORDER_WAIT.getType());
        List<OrderRecode> orderRecodes = menuService.getOrderRecode(req);
        if(orderRecodes != null
                && orderRecodes.size() > 0){
            recode = orderRecodes.get(0);
        }
        if(recode != null){
            OrderRecode updateReq = new OrderRecode();
            updateReq.setId(recode.getId());
            updateReq.setStatus(OrderType.ORDER_DELETE.getType());
            Integer res = menuService.updateOrderRecode(updateReq);
            if(res > 0){
                return Response.ok();
            }else{
                return Response.fail(Response.RespType.NETWORK_ERROR);
            }
        }
        return Response.fail(Response.RespType.PARAM_WRONG);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "postComment", method = RequestMethod.POST)
    public Object postComment(HttpServletRequest request,
                              @RequestParam String storeId,
                              @RequestParam String tableId,
                              @RequestParam String orderId,
                              @RequestParam String comment){

        if(org.apache.commons.lang.StringUtils.isBlank(comment)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(tableId)
                && org.apache.commons.lang.StringUtils.isNumeric(tableId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(orderId)
                && org.apache.commons.lang.StringUtils.isNumeric(orderId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG,OVER_DATE_MESSAGE);
        }


        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(Long.parseLong(storeId));
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
            }
        }


        OrderRecode req = new OrderRecode();
        OrderRecode recode = null;
        req.setUid(id);
        req.setTableId(Long.parseLong(tableId));
        req.setId(Long.parseLong(orderId));
        List<OrderRecode> orderRecodes = menuService.getOrderRecode(req);
        if(orderRecodes != null
                && orderRecodes.size() > 0){
            recode = orderRecodes.get(0);
        }
        if(recode != null){
            OrderRecode updateReq = new OrderRecode();
            updateReq.setId(recode.getId());
            updateReq.setManageComment(comment);
            Integer res = menuService.updateOrderRecode(updateReq);
            if(res > 0){
                return Response.ok();
            }else{
                return Response.fail(Response.RespType.NETWORK_ERROR, "保存评论记录失败");
            }
        }

        return Response.fail(Response.RespType.PARAM_WRONG);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "postDishStatus", method = RequestMethod.POST)
    public Object postDishStatus(HttpServletRequest request,
                                 @RequestParam String storeId,
                                 @RequestParam String dishId,
                                 @RequestParam String status){
        if(org.apache.commons.lang.StringUtils.isNotBlank(storeId)
                && org.apache.commons.lang.StringUtils.isNumeric(storeId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(dishId)
                && org.apache.commons.lang.StringUtils.isNumeric(dishId)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(status)
                && org.apache.commons.lang.StringUtils.isNumeric(status)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        Long id = Long.parseLong(storeId);

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(id);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.PARAM_WRONG,OVER_DATE_MESSAGE);
        }

        //验证是否有登录权限
        if(!menuService.checkCkForCahsier(request,CASHIER_COOK_NAME)){
            return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
        }else{
            CashierInfo req = new CashierInfo();
            req.setUid(Long.parseLong(storeId));
            Long uid = Long.parseLong((String)request.getAttribute("cashierId"));
            req.setCashierid(uid);
            req.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
            if(cashieres != null
                    && cashieres.size() == 1){
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG, "无权限");
            }
        }

        DishInfo req = new DishInfo();
        Integer res = -1;
        req.setDishId(Long.parseLong(dishId));
        if(status.equals("1")){
            req.setStatus(CommonTypeEnum.OK.getType());
            res = menuService.updateDishInfo(req);
        }else if(status.equals("2")){
            req.setStatus(CommonTypeEnum.PARSED.getType());
            res = menuService.updateDishInfo(req);
        }

        if(res > 0){
            return Response.ok();
        }else{
            return Response.fail(Response.RespType.SERVER_ERROR);
        }
    }

}

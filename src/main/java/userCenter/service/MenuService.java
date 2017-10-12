package userCenter.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import userCenter.Utils.CookiesUtil;
import userCenter.controllers.userCenterController;
import userCenter.interceptor.LoginInterceptor;
import userCenter.mapper.*;
import userCenter.model.CashierOrderVo;
import userCenter.model.CashierStaticInfo;
import userCenter.model.ManageDishInfo;
import userCenter.model.OrderDishInfo;
import userCenter.model.batis.*;
import userCenter.model.enumModel.CommonTypeEnum;
import userCenter.model.enumModel.OrderType;
import userCenter.model.enumModel.SignTypeEnum;
import userCenter.model.enumModel.UserType;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Hayden on 2017/3/22.
 */
@Service
public class MenuService {

    @Autowired
    UserStoreInfoMapper userStoreInfoMapper;

    @Autowired
    SignMapMapper signMapMapper;

    @Autowired
    DishInfoMapper dishInfoMapper;

    @Autowired
    OrderRecodeMapper orderRecodeMapper;

    @Autowired
    CosService cosService;

    @Autowired
    DishCategoryInfoMapper dishCategoryInfoMapper;

    @Autowired
    CashierInfoMapper cashierInfoMapper;

    @Autowired
    StoreTableNumberMapper storeTableNumberMapper;

    public static String DEFAULT_DATE = "2017-5-20";

    public List<UserStoreInfo> getStoreInfo(UserStoreInfo storeReq) {

        List<UserStoreInfo> stores = userStoreInfoMapper.selectStoreInfoByAccountName(storeReq);
        return stores;

    }

    public Integer getSignMap(SignTypeEnum signTypeEnum, Long storeId) {
        if(signTypeEnum == null || storeId == null || storeId <= 0){
            return -1;
        }
        Integer value = -1;
        SignMapKey signMapKey = new SignMap();
        signMapKey.setSignId(signTypeEnum.getType());
        signMapKey.setStoreId(storeId);
        SignMap signMap = signMapMapper.selectByPrimaryKey(signMapKey);
        if(signMap != null){
            value = signMap.getSignValue();
        }
        return value;

    }

    public Integer updateStoreInfo(UserStoreInfo storeReq) {

        Integer res = userStoreInfoMapper.updateStoreInfoByUid(storeReq);
        return res;

    }

    public Integer insertStoreInfo(UserStoreInfo storeReq) {
        Integer res = userStoreInfoMapper.insertStoreInfo(storeReq);
        return res;
    }


    public List<DishInfo> getDishInfo(DishInfo dishReq) {

        List<DishInfo> dishes = dishInfoMapper.selectDishInfoByReq(dishReq);
        return dishes;

    }

    public List<DishInfo> getDishInfoByList(List<Long> dishIdList) {

        List<DishInfo> dishes = dishInfoMapper.selectDishInfoByButch(dishIdList);
        return dishes;

    }

    public List<OrderRecode> getOrderRecode(OrderRecode req) {
        List<OrderRecode> recodes = Lists.newArrayList();
        if(req != null){
            recodes = orderRecodeMapper.selectOrderRecodeByReq(req);
        }
        return recodes;

    }

    public Integer getOrderRecodeCount(OrderRecode req) {
        Integer res = 0;
        if(req != null){
            res = orderRecodeMapper.countOrderRecodeByReq(req);
        }
        return res;

    }

    public Integer updateOrderRecode(OrderRecode req) {

        Integer count = orderRecodeMapper.updateOrderRecode(req);
        //添加信号量
        SignMapKey signMapKey = new SignMapKey();
        signMapKey.setSignId(SignTypeEnum.NEW_ORDER_AND_COMMENT.getType());
        signMapKey.setStoreId(req.getUid());
        SignMap signMap = signMapMapper.selectByPrimaryKey(signMapKey);
        if(signMap != null){
            Integer value = signMap.getSignValue();
            value = (value + 1)%10;
            signMap.setSignValue(value);
            signMapMapper.updateByPrimaryKey(signMap);
        }else{
            signMap = new SignMap();
            signMap.setSignId(SignTypeEnum.NEW_ORDER_AND_COMMENT.getType());
            signMap.setStoreId(req.getUid());
            signMap.setSignValue(1);
            signMapMapper.insertSelective(signMap);
        }
        return count;

    }

    public Integer insertOrderRecode(OrderRecode req) {

        Integer count = orderRecodeMapper.insertOrderRecode(req);
        //添加信号量
        SignMapKey signMapKey = new SignMapKey();
        signMapKey.setSignId(SignTypeEnum.NEW_ORDER_AND_COMMENT.getType());
        signMapKey.setStoreId(req.getUid());
        SignMap signMap = signMapMapper.selectByPrimaryKey(signMapKey);
        if(signMap != null){
            Integer value = signMap.getSignValue();
            value = (value + 1)%10;
            signMap.setSignValue(value);
            signMapMapper.updateByPrimaryKey(signMap);
        }else{
            signMap = new SignMap();
            signMap.setSignId(SignTypeEnum.NEW_ORDER_AND_COMMENT.getType());
            signMap.setStoreId(req.getUid());
            signMap.setSignValue(1);
            signMapMapper.insertSelective(signMap);
        }
        return count;

    }

    public Integer insertDishInfo(DishInfo dishReq) {

        Integer count = dishInfoMapper.insertDishInfo(dishReq);
        return count;

    }

    public Integer updateIndexList(List<DishInfo> updateIndexList) {
        Integer count = dishInfoMapper.updateIndexList(updateIndexList);
        return count;
    }


    public Integer updateDishInfo(DishInfo dishReq) {

        Integer count = dishInfoMapper.updateDishInfo(dishReq);
        return count;

    }

    public Integer swapDishIndex(DishInfo d1, DishInfo d2) {
        if(d1 == null || d2 == null){
            return -1;
        }
        int index = d1.getDishIndex();
        d1.setDishIndex(d2.getDishIndex());
        d2.setDishIndex(index);
        List<DishInfo> updateDishInfo = Lists.newArrayList();
        updateDishInfo.add(d1);
        updateDishInfo.add(d2);
        return updateIndexList(updateDishInfo);
    }

    public Map<Long,Integer> convertOrderStrtoMap(String dishListStr) {
        Map<Long,Integer> orderMap = Maps.newLinkedHashMap();
        if(StringUtils.isBlank(dishListStr)){
            return orderMap;
        }
        String [] orderpairs = dishListStr.split(";");
        if(orderpairs != null){
            for(String p : orderpairs){
                String [] orderpair = p.split(":");
                Long dishId = Long.parseLong(orderpair[0]);
                Integer count = Integer.parseInt(orderpair[1]);
                orderMap.put(dishId,count);
            }
        }
        return orderMap;
    }

    public String convertMaptoOrderStr(Map<Long, Integer> orderMap) {
        if(orderMap == null || orderMap.size() == 0){
            return "";
        }
        StringBuilder res = new StringBuilder();
        for(Long dishId : orderMap.keySet()){
            Integer count = orderMap.get(dishId);
            res.append(dishId).append(":")
                    .append(count).append(";");
        }
        return res.toString();
    }

    public Float countPrice(List<OrderDishInfo> orderDishInfos) {
        if(orderDishInfos == null || orderDishInfos.size() == 0){
            return 0f;
        }
        Float totalPrice = 0f;
        for(OrderDishInfo dish : orderDishInfos){
            Float price = dish.getDishPrice();
            if(price != null && price > 0){
                totalPrice += price;
            }
        }
        BigDecimal b  =   new BigDecimal(totalPrice);
        totalPrice = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        return totalPrice;
    }

    public Float calcPrice(Float price, Integer count) {
        Float res = 0f;
        if(count > 0 && price > 0){
            res = count * price;
            BigDecimal b  =   new BigDecimal(res);
            res = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return res;
    }

    public Long generateOrderId() {
        Integer suffix = UtilService.random.nextInt(1000);
        Long time = System.currentTimeMillis();
        String idstr = time.toString() + suffix.toString();
        Long res = Long.parseLong(idstr);
        return res;
    }

    public void setOrderIdCookies(Long orderIdCookies, HttpServletResponse response) {
        if(orderIdCookies == null){
            return;
        }
        String orderIdStr = orderIdCookies.longValue() + "";
        orderIdStr  = userCenterController.encoder.encode(orderIdStr.getBytes());
        CookiesUtil.setCookie("orderinfo",
                orderIdStr,
                CookiesUtil.ORDER_EXPIRE_TIME,
                CookiesUtil.THIS_DOMAIN,
                CookiesUtil.THIS_PATH,
                response);
    }

    public List<DishCategoryInfo> getCateInfoByReq(DishCategoryInfo req) {
        List<DishCategoryInfo> cates = Lists.newArrayList();
        if(req != null){
            cates = dishCategoryInfoMapper.selectByReq(req);
        }
        return cates;
    }

    public Integer insertCateInfo(DishCategoryInfo cateInfo) {
        Integer res = dishCategoryInfoMapper.insertSelective(cateInfo);
        return res;
    }

    public Integer updateCateInfo(DishCategoryInfo cateInfo) {
        Integer res = dishCategoryInfoMapper.updateByPrimaryKeySelective(cateInfo);
        return res;
    }

    public Integer updateCateList(List<DishCategoryInfo> updateCateList) {
        Integer count = dishCategoryInfoMapper.updateIndexList(updateCateList);
        return count;
    }

    public Integer swapCateIndex(DishCategoryInfo cate1, DishCategoryInfo cate2) {
        if(cate1 == null || cate2 == null){
            return -1;
        }
        int index = cate1.getCateIndex();
        cate1.setCateIndex(cate2.getCateIndex());
        cate2.setCateIndex(index);
        List<DishCategoryInfo> updateCateInfo = Lists.newArrayList();
        updateCateInfo.add(cate1);
        updateCateInfo.add(cate2);
        return updateCateList(updateCateInfo);
    }

    public List<DishInfo> refreshDishIndex(DishInfo dishInfo) {
        List<DishInfo> res = Lists.newArrayList();
        if(dishInfo.getCateId() == null || dishInfo.getCateId() < 0
                || dishInfo.getUid() == null || dishInfo.getUid() < 0){
            return res;
        }
        DishInfo req = new DishInfo();
        req.setUid(dishInfo.getUid());
        req.setCateId(dishInfo.getCateId());
        req.setStatus(CommonTypeEnum.OK.getType());
        res = dishInfoMapper.selectDishInfoByReq(req);
        int index = 1;
        for(DishInfo dish : res){
            dish.setDishIndex(index);
            index += 1;
        }
        dishInfoMapper.updateIndexList(res);
        return res;
    }

    public List<CashierInfo> selectCashierByReq(CashierInfo req) {
        List<CashierInfo> res = Lists.newArrayList();
        if(req != null){
            res =  cashierInfoMapper.selectByReq(req);
        }
        return res;
    }

    public int insertSelective(CashierInfo cashierInfo) {
        Integer res = -1;
        if(cashierInfo != null){
            res = cashierInfoMapper.insertSelective(cashierInfo);
        }
        return res;
    }

    public Integer updateCashInfo(CashierInfo req) {
        Integer res = -1;
        if (req != null) {
            res = cashierInfoMapper.updateByPrimaryKeySelective(req);
        }
        return res;
    }

    public Integer getTableMaxCount(Long uid) {
        Integer res = -1;
        if(uid != null && uid > 0){
            StoreTableNumber number = storeTableNumberMapper.selectByPrimaryKey(uid);
            if (number != null) {
                res = number.getTableMaxCount();
            }
        }
        return res;
    }

    public StoreTableNumber getStoreTableNumber(Long uid) {
        StoreTableNumber res = null;
        if(uid != null && uid > 0){
            StoreTableNumber number = storeTableNumberMapper.selectByPrimaryKey(uid);
            if(number != null){
                res = number;
            }
        }
        return res;
    }

    public Integer updateByPrimaryKeySelective(StoreTableNumber tnum) {
        Integer res = -1;
        if(tnum != null && tnum.getUid() > 0){
            StoreTableNumber old = storeTableNumberMapper.selectByPrimaryKey(tnum.getUid());
            if(old != null){
                res = storeTableNumberMapper.updateByPrimaryKeySelective(tnum);
            }else{
                res =  storeTableNumberMapper.insertSelective(tnum);
            }
        }
        return res;
    }

    public Boolean tableNumberAvailable(Long uid, Long tableId) {
        Boolean res = false;
        if(tableId == null || tableId <= 0){
            return res;
        }
        if(uid != null && uid > 0){
            StoreTableNumber tableNum = storeTableNumberMapper.selectByPrimaryKey(uid);
            if(tableNum != null){
                Integer max = tableNum.getTableMaxCount();
                if(tableId > max){
                    return res;
                }
                String excludeStr = tableNum.getExcludeTableNumber();
                if(StringUtils.isNotBlank(excludeStr)){
                    String [] tidStr = excludeStr.split(",");
                    if(tidStr != null){
                        for(String s:tidStr){
                            if(s.equals(String.valueOf(tableId))){
                                return false;
                            }
                        }
                    }
                    res = true;
                }
            }
        }
        return res;
    }

    public Date getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public CashierStaticInfo getStaticInfo(List<CashierOrderVo> cashierOrderVos, List<OrderRecode> orderRecodes) {

        CashierStaticInfo  cashierStaticInfo = new CashierStaticInfo();
        if(cashierOrderVos != null && cashierOrderVos.size() > 0){
            Integer total = cashierOrderVos.size();
            Integer unfinish = 0;
            Integer finish = 0;
            Integer excludeCount = 0;
            for(CashierOrderVo cashierOrderVo : cashierOrderVos){
                if(cashierOrderVo.getExclude()){
                    excludeCount += 1;
                }else if(cashierOrderVo.getOrderRecode() != null
                        && cashierOrderVo.getOrderRecode().getStatus() != null
                        && cashierOrderVo.getOrderRecode().getStatus().equals(OrderType.ORDER_WAIT.getType())){
                    unfinish += 1;
                }
            }
            finish = total - excludeCount - unfinish;
            cashierStaticInfo.setTotalTable(total);
            cashierStaticInfo.setFinishTable(finish);
            cashierStaticInfo.setUnfinishTable(unfinish);
            cashierStaticInfo.setParsedTable(excludeCount);
        }
        if(orderRecodes != null && orderRecodes.size() > 0){
            Float totoaMoney = 0f;
            Integer totalOrderCount = 0;
            for(OrderRecode orderRecode : orderRecodes){
                if(orderRecode.getStatus().equals(OrderType.ORDER_FINISH.getType())){
                    totoaMoney += orderRecode.getTotalPrice();
                    totalOrderCount += 1;
                }

            }
            BigDecimal bg = new BigDecimal(totoaMoney);
            totoaMoney = bg.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            cashierStaticInfo.setTotalMoney(totoaMoney);
            cashierStaticInfo.setOrderCount(totalOrderCount);
        }
        return cashierStaticInfo;
    }

    public Float getPriceFromOrderMap(Map<Long, Integer> oldOrderMap) {
        if(oldOrderMap == null || oldOrderMap.size() <= 0){
            return 0f;
        }
        Float res = 0f;
        List<Long>dishIdList = Lists.newArrayList();
        dishIdList.addAll(oldOrderMap.keySet());
        List<DishInfo> dishes = getDishInfoByList(dishIdList);
        if(dishes != null){
            for(DishInfo dishInfo : dishes){
                Long dishId = dishInfo.getDishId();
                Float dishPrice = calcPrice(dishInfo.getDishPriceFinal(), oldOrderMap.get(dishId));
                res += dishPrice;
            }
        }
        BigDecimal b  =   new BigDecimal(res);
        res = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return res;
    }

    public Integer excludeTable(StoreTableNumber number, Integer excludeNum) {
        Integer res = -1;
        if(number != null && number.getUid() > 0 && excludeNum > 0){
            String excStr = number.getExcludeTableNumber();
            if(StringUtils.isNotBlank(excStr)){
                String [] excStrs = excStr.split(",");
                if(excStrs != null){
                    for(String exc : excStrs){
                        if(String.valueOf(excludeNum).equals(exc)){
                            return 1;
                        }
                    }
                }
                excStr = excStr + "," + String.valueOf(excludeNum);
                number.setExcludeTableNumber(String.valueOf(excStr));
            }else{
                number.setExcludeTableNumber(String.valueOf(excludeNum));
            }
            res = storeTableNumberMapper.updateByPrimaryKeySelective(number);
        }
        return res;
    }

    public Integer resumeTable(StoreTableNumber number, Integer excludeNum) {
        Integer res = -1;
        if(number != null && number.getUid() > 0 && excludeNum > 0){
            String excStr = number.getExcludeTableNumber();
            if(StringUtils.isNotBlank(excStr)){
                String [] excStrs = excStr.split(",");
                List<String> newExc = Lists.newArrayList();
                if(excStrs != null){
                    for(String exc : excStrs){
                        if(String.valueOf(excludeNum).equals(exc)){
                            continue;
                        }
                        newExc.add(exc);
                    }
                }else {
                    return 1;
                }
                if(newExc.size() == excStrs.length){
                    return 1;
                }
                excStr = String.join(",",newExc);
                number.setExcludeTableNumber(String.valueOf(excStr));
            }else{
                return 1;
            }
            res = storeTableNumberMapper.updateByPrimaryKeySelective(number);
        }
        return res;
    }

    public void setCashierCookies(CashierInfo cashier, HttpServletResponse response) {
        if(cashier == null || cashier.getUid() < 0){
            return;
        }
        String cashName = cashier.getCashierName();
        Long cashId = cashier.getCashierid();
        String encodeStr = cashName + ":" + cashId;
        encodeStr  = userCenterController.encoder.encode(encodeStr.getBytes());
        CookiesUtil.setCookie("cashierId",
                encodeStr,
                CookiesUtil.CASHIER_EXPIRE_TIME,
                CookiesUtil.THIS_DOMAIN,
                CookiesUtil.THIS_PATH,
                response);
    }


    public boolean checkCkForCahsier(HttpServletRequest request, String cashierCookName) {
        if(StringUtils.isBlank(cashierCookName)){
            return false;
        }
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for (Cookie cookie : cookies) {
                try {
                    if (cookie.getName().equals(cashierCookName)) {
                        String encodeStr = cookie.getValue();
                        encodeStr = LoginInterceptor.decodeBase64(encodeStr);
                        String[] strs = encodeStr.split(":");
                        if (strs.length == 2) {
                            String cashierName = strs[0];
                            String cashierId = strs[1];
                            if (StringUtils.isNumeric(cashierId)) {
                                request.setAttribute("cashierName", cashierName);
                                request.setAttribute("cashierId", cashierId);
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {

                }
            }
        }
        return false;
    }

    public OrderRecode shortlizeComment(OrderRecode orderRecode) {
        if(orderRecode != null && orderRecode.getCustomComment() != null){
            String comment = orderRecode.getCustomComment();
            if(comment.length() > 8){
                comment = comment.substring(0,8);
                comment += "…";
                orderRecode.setCustomComment(comment);
            }
        }
        if(orderRecode != null && orderRecode.getManageComment() != null){
            String comment = orderRecode.getManageComment();
            if(comment.length() > 8){
                comment = comment.substring(0,8);
                comment += "…";
                orderRecode.setManageComment(comment);
            }
        }
        return orderRecode;
    }

    public Float countTotalPrice(List<OrderRecode> orderRecodes) {
        Float price = 0f;
        if(orderRecodes != null){
            for(OrderRecode or : orderRecodes){
                if(or.getStatus().equals(OrderType.ORDER_FINISH.getType())){
                    price += or.getTotalPrice();
                }
            }
            BigDecimal b  =   new BigDecimal(price);
            price = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        }
        return price;
    }

    public Map<Long, ManageDishInfo> getManageDishInfo(Map<String, List<DishInfo>> dishLists) {
        Map<Long, ManageDishInfo> res = Maps.newLinkedHashMap();
        if(dishLists != null){
            for(String cateName : dishLists.keySet()){
                List<DishInfo> list = dishLists.get(cateName);
                for(DishInfo dish : list){
                    ManageDishInfo mdi = new ManageDishInfo();
                    org.springframework.beans.BeanUtils.copyProperties(dish,mdi);
                    mdi.setCateName(cateName);
                    mdi.setConsumeCount(0);
                    res.put(mdi.getDishId(),mdi);
                }
            }
        }
        return res;
    }

    public void fillConsumeCount(Map<Long, ManageDishInfo> manageDishInfoMap, List<OrderRecode> orderRecodes) {
        if(manageDishInfoMap == null || orderRecodes == null){
            return;
        }
        for(OrderRecode orderRecode : orderRecodes){
            String dishStr = orderRecode.getDishListStr();
            Map<Long,Integer> dishCountMap = convertOrderStrtoMap(dishStr);
            for(Long dishId : dishCountMap.keySet()){
                if(manageDishInfoMap.containsKey(dishId)){
                    if(manageDishInfoMap.get(dishId) != null
                            && manageDishInfoMap.get(dishId).getConsumeCount() != null){
                        int count = manageDishInfoMap.get(dishId).getConsumeCount();
                        count += dishCountMap.get(dishId);
                        manageDishInfoMap.get(dishId).setConsumeCount(count);
                    }
                }
            }
        }
    }

    public boolean isPaidUser(User user) {
        if(user == null || !UserType.isPaid(user.getUserType())){
            return false;
        }else{
            Long expireTime = user.getExpireTime();
            if(expireTime != null && expireTime > 0 && expireTime > System.currentTimeMillis()){
                return true;
            }
            return false;
        }
    }


//    public void setStoreNameInModle(UserStoreInfo userStoreInfo, HttpServletRequest request) {
//        if(userStoreInfo == null
//                || StringUtils.isBlank(userStoreInfo.getStoreName())
//                || request == null){
//            return;
//        }
//        String name = userStoreInfo.getStoreName();
//        int size = name.length();
//        List<String> nameLists = Lists.newArrayList();
//        if(size < 5){
//            nameLists.add(name);
//        }else if(size > 5 && size <=10){
//            int first = (size + 1)/2;
//            int rest = size - first;
//            nameLists.add(name.substring(0,first));
//            nameLists.add(name.substring(first,rest));
//        }else if(size > 10){
//            for(int start=0;start < size;start += 5){
//                nameLists.add(name.substring(start,start+5));
//            }
//        }
//
//        request.setAttribute("nameLists",nameLists);
//    }
}

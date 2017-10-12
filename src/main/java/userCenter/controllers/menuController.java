package userCenter.controllers;


import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import userCenter.Utils.Response;
import userCenter.mapper.UserMapper;
import userCenter.model.batis.*;
import userCenter.model.enumModel.CommonTypeEnum;
import userCenter.model.enumModel.UserType;
import userCenter.service.MenuService;
import userCenter.service.UtilService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("/menu")
public class menuController {

    @Autowired
    MenuService menuService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    UtilService utilService;

    @Value("#{configProperties['host_domain']}")
    public String host_domain;

    public static String OVER_DATE_MESSAGE = "请在 个人中心 升级您的用户级别";

    @RequestMapping(value = "menuIndex", method = RequestMethod.GET)
    public String menuIndex(HttpServletRequest httpRequest){
        String IframeSrc= "/customMenu/list/"+ httpRequest.getAttribute("uid");
        httpRequest.setAttribute("IframeSrc", IframeSrc);
        return "/manage/menu";
    }

    @ResponseBody
    @RequestMapping(value = "getStoreInfo", method = RequestMethod.GET)
    public Object getStoreInfo(HttpServletRequest request){
        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }
        Map res = new HashMap<String, Object>();
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(uid);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            res.put("store",stores.get(0));
        }

        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(uid);
        cateReq.setStatus(CommonTypeEnum.OK.getType());
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        if(cates != null
                && cates.size() > 0){
            res.put("cates",cates);
        }

        //没有类型的话就不用取菜单了
        if(cates != null
                && cates.size() > 0) {
            DishCategoryInfo defaultCat = cates.get(0);
            DishInfo dishReq = new DishInfo();
            dishReq.setUid(uid);
            dishReq.setCateId(defaultCat.getCateId());
            dishReq.setStatus(CommonTypeEnum.OK.getType());
            List<DishInfo> dishes = menuService.getDishInfo(dishReq);
            if(dishes != null
                    && dishes.size() > 0){
                res.put("dishes",dishes);
            }
        }

        CashierInfo req = new CashierInfo();
        req.setUid(uid);
        req.setStatus(CommonTypeEnum.OK.getType());
        List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
        if(cashieres != null
                && cashieres.size() > 0){
            if(cashieres.size() >= 3){
                cashieres.subList(0,3);
            }
            res.put("cashieres",cashieres);
        }

        Integer maxCount = menuService.getTableMaxCount(uid);
        if(maxCount != null
                && maxCount > 0){
            res.put("maxCount",maxCount);
        }
        return Response.ok(res);
    }

    @ResponseBody
    @RequestMapping(value = "getDishInfoByCate", method = RequestMethod.GET)
    public Object getDishInfoByCate(HttpServletRequest request,
                                    Long cateId){
        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(uid);
        cateReq.setCateId(cateId);
        cateReq.setStatus(CommonTypeEnum.OK.getType());
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        List<DishInfo> dishes = Lists.newArrayList();
        //没有类型的话就不用取菜单了
        if(cates != null
                && cates.size() > 0) {
            DishCategoryInfo cate = cates.get(0);
            DishInfo dishReq = new DishInfo();
            dishReq.setUid(uid);
            dishReq.setCateId(cate.getCateId());
            dishReq.setStatus(CommonTypeEnum.OK.getType());
            dishes = menuService.getDishInfo(dishReq);

        }
        return Response.ok(dishes);
    }


    @ResponseBody
    @RequestMapping(value = "getDishInfo", method = RequestMethod.GET)
    public Object getDishInfo(HttpServletRequest request,
                              Long dishId){

        String isLogin = (String)request.getAttribute("isLogin");
        if(!"true".equals(isLogin)){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if(dishId == null || dishId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        DishInfo dishReq = new DishInfo();
        dishReq.setDishId(dishId);
        List<DishInfo> dishes = menuService.getDishInfo(dishReq);
        if(dishes != null
                && dishes.size() > 0){
            DishInfo res = dishes.get(0);
            return Response.ok(res);
        }
        return  Response.fail(Response.RespType.DISH_NOT_EXITED);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "updateStoreInfo", method = RequestMethod.POST)
    public Object updateStoreInfo(HttpServletRequest request,
                                  @RequestBody UserStoreInfo storeinfo) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if(storeinfo == null
                || StringUtils.isBlank(storeinfo.getStoreName())
                || StringUtils.isBlank(storeinfo.getStoreAddress())
                || StringUtils.isBlank(storeinfo.getStoreHeadimg())
                || (StringUtils.isNotBlank(storeinfo.getStoreMobile()) && (!StringUtils.isNumeric(storeinfo.getStoreMobile())))
                || (StringUtils.isNotBlank(storeinfo.getStoreMobile()) && (storeinfo.getStoreMobile().length() > 20))
                ){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        storeinfo.setUid(uid);
        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(storeinfo.getUid());
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        int res = -1;

        if(stores != null
                && stores.size() > 0){
            UserStoreInfo oldStore = stores.get(0);
            //menuService.checkImgChanged(oldStore, storeinfo);
            res = menuService.updateStoreInfo(storeinfo);
        }else{
            res = menuService.insertStoreInfo(storeinfo);
        }

        if(res > 0){
            return Response.ok(Response.RespType.SUCCESS);
        }else{
            return Response.fail(Response.RespType.SERVER_ERROR);
        }
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "updateTableMaxNo", method = RequestMethod.POST)
    public Object updateTableMaxNo(HttpServletRequest request,
                                   @RequestParam Integer maxNumber) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if(maxNumber == null || maxNumber <= 0 || maxNumber > 200){
            return Response.fail(Response.RespType.PARAM_WRONG,"桌号应该为1至200之间的整数");
        }
        StoreTableNumber tnum = new StoreTableNumber();
        tnum.setTableMaxCount(maxNumber);
        tnum.setUid(uid);
        tnum.setSummitTime(new Date());
        Integer count = menuService.updateByPrimaryKeySelective(tnum);

        if(count > 0){
            return Response.ok(maxNumber);
        }else{
            return Response.fail(Response.RespType.SERVER_ERROR);
        }
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "addCashier", method = RequestMethod.POST)
    public Object addCashier(HttpServletRequest request,
                                  @RequestBody CashierInfo cashierInfo) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }
        cashierInfo.setUid(uid);

        if(cashierInfo == null
                || StringUtils.isBlank(cashierInfo.getCashierName())
                || StringUtils.isBlank(cashierInfo.getPassword())
                || StringUtils.isBlank(cashierInfo.getDescription())){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(cashierInfo.getCashierName().length() > 30
                || cashierInfo.getPassword().length() > 40
                || cashierInfo.getDescription().length() > 50){
            return Response.fail(Response.RespType.PARAM_WRONG, "用户名、密码长度小于20");
        }

        CashierInfo req = new CashierInfo();
        req.setUid(uid);
        req.setStatus(CommonTypeEnum.OK.getType());
        List<CashierInfo> cashieres = menuService.selectCashierByReq(req);
        if(cashieres != null
                && cashieres.size() > 0){
            if(cashieres.size() >= 3){
                return Response.fail(Response.RespType.CASHIER_TOO_MUCH);
            }else{
                for(CashierInfo c : cashieres){
                    if(c.getCashierName().equals(cashierInfo.getCashierName())){
                        return Response.fail(Response.RespType.CASHIER_EXIST_AREADY);
                    }
                }
            }
        }

        int res = -1;
        cashierInfo.setStatus(CommonTypeEnum.OK.getType());
        cashierInfo.setSummitTime(new Date());
        res = menuService.insertSelective(cashierInfo);
        if(res > 0){
            List<CashierInfo> resList = menuService.selectCashierByReq(req);
            return Response.ok(resList);
        }else{
            return Response.fail(Response.RespType.SERVER_ERROR);
        }
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "deleteCashier", method = RequestMethod.POST)
    public Object deleteCashier(HttpServletRequest request,
                                Long cashierId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }
        if(cashierId == null || cashierId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        CashierInfo req = new CashierInfo();
        req.setUid(uid);
        req.setCashierid(cashierId);
        req.setStatus(CommonTypeEnum.DELETE.getType());
        int res = -1;
        res = menuService.updateCashInfo(req);

        if(res > 0){
            CashierInfo resreq = new CashierInfo();
            resreq.setUid(uid);
            resreq.setStatus(CommonTypeEnum.OK.getType());
            List<CashierInfo> resList = menuService.selectCashierByReq(resreq);
            return Response.ok(resList);
        }else{
            return Response.fail(Response.RespType.SERVER_ERROR);
        }
    }


    @Transactional
    @ResponseBody
    @RequestMapping(value = "uploadDishInfo", method = RequestMethod.POST)
    public Object updateStoreInfo(@RequestBody DishInfo dishInfo,
                                  HttpServletRequest request,
                                  String optType,
                                  Long insertId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if (dishInfo == null
                || dishInfo.getDishPriceOrigin() == null || dishInfo.getDishPriceOrigin() < 0
                || dishInfo.getDishPriceFinal() == null || dishInfo.getDishPriceFinal() < 0
                || dishInfo.getCateId() == null || dishInfo.getCateId() < 0
                || StringUtils.isBlank(dishInfo.getDishName())) {
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        dishInfo.setUid(uid);

        //返回要用的req
        DishInfo dishReq = new DishInfo();
        dishReq.setUid(dishInfo.getUid());
        dishReq.setCateId(dishInfo.getCateId());
        if(optType != null && "insert".equals(optType)){
            List<DishInfo> dishes = menuService.getDishInfo(dishReq);

            //确认一下名称没有重复
            DishInfo dishReqForName = new DishInfo();
            dishReqForName.setUid(dishInfo.getUid());
            dishReqForName.setDishName(dishInfo.getDishName());
            List<DishInfo> sameNameList = menuService.getDishInfo(dishReqForName);
            if(sameNameList != null && sameNameList.size() > 0){
                return Response.fail(Response.RespType.NAME_EXIST_AREADY);
            }

            //需要插入到固定位置
            if(insertId != null && insertId > 0){
                List<DishInfo> updateIndexList = Lists.newArrayList();
                if(dishes != null){
                    Long dishId = insertId;
                    Integer index = 0;
                    boolean resetIndx = false;
                    for(DishInfo dish : dishes){
                        if(resetIndx){
                            index += 1;
                            dish.setDishIndex(index);
                            updateIndexList.add(dish);
                        }
                        if(dish.getDishId().longValue() == dishId){
                            index = dish.getDishIndex() + 1;
                            dishInfo.setDishIndex(index);
                            resetIndx = true;
                        }
                    }
                    if(updateIndexList.size() > 0){
                        Integer updateRes = menuService.updateIndexList(updateIndexList);
                        if(updateRes < 0){
                            return Response.fail(Response.RespType.SERVER_ERROR);
                        }
                    }
                    //insertId指向的对象已被删掉了，插到最后吧
                    if(index == 0){
                        index = dishes.size() - 1;
                        dishInfo.setDishIndex(index);
                    }

                }
            }else{
                //直接插入到末尾
                if(dishes != null && dishes.size() > 0){
                    int length = dishes.size();
                    DishInfo lastDish = dishes.get(length-1);
                    Integer index = lastDish.getDishIndex() + 1;
                    dishInfo.setDishIndex(index);
                }else{
                    dishInfo.setDishIndex(1);
                }
            }
            Integer insertRes = menuService.insertDishInfo(dishInfo);
            if(insertRes > 0){
                List<DishInfo> resDishList = menuService.getDishInfo(dishReq);
                return Response.ok(resDishList);
            }
        }else if("edit".equals(optType)){
            if(insertId != null && insertId > 0){

                //验证名字是否重复
                DishInfo dishReqForName = new DishInfo();
                dishReqForName.setUid(dishInfo.getUid() );
                dishReqForName.setDishName(dishInfo.getDishName());
                List<DishInfo> sameNameList = menuService.getDishInfo(dishReqForName);
                if(sameNameList != null && sameNameList.size() > 0){
                    for(DishInfo dish : sameNameList){
                        if(dish.getDishId().intValue() != insertId){
                            return Response.fail(Response.RespType.NAME_EXIST_AREADY);
                        }
                    }
                }

                dishInfo.setDishId(insertId);
                Integer updateRes = menuService.updateDishInfo(dishInfo);
                if(updateRes > 0){
                    List<DishInfo> resDishList = menuService.refreshDishIndex(dishInfo);
                    return Response.ok(resDishList);
                }
            }
        }
        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "uploadCateInfo", method = RequestMethod.POST)
    public Object uploadCateInfo(@RequestBody DishCategoryInfo cateInfo,
                                 HttpServletRequest request,
                                 String optType,
                                 Long insertId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }
        if (cateInfo == null
                || StringUtils.isBlank(cateInfo.getCateName())) {
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //返回值用到的req
        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(uid);
        cateInfo.setUid(uid);

        if(optType != null && "insert".equals(optType)){
            List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);

            //确认一下名称没有重复
            for(DishCategoryInfo cate: cates){
                if(cate.getCateName().equals(cateInfo.getCateName())){
                    return Response.fail(Response.RespType.NAME_EXIST_AREADY);
                }
            }
            //需要插入到固定位置
            if(insertId != null && insertId > 0){
                List<DishCategoryInfo> updateCateList = Lists.newArrayList();
                if(cates != null){
                    Long cateId = insertId;
                    Integer index = 0;
                    boolean resetIndx = false;
                    for(DishCategoryInfo cate : cates){
                        if(resetIndx){
                            index += 1;
                            cate.setCateIndex(index);
                            updateCateList.add(cate);
                        }
                        if(cate.getCateId().longValue() == cateId){
                            index = cate.getCateIndex() + 1;
                            cateInfo.setCateIndex(index);
                            resetIndx = true;
                        }
                    }
                    if(updateCateList.size() > 0){
                        Integer updateRes = menuService.updateCateList(updateCateList);
                        if(updateRes < 0){
                            return Response.fail(Response.RespType.SERVER_ERROR);
                        }
                    }
                    //insertId指向的对象已被删掉了，插到最后吧
                    if(index == 0){
                        index = cates.size();
                        cateInfo.setCateIndex(index);
                    }

                }
            }else{
                //直接插入到末尾
                if(cates != null && cates.size() > 0){
                    int length = cates.size();
                    DishCategoryInfo last = cates.get(length-1);
                    Integer index = last.getCateIndex() + 1;
                    cateInfo.setCateIndex(index);
                }
            }
            cateInfo.setStatus(CommonTypeEnum.OK.getType());
            Integer insertRes = menuService.insertCateInfo(cateInfo);
            if(insertRes > 0){
                List<DishCategoryInfo> resCateList = menuService.getCateInfoByReq(cateReq);
                return Response.ok(resCateList);
            }
        }else if("edit".equals(optType)){
            if(insertId != null && insertId > 0){
                //验证名字是否重复
                DishCategoryInfo cateReqForName = new DishCategoryInfo();
                cateReqForName.setUid(uid);
                cateReqForName.setCateName(cateInfo.getCateName());
                List<DishCategoryInfo> sameNameList = menuService.getCateInfoByReq(cateReqForName);
                if(sameNameList != null && sameNameList.size() > 0){
                    for(DishCategoryInfo cate : sameNameList){
                        if(cate.getCateId().intValue() != insertId){
                            return Response.fail(Response.RespType.NAME_EXIST_AREADY);
                        }
                    }
                }

                cateInfo.setCateId(insertId);
                Integer updateRes = menuService.updateCateInfo(cateInfo);
                if(updateRes > 0){
                    List<DishCategoryInfo> resCateList = menuService.getCateInfoByReq(cateReq);
                    return Response.ok(resCateList);
                }
            }
        }
        return Response.fail(Response.RespType.SERVER_ERROR);
    }


    @ResponseBody
    @RequestMapping(value = "deleteDishInfo", method = RequestMethod.POST)
    public Object updateStoreInfo(HttpServletRequest request,
                                  Long dishId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if(dishId == null || dishId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        //这获取cateId的逻辑
        if(dishId == null || dishId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        DishInfo dishReqForCate = new DishInfo();
        dishReqForCate.setDishId(dishId);
        List<DishInfo> dishForCateList = menuService.getDishInfo(dishReqForCate);
        if(dishForCateList == null || dishForCateList.size() != 1){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        Long cateId =  dishForCateList.get(0).getCateId();

        DishInfo req = new DishInfo();
        req.setDishId(dishId);
        req.setStatus(-1);
        Integer count = menuService.updateDishInfo(req);
        if(count > 0){
            DishInfo dishReq = new DishInfo();
            dishReq.setUid(uid);
            dishReq.setCateId(cateId);
            List<DishInfo> resDishList = menuService.getDishInfo(dishReq);
            return Response.ok(resDishList);
        }

        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = "deleteCateInfo", method = RequestMethod.POST)
    public Object deleteCateInfo(HttpServletRequest request,
                                  Long cateId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if(cateId == null || cateId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        DishCategoryInfo cateInfo = new DishCategoryInfo();
        cateInfo.setCateId(cateId);
        cateInfo.setStatus(CommonTypeEnum.DELETE.getType());
        Integer count = menuService.updateCateInfo(cateInfo);
        if(count > 0){
            DishCategoryInfo req = new DishCategoryInfo();
            req.setUid(uid);
            List<DishCategoryInfo> resCateList = menuService.getCateInfoByReq(req);
            return Response.ok(resCateList);
        }

        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = "moveDishInfo", method = RequestMethod.POST)
    public Object moveDishInfo(HttpServletRequest request,
                                  String direct,
                                  Long dishId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        //这获取cateId的逻辑
        if(dishId == null || dishId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        DishInfo dishReqForCate = new DishInfo();
        dishReqForCate.setDishId(dishId);
        dishReqForCate.setStatus(CommonTypeEnum.OK.getType());
        List<DishInfo> dishForCateList = menuService.getDishInfo(dishReqForCate);
        if(dishForCateList == null || dishForCateList.size() != 1){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        Long cateId =  dishForCateList.get(0).getCateId();

        if(!"up".equals(direct) && !"down".equals(direct)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        DishInfo dishReq = new DishInfo();
        dishReq.setUid(uid);
        dishReq.setCateId(cateId);
        List<DishInfo> dishes = menuService.getDishInfo(dishReq);
        Integer index = 0;
        if(dishes == null){
            return Response.ok();
        }
        for(DishInfo dish : dishes){
            if(dish.getDishId().longValue() == dishId){
                index = dishes.indexOf(dish);
            }
        }
        Integer count = -1;
        if("up".equals(direct)){
            if(index < 1){
                return Response.ok(dishes);
            }else{
                count = menuService.swapDishIndex(dishes.get(index - 1), dishes.get(index));
            }
        }else if("down".equals(direct)){
            if(index >= dishes.size() - 1){
                return Response.ok(dishes);
            }else{
                count = menuService.swapDishIndex(dishes.get(index + 1), dishes.get(index));
            }
        }
        if(count > 0){
            List<DishInfo> res = menuService.getDishInfo(dishReq);
            return Response.ok(res);
        }
        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = "moveCateInfo", method = RequestMethod.POST)
    public Object moveCateInfo(HttpServletRequest request,
                               String direct,
                               Long cateId) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }
        if(cateId == null || cateId < 0){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        if(!"up".equals(direct) && !"down".equals(direct)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }
        DishCategoryInfo cateReq = new DishCategoryInfo();
        cateReq.setUid(uid);
        List<DishCategoryInfo> cates = menuService.getCateInfoByReq(cateReq);
        Integer index = 0;
        if(cates == null){
            return Response.ok();
        }
        for(DishCategoryInfo cate : cates){
            if(cate.getCateId().longValue() == cateId){
                index = cates.indexOf(cate);
                break;
            }
        }
        Integer count = -1;
        if("up".equals(direct)){
            if(index < 1){
                return Response.ok(cates);
            }else{
                count = menuService.swapCateIndex(cates.get(index - 1), cates.get(index));
            }
        }else if("down".equals(direct)){
            if(index >= cates.size() - 1){
                return Response.ok(cates);
            }else{
                count = menuService.swapCateIndex(cates.get(index + 1), cates.get(index));
            }
        }
        if(count > 0){
            List<DishCategoryInfo> res = menuService.getCateInfoByReq(cateReq);
            return Response.ok(res);
        }
        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = "previewBincode", method = RequestMethod.POST)
    public Object previewBincode(HttpServletRequest request) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        //是否付费用户
//        User loginUser = new User();
//        loginUser.setUid(uid);
//        User user = userMapper.selectUserByAccountName(loginUser);
//        if(!menuService.isPaidUser(user)){
//            return Response.fail(Response.RespType.NEED_PERMITTE, OVER_DATE_MESSAGE);
//        }

        return Response.ok();
    }

    @RequestMapping(value = "getBincode/{uidStr}", method = RequestMethod.GET)
    public void getBincode(HttpServletResponse response,
                           @PathVariable String uidStr) throws Exception {

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");


        Long uid = Long.parseLong(uidStr);
        if(uid == null || uid < 0){
            return;
        }

        //是否付费用户
//        User loginUser = new User();
//        loginUser.setUid(uid);
//        User user = userMapper.selectUserByAccountName(loginUser);
//        if(!menuService.isPaidUser(user)){
//            return;
//        }

        //生成二维码
        String bincodeSrc = host_domain + "customMenu/orderList/" + uid + "/1";
        utilService.generateBincode(bincodeSrc,response);
    }

    @ResponseBody
    @RequestMapping(value = "generateBincode", method = RequestMethod.POST)
    public Object generateBincode(HttpServletRequest request) throws Exception {

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        //是否付费用户
        User loginUser = new User();
        loginUser.setUid(uid);
        User user = userMapper.selectUserByAccountName(loginUser);
        if(!menuService.isPaidUser(user)){
            return Response.fail(Response.RespType.NEED_PERMITTE, OVER_DATE_MESSAGE);
        }

        StoreTableNumber tableNumber = menuService.getStoreTableNumber(uid);
        if(tableNumber != null) {
            Integer count = tableNumber.getTableMaxCount();

            if(count > 0){
                utilService.generateBincodeToPdf(uid,count);
            }

            return Response.ok(count);
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG, "生成二维码失败，请到 管理收银界面 设置最大桌号");
        }
    }


}

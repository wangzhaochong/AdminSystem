package userCenter.controllers;


import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.misc.BASE64Encoder;
import userCenter.Utils.CookiesUtil;
import userCenter.Utils.HtmlTagUtil;
import userCenter.Utils.Response;
import userCenter.constant.PasswordQuestionConstant;
import userCenter.mapper.UserMapper;
import userCenter.model.batis.CashierInfo;
import userCenter.model.batis.PasswordQuestion;
import userCenter.model.batis.User;
import userCenter.model.batis.UserStoreInfo;
import userCenter.model.enumModel.CommonTypeEnum;
import userCenter.model.enumModel.UserType;
import userCenter.service.MenuService;
import userCenter.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("/manage")
public class userAuditController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MenuService menuService;

    public static Random random = new Random(System.currentTimeMillis());
    public static BASE64Encoder encoder = new BASE64Encoder();


    @RequestMapping(value = "index", method = RequestMethod.GET)
    public String audit(HttpServletRequest request,
                        @RequestParam(required = false)String accountName,
                        @RequestParam(required = false)Long reqUid,
                        @RequestParam(required = false)Integer searchType){
        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return "redirect:/";
        }

        User req = new User();
        req.setUid(uid);
        User user = userMapper.selectUserByAccountName(req);
        if(user == null || user.getUid() < 0 || user.getUserType() < 100){
            return "redirect:/";
        }else{
            if(user.getUserType().equals(UserType.ROOT_MANAGE.getType())){
                request.setAttribute("userType","root");
                Map<String, Integer> priceMap = PasswordQuestionConstant.getPriceMap();
                request.setAttribute("priceMap",priceMap);
            }else if(user.getUserType().equals(UserType.COMMON_MANAGE.getType())){
                request.setAttribute("userType","manage");
            }
        }

        //对参数做一个处理
        String manageUid = "";
        String manageName = "";
        String userUid = "";
        String userName = "";
        if(searchType != null){
            if(reqUid != null){
                if(searchType == 0){
                    manageUid =  reqUid.toString();
                }else if(searchType == 1){
                    userUid = reqUid.toString();
                }
            }
            if(StringUtils.isNotBlank(accountName)){
                if(searchType == 0){
                    manageName =  accountName;
                }else if(searchType == 1){
                    userName = accountName;
                }
            }
        }
        request.setAttribute("manageUid",manageUid);
        request.setAttribute("manageName",manageName);
        request.setAttribute("userUid",userUid);
        request.setAttribute("userName",userName);


        User userreq = null;
        if(reqUid != null && reqUid > 0){
            req.setUid(reqUid);
            userreq = userMapper.selectUserByAccountName(req);
        }
        if(userreq == null && StringUtils.isNotBlank(accountName)){
            User userreq2 = new User();
            userreq2.setAccountName(accountName);
            userreq = userMapper.selectUserByAccountName(userreq2);
        }

        if (searchType != null && searchType == 0) {
            request.setAttribute("userreq",userreq);
        }else if(searchType != null && searchType == 1){
            request.setAttribute("commonreq",userreq);
        }

        if(userreq != null){
            Date expireTime = new Date(userreq.getExpireTime());
            request.setAttribute("expireTime",expireTime);
            request.setAttribute("userreqType",UserType.getDesc4Int(userreq.getUserType()));
            //计算价格需要的数据
            if(searchType == 1){
                Date today = new Date();
                if(expireTime.after(today)){
                    Long dayCounts = (userreq.getExpireTime() -today.getTime())/(24*3600*1000);
                    Float minusPrice = 0f;
                    if(userreq.getUserType().equals(UserType.PAID_USER.getType())){
                        Integer price = PasswordQuestionConstant.getPriceMap().get("v1_final_price_1y");
                        minusPrice = ((float)dayCounts)/365*price;
                    }else if(userreq.getUserType().equals(UserType.PAID_USER_V2.getType())){
                        Integer price = PasswordQuestionConstant.getPriceMap().get("v2_final_price_1y");
                        minusPrice = ((float)dayCounts)/365*price;
                    }
                    request.setAttribute("minusPrice",minusPrice.intValue());
                }else{
                    request.setAttribute("minusPrice",0);
                }

            }
        }


        if(searchType != null && userreq != null){
            if(searchType == 1){
                //这是搜索用户
                UserStoreInfo storeReq = new UserStoreInfo();
                storeReq.setUid(reqUid);
                List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
                if(stores != null
                        && stores.size() == 1){
                    request.setAttribute("store",stores.get(0));
                }
            }
        }

        return "/manage/manageIndex";
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "upgrateUser", method = {RequestMethod.POST})
    public Response upgrateUser(HttpServletRequest request,
                                 Long uid,
                                 Integer grateType,
                                 Integer grateTime){

        Long idFromCookie = Long.parseLong((String)request.getAttribute("uid"));
        if(idFromCookie == null || idFromCookie < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        User req = new User();
        req.setUid(idFromCookie);
        User user = userMapper.selectUserByAccountName(req);
        if(user == null || user.getUid() < 0 || user.getUserType() < 100){
            return Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY, "无权限");
        }

        if(uid == null || grateType == null || grateTime == null){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        User reqUser = new User();
        reqUser.setUid(uid);
        User user2upgrate  = userMapper.selectUserByAccountName(reqUser);
        if(user2upgrate != null){
            Date expireTime = new Date();
            Calendar cl = Calendar.getInstance();
            cl.setTime(expireTime);
            if(grateTime.equals(0)){
                cl.add(Calendar.MONTH, 6);
                expireTime = cl.getTime();
            }else if(grateTime.equals(1)){
                cl.add(Calendar.YEAR, 1);
                expireTime = cl.getTime();
            }else if(grateTime.equals(1)){
                cl.add(Calendar.YEAR, 2);
                expireTime = cl.getTime();
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG,"时间不合法");
            }
            Date oldTime = new Date(user2upgrate.getExpireTime());
            //只有v1升v2去验证过期时间
            //v1、v2续期也要计算
            Integer originType = user2upgrate.getUserType();
            Integer finalType = grateType;
            if((finalType.equals(UserType.PAID_USER_V2.getType())
                    && originType.equals(UserType.PAID_USER.getType()))
                        || (originType.equals(finalType)
                                && !originType.equals(UserType.ORDINARY_USER.getType()))
            ){
                if(!expireTime.after(oldTime)){
                    return Response.fail(Response.RespType.PARAM_WRONG,"日期短于当前过期日期");
                }
            }
            //只有v1和v2需要换expiretime
            if(grateType.equals(UserType.PAID_USER.getType())
                    || grateType.equals(UserType.PAID_USER_V2.getType())){
                user2upgrate.setExpireTime(expireTime.getTime());
            }else{
                user2upgrate.setExpireTime(0l);
            }

            if(UserType.isValid(grateType)){
                user2upgrate.setUserType(grateType);
            }else{
                return Response.fail(Response.RespType.PARAM_WRONG,"升级类型不合法");
            }

            Integer res = userMapper.updateByPrimaryKeySelective(user2upgrate);
            if(res > 0){
                return Response.ok();
            }else{
                return Response.fail(Response.RespType.SERVER_ERROR);
            }
        }else{
            return Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY);
        }

    }

}

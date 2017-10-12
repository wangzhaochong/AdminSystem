package userCenter.controllers;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import scala.Int;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import userCenter.Utils.CookiesUtil;
import userCenter.Utils.HtmlTagUtil;
import userCenter.Utils.Response;
import userCenter.constant.PasswordQuestionConstant;
import userCenter.interceptor.LoginInterceptor;
import userCenter.mapper.UserMapper;
import userCenter.model.batis.CashierInfo;
import userCenter.model.batis.PasswordQuestion;
import userCenter.model.batis.User;
import userCenter.model.batis.UserStoreInfo;
import userCenter.model.enumModel.CommonTypeEnum;
import userCenter.model.enumModel.UserType;
import userCenter.service.MenuService;
import userCenter.service.UserService;
import userCenter.service.UtilService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("/user")
public class userCenterController {

    @Autowired
    UserService userService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    MenuService menuService;

    public static Random random = new Random(System.currentTimeMillis());
    public static BASE64Encoder encoder = new BASE64Encoder();


    @RequestMapping(value = "userCenter", method = RequestMethod.GET)
    public String userCenter(HttpServletRequest request, Model m){
        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return "redirect:/user/login";
        }

        //判断是否设置过密保问题
        List<PasswordQuestion> questionList = userService.getUserQuestion(uid);
        if(questionList != null && questionList.size() == 3){
            m.addAttribute("setQuestionAready",true);
        }else{
            m.addAttribute("setQuestionAready",false);
        }

        User req = new User();
        req.setUid(uid);
        User user = userMapper.selectUserByAccountName(req);
        if(user == null || user.getUid() < 0){
            return "redirect:/user/login";
        }else{
            m.addAttribute("accountName",user.getAccountName());
            if(user.getUserType() == 2){
                m.addAttribute("usertype","V1");
            }else if(user.getUserType() == 3){
                m.addAttribute("usertype","V2");
            }else{
                m.addAttribute("usertype","普通用户");
            }
            Date expireTime = new Date(user.getExpireTime());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd");
            m.addAttribute("expireTime",simpleDateFormat.format(expireTime));

            //计算价格要用的数据
            Date today = new Date();
            if(expireTime.after(today)){
                Long dayCounts = (user.getExpireTime() -today.getTime())/(24*3600*1000);
                Float minusPrice = 0f;
                if(user.getUserType().equals(UserType.PAID_USER.getType())){
                    Integer price = PasswordQuestionConstant.getPriceMap().get("v1_final_price_1y");
                    minusPrice = ((float)dayCounts)/365*price;
                }else if(user.getUserType().equals(UserType.PAID_USER_V2.getType())){
                    Integer price = PasswordQuestionConstant.getPriceMap().get("v2_final_price_1y");
                    minusPrice = ((float)dayCounts)/365*price;
                }
                request.setAttribute("minusPrice",minusPrice.intValue());
            }else{
                request.setAttribute("minusPrice",0);
            }
            Map<String, Integer> priceMap = PasswordQuestionConstant.getPriceMap();
            request.setAttribute("priceMap",priceMap);
        }

        UserStoreInfo storeReq = new UserStoreInfo();
        storeReq.setUid(uid);
        List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
        if(stores != null
                && stores.size() > 0){
            m.addAttribute("storeName",stores.get(0).getStoreName());
        }else{
            m.addAttribute("storeName","未设置");
        }

        Integer maxCount = menuService.getTableMaxCount(uid);
        if(maxCount != null
                && maxCount > 0){
            m.addAttribute("maxCount",maxCount);
        }else{
            m.addAttribute("maxCount",0);
        }

        CashierInfo cashierReq = new CashierInfo();
        cashierReq.setUid(uid);
        cashierReq.setStatus(CommonTypeEnum.OK.getType());
        List<CashierInfo> cashieres = menuService.selectCashierByReq(cashierReq);
        if(cashieres != null
                && cashieres.size() > 0){
            m.addAttribute("cashierCount",cashieres.size());
        }else{
            m.addAttribute("cashierCount",0);
        }

        Map<Integer,String> questionMap = PasswordQuestionConstant.getQuestionMap();
        m.addAttribute("questionMap",questionMap);

        return "/manage/userCenter";
    }

    @RequestMapping(value = "findPsw", method = RequestMethod.GET)
    public String findPsw(HttpServletRequest request, String name){
        if(StringUtils.isBlank(name)){
            return "redirect:/user/login";
        }

        User req = new User();
        req.setAccountName(name);
        User user = userMapper.selectUserByAccountName(req);
        if(user == null || user.getUid() < 0){
            return "redirect:/user/login";
        }else{
            Long uid = user.getUid();
            List<PasswordQuestion> questions = userService.selectQuestionByUid(uid);
            if(questions != null && questions.size() == 3){
                request.setAttribute("accountName",name);
                request.setAttribute("questions1",questions.get(0).getQuestionName());
                request.setAttribute("questions2",questions.get(1).getQuestionName());
                request.setAttribute("questions3",questions.get(2).getQuestionName());
                request.setAttribute("qid1",questions.get(0).getId());
                request.setAttribute("qid2",questions.get(1).getId());
                request.setAttribute("qid3",questions.get(2).getId());
            }else{
                request.setAttribute("msg","您尚未设置密保问题，无法找回密码。特殊需求请联系管理员");
            }

        }

        return "/manage/findPsw";
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "answerPost", method = RequestMethod.POST)
    public Object answerPost(HttpServletRequest request,
                             Integer q1, Integer q2, Integer q3,
                             String answer1, String answer2, String answer3){
        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        Map<Integer,String> questionMap = PasswordQuestionConstant.getQuestionMap();
        if(StringUtils.isBlank(answer1)
                || StringUtils.isBlank(answer2)
                || StringUtils.isBlank(answer3)
                || answer1.length() > 64
                || answer2.length() > 64
                || answer3.length() > 64){
            return Response.fail(Response.RespType.PARAM_WRONG, "答案长度过长");
        }
        if(questionMap.containsKey(q1) && questionMap.containsKey(q2) && questionMap.containsKey(q3)){
            String qstr1 = questionMap.get(q1);
            String qstr2 = questionMap.get(q2);
            String qstr3 = questionMap.get(q3);
            List<PasswordQuestion> passwordQuestions = Lists.newArrayList();
            PasswordQuestion pq1 = new PasswordQuestion(uid,qstr1,answer1);
            PasswordQuestion pq2 = new PasswordQuestion(uid,qstr2,answer2);
            PasswordQuestion pq3 = new PasswordQuestion(uid,qstr3,answer3);
            passwordQuestions.add(pq1);
            passwordQuestions.add(pq2);
            passwordQuestions.add(pq3);
            Integer count = userService.setUserQuestion(uid,passwordQuestions);
            if(count != 3){
                return Response.fail(Response.RespType.SERVER_ERROR, "数据库异常");
            }else{
                return Response.ok();
            }
        }
        return Response.fail(Response.RespType.SERVER_ERROR);
    }

    @ResponseBody
    @RequestMapping(value = "answerVerify", method = RequestMethod.POST)
    public Object answerVerify(HttpServletRequest request,
                               String accountName,
                               Long qid1, Long qid2, Long qid3,
                               String answer1, String answer2, String answer3){

        if(StringUtils.isBlank(accountName)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(qid1 < 0 || qid2 < 0 || qid3 < 0
                || StringUtils.isBlank(answer1)
                || StringUtils.isBlank(answer2)
                || StringUtils.isBlank(answer3)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        User req = new User();
        req.setAccountName(accountName);
        User user = userMapper.selectUserByAccountName(req);
        if(user == null || user.getUid() < 0){
            return Response.fail(Response.RespType.ACCOUNT_NOT_EXITED);
        }else{
            Long uid = user.getUid();
            List<PasswordQuestion> questions = userService.selectQuestionByUid(uid);
            if(questions != null && questions.size() == 3){
                //验证问题1
                Long qid = qid1;
                for(PasswordQuestion question : questions){
                    if(question.getId().equals(qid) && question.getQuestionAnswer().equals(answer1)){
                        qid1 = -1l;
                        break;
                    }
                }

                //验证问题2
                qid = qid2;
                for(PasswordQuestion question : questions){
                    if(question.getId().equals(qid) && question.getQuestionAnswer().equals(answer2)){
                        qid2 = -1l;
                        break;
                    }
                }

                //验证问题3
                qid = qid3;
                for(PasswordQuestion question : questions){
                    if(question.getId().equals(qid) && question.getQuestionAnswer().equals(answer3)){
                        qid3 = -1l;
                        break;
                    }
                }

                if(qid1 < 0 && qid2 < 0 && qid3 < 0){
                    return Response.ok();
                }else{
                    return Response.fail(Response.RespType.OPERATE_FAILED,"密保问题回答错误");
                }
            }else{
                return Response.fail(Response.RespType.SERVER_ERROR,"获取找回密码失败");
            }
        }

    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "findResetPsw", method = RequestMethod.POST)
    public Object findResetPsw(HttpServletRequest request,
                               HttpServletResponse response,
                               String accountName,
                               Long qid1, Long qid2, Long qid3,
                               String answer1, String answer2, String answer3,
                               String newPsw){

        if(StringUtils.isBlank(accountName)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(qid1 < 0 || qid2 < 0 || qid3 < 0
                || StringUtils.isBlank(answer1)
                || StringUtils.isBlank(answer2)
                || StringUtils.isBlank(answer3)){
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        User req = new User();
        req.setAccountName(accountName);
        User user = userMapper.selectUserByAccountName(req);
        if(user == null || user.getUid() < 0){
            return Response.fail(Response.RespType.ACCOUNT_NOT_EXITED);
        }else{
            Long uid = user.getUid();
            List<PasswordQuestion> questions = userService.selectQuestionByUid(uid);
            if(questions != null && questions.size() == 3){
                //验证问题1
                Long qid = qid1;
                for(PasswordQuestion question : questions){
                    if(question.getId().equals(qid) && question.getQuestionAnswer().equals(answer1)){
                        qid1 = -1l;
                        break;
                    }
                }

                //验证问题2
                qid = qid2;
                for(PasswordQuestion question : questions){
                    if(question.getId().equals(qid) && question.getQuestionAnswer().equals(answer2)){
                        qid2 = -1l;
                        break;
                    }
                }

                //验证问题3
                qid = qid3;
                for(PasswordQuestion question : questions){
                    if(question.getId().equals(qid) && question.getQuestionAnswer().equals(answer3)){
                        qid3 = -1l;
                        break;
                    }
                }

                if(qid1 < 0 && qid2 < 0 && qid3 < 0){
                    user.setPassword(newPsw);
                    int count = userMapper.updateByPrimaryKeySelective(user);
                    if(count > 0){
                        CookiesUtil.deleteCookie("pcsignature",request, response);
                        return Response.ok();
                    }
                    return Response.fail(Response.RespType.SERVER_ERROR);
                }else{
                    return Response.fail(Response.RespType.OPERATE_FAILED,"密保问题回答错误");
                }
            }else{
                return Response.fail(Response.RespType.SERVER_ERROR,"获取找回密码失败");
            }
        }
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "modifyPswPost", method = {RequestMethod.POST})
    public Response modifyPswPost(HttpServletRequest request,
                                  HttpServletResponse response,
                                  String psw,
                                  String oldpsw){

        Long uid = Long.parseLong((String)request.getAttribute("uid"));
        if(uid == null || uid < 0){
            return Response.fail(Response.RespType.LOGIN_NEEDED);
        }

        if(StringUtils.isNotBlank(psw)){

            //防止有人故意post垃圾数据导致回滚
            if(psw.length()>40){
                return  Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY,"密码过长");
            }


            User registerUser = new User();
            registerUser.setUid(uid);
            User user = userMapper.selectUserByAccountName(registerUser);
            if(user == null){
                return  Response.fail(Response.RespType.ACCOUNT_NOT_EXITED);
            }
            if(!user.getPassword().equals(oldpsw)){
                return  Response.fail(Response.RespType.PASSWORD_WRONG,"原密码输入错误");
            }
            registerUser.setPassword(psw);
            int count = userMapper.updateByPrimaryKeySelective(registerUser);
            if(count > 0){
                CookiesUtil.deleteCookie("pcsignature",request, response);
                return Response.ok();
            }
            return Response.fail(Response.RespType.SERVER_ERROR);

        }
        return Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY);
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(HttpServletRequest httpRequest, Model m){
        if("true".equals(httpRequest.getAttribute("isLogin"))){
            return "redirect:/";
        }
        if(httpRequest.getAttribute("setReturnUrl") != null){
            m.addAttribute("returnUrl", httpRequest.getAttribute("setReturnUrl"));
        }else{
            String returnUrl = userService.getCallbackUrl(httpRequest);
            m.addAttribute("returnUrl", returnUrl);
        }
        Long timeStamp = new Date().getTime();
        m.addAttribute("timeStamp", String.valueOf(timeStamp));
        return "/manage/login";
    }

    /*@ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public Response testselect(HttpServletRequest httpRequest,
                               @RequestParam String accountName,
                               @RequestParam Long uid){

        User request = new User();
        request.setAccountName(accountName);
        request.setUid(uid);
        User user = userMapper.selectUserByAccountName(request);
        return Response.ok(user);

    }*/


    @ResponseBody
    @RequestMapping(value = "loginPost", method = {RequestMethod.POST})
    public Response loginPost(@RequestParam String name,
                              @RequestParam String pwd,
                              @RequestParam String vrcode,
                              HttpServletRequest request,
                              HttpServletResponse response){

        //验证验证码
        if(StringUtils.isBlank(vrcode)){
            return Response.fail(Response.RespType.VERIFYCODE_EMPTY);
        }else{
            String sessionId = request.getSession(true).getId();
            String verifycode = utilsController.getVerifyCodeBySessionId(sessionId);
            if(!vrcode.equalsIgnoreCase(verifycode)){
                return Response.fail(Response.RespType.VERIFYCODE_WRONG);
            }else{
                utilsController.deleteVerifyCodeBySessionId(sessionId);
            }
        }


        if(StringUtils.isNotBlank(name)
                && StringUtils.isNotBlank(pwd)){

            //防止有人故意post垃圾数据导致回滚
            if(name.length() > 30 || pwd.length()>40){
                return  Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY);
            }

            User loginUser = new User();
            loginUser.setAccountName(name);
            User user = userMapper.selectUserByAccountName(loginUser);
            if(user == null || user.getUid() < 0){
                return  Response.fail(Response.RespType.ACCOUNT_NOT_EXITED);
            }
            if(user.getPassword().equals(pwd)){
                Integer token = random.nextInt(0xfffff);
                String codesource = user.getUid() + ":" + name + ":" + token;
                String signature = encoder.encode(codesource.getBytes());
                CookiesUtil.setCookie("pcsignature",
                        signature,
                        CookiesUtil.DEFAULT_EXPIRE_TIME,
                        CookiesUtil.THIS_DOMAIN,
                        CookiesUtil.THIS_PATH,
                        response);
                return Response.ok();
            }else{
                return  Response.fail(Response.RespType.PASSWORD_WRONG);
            }

        }
        return Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY);
    }


    @RequestMapping(value = "logout", method = {RequestMethod.GET})
    public String loginPost(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam String curPath){
        CookiesUtil.deleteCookie("pcsignature",request, response);
        String redUrl = "/";
        if(StringUtils.isNotBlank(curPath)){
            redUrl = curPath;
        }
        return "redirect:" + redUrl;
    }

    @Transactional
    @ResponseBody
    @RequestMapping(value = "registerPost", method = {RequestMethod.POST})
    public Response registerPost(@RequestParam String name,
                                 @RequestParam String pwd,
                                 @RequestParam String vrcode,
                                 HttpServletRequest request,
                                 HttpServletResponse response){

        //验证验证码
        if(StringUtils.isBlank(vrcode)){
            return Response.fail(Response.RespType.VERIFYCODE_EMPTY);
        }else{
            String sessionId = request.getSession(true).getId();
            String verifycode = utilsController.getVerifyCodeBySessionId(sessionId);
            if(!vrcode.equalsIgnoreCase(verifycode)){
                return Response.fail(Response.RespType.VERIFYCODE_WRONG);
            }else{
                utilsController.deleteVerifyCodeBySessionId(sessionId);
            }
        }

        if(StringUtils.isNotBlank(name)
                && StringUtils.isNotBlank(pwd)){

            //防止有人故意post垃圾数据导致回滚
            if(name.length() > 30 || pwd.length()>40){
                return  Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY);
            }

            if(HtmlTagUtil.containHTMLTag(name)){
                return  Response.fail(Response.RespType.USERNAME_SCRIPT);
            }

            User registerUser = new User();
            registerUser.setAccountName(name);
            User user = userMapper.selectUserByAccountName(registerUser);
            if(user != null && user.getUid() > 0){
                return  Response.fail(Response.RespType.ACCOUNT_EXITED);
            }
            registerUser.setPassword(pwd);
            registerUser.setExpireTime(System.currentTimeMillis());
            registerUser.setIsExpired(1);
            registerUser.setUserType(UserType.ORDINARY_USER.getType());
            int count = userMapper.insertUser(registerUser);
            if(count > 0 && registerUser.getUid() > 0){
                Integer token = random.nextInt(0xfffff);
                String codesource = registerUser.getUid() + ":" +name + ":" + token;
                String signature = encoder.encode(codesource.getBytes());
                CookiesUtil.setCookie("pcsignature",
                        signature,
                        CookiesUtil.DEFAULT_EXPIRE_TIME,
                        CookiesUtil.THIS_DOMAIN,
                        CookiesUtil.THIS_PATH,
                        response);
                return Response.ok();
            }
            return Response.fail(Response.RespType.SERVER_ERROR);

        }
        return Response.fail(Response.RespType.ACCOUNT_NOT_QUILITY);
    }

    public static void main(String [] s) throws IOException {
//        String str = "MTQ5NDU5OTE5MTk1MDY5MA";
//        byte[] bytes = LoginInterceptor.decoder.decodeBuffer(str);
//        String orderId = new String(bytes, "UTF-8");
//        System.out.println(Long.parseLong(orderId));

//        String codesource = "abcd";
//        String encodeStr = encoder.encode(codesource.getBytes());
//        System.out.println(encodeStr);
    }


}

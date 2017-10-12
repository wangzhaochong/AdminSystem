package userCenter.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhaochongwang on 2017/3/1.
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

    public static BASE64Decoder decoder = new BASE64Decoder();

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {

        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for(Cookie cookie : cookies){
                try{
                    if(cookie.getName().equals("pcsignature")){
                        if(StringUtils.isNotBlank(cookie.getValue())){
                            String encodeStr = decodeBase64(cookie.getValue());
                            String [] userinfo = encodeStr.split(":");
                            String uid = userinfo[0];
                            String accountName = userinfo[1];
                            if(StringUtils.isNoneBlank(uid)
                                    &&StringUtils.isNoneBlank(accountName)){
                                request.setAttribute("uid", uid);
                                request.setAttribute("accoutName", accountName);
                                request.setAttribute("isLogin", "true");
                            }else{
                                request.setAttribute("uid", "-1");
                                request.setAttribute("accoutName", "");
                                request.setAttribute("isLogin", "false");
                            }
                            return true;
                        }
                        break;
                    }
                }catch (Exception e){

                }

            }
        }
        request.setAttribute("uid", "-1");
        request.setAttribute("accoutName", "");
        request.setAttribute("isLogin", "false");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler,
                             ModelAndView modelAndView) throws Exception {
        if(modelAndView == null){
            return ;
        }
        String islogin = "false";
        String name = (String)request.getAttribute("accoutName");
        if(StringUtils.isNotBlank(name)){
            islogin = "true";
        }
        modelAndView.addObject("islogin", islogin);
        String path = request.getServletPath();
        modelAndView.addObject("curPath", path);

    }

    public static String decodeBase64(String s) {
        switch(s.length()%4) {
            case 3:
                s+= "="; break;
            case 2:
                s+= "=="; break;
            case 1:
                s+= "==="; break;
            default:
        }

        try {
            byte[] bytes = decoder.decodeBuffer(s);
            String decodeStr = new String(bytes, "UTF-8");
            return decodeStr;
        } catch (Exception e) {
        }
        return null;
    }

}

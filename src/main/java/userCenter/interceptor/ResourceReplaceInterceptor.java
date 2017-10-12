package userCenter.interceptor;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import sun.misc.BASE64Decoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by zhaochongwang on 2017/3/1.
 */
public class ResourceReplaceInterceptor extends HandlerInterceptorAdapter {

    public static BASE64Decoder decoder = new BASE64Decoder();

    @Value("#{configProperties['resourcePath']}")
    public String resourcePath;

    @Value("#{configProperties['resourceVersion']}")
    public String resourceVersion;

    @Override
    public boolean preHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler) throws Exception {
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

        modelAndView.addObject("resourcePath", resourcePath);
        modelAndView.addObject("resourceVersion", resourceVersion);
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }
}

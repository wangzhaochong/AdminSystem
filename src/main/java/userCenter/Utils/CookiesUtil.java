package userCenter.Utils;



import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Hayden on 2017/3/22.
 */
public class CookiesUtil {

    public static int DEFAULT_EXPIRE_TIME = 7 * 24 * 3600;
    public static int ORDER_EXPIRE_TIME = 6 * 3600;
    public static int CASHIER_EXPIRE_TIME = 7 * 24 * 3600;
    public static final String THIS_DOMAIN = "huluweizhan.cn";
    public static final String THIS_PATH = "/";


    public static void setCookie(String key, String value, int expireTime, String domain, String path, HttpServletResponse res) {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append("=").append(value).append("; ");
        Date expireDate = new Date();
        if (expireTime > 0) {
            expireDate = org.apache.commons.lang.time.DateUtils.addSeconds(expireDate, expireTime);
            sb.append("Expires=").append(expireDate.toString()).append("; ");
        } else {
            sb.append("Expires=").append("Session; ");
        }

        sb.append("HttpOnly; ");
        if (!org.springframework.util.StringUtils.isEmpty(domain))
            sb.append("Domain=").append(domain).append("; ");
        if (!org.springframework.util.StringUtils.isEmpty(path))
            sb.append("Path=").append(path).append("; ");
        res.addHeader("Set-Cookie", sb.toString());
    }

    public static void deleteCookie(String key, HttpServletRequest req, HttpServletResponse res) {

        Cookie [] cookies = req.getCookies();
        if(cookies == null) return;

        for(Cookie cookie : cookies){
            if(cookie.getName().equals(key)){
                cookie.setDomain(THIS_DOMAIN);
                cookie.setPath(THIS_PATH);
                cookie.setMaxAge(0);
                res.addCookie(cookie);
            }
        }

    }
}

package userCenter.Utils;


import com.alibaba.fastjson.JSONObject;
import org.omg.PortableInterceptor.SUCCESSFUL;

/**
 * Created by Hayden on 2017/3/22.
 */
public class Response {

    public static String code;

    public String message;

    public Object data;

    public static Response ok(){
        Response resp = new Response();
        resp.setCode(RespType.SUCCESS.getCode());
        resp.setMessage(RespType.SUCCESS.getMessage());
        return resp;
    }

    public static Response ok(Object data){
        Response resp = new Response();
        resp.setCode(RespType.SUCCESS.getCode());
        resp.setMessage(RespType.SUCCESS.getMessage());
        resp.setData(data);
        return resp;
    }

    public static Response fail(RespType resptype){
        Response resp = new Response();
        resp.setCode(resptype.getCode());
        resp.setMessage(resptype.getMessage());
        return resp;
    }

    public static Response fail(RespType resptype, String msg){
        Response resp = new Response();
        resp.setCode(resptype.getCode());
        resp.setMessage(msg);
        return resp;
    }


    public enum RespType {

        SUCCESS("200", "请求成功"),

        PARAM_WRONG("400", "参数错误"),
        ACCOUNT_EXITED("411", "用户名或密码已存在"),
        ACCOUNT_NOT_QUILITY("412", "用户名或密码不符合要求"),
        ACCOUNT_NOT_EXITED("413", "用户名不存在"),
        PASSWORD_WRONG("414", "密码错误"),
        VERIFYCODE_EMPTY("415", "验证码为空"),
        VERIFYCODE_WRONG("416", "验证码错误"),
        USERNAME_SCRIPT("417", "检测到不法标签，请联系管理员"),
        LOGIN_NEEDED("418", "请先登录"),
        NEED_PERMITTE("419", "需要升级用户等级"),

        STORE_NOT_EXITED("601", "商户信息不存在"),
        DISH_NOT_EXITED("602", "菜单信息不存在"),

        IMG_SEZE_WRONG("701", "图片大小不符合要求"),
        IMG_TYPE_WRONG("702", "图片类型不符合要求"),
        IMG_UPLOAD_FAIL("703", "上传图片失败"),
        IMG_NO_EXIST("704", ""),
        NAME_EXIST_AREADY("705", "该名称已经存在于您的菜单"),

        NOT_UNIQUE_PHONE("801", "每桌同时只支持一个手机号下单或修改"),
        OT_UNIQUE_PHONE("802", "当前桌号的服务已被暂停，请联系商家管理员"),

        CASHIER_TOO_MUCH("901", "每个店铺最多添加3位收银员"),
        CASHIER_EXIST_AREADY("902", "该账号名已存在"),

        OPERATE_FAILED("1001", "操作失败"),

        SERVER_ERROR("500", "服务异常"),
        NETWORK_ERROR("501", "网络异常");

        private String code;
        private String message;

        RespType(String code, String message){
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }



}

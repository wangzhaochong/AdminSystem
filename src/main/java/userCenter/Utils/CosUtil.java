package userCenter.Utils;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * Created by Hayden on 2017/3/22.
 */
public class CosUtil {

    public static final Long appId = 1253547645l;
    public static final String secretId = "AKIDvrdDdrboV1McnEIjkaGpERTL2Ys1Dtb9";
    public static final String secretKey = "b2IOJ3c4LRM5d4SgllfOwJZeYL7mFev1";
    public static final String bucketName = "wxproject";
    public static final String region = "tj";
    public static final String cosPathPrifix = "/upload_img/";
    public static Credentials cred ;
    public static ClientConfig clientConfig;
    public static COSClient cosClient;

    static {
        cred = new Credentials(appId, secretId, secretKey);
        clientConfig = new ClientConfig();
        // 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
        clientConfig.setRegion(region);
        cosClient = new COSClient(clientConfig, cred);
    }


    public static Integer sendCos(String filename, String path) {
        String cosPath = cosPathPrifix + filename;
        UploadFileRequest request = new UploadFileRequest(bucketName, cosPath, path);
        request.setEnableShaDigest(false);
        try{
            String msg = cosClient.uploadFile(request);
            JSONObject jo = JSON.parseObject(msg);
            Integer code = jo.getInteger("code");
            //String message = jo.getString("message");
            cosClient.shutdown();
            return code;
        }catch (Exception e){
            System.out.println(e);
        }
        return -1;
    }

    public static Integer deleteCos(String filename) {
        String cosPath = cosPathPrifix + filename;
        DelFileRequest request = new DelFileRequest(bucketName, cosPath);
        try{
            String msg = cosClient.delFile(request);
            JSONObject jo = JSON.parseObject(msg);
            Integer code = jo.getInteger("code");
            //String message = jo.getString("message");
            cosClient.shutdown();
            return code;
        }catch (Exception e){
            System.out.println(e);
        }
        return -1;
    }

    public static void main(String [] s){
        String path = "C:\\Users\\Hayden\\Desktop\\logo.png";
        //Integer res = sendCos("logo.png",path);
        Integer res = deleteCos("1.png");
        System.out.println(res);
    }

    @PostConstruct
    public void init(){
        System.out.println("init");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("destroy");
        cosClient.shutdown();
    }
}

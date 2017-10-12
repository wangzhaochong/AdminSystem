package userCenter.controllers;


import com.google.common.collect.Maps;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import userCenter.Utils.CookiesUtil;
import userCenter.Utils.Response;
import userCenter.Utils.VerifyCodeUtils;
import userCenter.mapper.UserMapper;
import userCenter.model.batis.DeleteImgInfo;
import userCenter.model.batis.DishInfo;
import userCenter.model.batis.User;
import userCenter.model.batis.UserStoreInfo;
import userCenter.service.CosService;
import userCenter.service.MenuService;
import userCenter.service.UserService;
import userCenter.service.UtilService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("/utils")
public class utilsController {

    public static Map<String, String> verifyCodeMap1 = Maps.newHashMap();
    public static Map<String, String> verifyCodeMap2 = Maps.newHashMap();
    public static Map<String, String> curmap = verifyCodeMap1;

    //windows
    @Value("#{configProperties['filePath']}")
    private String FILEPATH;

    //Linux Server
    //private static final String FILEPATH = "/uploadimg/";
    private static final String DEFAULT_URI = "http://wxproject-1253547645.costj.myqcloud.com/proj_res/img/common/img404.png";
    private static final String UPLOAD_PREFFIX = "http://wxproject-1253547645.costj.myqcloud.com/upload_img/";

    private static final String IMG_PREFIX = "http://wxproject-1253547645.costj.myqcloud.com/proj_res/img/";
    private static final String JS_PREFIX = "http://wxproject-1253547645.costj.myqcloud.com/proj_res/js/";
    private static final String CSS_PREFIX = "http://wxproject-1253547645.costj.myqcloud.com/proj_res/css/";


    @Autowired
    UtilService utilService;

    @Autowired
    MenuService menuService;

    @Autowired
    CosService cosService;

    @RequestMapping(value = "getVerifyCode/{randomCode:[0-9]*}", method = RequestMethod.GET)
    public void login(HttpServletRequest request,
                        HttpServletResponse response){

        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        //生成随机字串
        String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
        //map存入session和code
        HttpSession session = request.getSession(true);
        String sessionId = session.getId();
        curmap.put(sessionId, verifyCode);

        //生成图片
        int w = 150, h = 50;

        try{
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode);
        }catch (Exception e){

        }

    }

    @RequestMapping(value = "staticRes", method = RequestMethod.GET)
    public String staticRes(@RequestParam String resourcePath,
                          @RequestParam String resType,
                          HttpServletResponse response){
        if(resType != null
                && "img".equals(resType)
                && StringUtils.isNotBlank(resourcePath)){
            String returnUrl = IMG_PREFIX + resourcePath;
            return "redirect:" + returnUrl;
        }else if(resType != null
                && "js".equals(resType)
                && StringUtils.isNotBlank(resourcePath)){
            String returnUrl = JS_PREFIX + resourcePath;
            return "redirect:" + returnUrl;
        }else if(resType != null
                && "css".equals(resType)
                && StringUtils.isNotBlank(resourcePath)){
            String returnUrl = CSS_PREFIX + resourcePath;
            return "redirect:" + returnUrl;
        }else{
            response.setHeader("Content-Type", "application/json");
            response.setStatus(404);
            return "{\"code\":\"404\",\"msg\":\"resource missing\"}";
        }


    }

    @ResponseBody
    @RequestMapping(value = "imgUpload/{source}/{uid}", method = RequestMethod.POST)
    public Object imgUpload(HttpServletRequest request,
                            @PathVariable String source,
                            @PathVariable String uid) {

        if(org.apache.commons.lang.StringUtils.isNotBlank(source)
                && org.apache.commons.lang.StringUtils.isNumeric(source)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        if(org.apache.commons.lang.StringUtils.isNotBlank(uid)
                && org.apache.commons.lang.StringUtils.isNumeric(uid)){
        }else{
            return Response.fail(Response.RespType.PARAM_WRONG);
        }

        String uri = DEFAULT_URI;

        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter = multiRequest.getFileNames();



            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file = multiRequest.getFile(iter.next().toString());
                if(file != null)
                {

                    Long size = file.getSize();
                    if(size > 100 * 1024 || size < 0){
                        return Response.fail(Response.RespType.IMG_SEZE_WRONG);
                    }

                    String name = file.getOriginalFilename();
                    name = utilService.generateName(name);

                    if(StringUtils.isBlank(name)){
                        return Response.fail(Response.RespType.IMG_TYPE_WRONG);
                    }

                    if(FILEPATH == null){
                        return Response.fail(Response.RespType.IMG_UPLOAD_FAIL, "FILEPATH配置失败");
                    }
                    String path= FILEPATH + name;

                    //上传
                    try {
                        file.transferTo(new File(path));
                        Integer res = startUploadCOS(path,name);
                        if(res == 0){
                            //发送到cos记录表
                            uri = UPLOAD_PREFFIX + name;

                            DeleteImgInfo dif = new DeleteImgInfo();
                            dif.setImgUrl(uri);
                            dif.setStatus(1);
                            dif.setSummitTime(new Date());
                            dif.setSource(Integer.parseInt(source));
                            dif.setSourceId(Long.parseLong(uid));
                            cosService.addDeleteImgInfo(dif);

                            return Response.ok(uri);
                        }else{
                            return Response.fail(Response.RespType.IMG_UPLOAD_FAIL);
                        }
                    } catch (IOException e) {
                        return Response.fail(Response.RespType.NETWORK_ERROR);
                    }
                }
            }
        }

        return  Response.fail(Response.RespType.IMG_NO_EXIST);
    }

    @ResponseBody
    @RequestMapping(value = "deleteOldSrc", method = RequestMethod.GET)
    public Object deleteOldSrc(Long storeId,
                               Long dishId,
                               String oldSrc) {

        String dishImg = null;
        if(storeId > 0){
            UserStoreInfo storeReq = new UserStoreInfo();
            storeReq.setUid(storeId);
            List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
            if(stores != null
                    && stores.size() > 0){
                UserStoreInfo userStoreInfo= stores.get(0);
                dishImg = userStoreInfo.getStoreHeadimg();
            }
        }
        if(dishId > 0){
            DishInfo dishReq = new DishInfo();
            dishReq.setDishId(dishId);
            List<DishInfo> dishes = menuService.getDishInfo(dishReq);
            if(dishes != null
                    && dishes.size() > 0){
                DishInfo dish = dishes.get(0);
                dishImg = dish.getDishImg();
            }
        }

        if(StringUtils.isNotBlank(oldSrc) && !oldSrc.equals(dishImg)){
            DeleteImgInfo dif = new DeleteImgInfo();
            dif.setImgUrl(oldSrc);
            dif.setStatus(1);
            dif.setSummitTime(new Date());
            cosService.addDeleteImgInfo(dif);
        }
        return Response.ok();
    }

    private Integer startUploadCOS(String path, String name) {
        Integer res = cosService.sendCos(name,path);
        File file = new File(path);
        file.delete();
        return res;
    }


    @Scheduled(cron = "0 0/5 * * * ?")
    public void switchVerifyCodeMap(){
       if(curmap == verifyCodeMap1){
           verifyCodeMap2.clear();
           curmap = verifyCodeMap2;
       }else{
           verifyCodeMap1.clear();
           curmap = verifyCodeMap1;
       }
    }

    public static String getVerifyCodeBySessionId(String sessionId){
        String verifycode = curmap.get(sessionId);
        if(StringUtils.isBlank(verifycode)){
            if(curmap == verifyCodeMap1){
                verifycode = verifyCodeMap2.get(sessionId);
            }else{
                verifycode = verifyCodeMap1.get(sessionId);
            }
        }
        return verifycode;
    }

    public static void deleteVerifyCodeBySessionId(String sessionId){
        String verifycode = curmap.remove(sessionId);
        if(StringUtils.isBlank(verifycode)){
            if(curmap == verifyCodeMap1){
                verifyCodeMap2.remove(sessionId);
            }else{
                verifyCodeMap1.remove(sessionId);
            }
        }
    }

    public void setFILEPATH(String FILEPATH) {
        this.FILEPATH = FILEPATH;
    }
}

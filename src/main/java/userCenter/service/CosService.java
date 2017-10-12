package userCenter.service;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.request.DelFileRequest;
import com.qcloud.cos.request.UploadFileRequest;
import com.qcloud.cos.sign.Credentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import userCenter.mapper.DelImgMapper;
import userCenter.model.batis.DeleteImgInfo;
import userCenter.model.batis.DishInfo;
import userCenter.model.batis.UserStoreInfo;
import userCenter.model.enumModel.DelImgSouceType;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

/**
 * Created by Hayden on 2017/3/22.
 */
@Service
public class CosService {

    public static final Long APPID = 1253547645l;
    public static final String SECRETID = "AKIDvrdDdrboV1McnEIjkaGpERTL2Ys1Dtb9";
    public static final String SECRETKEY = "b2IOJ3c4LRM5d4SgllfOwJZeYL7mFev1";
    public static final String BUCKETNAME = "wxproject";
    public static final String REGION = "tj";
    public static final String COSPATH_PRIFFIX = "/upload_img/";
    public Credentials cred ;
    public ClientConfig clientConfig;
    public COSClient cosClient;
    private List<String> needRemoveFromCOS;

    @Autowired
    DelImgMapper delImgMapper;

    @Autowired
    MenuService menuService;

    @PostConstruct
    public void init(){
        System.out.println("init CosService");
        cred = new Credentials(APPID, SECRETID, SECRETKEY);
        clientConfig = new ClientConfig();
        // 设置bucket所在的区域，比如华南园区：gz； 华北园区：tj；华东园区：sh ；
        clientConfig.setRegion(REGION);
        cosClient = new COSClient(clientConfig, cred);
        //needRemoveFromCOS = Lists.newArrayList();
    }

    @PreDestroy
    public void destroy(){
        cosClient.shutdown();
//        if(needRemoveFromCOS != null
//                && needRemoveFromCOS.size() > 0){
//            removeListFromCOS(needRemoveFromCOS);
//        }
        System.out.println("destroy CosService");
    }

    //下午和半夜的3点7分7秒启动
    @Scheduled(cron = "7 7 3,15 * * ?")
    //@Scheduled(cron = "7 0/1 * * * ?")
    public void delCosImg(){
        DeleteImgInfo deleteImgInfo = new DeleteImgInfo();
        List<DeleteImgInfo> delImgList = delImgMapper.selectDelImgInfoByReq(deleteImgInfo);
        List<DeleteImgInfo> delList = Lists.newArrayList();

        if(delImgList != null){
            for(DeleteImgInfo delImg : delImgList){
                Integer source = delImg.getSource();
                Long uid = delImg.getSourceId();
                if(source == null || uid == null
                        || source <= 0 || uid <= 0){
                    continue;
                }
                String imgInUse = null;
                //图片来自商铺
                if(source.equals(DelImgSouceType.FROM_STORE.getType())){
                    UserStoreInfo storeReq = new UserStoreInfo();
                    storeReq.setUid(uid);
                    List<UserStoreInfo> stores = menuService.getStoreInfo(storeReq);
                    if(stores != null
                            && stores.size() > 0){
                        UserStoreInfo userStoreInfo= stores.get(0);
                        imgInUse = userStoreInfo.getStoreHeadimg();
                    }

                }else if(source.equals(DelImgSouceType.FROM_DISH.getType())){
                    //图片来自菜品
                    DishInfo dishReq = new DishInfo();
                    dishReq.setUid(uid);
                    List<DishInfo> dishes = menuService.getDishInfo(dishReq);
                    if(dishes != null){
                        for(DishInfo dish : dishes){
                            if(dish.getDishImg() != null && dish.getDishImg().equals(delImg.getImgUrl())){
                                imgInUse = dish.getDishImg();
                                break;
                            }
                        }
                    }
                }
                if(imgInUse == null || !imgInUse.equals(delImg.getImgUrl())){
                    delList.add(delImg);
                }
            }
        }

        for(DeleteImgInfo del : delList){
            String removeUrl = del.getImgUrl();
            if(removeUrl == null || !removeUrl.contains(COSPATH_PRIFFIX)){
                continue;
            }
            int index = removeUrl.indexOf(COSPATH_PRIFFIX);
            index += COSPATH_PRIFFIX.length();
            String filename = removeUrl.substring(index);
            Integer res = deleteCos(filename);
            if(res == 0){
                del.setStatus(-1);
                delImgMapper.updateDelImgInfo(del);
            }
        }

    }

    public Integer sendCos(String filename, String path) {
        String cosPath = COSPATH_PRIFFIX + filename;
        UploadFileRequest request = new UploadFileRequest(BUCKETNAME, cosPath, path);
        request.setEnableShaDigest(false);
        try{
            String msg = cosClient.uploadFile(request);
            JSONObject jo = JSON.parseObject(msg);
            Integer code = jo.getInteger("code");
            //String message = jo.getString("message");
            //cosClient.shutdown();
            return code;
        }catch (Exception e){
            System.out.println(e);
        }
        return -1;
    }

    public Integer deleteCos(String filename) {
        String cosPath = COSPATH_PRIFFIX + filename;
        DelFileRequest request = new DelFileRequest(BUCKETNAME, cosPath);
        try{
            String msg = cosClient.delFile(request);
            JSONObject jo = JSON.parseObject(msg);
            Integer code = jo.getInteger("code");
            //String message = jo.getString("message");
            //cosClient.shutdown();
            return code;
        }catch (Exception e){
            System.out.println(e);
        }
        return -1;
    }

//    public void addDeleteImg(String oldImg) {
//        if(needRemoveFromCOS == null){
//            needRemoveFromCOS = Lists.newArrayList();
//        }
//        needRemoveFromCOS.add(oldImg);
//        if(needRemoveFromCOS.size() > 9){
//
//            Thread deleteTask = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    removeListFromCOS(needRemoveFromCOS);
//                }
//            });
//            deleteTask.setDaemon(true);
//            deleteTask.start();
//        }
//    }

    private void removeListFromCOS(List<String> needRemoveFromCOS) {

        if(needRemoveFromCOS != null
                && needRemoveFromCOS.size() > 0){
            for(String removeUrl : needRemoveFromCOS){
                if(!removeUrl.contains(COSPATH_PRIFFIX)){
                    continue;
                }
                int index = removeUrl.indexOf(COSPATH_PRIFFIX);
                index += COSPATH_PRIFFIX.length();
                String filename = removeUrl.substring(index);
                deleteCos(filename);
            }
            needRemoveFromCOS.clear();
        }

    }

    public static void main(String [] s){
        String path = "http://wxproject-1253547645.costj.myqcloud.com/proj_res/img/manage/upload_img/start.png";
        int index = path.indexOf(COSPATH_PRIFFIX);
        index += COSPATH_PRIFFIX.length();
        String filename = path.substring(index);
        System.out.println(filename);
    }


    public void addDeleteImgInfo(DeleteImgInfo dif) {
        delImgMapper.insertDishInfo(dif);
    }
}

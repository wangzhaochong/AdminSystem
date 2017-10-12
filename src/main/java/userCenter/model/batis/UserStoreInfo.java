package userCenter.model.batis;


import java.io.Serializable;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class UserStoreInfo implements Serializable {

    private Long uid;

    private String storeName;

    private String storeAddress;

    private String storeOnwer;

    private String storeMobile;

    private String storeDescription;

    private String storeHeadimg;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getStoreOnwer() {
        return storeOnwer;
    }

    public void setStoreOnwer(String storeOnwer) {
        this.storeOnwer = storeOnwer;
    }

    public String getStoreMobile() {
        return storeMobile;
    }

    public void setStoreMobile(String storeMobile) {
        this.storeMobile = storeMobile;
    }

    public String getStoreDescription() {
        return storeDescription;
    }

    public void setStoreDescription(String storeDescription) {
        this.storeDescription = storeDescription;
    }

    public String getStoreHeadimg() {
        return storeHeadimg;
    }

    public void setStoreHeadimg(String storeHeadimg) {
        this.storeHeadimg = storeHeadimg;
    }
}

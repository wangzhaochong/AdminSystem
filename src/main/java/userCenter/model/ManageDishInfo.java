package userCenter.model;


import java.io.Serializable;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class ManageDishInfo implements Serializable {

    private Long dishId;

    private String dishName;

    private Float dishPriceOrigin;

    private Float dishPriceFinal;

    private Float dishPriceDiscount;

    private String dishIngredient;

    private String dishAbstract;

    private String dishDescription;

    private String dishImg;

    private Long uid;

    private Integer status;

    private Integer dishIndex;

    private Long cateId;

    private String cateName;

    private Integer consumeCount;

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public Float getDishPriceOrigin() {
        return dishPriceOrigin;
    }

    public void setDishPriceOrigin(Float dishPriceOrigin) {
        this.dishPriceOrigin = dishPriceOrigin;
    }

    public Float getDishPriceFinal() {
        return dishPriceFinal;
    }

    public void setDishPriceFinal(Float dishPriceFinal) {
        this.dishPriceFinal = dishPriceFinal;
    }

    public Float getDishPriceDiscount() {
        return dishPriceDiscount;
    }

    public void setDishPriceDiscount(Float dishPriceDiscount) {
        this.dishPriceDiscount = dishPriceDiscount;
    }

    public String getDishIngredient() {
        return dishIngredient;
    }

    public void setDishIngredient(String dishIngredient) {
        this.dishIngredient = dishIngredient;
    }

    public String getDishAbstract() {
        return dishAbstract;
    }

    public void setDishAbstract(String dishAbstract) {
        this.dishAbstract = dishAbstract;
    }

    public String getDishDescription() {
        return dishDescription;
    }

    public void setDishDescription(String dishDescription) {
        this.dishDescription = dishDescription;
    }

    public String getDishImg() {
        return dishImg;
    }

    public void setDishImg(String dishImg) {
        this.dishImg = dishImg;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getDishIndex() {
        return dishIndex;
    }

    public void setDishIndex(Integer dishIndex) {
        this.dishIndex = dishIndex;
    }

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }

    public Integer getConsumeCount() {
        return consumeCount;
    }

    public void setConsumeCount(Integer consumeCount) {
        this.consumeCount = consumeCount;
    }
}

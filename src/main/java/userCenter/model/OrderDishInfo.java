package userCenter.model;


import java.io.Serializable;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class OrderDishInfo implements Serializable {

    private Long dishId;

    private String dishName;

    private Float dishPrice;

    private Integer count;

    private Float singlePrice;

    private Float originPrice;

    private Float disount;

    public Long getDishId() {
        return dishId;
    }

    public void setDishId(Long dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Float getDishPrice() {
        return dishPrice;
    }

    public void setDishPrice(Float dishPrice) {
        this.dishPrice = dishPrice;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Float getOriginPrice() {
        return originPrice;
    }

    public void setOriginPrice(Float originPrice) {
        this.originPrice = originPrice;
    }

    public Float getDisount() {
        return disount;
    }

    public void setDisount(Float disount) {
        this.disount = disount;
    }

    public Float getSinglePrice() {
        return singlePrice;
    }

    public void setSinglePrice(Float singlePrice) {
        this.singlePrice = singlePrice;
    }
}

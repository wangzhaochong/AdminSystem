package userCenter.model;


import userCenter.model.batis.OrderRecode;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class CashierOrderDetailVo implements Serializable {

    String customComment;

    String manageComment;

    Float totalPrice;

    List<OrderDishInfo> orderDishInfoList;

    Integer orderCount;

    Integer status;

    public String getCustomComment() {
        return customComment;
    }

    public void setCustomComment(String customComment) {
        this.customComment = customComment;
    }

    public String getManageComment() {
        return manageComment;
    }

    public void setManageComment(String manageComment) {
        this.manageComment = manageComment;
    }

    public List<OrderDishInfo> getOrderDishInfoList() {
        return orderDishInfoList;
    }

    public void setOrderDishInfoList(List<OrderDishInfo> orderDishInfoList) {
        this.orderDishInfoList = orderDishInfoList;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}

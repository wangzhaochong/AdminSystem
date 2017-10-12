package userCenter.model.batis;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class OrderRecode implements Serializable {

    private Long id;

    private Long uid;

    private Long tableId;

    private Long orderId;

    private String dishListStr;

    private Integer status;

    private Float totalPrice;

    private String customComment;

    private String manageComment;

    private Date summitTime;

    private Date finishTime;

    private Date startTime;

    private Date endTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public String getDishListStr() {
        return dishListStr;
    }

    public void setDishListStr(String dishListStr) {
        this.dishListStr = dishListStr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

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

    public Date getSummitTime() {
        return summitTime;
    }

    public void setSummitTime(Date summitTime) {
        this.summitTime = summitTime;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}

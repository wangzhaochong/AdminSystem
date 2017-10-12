package userCenter.model;


import userCenter.model.batis.OrderRecode;

import java.io.Serializable;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class CashierStaticInfo implements Serializable {

    Integer totalTable = 0;

    Integer finishTable  = 0;

    Integer unfinishTable  = 0;

    Integer parsedTable  = 0;

    Integer orderCount  = 0;

    Float totalMoney  = 0f;

    Integer unfinishOrderCount  = 0;

    public Integer getTotalTable() {
        return totalTable;
    }

    public void setTotalTable(Integer totalTable) {
        this.totalTable = totalTable;
    }

    public Integer getFinishTable() {
        return finishTable;
    }

    public void setFinishTable(Integer finishTable) {
        this.finishTable = finishTable;
    }

    public Integer getUnfinishTable() {
        return unfinishTable;
    }

    public void setUnfinishTable(Integer unfinishTable) {
        this.unfinishTable = unfinishTable;
    }

    public Integer getParsedTable() {
        return parsedTable;
    }

    public void setParsedTable(Integer parsedTable) {
        this.parsedTable = parsedTable;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }

    public Float getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Float totalMoney) {
        this.totalMoney = totalMoney;
    }

    public Integer getUnfinishOrderCount() {
        return unfinishOrderCount;
    }

    public void setUnfinishOrderCount(Integer unfinishOrderCount) {
        this.unfinishOrderCount = unfinishOrderCount;
    }
}

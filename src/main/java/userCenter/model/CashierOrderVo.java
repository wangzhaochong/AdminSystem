package userCenter.model;


import userCenter.model.batis.OrderRecode;
import userCenter.model.batis.StoreTableNumber;

import java.io.Serializable;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class CashierOrderVo implements Serializable {

    Integer tableId;

    Boolean exclude;

    OrderRecode orderRecode;

    public Integer getTableId() {
        return tableId;
    }

    public void setTableId(Integer tableId) {
        this.tableId = tableId;
    }

    public OrderRecode getOrderRecode() {
        return orderRecode;
    }

    public void setOrderRecode(OrderRecode orderRecode) {
        this.orderRecode = orderRecode;
    }

    public Boolean getExclude() {
        return exclude;
    }

    public void setExclude(Boolean exclude) {
        this.exclude = exclude;
    }
}

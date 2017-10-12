package userCenter.model.batis;

import java.util.Date;

public class CashierInfo {
    private Long cashierid;

    private Long uid;

    private String cashierName;

    private String password;

    private String description;

    private Integer status;

    private Date summitTime;

    public Long getCashierid() {
        return cashierid;
    }

    public void setCashierid(Long cashierid) {
        this.cashierid = cashierid;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getCashierName() {
        return cashierName;
    }

    public void setCashierName(String cashierName) {
        this.cashierName = cashierName == null ? null : cashierName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getSummitTime() {
        return summitTime;
    }

    public void setSummitTime(Date summitTime) {
        this.summitTime = summitTime;
    }
}
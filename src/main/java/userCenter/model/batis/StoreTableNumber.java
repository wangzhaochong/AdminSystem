package userCenter.model.batis;

import java.util.Date;

public class StoreTableNumber {
    private Long uid;

    private Integer tableMaxCount;

    private String excludeTableNumber;

    private Date summitTime;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getTableMaxCount() {
        return tableMaxCount;
    }

    public void setTableMaxCount(Integer tableMaxCount) {
        this.tableMaxCount = tableMaxCount;
    }

    public String getExcludeTableNumber() {
        return excludeTableNumber;
    }

    public void setExcludeTableNumber(String excludeTableNumber) {
        this.excludeTableNumber = excludeTableNumber == null ? null : excludeTableNumber.trim();
    }

    public Date getSummitTime() {
        return summitTime;
    }

    public void setSummitTime(Date summitTime) {
        this.summitTime = summitTime;
    }
}
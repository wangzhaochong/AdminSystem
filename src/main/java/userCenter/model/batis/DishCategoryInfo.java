package userCenter.model.batis;

public class DishCategoryInfo {
    private Long cateId;

    private String cateName;

    private Integer status;

    private Integer cateIndex;

    private Long uid;

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
        this.cateName = cateName == null ? null : cateName.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCateIndex() {
        return cateIndex;
    }

    public void setCateIndex(Integer cateIndex) {
        this.cateIndex = cateIndex;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}
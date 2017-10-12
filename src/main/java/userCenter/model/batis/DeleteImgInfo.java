package userCenter.model.batis;


import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
public class DeleteImgInfo implements Serializable {

    private Long id;

    private String imgUrl;

    private Date summitTime;

    private Integer status;

    //1表示商铺来源 2表示菜品来源
    private Integer source;

    private Long sourceId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Date getSummitTime() {
        return summitTime;
    }

    public void setSummitTime(Date summitTime) {
        this.summitTime = summitTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }
}

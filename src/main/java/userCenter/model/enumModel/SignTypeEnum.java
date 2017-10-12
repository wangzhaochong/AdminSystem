package userCenter.model.enumModel;

/**
 * Created by Hayden on 2017/5/7.
 */
public enum SignTypeEnum {

    NEW_ORDER_AND_COMMENT(1,"是否有新的订单和评论");

    private Integer type;
    private String descript;

    SignTypeEnum(Integer type, String descript){
        this.type = type;
        this.descript = descript;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}

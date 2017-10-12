package userCenter.model.enumModel;

/**
 * Created by Hayden on 2017/5/7.
 */
public enum CommonTypeEnum {

    DELETE(-1,"已删除"),
    OK(1,"正常"),
    PARSED(2,"暂停");

    private Integer type;
    private String descript;

    CommonTypeEnum(Integer type, String descript){
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

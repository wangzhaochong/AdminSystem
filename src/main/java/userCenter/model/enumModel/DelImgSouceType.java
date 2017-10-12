package userCenter.model.enumModel;

/**
 * Created by Hayden on 2017/5/7.
 */
public enum DelImgSouceType {

    FROM_STORE(1,"图片来自商铺信息"),
    FROM_DISH(2,"图片来自菜品信息");

    private Integer type;
    private String descript;

    DelImgSouceType(Integer type, String descript){
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

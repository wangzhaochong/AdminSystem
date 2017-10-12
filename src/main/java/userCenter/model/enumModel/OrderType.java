package userCenter.model.enumModel;

/**
 * Created by Hayden on 2017/5/7.
 */
public enum OrderType {

    ORDER_WAIT(1,"进行中的菜单"),
    ORDER_FINISH(2,"已完成菜单"),
    ORDER_DELETE(-1,"删除或撤单");

    private Integer type;
    private String descript;

    OrderType(Integer type, String descript){
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

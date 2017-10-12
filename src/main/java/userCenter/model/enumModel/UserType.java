package userCenter.model.enumModel;

/**
 * Created by Hayden on 2017/5/7.
 */
public enum UserType {

    ORDINARY_USER(1,"普通户用"),
    PAID_USER(2,"V1会员"),
    PAID_USER_V2(3,"V2会员"),
    ROOT_MANAGE(101,"root管理员"),
    COMMON_MANAGE(102,"普通管理员");

    private Integer type;
    private String descript;

    UserType(Integer type, String descript){
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

    public static String getDesc4Int(Integer type) {
        for(UserType e : values()){
            if(e.getType().equals(type)){
                return e.getDescript();
            }
        }
        return null;
    }

    public static boolean isValid(Integer type) {
        for(UserType e : values()){
            if(e.getType().equals(type)){
                return true;
            }
        }
        return false;
    }

    public static boolean isPaid(Integer type) {
        if(PAID_USER.getType().equals(type)
                || PAID_USER_V2.getType().equals(type) ){
            return true;
        }
        return false;
    }
}

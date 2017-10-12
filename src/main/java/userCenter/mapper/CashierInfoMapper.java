package userCenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import userCenter.model.batis.CashierInfo;

import java.util.List;

public interface CashierInfoMapper {
    @Insert({
        "insert into cashier_info (cashierid, uid, ",
        "cashier_name, password, ",
        "description, status, ",
        "summit_time)",
        "values (#{cashierid,jdbcType=BIGINT}, #{uid,jdbcType=BIGINT}, ",
        "#{cashierName,jdbcType=VARCHAR}, #{password,jdbcType=VARCHAR}, ",
        "#{description,jdbcType=VARCHAR}, #{status,jdbcType=INTEGER}, ",
        "#{summitTime,jdbcType=DATE})"
    })
    int insert(CashierInfo record);

    int insertSelective(CashierInfo record);

    @Select({
        "select",
        "cashierid, uid, cashier_name, password, description, status, summit_time",
        "from cashier_info",
        "where cashierid = #{cashierid,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    CashierInfo selectByPrimaryKey(Long cashierid);

    List<CashierInfo> selectByReq(CashierInfo req);

    int updateByPrimaryKeySelective(CashierInfo record);

    @Update({
        "update cashier_info",
        "set uid = #{uid,jdbcType=BIGINT},",
          "cashier_name = #{cashierName,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "description = #{description,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "summit_time = #{summitTime,jdbcType=DATE}",
        "where cashierid = #{cashierid,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(CashierInfo record);
}
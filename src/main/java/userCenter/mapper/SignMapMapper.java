package userCenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import userCenter.model.batis.SignMap;
import userCenter.model.batis.SignMapKey;

public interface SignMapMapper {
    @Insert({
        "insert into sign_map (sign_id, store_id, ",
        "sign_value)",
        "values (#{signId,jdbcType=INTEGER}, #{storeId,jdbcType=BIGINT}, ",
        "#{signValue,jdbcType=INTEGER})"
    })
    int insert(SignMap record);

    int insertSelective(SignMap record);

    @Select({
        "select",
        "sign_id, store_id, sign_value",
        "from sign_map",
        "where sign_id = #{signId,jdbcType=INTEGER}",
          "and store_id = #{storeId,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    SignMap selectByPrimaryKey(SignMapKey key);

    int updateByPrimaryKeySelective(SignMap record);

    @Update({
        "update sign_map",
        "set sign_value = #{signValue,jdbcType=INTEGER}",
        "where sign_id = #{signId,jdbcType=INTEGER}",
          "and store_id = #{storeId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(SignMap record);
}
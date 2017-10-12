package userCenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import userCenter.model.batis.StoreTableNumber;

public interface StoreTableNumberMapper {
    @Insert({
        "insert into store_table_number (uid, table_max_count, ",
        "exclude_table_number, summit_time)",
        "values (#{uid,jdbcType=BIGINT}, #{tableMaxCount,jdbcType=INTEGER}, ",
        "#{excludeTableNumber,jdbcType=VARCHAR}, #{summitTime,jdbcType=DATE})"
    })
    int insert(StoreTableNumber record);

    int insertSelective(StoreTableNumber record);

    @Select({
        "select",
        "uid, table_max_count, exclude_table_number, summit_time",
        "from store_table_number",
        "where uid = #{uid,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    StoreTableNumber selectByPrimaryKey(Long uid);

    int updateByPrimaryKeySelective(StoreTableNumber record);

    @Update({
        "update store_table_number",
        "set table_max_count = #{tableMaxCount,jdbcType=INTEGER},",
          "exclude_table_number = #{excludeTableNumber,jdbcType=VARCHAR},",
          "summit_time = #{summitTime,jdbcType=DATE}",
        "where uid = #{uid,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(StoreTableNumber record);
}
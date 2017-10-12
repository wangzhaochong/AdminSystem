package userCenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import userCenter.model.batis.DishCategoryInfo;

import java.util.List;

public interface DishCategoryInfoMapper {
    @Insert({
        "insert into dish_category_info (cate_id, cate_name, ",
        "status, cate_index, ",
        "uid)",
        "values (#{cateId,jdbcType=BIGINT}, #{cateName,jdbcType=VARCHAR}, ",
        "#{status,jdbcType=INTEGER}, #{cateIndex,jdbcType=INTEGER}, ",
        "#{uid,jdbcType=BIGINT})"
    })
    int insert(DishCategoryInfo record);

    int insertSelective(DishCategoryInfo record);

    @Select({
        "select",
        "cate_id, cate_name, status, cate_index, uid",
        "from dish_category_info",
        "where cate_id = #{cateId,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    DishCategoryInfo selectByPrimaryKey(Long cateId);

    List<DishCategoryInfo> selectByReq(DishCategoryInfo req);

    int updateByPrimaryKeySelective(DishCategoryInfo record);

    @Update({
        "update dish_category_info",
        "set cate_name = #{cateName,jdbcType=VARCHAR},",
          "status = #{status,jdbcType=INTEGER},",
          "cate_index = #{cateIndex,jdbcType=INTEGER},",
          "uid = #{uid,jdbcType=BIGINT}",
        "where cate_id = #{cateId,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(DishCategoryInfo record);

    Integer updateIndexList(List<DishCategoryInfo> updateCateList);
}
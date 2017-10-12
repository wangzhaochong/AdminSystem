package userCenter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import userCenter.model.batis.PasswordQuestion;

import java.util.List;

public interface PasswordQuestionMapper {
    @Insert({
        "insert into password_question (id, uid, question_name, ",
        "question_answer)",
        "values (#{id,jdbcType=BIGINT}, #{uid,jdbcType=BIGINT}, #{questionName,jdbcType=VARCHAR}, ",
        "#{questionAnswer,jdbcType=VARCHAR})"
    })
    int insert(PasswordQuestion record);

    int insertSelective(PasswordQuestion record);

    @Select({
        "select",
        "id, uid, question_name, question_answer",
        "from password_question",
        "where id = #{id,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    PasswordQuestion selectByPrimaryKey(Long id);

    @Select({
            "select",
            "id, uid, question_name, question_answer",
            "from password_question",
            "where uid = #{uid,jdbcType=BIGINT}"
    })
    @ResultMap("BaseResultMap")
    List<PasswordQuestion> selectByUid(Long id);

    int updateByPrimaryKeySelective(PasswordQuestion record);

    @Update({
        "update password_question",
        "set uid = #{uid,jdbcType=BIGINT},",
          "question_name = #{questionName,jdbcType=VARCHAR},",
          "question_answer = #{questionAnswer,jdbcType=VARCHAR}",
        "where id = #{id,jdbcType=BIGINT}"
    })
    int updateByPrimaryKey(PasswordQuestion record);
}
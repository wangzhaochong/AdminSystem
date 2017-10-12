package userCenter.mapper;

import userCenter.model.batis.User;


/**
 * Created by zhaochongwang on 2017/1/10.
 */
public interface UserMapper {

    User selectUserByAccountName(User u);

    int insertUser(User u);

    int updateByPrimaryKeySelective(User u);

}

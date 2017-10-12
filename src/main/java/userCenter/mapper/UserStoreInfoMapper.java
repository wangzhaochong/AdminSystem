package userCenter.mapper;

import org.apache.lucene.queryparser.xml.builders.UserInputQueryBuilder;
import userCenter.model.batis.User;
import userCenter.model.batis.UserStoreInfo;

import java.util.List;


/**
 * Created by zhaochongwang on 2017/1/10.
 */
public interface UserStoreInfoMapper {

   List<UserStoreInfo> selectStoreInfoByAccountName(UserStoreInfo store);

   Integer updateStoreInfoByUid(UserStoreInfo store);

   Integer insertStoreInfo(UserStoreInfo storeReq);
}

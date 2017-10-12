package userCenter.mapper;

import userCenter.model.batis.DeleteImgInfo;
import userCenter.model.batis.DishInfo;

import java.util.List;


/**
 * Created by zhaochongwang on 2017/1/10.
 */
public interface DelImgMapper {

   List<DeleteImgInfo> selectDelImgInfoByReq(DeleteImgInfo deleteImgInfo);

   Integer updateDelImgInfo(DeleteImgInfo deleteImgInfo);

   Integer insertDishInfo(DeleteImgInfo deleteImgInfo);
}

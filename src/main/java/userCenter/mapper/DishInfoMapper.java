package userCenter.mapper;

import userCenter.model.batis.DishInfo;

import java.util.List;


/**
 * Created by zhaochongwang on 2017/1/10.
 */
public interface DishInfoMapper {

   List<DishInfo> selectDishInfoByReq(DishInfo dishInfo);

   List<DishInfo> selectDishInfoByButch(List<Long> dishIdList);

   Integer insertDishInfo(DishInfo dishInfo);

   Integer updateDishInfo(DishInfo dishInfo);

   Integer updateIndexList(List<DishInfo> updateIndexList);
}

package userCenter.mapper;

import userCenter.model.batis.DeleteImgInfo;
import userCenter.model.batis.OrderRecode;

import java.util.List;


/**
 * Created by zhaochongwang on 2017/1/10.
 */
public interface OrderRecodeMapper {

   List<OrderRecode> selectOrderRecodeByReq(OrderRecode req);

   Integer updateOrderRecode(OrderRecode req);

   Integer insertOrderRecode(OrderRecode req);

   Integer countOrderRecodeByReq(OrderRecode req);

}

package userCenter.constant;


import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import userCenter.model.OrderDishInfo;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by zhaochongwang on 2017/1/10.
 */
@Component
public class PasswordQuestionConstant {

    public static Map<Integer,String> questionMap;

    public static Map<String,Integer> priceMap;

    public static Map<String,String> sentenceMap;

    @PostConstruct
    public void init(){
        questionMap = Maps.newLinkedHashMap();
        questionMap.put(0,"您高中班主任的名字是？");
        questionMap.put(1,"您初中班主任的名字是？");
        questionMap.put(2,"您小学班主任的名字是？");
        questionMap.put(3,"您配偶的姓名是？");
        questionMap.put(4,"您的小学校名是？");
        questionMap.put(5,"您配偶的生日是？");
        questionMap.put(6,"您母亲的生日是？");
        questionMap.put(7,"您父亲的生日是？");
        questionMap.put(8,"您宠物的名字是？");
        questionMap.put(9,"您的初中校名是？");

        priceMap = Maps.newHashMap();
        priceMap.put("v1_origin_price_6m",79);
        priceMap.put("v1_final_price_6m",59);
        priceMap.put("v1_origin_price_1y",129);
        priceMap.put("v1_final_price_1y",99);
        priceMap.put("v1_origin_price_2y",199);
        priceMap.put("v1_final_price_2y",179);
        priceMap.put("v2_origin_price_6m",119);
        priceMap.put("v2_final_price_6m",99);
        priceMap.put("v2_origin_price_1y",189);
        priceMap.put("v2_final_price_1y",159);
        priceMap.put("v2_origin_price_2y",359);
        priceMap.put("v2_final_price_2y",329);

        sentenceMap = Maps.newHashMap();
    }

    public static Map<Integer,String> getQuestionMap(){
        return questionMap;
    }

    public static Map<String, Integer> getPriceMap() {
        return priceMap;
    }

    public static Map<String, String> getSentenceMap() {
        return sentenceMap;
    }
}

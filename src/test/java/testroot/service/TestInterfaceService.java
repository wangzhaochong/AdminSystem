package testroot.service;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 调用外部接口，服务类
 */
//@Service
public class TestInterfaceService {


    private static Log logger = LogFactory.getLog(TestInterfaceService.class);

    //@Resource
    private RestTemplate testTemplate;

    private int i = 0;


    public JSONObject getJSONFromInterface(String url, Map<String, Object> params) {
        i += 1;
        JSONObject ret = null;
        try {
            String jsonStr = testTemplate.getForObject(url, String.class, params);
            if (StringUtils.isNotBlank(jsonStr)) {
                ret = JSONObject.parseObject(jsonStr);
            }
        } catch (Exception e) {
            //logger.error("error and url = " + url + "and para = " + params, e);
        } finally {
        }
        return ret;
    }

    public RestTemplate getTestTemplate() {
        return testTemplate;
    }

    public void setTestTemplate(RestTemplate testTemplate) {
        this.testTemplate = testTemplate;
    }
}

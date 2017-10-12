package testroot.testclass;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import testroot.service.TestInterfaceService;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.*;

/**
 * Created by zhaochongwang on 2016/9/21.
 */
@SuppressWarnings("deprecation")
public class HttpConnectionTest extends BasicTest{

    //@Resource
    private TestInterfaceService testInterfaceService = (TestInterfaceService)atx.getBean("TestInterfaceService");;

    private int index = 0;

    //@Test
    public void testConnection(){
        JSONObject jsonObject = testInterfaceService.getJSONFromInterface("http://m.focus.cn/cz/rss/news/11297017/?channelId=433",
                new HashMap<String, Object>());
        System.out.println(jsonObject);

    }

    //并发检索测试，直接跑这个方法
    //@Test
    public void testCocurrent(){

        //i上限是线程数
        for(int i=0; i<50; i+=1){
            Thread connectThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for(;;){
                            index += 1;
                            String url = urls[index % (urls.length-1)];
                            long start = System.currentTimeMillis();
                            JSONObject res = testInterfaceService.getJSONFromInterface(url,
                                    new HashMap<String, Object>());
                            long timecost = System.currentTimeMillis() - start;
                            if(timecost > 100){
                                System.out.println("timecost :" + timecost + " | url : " + url);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            });
            connectThread.setDaemon(true);
            connectThread.start();
        }
        for(;;);

    }

    public static String [] urls =  new String[]{
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=契税13",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=92",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=房屋（乌鲁木齐",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=税11（北京bb",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=契税5",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=（乌鲁",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=契 税1 11",
            "http://book-search.focus-dev.cn/esBaike/searchIndex?q=户33",

    };



}

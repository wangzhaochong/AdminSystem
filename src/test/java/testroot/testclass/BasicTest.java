package testroot.testclass;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhaochongwang on 2017/3/7.
 */
public class BasicTest {

    public static ApplicationContext atx;

    //@BeforeClass
    public static void beforeClass() {
        atx = new ClassPathXmlApplicationContext(new String[] {"context/ApplicationContext.xml"});
    }


    //@AfterClass
    public static void afterClass() {
        System.out.println("Test finish!");
    }


}

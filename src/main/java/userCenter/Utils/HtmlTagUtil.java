package userCenter.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hayden on 2017/3/22.
 */
public class HtmlTagUtil {

    //是否包含html标签
    public static boolean containHTMLTag(String htmlStr){

        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(htmlStr);
        if(m_html.find()){
            return true;
        }
        return false;
    }


    public static void main(String [] s){

        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式

        String htmlStr = "<adadw>dwadwa";

        Pattern p_script=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_script=p_script.matcher(htmlStr);
        if(m_script.find()){
            System.out.println(m_script.group());
        }
    }
}

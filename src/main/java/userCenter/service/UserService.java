package userCenter.service;

import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.ECMAErrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import userCenter.mapper.PasswordQuestionMapper;
import userCenter.model.batis.PasswordQuestion;

import javax.security.auth.callback.PasswordCallback;
import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

/**
 * Created by Hayden on 2017/3/22.
 */
@Service
public class UserService {

    public static final String THIS_DOMAIN = "www.huluweizhan.cn";

    @Autowired
    PasswordQuestionMapper passwordQuestionMapper;

    public String getCallbackUrl(HttpServletRequest httpRequest){
        String returnUrl = null;
        try {
            String referer = httpRequest.getHeader("Referer");
            String scheme = httpRequest.getScheme();
            if(referer != null){
                URL url = new URL(referer);
                String host = url.getHost();
                if(host == null){
                    returnUrl = scheme + "://" + THIS_DOMAIN;
                }else{
                    if(host.contains(".paicai.cn")
                            && !url.getPath().contains("user/login")){
                        returnUrl = referer;
                    }else{
                        returnUrl = scheme + "://" + host;
                    }
                }
            }else{
                returnUrl = scheme + "://" + THIS_DOMAIN;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnUrl;
    }

    public static void main(String [] s) throws URISyntaxException, MalformedURLException {
        String ss = "http://www.baidu.com/abc/def";
        URL url = new URL(ss);
        String path = url.getPath();
        System.out.println(path);
        URI uri = new URI(ss);
        System.out.println( uri.getScheme());
    }


    public List<PasswordQuestion> getUserQuestion(Long uid) {

        List<PasswordQuestion> res = Lists.newArrayList();
        if(uid != null && uid > 0){
            res = passwordQuestionMapper.selectByUid(uid);
        }
        return res;
    }

    public Integer setUserQuestion(Long uid, List<PasswordQuestion> passwordQuestions) {

        Integer res = -1;

        if(uid == null || uid < 0 || passwordQuestions == null || passwordQuestions.size() != 3){
            return -1;
        }

        //老的问题一定要删掉
        List<PasswordQuestion> oldList =  passwordQuestionMapper.selectByUid(uid);
        if(oldList != null){
            for(PasswordQuestion passwordQuestion: oldList){
                //就当是删掉了吧
                passwordQuestion.setUid(-1l);
                passwordQuestionMapper.updateByPrimaryKey(passwordQuestion);
            }
        }

        for(PasswordQuestion passwordQuestion : passwordQuestions){
            res = passwordQuestionMapper.insertSelective(passwordQuestion);
            if(res <= 0) return -1;
        }

        return 3;
    }

    public List<PasswordQuestion> selectQuestionByUid(Long uid){
        List<PasswordQuestion> res = Lists.newArrayList();
        if(uid != null && uid > 0){
            res =  passwordQuestionMapper.selectByUid(uid);
        }
        return res;
    }


}

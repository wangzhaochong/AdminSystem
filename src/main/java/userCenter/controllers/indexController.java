package userCenter.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by zhaochongwang on 2017/2/24.
 */
@Controller
@RequestMapping("/index")
public class indexController {

    @RequestMapping(value = "manageIndex", method = RequestMethod.GET)
    public String manageIndex(){
        return "/manage/index";
    }

    @RequestMapping(value = "guide", method = RequestMethod.GET)
    public String guide(){
        return "/manage/guide";
    }



}

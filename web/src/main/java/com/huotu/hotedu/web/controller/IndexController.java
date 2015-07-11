package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Login;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shiliting on 2015/6/10.
 * 进入后台首页的Controller
 * @author shiliting741@163.com
 */
@Controller
public class IndexController {

    @RequestMapping("/sayMyname")
    @ResponseBody
    public String sayMyname(@AuthenticationPrincipal Login who){
        return who.getLoginName();
    }

    @RequestMapping("/")
    public String loadIndex(){
        return "/backend/index";
    }
}

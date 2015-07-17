package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Login;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by luffy on 2015/6/10.
 * 登录有关的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 进入登录页面
     * @return login.html
     */
    @RequestMapping("/backend/login")
    public String index(){
        return "/backend/login";
    }

    @RequestMapping("/pc/login")
    public String login(String userName,String pasword,Model model) {
        String turnPage = "/pc/yun-index";
        String msgInfo = "";
        String errInfo = "";
        if(userName==null||"".equals(userName)) {
            errInfo = "用户名不能为空";
        }else if("".equals(pasword)||pasword==null) {
            errInfo = "密码不能为空";
        }else {

        }

        return turnPage;
    }
}

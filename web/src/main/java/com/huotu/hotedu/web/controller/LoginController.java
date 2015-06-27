package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Login;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @RequestMapping("/backend/login")
    public String index(){
        return "/backend/login";
    }
}

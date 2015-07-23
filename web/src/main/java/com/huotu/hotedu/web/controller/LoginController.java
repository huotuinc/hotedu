package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by luffy on 2015/6/10.
 * 登录有关的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class LoginController {

    /**
     * 进入登录页面
     * @return login.html
     */
    @RequestMapping("/backend/login")
    public String index(Model model){
        return "backend/login";
    }


    @RequestMapping("/pc/login")
    public String check(Model model) {
        String turnPage = "pc/yun-index";
        String msgInfo = "登录";
        String style = "padding:0px;display:none";
        model.addAttribute("style",style);
        model.addAttribute("msgInfo",msgInfo);
        return turnPage;
    }

    @RequestMapping("/pc/loginSuccess")
    public String loginSuccess(Model model) {
        String turnPage = "pc/yun-index";
        String msgInfo = "";
        String style = "padding:0px;";
        model.addAttribute("style",style);
        model.addAttribute("msgInfo",msgInfo);
        return turnPage;
    }

    @RequestMapping("/pc/loginFailed")
    public String loginFailed(Model model) {
        String turnPage = "pc/yun-index";
        String loginForm = "display:block";
        String msgInfo = "登录";
        String errInfo = "用户或密码错误";
        String style = "padding:0px;display:none";
        model.addAttribute("style",style);
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        model.addAttribute("loginForm",loginForm);
        return turnPage;
    }

}

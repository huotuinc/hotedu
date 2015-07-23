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

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MemberService memberService;

    /**
     * 进入登录页面
     * @return login.html
     */
    @RequestMapping("/backend/login")
    public String index(){
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
        String msgInfo = "登录";
        String style = "padding:0px;display:none";
        model.addAttribute("style",style);
        model.addAttribute("msgInfo",msgInfo);
        return turnPage;
    }


    /*@RequestMapping("/pc/login")
    public String login(String loginName,String password,Model model) throws Exception {
        String turnPage = "/pc/yun-index";
        String style = "padding:0px;display: none";
        String msgInfo = "";
        String errInfo = "";
        if(loginName==null||"".equals(loginName)) {
            errInfo = "用户名不能为空";
        }else if("".equals(password)||password==null) {
            errInfo = "密码不能为空";
        }else {
            Member mb = memberService.findOneByLoginName(loginName);
            String pwd = DigestUtils.md5DigestAsHex(password.getBytes("UTF-8")).toLowerCase();
            if(mb==null) {
                errInfo = "不存在该用户";
            }else if(!pwd.equals(mb.getPassword())) {
                errInfo = "密码不正确";
            }else {
                model.addAttribute("realName",mb.getRealName());
                msgInfo = "";
                style = "padding:0px;";
            }
        }
        model.addAttribute("errInfo",errInfo);
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("style",style);
        return turnPage;
    }*/
}

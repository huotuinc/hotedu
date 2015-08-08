package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Editor;
import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.service.MessageContentService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luffy on 2015/6/10.
 * Modify by shiliting on 2015/8/1
 * 登录有关的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class LoginController {
    /**
     * 用来储存处理静态资源的接口
     */
    @Autowired
    StaticResourceService staticResourceService;

    @Autowired
    MessageContentService messageContentService;

    /**
     * 进入登录页面
     * @return login.html
     */
    @RequestMapping("/backend/login")
    public String index(){
        return "backend/index";
    }

    @RequestMapping("/pc/gerenToIndex")
    public String gerenToIndex(Model model) {
        String turnPage = "pc/yun-index";
        String style = "padding:0px;";
        model.addAttribute("style",style);
        return turnPage;
    }

    @RequestMapping("/pc/baominLogin")
    public String baominLogin(Model model) {
        String turnPage = "pc/yun-index";
        String loginForm = "display:block";
        String msgInfo = "登录";
        String style = "padding:0px;display:none";
        model.addAttribute("style",style);
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("loginForm",loginForm);
        return turnPage;
    }

    @RequestMapping("/pc/index")
    public String index(Model model) throws Exception{
        String turnPage = "pc/yun-index";
        /*Page<MessageContent> pages = messageContentService.loadIndexMessageContent();
        ArrayList<MessageContent> list = new ArrayList<MessageContent>(13);
        for(MessageContent messageContent : pages){
            messageContent.setPictureUri(staticResourceService.getResource(messageContent.getPictureUri()).toURL().toString());
            list.add(messageContent);
        }

        for(int i=0;i<12;i++)
            model.addAttribute("messageContent"+String.valueOf(i),list.get(i));*/

        return turnPage;
    }

    @RequestMapping("/pc/videoLoginIndex")
    public String videoLoginIndex() {
        String turnPage = "pc/yun-jxspnew";
        return turnPage;
    }

    @RequestMapping("/pc/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal Login user) {
        String turnPage = "pc/yun-index";
        if(user instanceof Manager) {
            turnPage = "redirect:/backend/login";
        }else if(user instanceof Editor){
            turnPage = "redirect:/backend/login";
        }
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

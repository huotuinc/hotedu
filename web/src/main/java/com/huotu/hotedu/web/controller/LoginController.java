package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.LinkRepository;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.service.MessageContentService;
import com.huotu.hotedu.service.QaService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
    @Autowired
    LinkRepository linkRepository;
    @Autowired
    QaService qaService;
    @Autowired
    ExamGuideService examGuideService;

    /**
     * 进入登录页面
     * @return login.html
     */
    @RequestMapping("/backend/login")
    public String index(){
        return "backend/index";
    }


    @RequestMapping("/pc/index")
    public String index(Model model) throws Exception{
        String turnPage = "pc/yun-index";
        List<Link> linkList=linkRepository.findAll();
        if(linkList.size()>0) {
            model.addAttribute("LinkList", linkList);
        }
        List<MessageContent> messageContentList=messageContentService.loadPcMessageContent(0,3).getContent();
        for(MessageContent mc:messageContentList){
            mc.setPictureUri(staticResourceService.getResource(mc.getPictureUri()).toString());
        }
        model.addAttribute("MessageContentList",messageContentList);
        List<Qa> qaList=qaService.loadPcQa(0,3).getContent();
        for(Qa qa:qaList){
            qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toString());
        }
        model.addAttribute("QaList",qaList);

        List<ExamGuide> examGuideList=examGuideService.loadPcExamGuide(0,3).getContent();
        for(ExamGuide eg:examGuideList){
            eg.setPictureUri(staticResourceService.getResource(eg.getPictureUri()).toString());
        }
        model.addAttribute("ExamGuideList",examGuideList);
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }


    @RequestMapping("/pc/videoLoginIndex")
    public String videoLoginIndex() {
        String turnPage = "pc/yun-jxspnew";
        return turnPage;
    }

    @RequestMapping("/pc/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal Login user) {
        String turnPage = "redirect:/pc/index";
        if(user instanceof Manager) {
            turnPage = "redirect:/backend/login";
        }else if(user instanceof Editor){
            turnPage = "redirect:/backend/login";
        }
        return turnPage;
    }

    @RequestMapping("/pc/loginFailed")
    public String loginFailed(Model model) throws Exception{
        String returnPage = "pc/yun-index";
        String errInfo = "用户或密码错误";

        List<Link> linkList=linkRepository.findAll();
        if(linkList.size()>0) {
            model.addAttribute("LinkList", linkList);
        }
        List<MessageContent> messageContentList=messageContentService.loadPcMessageContent(0,3).getContent();
        for(MessageContent mc:messageContentList){
            mc.setPictureUri(staticResourceService.getResource(mc.getPictureUri()).toString());
        }
        model.addAttribute("MessageContentList",messageContentList);
        List<Qa> qaList=qaService.loadPcQa(0,3).getContent();
        for(Qa qa:qaList){
            qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toString());
        }
        model.addAttribute("QaList",qaList);

        List<ExamGuide> examGuideList=examGuideService.loadPcExamGuide(0,3).getContent();
        for(ExamGuide eg:examGuideList){
            eg.setPictureUri(staticResourceService.getResource(eg.getPictureUri()).toString());
        }
        model.addAttribute("ExamGuideList",examGuideList);
        model.addAttribute("errInfo",errInfo);
        return returnPage;
    }

}

package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.LinkRepository;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.service.MessageContentService;
import com.huotu.hotedu.service.QaService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
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
    @Autowired
    private  HttpServletRequest request;



    @RequestMapping("/pc/logoutSuccess")
    public String logout() {
        String turnPage = "redirect:/pc/index";
        return turnPage;
    }


    @RequestMapping("/pc/index")
    public String index(Model model) throws Exception{
        String turnPage = "pc/yun-index";
        List<Link> linkList=linkRepository.findAll();
        if(linkList.size()>0) {
            model.addAttribute("linkList", linkList);
        }
        List<MessageContent> messageContentList=messageContentService.loadPcMessageContent(0,4).getContent();
        for(MessageContent mc:messageContentList){
            mc.setPictureUri(staticResourceService.getResource(mc.getPictureUri()).toString());
        }
        model.addAttribute("messageContentList",messageContentList);
        List<Qa> qaList=qaService.loadPcQa(0,3).getContent();
        for(Qa qa:qaList){
            qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toString());
        }
        model.addAttribute("qaList",qaList);

        List<ExamGuide> examGuideList=examGuideService.loadPcExamGuide(0,3).getContent();
        for(ExamGuide eg:examGuideList){
            eg.setPictureUri(staticResourceService.getResource(eg.getPictureUri()).toString());
        }
        model.addAttribute("examGuideList",examGuideList);
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }


    @RequestMapping("/pc/videoLoginIndex")
    public String videoLoginIndex() {
        String turnPage = "pc/yun-jxspnew";
        return turnPage;
    }

    @RequestMapping("/pc/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal Login user,HttpServletRequest request) throws URISyntaxException {
        String turnPage = "redirect:/pc/index";
        if(user instanceof Manager||user instanceof Editor) {
            turnPage = "redirect:/backend/index";
        }
        if(user instanceof Agent) {
            String newPicUrl = null;
            if(((Agent) user).getPictureUri()!=null&&!"".equals(((Agent) user).getPictureUri())) {
                if(staticResourceService.getResource(((Agent) user).getPictureUri())!=null) {
                    newPicUrl = staticResourceService.getResource(((Agent) user).getPictureUri()).toString();
                }
            }
            ((Agent) user).setPictureUri(newPicUrl);
        }
        return turnPage;
    }

    @RequestMapping("/pc/loginFailed")
    public String loginFailed(HttpServletRequest request,Model model) throws Exception{
        String turnPage = "pc/yun-index";
        String errInfo = "用户或密码错误";
        /*String turnPage = "redirect:/pc/index";
        String retUrl = request.getHeader("Referer");
        if(retUrl != null){
            String[] url = retUrl.split("/");
            turnPage="redirect:";
            for (int i = 0 ; i <url.length ; i++ ) {
                if("hotedu".equals(url[i]) || url[i].contains("www")){
                    for(int j=i+1; j<url.length; j++){
                        if(url[j].contains("?")) {
                            turnPage += "/"+url[j].substring(0,url[j].indexOf("?"));
                            break;
                        }
                        turnPage += "/" + url[j];
                    }
                    break;
                }
            }
        }*/
        List<Link> linkList=linkRepository.findAll();
        if(linkList.size()>0) {
            model.addAttribute("linkList", linkList);
        }
        List<MessageContent> messageContentList=messageContentService.loadPcMessageContent(0,3).getContent();
        for(MessageContent mc:messageContentList){
            mc.setPictureUri(staticResourceService.getResource(mc.getPictureUri()).toString());
        }
        model.addAttribute("messageContentList",messageContentList);
        List<Qa> qaList=qaService.loadPcQa(0,3).getContent();
        for(Qa qa:qaList){
            qa.setPictureUri(staticResourceService.getResource(qa.getPictureUri()).toString());
        }
        model.addAttribute("qaList",qaList);

        List<ExamGuide> examGuideList=examGuideService.loadPcExamGuide(0,3).getContent();
        for(ExamGuide eg:examGuideList){
            eg.setPictureUri(staticResourceService.getResource(eg.getPictureUri()).toString());
        }
        model.addAttribute("examGuideList",examGuideList);
        model.addAttribute("errInfo",errInfo);
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }

}

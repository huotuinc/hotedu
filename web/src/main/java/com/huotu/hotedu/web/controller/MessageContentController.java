package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.service.MessageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/25.
 * 资讯动态有关的Controller
 * @author shiliting741@163.com
 */
@Controller
public class MessageContentController {
    @Autowired
    private MessageContentService messageContentService;
    public static final int PAGE_SIZE=10;//每张页面的记录数
    //后台单击考试指南链接显示的消息
    @RequestMapping("/backend/loadMessagecontent")
    public String loadMessageContent(Model model){
        Page<MessageContent> pages=messageContentService.loadMessageContent(0, PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allMessageContentList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/messagecontent";
    }

    //后台单机搜索按钮显示的考试指南消息
    @RequestMapping("/backend/searchMessagecontent")
    public String searchMessageContent(String keywords,Model model){
        Page<MessageContent> pages=messageContentService.searchMessageContent(0, PAGE_SIZE, keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allMessageContentList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/messagecontent";
    }

    //后台单击考试指南的分页
    @RequestMapping("/backend/pageMessagecontent")
    public String pageMessageContent(int n,int sumpage,String keywords,Model model){
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<MessageContent> pages = messageContentService.searchMessageContent(n, PAGE_SIZE, keywords);
        model.addAttribute("allMessageContentList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/messagecontent";
    }

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/delMessagecontent")
    public String delMessageContent(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
        messageContentService.delMessageContent(id);
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;

        }
        sumElement--;
        Page<MessageContent> pages = messageContentService.searchMessageContent(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allMessageContentList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/messagecontent";
    }




    //后台单击新建按钮
    @RequestMapping("/backend/addMessagecontent")
    public String addMessageContent(Model model){
        return "/backend/newmessagecontent";
    }
    //后台单机修改按钮
    @RequestMapping("/backend/modify/messagecontent")
    public String ModifyMessageContent(Long id, Model model){
        MessageContent messageContent=messageContentService.findOneById(id);
        model.addAttribute("messagecontent",messageContent);
        return "/backend/modifymessagecontent";
    }


    //后台单击添加保存按钮
    @RequestMapping("/backend/addSaveMessagecontent")
    public String addSaveMessageContent(String title,String content,String top,Model model){
        MessageContent messageContent=new MessageContent();
        messageContent.setTitle(title);
        messageContent.setContent(content);
        messageContent.setLastUploadDate(new Date());
        messageContent.setTop("1".equals(top)? true:false);
        messageContentService.addMessageContent(messageContent);
        return "redirect:/backend/loadMessagecontent";
    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveMessagecontent")
    public String modifySaveMessageContent(Long id,String title,String content,Boolean top,Model model){
        MessageContent messageContent=messageContentService.findOneById(id);
        messageContent.setTitle(title);
        messageContent.setContent(content);
        messageContent.setTop(top);
        messageContent.setLastUploadDate(new Date());
        messageContentService.modify(messageContent);
        return "redirect:/backend/loadMessagecontent";
    }
}

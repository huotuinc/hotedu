package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.service.MessageContentService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shiLiTing on 2015/6/25.
 * 资讯动态有关的Controller
 * @author shiliting741@163.com
 */
@Controller
public class MessageContentController {
    /**
     * 咨询动态的service层
     */
    @Autowired
    private MessageContentService messageContentService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    /**
     * 用来储存处理静态资源的接口
     */
    @Autowired
    StaticResourceService staticResourceService;



    /**
     * 搜索符合条件的咨询动态信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      messageContent.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/searchMessageContent")
    public String searchMessageContent(@RequestParam(required = false)Integer pageNo,
                                       @RequestParam(required = false) String keywords, Model model){
        String turnPage="/backend/messageContent";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<MessageContent> pages = messageContentService.searchMessageContent(pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = messageContentService.searchMessageContent(pageNo, PAGE_SIZE, keywords);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("allMessageContentList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }


    /**
     * 删除咨询动态信息
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param model         返回客户端集合
     * @return      messageContent.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delMessageContent")
    public String delMessageContent(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords,Long id,Model model){
        String returnPage="redirect:/backend/searchMessageContent";
        messageContentService.delMessageContent(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        return returnPage;
    }

    /**
     * messageContent.html页面单击新建跳转
     * @return newmessageContent.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addMessageContent")
    public String addMessageContent(){

        return "/backend/newMessageContent";
    }

    /**
     * messageContent.html页面点击修改后跳转
     * @param id        需要修改的id
     * @param model     返回客户端集
     * @return      modifyMessageContent.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyMessageContent")
    public String ModifyMessageContent(Long id, Model model) throws Exception{
        MessageContent messageContent=messageContentService.findOneById(id);
        messageContent.setPictureUri(staticResourceService.getResource(messageContent.getPictureUri()).toURL().toString());
        model.addAttribute("messageContent",messageContent);
        return "/backend/modifyMessageContent";
    }

    /**
     * newmessageContent.html页面点击保存添加后跳转
     * @param title     标题
     * @param content   描述
     * @return      不出异常重定向：/backend/searchMessageContent
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping(value="/backend/addSaveMessageContent",method = RequestMethod.POST)
    public String addSaveMessageContent(String title,String synopsis,String content,Boolean top,@RequestParam("smallimg") MultipartFile file) throws Exception{
            //文件格式判断
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            if(file.getSize()==0){throw new Exception("文件为空！");}
            //保存图片
            String fileName = StaticResourceService.MESSAGE_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, file.getInputStream());

            MessageContent messageContent=new MessageContent();
            messageContent.setTitle(title);
            messageContent.setSynopsis(synopsis);
            messageContent.setPictureUri(fileName);
            messageContent.setContent(content);
            messageContent.setLastUploadDate(new Date());
            messageContent.setTop(top);
            messageContentService.addMessageContent(messageContent);
            return "redirect:/backend/searchMessageContent";
    }

    /**
     * modifymessageContent.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param content     描述
     * @param top    是否置顶
     * @return      重定向到：/backend/searchMessagecontent
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveMessageContent")
    public String modifySaveMessageContent(Long id,String title,String content,String synopsis,Boolean top,@RequestParam("smallimg") MultipartFile file) throws Exception{
            MessageContent messageContent=messageContentService.findOneById(id);
        if(file.getSize()!=0){
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            staticResourceService.deleteResource(staticResourceService.getResource(messageContentService.findOneById(id).getPictureUri()));
            String fileName = StaticResourceService.MESSAGE_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName,file.getInputStream());
            messageContent.setPictureUri(fileName);
        }
            messageContent.setTitle(title);
            messageContent.setSynopsis(synopsis);
            messageContent.setContent(content);
            messageContent.setTop(top);
            messageContent.setLastUploadDate(new Date());
            messageContentService.modify(messageContent);
            return "redirect:/backend/searchMessageContent";
    }

    /**
     * Created by jiashubing on 2015/8/11.
     * 前台加载考试指南
     * @param pageNo    第几页
     * @param model     返回客户端集
     * @return          yun-dongtaizx.html
     */
    @RequestMapping("/pc/loadMessageContent")
    public String loadMessageContent(@RequestParam(required = false)Integer pageNo,Model model) throws Exception{
        String turnPage="/pc/yun-dongtaizx";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<MessageContent> pages = messageContentService.loadPcMessageContent(pageNo, PAGE_SIZE);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = messageContentService.loadPcMessageContent(pageNo, PAGE_SIZE);
            totalRecords = pages.getTotalElements();
        }

        for(MessageContent examGuide : pages){
            examGuide.setPictureUri(staticResourceService.getResource(examGuide.getPictureUri()).toURL().toString());
        }

        Date today = new Date();
        model.addAttribute("allMessageContentList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("today", today);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }

    /**
     * 加载每一条资讯动态的详细信息
     * @param id    要查看的资讯动态的id
     * @param model 返回客户端集
     * @return      yun-xqzixun.html
     */
    @RequestMapping("/pc/loadDetailMessageContent")
    public String loadDetailMessageContent(Long id,Model model) throws Exception{
        String turnPage="/pc/yun-xqzixun";
        MessageContent messageContent = messageContentService.findOneById(id);
        messageContent.setPictureUri(staticResourceService.getResource(messageContent.getPictureUri()).toURL().toString());
        model.addAttribute("messageContent",messageContent);
        model.addAttribute("flag","yun-index.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }
}

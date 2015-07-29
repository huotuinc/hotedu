package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.service.MessageContentService;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by shiliting on 2015/6/25.
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

//    /**
//     * 显示咨询动态信息
//     * @param model 返回客户端集
//     * @return  messagecontent.html
//     */
//    @RequestMapping("/backend/loadMessagecontent")
//    public String loadMessageContent(Model model){
//        Page<MessageContent> pages=messageContentService.loadMessageContent(0, PAGE_SIZE);
//        long sumElement=pages.getTotalElements();
//        model.addAttribute("allMessageContentList",pages);
//        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
//        model.addAttribute("n",0);
//        model.addAttribute("keywords","");
//        model.addAttribute("sumElement",sumElement);
//        return "/backend/messagecontent";
//    }

    /**
     * 搜索符合条件的咨询动态信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      messagecontent.html
     */
    @RequestMapping("/backend/searchMessagecontent")
    public String searchMessageContent(@RequestParam(required = false)Integer pageNo,
                                       @RequestParam(required = false)Integer pageSize,
                                       @RequestParam(required = false) String keywords, Model model){
        String turnPage="/backend/messagecontent";
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

//    /**
//     * 分页显示
//     * @param n             显示第几页
//     * @param sumpage    分页总页数
//     * @param keywords      检索关键字(使用检索功能后有效)
//     * @param model         返回客户端集合
//     * @return          messagecontent.html
//     */
//    @RequestMapping("/backend/pageMessagecontent")
//    public String pageMessageContent(int n,int sumpage,String keywords,Model model){
//        //如果已经到分页的第一页了，将页数设置为0
//        if (n < 0){
//            n++;
//        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
//            n--;
//        }
//        Page<MessageContent> pages = messageContentService.searchMessageContent(n, PAGE_SIZE, keywords);
//        model.addAttribute("allMessageContentList",pages);
//        model.addAttribute("sumpage",sumpage);
//        model.addAttribute("n",n);
//        model.addAttribute("keywords",keywords);
//        model.addAttribute("sumElement",pages.getTotalElements());
//        return "/backend/messagecontent";
//    }

    /**
     * 删除咨询动态信息
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param model         返回客户端集合
     * @return      messagecontent.html
     */
    @RequestMapping("/backend/delMessagecontent")
    public String delMessageContent(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords,Long id,Model model){
        String returnPage="redirect:/backend/searchMessagecontent";
        messageContentService.delMessageContent(id);
        return returnPage;
    }

    /**
     * messagecontent.html页面单击新建跳转
     * @return newmessagecontent.html
     */
    @RequestMapping("/backend/addMessagecontent")
    public String addMessageContent(Model model){
        return "/backend/newmessagecontent";
    }

    /**
     * messagecontent.html页面点击修改后跳转
     * @param id        需要修改的id
     * @param model     返回客户端集
     * @return      modifymessagecontent.html
     */
    @RequestMapping("/backend/modify/messagecontent")
    public String ModifyMessageContent(Long id, Model model){
        MessageContent messageContent=messageContentService.findOneById(id);
        model.addAttribute("messagecontent",messageContent);
        return "/backend/newmessagecontent";
    }

    /**
     * newmessagecontent.html页面点击保存添加后跳转
     * @param title     标题
     * @param content   描述
     * @return      不出异常重定向：/backend/loadMessagecontent
     */
    //TODO 是否搞抛出异常
    @RequestMapping(value="/backend/addSaveMessagecontent",method = RequestMethod.POST)
    public String addSaveMessageContent(String title,String content,String top,@RequestParam("smallimg") MultipartFile file) throws Exception{
        try {
            //文件格式判断
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            System.out.println("文件大小：" + file.getSize());
            if(file.getSize()==0){throw new Exception("文件为空！");}
            if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}

            //保存图片
            String fileName = StaticResourceService.MESSAGECONTENT_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, file.getInputStream());

            MessageContent messageContent=new MessageContent();
            messageContent.setTitle(title);
            messageContent.setPictureUri(fileName);
            messageContent.setContent(content);
            messageContent.setLastUploadDate(new Date());
            messageContent.setTop("1".equals(top)? true:false);
            messageContentService.addMessageContent(messageContent);
            return "redirect:/backend/loadMessagecontent";

        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/backend/error";

    }

    /**
     * modifymessagecontent.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param content     描述
     * @param top    是否置顶
     * @return      重定向到：/backend/loadMessagecontent
     */
    @RequestMapping("/backend/modifySaveMessagecontent")
    public String modifySaveMessageContent(Long id,String title,String content,Boolean top,@RequestParam("smallimg") MultipartFile file) throws Exception{
        try {
            //文件格式判断
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            System.out.println("文件大小：" + file.getSize());
            if(file.getSize()==0){throw new Exception("文件为空！");}
            if(file.getSize()>1024*1024*5){throw new Exception("文件太大");}

            //保存图片
            String fileName = StaticResourceService.MESSAGECONTENT_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, file.getInputStream());
            MessageContent messageContent=messageContentService.findOneById(id);
            messageContent.setTitle(title);
            messageContent.setContent(content);
            messageContent.setTop(top);
            messageContent.setLastUploadDate(new Date());
            messageContentService.modify(messageContent);
            return "redirect:/backend/loadMessagecontent";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/backend/error";
    }
}

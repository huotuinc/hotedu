package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Link;
import com.huotu.hotedu.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 友情链接有关的Controller
 * @author jiashubing
 */
@Controller
public class LinkController {
    /**
     * 友情链接的service层
     */
    @Autowired
    private LinkService linkService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    /**
     * 搜索符合条件的友情链接信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      link.html
     */
    @RequestMapping("/backend/searchLink")
    public String searchLinkController(@RequestParam(required = false)Integer pageNo,
                                       @RequestParam(required = false) String keywords, Model model) {
        String turnPage="/backend/link";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Link> pages = linkService.searchLink(pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = linkService.searchLink(pageNo, PAGE_SIZE, keywords);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("allLinkList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;


    }



     /**
     * 删除友情链接信息
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param model         返回客户端集合
     * @return      link.html
     */
    @RequestMapping("/backend/delLink")

    public String delLink(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords, Long id, Model model) {
        String returnPage="redirect:/backend/searchLink";
        linkService.delLink(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        return returnPage;
    }

    /**
     * link.html页面单击新建跳转
     * @return newlink.html
     */
    @RequestMapping("/backend/addLink")
    public String AddLink(){
        return "/backend/newlink";
    }

    /**
     * link.html页面点击修改后跳转
     * @param id        需要修改的id
     * @param model     返回客户端集
     * @return      modifylink.html
     */
    @RequestMapping("/backend/modifyLink")
    public String ModifyLink(Long id, Model model){
        Link link=linkService.findOneById(id);
        model.addAttribute("link",link);
        return "/backend/modifylink";
    }

    /**
     * newlink.html页面点击保存添加后跳转
     * @param title     标题
     * @param url   url
     * @return      不出异常重定向：/backend/searchLink
     */
    //TODO 是否搞抛出异常
    @RequestMapping("/backend/addSaveLink")
    public String AddSaveLink(String title,String url){
        Link link=new Link();
        link.setTitle(title);
        link.setUrl(url);
        link.setLastUploadDate(new Date());
        linkService.addLink(link);
        return "redirect:/backend/searchLink";
    }

    /**
     * modifylink.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param url     url
     * @return      重定向到：/backend/searchLink
     */
    @RequestMapping("/backend/modifySaveLink")
    public String ModifySaveLink(Long id,String title,String url){
        Link link=linkService.findOneById(id);
        link.setTitle(title);
        link.setUrl(url);
        link.setLastUploadDate(new Date());
        linkService.modify(link);
        return "redirect:/backend/searchLink";
    }
}

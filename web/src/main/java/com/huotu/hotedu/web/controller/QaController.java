package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * 常见问题有关的Controller
 * @author shiliting shiliting741@163.com
 */
@Controller
public class QaController {

    /**
     * 常见问题的service层
     */
    @Autowired
    private QaService qaService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;



    /**
     * 搜索符合条件的常见问题信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      qa.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/searchQa")
    public String searchQa(@RequestParam(required = false)Integer pageNo,
                           @RequestParam(required = false) String keywords, Model model) {
        String turnPage="/backend/qa";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        Page<Qa> pages = qaService.searchQa(pageNo, PAGE_SIZE, keywords);
        long totalRecords = pages.getTotalElements();
        int numEl =  pages.getNumberOfElements();
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = qaService.searchQa(pageNo, PAGE_SIZE, keywords);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("allQaList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }



    /**
     * 删除视频信息
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param model         返回客户端集合
     * @return      video.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/delQa")
    public String delQa(@RequestParam(required = false)Integer pageNo,@RequestParam(required = false)String keywords, Long id, Model model) {
        String returnPage="redirect:/backend/searchQa";
        qaService.delQa(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        return returnPage;
    }

    /**
     * qa.html页面单击新建跳转
     * @return newqa.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addQa")
    public String addQa(){
        return "/backend/newqa";
    }

    /**
     * qa.html页面点击修改后跳转
     * @param id        需要修改的id
     * @param model     返回客户端集
     * @return      modifyqa.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifyQa")
    public String modifyQa(Long id, Model model){
        Qa qa=qaService.findOneById(id);
        model.addAttribute("qa",qa);
        return "/backend/modifyqa";
    }

    /**
     * newqa.html页面点击保存添加后跳转
     * @param title     标题
     * @param content   描述
     * @return      不出异常重定向：/backend/searchQa
     */
    //TODO 是否搞抛出异常
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/addSaveQa")
    public String addSaveQa(String title,String content,String top){
        Qa qa=new Qa();
        qa.setTitle(title);
        qa.setContent(content);
        qa.setLastUploadDate(new Date());
        qa.setTop("1".equals(top));
        qaService.addQa(qa);
        return "redirect:/backend/searchQa";
    }

    /**
     * modifyqa.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param content     描述
     * @param top    是否置顶
     * @return      重定向到：/backend/searchQa
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/modifySaveQa")
    public String modifySaveQa(Long id,String title,String content,Boolean top){
        Qa qa=qaService.findOneById(id);
        qa.setTitle(title);
        qa.setContent(content);
        qa.setTop(top);
        qa.setLastUploadDate(new Date());
        qaService.modify(qa);
        return "redirect:/backend/searchQa";
    }

}

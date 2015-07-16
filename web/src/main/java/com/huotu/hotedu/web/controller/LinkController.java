package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Link;
import com.huotu.hotedu.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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
     * 显示友情链接信息
     * @param model 返回客户端集
     * @return  link.html
     */
    @RequestMapping("/backend/loadLink")
    public String loadExamGuide(Model model){
        Page<Link> pages=linkService.loadLink(0,PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allLinkList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/link";
    }

    /**
     * 搜索符合条件的友情链接信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      link.html
     */
    @RequestMapping("/backend/searchLink")
    public String searchLinkController(String keywords,Model model) {

        Page<Link> pages=linkService.searchLink(0, PAGE_SIZE, keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allLinkList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/link";

    }

    /**
     * 分页显示
     * @param n             显示第几页
     * @param sumpage    分页总页数
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param model         返回客户端集合
     * @return          link.html
     */
    @RequestMapping("/backend/pageLink")
    public String pageLink(int n,int sumpage,String keywords,Model model){
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Link> pages = linkService.searchLink(n, PAGE_SIZE, keywords);
        model.addAttribute("allLinkList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/link";
    }

     /**
     * 删除友情链接信息
     * @param n             显示第几页
     * @param sumpage       分页总页数
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param sumElement    总记录数
     * @param model         返回客户端集合
     * @return      link.html
     */
    @RequestMapping("/backend/delLink")
    public String delLink(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
        linkService.delLink(id);
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;
        }
        sumElement--;
        Page<Link> pages = linkService.searchLink(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allLinkList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/link";
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
    @RequestMapping("/backend/modify/link")
    public String ModifyLink(Long id, Model model){
        Link link=linkService.findOneById(id);
        model.addAttribute("link",link);
        return "/backend/modifylink";
    }

    /**
     * newlink.html页面点击保存添加后跳转
     * @param title     标题
     * @param url   url
     * @return      不出异常重定向：/backend/loadLink
     */
    //TODO 是否搞抛出异常
    @RequestMapping("/backend/addSaveLink")
    public String AddSaveLink(String title,String url){
        Link link=new Link();
        link.setTitle(title);
        link.setUrl(url);
        link.setLastUploadDate(new Date());
        linkService.addLink(link);
        return "redirect:/backend/loadLink";
    }

    /**
     * modifylink.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param url     url
     * @return      重定向到：/backend/loadLink
     */
    @RequestMapping("/backend/modifySaveLink")
    public String ModifySaveLink(Long id,String title,String url){
        Link link=linkService.findOneById(id);
        link.setTitle(title);
        link.setUrl(url);
        link.setLastUploadDate(new Date());
        linkService.modify(link);
        return "redirect:/backend/loadLink";
    }
}

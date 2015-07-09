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
    @Autowired
    private LinkService linkService;
    public static final int PAGE_SIZE=10;

    //后台单击友情链接链接显示的消息
    @RequestMapping("/backend/load/link")
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

    //后台显示检索之后的友情链接
    @RequestMapping("/backend/search/link")
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

    //后台单击友情链接的分页
    @RequestMapping("/backend/page/link")
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

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/del/link")
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

    //后台单击新建按钮
    @RequestMapping("/backend/add/link")
    public String AddLink(Model model){
        return "/backend/newlink";
    }

    //后台单击修改按钮
    @RequestMapping("/backend/modify/link")
    public String ModifyLink(Long id, Model model){
        Link link=linkService.findOneById(id);
        model.addAttribute("link",link);
        return "/backend/modifylink";
    }

    //后台单击添加保存按钮
    @RequestMapping("/backend/addsave/link")
    public String AddSaveLink(String title,String url,Model model){
        Link link=new Link();
        link.setTitle(title);
        link.setUrl(url);
        link.setLastUploadDate(new Date());
        linkService.addLink(link);
        return "redirect:/backend/load/link";
    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifysave/link")
    public String ModifySaveLink(Long id,String title,String url,Model model){
        Link link=linkService.findOneById(id);
        link.setTitle(title);
        link.setUrl(url);
        link.setLastUploadDate(new Date());
        linkService.modify(link);
        return "redirect:/backend/load/link";
    }
}

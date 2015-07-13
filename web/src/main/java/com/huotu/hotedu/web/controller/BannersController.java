package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Banners;
import com.huotu.hotedu.service.BannersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * Created by shiliting on 2015/6/10.
 * Banner图有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
 public class BannersController {
    @Autowired
    private BannersService BannersService;
    public static final int PAGE_SIZE=10;//每张页面的记录数


    //后台单击banners链接显示的消息
    @RequestMapping("/backend/loadBanners")
    public String loadBanners(Model model){
        Page<Banners> pages=BannersService.loadBanners(0, PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allbannersList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/banners";
    }

    //后台单机搜索按钮显示的banners消息
    @RequestMapping("/backend/searchBanners")
    public String searchBanners(String keywords,Model model){
        Page<Banners> pages=BannersService.searchBanners(0, PAGE_SIZE, keywords);
        long sumElement=pages.getTotalElements();
        model.addAttribute("allbannersList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/loadbanners";
    }

    //后台单击banners的分页
    @RequestMapping("/backend/pageBanners")
    public String pageBanners(int n,int sumpage,String keywords,Model model){
        //如果已经到分页的第一页了，将页数设置为0
        if (n < 0){
            n++;
        }else if(n + 1 > sumpage){//如果超过分页的最后一页了，将页数设置为最后一页
            n--;
        }
        Page<Banners> pages = BannersService.searchBanners(n, PAGE_SIZE, keywords);
        model.addAttribute("allbannersList",pages);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",pages.getTotalElements());
        return "/backend/banners";
    }

    //后台单击删除按钮返回的信息
    @RequestMapping("/backend/delBanners")
    public String delBanners(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
        BannersService.delBanners(id);
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;
        }
        sumElement--;
        Page<Banners> pages =BannersService.searchBanners(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allbannersList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/banners";
    }




    //后台单击新建按钮
    @RequestMapping("/backend/addbanners")
    public String addBanners(Model model){
        return "/backend/newbanners";
    }

    //后台单机修改按钮
    @RequestMapping("/backend/modifyBanners")
    public String ModifyBanners(Long id, Model model){
        Banners banners=BannersService.findOneById(id);
        model.addAttribute("banners",banners);
        return "/backend/modifybanners";
    }


    //后台单击添加保存按钮
    @RequestMapping("/backend/addSaveBanners")
    public String addSaveBanners(String title,String content,String top,Model model){
        Banners banners=new Banners();
        banners.setTitle(title);
        banners.setContent(content);
        banners.setLastUploadDate(new Date());
        banners.setTop("1".equals(top) ? true : false);
        BannersService.addBanners(banners);
        return "redirect:/backend/loadBanners";
    }


    //后台单击修改保存按钮
    @RequestMapping("/backend/modifySaveBanners")
    public String modifySaveBanners(Long id,String title,String content,Boolean top,Model model){
        Banners banners=BannersService.findOneById(id);
        banners.setTitle(title);
        banners.setContent(content);
        banners.setTop(top);
        banners.setLastUploadDate(new Date());
        BannersService.modify(banners);
        return "redirect:/backend/loadBanners";
    }
}


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
    /**
     * Banner图的service层
     */
    @Autowired
    private BannersService BannersService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    /**
     * 显示Banner图信息
     * @param model 返回客户端集
     * @return  banner.html
     */
    @RequestMapping("/backend/loadBanners")
    public String loadBanners(Model model){
        Page<Banners> pages=BannersService.loadBanners(0, PAGE_SIZE);
        long sumElement=pages.getTotalElements();
        System.out.println(sumElement);
        model.addAttribute("allbannersList",pages);
        model.addAttribute("sumpage",sumElement/pages.getSize()+(sumElement%pages.getSize()>0? 1:0));
        model.addAttribute("n",0);
        model.addAttribute("keywords","");
        model.addAttribute("sumElement",sumElement);
        return "/backend/banners";
    }

    /**
     * 搜索符合条件的Banner图信息
     * @param keywords  搜索关键字
     * @param model     返回客户端参数集
     * @return      banner.html
     */
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

    /**
     * 分页显示
     * @param n             显示第几页
     * @param sumpage    分页总页数
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param model         返回客户端集合
     * @return          banner.html
     */
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

    /**
     * 删除Banner图信息
     * @param n             显示第几页
     * @param sumpage       分页总页数
     * @param keywords      检索关键字(使用检索功能后有效)
     * @param id            需要被删除的记录id
     * @param sumElement    总记录数
     * @param model         返回客户端集合
     * @return      banner.html
     */
    @RequestMapping("/backend/delBanners")
    public String delBanners(int n,int sumpage,String keywords,Long id,Long sumElement,Model model){
        BannersService.delBanners(id);
        if((sumElement-1)%PAGE_SIZE==0){
            if(n>0&&n+1==sumpage){n--;}
            sumpage--;
        }
        sumElement--;
        Page<Banners> pages = BannersService.searchBanners(n, PAGE_SIZE, keywords);
        model.addAttribute("sumpage",sumpage);
        model.addAttribute("allbannersList",pages);
        model.addAttribute("n",n);
        model.addAttribute("keywords",keywords);
        model.addAttribute("sumElement",sumElement);
        return "/backend/banners";
    }

    /**
     * banners.html页面单击新建跳转
     * @return newbanner.html
     */
    @RequestMapping("/backend/addbanners")
    public String addBanners(){
        return "/backend/newbanner";
    }

    /**
     * banners.html页面点击修改后跳转
     * @param id        需要修改的id
     * @param model     返回客户端集
     * @return      modifybanners.html
     */
    @RequestMapping("/backend/modifyBanners")
    public String ModifyBanners(Long id, Model model){
        Banners banners=BannersService.findOneById(id);
        model.addAttribute("banners",banners);
        return "/backend/modifybanners";
    }

    /**
     * newbanner.html页面点击保存添加后跳转
     * @param title     标题
     * @param content   url
     * @return      不出异常重定向：/backend/loadBanners
     */
    //TODO 是否搞抛出异常
    @RequestMapping("/backend/addSaveBanners")
    public String addSaveBanners(String title,String content){
        Banners banners=new Banners();
        banners.setTitle(title);
        banners.setContent(content);
        banners.setLastUploadDate(new Date());
        BannersService.addBanners(banners);
        return "redirect:/backend/loadBanners";
    }

    /**
     * modifybanner.html页面点击保存修改后跳转
     * @param id    修改后的id
     * @param title     标题
     * @param content   url
     * @return      重定向到：/backend/loadBanners
     */
    @RequestMapping("/backend/modifySaveBanners")
    public String modifySaveBanners(Long id,String title,String content){
        Banners banners=BannersService.findOneById(id);
        banners.setTitle(title);
        banners.setContent(content);
        banners.setLastUploadDate(new Date());
        BannersService.modify(banners);
        return "redirect:/backend/loadBanners";
    }
}


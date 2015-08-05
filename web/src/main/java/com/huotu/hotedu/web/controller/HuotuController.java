package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Huotu;
import com.huotu.hotedu.service.HuotuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 火图公司有关的Controller
 * @author jiashubing
 */
@Controller
public class HuotuController {
    /**
     * 公司简介的service层
     */
    @Autowired
    private HuotuService huotuService;

    /**
     * 显示公司简介信息
     * @param model 返回客户端集
     * @return  editenterprise.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/loadEditenterprise")
    public String loadHuotu(Model model){
        List<Huotu> list=huotuService.findHuotu();
        if(list.size()==0){
            Huotu huotu=new Huotu();
            huotu.setTitle("");
            huotu.setIntroduction("");
            list.add(huotu);
        }
        model.addAttribute("Huotu",list);
        return "/backend/editenterprise";
    }

    /**
     * 添加保存公司信息
     * @param title 公司标题
     * @param introduction  公司描述
     * @param model 返回客户端集
     * @return  重定向到：/backend/loadEditenterprise
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/saveEditenterprise")
    //TODO 方法名应该以小写开头 by CJ(已经解决)
    public String saveHuotu(String title,String introduction,Model model){
        Huotu huotu=null;
        List<Huotu> list=huotuService.findHuotu();
        if(list.size()==0){
            huotu=new Huotu();
        }else{
            for(Huotu h:list){
                huotu=h;
            }
        }
        huotu.setTitle(title);
        huotu.setIntroduction(introduction);
        huotuService.modifyHuotu(huotu);
        return "redirect:/backend/loadEditenterprise";
    }
}

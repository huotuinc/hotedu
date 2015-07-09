package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Huotu;
import com.huotu.hotedu.service.HuotuService;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private HuotuService huotuService;
    //后台单击公司简介的消息
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


    //后台单击添加保存按钮
    @RequestMapping("/backend/saveEditenterprise")
    //TODO 方法名应该以小写开头 by CJ
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

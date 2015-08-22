package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/8/22.
 * 主要处理招聘应聘方面的Controller
 * @author shiliting shiliting741@163.com
 */
@Controller
public class RecruitmentController {
    /**
     * Create by shiliting on 2015.8.22
     * 进入招聘页面
     * @param model  用来给前台确定当前是哪个页面
     * @return
     */
    @RequestMapping("/pc/loadRecruitment")
    public String loadRecruitment(Model model) {
        String turnPage = "pc/yun-sorry";
        model.addAttribute("flag","yun-sorry.html");  //此属性用来给前台确定当前是哪个页面
        return turnPage;
    }



}

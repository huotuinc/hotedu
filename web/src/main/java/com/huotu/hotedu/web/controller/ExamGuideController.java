package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.service.ExamGuideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luffy on 2015/6/10.
 * 考试指南的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class ExamGuideController {
    @Autowired
    private ExamGuideService examGuideService;
    //后台显示检索之后考试指南信息
    @RequestMapping("/backend/load/examGuide")
    public String loadExamGuide(Model model){

        model.addAttribute("allGuideList",examGuideService.searchExamGuide(0,3));
        return "/backend/guides";
    }


    //后台显示检索之后考试指南信息
    @RequestMapping("/backend/search/guides")
    public String searchGuidesController() {
        return "";
    }

}

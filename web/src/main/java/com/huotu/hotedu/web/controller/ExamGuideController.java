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
 *
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class ExamGuideController {
    @Autowired
    private ExamGuideService examGuideService;

    @RequestMapping("/load/examGuide")
    public String loadExamGuide(Model model){
        model.addAttribute("list",examGuideService.loadExamGuide());
//        System.out.println("进入/load/examGuide");
//        Map model=new HashMap<>();
//        List<ExamGuide> list=examGuideService.loadExamGuide();
//        model.put("list",list);
//        model.put("slt","456");
//        return new ModelAndView("/guides",model);
        return "/guides";
    }
}

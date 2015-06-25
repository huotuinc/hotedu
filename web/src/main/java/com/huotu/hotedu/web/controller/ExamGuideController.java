package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class ExamGuideController {

    @RequestMapping("/load/examGuide")
    public String loadExamGuide(){
        return "/guides";
    }
}

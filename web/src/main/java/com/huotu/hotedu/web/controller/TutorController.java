package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 师资力量有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class TutorController {
    //后台显示所有师资力量
    @RequestMapping("/backend/load/tutor")
    public String loadTutorsController() {
        return "/backend/tutor";

    }

    //后台显示检索之后的师资力量
    @RequestMapping("/backend/search/tutor")
    public String searchTutorsController() {
        return "";
    }

}

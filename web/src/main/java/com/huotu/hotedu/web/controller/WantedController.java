package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 招聘信息有关的Controller
 */
@Controller
public class WantedController {
    //后台显示所有招聘信息
    @RequestMapping("/backend/load/wanted")
    public String loadWantedesController() {
        return "/backend/wanted";
    }

    //后台显示检索之后的招聘信息
    @RequestMapping("/backend/search/wanted")
    public String searchWantedesController() {
        return "";
    }

}

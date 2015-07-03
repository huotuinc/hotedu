package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 招聘信息有关的Controller
 */
@Controller
public class WantedesController {
    //后台显示所有招聘信息
    @RequestMapping("/backend/load/wanteds")
    public String loadWantedesController() {
        return "/backend/wanteds";
    }

    //后台显示检索之后的招聘信息
    @RequestMapping("/backend/search/wanteds")
    public String searchWantedesController() {
        return "";
    }

}

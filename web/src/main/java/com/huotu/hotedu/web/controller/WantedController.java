package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 招聘信息有关的Controller
 */
@Controller
public class WantedController {
    //后台显示所有招聘信息

    /**
     * 显示招聘信息
     * @return  wanted.html
     */
    @RequestMapping("/backend/loadWanted")
    public String loadWantedesController() {
        return "/backend/wanted";
    }

    /**
     * 搜索符合条件的招聘信息
     * @return  wanted.html
     */
    @RequestMapping("/backend/searchWanted")
    public String searchWantedesController() {
        return "";
    }

}

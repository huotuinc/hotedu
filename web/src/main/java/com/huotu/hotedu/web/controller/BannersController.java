package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * Banner图有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class BannersController {
    //后台显示所有Banner图
    @RequestMapping("/backend/load/banners")
    public String loadBannersController() {
        return "/backend/banners";
    }


    //后台显示检索过之后的Banner图
    @RequestMapping("/backend/search/banners")
    public String searchBannersController() {
        return "";
    }


}

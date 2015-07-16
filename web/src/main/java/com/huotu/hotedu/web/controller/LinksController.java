package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 友情链接有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class LinksController {
    //后台显示所有友情链接
    @RequestMapping("/backend/load/links")
    public String loadLinksController() {
        return "/backend/links";

    }

    //后台显示检索之后的友情链接
    @RequestMapping("/backend/search/links")
    public String searchLinksController() {
        return "";
    }

}

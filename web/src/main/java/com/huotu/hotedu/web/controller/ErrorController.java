package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 出错时跳转的页面Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class ErrorController {
    @RequestMapping("/backend/error")
    public String loadBannersController() {
        return "/backend/error";
    }
}

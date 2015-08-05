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

    /**
     * 出错时跳转
     * @return error.html
     */
    @RequestMapping("/error")
    public String loadBannersController() {
        return "/pc/yun-404.html";
    }

    @RequestMapping("/backend/errorTest")
    public String errorTest() throws Exception{
        throw new  Exception("测试错误！");
    }
}

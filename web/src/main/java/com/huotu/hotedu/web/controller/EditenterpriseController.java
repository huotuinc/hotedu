package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 师资力量有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class EditenterpriseController {
    //后台显示所有公司简介
    @RequestMapping("/backend/load/editenterprise")
    public String loadEditenterpriseController() {
        return "/backend/editenterprise";
    }

}

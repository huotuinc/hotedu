package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 招聘信息有关的Controller
 * TODO 按照驼峰命名规则 此处名字应该是ChangeThePasswordController吧？ by CJ
 */
@Controller
public class ChangethepasswordController {
    //后台显示所有修改密码信息
    @RequestMapping("/backend/load/changethepassword")
    public String loadWantedesController() {
        return "/backend/changethepassword";
    }

}

package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/8/3.
 * 修改密码Controller
 * @author shiliting
 */
@Controller
public class ChangeThePasswordController {
    @Autowired
    LoginService loginService;

    /**
     * 修改密码
     * @return backend/index.html
     */
    @RequestMapping("/backend/loadChangeThePassword")
    public String loadChangeThePassword(@AuthenticationPrincipal Login user) {
        String returnPage="redirect:/backend/changethepassword";
        return returnPage;
    }
}

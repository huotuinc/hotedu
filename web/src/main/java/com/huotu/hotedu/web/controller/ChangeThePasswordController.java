package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by shiliting on 2015/8/3.
 * 修改密码Controller
 * @author shiliting
 */
@Controller
public class ChangeThePasswordController {
    @Autowired
    LoginService loginService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 修改密码
     * @return backend/index.html
     */
    @PreAuthorize("hasRole('EDITOR')")
    @RequestMapping("/backend/loadChangeThePassword")
    public String loadChangeThePassword() {
        String turnPage="/backend/changethepassword";
        return turnPage;
    }

    @RequestMapping("/backend/changePassword")
    @ResponseBody
    public Result changePassword(@AuthenticationPrincipal Login user, CharSequence oldPd,String newPd){
        Result result=new Result();
        if(passwordEncoder.matches(oldPd,user.getPassword())){
            loginService.newLogin(user,newPd);
            result.setStatus(0);
            result.setMessage("密码修改成功！");
        }else{
            result.setStatus(1);
            result.setMessage("原密码错误！");
        }
        return result;
    }


}

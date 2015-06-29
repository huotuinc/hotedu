package com.huotu.hotedu.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 代理商有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class CertificateapplicationsController {
    //后台显示所有代理商列表
    @RequestMapping("/backend/load/certificateapplications")
    public String loadCertificateapplicationsController() {
        return "/backend/certificateapplications";
    }


    //后台显示检索之后的代理商
    @RequestMapping("/backend/search/agents")
    public String searchAgentsController() {
        return "";
    }



}

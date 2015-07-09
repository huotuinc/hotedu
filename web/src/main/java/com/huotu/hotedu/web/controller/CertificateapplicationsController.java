package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 代理商有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class CertificateapplicationsController {
    //后台显示所有申请领证的信息
    @RequestMapping("/backend/loadCertificateapplications")
    public String loadCertificateapplicationsController() {
        return "/backend/certificateapplications";
    }

    //后台显示所有代理商信息
    @RequestMapping("/backend/loadAgents")
    public String loaDagentsController() {
        return "/backend/agents";
    }

    //后台显示所有代理商信息的班级信息
    @RequestMapping("/backend/loadAgent")
    public String loadDagentController() {
        return "/backend/agent";
    }


    //后台显示检索之后的代理商
    @RequestMapping("/backend/searchAgents")
    public String searchAgentsController() {
        return "";
    }

    //后台显示检索之后的代理商班级信息
    @RequestMapping("/backend/searchAgent")
    public String searchAgentController() {
        return "";
    }


}

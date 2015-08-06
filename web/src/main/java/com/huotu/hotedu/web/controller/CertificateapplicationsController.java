package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 代理商有关的Controller,主要处理与代理商有关的请求
 * Created by shiliting on 2015/6/10.
 * @Time 2015-6-10
 * @author shiliting  shiliting741@163.com
 * //TODO 代理商模块有待完善 by shiliting
 */
@Controller
public class CertificateapplicationsController {
    /**
     * 接收一个请求，用来显示申请领证的信息，然后返回给客户端一个HTML页面
     * @return certificateapplications.html
     */
    @RequestMapping("/backend/loadCertificateapplications")
    public String loadCertificateapplicationsController() {
        return "/backend/certificateapplications";
    }

    /**
     * 接收一个请求，用来显示多条代理商信息，然后返回给客户端一个HTML页面
     * @return agents.html
     */
    @RequestMapping("/backend/loadAgents")
    public String loaDagentsController() {
        return "/backend/agents";
    }


    /**
     * 接收一个请求，用来显示某个代理商信息，然后返回给客户端一个HTML页面
     * @return agent.html
     */
    @RequestMapping("/backend/loadAgent")
    public String loadDagentController() {
        return "/backend/agent";
    }


    //后台显示检索之后的代理商
//
//    /**
//     * 接收一个检索代理商的请求，显示检索之后的多条代理商信息，然后返回给客户端一个HTML页面
//     * @return
//     */
//    @RequestMapping("/backend/searchAgents")
//    public String searchAgentsController() {
//        return "";
//    }

    //后台显示检索之后的代理商班级信息

//    /**
//     * 接收一个检索代理商班级的请求，显示检索之后的代理商班级的信息，然后返回给客户端一个HTML页面
//     * @return
//     */
//    @RequestMapping("/backend/searchAgent")
//    public String searchAgentController() {
//        return "";
//    }


}

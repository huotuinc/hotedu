package com.huotu.hotedu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shiliting on 2015/6/10.
 * 企业有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class EnterprisesController {

    //后台显示所有招聘信息
    @RequestMapping("/backend/load/wantedes")
    public String loadWantedesController() {
        return "/backend/wantedes";
    }


    //后台显示所有发布企业的信息
    @RequestMapping("/backend/load/enterprises")
    public String loadEnterprisesController() {
        return "/backend/enterprises";
    }




    //后台显示检索之后的招聘信息
    @RequestMapping("/backend/search/wantedes")
    public String searchWantedesController() {
        return "";
    }
    //后台显示检索之后的发布企业信息
    @RequestMapping("/backend/search/enterprises")
    public String searchEnterprisesController() {
        return "";
    }
}

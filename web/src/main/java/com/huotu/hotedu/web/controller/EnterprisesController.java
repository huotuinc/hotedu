package com.huotu.hotedu.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shiliting on 2015/6/10.
 * 企业有关的Controller
 * @author shiliting shiliting at gmail.com
 */
@Controller
public class EnterprisesController {

    @RequestMapping("/backend/load/wantedes")
    public String loadWantedesController() {
        return "/backend/wantedes";

    }
}

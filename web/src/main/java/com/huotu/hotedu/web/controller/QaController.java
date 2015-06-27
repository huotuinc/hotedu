package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.service.QaService;
import com.sun.javafx.collections.MappingChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class QaController {
    @Autowired
    private QaService qaService;

    @RequestMapping("/load/qa")
    public ModelAndView loadQa() {
        Map model=new HashMap<>();
        List<Qa>list=qaService.loadQa();
        model.put("list",list);
        return new ModelAndView("/backend/qa",model);
    }
}

package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.service.MessageContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class MessageContentController {
    @Autowired
    private MessageContentService messageContentService;

    @RequestMapping("/load/messageContent")
    public ModelAndView loadMessageContentController() {
        Map model=new HashMap<>();
        List<MessageContent>list=messageContentService.loadMessageContent();
        model.put("list",list);
        return new ModelAndView("/backend/messagecontents",model);
    }
}

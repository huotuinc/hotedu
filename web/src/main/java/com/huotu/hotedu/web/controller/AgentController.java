package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by luffy on 2015/6/10.
 * 登录有关的Controller
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class AgentController {

    @Autowired
    private AgentService agentService;
    @Autowired
    private MemberService memberService;
    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    @RequestMapping("/pc/loadAgents")
    public String showMemberByAgent(Model model){
        String turnPage = "/pc/agentCenter";//TODO 返回路径暂定
        return null;
//        Agent agent = (Agent) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if(agent==null){
//            return null;
//        }
//        Page<Member> pages=memberService.loadMembersByAgent(agent,0,PAGE_SIZE);
//        model.addAttribute("allMemberList",pages);
//        model.addAttribute("agent",agent);
//        return turnPage;
    }


    @RequestMapping("pc/arrangeClass")
    public String arrangeClassController(ArrayList<Object> arrayList,Model model){
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/agentCenter";

        return "/pc/1234";
    }
}

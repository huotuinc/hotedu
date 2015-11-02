package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.ui.Model;
import java.util.Date;
import java.util.List;

/**
 * 移动端报名
 * Created by WenbinChen on 2015/11/2 14:22.
 */
@Controller
public class MobileSignupController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private LoginService loginService;

    @RequestMapping("/mobile/phaseTwoSignup")
    public String phaseTwoSignup(Model model) {
        List<Agent> agentList = agentService.findAvailableAgents();
        model.addAttribute("agentList",agentList);
        return "/mobile/phaseTwoSignup_mobile";
    }

    /**
     * 报名
     * @param realName
     * @param sex
     * @param phoneNo
     * @param areaId
     * @return
     */
    @RequestMapping("/mobile/baomin")
    @ResponseBody
    public Result baomin(String realName, Integer sex, String phoneNo, String areaId) {
        Date d = new Date();
        //输入校验
        if (phoneNo == null || "".equals(phoneNo)) {
            return new Result(0,"手机号不能为空");
        }
        if (realName == null || "".equals(realName)) {
            return new Result(0,"姓名不能为空");
        }
        if (sex == null) {
            return new Result(0,"请选择性别");
        }
        if (areaId == null || "".equals(areaId)) {
            return new Result(0,"请选择报名区域");
        }
        Agent agent = agentService.findByAreaId(areaId);
        if (agent == null) {
            return new Result(0,"该地区报名点临时取消");
        }
        boolean exist = memberService.isPhoneNoExist(phoneNo);
        //手机号已被注册
        if (exist) {
            return new Result(0,"该手机号已注册");

        }
        Member mb = new Member();
        mb.setAgent(agent);
        mb.setRealName(realName);
        mb.setSex(sex);
        mb.setPhoneNo(phoneNo);
        mb.setLoginName(phoneNo);
        mb.setEnabled(true);
        mb.setRegisterDate(d);
        mb.setApplyDate(d);
        loginService.newLogin(mb, phoneNo.substring(7));
        return new Result(1,"申请报名成功");
    }

    /**
     * 检测手机号是否被注册
     * @param phoneNo
     * @return
     */
    @RequestMapping("/mobile/checkPhoneNo")
    @ResponseBody
    public Result checkPhoneNo(String phoneNo) {
        Result result = new Result();
        String message = "";
        int status = 0;
        if ("".equals(phoneNo) || phoneNo == null) {
            message = "手机号不能为空";
        } else {
            boolean exist = memberService.isPhoneNoExist(phoneNo);
            if (exist) {
                message = "该手机号已被注册";
            } else {
                status = 1;
                message = "该手机号可用";
            }
        }
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

}

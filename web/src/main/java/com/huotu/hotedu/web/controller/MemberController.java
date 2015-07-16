package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

/**
 * Created by cwb on 2015/7/15.
 */
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @RequestMapping("/pc/load")
    public String load() {
        return "pc/registerTest";
    }

    @RequestMapping("/pc/register")
    public String register(String realName,int sex,String phoneNo,String area,Model model) throws Exception {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/registerTest";
        if("".equals(realName)||realName==null) {
            errInfo = "姓名不能为空";
        }else if("".equals(phoneNo)||phoneNo==null) {
            errInfo = "手机号不能为空";
        }else if("".equals(area)||area==null) {
            errInfo = "请选择报名地点";
        }else {
            Member mb = new Member();
            mb.setRealName(realName);
            mb.setSex(sex);
            mb.setPhoneNo(phoneNo);
            mb.setArea(area);
            mb.setLoginName(phoneNo);
            mb.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes("UTF-8")).toLowerCase());
            mb.setEnabled(false);
            memberService.addMember(mb);
            msgInfo = "报名成功";
        }
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }
}

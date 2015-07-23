package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by cwb on 2015/7/15.
 * Modify by shiliting on 20157/21
 */
@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private LoginService loginService;

    @RequestMapping("/pc/loadMemberRegister")
    public String load() {
        return "pc/yun-baomin";
    }


    @RequestMapping("/pc/register")
    public String register(String realName,int sex,String phoneNo,String areaId,Model model) throws Exception {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/yun-baomin";
        if("".equals(realName)||realName==null) {
            errInfo = "姓名不能为空";
        }else if("".equals(phoneNo)||phoneNo==null) {
            errInfo = "手机号不能为空";
        }else if("".equals(areaId)||areaId==null) {
            errInfo = "请选择报名地点";
        }else {
            boolean exist = memberService.isPhoneNoExist(phoneNo);
            if(exist) {
                errInfo = "该手机号已被注册";
            }else {
                Agent agent = agentService.findByAreaId(areaId);
                if(agent==null) {
                    errInfo = "该地区报名点临时取消";
                    turnPage = "/pc/yun-baomin";
                }else {
                    Member mb = new Member();
                    mb.setAgent(agent);
                    mb.setRealName(realName);
                    mb.setSex(sex);
                    mb.setPhoneNo(phoneNo);
                    mb.setLoginName(phoneNo);
                    mb.setEnabled(false);
                    loginService.newLogin(mb,"123456");
                    msgInfo = "报名成功";
                }
            }
        }
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }

    /**
     * 查看学员信息详情
     * @param id 学员id
     * @param model 返回值
     * @return
     */
    @RequestMapping("pc/detailMember")
    public String detailMember(Long id,Model model)
    {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/detailTest";
        if(id==null) {
            errInfo = "请重新登录";
        }else {
            Member mb = memberService.findOneById(id);
            model.addAttribute("realName",mb.getRealName());
            model.addAttribute("phoneNo",mb.getPhoneNo());
            model.addAttribute("area",mb.getAgent().getArea());
            model.addAttribute("applyDate",mb.getApplyDate());

            model.addAttribute("payDate",mb.getPayDate());
            model.addAttribute("agent","负责人");

            model.addAttribute("examDate",mb.getTheClass().getExam().getExamDate());
            model.addAttribute("examAddress",mb.getTheClass().getExam().getExamAddress());
            model.addAttribute("theClass",mb.getTheClass());
            model.addAttribute("isPassed",mb.isPassed());

            msgInfo = "查看详情";
        }

        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }

    /**
     * 删除学员，设为不可用
     * @param id 学员id
     * @param model 返回值
     * @return
     */
    @RequestMapping("pc/delMember")
    public String delMember(Long id,Model model)
    {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/delTest";
        if(id==null) {
            errInfo = "请重新登录";
        }else {
            memberService.delMember(id);
            msgInfo = "删除成功";
        }
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }

    /**
     * 确认交费
     * @param id 学员id
     * @param model 返回值
     * @return
     */
    @RequestMapping("pc/checkPay")
    public String checkPay(Long id,Model model)
    {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/checkPay";
        if(id==null) {
            errInfo = "请重新登录";
        }else {
            memberService.checkPay(id);
            msgInfo = "交费成功";
        }
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }

//    /**
//     * 用来测试向页面返回会员之后页面是否正常显示会员信息
//     * @param MemberId    会员的sessionId
//     * @return            结果集，包含会员信息，json字符串格式返回
//     */
//    @RequestMapping("pc/checkMemberLogin")
//    @ResponseBody
//    public Result checkMemberLogin(String MemberId,HttpServletRequest request){
//        HttpSession session=request.getSession();
//        Result result=new Result();
//        Member member=new Member();
//        member.setPictureUri("/pc/images/600x20005326c6dcd9c7f.jpg");
//        member.setLoginName("wangyong11111");
//        result.setBody(member);
//        result.setStatus(0);
//        return result;
//    }


}

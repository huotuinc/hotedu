package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.entity.Login;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.entity.Result;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;


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
    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE=10;

    @RequestMapping("/pc/loadMemberRegister")
    public String load(Model model) {
        String style = "padding:0px;display:none";
        model.addAttribute("style",style);
        return "pc/yun-baomin";
    }


    @RequestMapping("/pc/register")
    public String register(String realName,int sex,String phoneNo,String areaId,Model model) throws Exception {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "/pc/yun-baomin";
        String style = "padding:0px;display:none";
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
                    Date d = new Date();
                    mb.setAgent(agent);
                    mb.setRealName(realName);
                    mb.setSex(sex);
                    mb.setPhoneNo(phoneNo);
                    mb.setLoginName(phoneNo);
                    mb.setEnabled(true);
                    mb.setRegisterDate(d);
                    mb.setApplyDate(d);
                    loginService.newLogin(mb,"123456");
                    msgInfo = "报名成功";
                }
            }
        }
        model.addAttribute("style",style);
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }

    /**
     * Created by cwb on 2015/7/23.
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/pc/loadPersonalCenter")
    public String loadPersonalCenter(@AuthenticationPrincipal Login user, Model model) {
        String errInfo = "";
        String turnPage = "/pc/yun-geren";
        String style = "padding:0px;";
        String loginButton = "";
        if(user==null) {
            throw new IllegalStateException("尚未登录");
        }else if(user instanceof Member) {
            Member mb = memberService.findOneByLoginName(user.getLoginName());
            if(mb==null) {
                errInfo = "加载信息失败";
                turnPage = "/pc/yun-index";
            }else {
                model.addAttribute("mb",mb);
            }
        }else if(user instanceof Agent) {
            return "redirect:/pc/searchMembers";
        }
        model.addAttribute("errInfo",errInfo);
        model.addAttribute("style",style);
        model.addAttribute("loginButton",loginButton);
        return turnPage;
    }

    /**
     * Created by shiliting on 2015/7/25.
     * 查询学员
     * @param agent      该学员属于的代理商
     * @param pageNo     当前显示的页数
     * @param keywords   搜索的关键字
     * @param type       搜索的类型
     * @param model      准备发动到浏览器的数据
     * @return           yun-daili.html
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/searchMembers")
    public String searchMembers(@AuthenticationPrincipal Agent agent,
                                @RequestParam(required = false)Integer pageNo,
                                @RequestParam(required = false)String keywords,
                                @RequestParam(required = false,value = "searchSort") String type,
                                Model model) {
        String turnPage = "/pc/yun-daili";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }

        Page<Member> pages = memberService.searchMembers(agent,pageNo,PAGE_SIZE,keywords,type);
        long totalRecords = pages.getTotalElements();  //总记录数
        int numEl =  pages.getNumberOfElements();      //当前分页记录数
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = memberService.searchMembers(agent, pageNo, PAGE_SIZE, keywords, type);
        }
        model.addAttribute("agent",agent);
        model.addAttribute("allMemberList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort",type);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("navigation","bmxx");
        model.addAttribute("totalMembers",memberService.searchMembers(agent,pageNo,PAGE_SIZE).getTotalElements());
        return turnPage;
    }


    /**
     * Created by shiliting on 2015/7/25.
     * 删除一位学员
     * @param pageNo    当前的页数
     * @param keywords  检索关键字
     * @param type      检索类型
     * @param id        学员ID
     * @param model     与分页有关的参数
     * @return          重定向/pc/searchMembers
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/delMember")
    public String delMember(@RequestParam(required = false,value = "pageNo")Integer pageNo,
                            @RequestParam(required = false,value = "keywords")String keywords,
                            @RequestParam(required = false,value = "searchSort") String type,
                            Long id, Model model) {
        String returnPage="redirect:/pc/searchMembers";
        memberService.delMember(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort",type);
        return returnPage;
    }


    /**
     * Created by shiliting on 2015/7/27
     * 毕业管理
     * @param agent     当前代理商
     * @param pageNo    当前页面
     * @param keywords  搜索关键字
     * @param type      搜索类型
     * @param model     参数集合
     * @return          yun-daili.html
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/graduationMembers")
    public String graduationMembers(@AuthenticationPrincipal Agent agent,
                                    @RequestParam(required = false,value = "pageNo")Integer pageNo,
                                    @RequestParam(required = false,value = "keywords")String keywords,
                                    @RequestParam(required = false,value = "searchSort") String type,
                                    Model model) {
        String turnPage = "/pc/yun-daili";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }
        model.addAttribute("agent",agent);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort",type);
        model.addAttribute("totalMembers",memberService.searchMembers(agent,pageNo,PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation","bygl");
        return turnPage;
    }





    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/addMembers")
    @ResponseBody
    public Result graduationMembers(@AuthenticationPrincipal Agent agent, String realName, int sex,String phoneNo, Model model) {
        Result result=new Result();
        if(memberService.isPhoneNoExist(phoneNo)){
            result.setStatus(1);
            result.setMessage("手机号已被注册！");
            return result;
        }
        Member member=new Member();
        member.setPhoneNo(phoneNo);
        member.setRegisterDate(new Date());
        member.setApplyDate(new Date());
        member.setRealName(realName);
        member.setAgent(agent);
        member.setSex(sex);
        memberService.addMember(member);
        result.setStatus(0);
        result.setMessage("注册成功");
        return result;
    }
















    /**
     * Created by jiashubing on 2015/7/24.
     * 查看学员信息详情（报名信息）
     * @param id    学员id
     * @param model 返回值
     * @return      查看学员个人详细信息页面
     */
    @RequestMapping("/pc/detailMember")
    public String detailMember(Long id,Model model)
    {
        String errInfo = "";
        String turnPage = "/pc/yun-xyge";
        if(id==null) {
            errInfo = "请重新登录";
            turnPage = "redirect:/pc/login";
        }else {
            Member mb = memberService.findOneById(id);
            model.addAttribute("mb",mb);
        }

        model.addAttribute("errInfo", errInfo);
        return turnPage;
    }


    /**
     * Created by jiashubing on 2015/7/24.
     * 查看个人详细信息时确认交费
     * @param id    学员id
     * @param model 返回值
     * @return
     */
    @RequestMapping("/pc/checkPay")
    public String checkPay(Long id,Model model)
    {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:/pc/searchMembers";
        if(id==null) {
            errInfo = "请重新登录";
            turnPage = "redirect:/pc/login";
        }else {
            memberService.checkPay(id);
            msgInfo = "交费成功";
        }
        model.addAttribute("msgInfo",msgInfo);
        model.addAttribute("errInfo",errInfo);
        return turnPage;
    }


    @RequestMapping("/pc/checkPayList")
    public String checkPayList(String checkPayLis,Model model){
        String turnPage = "redirect:/pc/searchMembers";
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(checkPayLis);
        memberService.checkPayList(arrayList);
        return turnPage;
    }


}

package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.CertificateService;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.service.MemberService;
import com.huotu.hotedu.web.service.StaticResourceService;
import com.huotu.iqiyi.sdk.IqiyiVideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


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
    @Autowired
    private StaticResourceService  staticResourceService;
    @Autowired
    private CertificateService certificateService;
    @Autowired
    IqiyiVideoRepository iqiyiVideoRepository;

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


    /**
     * Created by cwb on 2015/7/21.
     * 学员报名、注册
     * @param realName
     * @param sex
     * @param phoneNo
     * @param areaId
     * @param model
     * @return
     * @throws Exception
     */
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
    public String loadPersonalCenter(@AuthenticationPrincipal Login user, Model model) throws URISyntaxException {
        String errInfo = "";
        String turnPage = "/pc/yun-geren";
        String loginButton = "";
        if(user==null) {
            throw new IllegalStateException("尚未登录");
        }else if(user instanceof Member) {
            Member mb = memberService.findOneByLoginName(user.getLoginName());
            if(mb==null) {
                errInfo = "加载信息失败";
                turnPage = "/pc/yun-index";
            }else {
                Certificate certificate = certificateService.findOneByMember(mb);
                int picExist = 0;
                if(certificate!=null) {
                    if(certificate.getPictureUri()!=null) {
                        certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
                        picExist = 1;
                    }
                    model.addAttribute("certificate",certificate);
                }
                model.addAttribute("picUrl",picExist);
                model.addAttribute("mb",mb);
            }
        }else if(user instanceof Agent) {
            return "redirect:/pc/searchMembers";
        }else if(user instanceof Manager) {
            return "redirect:/backend/index";
        }
        model.addAttribute("errInfo",errInfo);
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
        model.addAttribute("searchSort", type==null?"all":type);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("navigation","bmxx");
        model.addAttribute("totalMembers",memberService.searchMembers(agent,pageNo,PAGE_SIZE).getTotalElements());
        return turnPage;
    }




    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/searchMembers")
    public String applyForMembers(@RequestParam(required = false)Integer pageNo,
                                  @RequestParam(required = false)String info,
                                  @RequestParam(required = false)Long certificateId,
                                  Model model) {
        String turnPage = "/backend/certificateapplications";
        if(pageNo==null||pageNo<0){
            pageNo=0;
        }

        Page<Member> pages = memberService.searchMembers(pageNo,PAGE_SIZE,"applyed");
        long totalRecords = pages.getTotalElements();  //总记录数
        int numEl =  pages.getNumberOfElements();      //当前分页记录数
        if(numEl==0) {
            pageNo=pages.getTotalPages()-1;
            if(pageNo<0) {
                pageNo = 0;
            }
            pages = memberService.searchMembers(pageNo,PAGE_SIZE,"applyed");
        }
        if(info!=null){
            model.addAttribute("info",info);
            model.addAttribute("certificate",certificateService.findOneById(certificateId));
        }
        model.addAttribute("allMemberList", pages);
        model.addAttribute("totalPages",pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }




    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/lookMember")
    public String lookMembers(Long id, Model model)throws Exception{
        String turnPage = "/backend/prentice";
        Member member=memberService.findOneById(id);
        Certificate certificate = null;
        if(member.getCertificateStatus()==2) {
            certificate=certificateService.findOneByMember(member);
            certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
        }
        model.addAttribute("member",member);
        model.addAttribute("certificate",certificate);
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
        model.addAttribute("agent", agent);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort", type);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "bygl");
        return turnPage;
    }


    /**
     * Created by shiliting on 2015/7/27
     * 代理商新增学员
     * @param agent
     * @param realName
     * @param sex
     * @param phoneNo
     * @return
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/addMembers")
    @ResponseBody
    public Result graduationMembers(@AuthenticationPrincipal Agent agent, String realName, int sex,String phoneNo) {
        Result result=new Result();
        if(memberService.isPhoneNoExist(phoneNo)){
            result.setStatus(1);
            result.setMessage("手机号已被注册！");
            return result;
        }
        Member mb = new Member();
        Date d = new Date();
        mb.setAgent(agent);
        mb.setRealName(realName);
        mb.setSex(sex);
        mb.setPhoneNo(phoneNo);
        mb.setLoginName(phoneNo);
        mb.setRegisterDate(d);
        mb.setApplyDate(d);
        loginService.newLogin(mb, "123456");
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
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/detailMember")
    public String detailMember(Long id,Model model)
    {
        String errInfo = "";
        String turnPage = "/pc/yun-xyge";
        if(id==null) {
            errInfo = "请重新登录";
            turnPage = "redirect:/pc/index";
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
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/checkPay")
    public String checkPay(Long id,Model model)
    {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:/pc/searchMembers";
        if(id==null) {
            errInfo = "请重新登录";
            turnPage = "redirect:/pc/index";
        }else {
            memberService.checkPay(id);
            msgInfo = "交费成功";
        }
        model.addAttribute("msgInfo", msgInfo);
        model.addAttribute("errInfo", errInfo);
        return turnPage;
    }


    /**
     * Created by jiashubing on 2015/7/28.
     * 查看个人详细信息时确认交费
     * @param checkPayLis    学员id集合
     * @param model          返回客户端集合
     * @return
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/checkPayList")
    @ResponseBody
    public Result checkPayList(String checkPayLis,Model model){
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(checkPayLis);
        memberService.checkPayList(arrayList);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }


    @PreAuthorize("hasRole('MEMBER')")
    @RequestMapping("/pc/applyForCertificate")
    @ResponseBody
    public Result applyForCertificate(@AuthenticationPrincipal Login user,
                                      String receiveName,String receiveAddress,String contactAddress,String phoneNo)throws Exception {

        Result result = new Result();
        if(user==null) {
            result.setStatus(0);
            result.setMessage("尚未登录,请登录");
        }else if(user instanceof Member) {
            Member mb=(Member)user;
            Certificate certificate = certificateService.findOneByMember(mb);
            if(certificate==null) {
                certificate = new Certificate();
                certificate.setReceiveName(receiveName);
                certificate.setReceiveAddress(receiveAddress);
                certificate.setContactAddress(contactAddress);
                certificate.setPhoneNo(phoneNo);
                certificate.setMember(mb);
                mb.setCertificateStatus(0);
            }else {
                certificate.setReceiveName(receiveName);
                certificate.setReceiveAddress(receiveAddress);
                certificate.setContactAddress(contactAddress);
                certificate.setPhoneNo(phoneNo);
                mb.setCertificateStatus(2);
                //设置学员的申请领证时间
                mb.setApplyCertificateDate(new Date());
            }
            memberService.modifyMember(mb);
            certificateService.addCertificate(certificate);
            result.setStatus(1);
            result.setMessage("操作成功");
        }
        return result;
    }

    @RequestMapping("/pc/checkIsPassExam")
    @ResponseBody
    public Result checkIsPassExam(long id){
        Member member=memberService.findOneById(id);
        Result result=new Result();
        result.setStatus(member.getPassed());
        return result;
    }

    @RequestMapping("/pc/checkHaveCertificate")
    @ResponseBody
    public Result checkHaveCertificate(long id){
        Member member=memberService.findOneById(id);
        Result result=new Result();
        boolean flag=false;
        if(member!=null) {
            flag = member.getPassed() == 1 && member.getCertificate() != null;
            result.setStatus(flag?1:0);
            if(result.getStatus()==1) {
                result.setBody(member.getCertificate().getCertificateNo());
            }
        }else{
            result.setStatus(0);
        }
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/getCertificateByMemberId")
    @ResponseBody
    public Result getCertificateByMemberId(long id){
        Result result=new Result();
        Member member=memberService.findOneById(id);

        if(member!=null){
            if(member.getCertificate()==null){
                Certificate certificate=certificateService.findOneByMember(member);
                result.setStatus(1);
                result.setBody(certificate);
            }else{
                result.setStatus(2);
            }
        }else{
            result.setStatus(0);
        }
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/issueCertificateAjax")
    @ResponseBody
    public Result issueCertificateAjax(String certificateNo,long memberId,long certificateId){
        Member member=memberService.findOneById(memberId);
        Result result=new Result();
        if(certificateService.findOneBycertificateNo(certificateNo)!=null){  //验证发的证书是否已经发过了
            result.setStatus(0);
        }else if(member.getAgent().getSendCertificateNumber()>=member.getAgent().getCertificateNumber()){
            result.setStatus(2);
        }
        else{
            Certificate certificate=certificateService.findOneById(certificateId);
            certificate.setCertificateNo(certificateNo);
            certificateService.modifyCertificate(certificate);
            member.setCertificate(certificate);
            member.setReceiveCertificateDate(new Date());
            member.setCertificateStatus(1);
            Agent agent=member.getAgent();
            member.getAgent().setSendCertificateNumber(agent.getSendCertificateNumber()+1);//代理商的证书减1
            agentService.modifyAgent(agent);
            memberService.modifyMember(member);
            result.setStatus(1);
        }
        return result;
    }




    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/issueCertificate")
        public String issueCertificate(String certificateNo,long memberId,long certificateId,
                                   @RequestParam(required = false)Integer pageNo,
                                   @RequestParam(required = false)String keywords,
                                   @RequestParam(required = false)String searchSort,
                                   @RequestParam(required = false)String classId,
                                   @RequestParam(required = false)String returnPage,
                                   Model model) {
        Member member = memberService.findOneById(memberId);
        if (certificateService.findOneBycertificateNo(certificateNo) != null) {//验证发的证书是否已经发过了
            model.addAttribute("info", "该证书已经发过了");
            model.addAttribute("certificateId", certificateId);
        } else if (member.getAgent().getSendCertificateNumber() >= member.getAgent().getCertificateNumber()) {
            model.addAttribute("info", "该代理商的证书已经发完了~");
            model.addAttribute("certificateId", certificateId);
        } else {

            Certificate certificate = certificateService.findOneById(certificateId);
            certificate.setCertificateNo(certificateNo);
            certificateService.modifyCertificate(certificate);
            member.setCertificate(certificate);
            member.setReceiveCertificateDate(new Date());
            member.setCertificateStatus(1);
            Agent agent = member.getAgent();
            member.getAgent().setSendCertificateNumber(agent.getSendCertificateNumber() + 1);//代理商的证书减1
            agentService.modifyAgent(agent);
            memberService.modifyMember(member);
        }
        model.addAttribute("pageNo", pageNo);
        if ("applyList".equals(returnPage)) {
            return "redirect:/backend/searchMembers";
        }else{
            model.addAttribute("keywords", keywords);
            model.addAttribute("searchSort", searchSort);
            model.addAttribute("classId", classId);
            return "redirect:/backend/agentClassMember";
        }
    }

    /**
     * 用ajaxfileupload上传文件
     * @param user
     * @param file
     * @param response
     * @return
     * @throws Exception
     */
    @RequestMapping("/pc/ajaxFileUpload")
    @ResponseBody
    public Result ajaxFileUpload(@AuthenticationPrincipal Login user,@RequestParam("pictureImg") MultipartFile file,HttpServletResponse response)throws Exception {
        Result result = new Result();
        if(user==null) {
            result.setStatus(0);
            result.setMessage("尚未登录,请登录");
        }else if(user instanceof Member) {
            Member mb=(Member)user;
            //文件格式判断
            if(ImageIO.read(file.getInputStream())==null){throw new Exception("不是图片！");}
            if(file.getSize()==0){throw new Exception("文件为空！");}
            //保存图片
            String fileName = StaticResourceService.MEMBER_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, file.getInputStream());
            Certificate certificate = certificateService.findOneByMember(mb);
            //判断该学员是否提交过申请信息
            if(certificate!=null) {
                certificate.setPictureUri(fileName);
                certificateService.addCertificate(certificate);
                certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
            }else {
                certificate = new Certificate();
                certificate.setPictureUri(fileName);
                certificate.setMember(mb);
                certificateService.addCertificate(certificate);
                certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
            }
            result.setStatus(1);
            result.setBody(certificate);
            response.setHeader("X-frame-Options","SAMEORIGIN");
            result.setMessage("操作成功");
        }
        return result;
    }

}

package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.exception.InterrelatedException;
import com.huotu.hotedu.model.CodeType;
import com.huotu.hotedu.model.VerificationType;
import com.huotu.hotedu.service.*;
import com.huotu.hotedu.util.EnumHelper;
import com.huotu.hotedu.util.StringHelper;
import com.huotu.hotedu.util.SysRegex;
import com.huotu.hotedu.web.service.StaticResourceService;
import com.huotu.iqiyi.sdk.IqiyiVideoRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import java.util.*;


/**
 * Created by cwb on 2015/7/15.
 * Modify by shiliting on 20157/21
 */
@Controller
public class MemberController {

    private static final Log log = LogFactory.getLog(MemberController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private AgentService agentService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private StaticResourceService staticResourceService;
    @Autowired
    private CertificateService certificateService;
    @Autowired
    IqiyiVideoRepository iqiyiVideoRepository;
    @Autowired
    private Environment env;
    @Autowired
    VerificationService verificationService;


    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 10;

    @RequestMapping("/pc/loadMemberRegister")
    public String load(@AuthenticationPrincipal Login user, Model model) {
        Member mb = null;
        if (user instanceof Member) {
            mb = (Member) user;
            mb=memberService.findOneById(mb.getId());
        }
        String style = "padding:0px;display:none";
        List<Agent> agentList = agentService.findAvailableAgents();
        model.addAttribute("agentList",agentList);
        model.addAttribute("mbInfo",mb);
        model.addAttribute("style",style);
        model.addAttribute("flag","yun-baomin.html");  //此属性用来给前台确定当前是哪个页面
        return "pc/yun-baomin";
    }


    /**
     * Created by cwb on 2015/7/21.
     * Modified by cwb on 2015/8/11.
     * 未注册学员报名
     *
     * @param realName
     * @param sex
     * @param phoneNo
     * @param areaId
     * @return
     * @throws Exception
     */
    @RequestMapping("/pc/baomin")
    @ResponseBody
    public Result baomin(@AuthenticationPrincipal Login user, String realName, Integer sex, String phoneNo, String areaId) {
        Result result = new Result();
        String message = "";
        int status = 0;
        Member mb = null;
        Date d = new Date();
        Agent agent = null;
        //输入校验
        if (phoneNo == null || "".equals(phoneNo)) {
            message = "手机号不能为空";
        } else if (realName == null || "".equals(realName)) {
            message = "姓名不能为空";
        } else if (sex == null) {
            message = "请选择性别";
        } else if (areaId == null || "".equals(areaId)) {
            message = "请选择报名区域";
        } else {
            agent = agentService.findByAreaId(areaId);
            boolean exist = memberService.isPhoneNoExist(phoneNo);
            //手机号已被注册
            if (exist) {
                //学员已登录
                if (user instanceof Member) {
                    if (agent == null) {
                        message = "该地区报名点临时取消";
                    } else {
                        mb = ((Member) user);
                        if (mb.getAgent() != null) {
                            message = "您已经报名过了";
                        } else {
                            mb.setAgent(agent);
                            mb.setApplyDate(d);
                            mb.setRealName(realName);
                            mb.setSex(sex);
                            memberService.addMember(agent, mb);
                            status = 1;
                            message = "申请报名成功";
                        }
                    }
                } else {//学员未登录
                    message = "该手机号已注册";
                }
            } else {//手机号可用
                if (agent == null) {
                    message = "该地区报名点临时取消";
                } else {
                    mb = new Member();
                    mb.setAgent(agent);
                    mb.setRealName(realName);
                    mb.setSex(sex);
                    mb.setPhoneNo(phoneNo);
                    mb.setLoginName(phoneNo);
                    mb.setEnabled(true);
                    mb.setRegisterDate(d);
                    mb.setApplyDate(d);
                    loginService.newLogin(mb, phoneNo.substring(7));
                    status = 1;
                    message = "申请报名成功";
                }
            }
        }
        result.setStatus(status);
        result.setMessage(message);
        return result;
    }

    /**
     * 发送验证码
     *
     * @param phone    String(11)
     * @param type     类型  1：注册    2：忘记密码
     * @param codeType 0文本 1语音
     * @return
     * @throws Exception
     */
    @RequestMapping("/pc/sendSMS")
    @ResponseBody
    public Result sendSMS(String phone, int type, @RequestParam(required = false) Integer codeType) throws InterrelatedException {
        Result result = new Result();
        VerificationType verificationType = EnumHelper.getEnumType(VerificationType.class, type);
        Date date = new Date();
        // **********************************************************
        // 发送短信前处理
        if ("".equals(phone) || phone == null) {
            result.setMessage("手机号不能为空");
        } else if (!SysRegex.IsValidMobileNo(phone)) {
            result.setMessage("不合法的手机号");
        } else if (checkPhoneNo(phone).getStatus() == 0) {
            result.setMessage("该手机号已经被注册，请选择其他手机号");
        } else {
            Random rnd = new Random();
            String code = StringHelper.RandomNum(rnd, 4);
            try {
                verificationService.sendCode(phone, VerificationService.VerificationProject.hotedu, code, date, verificationType, codeType != null ? EnumHelper.getEnumType(CodeType.class, codeType) : CodeType.text);
                result.setStatus(1);
                result.setMessage("发送成功");
                return result;
            } catch (IllegalStateException ex) {
                result.setMessage("验证码发送间隔为60秒");
                return result;
            } catch (IllegalArgumentException ex) {
                result.setMessage("不合法的手机号");
                return result;
            } catch (NoSuchMethodException ex) {
                //发送类别不受支持！
                result.setMessage("短信发送通道不稳定，请重新尝试");
                return result;
            }
        }
        return result;
    }

    /**
     * Created by cwb on 2015/8/11
     * 学员注册
     */
    @RequestMapping("/pc/register")
    @ResponseBody
    public Result register(String phoneNo, String authCode) {
        if ("".equals(phoneNo) || phoneNo == null) {
            return new Result(0,"手机号不能为空");
        }
        if(memberService.isPhoneNoExist(phoneNo)) {
            return new Result(0,"该手机号已被注册");
        }
        Member mb = new Member();
        Date d = new Date();
        mb.setPhoneNo(phoneNo);
        mb.setLoginName(phoneNo);
        mb.setEnabled(true);
        mb.setRegisterDate(d);
        loginService.newLogin(mb, phoneNo.substring(7));
        return new Result(1,mb.getPhoneNo());
        /*Result result = new Result();
        VerificationType verificationType = EnumHelper.getEnumType(VerificationType.class, 1);
        String message = "";
        int status = 0;
        if ("".equals(phoneNo) || phoneNo == null) {
            message = "手机号不能为空";
        } else {
            boolean exist = memberService.isPhoneNoExist(phoneNo);
            if (exist) {
                message = "该手机号已被注册";
            } else {
                result = verificationService.verifyCode(phoneNo, authCode,verificationType);
                if (result.getStatus() == 0) {
                    message = result.getMessage();
                } else {
                    Member mb = new Member();
                    Date d = new Date();
                    mb.setPhoneNo(phoneNo);
                    mb.setLoginName(phoneNo);
                    mb.setEnabled(true);
                    mb.setRegisterDate(d);
                    loginService.newLogin(mb, phoneNo.substring(7));
                    status = 1;
                    message = mb.getPhoneNo();
                }
            }
        }
        result.setStatus(status);
        result.setMessage(message);
        return result;*/
    }

    /**
     * Created by cwb on 2015/8/11
     * 检测手机号是否被注册
     */
    @RequestMapping("/pc/checkPhoneNo")
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

    /**
     * Created by cwb on 2015/7/23.
     *
     * @param user
     * @param model
     * @return
     */
    @RequestMapping("/pc/loadPersonalCenter")
    public String loadPersonalCenter(@AuthenticationPrincipal Login user, Model model) throws URISyntaxException {
        String errInfo = "";
        String turnPage = "/pc/yun-geren";
        String loginButton = "";
        if (user == null) {
            throw new IllegalStateException("尚未登录");
        } else if (user instanceof Member) {
            Member mb = memberService.findOneByLoginName(user.getLoginName());
            if (mb == null) {
                errInfo = "加载信息失败";
                turnPage = "/pc/yun-index";
            } else {
                Certificate certificate = certificateService.findOneByMember(mb);
                int picExist = 0;
                if (certificate != null) {
                    if (certificate.getPictureUri() != null) {
                        certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
                        picExist = 1;
                    }
                    model.addAttribute("certificate", certificate);
                }
                model.addAttribute("picUrl", picExist);
                model.addAttribute("mb", mb);
            }
        } else if (user instanceof Agent) {
            return "redirect:/pc/searchMembers";
        } else if (user instanceof Manager) {
            return "redirect:/backend/index";
        }
        model.addAttribute("errInfo", errInfo);
        model.addAttribute("loginButton", loginButton);
        return turnPage;
    }

    /**
     * Created by shiliting on 2015/7/25.
     * 查询学员
     *
     * @param agent    该学员属于的代理商
     * @param pageNo   当前显示的页数
     * @param keywords 搜索的关键字
     * @param type     搜索的类型
     * @param model    准备发动到浏览器的数据
     * @return yun-daili.html
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/searchMembers")
    public String searchMembers(@AuthenticationPrincipal Agent agent,
                                @RequestParam(required = false) Integer pageNo,
                                @RequestParam(required = false) String keywords,
                                @RequestParam(required = false, value = "searchSort") String type,
                                Model model) {
        String turnPage = "/pc/yun-daili";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }

        Page<Member> pages = memberService.searchMembers(agent, pageNo, PAGE_SIZE, keywords, type);
        long totalRecords = pages.getTotalElements();  //总记录数
        int numEl = pages.getNumberOfElements();      //当前分页记录数
        if (numEl == 0) {
            pageNo = pages.getTotalPages() - 1;
            if (pageNo < 0) {
                pageNo = 0;
            }
            pages = memberService.searchMembers(agent, pageNo, PAGE_SIZE, keywords, type);
        }
        model.addAttribute("agent", agentService.findAgentById(agent.getId()));
        model.addAttribute("allMemberList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort", type == null ? "all" : type);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("navigation", "bmxx");
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        return turnPage;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/searchMembers")
    public String applyForMembers(@RequestParam(required = false) Integer pageNo,
                                  @RequestParam(required = false) String info,
                                  @RequestParam(required = false) Long certificateId,
                                  Model model) {
        String turnPage = "/backend/certificateapplications";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }

        Page<Member> pages = memberService.searchMembers(pageNo, PAGE_SIZE, "applyed");
        long totalRecords = pages.getTotalElements();  //总记录数
        int numEl = pages.getNumberOfElements();      //当前分页记录数
        if (numEl == 0) {
            pageNo = pages.getTotalPages() - 1;
            if (pageNo < 0) {
                pageNo = 0;
            }
            pages = memberService.searchMembers(pageNo, PAGE_SIZE, "applyed");
        }
        if (info != null) {
            model.addAttribute("info", info);
            model.addAttribute("certificate", certificateService.findOneById(certificateId));
        }
        model.addAttribute("allMemberList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        return turnPage;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/lookMember")
    public String lookMembers(@RequestParam(required = false) Integer pageNo,
                              @RequestParam(required = false) String keywords,
                              @RequestParam(required = false, value = "searchSort") String searchSort,
                              Long id, Model model) throws Exception {
        String turnPage = "/backend/prentice";
        Member member = memberService.findOneById(id);
        Certificate certificate = null;
        if (member.getCertificateStatus() != 0) {
            certificate = certificateService.findOneByMember(member);
            certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
        }
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort", searchSort);
        model.addAttribute("member", member);
        model.addAttribute("certificate", certificate);
        return turnPage;
    }

    /**
     * Created by shiliting on 2015/7/25.
     * 删除一位学员
     *
     * @param pageNo   当前的页数
     * @param keywords 检索关键字
     * @param type     检索类型
     * @param id       学员ID
     * @param model    与分页有关的参数
     * @return 重定向/pc/searchMembers
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/delMember")
    public String delMember(@RequestParam(required = false, value = "pageNo") Integer pageNo,
                            @RequestParam(required = false, value = "keywords") String keywords,
                            @RequestParam(required = false, value = "searchSort") String type,
                            Long id, Model model) {
        String returnPage = "redirect:/pc/searchMembers";
        memberService.delMember(id);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("searchSort", type);
        return returnPage;
    }

    /**
     * Created by shiliting on 2015/7/27
     * 毕业管理
     *
     * @param agent    当前代理商
     * @param pageNo   当前页面
     * @param keywords 搜索关键字
     * @param type     搜索类型
     * @param model    参数集合
     * @return yun-daili.html
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/graduationMembers")
    public String graduationMembers(@AuthenticationPrincipal Agent agent,
                                    @RequestParam(required = false, value = "pageNo") Integer pageNo,
                                    @RequestParam(required = false, value = "keywords") String keywords,
                                    @RequestParam(required = false, value = "searchSort") String type,
                                    Model model) {
        String turnPage = "/pc/yun-daili";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        model.addAttribute("agent", agentService.findAgentById(agent.getId()));
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
     *
     * @param agent
     * @param realName
     * @param sex
     * @param phoneNo
     * @return
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/addMembers")
    @ResponseBody
    public Result graduationMembers(@AuthenticationPrincipal Agent agent, String realName, int sex, String phoneNo) {
        Result result = new Result();
        if (memberService.isPhoneNoExist(phoneNo)) {
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
        loginService.newLogin(mb, phoneNo.substring(7));
        result.setStatus(0);
        result.setMessage("注册成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 查看学员信息详情（报名信息）
     *
     * @param id    学员id
     * @param model 返回值
     * @return 查看学员个人详细信息页面
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/detailMember")
    public String detailMember(Long id, Model model) {
        String errInfo = "";
        String turnPage = "/pc/yun-xyge";
        if (id == null) {
            errInfo = "请重新登录";
            turnPage = "redirect:/pc/index";
        } else {
            Member mb = memberService.findOneById(id);
            model.addAttribute("mb", mb);
        }

        model.addAttribute("errInfo", errInfo);
        return turnPage;
    }


    /**
     * Created by jiashubing on 2015/7/24.
     * 查看个人详细信息时确认交费
     *
     * @param id    学员id
     * @param model 返回值
     * @return
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/checkPay")
    public String checkPay(Long id, Model model) {
        String errInfo = "";
        String msgInfo = "";
        String turnPage = "redirect:/pc/searchMembers";
        if (id == null) {
            errInfo = "请重新登录";
            turnPage = "redirect:/pc/index";
        } else {
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
     *
     * @param checkPayLis 学员id集合
     * @param model       返回客户端集合
     * @return
     */
    @PreAuthorize("hasRole('AGENT')")
    @RequestMapping("/pc/checkPayList")
    @ResponseBody
    public Result checkPayList(String checkPayLis, Model model) {
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
                                      String receiveName, String receiveAddress, String phoneNo) throws Exception {

        Result result = new Result();
        if (user == null) {
            result.setStatus(0);
            result.setMessage("尚未登录,请登录");
        } else if (user instanceof Member) {
            Member mb = (Member) user;
            mb = memberService.findOneById(mb.getId());
            Certificate certificate = certificateService.findOneByMember(mb);
            if (certificate == null) {
                certificate = new Certificate();
                certificate.setReceiveName(receiveName);
                certificate.setReceiveAddress(receiveAddress);
                certificate.setPhoneNo(phoneNo);
                certificate.setMember(mb);
                mb.setCertificateStatus(0);
            } else {
                certificate.setReceiveName(receiveName);
                certificate.setReceiveAddress(receiveAddress);
                certificate.setPhoneNo(phoneNo);
                if (certificate.getPictureUri() == null || "".equals(certificate.getPictureUri())) {
                    mb.setCertificateStatus(0);
                } else {
                    mb.setCertificateStatus(2);
                }
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
    public Result checkIsPassExam(long id) {
        Member member = memberService.findOneById(id);
        Result result = new Result();
        result.setStatus(member.getPassed());
        return result;
    }

    @RequestMapping("/pc/checkHaveCertificate")
    @ResponseBody
    public Result checkHaveCertificate(long id) {
        Member member = memberService.findOneById(id);
        Result result = new Result();
        boolean flag = false;
        if (member != null) {
            flag = member.getPassed() == 1 && member.getCertificate() != null;
            result.setStatus(flag ? 1 : 0);
            if (result.getStatus() == 1) {
                result.setBody(member.getCertificate().getCertificateNo());
            }
        } else {
            result.setStatus(0);
        }
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/getCertificateByMemberId")
    @ResponseBody
    public Result getCertificateByMemberId(long id) {
        Result result = new Result();
        Member member = memberService.findOneById(id);

        if (member != null) {
            if (member.getCertificate() == null) {
                Certificate certificate = certificateService.findOneByMember(member);
                result.setStatus(1);
                result.setBody(certificate);
            } else {
                result.setStatus(2);
            }
        } else {
            result.setStatus(0);
        }
        return result;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/issueCertificateAjax")
    @ResponseBody
    public Result issueCertificateAjax(String certificateNo, long memberId, long certificateId) {
        Member member = memberService.findOneById(memberId);
        Result result = new Result();
        if (certificateService.findOneByCertificateNo(certificateNo) != null) {  //验证发的证书是否已经发过了
            result.setStatus(0);
        } else if (member.getAgent().getSendCertificateNumber() >= member.getAgent().getCertificateNumber()) {
            result.setStatus(2);
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
            result.setStatus(1);
        }
        return result;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/issueCertificate")
    public String issueCertificate(String certificateNo, long memberId, long certificateId,
                                   @RequestParam(required = false) Integer pageNo,
                                   @RequestParam(required = false) String keywords,
                                   @RequestParam(required = false) String searchSort,
                                   @RequestParam(required = false) String classId,
                                   @RequestParam(required = false) String returnPage,
                                   Model model) {
        Member member = memberService.findOneById(memberId);
        if (certificateService.findOneByCertificateNo(certificateNo) != null) {//验证发的证书是否已经发过了
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
        } else {
            model.addAttribute("keywords", keywords);
            model.addAttribute("searchSort", searchSort);
            model.addAttribute("classId", classId);
            return "redirect:/backend/agentClassMember";
        }
    }

    /**
     * 用ajaxfileupload上传文件
     *
     * @param user     当前用户角色
     * @param file     上传的文件
     * @param response http相应请求
     * @return 结果集
     * @throws Exception 上传文件出错异常
     */
    @RequestMapping("/pc/ajaxFileUpload")
    @ResponseBody
    public Result ajaxFileUpload(@AuthenticationPrincipal Login user, @RequestParam("pictureImg") MultipartFile file, HttpServletResponse response) throws Exception {
        Result result = new Result();
        if (user == null) {
            result.setStatus(0);
            result.setMessage("尚未登录,请登录");
        } else if (user instanceof Member) {
            Member mb = (Member) user;
            //文件格式判断
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new Exception("不是图片！");
            }
            if (file.getSize() == 0) {
                throw new Exception("文件为空！");
            }
            //保存图片
            String fileName = StaticResourceService.MEMBER_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, file.getInputStream());
            Certificate certificate = certificateService.findOneByMember(mb);
            //判断该学员是否提交过申请信息
            if (certificate != null) {
                if(certificate.getPictureUri() == null || ("").equals(certificate.getPictureUri()))
                    certificate.setPictureUri(fileName);
                else{
                    staticResourceService.deleteResource(certificate.getPictureUri());
                    certificateService.deletePic(certificate);
                    certificate.setPictureUri(fileName);
                }
                certificateService.addCertificate(certificate);
                certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
            } else {
                certificate = new Certificate();
                certificate.setPictureUri(fileName);
                certificate.setMember(mb);
                certificateService.addCertificate(certificate);
                certificate.setPictureUri(staticResourceService.getResource(certificate.getPictureUri()).toString());
            }
            result.setStatus(1);
            result.setBody(certificate);
            response.setHeader("X-frame-Options", "SAMEORIGIN");
            result.setMessage("操作成功");
        }
        return result;
    }

    @RequestMapping("/pc/haierSignup")
    public String haierSingup() {
        return "/mobile/hr-signup_mobile";
    }

    @RequestMapping("/pc/haierPay")
    public String haierPay() {
        return "/mobile/hr-pay_mobile";
    }

    @RequestMapping("/pc/ruiliSignup")
    public String ruiliSignup() {
        return "/mobile/rl-signup_mobile";
    }


}

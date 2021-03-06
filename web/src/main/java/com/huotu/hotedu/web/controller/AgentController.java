package com.huotu.hotedu.web.controller;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.model.WechatPay;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.service.ClassTeamService;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.service.MemberService;
import com.huotu.hotedu.util.StringHelper;
import com.huotu.hotedu.web.service.StaticResourceService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Controller
public class AgentController {

    @Autowired
    private AgentService agentService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ClassTeamService classTeamService;
    /**
     * 用来储存处理静态资源的接口
     */
    @Autowired
    StaticResourceService staticResourceService;
    @Autowired
    LoginService loginService;

    /**
     * 用来储存分页中每页的记录数
     */
    public static final int PAGE_SIZE = 10;


    /**
     * Created by jiashubing on 2015/7/24.
     * 将学员保存到新建班级中
     *
     * @param agent                 当前代理商
     * @param className             新建班级的名字
     * @param noClassMemberArrayLis 复选框选中成员的id集合,Strring类型
     * @return confirm.js中 check_arrangeNewClass方法
     */

    @RequestMapping("/pc/addSaveNewClassTeam")
    @ResponseBody
    public Result addSaveNewClassTeam(@AuthenticationPrincipal Agent agent, String className, String noClassMemberArrayLis) {
        Result result = new Result();
        if (agentService.isClassTeamNameAvailable(className)) {
            MyJsonUtil myJsonUtil = new MyJsonUtil();
            ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
            ClassTeam classTeam = new ClassTeam();
            classTeam.setAgent(agent);
            classTeam.setClassName(className);
            ClassTeam ct = agentService.addClassTeam(classTeam);
            agentService.arrangeClass(arrayList, ct);
            result.setStatus(1);
            result.setMessage("安排成功");

        } else {
            result.setStatus(0);
            result.setMessage("班级已经存在，请重新设置班级名称");
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 将班级保存到新建考场中  AJAX
     *
     * @param agent             当前代理商
     * @param examDate          考试时间
     * @param examAddress       考试地点
     * @param classExamArrayLis 需要安排的班级
     * @return confirm.js中 check_arrangeNewExam方法
     */
    @RequestMapping("/pc/addSaveNewExam")
    @ResponseBody
    public Result addSaveNewExam(@AuthenticationPrincipal Agent agent, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date examDate, String examAddress, String classExamArrayLis) {
        Result result = new Result();
        String examName = examAddress + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(examDate);
        if (agentService.isExamNameAvailable(examName)) {
            MyJsonUtil myJsonUtil = new MyJsonUtil();
            ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(classExamArrayLis);
            Exam exam = new Exam();
            exam.setExamAddress(examAddress);
            exam.setExamDate(examDate);
            exam.setExamName(examName);
            Exam ex = agentService.addExam(agent, exam);
            agentService.arrangeExam(arrayList, ex);
            result.setStatus(1);
            result.setMessage("安排成功");

        } else {
            result.setStatus(0);
            result.setMessage("考场已经存在，请重新设置考试时间和地点");
        }
        return result;
    }

    /**
     * Created by cwb on 2015/7/24
     * 将学员保存到已有班级中 AJAX实现
     *
     * @param noClassMemberArrayLis 未分班的学员
     * @return confirm.js中 check_arrangeExistClass方法
     */

    @RequestMapping("/pc/addMembersIntoExitClass")
    @ResponseBody
    public Result addMembersIntoExitClass(String className, String noClassMemberArrayLis) {
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(noClassMemberArrayLis);
        if (arrayList == null || arrayList.isEmpty()) {
            result.setStatus(0);
            result.setMessage("成员集合为空，没有需要安排分班的学员");
        } else {
            ClassTeam classTeam = agentService.findClassTeamById(Long.parseLong(className));
            agentService.arrangeClass(arrayList, classTeam);
            result.setStatus(1);
            result.setMessage("分班成功！");
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/30
     * 将班级保存到已有考场中 AJAX实现
     *
     * @param classExamArrayLis 未分考场的班级
     * @return confirm.js中 check_arrangeExistExam方法
     */
    @RequestMapping("/pc/addClassIntoExistExam")
    @ResponseBody
    public Result addClassIntoExistExam(String examName, String classExamArrayLis) {
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(classExamArrayLis);
        if (arrayList == null || arrayList.isEmpty()) {
            result.setStatus(0);
            result.setMessage("班级集合为空，没有需要安排分考场的班级");
        } else {
            Exam exam = agentService.findExamById(Long.parseLong(examName));
            agentService.arrangeExam(arrayList, exam);
            result.setStatus(1);
            result.setMessage("分配考场成功！");
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/24.
     * 显示未分班的学员信息
     * 加载、搜索、上一页、下一页
     *
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param pageNo     第几页
     * @param model      返回客户端集
     * @return yun-daili.html  班级管理选项卡
     */
    @RequestMapping("/pc/loadClassMembers")
    public String loadClassMembers(@AuthenticationPrincipal Agent agent,
                                   @RequestParam(required = false) String keywords,
                                   @RequestParam(required = false) String searchSort,
                                   @RequestParam(required = false) Integer pageNo,
                                   Model model) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Member> pages = agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo < 0 ? 0 : pageNo;
            pages = agentService.findNoClassMembers(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        }
        model.addAttribute("agent", agentService.findAgentById(agent.getId()));
        model.addAttribute("allClassMembersList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "bjgl");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());

        return "/pc/yun-daili";
    }

    /**
     * Created by cwb on 2015/7/29
     * 当前代理商选择已有班级 AJAX实现
     *
     * @param agent 当前代理商
     * @return indexuser.js中 btn_chooseExistClass按钮事件
     */
    @RequestMapping("/pc/loadAvailableClassTeams")
    @ResponseBody
    public Result loadAvailableClassTeams(@AuthenticationPrincipal Agent agent) {
        Result result = new Result();
        List<ClassTeam> existClassList = agentService.findAvailableClassTeams(agent);
        if (existClassList.size() == 0) {
            result.setStatus(0);
            result.setMessage("没有可用的班级，请新建");
        } else {
            result.setStatus(1);
            result.setBody(existClassList);
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 当前代理商选择已有的考场
     *
     * @param agent 当前代理商
     * @return AJAX实现已有考场
     */
    @RequestMapping("/pc/loadAvailableExam")
    @ResponseBody
    public Result loadAvailableExamTeams(@AuthenticationPrincipal Agent agent) {
        Result result = new Result();
        List<Exam> existExamList = agentService.findAvailableExamTeams(agent);
        if (existExamList.size() == 0) {
            result.setStatus(0);
            result.setMessage("没有可用的考场，请新建");
        } else {
            result.setStatus(1);
            result.setBody(existExamList);
        }
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 显示安排考场信息
     *
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param pageNo     第几页
     * @param model      返回客户端集
     * @return yun-daili.html  安排考场
     */
    @RequestMapping("/pc/loadClassExam")
    public String loadClassExam(@AuthenticationPrincipal Agent agent,
                                @RequestParam(required = false) String keywords,
                                @RequestParam(required = false) String searchSort,
                                @RequestParam(required = false) Integer pageNo,
                                Model model) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<ClassTeam> pages = agentService.findClassArrangeExam(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo < 0 ? 0 : pageNo;
            pages = agentService.findClassArrangeExam(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        }
        model.addAttribute("agent", agentService.findAgentById(agent.getId()));
        model.addAttribute("allClassExamList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "apkc");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());

        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/7/30.
     * 显示毕业管理信息
     * 加载、搜索、上一页、下一页
     *
     * @param agent          当前代理商
     * @param keywords       关键词
     * @param searchSort     搜索类型
     * @param passedSortText 是否通过考试的状态
     * @param pageNo         第几页
     * @param model          返回客户端集
     * @return yun-daili.html  毕业管理选项卡
     */
    @RequestMapping("/pc/loadGraduationMembers")
    public String loadGraduationMembers(@AuthenticationPrincipal Agent agent,
                                        @RequestParam(required = false) String keywords,
                                        @RequestParam(required = false) String searchSort,
                                        @RequestParam(required = false) Integer passedSortText,
                                        @RequestParam(required = false) Integer pageNo,
                                        Model model) {
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Member> pages = agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, passedSortText, searchSort);
        long totalRecords = pages.getTotalElements();
        if (pages.getNumberOfElements() == 0) {
            pageNo = pages.getTotalPages() - 1;
            pageNo = pageNo < 0 ? 0 : pageNo;
            pages = agentService.findExamedMembers(agent, pageNo, PAGE_SIZE, keywords, passedSortText, searchSort);
        }
        model.addAttribute("agent", agentService.findAgentById(agent.getId()));
        model.addAttribute("allExamMembersList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "bygl");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("passedSortText", passedSortText == null ? 4 : passedSortText);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());

        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 设置学员通过考试
     *
     * @param id 学员id
     * @return confirm.js中 btn_setExamPass方法
     */
    @RequestMapping("/pc/setExamPass")
    @ResponseBody
    public Result setExamPass(Long id) {
        Result result = new Result();
        agentService.setExamPassById(id, 1);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/7/31.
     * 设置学员未通过考试
     *
     * @param id 学员id
     * @return confirm.js中 btn_setExamNoPass方法
     */
    @RequestMapping("/pc/setExamNoPass")
    @ResponseBody
    public Result setExamNoPass(Long id) {
        Result result = new Result();
        agentService.setExamPassById(id, 2);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/8/1.
     * 班级管理 安排考场 查看班级详细信息
     *
     * @param agent      当前代理商
     * @param keywords   关键词
     * @param searchSort 搜索类型
     * @param pageNo     第几页
     * @param id         查看班级的id
     * @param model      返回客户端集
     * @return 查看班级详细信息
     */
    @RequestMapping("/pc/loadClassTeamDetailInfo")
    public String loadClassTeamDetailInfo(@AuthenticationPrincipal Agent agent,
                                          @RequestParam(required = false) String keywords,
                                          @RequestParam(required = false) String searchSort,
                                          @RequestParam(required = false) Integer pageNo,
                                          Long id, Model model) {
        ClassTeam classTeam = classTeamService.findOneById(id);
        model.addAttribute("classTeamInfo", classTeam);
        model.addAttribute("classTeamDetailcss", true);

        Page<ClassTeam> pages = agentService.findClassArrangeExam(agent, pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        model.addAttribute("agent", agentService.findAgentById(agent.getId()));
        model.addAttribute("allClassExamList", pages);
        model.addAttribute("totalMembers", memberService.searchMembers(agent, pageNo, PAGE_SIZE).getTotalElements());
        model.addAttribute("navigation", "apkc");
        model.addAttribute("searchSort", searchSort == null ? "all" : searchSort);
        model.addAttribute("keywords", keywords);
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("totalPages", pages.getTotalPages());
        return "/pc/yun-daili";
    }

    /**
     * Created by jiashubing on 2015/8/1.
     * 修改班级名称 AJAX
     *
     * @param id        班级ID
     * @param className 修改的名称
     * @return confirm.js中 btn_modifyClassTeamInfo方法
     */
    @RequestMapping("/pc/modifyClassTeamName")
    @ResponseBody
    public Result modifyClassTeamName(Long id, String className) {
        Result result = new Result();
        agentService.modifyClassTeamName(id, className);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }

    /**
     * Created by jiashubing on 2015/8/3.
     * 毕业管理的确认通过
     *
     * @param checkExamMemberList 学员id集合
     * @return confirm.js中 check_ExamMemberEnter方法
     */
    @RequestMapping("/pc/allMemberPassExam")
    @ResponseBody
    public Result allMemberPassExam(String checkExamMemberList) {
        Result result = new Result();
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        ArrayList<Long> arrayList = myJsonUtil.convertJsonBytesToArrayList(checkExamMemberList);
        agentService.allMemberPassExam(arrayList);
        result.setStatus(1);
        result.setMessage("操作成功");
        return result;
    }
//    ==============================================华丽丽的分割线=========================================================

    /**
     * Create by shiliting on 2015,8,5
     * 查找代理商
     *
     * @param searchSort 查询代理商类型
     * @param pageNo     显示代理商页数
     * @param keywords   查询代理商关键字
     * @param model      返回的参数
     * @return agents.html
     */

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/searchAgents")
    public String searchAgents(@RequestParam(required = false, value = "searchSort") String searchSort,
                               @RequestParam(required = false) Integer pageNo,
                               @RequestParam(required = false) String keywords,
                               Model model) {
        String turnPage = "/backend/agents";
        if (pageNo == null || pageNo < 0) {
            pageNo = 0;
        }
        Page<Agent> pages = agentService.searchAgent(pageNo, PAGE_SIZE, keywords, searchSort);
        long totalRecords = pages.getTotalElements();
        int numEl = pages.getNumberOfElements();
        if (numEl == 0) {
            pageNo = pages.getTotalPages() - 1;
            if (pageNo < 0) {
                pageNo = 0;
            }
            pages = agentService.searchAgent(pageNo, PAGE_SIZE, keywords, searchSort);
            totalRecords = pages.getTotalElements();
        }
        model.addAttribute("allAgentList", pages);
        model.addAttribute("totalPages", pages.getTotalPages());
        model.addAttribute("pageNo", pageNo);
        model.addAttribute("keywords", keywords);
        model.addAttribute("totalRecords", totalRecords);
        model.addAttribute("searchSort", searchSort);
        return turnPage;
    }


    /**
     * Create by shiliting on 2015,8,20
     * 进入修改代理商页面
     *
     * @param id    代理商ID
     * @param model 返回的参数
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/modifyAgent")
    public String modifyAgent(Long id, Model model) throws Exception {
        Agent agent = agentService.findOneById(id);
        if (agent.getPictureUri() != null && !"".equals(agent.getPictureUri())) {
            agent.setPictureUri(staticResourceService.getResource(agent.getPictureUri()).toString());
        }
        model.addAttribute("agent", agent);
        return "/backend/modifyAgent";
    }

    /**
     * Create by shiliting on 2015,8,20
     * 跳转到添加代理商页面
     *
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/addAgent")
    public String addAgent() {
        return "/backend/newagent";
    }


    /**
     * Create by shiliting on 2015,8,20
     * 添加代理商
     *
     * @param name      代理商姓名
     * @param loginName 登录用户名
     * @param sex       性别
     * @param area      区域
     * @param phoneNo   手机号
     * @param level     代理商等级
     * @param file      代理商照片
     * @return 重定向到搜索代理商
     * @throws Exception 图片出错异常
     */
    /*@PreAuthorize("hasRole('ADMIN')")*/
    @RequestMapping(value = "/backend/addSaveAgent", method = RequestMethod.POST)
    public String addSaveAgent(String areaId, String name, String loginName, int sex, String area, String phoneNo, String level, int certificateNumber, @RequestParam("smallimg") MultipartFile file) throws Exception {

        String fileName = null;
        //文件格式判断
        BufferedImage image = ImageIO.read(file.getInputStream());
        if (image != null) {
            fileName = StaticResourceService.AGENT_ICON + UUID.randomUUID().toString() + ".png";
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            ImageIO.write(image, "png", buffer);
            staticResourceService.uploadResource(fileName, new ByteArrayInputStream(buffer.toByteArray()));
        }

        Agent agent = new Agent();
        agent.setAreaId(areaId);
        agent.setPictureUri(fileName);
        agent.setArea(area);
        agent.setName(name);
        agent.setRegisterDate(new Date());
        agent.setPhoneNo(phoneNo);
        agent.setLevel(level);
        agent.setCertificateNumber(certificateNumber);
        agent.setLoginName(loginName);
        agent.setSex(sex);
        loginService.newLogin(agent, "123456");
        return "redirect:/backend/searchAgents";
    }


    /**
     * Create by shiliting on 2015,8,20
     * 修改一位代理商
     *
     * @param id        代理商ID
     * @param name      代理商姓名
     * @param loginName 登录用户名
     * @param sex       性别
     * @param area      区域
     * @param phoneNo   手机号
     * @param level     代理商等级
     * @param file      代理商照片
     * @return 重定向到/backend/searchAgents
     * @throws Exception 文件格式出错
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping("/backend/modifySaveAgent")
    public String modifySaveAgent(String areaId, Long id, String name, String loginName, int sex, String area, String phoneNo, String level, int certificateNumber, @RequestParam("smallimg") MultipartFile file) throws Exception {
        Agent agent = agentService.findOneById(id);
        if (file.getSize() != 0) {
            if (ImageIO.read(file.getInputStream()) == null) {
                throw new Exception("不是图片！");
            }
            String agentPicUrl = agentService.findOneById(id).getPictureUri();
            if (agentPicUrl != null && !"".equals(agentPicUrl)) {
                URI agentPicUri = staticResourceService.getResource(agentPicUrl);
                //TODO 可能有问题，不一定为null
                if (agentPicUri != null) {
                    staticResourceService.deleteResource(agentPicUri);
                }
            }
            String fileName = StaticResourceService.AGENT_ICON + UUID.randomUUID().toString() + ".png";
            staticResourceService.uploadResource(fileName, file.getInputStream());
            agent.setPictureUri(fileName);
        }
        agent.setArea(area);
        agent.setAreaId(areaId);
        agent.setName(name);
        agent.setPhoneNo(phoneNo);
        agent.setLevel(level);
        agent.setCertificateNumber(certificateNumber);
        agent.setLoginName(loginName);
        agent.setSex(sex);
        agentService.modifyAgent(agent);
        return "redirect:/backend/searchAgents";
    }

    /**
     * 异步检测代理商编号是否可用
     *
     * @param areaId
     * @return
     */
    @RequestMapping("/backend/checkAreaId")
    @ResponseBody
    public Result checkAreaId(String areaId) {
        Result result = new Result();
        boolean areaIdAvailable = agentService.checkAreaIdAvailable(areaId);
        if (!areaIdAvailable) {
            result.setStatus(0);
            result.setMessage("该编号已存在");
        } else {
            result.setStatus(1);
        }
        return result;
    }

    @RequestMapping("/backend/checkOtherAreaId")
    @ResponseBody
    public Result checkOtherAreaId(String areaId, long agentId) {
        Result result = new Result();
        boolean areaIdAvailable = agentService.checkAreaIdAvailable(areaId);
        if (!areaIdAvailable) {
            Agent agent = agentService.findOneById(agentId);
            if (agent != null) {
                if (areaId.equals(agent.getAreaId())) {
                    result.setStatus(1);
                } else {
                    result.setStatus(0);
                    result.setMessage("该编号已存在");
                }
            }
        } else {
            result.setStatus(1);
        }
        return result;
    }

   /* @RequestMapping("/mobile/haierPay")
    public ModelAndView showPayPage(HttpServletRequest request) throws Exception {
        String viewName = "/mobile/jiaofei_mobile";
        Map<String, Object> model = new HashMap<>();
        SortedMap<Object,Object> params = new TreeMap<>();
        params.put("appid","wx9560ee416a5af27c");
        params.put("mch_id","1275249701");
        params.put("nonce_str","5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        params.put("body","微商中级课程");
        params.put("out_trade_no",create());
        params.put("total_fee","1");
        params.put("spbill_create_ip",StringHelper.getIp(request));
        params.put("notify_url","http://www.baidu.com");
        params.put("trade_type","NATIVE");
        String sign = StringHelper.createSign("UTF-8",params,"118918ckmf108szzhygcyzwmyhtkjywl");
        params.put("sign",sign);
        XmlMapper xmlMapper = new XmlMapper();
        String xmlResult = xmlMapper.writeValueAsString(params);
        String result = sendPost("https://api.mch.weixin.qq.com/pay/unifiedorder",xmlResult,"text/xml");
        WechatPay wechatPay = xmlMapper.readValue(result, WechatPay.class);
        model.put("wechatPay",wechatPay);
        ModelAndView modelAndView = new ModelAndView(viewName, model);
        return modelAndView;
    }*/


    /**
     * 以时间戳为基础生成20位随机序列号
     * @return String
     */
    public static  String create() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String date = sdf.format(new Date());
        Random random = new Random();
        String code="";
        //随机产生6位数字的字符串
        for (int i=0;i<6;i++){
            String rand=String.valueOf(random.nextInt(10));
            code+=rand;
        }
        return date+code;
    }

    /**
     * 创建一个sign签名
     *
     * @param params 代签名参数，key排序的treemap,值为""或者null值的参数将被排除
     * @param prefix
     * @param suffix
     * @return
     */
    public static String build(Map<String, String> params, String prefix, String suffix) throws UnsupportedEncodingException {
        if (prefix == null)
            prefix = "";
        if (suffix == null)
            suffix = "";
        StringBuilder stringBuilder = new StringBuilder(prefix);
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            if(next.getValue()!=null&&!"".equals(next.getValue())) {
                stringBuilder.append(next.getKey()).append(next.getValue());
            }
        }
        stringBuilder.append(suffix);
        return DigestUtils.md5Hex(stringBuilder.toString().getBytes("utf-8"));
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param targetUrl 发送请求的 URL
     * @param requestParam      数据
     * @param dataType  数据格式
     *                  text/xml->xml数据
     *                  application/x-javascript->json对象
     *                  application/x-www-form-urlencoded->表单数据
     * @return 所代表远程资源的响应结果
     */
    public String sendPost(String targetUrl, String requestParam, String dataType) throws Exception {
        String result = "";

        byte[] data = requestParam.getBytes("UTF-8");
        URL url = new URL(targetUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", dataType+";charset=UTF-8");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        conn.setConnectTimeout(5 * 1000);
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        int statusCode = conn.getResponseCode(); //响应代码 200表示成功
        if (statusCode == 200) {
            InputStream inStream = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inStream.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            result = baos.toString("UTF-8");
            baos.close();
            inStream.close();
        }
        return result;
    }
}

package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.ClassTeamRepository;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.test.TestWebConfig;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * Created by jiashubing on 2015/7/17.
 *
 * @author jiashubing
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class MemberControllerTest extends WebTestBase {
    private static final Log log = LogFactory.getLog(MemberControllerTest.class);
    @Autowired
    private LoginService loginService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ClassTeamRepository classTeamRepository;
    //  /pc/searchMembers  没有登录无法访问，没有Agent权限无法访问
    //  /pc/searchMembers 最多展示 10 个数据
    //  /

    @Test
    @Rollback
    public void searchMembersTest() throws Exception {
        //准备测试环境
        String password = UUID.randomUUID().toString();
        String complexKeyword = UUID.randomUUID().toString();  //关键字
        String memberUsername = UUID.randomUUID().toString();
        String editorUsername = UUID.randomUUID().toString();
        String ManagerUsername = UUID.randomUUID().toString();
        String agentUsername=UUID.randomUUID().toString();

        Agent agent=new Agent();
        agent.setLoginName(agentUsername);
        agent.setArea("杭州");
        agent.setRegisterDate(new Date());
        agent.setName("测试代理商");
        agent.setCertificateNumber(1000);
        agent.setSendCertificateNumber(100);
        agent.setLevel("一级");
        agent.setAreaId(UUID.randomUUID().toString());
        agent.setCorporation("火图科技法人代表");


        Member member = new Member();
        member.setLoginName(memberUsername);

        Editor editor = new Editor();
        editor.setLoginName(editorUsername);

        Manager manager = new Manager();
        manager.setLoginName(ManagerUsername);

        loginService.newLogin(member, password);
        loginService.newLogin(editor, password);
        loginService.newLogin(manager, password);
        loginService.newLogin(agent,password);

        



        //准备需要测试的Member
        // 10+随机10

        String type="all";

        Random random = new Random();

        int count = 20 + random.nextInt(20);
        int sum=count;
        int countHaveKeywordsName=0;
        int countHaveKeywordsClass=0;
        int countHaveKeywordsPnoto=0;
        int pass=0;
        int noPass=0;

        ClassTeam classTeamTest=new ClassTeam();
        classTeamTest.setAgent(agent);
        classTeamTest.setClassName("测试班级" + complexKeyword + System.currentTimeMillis());
        classTeamTest.setId((long) 1);
        classTeamRepository.save(classTeamTest);
        ArrayList<Member> containsMembers = new ArrayList<>();//拿到包含关键字的member记录

        while (count-- > 0) {
            member = new Member();
            member.setLastLoginDate(new Date());
            member.setRealName("123");
            member.setPhoneNo("18045879654");
            member.setLastLoginDate(new Date());
            member.setApplyDate(new Date());
            member.setSex(random.nextInt(2));
            member.setAgent(agent);
            boolean containKeyword=random.nextBoolean();
            if(containKeyword){
                switch (random.nextInt(3)){
                    case 0:
                        member.setRealName("测试姓名" + complexKeyword + System.currentTimeMillis());
                        member.setPayed(true);
                        countHaveKeywordsName++;
                        break;
                    case 1:
                        member.setPhoneNo("测试手机"+complexKeyword+System.currentTimeMillis());
                        member.setCertificateStatus(1);
                        countHaveKeywordsPnoto++;
                        break;
                    case 2:
                        member.setTheClass(classTeamTest);
                        if(random.nextInt(2)+1==1){
                            member.setPassed(1);
                            pass++;
                        }else{
                            member.setPassed(2);
                            noPass++;
                        }
                        countHaveKeywordsClass++;
                        break;
                }
            }
            member = memberRepository.save(member);

            if (containKeyword)
                containsMembers.add(member);
        }
        //准备测试环境END
        System.out.println("关键字："+complexKeyword);
        System.out.println("名字："+countHaveKeywordsName);
        System.out.println("班级："+countHaveKeywordsClass);
        System.out.println("电话："+countHaveKeywordsPnoto);
        System.out.println("总数："+containsMembers.size());
        System.out.println("List："+containsMembers.toString());
        mockMvc.perform(
                get("/pc/searchMembers")
        )
                .andExpect(status().isFound())
        .andExpect(redirectedUrlPattern("**/pc/index"));

        mockMvc.perform(
                get("/pc/loadPersonalCenter")
        )       .andDo(print())
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/pc/index"));


        mockMvc.perform(
                get("/pc/loadPersonalCenter")
                .session(loginAs(agentUsername, password))
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/pc/searchMembers"));


        mockMvc.perform(
                get("/pc/searchMembers")
                .session(loginAs(agentUsername, password))
        )
                .andExpect(status().isOk());


        Map<String, Object> model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))


        ) .andExpect(status().isOk())
          .andReturn().getModelAndView().getModel();
        Page<Member> pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("记录数为固定10条",10,pages.getSize());



         model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                 .param("searchSort","all")
                 .param("keywords",complexKeyword)
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("包含关键字的记录数",containsMembers.size(),pages.getTotalElements());



        model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                        .param("searchSort","all")
                        .param("keywords","姓名"+complexKeyword)
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("包含关键字的姓名记录数",countHaveKeywordsName,pages.getTotalElements());

        model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                        .param("searchSort","all")
                        .param("keywords","班级"+complexKeyword)
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("包含关键字的班级记录数",countHaveKeywordsClass,pages.getTotalElements());

        model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                        .param("searchSort","all")
                        .param("keywords","手机"+complexKeyword)
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("包含关键字的电话记录数",countHaveKeywordsPnoto,pages.getTotalElements());


        model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                        .param("searchSort","all")
                        .param("keywords","手机"+complexKeyword)
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("包含关键字的电话记录数",countHaveKeywordsPnoto,pages.getTotalElements());

        model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                        .param("searchSort","certificateStatus")
                        .param("keywords","1")
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("已经领证的记录数",countHaveKeywordsPnoto,pages.getTotalElements());

        model=  mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
                        .param("searchSort","passed")
                        .param("keywords","1")
        ) .andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();
        pages=(Page<Member>)model.get("allMemberList");
        Assert.assertEquals("通过考试的记录数",pass,pages.getTotalElements());

        String ViewName=mockMvc.perform(
                get("/pc/searchMembers")
                        .session(loginAs(agentUsername, password))
        )
                .andExpect(status().isOk())
                .andReturn().getModelAndView().getViewName();
        Assert.assertEquals("返回的视图名字是否相等","/pc/yun-daili",ViewName);

    }
    @Test
    public void searchMember() throws Exception{
        String password=UUID.randomUUID().toString();
        String adminName=UUID.randomUUID().toString();
        Manager manager=new Manager();
        manager.setLoginName(adminName);
        loginService.newLogin(manager,password);
        mockMvc.perform(
                get("/backend/searchMembers")
                        .session(loginAs(adminName, password))
        ).andExpect(status().isOk());
    }


    @Test
    public void getCertificateByMemberIdTest() throws Exception{
        String password=UUID.randomUUID().toString();
        String adminName=UUID.randomUUID().toString();
        Manager manager=new Manager();
        manager.setLoginName(adminName);
        loginService.newLogin(manager,password);
        mockMvc.perform(
                get("/backend/getCertificateByMemberId")
                        .session(loginAs(adminName, password))
                .param("id","3919")
        ).andDo(print()).andExpect(status().isOk());
    }


    @Test
    @Rollback
    public void issueCertificate() throws Exception{
        String password=UUID.randomUUID().toString();
        String adminName=UUID.randomUUID().toString();
        Manager manager=new Manager();
        manager.setLoginName(adminName);
        loginService.newLogin(manager,password);
        mockMvc.perform(
                get("/backend/issueCertificate")
                        .session(loginAs(adminName, password))
                        .param("certificateNo","123456789")
                        .param("memberId","4")
                        .param("certificateId","52")
        ).andDo(print()).andExpect(status().isFound());
    }



}

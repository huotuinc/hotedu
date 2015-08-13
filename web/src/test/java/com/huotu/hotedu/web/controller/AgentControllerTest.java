package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.entity.Agent;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.test.TestWebConfig;
import libspringtest.SpringWebTest;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


/**
 * Created by Administrator on 2015/7/8.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class AgentControllerTest extends SpringWebTest{

    protected MockHttpSession loginAs(String userName, String password) throws Exception {
        MockHttpSession session = (MockHttpSession) this.mockMvc.perform(get("/"))
                .andReturn().getRequest().getSession(true);
        session = (MockHttpSession) this.mockMvc.perform(post("/login").session(session)
                .param("username", userName).param("password", password))
                .andDo(print())
                .andReturn().getRequest().getSession();

        saveAuthedSession(session);
        return session;
    }
    @Autowired
    AgentService agentService;

    @Test
    public void showMemberByAgentTest() throws Exception {
        mockMvc.perform(get("/pc/loadAgents"));
    }

    /**
     * 测试将学员保存到新建班级中
     * @throws Exception
     */
    @Test
    @Rollback
    public void addSaveNewClassTeamTest() throws Exception {
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add((long)1);
        arrayList.add((long)2);
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        String stringArrayList = myJsonUtil.convertObjectToJsonBytes(arrayList);
        mockMvc.perform(
                post("/pc/addSaveNewClassTeam")
                        .param("className", "测试1234")
                        .param("arrayList", stringArrayList)
        ).andDo(print());
    }

    /**
     * 测试将学员保存到已有班级中
     * @throws Exception
     */
    @Test
    @Rollback
    public void addSaveExistClassTeamTest() throws Exception {
        ArrayList<Long> arrayList = new ArrayList<>();
        arrayList.add((long)1);
        arrayList.add((long)2);
        ClassTeam classTeam = new ClassTeam();
        classTeam.setClassName("测试nihao");
        ClassTeam ct = agentService.addClassTeam(classTeam);
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        String stringArrayList = myJsonUtil.convertObjectToJsonBytes(arrayList);
        mockMvc.perform(
                post("/pc/addSaveExistClassTeam")
                        .param("classId", ct.getId().toString())
                        .param("arrayList", stringArrayList)
        ).andDo(print());
    }

    /**.
     * 1.测试新增学员
     */
    @Test
    @Rollback
    @Ignore
    public void agentTest() throws Exception{
        Agent agen = new Agent();
        agen.setName("测试agent");
        agen.setArea("杭州");
        Agent agent = agentService.addAgent(agen);
        mockMvc.perform(
                post("/pc/addMembers")
//                .param("agent",agent)
                .param("realName", "测试测试名字")
                .param("sex", "0")
                .param("phoneNo", "18011122233")
        ).andDo(print());
    }
}
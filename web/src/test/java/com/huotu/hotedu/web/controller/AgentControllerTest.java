package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.entity.ClassTeam;
import com.huotu.hotedu.service.AgentService;
import com.huotu.hotedu.test.TestWebConfig;
import libspringtest.SpringWebTest;
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
    public void addSaveNewClassTeam() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        String stringArrayList = myJsonUtil.convertObjectToJsonBytes(arrayList);
        mockMvc.perform(
                post("pc/addSaveNewClassTeam")
                        .param("className", "1234")
                        .param("arrayList", stringArrayList)
        ).andDo(print());
    }

    /**
     * 测试将学员保存到已有班级中
     * @throws Exception
     */
    @Test
    @Rollback
    public void addSaveOldClassTeam() throws Exception {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.add(1);
        arrayList.add(2);
        ClassTeam classTeam = new ClassTeam();
        classTeam.setClassName("nihao");;
        ClassTeam ct = agentService.addClassTeam(classTeam);
        MyJsonUtil myJsonUtil = new MyJsonUtil();
        String stringArrayList = myJsonUtil.convertObjectToJsonBytes(arrayList);
        mockMvc.perform(
                post("pc/addSaveOldClassTeam")
                        .param("classId", ct.getId().toString())
                        .param("arrayList", stringArrayList)
        ).andDo(print());
    }


}
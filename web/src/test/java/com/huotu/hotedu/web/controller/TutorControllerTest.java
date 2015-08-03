package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.test.TestWebConfig;
import libspringtest.SpringWebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
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
public class TutorControllerTest extends SpringWebTest{

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

    /**
     * 搜索特定日期之间的数据
     * @throws Exception
     */
    @Test
    @Rollback
    public void date() throws Exception{
        mockMvc.perform(
                post("/backend/searchTutor")
                        .param("date", "date")
                        .param("keywords", "1234")
                        .param("dateStart", "2014.10.10")
                        .param("dateEnd", "2014.10.11")
        ).andDo(print());
    }

    @Test
    @Rollback
    public void newTutorCheck() throws Exception{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(getClass().getResourceAsStream("testUpload.jpg"), buffer);
        mockMvc.perform(
                fileUpload("/backend/addSaveTutor")
                        .file("smallimg", buffer.toByteArray())
                        .param("name", "张飞4")
                        .param("qualification","本科")
                        .param("area","杭州")
                        .param("introduction","武将")
        ).andDo(print());
    }

    @Test
    public void testAddSaveTutor() throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(getClass().getResourceAsStream("testUpload.jpg"),buffer);
        mockMvc.perform(
                fileUpload("/backend/addSaveTutor")
                .file("smallimg",buffer.toByteArray())
        );

    }

    @Test
    public  void testDelTutor() throws Exception{
        mockMvc.perform(get("/backend/delTutor?n=0&id=2&sumpage=1&searchSort='all'&keywords=''&dateStart=''&dateEnd=''&sumElement=4"));
    }
    @Test
    public  void testSaveTutorByTime() throws Exception{
        mockMvc.perform(
                get("/backend/searchTutor?searchSort='all'&keywords=''&dateStart=''&dateEnd=''")
        );
    }
}
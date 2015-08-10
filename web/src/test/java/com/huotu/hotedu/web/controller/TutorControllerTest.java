package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Editor;
import com.huotu.hotedu.repository.TutorRepository;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.test.TestWebConfig;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by Administrator on 2015/7/8.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class TutorControllerTest extends WebTestBase {
    @Autowired
    LoginService loginService;
    @Autowired
    TutorRepository tutorRepository;
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
//    @Rollback
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


    @Test
    @Ignore
    public  void errorTest() throws Exception{
        String editName= UUID.randomUUID().toString();
        String passWord=UUID.randomUUID().toString();
        Editor editor=new Editor();
        editor.setLoginName(editName);
        loginService.newLogin(editor,passWord);
        mockMvc.perform(
                get("/backend/errorTest")
                .session(loginAs(editName,passWord))

        ).andDo(print());
    }
}
package com.huotu.hotedu.test.web;

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

import java.io.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by jiashubing on 2015/7/17.
 *
 * @author jiashubing
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class TutorControllerTest extends SpringWebTest {

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

//    @Test
//    public void date() throws {
//        Date nowDate = new Date();
//        mockMvc.perform(
//                get("/backend/searchTutor")
//                        .param("date", "date").param("keywords", "1234")
//                        .param("dateStart", nowDate).param("dateEnd", nowDate)
//        .andDo(print());
//    }


    @Test
    public void newCheck() throws Exception{
//        mockMvc.perform(
//                post("/backend/saveEditenterprise")
//                .param("title","杭州火图科技12345")
//                .param("introduction","测试")
//        ).andDo(print());
//        mockMvc.perform(
//                get("/backend/loadLink")
//        ).andDo(print());
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

}

package com.huotu.hotedu.test.web;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.MessageContentRepository;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.test.TestWebConfig;
import libspringtest.SpringWebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class MessageContentControllerTest extends SpringWebTest {

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
    private LoginService loginService;
    @Autowired
    private MessageContentRepository messageContentRepository;




    private void checkMemeber(String name) {
        try {
            loginService.loadUserByUsername(name);
        } catch (UsernameNotFoundException ex) {
            Member member = new Member();
            member.setLoginName(name);
            loginService.newLogin(member, name);
        }
    }

    @Test
    public void newMessageCheck() throws Exception{
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(getClass().getResourceAsStream("testUpload.jpg"), buffer);
        mockMvc.perform(
                fileUpload("/backend/addSaveMessagecontent")
                .file("smallimg", buffer.toByteArray())
                .param("title", "1234567890")
                .param("content", "asdfghjkll")
                .param("top", "false")
        ).andDo(print());
    }
}

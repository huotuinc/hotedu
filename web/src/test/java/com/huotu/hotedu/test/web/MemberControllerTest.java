package com.huotu.hotedu.test.web;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.service.MemberService;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.test.TestWebConfig;
import com.jcraft.jsch.Session;
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
public class MemberControllerTest extends SpringWebTest {

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

    @Test
    public void detailMember() throws Exception {
        //MockHttpSession session = loginAs("admin","admin");
        // System.out.println("session = "+ session);
        mockMvc.perform(
                get("pc/detailMember")
        ).andDo(print());
    }

    @Test
    public void delMember() throws Exception{
        mockMvc.perform(
                delete("pc/delMember?id=20")
        ).andDo(print());
    }
}
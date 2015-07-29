package com.huotu.hotedu;

import com.huotu.hotedu.test.TestWebConfig;
import com.huotu.hotedu.web.config.SecurityConfig;
import libspringtest.SpringWebTest;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * web方向测试基类
 * @author CJ
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
@Transactional
public class WebTestBase extends SpringWebTest{

    protected MockHttpSession loginAs(String userName, String password) throws Exception {
        MockHttpSession session = (MockHttpSession) this.mockMvc.perform(get("/"))
                .andReturn().getRequest().getSession(true);
        session = (MockHttpSession) this.mockMvc.perform(post(SecurityConfig.LoginURI).session(session)
                .param("username", userName).param("password", password))
//                .andDo(print())
                .andReturn().getRequest().getSession();

        saveAuthedSession(session);
        return session;
    }

}

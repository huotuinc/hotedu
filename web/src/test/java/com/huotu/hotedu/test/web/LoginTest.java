package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.test.TestWebConfig;
import com.huotu.hotedu.web.DispatcherServletInitializer;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
public class LoginTest extends WebTestBase {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ExamGuideRepository examGuideRepository;

    @Test
    public void index() throws Exception {
        mockMvc.perform(
                get("/pc/index")
        )
                .andDo(print());
    }
    @Test
    public void login() throws  Exception{

        mockMvc.perform(
                get("/backend/index")
        ).andDo(print())
        ;

    }
    private void checkMemeber(String name) {
        try {
            loginService.loadUserByUsername(name);
        } catch (UsernameNotFoundException ex) {
            Member member = new Member();
            member.setLoginName(name);
            loginService.newLogin(member, name);
        }
    }
}

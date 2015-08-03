package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.service.LoginService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
public class WebFlowTest extends WebTestBase {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ExamGuideRepository examGuideRepository;
    @Autowired
    private Environment env;

    @Test
    @Rollback
    public void login() throws Exception {
        String username = UUID.randomUUID().toString();
        String password = UUID.randomUUID().toString();
        Manager manager = new Manager();
        manager.setLoginName(username);

        manager = loginService.newLogin(manager,password);

        mockMvc.perform(
                get("/backend/searchExamGuide")
        )
                .andExpect(status().isFound());//尚未登录是302 已登录则是403
        MockHttpSession session = loginAs(username,password);
        mockMvc.perform(
                get("/backend/searchExamGuide")
                .session(session)
        )
                .andExpect(status().isOk());
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

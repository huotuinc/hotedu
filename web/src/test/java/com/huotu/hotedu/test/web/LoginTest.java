package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.service.LoginService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
                post("/pc/index")
                .session(loginAs("123456", "4578"))
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

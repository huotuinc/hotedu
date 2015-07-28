package com.huotu.hotedu.test.web;

import com.huotu.hotedu.entity.Editor;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.service.LoginService;
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

import java.util.UUID;

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
    @Autowired
    private LoginService loginService;
    @Autowired
    private ExamGuideRepository examGuideRepository;


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
    @Rollback
    public void chargelMember() throws Exception {

        String password = UUID.randomUUID().toString();
        String memberUsername = UUID.randomUUID().toString();
        String editorUsername = UUID.randomUUID().toString();
        String ManagerUsername = UUID.randomUUID().toString();

        Member member = new Member();
        member.setLoginName(memberUsername);

        Editor editor = new Editor();
        editor.setLoginName(editorUsername);

        Manager manager = new Manager();
        manager.setLoginName(ManagerUsername);

        loginService.newLogin(member, password);
        loginService.newLogin(editor, password);
        loginService.newLogin(manager, password);
        mockMvc.perform(
                post("pc/chargeMembers") .session(loginAs(editorUsername, password))
                        .param("arrayIds", "[\"5\",\"6\",\"9\",\"10\",\"11\",\"12\",\"13\",\"14\",\"15\",\"16\"]")
        ).andDo(print());
    }

    @Test
    @Rollback
    public void delMember() throws Exception{
        mockMvc.perform(
                delete("pc/delMember?id=20")
        ).andDo(print());
    }
}

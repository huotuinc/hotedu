package com.huotu.hotedu.test.web;

import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.LoginRepository;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.repository.QaRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
public class ExamGuideControllerTest extends SpringWebTest {

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
    private MemberRepository memberRepository;
    @Autowired
    private LoginRepository loginRepository;
    @Autowired
    private QaRepository qaRepository;

    // /backend/loadExamGuide 非登录状态无法访问
    // /backend/loadExamGuide 最多展示 n 个数据 n 自定义
    // /backend/delExamGuide  删除&查询
    // ?id=1 要删的记录的id
    // &n=0 删除完成以后显示的页索引   （3）
    // &keywords= 删除以后 显示指定关键字搜索页面
    // &sumpage=4 总共能分的页数    4页 31条数据   30
    // &sumElement=39

    // 删除
    // id 要删除的ID
    // 查询
    // &keywords= 删除以后 显示指定关键字搜索页面
    // &n=0 删除完成以后显示的页索引
    // &paging=10 每页10条

    @Test
    public void examGuideTest() throws Exception {
        mockMvc.perform(
                get("/backend/loadExamGuide")
        )
                .andDo(print())
//                .andExpect(status().isForbidden())
        ;
    }

    @Test
    public void index() throws Exception {
        mockMvc.perform(
                get("/backend/pageLink?n=0;keywords='';sumpage=1")
        ).andDo(print());
    }
    @Test
    public void login() throws  Exception{


/*        mockMvc.perform(
                get("/backend/search/examGuide")
        ).andDo(print())
        ;*/
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

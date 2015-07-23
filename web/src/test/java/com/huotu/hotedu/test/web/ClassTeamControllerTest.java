package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.ClassTeamRepository;
import com.huotu.hotedu.repository.LoginRepository;
import com.huotu.hotedu.repository.MemberRepository;
import com.huotu.hotedu.repository.QaRepository;
import com.huotu.hotedu.service.ClassTeamService;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.web.config.SecurityConfig;
import com.huotu.hotedu.web.controller.ClassTeamController;
import com.huotu.hotedu.web.controller.ExamGuideController;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.Rollback;

import java.time.Instant;
import java.util.*;
import java.util.stream.BaseStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
public class ClassTeamControllerTest extends WebTestBase {

    private static final Log log = LogFactory.getLog(ClassTeamControllerTest.class);

    @Autowired
    private ClassTeamService classTeamService;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ClassTeamRepository classTeamRepository;

    // /backend/searchExamGuide 非登录状态无法访问
    //
    // /backend/searchExamGuide 最多展示 n 个数据 n 自定义
    //
    // /backend/delExamGuide  删除&查询
    // 删除
    // id 要删除的ID
    // 查询
    // &keywords= 删除以后 显示指定关键字搜索页面
    // &n=0 删除完成以后显示的页索引
    // &paging=10 每页10条

    // /backend/searchExamGuide
    // 查询
    // &keywords= 删除以后 显示标题包含指定关键字搜索页面
    // &n=0 删除完成以后显示的页索引
    // &paging=10 每页10条

    // 修改的操作 需要ROLE_EDITOR 权限
    // Editor 是可以修改的
    // 但是Member 是不可以的


    @Test
    @Rollback
    public void examGuideTest() throws Exception {

/**
 *测试环境配置
 *
 * */
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
        //准备需要测试的ClassTeam
        // 10+随机10
        String complexKeyword = UUID.randomUUID().toString();
        // TODO 删除目前包含这个奇葩关键字的ExamGuide

        Random random = new Random();

        int count = 20 + random.nextInt(20);

        ArrayList<ClassTeam> containsClassTeam = new ArrayList<>();//拿到包含关键字的examGuide记录


        while (count-- > 0) {
            ClassTeam classTeam = new ClassTeam();
            Exam exam = new Exam();
            exam.setExamAddress("杭州");
            classTeam.setExam(exam);
            classTeam.setAgent(classTeam.getAgent());

            boolean containKeyword = random.nextBoolean();
            if (containKeyword) {
                classTeam.setExam(classTeam.getExam());
            } else {
                classTeam.setExam(classTeam.getExam());
            }

            classTeam = classTeamRepository.save(classTeam);

            if (containKeyword)
                containsClassTeam.add(classTeam);
        }
        //准备测试环境END

        /**
         * 测试在没有登录的情况下不能访问查询操作
         */
        mockMvc.perform(
                get("/backend/searchClassTeam")
        )
                .andExpect(status().isFound())
                .andDo(print())
                .andExpect(redirectedUrlPattern("**/" + SecurityConfig.LoginURI));


        /**
         * 测试登录后能否正常访问
         * 测试分页是否显示正确
         */
        int totalCount = (int) classTeamRepository.count();
        int defaultPageSize = ClassTeamController.PAGE_SIZE;
        int pages = (totalCount + defaultPageSize - 1) / defaultPageSize;
        mockMvc.perform(
                get("/backend/searchClassTeam")
                        .session(loginAs(memberUsername, password))
        )
                .andExpect(status().isForbidden());
        mockMvc.perform(
                get("/backend/searchClassTeam")
                        .session(loginAs(memberUsername, password))
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute("allclassteamList", new BaseMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        // 这个是一个Page类型的 第二长度必须有..
                        if (o == null || !(o instanceof Page)) {
                            log.error("非Page类型");
                            return false;
                        }
                        Page page = (Page) o;
                        return page.getContent().size() == ClassTeamController.PAGE_SIZE;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }))
                .andExpect(model().attribute("totalPages", pages));
    }


        /**
         * 测试
         *
         */






}

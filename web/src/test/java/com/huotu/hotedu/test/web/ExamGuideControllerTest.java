package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Editor;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Member;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.web.controller.ExamGuideController;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
public class ExamGuideControllerTest extends WebTestBase {

    private static final Log log = LogFactory.getLog(ExamGuideControllerTest.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private ExamGuideRepository examGuideRepository;

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

        //准备测试环境
        String password = UUID.randomUUID().toString();

        String memberUsername = UUID.randomUUID().toString();
        String editorUsername = UUID.randomUUID().toString();

        Member member = new Member();
        member.setLoginName(memberUsername);

        Editor editor = new Editor();
        editor.setLoginName(editorUsername);

        loginService.newLogin(member, password);
        loginService.newLogin(editor, password);

        //准备需要测试的ExamGuide
        // 10+随机10
        String complexKeyword = UUID.randomUUID().toString();
        // TODO 删除目前包含这个奇葩关键字的ExamGuide

        Random random = new Random();

        int count = 20 + random.nextInt(20);

        ArrayList<ExamGuide> containsExamGuides = new ArrayList<>();//拿到包含关键字的examGuide记录

        while (count-- > 0) {
            ExamGuide examGuide = new ExamGuide();
            examGuide.setLastUploadDate(new Date());
            examGuide.setContent("123");
            examGuide.setIsTop(random.nextBoolean());
            boolean containKeyword = random.nextBoolean();
            if (containKeyword) {
                examGuide.setTitle("测试t" + complexKeyword + System.currentTimeMillis());
            } else {
                examGuide.setTitle("测试t" + System.currentTimeMillis());
            }

            examGuide = examGuideRepository.save(examGuide);

            if (containKeyword)
                containsExamGuides.add(examGuide);
        }
        //准备测试环境END

        mockMvc.perform(
                get("/backend/searchExamGuide")
        )
                .andExpect(status().isFound());

        int totalCount =  (int)examGuideRepository.count();
        int defaultPageSize = ExamGuideController.PAGE_SIZE;
        int pages = (totalCount+defaultPageSize-1)/defaultPageSize;
        mockMvc.perform(
                get("/backend/searchExamGuide")
                        .session(loginAs(memberUsername, password))
        )
                .andExpect(status().isOk())
                .andExpect(model().attribute("allGuideList", new BaseMatcher<Object>() {
                    @Override
                    public boolean matches(Object o) {
                        // 这个是一个Page类型的 第二长度必须有..
                        if (o == null || !(o instanceof Page)) {
                            log.error("非Page类型");
                            return false;
                        }
                        Page page = (Page) o;
                        return page.getContent().size() == ExamGuideController.PAGE_SIZE;
                    }

                    @Override
                    public void describeTo(Description description) {

                    }
                }))
                .andExpect(model().attribute("totalPages",pages))
        ;

        ArrayList<ExamGuide> found = new ArrayList<>();
        int currentIndex = 0;

        while(found.size()<containsExamGuides.size()){

            Map<String,Object> model = mockMvc.perform(
                    get("/backend/searchExamGuide")
                            .session(loginAs(memberUsername, password))
                            .param("keywords", complexKeyword)
                            .param("pageNo",""+currentIndex)
                            .param("pageSize","1") //每页显示多少
            )
                    .andExpect(status().isOk())
                    .andReturn().getModelAndView().getModel();

            Page<ExamGuide> allGuideList = (Page<ExamGuide>) model.get("allGuideList");

            Assert.assertEquals("查询出来的记录必须是之前设定的长度",1,allGuideList.getContent().size());

            ExamGuide foundGuid = allGuideList.getContent().get(0);

            Assert.assertTrue("查询出来的考试指南记录是否包含在之前预期的记录里",containsExamGuides.contains(foundGuid));

            currentIndex = ((Number)(model.get("pageNo"))).intValue()+1;//当前显示第几页，+1

            found.add(allGuideList.getContent().get(0));
        }


    }
}

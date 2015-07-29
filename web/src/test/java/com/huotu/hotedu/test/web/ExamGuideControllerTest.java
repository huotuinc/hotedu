package com.huotu.hotedu.test.web;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.Editor;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Manager;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

        //准备需要测试的ExamGuide
        // 10+随机10
        String complexKeyword = UUID.randomUUID().toString();
        // TODO 删除目前包含这个奇葩关键字的ExamGuide

        Random random = new Random();

        int count = 20 + random.nextInt(20);
        int countHaveKeywords=0;

        ArrayList<ExamGuide> containsExamGuides = new ArrayList<>();//拿到包含关键字的examGuide记录

        while (count-- > 0) {
            ExamGuide examGuide = new ExamGuide();
            examGuide.setLastUploadDate(new Date());
            examGuide.setContent("123");
            examGuide.setIsTop(random.nextBoolean());
            boolean containKeyword = random.nextBoolean();
            if (containKeyword) {
                examGuide.setTitle("测试t" + complexKeyword + System.currentTimeMillis());
                countHaveKeywords++;
            } else {
                examGuide.setTitle("测试t" + System.currentTimeMillis());
            }

            examGuide = examGuideRepository.save(examGuide);

            if (containKeyword)
                containsExamGuides.add(examGuide);
        }
        //准备测试环境END

        /**
         * 测试在没有登录的情况下不能访问查询操作
         */
        mockMvc.perform(
                get("/backend/searchExamGuide")
        )
                .andExpect(status().isFound())
                .andDo(print());
               // .andExpect(redirectedUrlPattern("**/" + SecurityConfig.LoginURI)); 暂时不测试
        /**
         * 1.测试用户登录之后，是否能正常访问
         * 2.测试返回model中的"allGuideList"属性是否是Page类型，长度是否为设置的长度10
         * 3.测试分页总数是否正确
         */
        int totalCount = (int) examGuideRepository.count();
        int defaultPageSize = ExamGuideController.PAGE_SIZE;
        int pages = (totalCount + defaultPageSize - 1) / defaultPageSize;
        int haveKeywordPages=(countHaveKeywords+defaultPageSize - 1)/defaultPageSize;
        mockMvc.perform(
                get("/backend/searchExamGuide")
                        .session(loginAs(memberUsername, password))
        )
                .andExpect(status().isForbidden());

        mockMvc.perform(
                get("/backend/searchExamGuide")
                        .session(loginAs(editorUsername, password))
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
                .andExpect(model().attribute("totalPages", pages))
        ;

        /**
         * 1.测试登录之后能否根据所传参数找全所有的包含关键字的记录
         */
        ArrayList<ExamGuide> found = new ArrayList<>();
        int currentIndex = 0;

        while (found.size() < containsExamGuides.size()) {
            Map<String, Object> model = mockMvc.perform(
                    get("/backend/searchExamGuide")
                            .session(loginAs(editorUsername, password))
                            .param("keywords", complexKeyword)
                            .param("pageNo", "" + currentIndex)
                            .param("pageSize", "1") //每页显示多少
            )
                    .andExpect(status().isOk())
                    .andReturn().getModelAndView().getModel();

            Page<ExamGuide> allGuideList = (Page<ExamGuide>) model.get("allGuideList");

            Assert.assertEquals("查询出来的记录必须是之前设定的长度", 1, allGuideList.getContent().size());

            ExamGuide foundGuid = allGuideList.getContent().get(0);

            Assert.assertTrue("查询出来的考试指南记录是否包含在之前预期的记录里", containsExamGuides.contains(foundGuid));

            currentIndex = ((Number) (model.get("pageNo"))).intValue() + 1;//当前显示第几页，+1

            found.add(allGuideList.getContent().get(0));
        }

        for(int i=-2;i<pages+2;i++){
            Map<String, Object> model=mockMvc.perform(
                    get("/backend/searchExamGuide")
                            .session(loginAs(editorUsername, password))
                            .param("keywords", "")
                            .param("pageNo", "" +(i))
                            .param("pageSize",ExamGuideController.PAGE_SIZE+"") //每页显示多少
            )
                    .andReturn().getModelAndView().getModel();
            int actualpages=(int)model.get("pageNo");
            int expectpages=i;
            if(i<=0){expectpages=0;}
            if(i>=pages){expectpages=pages-1;}
            Assert.assertEquals("输入当前页检查",expectpages,actualpages);
        }



        for(int i=-2;i<haveKeywordPages+2;i++){
            Map<String, Object> model=mockMvc.perform(
                    get("/backend/searchExamGuide")
                            .session(loginAs(editorUsername, password))
                            .param("keywords", complexKeyword)
                            .param("pageNo", "" +(i))
                            .param("pageSize",ExamGuideController.PAGE_SIZE+"") //每页显示多少
            )
                    .andReturn().getModelAndView().getModel();
            int actualpages=(int)model.get("pageNo");
            int expectpages=i;
            if(i<=0){expectpages=0;}
            if(i>=haveKeywordPages){expectpages=haveKeywordPages-1;}
            Assert.assertEquals("输入当前页检查",expectpages,actualpages);
        }

        /**
         * 删除测试
         */
        mockMvc.perform(
                get("/backend/delExamGuide")
        )
                .andExpect(status().isFound());

        mockMvc.perform(
                get("/backend/searchExamGuide")
                        .session(loginAs(editorUsername, password))
        ).andExpect(status().isOk());




        ExamGuide examGuide=new ExamGuide();
        examGuide.setTitle("删除测试"+System.currentTimeMillis());
        examGuide.setIsTop(random.nextBoolean());
        examGuide.setLastUploadDate(new Date());
        examGuide.setContent("5555");
        ExamGuide examGuidenew = examGuideRepository.save(examGuide);//保存新增的对象，为了之后删除做比较

        Map<String, Object> model = mockMvc.perform(
                get("/backend/searchExamGuide")
                        .session(loginAs(editorUsername, password))
                        .param("keywords","删除测试")

        ).andExpect(status().isOk())
         .andReturn().getModelAndView().getModel();


        Page<ExamGuide> allGuideList = (Page<ExamGuide>) model.get("allGuideList");
        Assert.assertEquals("删除之前应该有数据1条：", 1, allGuideList.getTotalElements());//删除之前能找到


        //
        mockMvc.perform(
                get("/backend/delExamGuide")
                        .session(loginAs(editorUsername, password))
                        .param("id", "" + examGuidenew.getId())
                        .param("keywords",examGuidenew.getTitle())

        )
                .andExpect(status().isFound());
//                .andDo(print());
//        .andExpect(redirectedUrlPattern("redirect:/backend/searchExamGuide"));//有问题


        model = mockMvc.perform(
                get("/backend/searchExamGuide")
                        .session(loginAs(editorUsername, password))
                        .param("keywords","删除测试")
        ).andReturn().getModelAndView().getModel();
        allGuideList = (Page<ExamGuide>) model.get("allGuideList");
        Assert.assertEquals("删除之后应该没有数据：",0,allGuideList.getTotalElements());//删除之后应该找不到数据
    }

    @Test
    public void addExamGuideTest() throws Exception{

        //准备测试环境
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
        //准备测试环境END


        mockMvc.perform(
                get("/backend/addExamGuide")
        )
                .andExpect(status().isFound());

       String ViewName=mockMvc.perform(
                get("/backend/addExamGuide")
                        .session(loginAs(editorUsername, password))
        )
                .andExpect(status().isOk())
                .andReturn().getModelAndView().getViewName();
        Assert.assertEquals("返回的视图名字是否相等","/backend/newguide",ViewName);




    }




    @Test
    public void modifyExamGuideTest() throws Exception{
        Random random = new Random();
        //准备测试环境
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



        ExamGuide examGuide=new ExamGuide();
        examGuide.setTitle("修改测试"+System.currentTimeMillis());
        examGuide.setIsTop(random.nextBoolean());
        examGuide.setLastUploadDate(new Date());
        examGuide.setContent("6666");
        ExamGuide examGuidenew = examGuideRepository.save(examGuide);//修改的examguide
        //准备测试环境END


        mockMvc.perform(
                get("/backend/modifyExamGuide")
        )
                .andExpect(status().isFound());

        String ViewName=mockMvc.perform(
                get("/backend/modifyExamGuide")
                        .session(loginAs(editorUsername, password))
                        .param("id", "" + examGuidenew.getId())
        )
                .andExpect(status().isOk())
                .andReturn().getModelAndView().getViewName();
        Assert.assertEquals("返回的视图名字是否相等","/backend/modifyguide",ViewName);


        Map<String, Object> model = mockMvc.perform(
                get("/backend/modifyExamGuide")
                        .session(loginAs(editorUsername, password))
                        .param("id",""+examGuidenew.getId())

        ).andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();


        ExamGuide examGuide1 = (ExamGuide) model.get("examGuide");
        Assert.assertEquals("判断获取的对象是否是修改的对象：", examGuidenew, examGuide1);
    }
    @Test
    @Rollback
    public void modifySaveExamGuide()throws Exception{
        //准备测试环境
        Random random = new Random();
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


        ExamGuide examGuide=new ExamGuide();
        examGuide.setTitle("测试1");
        examGuide.setContent("内容测试1");
        examGuide.setIsTop(false);
        examGuide.setLastUploadDate(new Date());
        ExamGuide examGuideold=examGuideRepository.save(examGuide);
        //准备测试环境END

        mockMvc.perform(
                get("/backend/modifySaveExamGuide")
        )
                .andExpect(status().isFound());
        mockMvc.perform(
                get("/backend/modifySaveExamGuide")
                .session(loginAs(editorUsername, password))
                .param("id", examGuideold.getId() + "")
                .param("title","测试2")
                .param("content","内容测试2")
                .param("top",true+"")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrlPattern("**/backend/searchExamGuide/**"));

        ExamGuide examGuidenew=examGuideRepository.findOne(examGuideold.getId());
        Assert.assertEquals(examGuidenew.getTitle(),"测试2");
        Assert.assertEquals(examGuidenew.getContent(),"内容测试2");
        Assert.assertEquals(examGuidenew.isTop(),true);




    }












}

package com.huotu.hotedu.test.web;
import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.MessageContentRepository;
import com.huotu.hotedu.service.LoginService;
import com.huotu.hotedu.web.controller.MessageContentController;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

/**
 * Created by shiliting on 2015/7/29.
 *
 * @author shiliting
 */

public class MessageContentControllerTest extends WebTestBase {

    private static final Log log = LogFactory.getLog(MessageContentControllerTest.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private MessageContentRepository messageContentRepository;
    // /backend/searchMessageContent 非登录状态无法访问
    //
    // /backend/searchMessageContent 最多展示 n 个数据 n 自定义
    //
    // /backend/delMessageContent  删除&查询
    // 删除
    // id 要删除的ID
    // 查询
    // &keywords= 删除以后 显示指定关键字搜索页面
    // &n=0 删除完成以后显示的页索引
    // &paging=10 每页10条

    // /backend/searchMessageContent
    // 查询
    // &keywords= 删除以后 显示标题包含指定关键字搜索页面
    // &n=0 删除完成以后显示的页索引
    // &paging=10 每页10条

    // 修改的操作 需要ROLE_EDITOR 权限
    // Editor 是可以修改的
    // 但是Member 是不可以的

    @Test
    @Rollback
    public void messageContentTest() throws Exception {

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

        //准备需要测试的MessageContent
        // 10+随机10
        String complexKeyword = UUID.randomUUID().toString();
        // TODO 删除目前包含这个奇葩关键字的MessageContent

        Random random = new Random();

        int count = 20 + random.nextInt(20);
        int countHaveKeywords=0;

        ArrayList<MessageContent> containsMessageContents = new ArrayList<>();//拿到包含关键字的messageContent记录

        while (count-- > 0) {
            MessageContent messageContent = new MessageContent();
            messageContent.setLastUploadDate(new Date());
            messageContent.setContent("123");
            messageContent.setTop(random.nextBoolean());
            boolean containKeyword = random.nextBoolean();
            if (containKeyword) {
                messageContent.setTitle("测试t" + complexKeyword + System.currentTimeMillis());
                countHaveKeywords++;
            } else {
                messageContent.setTitle("测试t" + System.currentTimeMillis());
            }

            messageContent = messageContentRepository.save(messageContent);

            if (containKeyword)
                containsMessageContents.add(messageContent);
        }
        //准备测试环境END

        /**
         * 测试在没有登录的情况下不能访问查询操作
         */
        mockMvc.perform(
                get("/backend/searchMessageContent")
        )
                .andExpect(status().isFound());
        //.andDo(print());
        // .andExpect(redirectedUrlPattern("**/" + SecurityConfig.LoginURI)); 暂时不测试

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(getClass().getResourceAsStream("testUpload.jpg"), buffer);
        mockMvc.perform(
//                get("/backend/addSaveMessageContent")
                fileUpload("/backend/addSaveMessageContent")
                        .file("smallimg",buffer.toByteArray())
                        .session(loginAs(ManagerUsername, password))
        ).andExpect(status().isFound());
        /**
         * 1.测试用户登录之后，是否能正常访问
         * 2.测试返回model中的"allGuideList"属性是否是Page类型，长度是否为设置的长度10
         * 3.测试分页总数是否正确
         */
        int totalCount = (int) messageContentRepository.count();
        int defaultPageSize = MessageContentController.PAGE_SIZE;
        int pages = (totalCount + defaultPageSize - 1) / defaultPageSize;
        int haveKeywordPages=(countHaveKeywords+defaultPageSize - 1)/defaultPageSize;
        mockMvc.perform(
                get("/backend/searchMessageContent")
                        .session(loginAs(memberUsername, password))
        )
                .andExpect(status().isForbidden());

        mockMvc.perform(
                get("/backend/searchMessageContent")
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
                        return page.getContent().size() == MessageContentController.PAGE_SIZE;
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
        ArrayList<MessageContent> found = new ArrayList<>();
        int currentIndex = 0;

        while (found.size() < containsMessageContents.size()) {
            Map<String, Object> model = mockMvc.perform(
                    get("/backend/searchMessageContent")
                            .session(loginAs(editorUsername, password))
                            .param("keywords", complexKeyword)
                            .param("pageNo", "" + currentIndex)
                    //.param("pageSize", "1") //每页显示多少
            )
                    .andExpect(status().isOk())
                    .andReturn().getModelAndView().getModel();

            Page<MessageContent> allGuideList = (Page<MessageContent>) model.get("allGuideList");
            if(allGuideList.getContent().size()!=10){
                Assert.assertEquals("查询出来的记录必须是之前设定的长度",containsMessageContents.size()%10,allGuideList.getContent().size());
            }
            //Assert.assertEquals("查询出来的记录必须是之前设定的长度",10, allGuideList.getContent().size()); //长度固定为10


            MessageContent foundGuid = allGuideList.getContent().get(0);

            Assert.assertTrue("查询出来的资讯动态记录是否包含在之前预期的记录里", containsMessageContents.contains(foundGuid));

            currentIndex = ((Number) (model.get("pageNo"))).intValue() + 1;//当前显示第几页，+1

            found.add(allGuideList.getContent().get(0));
        }

        for(int i=-2;i<pages+2;i++){
            Map<String, Object> model=mockMvc.perform(
                    get("/backend/searchMessageContent")
                            .session(loginAs(editorUsername, password))
                            .param("keywords", "")
                            .param("pageNo", "" +(i))
                            .param("pageSize",MessageContentController.PAGE_SIZE+"") //每页显示多少
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
                    get("/backend/searchMessageContent")
                            .session(loginAs(editorUsername, password))
                            .param("keywords", complexKeyword)
                            .param("pageNo", "" +(i))
                            .param("pageSize",MessageContentController.PAGE_SIZE+"") //每页显示多少
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
                get("/backend/delMessageContent")
        )
                .andExpect(status().isFound());

        mockMvc.perform(
                get("/backend/addSaveMessageContent")
                        .session(loginAs(ManagerUsername, password))
        )
                .andExpect(status().isFound());
        mockMvc.perform(
                get("/backend/searchMessageContent")
                        .session(loginAs(editorUsername, password))
        ).andExpect(status().isOk());




        MessageContent messageContent=new MessageContent();
        messageContent.setTitle("删除测试"+System.currentTimeMillis());
        messageContent.setTop(random.nextBoolean());
        messageContent.setLastUploadDate(new Date());
        messageContent.setContent("5555");
        MessageContent messageContentnew = messageContentRepository.save(messageContent);//保存新增的对象，为了之后删除做比较

        Map<String, Object> model = mockMvc.perform(
                get("/backend/searchMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("keywords","删除测试")

        ).andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();


        Page<MessageContent> allGuideList = (Page<MessageContent>) model.get("allGuideList");
        Assert.assertEquals("删除之前应该有数据1条：", 1, allGuideList.getTotalElements());//删除之前能找到


        //
        mockMvc.perform(
                get("/backend/delMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("id", "" + messageContentnew.getId())
                        .param("keywords",messageContentnew.getTitle())

        )
                .andExpect(status().isFound());
//        .andExpect(redirectedUrl("/backend/searchMessageContent?keywords="));


        model = mockMvc.perform(
                get("/backend/searchMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("keywords","删除测试")
        ).andReturn().getModelAndView().getModel();
        allGuideList = (Page<MessageContent>) model.get("allGuideList");
        Assert.assertEquals("删除之后应该没有数据：",0,allGuideList.getTotalElements());//删除之后应该找不到数据
    }

    @Test
    public void addMessageContentTest() throws Exception{

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
                get("/backend/addMessageContent")
        )
                .andExpect(status().isFound());
        mockMvc.perform(
                get("/backend/addSaveMessageContent")
                        .session(loginAs(ManagerUsername, password))
        )
                .andExpect(status().isFound());

        String ViewName=mockMvc.perform(
                get("/backend/addMessageContent")
                        .session(loginAs(editorUsername, password))
        )
                .andExpect(status().isOk())
                .andReturn().getModelAndView().getViewName();
        Assert.assertEquals("返回的视图名字是否相等","/backend/newguide",ViewName);




    }




    @Test
    public void modifyMessageContentTest() throws Exception{
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



        MessageContent messageContent=new MessageContent();
        messageContent.setTitle("修改测试"+System.currentTimeMillis());
        messageContent.setTop(random.nextBoolean());
        messageContent.setLastUploadDate(new Date());
        messageContent.setContent("6666");
        MessageContent messageContentnew = messageContentRepository.save(messageContent);//修改的examguide
        //准备测试环境END


        mockMvc.perform(
                get("/backend/modifyMessageContent")
        )
                .andExpect(status().isFound());
        mockMvc.perform(
                get("/backend/addSaveMessageContent")
                        .session(loginAs(ManagerUsername, password))
        )
                .andExpect(status().isFound());

        String ViewName=mockMvc.perform(
                get("/backend/modifyMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("id", "" + messageContentnew.getId())
        )
                .andExpect(status().isOk())
                .andReturn().getModelAndView().getViewName();
        Assert.assertEquals("返回的视图名字是否相等","/backend/modifyguide",ViewName);


        Map<String, Object> model = mockMvc.perform(
                get("/backend/modifyMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("id",""+messageContentnew.getId())

        ).andExpect(status().isOk())
                .andReturn().getModelAndView().getModel();


        MessageContent messageContent1 = (MessageContent) model.get("messageContent");
        Assert.assertEquals("判断获取的对象是否是修改的对象：", messageContentnew, messageContent1);
    }
    @Test
    @Rollback
    public void modifySaveMessageContent()throws Exception{
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
        MessageContent messageContent=new MessageContent();
        messageContent.setTitle("测试1");
        messageContent.setContent("内容测试1");
        messageContent.setTop(false);
        messageContent.setLastUploadDate(new Date());
        MessageContent messageContentOld=messageContentRepository.save(messageContent);
        //准备测试环境END

        mockMvc.perform(
                get("/backend/modifySaveMessageContent")
        )
                .andExpect(status().isFound());
        mockMvc.perform(
                get("/backend/addSaveMessageContent")
                        .session(loginAs(ManagerUsername, password))
        )
                .andExpect(status().isFound());
        mockMvc.perform(
                get("/backend/modifySaveMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("id", messageContentOld.getId() + "")
                        .param("title","测试2")
                        .param("content","内容测试2")
                        .param("top","1")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/backend/searchMessageContent"));

        MessageContent messageContentNew=messageContentRepository.findOne(messageContentOld.getId());
        Assert.assertEquals(messageContentNew.getTitle(),"测试2");
        Assert.assertEquals(messageContentNew.getContent(),"内容测试2");
        Assert.assertEquals(messageContentNew.isTop(),true);

    }


    @Test
    @Rollback
    public void addSaveMessageContent()throws Exception{
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
        String messageContentTitle = UUID.randomUUID().toString();
        //准备测试环境END

        mockMvc.perform(
                get("/backend/addSaveMessageContent")
        )
                .andExpect(status().isFound());


        mockMvc.perform(
                get("/backend/addSaveMessageContent")
                        .session(loginAs(ManagerUsername, password))
        )
                .andExpect(status().isFound());



        List<MessageContent> lists=messageContentRepository.findAll();
        Boolean flag=true;

        for(int i=0;i<lists.size();i++){
            if(lists.get(i).getTitle()!=null) {
                if (lists.get(i).getTitle().equals(messageContentTitle)) {
                    flag = false;
                    break;
                }
            }
        }
        Assert.assertTrue("添加资讯动态的时候已经有被添加的数据",flag);
        mockMvc.perform(
                post("/backend/addSaveMessageContent")
                        .session(loginAs(editorUsername, password))
                        .param("title",messageContentTitle)
                        .param("content","内容测试1")
                        .param("top","0")
        )
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/backend/searchMessageContent"));

        lists=messageContentRepository.findAll();
        int n=0;
        for(int i=0;i<lists.size();i++){
            if(lists.get(i).getTitle()!=null) {
                if (lists.get(i).getTitle().equals(messageContentTitle)) {
                    n++;
                }
            }
        }
        Assert.assertEquals("添加资讯动态之后是否能找到该记录且只有一条",1,n);


    }


}

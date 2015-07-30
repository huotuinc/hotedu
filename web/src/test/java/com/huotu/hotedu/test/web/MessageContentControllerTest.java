package com.huotu.hotedu.test.web;

import com.huotu.hotedu.repository.MessageContentRepository;
import com.huotu.hotedu.service.LoginService;
import libspringtest.SpringWebTest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by shiliting on 2015/7/29.
 *
 * @author shiliting
 */

public class MessageContentControllerTest extends SpringWebTest {

    private static final Log log = LogFactory.getLog(ExamGuideControllerTest.class);

    @Autowired
    private LoginService loginService;
    @Autowired
    private MessageContentRepository messageContentRepository;
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



}

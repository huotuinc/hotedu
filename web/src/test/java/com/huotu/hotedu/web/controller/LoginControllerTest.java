package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.WebTestBase;
import com.huotu.hotedu.test.TestWebConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * Created by Administrator on 2015/7/8.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class LoginControllerTest extends WebTestBase {
    private static final Log log = LogFactory.getLog(LoginControllerTest.class);

    @Test
    public void testLoginTutor() throws Exception {
        mockMvc.perform(get("/pc/login?LoginName='admin'&password='admin'")).andDo(print());

    }


}
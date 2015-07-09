package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.test.TestWebConfig;
import libspringtest.SpringWebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

/**
 * Created by Administrator on 2015/7/8.
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestWebConfig.class)
@WebAppConfiguration
public class TutorControllerTest extends SpringWebTest{

    @Test
    public void testAddSaveTutor() throws Exception {
        mockMvc.perform(
                fileUpload("/backend/addSaveTutor")
                .file("smallimg",new byte[]{1,2,3})
        );
    }
}
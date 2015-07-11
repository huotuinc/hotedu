package com.huotu.hotedu.web.controller;

import com.huotu.hotedu.test.TestWebConfig;
import libspringtest.SpringWebTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        StreamUtils.copy(getClass().getResourceAsStream("test.jpg"),buffer);

        mockMvc.perform(
                fileUpload("/backend/addSaveTutor")
                .file("smallimg",buffer.toByteArray())
        );
    }
    @Test
    public  void testDelTutor() throws Exception{
        mockMvc.perform(get("/backend/delTutor?n=0&id=2&sumpage=1&searchSort='all'&keywords=''&dateStart=''&dateEnd=''&sumElement=4"));
    }
    @Test
    public  void testSaveTutorByTime() throws Exception{
        mockMvc.perform(
                get("/backend/searchTutor?searchSort='date'&keywords=''&dateStart='2014.4.5'&dateEnd='2015.9.8'")
        );
    }
}
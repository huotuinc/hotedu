package com.huotu.hotedu.test.web;

//import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Member;
//import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.repository.LoginRepository;
import com.huotu.hotedu.repository.MemberRepository;
//import com.huotu.hotedu.service.ExamGuideService;
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
import org.springframework.ui.Model;

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
//    @Autowired
//    private ExamGuideService examGuideService;
//    @Autowired
//    private ExamGuideRepository examGuideRepository;


    @Test
    public void loadexam() throws Exception {

    }




}

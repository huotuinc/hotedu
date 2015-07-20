package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.Tutor;
import com.huotu.hotedu.repository.TutorRepository;
import com.huotu.hotedu.service.TutorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class TutorServiceTest {


    @Autowired
    TutorRepository tutorRepository;
    @Autowired
    TutorService tutorService;



    @Test
    public void searchTutorTypeTest(){
        Page<Tutor> pages=tutorService.searchTutorType(0, 10, "", "name");
        for(Tutor t:pages){
            System.out.println(t);
        }


    }

}

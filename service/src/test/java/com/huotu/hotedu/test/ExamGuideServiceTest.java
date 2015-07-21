package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.repository.QaRepository;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.service.QaService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ExamGuideServiceTest {


    @Autowired
    ExamGuideRepository examGuideRepository;
    @Autowired
    QaRepository qaRepository;
    @Autowired
    private QaService qaService;
    @Autowired
    private ExamGuideService examGuideService;


    @Test
    @Rollback
    public void loadExamGuide(){
        ExamGuide examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide1");
        examGuide.setLastUploadDate(new Date());
        examGuide.setIsTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide2");
        examGuide.setLastUploadDate(new Date());
        examGuide.setIsTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide3");
        examGuide.setLastUploadDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        examGuide.setIsTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide4");
        examGuide.setLastUploadDate(new Date(System.currentTimeMillis() - 5 * 60 * 60 * 1000));
        examGuide.setIsTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide5");
        examGuide.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        examGuide.setIsTop(true);
        examGuideRepository.save(examGuide);

        List<ExamGuide> allList = examGuideRepository.findAll();

        //http://stackoverflow.com/questions/15802989/subsitute-for-month-year-date-functions-in-jpa
        List<ExamGuide> list  = examGuideRepository.queryHql("select u from ExamGuide u where FUNC('DATE', u.lastUploadDate)=:date ",ExamGuide.class, examGuideTypedQuery -> {
            Date date = new Date();
            examGuideTypedQuery.setParameter("date",date, TemporalType.DATE);
        });


        List<ExamGuide> list2 = examGuideRepository.findAll((root, query, cb) -> {
            return cb.equal(cb.function("DATE",Date.class,root.get("lastUploadDate").as(Date.class)),new java.sql.Date(System.currentTimeMillis()));
        });

        System.out.println(list);

    }
    @Test
    public void loadQa(){
        Page<Qa> pages=qaService.loadQa(0, 10);
        for(Qa q:pages){
            System.out.println(q);
        }

    }

}

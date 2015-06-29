package com.huotu.hotedu.test;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.repository.MessageContentRepository;
import com.huotu.hotedu.repository.QaRepository;
import com.huotu.hotedu.service.ExamGuideService;
import com.huotu.hotedu.service.QaService;
import com.huotu.hotedu.service.MessageContentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.DateUtils;

import javax.crypto.ExemptionMechanism;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    private ExamGuideService guideService;
    @Autowired
    QaRepository qaRepository;
    @Autowired
    private QaService qaService;

    @Autowired
    private MessageContentRepository messageContentRepository;
    @Autowired
    private MessageContentService messageContentsService;


    @Test
    @Rollback
    public void loadExamGuide(){
        ExamGuide examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide1");
        examGuide.setLastUploadDate(new Date());
        examGuide.setTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide2");
        examGuide.setLastUploadDate(new Date());
        examGuide.setTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide3");
        examGuide.setLastUploadDate(new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000));
        examGuide.setTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide4");
        examGuide.setLastUploadDate(new Date(System.currentTimeMillis() - 5 * 60 * 60 * 1000));
        examGuide.setTop(true);
        examGuideRepository.save(examGuide);

        examGuide =new ExamGuide();
        examGuide.setContent("examguide");
        examGuide.setTitle("title examguide5");
        examGuide.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
        examGuide.setTop(true);
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
        Qa qa=new Qa();
        qa.setContent("qaqaqaqa");
        qa.setTitle("我的第二次测试");
        qa.setTop(true);
        qa.setLastUploadDate(new Date());
        qaRepository.save(qa);
        List<Qa> list=qaService.loadQa();
        System.out.println(list);
    }



    @Test
    public void loadMessageContent(){
        MessageContent messageContent=new MessageContent();
        messageContent.setContent("mmmmmmmm");
        messageContent.setTitle("我的第三次测试");
        messageContent.setTop(true);
        messageContent.setLastUploadDate(new Date());
        messageContentRepository.save(messageContent);
        List<MessageContent> list=messageContentsService.loadMessageContent();
        System.out.println(list);

    }

}

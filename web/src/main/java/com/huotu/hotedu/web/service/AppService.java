package com.huotu.hotedu.web.service;

import com.huotu.hotedu.entity.*;
import com.huotu.hotedu.repository.*;
import com.huotu.hotedu.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * @author CJ
 */
@Service
public class AppService implements ApplicationListener<ContextRefreshedEvent> {

    private static final Log log = LogFactory.getLog(AppService.class);

    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private LoginService loginService;
    @Autowired
    private ExamGuideRepository examGuideRepository;
    @Autowired
    private LinkRepository linkRepository;
    @Autowired
    private TutorRepository tutorRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private QaRepository qaRepository;
    @Autowired
    private MessageContentRepository messageContentRepository;


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("user.dir="+System.getProperty("user.dir"));
            if (managerRepository.count()==0){
                Manager manager = new Manager();
                manager.setLoginName("admins");
                loginService.newLogin(manager,"admins");
                System.out.println("测试用户以添加！");
            }

        }

        if (event.getApplicationContext().getParent() == null) {
            // 做一些初始化工作 比如
            if (examGuideRepository.count()==0){
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
                Random r=new Random();
                for(int i=0;i<34;i++){
                    examGuide=new ExamGuide();
                    examGuide.setTitle(String.valueOf(r.nextInt()*20));
                    examGuide.setContent(String.valueOf(r.nextDouble()));
                    examGuide.setLastUploadDate(new Date(System.currentTimeMillis()+i*100000));
                    examGuide.setTop(i%3==0? true:false);
                    examGuideRepository.save(examGuide);
                }
                examGuide =new ExamGuide();
                examGuide.setContent("中文测试标题");
                examGuide.setTitle("中文测试内容");
                examGuide.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 2000));
                examGuide.setTop(true);
                examGuideRepository.save(examGuide);
                /// do something
            }
          if(linkRepository.count()==0){
              Link link=new Link();
              link.setTitle("spring");
              link.setUrl("spring.io");
              linkRepository.save(link);
              link=new Link();
              link.setTitle("百度");
              link.setUrl("www.baidu.com");
              linkRepository.save(link);
              link=new Link();
              link.setTitle("粉猫");
              link.setUrl("www.fanmore.cn");
              linkRepository.save(link);
          }
            if(messageContentRepository.count()==0){
                MessageContent messageContent;
                for(int i=0;i<5;i++){
                    messageContent=new MessageContent();
                    messageContent.setTitle("messageContent"+i);
                    messageContent.setContent("content message Content"+i);
                    messageContent.setLastUploadDate(new Date(System.currentTimeMillis()+24*60*60*1000*(i+1)));
                    messageContentRepository.save(messageContent);
                }
            }

            if (tutorRepository.count()==0){
                Tutor tutor;
                for(int i=0;i<7;i++){
                    tutor=new Tutor();
                    tutor.setName("tutor"+i);
                    tutor.setIntroduction("i am a tutor my name is"+i);
                    tutor.setArea("杭州");
                    tutor.setQualification("教授");
                    tutor.setPicture("slt.jpg");
                    tutorRepository.save(tutor);
                }
            }


            if (qaRepository.count()==0){
                Qa qa;
                for(int i=0;i<4;i++){
                    qa=new Qa();
                    qa.setTitle("qa"+i);
                    qa.setContent("问题"+(i+1));
                    qa.setLastUploadDate(new Date(System.currentTimeMillis()+24*60*60*1000*(i+1)));
                    qa.setTop(true);
                }
            }

        }





    }






}

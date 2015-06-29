package com.huotu.hotedu.web.service;

import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Manager;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.repository.ManagerRepository;
import com.huotu.hotedu.service.LoginService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import java.util.Date;

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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("user.dir="+System.getProperty("user.dir"));
            if (managerRepository.count()==0){
                Manager manager = new Manager();
                manager.setLoginName("admin");
                loginService.newLogin(manager,"admin");
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

                examGuide =new ExamGuide();
                examGuide.setContent("examguide");
                examGuide.setTitle("title examguide5");
                examGuide.setLastUploadDate(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000));
                examGuide.setTop(true);
                examGuideRepository.save(examGuide);

                /// do something
            }
        }





    }






}

package com.huotu.hotedu.web.service;

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
                /// do something
            }
        }





    }






}

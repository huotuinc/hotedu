package com.huotu.hotedu.web.service;

import com.huotu.hotedu.entity.Manager;
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

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            log.info("user.dir="+System.getProperty("user.dir"));
            if (managerRepository.count()==0){
                Manager manager = new Manager();
                manager.setLoginName("admin");
                loginService.newLogin(manager,"admin");
            }

        }
    }
}

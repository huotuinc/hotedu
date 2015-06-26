package com.huotu.hotedu.service;

import com.huotu.hotedu.repository.ExamGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

/**
 * @author CJ
 */
@Service
public class AppService implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ExamGuideRepository examGuideRepository;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (event.getApplicationContext().getParent() == null) {
            // 做一些初始化工作 比如
            if (examGuideRepository.count()==0){
                /// do something
            }
        }
    }
}

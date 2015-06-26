package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.repository.LoginRepository;
import com.huotu.hotedu.repository.ExamGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class ExamGuideService {

    @Autowired
    private ExamGuideRepository examGuideRepository;

    //返回所有考试指南
    public List<ExamGuide> loadExamGuide(){
        List<ExamGuide> list=new ArrayList<ExamGuide>();
        list=examGuideRepository.findAll();
        return list;
    }
}

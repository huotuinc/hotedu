package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.repository.ExamGuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
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
        return examGuideRepository.findAll();
    }

    //分页
    public Page<ExamGuide> searchExamGuide(int n,int pagesize){
        return  examGuideRepository.findAll(new PageRequest(n, pagesize));
    }

}

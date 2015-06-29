package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.Qa;
import com.huotu.hotedu.repository.QaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class TutorService {

    @Autowired
    private QaRepository qaRepository;

    //返回所有常见问题
    public List<Qa> loadQa(){
        return qaRepository.findAll();
    }
}

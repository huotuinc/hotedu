package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.ExamGuide;
import com.huotu.hotedu.entity.Link;
import com.huotu.hotedu.repository.ExamGuideRepository;
import com.huotu.hotedu.repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shiliting 2015/6/25.
 *
 * @author shiliting
 */
@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    //返回所有超链接信息
    public List<Link> loadLink(){
        return linkRepository.findAll();
    }
}

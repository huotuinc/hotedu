package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.repository.MessageContentRepository;
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
public class MessageContentService {

    @Autowired
    private MessageContentRepository messageContentRepository;

    //返回所有常见问题
    public List<MessageContent> loadMessageContent(){
        return messageContentRepository.findAll();
    }
}

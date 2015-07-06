package com.huotu.hotedu.service;
import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.entity.MessageContent;
import com.huotu.hotedu.repository.MessageContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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

    //返回所有资讯指南
    public Page<MessageContent> loadMessageContent(int n,int pagesize){
        return messageContentRepository.findAll(new PageRequest(n,pagesize));
    }

    //分页
    public Page<MessageContent> searchMessageContent(int n,int pagesize,String keyword){
        // SQL
        // 面向对象的SQL
        // select MessageContent from MessageContent where title like ?
        return  messageContentRepository.findAll(new Specification<MessageContent>() {
            @Override
            public Predicate toPredicate(Root<MessageContent> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (keyword.length()==0)
                    return null;
                return cb.like(root.get("title").as(String.class),"%"+keyword+"%");
            }
        },new PageRequest(n, pagesize));

    }

    //删除一条资讯动态消息
    public void delMessageContent(Long id){
        messageContentRepository.delete(id);
    }

    //增加一条资讯动态信息
    public void addMessageContent(MessageContent MessageContent){
        messageContentRepository.save(MessageContent);
    }

    //修改一条资讯动态信息
    public void modify(MessageContent MessageContent){
        messageContentRepository.save(MessageContent);

    }
    //查找一条资讯动态消息
    public MessageContent findOneById(Long id){
        return messageContentRepository.findOne(id);
    }
}

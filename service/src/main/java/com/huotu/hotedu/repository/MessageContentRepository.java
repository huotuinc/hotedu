package com.huotu.hotedu.repository;

import com.huotu.hotedu.entity.MessageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by shiliting on 2015/6/25.
 *
 * @author shiliting
 */
@Repository
public interface MessageContentRepository extends JpaRepository<MessageContent,Long>{

}

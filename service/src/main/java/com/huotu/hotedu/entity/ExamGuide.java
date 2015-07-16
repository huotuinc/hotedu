package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shiliting on 2015/6/25.
 * 考试指南实体
 * @author shiliting
 */
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
//@Inheritance(strategy=InheritanceType.JOINED)  //有待解答 已在邮件中答复 :D
public  class ExamGuide implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;//有待解答...Serializable标准而已
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;                          //考试指南标题
    @Column(length = 5000)
    private String content;                       //考试指南内容
    @Column
    private boolean top=true;                    //是否置顶
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;                 //考试指南最后修改时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isTop() {
        return top;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public Date getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(Date lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    @Override
    public String toString() {
        return "ExamGuide{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", top=" + top +
                ", lastUploadDate=" + lastUploadDate +
                '}';
    }
}

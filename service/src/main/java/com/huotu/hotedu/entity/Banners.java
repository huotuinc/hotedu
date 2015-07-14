package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shiliting on 2015/6/25.
 *
 * @author shiliting
 */
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
//@Inheritance(strategy=InheritanceType.JOINED)  //有待解答 已在邮件中答复 :D
public  class Banners implements Serializable {
    private static final long serialVersionUID = -349012453592429794L;//有待解答...Serializable标准而已
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column(length = 1000)
    private String content;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;

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

    public String getContent() {return content;}

    public void setContent(String content) {this.content = content;}


    public Date getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(Date lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    @Override
    public String toString() {
        return "Banners{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", lastUploadDate=" + lastUploadDate +
                '}';
    }
}

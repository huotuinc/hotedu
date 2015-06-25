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
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
//@Inheritance(strategy=InheritanceType.JOINED)  //有待解答
public  class examGuide implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;//有待解答
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String title;
    @Column(length = 5000)
    private String content;
    @Column
    private boolean top=true;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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
}

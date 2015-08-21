package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shiliting on 2015/6/25.
 * 资讯实体
 * @author shiliting
 */
@Entity
//@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
//@Inheritance(strategy=InheritanceType.JOINED)  //有待解答
public  class MessageContent implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;//有待解答
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 标题
     */
    @Column
    private String title;
    /**
     * 简介
     */
    @Column
    private String synopsis;
    /**
     * 内容
     */
    @Lob
    private String content;
    /**
     * 是否置顶
     */
    @Column
    private boolean top=true;
    /**
     * 图片地址
     */
    @Column
    private String pictureUri;
    /**
     * 更新日期
     */
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

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }
}

package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 视频
 * Created by cwb on 2015/7/10.
 * Modify by shiliting on 2015/8/11
 */
@Entity
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Video implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 视频编号
     */
    private int videoNo;
    /**
     * 视频名称
     */
    @Column
    private String videoName;
    /**
     * 视频播放地址
     */
    private String playUrl;
    /**
     * 是否免费观看
     */
    private boolean free = true;
    /**
     * 视频缩略图
     */
    private String thumbnail;
    private String content;

    /**
     * 上传时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime;

    /**
     * 是否完整版
     */
    private boolean complete;

    public int getVideoNo() {
        return videoNo;
    }

    public void setVideoNo(int videoNo) {
        this.videoNo = videoNo;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Date getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(Date uploadTime) {
        this.uploadTime = uploadTime;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getPlayUrl() {
        return playUrl;
    }

    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

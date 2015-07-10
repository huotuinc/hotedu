package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 视频
 * Created by cwb on 2015/7/10.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"videoName"}))
@Inheritance(strategy= InheritanceType.TABLE_PER_CLASS)
public class Video implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private String videoName;
    private String videoAddr;
    private boolean free = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadTime;

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

    public String getVideoAddr() {
        return videoAddr;
    }

    public void setVideoAddr(String videoAddr) {
        this.videoAddr = videoAddr;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }
}

package com.huotu.hotedu.entity;

import com.huotu.hotedu.common.CommonEnum;

import javax.persistence.*;

/**
 * 公告
 * Created by WenbinChen on 2015/10/28 10:23.
 */
@Entity
@Table(name = "NOTICE")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 公告类型
     */
    @Column(name = "type")
    private CommonEnum.NoticeType type;

    /**
     * 链接地址
     */
    @Column(name = "linkUrl")
    private String linkUrl;

    /**
     * 是否开启公告
     */
    @Column(name = "enabled")
    private boolean enabled = true;

    /**
     * 图片保存地址
     */
    @Column(name = "picUrl")
    private String picUrl;

    public CommonEnum.NoticeType getType() {
        return type;
    }

    public void setType(CommonEnum.NoticeType type) {
        this.type = type;
    }


    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

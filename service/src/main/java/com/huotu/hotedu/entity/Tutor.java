package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * Created by shiliting on 2015/6/25.
 * 导师实体
 * @author shiliting
 */
@Entity
public  class Tutor extends Login implements Serializable {
    private static final long serialVersionUID = -349012453592429794L;
    @Column
    /**
     * 教师姓名
     */
    private String name;
    /**
     * 教师描述
     */
    private String introduction;
    /**
     * 图片地址
     */
    private String pictureUri;
    /**
     * 教师职称
     */
    private String qualification;
    /**
     * 教师地区
     */
    private String area;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public Date getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(Date lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_GUIDE")
        );
    }
}

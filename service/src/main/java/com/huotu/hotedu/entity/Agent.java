package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Collection;

/**
 * 代理商
 * Created by shiliting on 2015/7/1.
 */
@Entity
public class Agent extends Login{
    private static final long serialVersionUID = -349012453592429794L;
    @Column
    /**
     * 代理商负责人姓名
     */
    private String name;

    /**
     * 代理商编号
     */
    private String areaId;

    /**
     * 代理商电话
     */
    private String phoneNo;
    /**
     * 代理商头像
     */
    private String pictureUri;
    /**
     * 代理地区
     */
    private String area;

    /**
     * 代理商级别
     */
    private String level;
    /**
     * 证书总数量
     */
    private int certificateNumber;

    /**
     * 已发证书数量
     */
    private int sendCertificateNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(int certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public int getSendCertificateNumber() {
        return sendCertificateNumber;
    }

    public void setSendCertificateNumber(int sendCertificateNumber) {
        this.sendCertificateNumber = sendCertificateNumber;
    }
    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

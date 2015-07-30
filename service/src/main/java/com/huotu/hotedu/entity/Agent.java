package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

/**
 * 代理商
 * Created by shiliting on 2015/7/1.
 */
@Entity
public class Agent extends Login{
    private static final long serialVersionUID = -349012453592429794L;
    @Column
    /**
     * 代理商名字
     */
    private String name;
    /**
     * 法人代表
     */
    private String corporation;

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

    /**
     * 注册时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getCorporation() {
        return corporation;
    }

    public void setCorporation(String corporation) {
        this.corporation = corporation;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_AGENT")

        );
    }
}

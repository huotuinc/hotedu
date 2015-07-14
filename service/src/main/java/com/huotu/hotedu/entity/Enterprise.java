package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

/**
 * 企业，可以进行招聘
 * Created by shiliting on 2015/7/1.
 */
@Entity
public class Enterprise extends Login implements Serializable {
    private static final long serialVersionUID = -349012453592429794L;
    @Column
    private String name;
    private String information;
    private String tel;
    private String logoUri;
    private boolean isPutaway;
    private int status;            //0:表示不禁用，1：表示禁用  以后可能还有其他状态
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getLogoUri() {
        return logoUri;
    }

    public void setLogoUri(String logoUri) {
        this.logoUri = logoUri;
    }

    public boolean isPutaway() {
        return isPutaway;
    }

    public void setIsPutaway(boolean isPutaway) {
        this.isPutaway = isPutaway;
    }

    public Date getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(Date lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

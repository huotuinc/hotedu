package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;
import java.util.Collection;

/**
 * 企业，可以进行招聘
 * Created by Administrator on 2015/7/1.
 */
public class Enterprise extends Login{
    @Column
    private String name;
    private String information;
    private String tel;
    private String logoUri;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

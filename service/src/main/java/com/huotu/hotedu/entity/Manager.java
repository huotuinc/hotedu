package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Created by luffy on 2015/6/10.
 * 系统管理员
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
public class Manager extends Login{

    public String getManagerField() {
        return managerField;
    }

    public void setManagerField(String managerField) {
        this.managerField = managerField;
    }

    private String managerField;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_USER"),
                new SimpleGrantedAuthority("ROLE_ADMIN"),
                new SimpleGrantedAuthority("ROLE_GRANT"),
                new SimpleGrantedAuthority("ROLE_All")
        );
    }
}

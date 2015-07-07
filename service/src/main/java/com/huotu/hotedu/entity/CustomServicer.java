package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import sun.rmi.runtime.Log;

import javax.persistence.Entity;
import java.util.Collection;

/**
 * 客服
 * Created by Administrator on 2015/7/1.
 */
@Entity
public class CustomServicer extends Login{
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

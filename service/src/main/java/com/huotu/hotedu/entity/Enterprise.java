package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import sun.rmi.runtime.Log;

import java.util.Collection;

/**
 * 企业，可以进行招聘
 * Created by Administrator on 2015/7/1.
 */
public class Enterprise extends Login{
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

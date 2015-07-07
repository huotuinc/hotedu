package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 文案处理员
 * Created by Administrator on 2015/7/1.
 */
public class Editor extends Login{
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

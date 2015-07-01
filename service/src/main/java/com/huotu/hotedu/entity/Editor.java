package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 一些文案工作，比如QA 比如考试指南
 * Created by Administrator on 2015/7/1.
 */
public class Editor extends Login{
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

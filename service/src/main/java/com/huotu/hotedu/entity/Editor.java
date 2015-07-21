package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.Entity;
import java.util.Collection;
import java.util.Collections;

/**
 * 文案处理员
 * Created by shiliting on 2015/7/1.
 */
@Entity
public class Editor extends Login {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_EDITOR")//编辑权限
        );
    }
}

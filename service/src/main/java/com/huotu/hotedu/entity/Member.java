package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Collection;

/**
 * Created by luffy on 2015/6/10.
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
public class Member extends Login{

    private String memeberField;

    public String getMemeberField() {
        return memeberField;
    }

    public void setMemeberField(String memeberField) {
        this.memeberField = memeberField;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

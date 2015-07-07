package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by luffy on 2015/6/10.
 *
 * 会员 可以参加考试，如果是付费用户可以观看付费视频。
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
public class Member extends Login{

    @ManyToOne
    private Agent agent;

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

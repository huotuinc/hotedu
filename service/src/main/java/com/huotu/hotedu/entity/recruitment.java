package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by shiliting on 2015/7/11.
 *
 *
 *
 * @author shiliting741@163.com
 */
@Entity
public class recruitment extends Login{

    @ManyToOne
    private Agent agent;

    @Column
    private String pictureUri;
    private String name;
    private String tel;
    private String area;
    private String ConfirmPerson;   //确认人
    private String theClass;        //班级
    private String examAddress;     //考试地点
    private boolean isPass;         //是否通过
    private String status;          //状态
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyDate;    //报名时间
    private Date paymentDate;  //确认缴费时间
    private Date examDate;     //考试时间





    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

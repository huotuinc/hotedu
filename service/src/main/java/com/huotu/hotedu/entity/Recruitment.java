package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by shiliting on 2015/7/1.
 *
 * 招聘信息实体
 *
 * @author shiliting741@163.com
 */
@Entity
public class Recruitment implements Serializable {
    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 应聘的会员
     */
    @ManyToMany
    private Member applicant;        //应聘者
    private Enterprise enterprise;   //应聘企业
    @Column
    private int applyNumber;         //应聘总数
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;     //发布时间



}

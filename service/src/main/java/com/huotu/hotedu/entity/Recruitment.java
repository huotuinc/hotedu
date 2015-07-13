package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

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
    private Set<Member> applicants;
    @Column
    private int applyNumber;         //应聘总数
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUploadDate;     //发布时间

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastUploadDate() {
        return lastUploadDate;
    }

    public void setLastUploadDate(Date lastUploadDate) {
        this.lastUploadDate = lastUploadDate;
    }

    public int getApplyNumber() {
        return applyNumber;
    }

    public void setApplyNumber(int applyNumber) {
        this.applyNumber = applyNumber;
    }

    public Set<Member> getApplicants() {
        return applicants;
    }

    public void setApplicants(Set<Member> applicants) {
        this.applicants = applicants;
    }
}

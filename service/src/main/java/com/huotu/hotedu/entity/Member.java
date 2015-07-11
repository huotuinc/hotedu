package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by luffy on 2015/6/10.
 * Modify by shiliting on 2015/7/11
 *
 * 会员 可以参加考试，如果是付费用户可以观看付费视频。
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
public class Member extends Login{
    @ManyToOne
    private Agent agent;           //代理商
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

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getPictureUri() {
        return pictureUri;
    }

    public void setPictureUri(String pictureUri) {
        this.pictureUri = pictureUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getConfirmPerson() {
        return ConfirmPerson;
    }

    public void setConfirmPerson(String confirmPerson) {
        ConfirmPerson = confirmPerson;
    }

    public String getTheClass() {
        return theClass;
    }

    public void setTheClass(String theClass) {
        this.theClass = theClass;
    }

    public String getExamAddress() {
        return examAddress;
    }

    public void setExamAddress(String examAddress) {
        this.examAddress = examAddress;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setIsPass(boolean isPass) {
        this.isPass = isPass;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

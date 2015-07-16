package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

/**
 * Created by luffy on 2015/6/10.
 * Modify by cwb on 2015/7/15
 * <p/>
 * 会员 可以参加考试，如果是付费用户可以观看付费视频。
 *
 * @author luffy luffy.ja at gmail.com
 */
@Entity
public class Member extends Login {

    /**
     * 所属代理商
     */
    @ManyToOne
    private Agent agent;
    /**
     * 会员头像图片uri
     */
    @Column
    private String pictureUri;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 性别：0代表女生，1代表男生
     */
    private int sex;
    /**
     * 手机号
     */
    private String phoneNo;
    /**
     * 报考区域
     */
    private String area;
    /**
     * 报名信息确认者
     */
    private String ConfirmPerson;
    /**
     * 所属班级
     */
    private ClassTeam theClass;
    /**
     * 是否已缴费，默认：否
     */
    private boolean isPayed = false;
    /**
     * 是否通过考试
     */
    private boolean isPassed = false;
    /**
     * 报名时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyDate;
    /**
     * 缴费时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date payDate;

    /**
     * 注册时间
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setIsPassed(boolean isPassed) {
        this.isPassed = isPassed;
    }

    public boolean isPayed() {
        return isPayed;
    }

    public void setIsPayed(boolean isPayed) {
        this.isPayed = isPayed;
    }

    public Date getPayDate() {
        return payDate;
    }

    public void setPayDate(Date payDate) {
        this.payDate = payDate;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

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

    public ClassTeam getTheClass() {
        return theClass;
    }

    public void setTheClass(ClassTeam theClass) {
        this.theClass = theClass;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    @Override
    public String toString() {
        return "Member{" +
                "agent=" + agent +
                ", pictureUri='" + pictureUri + '\'' +
                ", realName='" + realName + '\'' +
                ", sex=" + sex +
                ", phoneNo='" + phoneNo + '\'' +
                ", area='" + area + '\'' +
                ", ConfirmPerson='" + ConfirmPerson + '\'' +
                ", theClass=" + theClass +
                ", isPayed=" + isPayed +
                ", isPassed=" + isPassed +
                ", applyDate=" + applyDate +
                ", payDate=" + payDate +
                ", registerDate=" + registerDate +
                '}';
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}

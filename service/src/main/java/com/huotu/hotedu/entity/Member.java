package com.huotu.hotedu.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Arrays;
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
     * 报名信息确认者
     */
    private String confirmPerson;

    /**
     * 所属班级
     */
    @ManyToOne
    private ClassTeam theClass;
    /**
     * 是否已缴费，默认：否
     */
    private boolean payed = false;
    /**
     * 是否通过考试
     * 0:还未考试，1:考试通过,2:考试没通过
     */
    private int passed=0;
    /**
     * 领证状态
     * 0：未领证，1：已领证，2申请领证中
     */
    private int certificateStatus =0;

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

    /**
     * 申请领证时间
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date applyCertificateDate;

    /**
     * 实际领证时间
     * @return
     */
    @Temporal(TemporalType.TIMESTAMP)
    private Date receiveCertificateDate;

    /**
     * 电子证书
     * @return
     */
    @OneToOne
    private Certificate certificate;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getPassed() {
        return passed;
    }

    public void setPassed(int passed) {
        this.passed = passed;
    }

    public boolean isPayed() {
        return payed;
    }

    public void setPayed(boolean payed) {
        this.payed = payed;
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


    public String getConfirmPerson() {
        return confirmPerson;
    }

    public void setConfirmPerson(String confirmPerson) {
        confirmPerson = confirmPerson;
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

    public int getCertificateStatus() {
        return certificateStatus;
    }

    public void setCertificateStatus(int certificateStatus) {
        this.certificateStatus = certificateStatus;
    }

    @Override
    public String getUsername() {
        return realName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(
                new SimpleGrantedAuthority("ROLE_MEMBER")//进入学员个人中心的权限
        );
    }

    public Date getApplyCertificateDate() {
        return applyCertificateDate;
    }

    public void setApplyCertificateDate(Date applyCertificateDate) {
        this.applyCertificateDate = applyCertificateDate;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public Date getReceiveCertificateDate() {
        return receiveCertificateDate;
    }

    public void setReceiveCertificateDate(Date receiveCertificateDate) {
        this.receiveCertificateDate = receiveCertificateDate;
    }
}

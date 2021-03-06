package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by cwb on 2015/7/16.
 */
@Entity
public class Exam implements Serializable {
    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 考场名字
     */
    private String examName;
    /**
     * 考试时间
     */
    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date examDate;
    /**
     * 考试地点
     */
    private String examAddress;

    /**
     * 所属代理商
     */
    @ManyToOne
    private Agent agent;

    public Agent getAgent() {
        return agent;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamAddress() {
        return examAddress;
    }

    public void setExamAddress(String examAddress) {
        this.examAddress = examAddress;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }
}

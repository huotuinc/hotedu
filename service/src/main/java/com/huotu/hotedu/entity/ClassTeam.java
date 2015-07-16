package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 培训班
 * Created by cwb on 2015/7/16.
 */
@Entity
public class ClassTeam implements Serializable {
    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    /**
     * 班级人数
     */
    @Column
    private int memberNum;
    /**
     * 所属代理商
     */
    @ManyToOne
    private Agent agent;
    /**
     * 班级名称
     */
    private String className;

    /**
     * 考场
     */
    private Exam exam;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public int getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(int memberNum) {
        this.memberNum = memberNum;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

}

package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shiliting on 2015/6/25.
 *
 * @author shiliting
 */
@Entity
public  class Company implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Lob
    private String introduction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}

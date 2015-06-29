package com.huotu.hotedu.entity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by shiliting on 2015/6/25.
 *
 * @author shiliting
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"title"}))
public  class Link implements Serializable {

    private static final long serialVersionUID = -349012453592429794L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String title;
    @Column
    private String url;
}

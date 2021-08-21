package com.eastbabel.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "question")
@Data
public class Question extends BaseEntity {

    private static final long serialVersionUID = 8607715971959507406L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    @Basic
    @Column(name = "question")
    private String question;
    @Basic
    @Column(name = "answer")
    private String answer;
    @Basic
    @Column(name = "active")
    private Integer active;

}

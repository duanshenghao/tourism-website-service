package com.eastbabel.dao.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "notify")
@Data
public class Notify extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String company;

    private String phone;

    private String content;

    private String email;

    private Integer status;

    private String remark;
}

package com.eastbabel.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "article_catalog")
@Data
public class ArticleCatalog extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "cat_name")
    private String catName;
    @Basic
    @Column(name = "cat_desc")
    private String catDesc;
    @Basic
    @Column(name = "built_in")
    private Integer builtIn;

    @Basic
    @Column(name = "status")
    private Integer status;

}

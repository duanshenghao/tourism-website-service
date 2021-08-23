package com.eastbabel.dao.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "article")
@Data
public class Article extends BaseEntity {
    private static final long serialVersionUID = -6363052414605173529L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Basic
    @Column(name = "cat_id")
    private Integer catId;

    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "img_key")
    private String imgKey;
    @Basic
    @Column(name = "summary")
    private String summary;
    @Lob
    @Column(name = "content")
    private String content;
    @Basic
    @Column(name = "seq")
    private Integer seq;
    @Basic
    @Column(name = "article_status")
    private Integer articleStatus;
    @ManyToOne
    @JoinColumn(name="cat_id",insertable = false,updatable = false)
    private ArticleCatalog articleCatalog;

}

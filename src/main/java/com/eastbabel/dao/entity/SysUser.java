package com.eastbabel.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user")
@Getter
@Setter
public class SysUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "username")
    private String userName;

    @Basic
    @Column(name = "salt")
    private String salt;

    @Basic
    @Column(name = "password")
    private String password;

    @Basic
    @Column(name = "active_status")
    private Integer activeStatus;

    @Basic
    @Column(name = "email")
    private String email;

    /**
     * 创建人
     */
    @Basic
    @Column(name = "creator")
    private Integer creator;


    /**
     * 创建时间
     */
    @Basic
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Basic
    @Column(name = "updater")
    private Integer updater;


    /**
     * 更新时间
     */
    @Basic
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @Basic
    @Column(name = "deleter")
    private Integer deleter;

    @Basic
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;

}

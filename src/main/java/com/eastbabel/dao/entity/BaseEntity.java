package com.eastbabel.dao.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -2178399209330218809L;
    /**
     * 创建人
     */
    @ManyToOne
    @JoinColumn(name = "creator",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser creatorUser;

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
    @ManyToOne
    @JoinColumn(name = "updater",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser updaterUser;

    @Basic
    @Column(name = "updater")
    private Integer updater;

    /**
     * 更新时间
     */
    @Basic
    @Column(name = "update_time")
    private LocalDateTime updateTime;

    /**
     * 删除人
     */
    @Basic
    @Column(name = "deleter")
    private Integer deleter;

    @ManyToOne
    @JoinColumn(name = "deleter",referencedColumnName = "id",insertable = false,updatable = false)
    private SysUser deleteUser;

    /**
     * 删除时间
     */
    @Basic
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
}

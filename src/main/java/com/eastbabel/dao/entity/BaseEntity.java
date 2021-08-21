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
    @JoinColumn(name = "creator",referencedColumnName = "id")
    private SysUser creatorUser;

    @Basic
    @Column(name = "creator", insertable = false, updatable = false)
    private Integer creator;

    /**
     * 创建时间
     */
    @Basic
    @Column(name = "create_time", insertable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @ManyToOne
    @JoinColumn(name = "updater",referencedColumnName = "id")
    private SysUser updaterUser;

    @Basic
    @Column(name = "updater", insertable = false, updatable = false)
    private Integer updater;

    /**
     * 更新时间
     */
    @Basic
    @Column(name = "update_time", insertable = false, updatable = false)
    private LocalDateTime updateTime;

    /**
     * 删除人
     */
    @Basic
    @Column(name = "deleter", insertable = false, updatable = false)
    private Integer deleter;

    @ManyToOne
    @JoinColumn(name = "deleter",referencedColumnName = "id")
    private SysUser deleteUser;

    /**
     * 删除时间
     */
    @Basic
    @Column(name = "delete_time")
    private LocalDateTime deleteTime;
}

package com.eastbabel.bo.user;

import com.eastbabel.bo.RestEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserBo extends RestEntity {

    private Integer id;

    private String username;

    private String email;

}

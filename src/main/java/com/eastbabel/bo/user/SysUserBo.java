package com.eastbabel.bo.user;

import com.eastbabel.bo.RestEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysUserBo extends RestEntity {

    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("是否冻结（0：否，1：是）")
    private Integer activeStatus;

}

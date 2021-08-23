package com.eastbabel.bo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditUser {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("用户名")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("是否冻结")
    private Integer activeStatus;
}

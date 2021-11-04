package com.eastbabel.bo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateUserReq implements Serializable {

    private static final long serialVersionUID = 3049933153320066324L;
    @ApiModelProperty("账号")
    private String username;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("是否冻结")
    private Integer activeStatus;
}

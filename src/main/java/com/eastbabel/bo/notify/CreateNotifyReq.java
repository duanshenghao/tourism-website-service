package com.eastbabel.bo.notify;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateNotifyReq implements Serializable {

    private static final long serialVersionUID = 3049933153320066324L;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("公司")
    private String company;
    @ApiModelProperty("电话")
    private String phone;
    @ApiModelProperty("描述")
    private String content;
    @ApiModelProperty("邮箱")
    private String email;

}

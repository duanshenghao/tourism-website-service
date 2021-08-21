package com.eastbabel.bo.notify;

import com.eastbabel.bo.RestEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class NotifyBo extends RestEntity implements Serializable {
    @ApiModelProperty("id")
    private Integer id;

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

    @ApiModelProperty("状态")
    private Integer status;
}

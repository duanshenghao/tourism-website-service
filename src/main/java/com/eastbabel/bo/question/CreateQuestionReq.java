package com.eastbabel.bo.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateQuestionReq implements Serializable {

    private static final long serialVersionUID = 3049933153320066324L;
    @ApiModelProperty("问题")
    private String question;
    @ApiModelProperty("回答")
    private String answer;
    @ApiModelProperty("状态")
    private Integer active;
}

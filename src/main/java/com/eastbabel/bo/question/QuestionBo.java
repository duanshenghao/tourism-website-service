package com.eastbabel.bo.question;

import com.eastbabel.bo.RestEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionBo extends RestEntity implements Serializable {
    private static final long serialVersionUID = -2230980076403934849L;
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("问题")
    private String question;
    @ApiModelProperty("回答")
    private String answer;
}

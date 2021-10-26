package com.eastbabel.bo.article;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateArticleReq implements Serializable {

    private static final long serialVersionUID = 3049933153320066324L;

    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("封面图key")
    private String imageKey;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("排序")
    private Integer seq;
    @ApiModelProperty("文章状态")
    private Integer articleStatus;
    @ApiModelProperty("栏目Id")
    private Integer catId;
}

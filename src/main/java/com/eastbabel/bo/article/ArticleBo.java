package com.eastbabel.bo.article;

import com.eastbabel.bo.RestEntity;
import com.eastbabel.dao.entity.ArticleCatalog;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class ArticleBo extends RestEntity implements Serializable {
    private static final long serialVersionUID = -2230980076403934849L;
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("栏目Id")
    private Integer catId;
    @ApiModelProperty("标题")
    private String title;
    @ApiModelProperty("封面图key")
    private String imgKey;
    @ApiModelProperty("摘要")
    private String summary;
    @ApiModelProperty("内容")
    private String content;
    @ApiModelProperty("图片地址")
    private String imageUrl;
    @ApiModelProperty("栏目名称")
    private String catName;
}

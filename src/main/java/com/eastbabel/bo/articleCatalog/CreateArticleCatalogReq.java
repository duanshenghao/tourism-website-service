package com.eastbabel.bo.articleCatalog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateArticleCatalogReq implements Serializable {

    private static final long serialVersionUID = 3049933153320066324L;

    @ApiModelProperty("栏目名称")
    private String catName;
    @ApiModelProperty("栏目描述")
    private String catDesc;
    @ApiModelProperty("是否上架")
    private Integer status;
}

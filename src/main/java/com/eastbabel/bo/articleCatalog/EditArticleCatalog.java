package com.eastbabel.bo.articleCatalog;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class EditArticleCatalog {

    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("栏目名称")
    private String catName;
    @ApiModelProperty("栏目描述")
    private String catDesc;
    @ApiModelProperty("是否上架")
    private Integer status;
}

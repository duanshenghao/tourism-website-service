package com.eastbabel.bo.articleCatalog;

import com.eastbabel.bo.RestEntity;
import com.eastbabel.dao.entity.Article;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class ArticleCatalogBo extends RestEntity implements Serializable {
    private static final long serialVersionUID = -2230980076403934849L;
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("栏目名称")
    private String catName;
    @ApiModelProperty("栏目描述")
    private String catDesc;
    @ApiModelProperty("是否内置栏目")
    private Integer builtIn;

    private List<Article> articleList = new ArrayList<>();
}

package com.eastbabel.service;


import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.articleCatalog.ArticleCatalogBo;
import com.eastbabel.bo.articleCatalog.CreateArticleCatalogReq;
import com.eastbabel.bo.articleCatalog.EditArticleCatalog;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.dao.entity.ArticleCatalog;

import java.util.List;

public interface ArticleCatalogService {
    List<ArticleCatalogBo> getArticleCatalog();

    ArticleCatalogBo createArticleCatalog(CreateArticleCatalogReq createArticleCatalogReq);

    void editArticleCatalog(EditArticleCatalog editArticleCatalog);

    void deleteArticleCatalog(Integer id);

    PagedResource<ArticleCatalogBo> getArticleCatalogs(Integer builtIn,Integer status, Integer page, Integer size);

    void updateArticleCatalogStatus(Integer id, Integer status);
}

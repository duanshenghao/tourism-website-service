package com.eastbabel.service;


import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.articleCatalog.ArticleCatalogBo;
import com.eastbabel.bo.articleCatalog.CreateArticleCatalogReq;
import com.eastbabel.bo.base.PagedResource;

import java.util.List;

public interface ArticleCatalogService {
    List<ArticleCatalogBo> getArticleCatalog();

    ArticleCatalogBo createArticleCatalog(CreateArticleCatalogReq createArticleCatalogReq);

    void editArticleCatalog(ArticleCatalogBo ArticleCatalogBo);

    void deleteArticleCatalog(Integer id);

    PagedResource<ArticleCatalogBo> getArticleCatalogs(Integer builtIn, Integer page, Integer size);

    void updateArticleCatalogStatus(Integer id, Integer builtIn);
}

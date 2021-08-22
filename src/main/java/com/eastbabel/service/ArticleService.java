package com.eastbabel.service;

import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.article.CreateArticleReq;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.question.CreateQuestionReq;
import com.eastbabel.bo.question.QuestionBo;

import java.util.List;

public interface ArticleService {
    List<ArticleBo> getArticle();

    ArticleBo createArticle(CreateArticleReq createArticleReq);

    void editArticle(ArticleBo articleBo);

    void deleteArticle(Integer id);

    PagedResource<ArticleBo> getArticles(Integer articleStatus,Integer catId, Integer page, Integer size);

    void updateArticleStatus(Integer id, Integer articleStatus);
}

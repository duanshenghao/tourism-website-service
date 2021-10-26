package com.eastbabel.dao.repository;

import com.eastbabel.dao.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    List<Article> findByDeleterIsNullAndArticleStatusOrderBySeqAsc(Integer articleStatus);

    Optional<Article> findByIdAndDeleterIsNull(Integer sysId);
}

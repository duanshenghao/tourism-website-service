package com.eastbabel.dao.repository;

import com.eastbabel.dao.entity.ArticleCatalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface ArticleCatalogRepository extends JpaRepository<ArticleCatalog, Integer>, JpaSpecificationExecutor<ArticleCatalog> {

    List<ArticleCatalog> findByDeleterIsNull();
}

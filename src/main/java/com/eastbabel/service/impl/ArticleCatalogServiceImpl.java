package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.articleCatalog.ArticleCatalogBo;
import com.eastbabel.bo.articleCatalog.CreateArticleCatalogReq;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.dao.entity.Article;
import com.eastbabel.dao.entity.ArticleCatalog;
import com.eastbabel.dao.repository.ArticleCatalogRepository;
import com.eastbabel.exception.CustomException;
import com.eastbabel.service.ArticleCatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleCatalogServiceImpl implements ArticleCatalogService {

    @Resource
    private ArticleCatalogRepository articleCatalogRepository;
    @Resource
    private WebContext webContext;

    @Override
    public List<ArticleCatalogBo> getArticleCatalog() {
        return articleCatalogRepository.findByDeleterIsNull().stream().map(articleCatalog -> {
            ArticleCatalogBo articleCatalogBo = new ArticleCatalogBo();
            articleCatalogBo.setId(articleCatalog.getId());
            articleCatalogBo.setCatName(articleCatalog.getCatName());
            articleCatalogBo.setCatDesc(articleCatalog.getCatDesc());
            articleCatalogBo.setBuiltIn(articleCatalog.getBuiltIn());
            articleCatalogBo.setArticleList(articleCatalog.getArticleList());
            return articleCatalogBo;
        }).collect(Collectors.toList());
    }

    @Override
    public ArticleCatalogBo createArticleCatalog(CreateArticleCatalogReq createArticleCatalogReq) {
        LocalDateTime now = LocalDateTime.now();
        ArticleCatalog articleCatalog = new ArticleCatalog();
        articleCatalog.setCatName(createArticleCatalogReq.getCatName());
        articleCatalog.setCatDesc(createArticleCatalogReq.getCatDesc());
        articleCatalog.setBuiltIn(createArticleCatalogReq.getBuiltIn());
        articleCatalog.setCreator(webContext.getUserId());
        articleCatalog.setCreateTime(now);
        articleCatalog.setUpdater(webContext.getUserId());
        articleCatalog.setUpdateTime(now);
        articleCatalogRepository.saveAndFlush(articleCatalog);
        ArticleCatalogBo bo = new ArticleCatalogBo();
        bo.setId(articleCatalog.getId());
        bo.setCatName(createArticleCatalogReq.getCatName());
        bo.setCatDesc(createArticleCatalogReq.getCatDesc());
        bo.setBuiltIn(createArticleCatalogReq.getBuiltIn());
        return bo;
    }

    @Override
    public void editArticleCatalog(ArticleCatalogBo articleCatalogBo) {
        ArticleCatalog articleCatalog = articleCatalogRepository.findById(articleCatalogBo.getId()).orElseThrow(() -> new CustomException("栏目不存在"));
        articleCatalog.setCatName(articleCatalogBo.getCatName());
        articleCatalog.setCatDesc(articleCatalogBo.getCatDesc());
        articleCatalog.setBuiltIn(articleCatalogBo.getBuiltIn());
        articleCatalog.setUpdater(webContext.getUserId());
        articleCatalog.setUpdateTime(LocalDateTime.now());
        articleCatalogRepository.saveAndFlush(articleCatalog);
    }

    @Override
    public void deleteArticleCatalog(Integer id) {
        LocalDateTime now = LocalDateTime.now();
        ArticleCatalog articleCatalog = articleCatalogRepository.findById(id).orElseThrow(() -> new CustomException("栏目不存在"));
        articleCatalog.setDeleter(webContext.getUserId());
        articleCatalog.setDeleteTime(now);
        articleCatalogRepository.save(articleCatalog);
    }

    @Override
    public PagedResource<ArticleCatalogBo> getArticleCatalogs(Integer builtIn, Integer page, Integer size) {
        Sort seq = Sort.by("updateTime");
        Pageable pageable = PageRequest.of(page - 1, size, seq);
        Specification<ArticleCatalog> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (builtIn != null) {
                predicates.add(criteriaBuilder.equal(root.get("builtIn"), builtIn));
            }
            predicates.add(criteriaBuilder.isNull(root.get("deleter")));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<ArticleCatalog> articlePage = articleCatalogRepository.findAll(specification, pageable);
        List<ArticleCatalogBo> collect = articlePage.stream().map(this::toArticleCatalogBo).collect(Collectors.toList());
        return new PagedResource<>(collect, page, size, articlePage.getTotalElements());
    }

    @Override
    public void updateArticleCatalogStatus(Integer id, Integer builtIn) {
        ArticleCatalog articleCatalog = articleCatalogRepository.findById(id).orElseThrow(() -> new CustomException("文章栏目不存在"));
        articleCatalog.setBuiltIn(builtIn);
        articleCatalog.setUpdater(webContext.getUserId());
        articleCatalog.setUpdateTime(LocalDateTime.now());
    }

    private ArticleCatalogBo toArticleCatalogBo(ArticleCatalog articleCatalog){
        ArticleCatalogBo articleCatalogBo = new ArticleCatalogBo();
        articleCatalogBo.setId(articleCatalog.getId());
        articleCatalogBo.setCatName(articleCatalog.getCatName());
        articleCatalogBo.setCatDesc(articleCatalog.getCatDesc());
        articleCatalogBo.setArticleList(articleCatalog.getArticleList());
        articleCatalogBo.setCreator(articleCatalog.getCreatorUser());
        articleCatalogBo.setCreateTime(articleCatalog.getCreateTime());
        articleCatalogBo.setUpdater(articleCatalog.getUpdaterUser());
        articleCatalogBo.setUpdateTime(articleCatalog.getUpdateTime());
        return articleCatalogBo;
    }

}

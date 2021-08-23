package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.article.CreateArticleReq;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.dao.entity.Article;
import com.eastbabel.dao.entity.SysUser;
import com.eastbabel.dao.repository.ArticleRepository;
import com.eastbabel.exception.CustomException;
import com.eastbabel.service.ArticleService;
import com.eastbabel.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class ArticleServiceImpl implements ArticleService {

    @Resource
    private ArticleRepository articleRepository;
    @Autowired
    private WebContext webContext;
    @Value("${qiniu.domain}")
    private String domain;

    @Override
    public List<ArticleBo> getArticle() {
        return articleRepository.findByDeleterIsNullAndArticleStatusOrderBySeqDesc(1).stream().map(article -> {
            ArticleBo articleBo = new ArticleBo();
            articleBo.setId(article.getId());
            articleBo.setTitle(article.getTitle());
            articleBo.setImgKey(article.getImgKey());
            articleBo.setSummary(article.getSummary());
            articleBo.setContent(article.getContent());
            return articleBo;
        }).collect(Collectors.toList());
    }

    @Override
    public ArticleBo createArticle(CreateArticleReq createArticleReq) {
        LocalDateTime now = LocalDateTime.now();
        Article article = new Article();
        article.setCatId(createArticleReq.getCatId());
        article.setTitle(createArticleReq.getTitle());
        article.setImgKey(createArticleReq.getImgKey());
        article.setSummary(createArticleReq.getSummary());
        article.setContent(createArticleReq.getContent());
        article.setSeq(createArticleReq.getSeq());
        article.setArticleStatus(createArticleReq.getArticleStatus());
        article.setCreator(webContext.getUserId());
        article.setCreateTime(now);
        article.setUpdater(webContext.getUserId());
        article.setUpdateTime(now);
        articleRepository.saveAndFlush(article);
        ArticleBo bo = new ArticleBo();
        bo.setId(article.getId());
        bo.setTitle(article.getTitle());
        bo.setImgKey(createArticleReq.getImgKey());
        bo.setSummary(createArticleReq.getSummary());
        bo.setContent(createArticleReq.getContent());
        return bo;
    }

    @Override
    public void editArticle(ArticleBo articleBo) {
        Article article = articleRepository.findById(articleBo.getId()).orElseThrow(() -> new CustomException("文章不存在"));
        if(!(articleBo.getImgKey().equals(article.getImgKey()))){
            QiniuUtils.deleteImage(article.getImgKey());
        }
        article.setTitle(articleBo.getTitle());
        article.setImgKey(articleBo.getImgKey());
        article.setSummary(articleBo.getSummary());
        article.setContent(articleBo.getContent());
        article.setUpdater(webContext.getUserId());
        article.setUpdateTime(LocalDateTime.now());
        articleRepository.saveAndFlush(article);

    }

    @Override
    public void deleteArticle(Integer id) {
        LocalDateTime now = LocalDateTime.now();
        Article article = articleRepository.findByIdAndDeleterIsNull(id).orElseThrow(() -> new CustomException("文章不存在"));
        article.setDeleter(webContext.getUserId());
        article.setDeleteTime(now);
        articleRepository.save(article);
    }

    @Override
    public PagedResource<ArticleBo> getArticles(Integer articleStatus,Integer catId, Integer page, Integer size) {
        Sort seq = Sort.by("updateTime");
        Pageable pageable = PageRequest.of(page - 1, size, seq);
        Specification<Article> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (articleStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("articleStatus"), articleStatus));
            }
            if(catId != null){
                predicates.add(criteriaBuilder.equal(root.get("catId"), catId));
            }
            predicates.add(criteriaBuilder.isNull(root.get("deleter")));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<Article> articlePage = articleRepository.findAll(specification, pageable);
        List<ArticleBo> collect = articlePage.stream().map(this::toArticleBo).collect(Collectors.toList());
        return new PagedResource<>(collect, page, size, articlePage.getTotalElements());
    }

    @Override
    public void updateArticleStatus(Integer id, Integer articleStatus) {
        Article article = articleRepository.findById(id).orElseThrow(() -> new CustomException("文章不存在"));
        article.setArticleStatus(articleStatus);
        article.setUpdater(webContext.getUserId());
        article.setUpdateTime(LocalDateTime.now());
        articleRepository.saveAndFlush(article);
    }

    private ArticleBo toArticleBo(Article article){
        ArticleBo articleBo = new ArticleBo();
        articleBo.setId(article.getId());
        articleBo.setCatId(article.getCatId());
        articleBo.setTitle(article.getTitle());
        articleBo.setImgKey(article.getImgKey());
        articleBo.setImageUrl(domain+article.getImgKey());
        articleBo.setSummary(article.getSummary());
        articleBo.setContent(article.getContent());
        SysUser creatorUser = article.getCreatorUser();
        if(creatorUser!=null){
            articleBo.setCreatorId(creatorUser.getId());
            articleBo.setCreatorName(creatorUser.getUserName());
        }
        articleBo.setCreateTime(article.getCreateTime());
        SysUser updateUser = article.getUpdaterUser();
        if(updateUser!=null){
            articleBo.setUpdaterId(updateUser.getId());
            articleBo.setUpdaterName(updateUser.getUserName());
        }
        articleBo.setUpdateTime(article.getUpdateTime());
        articleBo.setCatName(article.getArticleCatalog().getCatName());
        return articleBo;
    }

}

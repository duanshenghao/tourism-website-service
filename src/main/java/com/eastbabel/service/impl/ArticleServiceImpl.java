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
import com.qiniu.util.Auth;
import org.apache.commons.lang3.StringUtils;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
    @Value("${qiniu.ak}")
    private String accessKey;
    @Value("${qiniu.sk}")
    private String secretKey;

    @Override
    public List<ArticleBo> getArticle() {
        return articleRepository.findByDeleterIsNullAndArticleStatusOrderBySeqAsc(1).stream().map(article -> {
            ArticleBo articleBo = new ArticleBo();
            articleBo.setId(article.getId());
            articleBo.setTitle(article.getTitle());
            articleBo.setImgKey(article.getImgKey());
            articleBo.setSummary(article.getSummary());
            articleBo.setContent(article.getContent());
            articleBo.setArticleStatus(article.getArticleStatus());
            articleBo.setSeq(article.getSeq());
            String fileName = article.getImgKey();
            if(StringUtils.isNotEmpty(fileName)){
                String domainOfBucket = domain;
                String encodedFileName = null;
                try {
                    encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
                Auth auth = Auth.create(accessKey, secretKey);
                long expireInSeconds = 31536000;
                String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
                articleBo.setImageUrl(finalUrl);
            }

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
        if(createArticleReq.getArticleStatus()!=null){
            article.setArticleStatus(createArticleReq.getArticleStatus());
        }else{
            article.setArticleStatus(1);
        }
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
        if(StringUtils.isNotEmpty(articleBo.getImgKey())&&StringUtils.isNotEmpty(article.getImgKey())){
            if(!(articleBo.getImgKey().equals(article.getImgKey()))){
                QiniuUtils.deleteImage(article.getImgKey());
            }
        }
        if(StringUtils.isEmpty(articleBo.getImgKey())&&StringUtils.isNotEmpty(article.getImgKey())){
            QiniuUtils.deleteImage(article.getImgKey());
        }
        article.setTitle(articleBo.getTitle());
        article.setImgKey(articleBo.getImgKey());
        article.setSummary(articleBo.getSummary());
        article.setSeq(articleBo.getSeq());
        article.setArticleStatus(articleBo.getArticleStatus());
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
        String fileName = article.getImgKey();
        if(StringUtils.isNotEmpty(fileName)){
            String domainOfBucket = domain;
            String encodedFileName = null;
            try {
                encodedFileName = URLEncoder.encode(fileName, "utf-8").replace("+", "%20");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String publicUrl = String.format("%s/%s", domainOfBucket, encodedFileName);
            Auth auth = Auth.create(accessKey, secretKey);
            long expireInSeconds = 31536000;
            String finalUrl = auth.privateDownloadUrl(publicUrl, expireInSeconds);
            articleBo.setImageUrl(finalUrl);
        }

        articleBo.setSummary(article.getSummary());
        articleBo.setContent(article.getContent());
        articleBo.setArticleStatus(article.getArticleStatus());
        articleBo.setSeq(article.getSeq());
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
        if(article.getArticleCatalog()!=null){
            articleBo.setCatName(article.getArticleCatalog().getCatName());
        }
        return articleBo;
    }

}

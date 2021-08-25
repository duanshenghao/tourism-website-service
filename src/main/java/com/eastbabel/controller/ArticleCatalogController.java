package com.eastbabel.controller;

import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.article.CreateArticleReq;
import com.eastbabel.bo.articleCatalog.ArticleCatalogBo;
import com.eastbabel.bo.articleCatalog.CreateArticleCatalogReq;
import com.eastbabel.bo.articleCatalog.EditArticleCatalog;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.dao.entity.ArticleCatalog;
import com.eastbabel.service.ArticleCatalogService;
import com.eastbabel.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "文章栏目接口")
@RequestMapping("api")
public class ArticleCatalogController {

    @Resource
    private ArticleCatalogService articleCatalogService;

    @GetMapping("articleCatalog/list/page")
    @ApiOperation("获取文章栏目列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "builtIn", value = "是否内置栏目"),
            @ApiImplicitParam(name = "status", value = "状态"),
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "size", value = "数据条数")
    })
    public ResponseEntity<PagedResource<ArticleCatalogBo>> getArticleCatalogs(
            @RequestParam(value = "builtIn", required = false) Integer builtIn,
            @RequestParam(value = "status",required = false) Integer status,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(articleCatalogService.getArticleCatalogs(builtIn,status,page, size));
    }

    @GetMapping("articleCatalog/list")
    @ApiOperation("获取文章栏目列表")
    public ResponseEntity<List<ArticleCatalogBo>> getArticle() {
        return ResponseEntity.ok(articleCatalogService.getArticleCatalog());
    }

    @PutMapping("articleCatalog")
    @ApiOperation("新增文章栏目")
    public ResponseEntity createArticleCatalog(@Validated @RequestBody CreateArticleCatalogReq createArticleCatalogReq){
        return ResponseEntity.ok(articleCatalogService.createArticleCatalog(createArticleCatalogReq));
    }

    @PostMapping("articleCatalog")
    @ApiOperation("编辑文章栏目")
    public ResponseEntity<String> editArticleCatalog(@Validated @RequestBody EditArticleCatalog editArticleCatalog) {
        articleCatalogService.editArticleCatalog(editArticleCatalog);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("articleCatalog/{id}")
    @ApiOperation("删除文章栏目")
    public ResponseEntity<String> deleteArticleCatalog(@PathVariable("id") Integer id) {
        articleCatalogService.deleteArticleCatalog(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("articleCatalog/{id}/status/{status}")
    @ApiOperation("修改文章栏目状态")
    public ResponseEntity<String> updateArticleCatalogStatus(@PathVariable("id") Integer id,
                                                      @PathVariable("status") Integer status) {
        articleCatalogService.updateArticleCatalogStatus(id, status);
        return ResponseEntity.succeed();
    }
}

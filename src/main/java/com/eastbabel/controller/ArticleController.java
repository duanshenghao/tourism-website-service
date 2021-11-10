package com.eastbabel.controller;

import com.eastbabel.bo.article.ArticleBo;
import com.eastbabel.bo.article.CreateArticleReq;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.bo.question.QuestionBo;
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
@Api(tags = "文章接口")
@RequestMapping("api")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("article/list/page")
    @ApiOperation("获取文章列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleStatus", value = "文章状态"),
            @ApiImplicitParam(name = "catId", value = "栏目Id"),
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "size", value = "数据条数")
    })
    public ResponseEntity<PagedResource<ArticleBo>> getArticles(
            @RequestParam(value = "articleStatus", required = false) Integer articleStatus,
            @RequestParam(value = "catId", required = false) Integer catId,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size,
            @RequestParam(value = "title", defaultValue = "") String title) {
        return ResponseEntity.ok(articleService.getArticles(articleStatus,catId, page, size,title));
    }

    @GetMapping("article/list")
    @ApiOperation("获取文章列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "catId", value = "栏目Id")
    })
    public ResponseEntity<List<ArticleBo>> getArticleList(@RequestParam(value = "catId", required = false) Integer catId) {
        return ResponseEntity.ok(articleService.getArticle(catId));
    }

    @PutMapping("article")
    @ApiOperation("新增文章")
    public ResponseEntity createArticle(@Validated @RequestBody CreateArticleReq createArticleReq){
        return ResponseEntity.ok(articleService.createArticle(createArticleReq));
    }

    @PostMapping("article")
    @ApiOperation("编辑文章")
    public ResponseEntity<String> editArticle(@Validated @RequestBody ArticleBo articleBo) {
        articleService.editArticle(articleBo);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("article/{id}")
    @ApiOperation("删除文章")
    public ResponseEntity<String> deleteArticle(@PathVariable("id") Integer id) {
        articleService.deleteArticle(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("article/{id}/status/{articleStatus}")
    @ApiOperation("修改文章状态")
    public ResponseEntity<String> updateArticleStatus(@PathVariable("id") Integer id,
                                                       @PathVariable("articleStatus") Integer articleStatus) {
        articleService.updateArticleStatus(id, articleStatus);
        return ResponseEntity.succeed();
    }

    @GetMapping("articleDetail/{id}")
    @ApiOperation("获取文章详情")
    public ResponseEntity<ArticleBo> getArticle(@PathVariable("id") Integer id) {
        ArticleBo articleDetail = articleService.getArticleDetail(id);
        return ResponseEntity.ok(articleDetail);
    }
}

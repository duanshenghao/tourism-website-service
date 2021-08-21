package com.eastbabel.controller;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.bo.notify.NotifyBo;
import com.eastbabel.bo.question.CreateQuestionReq;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.service.QuestionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(tags = "常见问题接口")
@RequestMapping("api")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @GetMapping("question/list/page")
    @ApiOperation("获取常见问题列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "active", value = "状态"),
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "size", value = "数据条数")
    })
    public ResponseEntity<PagedResource<QuestionBo>> getQuestions(
            @RequestParam(value = "active", required = false) Integer active,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(questionService.getQuestions(active, page, size));
    }

    @GetMapping("question/list")
    @ApiOperation("获取常见问题列表")
    public ResponseEntity<List<QuestionBo>> getQuestion() {
        return ResponseEntity.ok(questionService.getQuestion());
    }

    @PutMapping("question")
    @ApiOperation("新增常见问题")
    public ResponseEntity createQuestion(@Validated @RequestBody CreateQuestionReq createQuestionReq){
        return ResponseEntity.ok(questionService.createQuestion(createQuestionReq));
    }

    @PostMapping("question")
    @ApiOperation("编辑常见问题")
    public ResponseEntity<String> editQuestion(@Validated @RequestBody QuestionBo questionBo) {
        questionService.editQuestion(questionBo);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("question/{id}")
    @ApiOperation("删除常见问题")
    public ResponseEntity<String> deleteQuestion(@PathVariable("id") Integer id) {
        questionService.deleteQuestion(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("question/{id}/status/{active}")
    @ApiOperation("修改轮播图状态")
    public ResponseEntity<String> updateQuestionStatus(@PathVariable("id") Integer id,
                                                          @PathVariable("active") Integer active) {
        questionService.updateQuestionStatus(id, active);
        return ResponseEntity.succeed();
    }
}

package com.eastbabel.service;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.notify.NotifyBo;
import com.eastbabel.bo.question.CreateQuestionReq;
import com.eastbabel.bo.question.QuestionBo;

import java.util.List;

public interface QuestionService {
    List<QuestionBo> getQuestion();

    QuestionBo createQuestion(CreateQuestionReq createQuestionReq);

    void editQuestion(QuestionBo questionBo);

    void deleteQuestion(Integer id);

    PagedResource<QuestionBo> getQuestions(Integer active, Integer page, Integer size);

    void updateQuestionStatus(Integer id, Integer active);
}

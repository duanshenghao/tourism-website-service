package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.question.CreateQuestionReq;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.dao.entity.Question;
import com.eastbabel.dao.entity.SysUser;
import com.eastbabel.dao.repository.QuestionRepository;
import com.eastbabel.exception.CustomException;
import com.eastbabel.service.QuestionService;
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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    @Resource
    private QuestionRepository questionRepository;
    @Autowired
    private WebContext webContext;

    @Override
    public List<QuestionBo> getQuestion() {
        return questionRepository.findByDeleterIsNullAndActive(1).stream().map(question -> {
            QuestionBo questionBo = new QuestionBo();
            questionBo.setId(question.getId());
            questionBo.setQuestion(question.getQuestion());
            questionBo.setAnswer(question.getAnswer());
            return questionBo;
        }).collect(Collectors.toList());
    }

    @Override
    public QuestionBo createQuestion(CreateQuestionReq createQuestionReq) {
        Question question = new Question();
        question.setQuestion(createQuestionReq.getQuestion());
        question.setAnswer(createQuestionReq.getAnswer());
        question.setActive(1);
        LocalDateTime now = LocalDateTime.now();
        question.setCreator(webContext.getUserId());
        question.setCreateTime(now);
        question.setUpdater(webContext.getUserId());
        question.setUpdateTime(now);
        questionRepository.saveAndFlush(question);
        QuestionBo bo = new QuestionBo();
        bo.setQuestion(createQuestionReq.getQuestion());
        bo.setAnswer(createQuestionReq.getAnswer());
        bo.setId(question.getId());
        return bo;
    }

    @Override
    public void editQuestion(QuestionBo questionBo) {
        Question question = questionRepository.findById(questionBo.getId()).orElseThrow(() -> new CustomException("问题不存在"));
        question.setQuestion(questionBo.getQuestion());
        question.setAnswer(questionBo.getAnswer());
        question.setUpdater(webContext.getUserId());
        question.setUpdateTime(LocalDateTime.now());
        questionRepository.saveAndFlush(question);
    }

    @Override
    public void deleteQuestion(Integer id) {
        Question question = questionRepository.findByIdAndDeleterIsNull(id).orElseThrow(() -> new CustomException("问题不存在"));
        question.setDeleter(webContext.getUserId());
        question.setDeleteTime(LocalDateTime.now());
        questionRepository.saveAndFlush(question);
    }

    @Override
    public PagedResource<QuestionBo> getQuestions(Integer active, Integer page, Integer size) {
        Sort seq = Sort.by("updateTime");
        Pageable pageable = PageRequest.of(page - 1, size, seq);
        Specification<Question> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (active != null) {
                predicates.add(criteriaBuilder.equal(root.get("active"), active));
            }
            predicates.add(criteriaBuilder.isNull(root.get("deleter")));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<Question> articlePage = questionRepository.findAll(specification, pageable);
        List<QuestionBo> collect = articlePage.stream().map(this::toQuestionBo).collect(Collectors.toList());
        return new PagedResource<>(collect, page, size, articlePage.getTotalElements());
    }

    @Override
    public void updateQuestionStatus(Integer id, Integer active) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new CustomException("问题不存在"));
        question.setActive(active);
        question.setUpdater(webContext.getUserId());
        question.setUpdateTime(LocalDateTime.now());
        questionRepository.saveAndFlush(question);
    }

    private QuestionBo toQuestionBo(Question question){
        QuestionBo questionBo = new QuestionBo();
        questionBo.setId(question.getId());
        questionBo.setQuestion(question.getQuestion());
        questionBo.setAnswer(question.getAnswer());
        questionBo.setActive(question.getActive());
        SysUser creatorUser = question.getCreatorUser();
        if(creatorUser!=null){
            questionBo.setCreatorId(creatorUser.getId());
            questionBo.setCreatorName(creatorUser.getUserName());
        }
        questionBo.setCreateTime(question.getCreateTime());
        SysUser updateUser = question.getUpdaterUser();
        if(updateUser!=null){
            questionBo.setUpdaterId(updateUser.getId());
            questionBo.setUpdaterName(updateUser.getUserName());
        }
        questionBo.setUpdateTime(question.getUpdateTime());
        return questionBo;
    }

}

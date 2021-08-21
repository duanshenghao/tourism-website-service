package com.eastbabel.dao.repository;

import com.eastbabel.dao.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Integer>, JpaSpecificationExecutor<Question> {

    List<Question> findByDeleterIsNullAndActive(Integer active);

    Optional<Question> findByIdAndDeleterIsNull(Integer sysId);
}

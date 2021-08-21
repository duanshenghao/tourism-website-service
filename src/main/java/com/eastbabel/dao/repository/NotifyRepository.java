package com.eastbabel.dao.repository;

import com.eastbabel.dao.entity.Notify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface NotifyRepository extends JpaRepository<Notify, Integer>, JpaSpecificationExecutor<Notify> {

}

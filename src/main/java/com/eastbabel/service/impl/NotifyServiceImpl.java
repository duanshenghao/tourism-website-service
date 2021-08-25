package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.email.ToEmail;
import com.eastbabel.bo.notify.CreateNotifyReq;
import com.eastbabel.bo.notify.NotifyBo;
import com.eastbabel.dao.entity.Notify;
import com.eastbabel.dao.entity.SysUser;
import com.eastbabel.dao.repository.NotifyRepository;
import com.eastbabel.dao.repository.UserRepository;
import com.eastbabel.exception.CustomException;
import com.eastbabel.service.EmailService;
import com.eastbabel.service.NotifyService;
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
public class NotifyServiceImpl implements NotifyService {

    @Resource
    private NotifyRepository notifyRepository;
    @Autowired
    private WebContext webContext;
    @Autowired
    private EmailService emailService;
    @Resource
    private UserRepository userRepository;


    @Override
    public void createNotify(CreateNotifyReq createNotifyReq) {
        Notify notify = new Notify();
        notify.setName(createNotifyReq.getName());
        notify.setCompany(createNotifyReq.getCompany());
        notify.setContent(createNotifyReq.getContent());
        notify.setPhone(createNotifyReq.getPhone());
        notify.setEmail(createNotifyReq.getEmail());
        notify.setStatus(0);
        LocalDateTime now = LocalDateTime.now();
//        notify.setCreator(webContext.getUserId());
        notify.setCreateTime(now);
//        notify.setUpdater(webContext.getUserId());
        notify.setUpdateTime(now);
        notifyRepository.save(notify);
        ToEmail toEmail = new ToEmail();
        toEmail.setSubject("客户垂询提醒");
        toEmail.setContent("客户姓名："+createNotifyReq.getName()+" 客户电话："+createNotifyReq.getPhone() + " 客户邮箱："+createNotifyReq.getEmail() +" 公司名称：" + createNotifyReq.getCompany() + " 客户需求："+createNotifyReq.getContent());
        List<SysUser> list = userRepository.findAll();
        String[] tos = new String[list.size()];
        for(int i=0;i<list.size();i++){
            tos[i] = list.get(i).getEmail();
        }
        toEmail.setTos(tos);
        try {
            emailService.sendEmail(toEmail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteNotify(Integer id) {
        Notify notify = notifyRepository.findById(id).orElseThrow(() -> new CustomException("垂询不存在"));
        notify.setDeleter(webContext.getUserId());
        notify.setDeleteTime(LocalDateTime.now());
        notifyRepository.save(notify);
    }

    @Override
    public void updateNotifyStatus(Integer id, Integer status) {
        Notify notify = notifyRepository.findById(id).orElseThrow(() -> new CustomException("垂询不存在"));
        notify.setStatus(status);
        notify.setUpdater(webContext.getUserId());
        notify.setUpdateTime(LocalDateTime.now());
        notifyRepository.save(notify);
    }

    @Override
    public PagedResource<NotifyBo> getNotifys(Integer status, Integer page, Integer size) {
        Sort seq = Sort.by("updateTime");
        Pageable pageable = PageRequest.of(page - 1, size, seq);
        Specification<Notify> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));
            }
            predicates.add(criteriaBuilder.isNull(root.get("deleter")));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<Notify> articlePage = notifyRepository.findAll(specification, pageable);
        List<NotifyBo> collect = articlePage.stream().map(this::toNotifyBo).collect(Collectors.toList());
        return new PagedResource<>(collect, page, size, articlePage.getTotalElements());
    }

    @Override
    public void editNotify(NotifyBo notifyBo) {
        Notify notify = notifyRepository.findById(notifyBo.getId()).orElseThrow(() -> new CustomException(""));
        notify.setRemark(notifyBo.getRemark());
        notify.setStatus(notifyBo.getStatus());
        notify.setUpdater(webContext.getUserId());
        notify.setUpdateTime(LocalDateTime.now());
        notifyRepository.saveAndFlush(notify);
    }

    private NotifyBo toNotifyBo(Notify notify){
        NotifyBo notifyBo = new NotifyBo();
        notifyBo.setId(notify.getId());
        notifyBo.setName(notify.getName());
        notifyBo.setCompany(notify.getCompany());
        notifyBo.setPhone(notify.getPhone());
        notifyBo.setContent(notify.getContent());
        notifyBo.setEmail(notify.getEmail());
        notifyBo.setStatus(notify.getStatus());
        notifyBo.setRemark(notify.getRemark());
        SysUser creatorUser = notify.getCreatorUser();
        if(creatorUser!=null){
            notifyBo.setCreatorId(creatorUser.getId());
            notifyBo.setCreatorName(creatorUser.getUserName());
        }
        notifyBo.setCreateTime(notify.getCreateTime());
        SysUser updateUser = notify.getUpdaterUser();
        if(updateUser!=null){
            notifyBo.setUpdaterId(updateUser.getId());
            notifyBo.setUpdaterName(updateUser.getUserName());
        }
        notifyBo.setUpdateTime(notify.getUpdateTime());
        return notifyBo;
    }

}

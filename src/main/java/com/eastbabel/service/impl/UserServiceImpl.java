package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.bo.user.CreateUserReq;
import com.eastbabel.bo.user.SysUserBo;
import com.eastbabel.dao.entity.Question;
import com.eastbabel.dao.entity.SysUser;
import com.eastbabel.dao.repository.UserRepository;
import com.eastbabel.exception.CustomException;
import com.eastbabel.service.UserService;
import com.eastbabel.utils.PasswordUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private WebContext webContext;

    @Override
    public List<SysUserBo> getUser() {
        return userRepository.findAll().stream().map(sysUser -> {
            SysUserBo sysUserBo = new SysUserBo();
            sysUserBo.setId(sysUser.getId());
            sysUserBo.setUsername(sysUser.getUserName());
            sysUserBo.setEmail(sysUser.getEmail());
            return sysUserBo;
        }).collect(Collectors.toList());
    }

    @Override
    public Integer createUser(CreateUserReq createUserReq) {
        SysUser user = new SysUser();
        user.setUserName(createUserReq.getUsername());
        String salt = PasswordUtil.genSalt();
        user.setPassword(PasswordUtil.encryption(createUserReq.getPassword(), salt));
        user.setSalt(salt);
        user.setEmail(createUserReq.getEmail());
        user.setActiveStatus(0);
        user.setCreator(webContext.getUserId());
        user.setCreateTime(LocalDateTime.now());
        user.setUpdater(webContext.getUserId());
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
        return user.getId();
    }


    @Override
    public void deleteUser(Integer id) {
        userRepository.findById(id).orElseThrow(()->new CustomException("用户不存在"));
        userRepository.deleteById(id);
    }

    @Override
    public void updateAdminPassword(Integer id, String password) {
        SysUser user = userRepository.findById(id).orElseThrow(()->new CustomException("用户不存在"));
        new CustomException("密码在6到20位之间").throwIf(StringUtils.isBlank(password) || password.length() > 20 || password.length() < 6);
        String salt = PasswordUtil.genSalt();
        user.setPassword(PasswordUtil.encryption(password, salt));
        user.setSalt(salt);
        user.setUpdater(webContext.getUserId());
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public PagedResource<SysUserBo> getUsers(Integer activeStatus, Integer page, Integer size) {
        Sort seq = Sort.by("updateTime");
        Pageable pageable = PageRequest.of(page - 1, size, seq);
        Specification<SysUser> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (activeStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("activeStatus"), activeStatus));
            }
//            predicates.add(criteriaBuilder.isNull(root.get("deleter")));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<SysUser> articlePage = userRepository.findAll(specification, pageable);
        List<SysUserBo> collect = articlePage.stream().map(this::toSysUserBo).collect(Collectors.toList());
        return new PagedResource<>(collect, page, size, articlePage.getTotalElements());
    }

    @Override
    public void editUser(SysUserBo sysUserBo) {
        SysUser user = userRepository.findById(sysUserBo.getId()).orElseThrow(() -> new CustomException("用户不存在"));
        user.setUserName(sysUserBo.getUsername());
        user.setEmail(sysUserBo.getEmail());
        user.setUpdater(webContext.getUserId());
        user.setUpdateTime(LocalDateTime.now());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateUserStatus(Integer id, Integer activeStatus) {
        SysUser user = userRepository.findById(id).orElseThrow(() -> new CustomException("用户不存在"));
        user.setActiveStatus(activeStatus);
        user.setUpdater(webContext.getUserId());
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    private SysUserBo toSysUserBo(SysUser sysUser){
        SysUserBo sysUserBo = new SysUserBo();
        sysUserBo.setId(sysUser.getId());
        sysUserBo.setUsername(sysUser.getUserName());
        sysUserBo.setEmail(sysUser.getEmail());
        if(sysUser.getCreator()!=null && sysUser.getCreator()!=0){
            SysUser creatorUser = userRepository.findById(sysUser.getCreator()).orElseThrow(() -> new CustomException("该用户不存在"));
            sysUserBo.setCreator(creatorUser);
        }
        sysUserBo.setCreateTime(sysUser.getCreateTime());
        if(sysUser.getUpdater()!=null && sysUser.getUpdater()!=0){
            SysUser updaterUser = userRepository.findById(sysUser.getUpdater()).orElseThrow(() -> new CustomException("该用户不存在"));
            sysUserBo.setUpdater(updaterUser);
        }
        sysUserBo.setUpdateTime(sysUser.getUpdateTime());
        return sysUserBo;
    }


}

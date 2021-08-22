package com.eastbabel.service.impl;

import com.eastbabel.aop.WebContext;
import com.eastbabel.bo.RestUserEntity;
import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.login.UpdPasswdEntity;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.bo.user.CreateUserReq;
import com.eastbabel.bo.user.EditUser;
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
        String password = "000000";
        user.setPassword(PasswordUtil.encryption(password, salt));
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
        SysUser user = userRepository.findById(id).orElseThrow(() -> new CustomException("用户不存在"));
        user.setDeleter(webContext.getUserId());
        user.setDeleteTime(LocalDateTime.now());
        userRepository.saveAndFlush(user);
    }

    @Override
    public void updateAdminPassword(Integer id, String password) {
        SysUser user = userRepository.findById(id).orElseThrow(()->new CustomException("用户不存在"));
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
            predicates.add(criteriaBuilder.isNull(root.get("deleter")));
            return criteriaBuilder.and(predicates.toArray(predicates.toArray(new Predicate[0])));
        };
        Page<SysUser> articlePage = userRepository.findAll(specification, pageable);
        List<SysUserBo> collect = articlePage.stream().map(this::toSysUserBo).collect(Collectors.toList());
        return new PagedResource<>(collect, page, size, articlePage.getTotalElements());
    }

    @Override
    public void editUser(EditUser editUser) {
        SysUser user = userRepository.findById(editUser.getId()).orElseThrow(() -> new CustomException("用户不存在"));
        user.setUserName(editUser.getUsername());
        user.setEmail(editUser.getEmail());
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

    @Override
    public void updPassword(UpdPasswdEntity updPasswdEntity) {
        if(StringUtils.isEmpty(updPasswdEntity.getUsername())){
            new CustomException("用户名不能为空");
        }
        if(StringUtils.isEmpty(updPasswdEntity.getOldPassword())){
            new CustomException("旧密码不能为空");
        }
        if(StringUtils.isEmpty(updPasswdEntity.getNewPassword())){
            new CustomException("新密码不能为空");
        }
        SysUser user = userRepository.findByUserName(updPasswdEntity.getUsername());
        new CustomException("账号或密码错误").throwIf(user == null);
        String securityPwd = PasswordUtil.encryption(updPasswdEntity.getOldPassword(), user.getSalt());
        new CustomException("账号或密码错误").throwIf(!securityPwd.equals(user.getPassword()));
        String salt = PasswordUtil.genSalt();
        user.setPassword(PasswordUtil.encryption(updPasswdEntity.getNewPassword(), salt));
        user.setSalt(salt);
        user.setUpdater(webContext.getUserId());
        user.setUpdateTime(LocalDateTime.now());
        userRepository.save(user);
    }

    private SysUserBo toSysUserBo(SysUser sysUser){
        SysUserBo sysUserBo = new SysUserBo();
        sysUserBo.setId(sysUser.getId());
        sysUserBo.setUsername(sysUser.getUserName());
        sysUserBo.setEmail(sysUser.getEmail());
        sysUserBo.setActiveStatus(sysUser.getActiveStatus());
        if(sysUser.getCreator()!=null && sysUser.getCreator()!=0){
            SysUser creatorUser = userRepository.findById(sysUser.getCreator()).orElseThrow(() -> new CustomException("该用户不存在"));
            RestUserEntity userEntity = new RestUserEntity();
            userEntity.setId(creatorUser.getId());
            userEntity.setUsername(creatorUser.getUserName());
            sysUserBo.setCreatorUser(userEntity);
        }
        sysUserBo.setCreateTime(sysUser.getCreateTime());
        if(sysUser.getUpdater()!=null && sysUser.getUpdater()!=0){
            SysUser updaterUser = userRepository.findById(sysUser.getUpdater()).orElseThrow(() -> new CustomException("该用户不存在"));
            RestUserEntity userEntity = new RestUserEntity();
            userEntity.setId(updaterUser.getId());
            userEntity.setUsername(updaterUser.getUserName());
            sysUserBo.setUpdaterUser(userEntity);
        }
        sysUserBo.setUpdateTime(sysUser.getUpdateTime());
        return sysUserBo;
    }


}

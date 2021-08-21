package com.eastbabel.service;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.bo.user.CreateUserReq;
import com.eastbabel.bo.user.SysUserBo;

import java.util.List;

public interface UserService {

    List<SysUserBo> getUser();

    Integer createUser(CreateUserReq createUserReq);


    void deleteUser(Integer id);

    void updateAdminPassword(Integer adminId, String password);

    PagedResource<SysUserBo> getUsers(Integer activeStatus, Integer page, Integer size);

    void editUser(SysUserBo sysUserBo);

    void updateUserStatus(Integer id, Integer activeStatus);
}

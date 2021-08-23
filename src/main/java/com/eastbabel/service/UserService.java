package com.eastbabel.service;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.login.UpdPasswdEntity;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.bo.user.CreateUserReq;
import com.eastbabel.bo.user.EditUser;
import com.eastbabel.bo.user.SysUserBo;
import com.eastbabel.dao.entity.SysUser;

import java.util.List;

public interface UserService {

    List<SysUserBo> getUser();

    Integer createUser(CreateUserReq createUserReq);


    void deleteUser(Integer id);

    void updateAdminPassword(Integer adminId, String password);

    PagedResource<SysUserBo> getUsers(Integer activeStatus, Integer page, Integer size);

    void editUser(EditUser editUser);

    void updateUserStatus(Integer id, Integer activeStatus);

    void updPassword(UpdPasswdEntity updPasswdEntity);
}

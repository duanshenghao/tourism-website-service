package com.eastbabel.controller;

import com.eastbabel.bo.base.PagedResource;
import com.eastbabel.bo.base.ResponseEntity;
import com.eastbabel.bo.login.LoginReq;
import com.eastbabel.bo.login.TokenBo;
import com.eastbabel.bo.login.UpdPasswdEntity;
import com.eastbabel.bo.question.CreateQuestionReq;
import com.eastbabel.bo.question.QuestionBo;
import com.eastbabel.bo.user.CreateUserReq;
import com.eastbabel.bo.user.EditUser;
import com.eastbabel.bo.user.SysUserBo;
import com.eastbabel.dao.entity.SysUser;
import com.eastbabel.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@Api(tags = "用户接口")
public class UserController {

    @Resource
    private UserService userService;


    @PutMapping("user")
    @ApiOperation("新增用户")
    public ResponseEntity createUser(@Validated @RequestBody CreateUserReq createUserReq){
        return ResponseEntity.ok(userService.createUser(createUserReq));
    }


    @DeleteMapping("user/{id}")
    @ApiOperation("删除用户")
    public ResponseEntity<String> deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("success");
    }

    @PutMapping("user/{id}/password")
    @ApiOperation("重置用户密码(账号管理员用)")
    public ResponseEntity<String> updateAdminPassword(@PathVariable("id") Integer id) {
        String password = "000000";
        userService.updateAdminPassword(id, password);
        return ResponseEntity.ok(password);
    }

    @GetMapping("user/list/page")
    @ApiOperation("获取用户列表(分页)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "activeStatus", value = "激活状态"),
            @ApiImplicitParam(name = "page", value = "页数"),
            @ApiImplicitParam(name = "size", value = "数据条数")
    })
    public ResponseEntity<PagedResource<SysUserBo>> getSysUsers(
            @RequestParam(value = "activeStatus", required = false) Integer activeStatus,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return ResponseEntity.ok(userService.getUsers(activeStatus, page, size));
    }

    @PostMapping("user")
    @ApiOperation("编辑用户信息")
    public ResponseEntity<String> editUser(@Validated @RequestBody EditUser editUser) {
        userService.editUser(editUser);
        return ResponseEntity.ok("success");
    }

    @PutMapping("user/{id}/status/{activeStatus}")
    @ApiOperation("修改用户状态")
    public ResponseEntity<String> updateNotifyStatus(@PathVariable("id") Integer id,
                                                     @PathVariable("activeStatus") Integer activeStatus) {
        userService.updateUserStatus(id, activeStatus);
        return ResponseEntity.succeed();
    }

    @PostMapping("updPassword")
    @ApiOperation("密码修改")
    public ResponseEntity<String> updPassword(@RequestBody @Valid UpdPasswdEntity updPasswdEntity) {
        userService.updPassword(updPasswdEntity);
        return ResponseEntity.succeed();
    }

}

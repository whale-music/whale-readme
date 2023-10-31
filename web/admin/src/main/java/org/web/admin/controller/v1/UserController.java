package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.PageUserReq;
import org.api.admin.model.req.SaveOrUpdateUserReq;
import org.api.admin.service.UserApi;
import org.common.result.R;
import org.core.common.annotation.AnonymousAccess;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "UserController")
@RequestMapping("/admin/user")
@Slf4j
public class UserController {
    
    @Autowired
    private UserApi userApi;
    
    @GetMapping("/{id}")
    public R getUserInfo(@PathVariable("id") Long id) {
        return R.success(userApi.getUserInfo(id));
    }
    
    @AnonymousAccess
    @GetMapping("/getExampleRoles")
    public R getExampleRoles() {
        return R.success(userApi.getExampleRoles());
    }
    
    @PostMapping("/page")
    public R getUserPage(@RequestBody PageUserReq user) {
        return R.success(userApi.getUserPage(user));
    }
    
    @PostMapping("/")
    public R saveOrUpdateUser(@RequestBody SaveOrUpdateUserReq saveOrUpdateUserReq) {
        return R.success(userApi.saveOrUpdateUser(saveOrUpdateUserReq));
    }
    
    @DeleteMapping("/{id}")
    public R deleteUser(@PathVariable("id") Long id) {
        userApi.deleteUser(id);
        return R.success();
    }
    
    @PostMapping("/update/account")
    public R updateUserPassword(Long id, String username, String nickname, String password) {
        userApi.updateUserPassword(id, username, nickname, password);
        return R.success();
    }
}

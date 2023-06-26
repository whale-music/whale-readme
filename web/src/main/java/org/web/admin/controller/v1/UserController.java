package org.web.admin.controller.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.service.UserApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

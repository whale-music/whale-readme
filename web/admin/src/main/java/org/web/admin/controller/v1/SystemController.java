package org.web.admin.controller.v1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.SystemLogRes;
import org.api.admin.model.res.SystemLogReq;
import org.api.admin.service.SystemApi;
import org.core.common.result.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController(AdminConfig.ADMIN + "SystemController")
@RequestMapping("/admin/system")
@Slf4j
@RequiredArgsConstructor
public class SystemController {
    
    private SystemApi systemApi;
    
    @Autowired
    public SystemController(SystemApi systemApi) {
        this.systemApi = systemApi;
    }
    
    // @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/page/log")
    public R getSystemLogPage(@RequestBody SystemLogReq req) {
        PageResCommon<SystemLogRes> res = systemApi.getSystemLogPage(req);
        return R.success(res);
    }
    
    @GetMapping("/remove/log")
    public R removeSystemLog(@RequestParam(value = "day", required = false) Integer day) {
        systemApi.removeSystemLog(day);
        return R.success();
    }
}

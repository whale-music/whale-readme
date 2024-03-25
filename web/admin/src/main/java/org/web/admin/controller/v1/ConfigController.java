package org.web.admin.controller.v1;

import cn.hutool.core.bean.BeanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.properties.DebugConfig;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@AllArgsConstructor
@RestController(AdminConfig.ADMIN + "ConfigController")
@RequestMapping("/admin/config")
@Slf4j
public class ConfigController {
    
    private final DebugConfig debugConfig;
    
    @AnonymousAccess
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/")
    public R getConfig() {
        Map<String, Object> map = BeanUtil.beanToMap(debugConfig,
                "enablePlugin",
                "logEnable",
                "enableAdminSpringBootApplication",
                "enableNeteaseCloudMusicSpringBootApplication",
                "enableSubsonicSpringBootApplication");
        return R.success(map);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/enable/plugin")
    public R getEnablePlugin() {
        return R.success(debugConfig.getEnablePlugin());
    }
}

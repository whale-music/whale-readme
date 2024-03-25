package org.web.admin.controller.v1;


import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.res.ActivityDeviceRes;
import org.api.admin.service.DevicesApi;
import org.core.common.result.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(AdminConfig.ADMIN + "DevicesController")
@RequestMapping("/admin/devices")
@Slf4j
public class DevicesController {
    
    private final DevicesApi devicesApi;
    
    public DevicesController(DevicesApi devicesApi) {
        this.devicesApi = devicesApi;
    }
    
    @GetMapping("/server/activity")
    public R getActivityDevice() {
        ActivityDeviceRes res = devicesApi.getActivityDevice();
        return R.success(res);
    }
    
    @GetMapping("/remove/{id}")
    public R removeActivityDevice(@PathVariable("id") String id) {
        devicesApi.removeActivityDevice(id);
        return R.success();
    }
}

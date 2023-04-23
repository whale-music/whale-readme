package org.web.controller.subsonic.v1;

import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.common.SubsonicResult;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.LicenseRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(SubsonicConfig.SUBSONIC + "AlbumController")
@RequestMapping("/rest")
@Slf4j
public class SystemController {
    
    @GetMapping(value = "/ping.view")
    @ManualSerialize
    public Object ping(SubsonicCommonReq req) {
        return new SubsonicResult().success();
    }
    
    @GetMapping(value = "/getLicense")
    @ManualSerialize
    public Object license(SubsonicCommonReq req) {
        LicenseRes licenseRes = new LicenseRes();
        licenseRes.setLicense(new LicenseRes.License(true));
        return licenseRes.success();
    }
}

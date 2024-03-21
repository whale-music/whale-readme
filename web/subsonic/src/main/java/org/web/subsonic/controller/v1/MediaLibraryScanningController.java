package org.web.subsonic.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.scanstatus.ScanStatusRes;
import org.api.subsonic.service.MediaLibraryScanningApi;
import org.core.model.HttpStatusStr;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Tag(name = "媒体检索")
@CrossOrigin(origins = "*")
@RequestMapping("/rest")
@RestController(SubsonicConfig.SUBSONIC + "MediaLibraryScanningController")
public class MediaLibraryScanningController {
    private final MediaLibraryScanningApi mediaLibraryScanningApi;
    
    public MediaLibraryScanningController(MediaLibraryScanningApi mediaLibraryScanningApi) {
        this.mediaLibraryScanningApi = mediaLibraryScanningApi;
    }
    
    @Operation(summary = "返回媒体库扫描的当前状态。不需要额外的参数")
    @ApiResponse(responseCode = HttpStatusStr.OK, content = @Content)
    @GetMapping({"/getScanStatus.view", "/getScanStatus"})
    @ManualSerialize
    public ResponseEntity<String> getScanStatus(SubsonicCommonReq req) {
        ScanStatusRes res = mediaLibraryScanningApi.getScanStatus(req);
        return res.success(req);
    }
}

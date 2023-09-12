package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.common.SubsonicResult;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.license.LicenseRes;
import org.core.model.HttpStatusStr;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统")
@RestController(SubsonicConfig.SUBSONIC + "AlbumController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class SystemRestController {
    
    @GetMapping(value = {"/ping.view", "/ping"})
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @ManualSerialize
    public ResponseEntity<SubsonicResult> ping(SubsonicCommonReq req) {
        return new SubsonicResult().success();
    }
    
    @GetMapping(value = {"/getLicense.view", "/getLicense"})
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @ManualSerialize
    public ResponseEntity<SubsonicResult> license(SubsonicCommonReq req) {
        LicenseRes licenseRes = new LicenseRes();
        licenseRes.setLicense(new LicenseRes.License(true));
        return licenseRes.success();
    }
}

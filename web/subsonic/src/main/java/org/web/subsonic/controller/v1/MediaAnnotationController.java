package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.aspect.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.star.StarRes;
import org.api.subsonic.model.res.unstar.UnStarRes;
import org.api.subsonic.service.MediaAnnotationApi;
import org.core.common.constant.HttpStatusStrConstant;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Tag(name = "媒体收藏")
@RequestMapping("/rest")
@RestController(SubsonicConfig.SUBSONIC + "MediaAnnotationController")
public class MediaAnnotationController {
    
    private final MediaAnnotationApi mediaAnnotationApi;
    
    public MediaAnnotationController(MediaAnnotationApi mediaAnnotationApi) {
        this.mediaAnnotationApi = mediaAnnotationApi;
    }
    
    @Operation(summary = "为歌曲、专辑或艺术家添加收藏")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/star.view", "/star"})
    @ManualSerialize
    public ResponseEntity<String> star(SubsonicCommonReq req,
                                       @Parameter(description = "要加注星标的文件（歌曲）或文件夹（专辑/艺术家）的 ID。允许多个参数。")
                                       @RequestParam(value = "id", required = false) final List<Long> id,
                                       
                                       @Parameter(description = "要加星的专辑的 ID。如果客户端根据 ID3 标签而不是文件结构访问媒体集合，请使用此选项而不是 id 。允许多个参数")
                                       @RequestParam(value = "albumId", required = false) final List<Long> albumId,
                                       
                                       @Parameter(description = "要主演的艺术家的 ID。如果客户端根据 ID3 标签而不是文件结构访问媒体集合，请使用此选项而不是 id 。允许多个参数。")
                                       @RequestParam(value = "artistId", defaultValue = "true", required = false) final List<Long> artistId
    ) {
        StarRes res = mediaAnnotationApi.star(req, id, albumId, artistId);
        return res.success(req);
    }
    
    @Operation(summary = "为歌曲、专辑或艺术家添加收藏")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/unstar.view", "/unstar"})
    @ManualSerialize
    public ResponseEntity<String> unstar(SubsonicCommonReq req,
                                       @Parameter(description = "要取消加星标的文件（歌曲）或文件夹（专辑/艺术家）的 ID。允许多个参数。")
                                       @RequestParam(value = "id", required = false) final List<Long> id,
                                       
                                       @Parameter(description = "要取消加注星标的相册的 ID。如果客户端根据 ID3 标签而不是文件结构访问媒体集合，请使用此选项而不是 id 。允许多个参数")
                                       @RequestParam(value = "albumId", required = false) final List<Long> albumId,
                                       
                                       @Parameter(description = "要取消加星标的艺术家的 ID。如果客户端根据 ID3 标签而不是文件结构访问媒体集合，请使用此选项而不是 id 。允许多个参数。")
                                       @RequestParam(value = "artistId", defaultValue = "true", required = false) final List<Long> artistId
    ) {
        UnStarRes res = mediaAnnotationApi.unStar(req, id, albumId, artistId);
        return res.success(req);
    }
}

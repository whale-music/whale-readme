package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.aspect.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.common.SubsonicResult;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.setrating.SetratingRes;
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
    
    
    @Operation(summary = "注册一个或多个媒体文件的本地回放。通常在播放缓存在客户端上的媒体时使用。此操作包括以下内容：",
               description = "如果用户已在Subsonic服务器上配置了他/她的last.fm凭证（Settings > Personal），则会“滚动”last.fm上的媒体文件。" +
                       "更新媒体文件的播放次数和上次播放时间戳。（自1.11.0版起）" +
                       "使媒体文件显示在Web应用程序的“正在播放”页面中，并显示在 getNowPlaying （自1.11.0起）返回的歌曲列表中"
    )
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/scrobble.view", "/scrobble"})
    @ManualSerialize
    public ResponseEntity<String> scrobble(SubsonicCommonReq req,
                                           @Parameter(description = "要标记为已播放的媒体文件的ID。")
                                           @RequestParam("id") Long id,
                                           
                                           @Parameter(description = "（自1.8.0起）歌曲被收听的时间（自1970年1月1日起，以毫秒为单位）", deprecated = true)
                                           @RequestParam(value = "time", required = false) Long timeStamp,
                                           
                                           @Parameter(description = "无论这是“提交”还是“正在播放”通知", deprecated = true)
                                           @RequestParam(value = "submission", defaultValue = "true", required = false) Boolean submission) {
        mediaAnnotationApi.scrobble(req, id, timeStamp, submission);
        return new SubsonicResult().success(req);
    }
    
    @Operation(summary = "设置评级", description = "该接口可以访问，但是不会进行任何操作", deprecated = true)
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/setrating.view", "/setrating"})
    @ManualSerialize
    public ResponseEntity<String> setrating(SubsonicCommonReq req,
                                            @Parameter(description = "唯一标识要评分的文件（歌曲）或文件夹（专辑/艺术家）的字符串")
                                            @RequestParam(value = "id", required = false) final Long id,
                                            
                                            @Parameter(description = "要取消加注星标的相册的 ID。如果客户端根据 ID3 标签而不是文件结构访问媒体集合，请使用此选项而不是 id 。允许多个参数")
                                            @RequestParam(value = "rating", required = false) final Integer rating
    ) {
        SetratingRes res = mediaAnnotationApi.setrating(req, id, rating);
        return res.success(req);
    }
}

package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.service.MediaRetrievalApi;
import org.core.common.constant.HttpStatusStrConstant;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "媒体检索")
@RestController(SubsonicConfig.SUBSONIC + "MediaRetrievalController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class MediaRetrievalController {
    
    @Autowired
    private MediaRetrievalApi mediaRetrievalApi;
    
    @Operation(summary = "返回封面艺术图像")
    @ApiResponse(responseCode = HttpStatusStrConstant.MOVED_TEMP, content = @Content)
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getCoverArt.view", "/getCoverArt"})
    public RedirectView getCoverArt(SubsonicCommonReq req,
                                    @Parameter(description = "歌曲、专辑或艺术家的ID")
                                    @RequestParam(value = "id") String id,
                                    
                                    @Parameter(description = "如果指定，将图像缩放到此大小", deprecated = true)
                                    @RequestParam(value = "size", required = false) Long size
    ) {
        String picUrl = mediaRetrievalApi.getCoverArt(req, id, size);
        return new RedirectView(picUrl);
    }
    
    @Operation(summary = "流式传输给定的媒体文件, 实际是返回302重定向地址")
    @ApiResponse(responseCode = HttpStatusStrConstant.MOVED_TEMP, content = @Content)
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/stream.view", "/stream"})
    public RedirectView stream(SubsonicCommonReq req,
                               @Parameter(description = "唯一标识要流传输的文件的字符串。通过调用getMusicDirectory获得")
                               @RequestParam(value = "id") String id,
                               
                               @Parameter(description = "（从1.2.0开始）如果指定，服务器将尝试将比特率限制在此值，以千比特每秒为单位。如果设置为零，则不施加限制。", deprecated = true)
                               @RequestParam(value = "maxBitRate", required = false) String maxBitRate,
                               
                               @Parameter(description = "（从1.6.0开始）指定首选的目标格式（例如，“mp3”或“flv”），以在存在多个适用的转码的情况下。从1.9.0开始，您可以使用特殊值“raw”来禁用转码。", deprecated = true)
                               @RequestParam(value = "format", required = false) String format,
                               
                               @Parameter(description = "仅适用于视频流。如果指定，则以给定偏移量（秒）开始流式传输到视频中。通常用于实现视频跳过。", deprecated = true)
                               @RequestParam(value = "timeOffset", required = false) String timeOffset,
                               
                               @Parameter(description = "（从1.6.0开始）仅适用于视频流。请求的视频大小指定为WxH，例如“640x480”。", deprecated = true)
                               @RequestParam(value = "size", required = false) String size,
                               
                               @Parameter(description = "（从1.8.0开始）。如果设置为“true”，则内容长度HTTP报头将被设置为转码或下采样媒体的估计值。", deprecated = true)
                               @RequestParam(value = "estimateContentLength", required = false) String estimateContentLength,
                               
                               @Parameter(description = "（自1.14.0起）仅适用于视频流。Subsonic可以通过将视频转换为MP4来优化流媒体。如果所讨论的视频存在转换，则将此参数设置为“true”将导致返回转换后的视频而不是原始视频。", deprecated = true)
                                   @RequestParam(value = "converted", required = false) String converted
    ) {
        String musicUrl = mediaRetrievalApi.stream(req, Long.valueOf(id), maxBitRate, format, timeOffset, size, estimateContentLength, converted);
        return new RedirectView(musicUrl);
    }
}

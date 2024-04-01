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
import org.api.subsonic.model.res.createplaylist.CreatePlaylistRes;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
import org.api.subsonic.service.PlaylistApi;
import org.core.common.constant.HttpStatusStrConstant;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "播放列表")
@RestController(SubsonicConfig.SUBSONIC + "PlaylistController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class PlaylistController {
    
    private final PlaylistApi playlistApi;
    
    public PlaylistController(PlaylistApi playlistApi) {
        this.playlistApi = playlistApi;
    }
    
    @Operation(summary = "返回允许用户播放的所有播放列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getPlaylists.view", "/getPlaylists"})
    @ManualSerialize
    public ResponseEntity<String> getPlaylists(SubsonicCommonReq req,
                                               @Parameter(description = "（从1.8.0开始）如果指定，则返回此用户的播放列表，而不是已验证用户的播放列表。如果使用此参数，已验证的用户必须具有管理员角色")
                                               String username) {
        PlaylistsRes playlists = playlistApi.getPlaylists(req, username);
        return playlists.success(req);
    }
    
    @Operation(summary = "返回已保存播放列表中的文件列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getPlaylist.view", "/getPlaylist"})
    @ManualSerialize
    public ResponseEntity<String> getPlaylist(SubsonicCommonReq req,
                                              @Parameter(description = "返回的播放列表ID，由`getPlaylists`获取。")
                                              @RequestParam("id") Long id) {
        PlaylistRes playlists = playlistApi.getPlaylist(id);
        return playlists.success(req);
    }
    
    @Operation(summary = "创建（或更新）播放列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/createPlaylist.view", "/createPlaylist"})
    @ManualSerialize
    public ResponseEntity<String> createPlaylist(SubsonicCommonReq req,
                                                 @Parameter(description = "播放列表ID(如果更新则此字段必须)")
                                                 @RequestParam(value = "playlistId", required = false) Long playlistId,
                                                 
                                                 @Parameter(description = "播放列表的人类可读名称(如果创建这此字段必须)")
                                                     @RequestParam(value = "name", required = false) String name,
                                                 
                                                 @Parameter(description = "播放列表中歌曲的ID。为播放列表中的每首歌曲使用一个`songId`参数。")
                                                     @RequestParam(value = "songId", required = false) Long songId
    ) {
        CreatePlaylistRes playlists = playlistApi.createPlaylist(req, playlistId, name, songId);
        return playlists.success(req);
    }
    
    @Operation(summary = "创建（或更新）播放列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/updatePlaylist.view", "/updatePlaylist"})
    @ManualSerialize
    public ResponseEntity<String> updatePlaylist(SubsonicCommonReq req,
                                                 @Parameter(description = "播放列表ID")
                                                 @RequestParam(value = "playlistId") Long playlistId,
                                                 
                                                 @Parameter(description = "歌单名")
                                                     @RequestParam(value = "name", required = false) String name,
                                                 
                                                 @Parameter(description = "播放列表评论")
                                                     @RequestParam(value = "comment", required = false) String comment,
                                                 
                                                 @Parameter(description = "true 如果播放列表对所有用户都是可见的，则 false 不可见(该功能未实现)", deprecated = true)
                                                     @RequestParam(value = "public", required = false) Boolean publicFlag,
                                                 
                                                 @Parameter(description = "将具有此ID的歌曲添加到播放列表中。允许多个参数")
                                                     @RequestParam(value = "songIdToAdd", required = false) List<Long> songIdToAdd,
                                                 
                                                 @Parameter(description = "删除播放列表中此位置的歌曲。允许多个参数")
                                                     @RequestParam(value = "songIndexToRemove", required = false) List<Long> songIndexToRemove
    ) {
        playlistApi.updatePlaylist(req, playlistId, name, comment, publicFlag, songIdToAdd, songIndexToRemove);
        return new SubsonicResult().success(req);
    }
    
    @Operation(summary = "删除保存的播放列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/deletePlaylist.view", "/deletePlaylist"})
    @ManualSerialize
    public ResponseEntity<String> deletePlaylist(SubsonicCommonReq req,
                                                 @Parameter(description = "要删除的播放列表ID，由`/getPlaylists`获取。")
                                                 @RequestParam(value = "id") Long id
    ) {
        playlistApi.deletePlaylist(req, id);
        return new SubsonicResult().success(req);
    }
}

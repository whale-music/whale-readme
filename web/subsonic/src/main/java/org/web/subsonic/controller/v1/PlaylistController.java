package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.common.SubsonicResult;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.createplaylist.CreatePlaylistRes;
import org.api.subsonic.model.res.playlist.PlaylistRes;
import org.api.subsonic.model.res.playlists.PlaylistsRes;
import org.api.subsonic.service.PlaylistApi;
import org.core.model.HttpStatusStr;
import org.springframework.beans.factory.annotation.Autowired;
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
    
    @Autowired
    private PlaylistApi playlistApi;
    
    @Operation(summary = "返回允许用户播放的所有播放列表")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @GetMapping({"/getPlaylists.view", "/getPlaylists"})
    @ManualSerialize
    public ResponseEntity<SubsonicResult> getPlaylists(SubsonicCommonReq req, String username) {
        PlaylistsRes playlists = playlistApi.getPlaylists(req, username);
        return playlists.success();
    }
    
    @Operation(summary = "返回已保存播放列表中的文件列表")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @GetMapping({"/getPlaylist.view", "/getPlaylist"})
    @ManualSerialize
    public ResponseEntity<SubsonicResult> getPlaylist(SubsonicCommonReq req, @RequestParam("id") Long id) {
        PlaylistRes playlists = playlistApi.getPlaylist(id);
        return playlists.success();
    }
    
    @Operation(summary = "创建（或更新）播放列表")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @GetMapping({"/createPlaylist.view", "/createPlaylist"})
    @ManualSerialize
    public ResponseEntity<SubsonicResult> createPlaylist(SubsonicCommonReq req,
                                                         @RequestParam(value = "playlistId", required = false) Long playlistId,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "songId", required = false) Long songId
    ) {
        CreatePlaylistRes playlists = playlistApi.createPlaylist(req, playlistId, name, songId);
        return playlists.success();
    }
    
    @Operation(summary = "创建（或更新）播放列表")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @GetMapping({"/updatePlaylist.view", "/updatePlaylist"})
    @ManualSerialize
    public ResponseEntity<SubsonicResult> updatePlaylist(SubsonicCommonReq req,
                                                         @RequestParam(value = "playlistId") Long playlistId,
                                                         @RequestParam(value = "name", required = false) String name,
                                                         @RequestParam(value = "comment", required = false) String comment,
                                                         @RequestParam(value = "public", required = false) Boolean publicFlag,
                                                         @RequestParam(value = "songIdToAdd", required = false) List<Long> songIdToAdd,
                                                         @RequestParam(value = "songIndexToRemove", required = false) List<Long> songIndexToRemove
    ) {
        playlistApi.updatePlaylist(req, playlistId, name, comment, publicFlag, songIdToAdd, songIndexToRemove);
        return new SubsonicResult().success();
    }
    
    @Operation(summary = "创建（或更新）播放列表")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class)),
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, contentSchema = @Schema(implementation = SubsonicResult.class))
                 }
    )
    @GetMapping({"/deletePlaylist.view", "/deletePlaylist"})
    @ManualSerialize
    public ResponseEntity<SubsonicResult> deletePlaylist(SubsonicCommonReq req,
                                                         @RequestParam(value = "id") Long id
    ) {
        playlistApi.deletePlaylist(req, id);
        return new SubsonicResult().success();
    }
}

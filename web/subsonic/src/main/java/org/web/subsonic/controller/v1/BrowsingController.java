package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.album.AlbumRes;
import org.api.subsonic.model.res.albuminfo.AlbumInfoRes;
import org.api.subsonic.model.res.albuminfo2.AlbumInfo2Res;
import org.api.subsonic.model.res.artist.ArtistRes;
import org.api.subsonic.model.res.artistinfo.ArtistInfoRes;
import org.api.subsonic.model.res.artistinfo2.ArtistInfo2Res;
import org.api.subsonic.model.res.artists.ArtistsRes;
import org.api.subsonic.model.res.genres.GenresRes;
import org.api.subsonic.model.res.indexes.IndexesRes;
import org.api.subsonic.model.res.musicdirectory.MusicDirectoryRes;
import org.api.subsonic.model.res.musicfolders.MusicFoldersRes;
import org.api.subsonic.model.res.similarsongs.SimilarSongsRes;
import org.api.subsonic.model.res.similarsongs2.SimilarSongs2Res;
import org.api.subsonic.model.res.song.SongRes;
import org.api.subsonic.model.res.topsongs.TopSongsRes;
import org.api.subsonic.model.res.videoinfo.VideoInfoRes;
import org.api.subsonic.model.res.videos.VideosRes;
import org.api.subsonic.service.BrowsingApi;
import org.core.model.HttpStatusStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "浏览音乐")
@RestController(SubsonicConfig.SUBSONIC + "BrowsingController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class BrowsingController {
    
    @Autowired
    private BrowsingApi browsingApi;
    
    @Operation(summary = "返回所有已配置的顶级音乐文件夹。不带额外的参数")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MusicFoldersRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = MusicFoldersRes.class))
                 }
    )
    @GetMapping({"/getMusicFolders.view", "/getMusicFolders"})
    @ManualSerialize
    public ResponseEntity<String> getMusicFolders(SubsonicCommonReq req) {
        MusicFoldersRes res = browsingApi.getMusicFolders(req);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有艺术家的索引结构", description = "忽略musicFolderId和ifModifiedSince参数")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = IndexesRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = IndexesRes.class))
                 }
    )
    @GetMapping({"/getIndexes.view", "/getIndexes"})
    @ManualSerialize
    public ResponseEntity<String> getIndexes(SubsonicCommonReq req, @RequestParam(value = "musicFolderId", required = false) String musicFolderId, @RequestParam(value = "ifModifiedSince", required = false) String ifModifiedSince) {
        IndexesRes res = browsingApi.getIndexes(req, musicFolderId, ifModifiedSince);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有艺术家的索引结构", description = "忽略musicFolderId和ifModifiedSince参数. 暂时未实现")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = MusicDirectoryRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = MusicDirectoryRes.class))
                 }
    )
    @GetMapping({"/getMusicDirectory.view", "/getMusicDirectory"})
    @ManualSerialize
    public ResponseEntity<String> getMusicDirectory(SubsonicCommonReq req) {
        MusicDirectoryRes res = browsingApi.getMusicDirectory(req);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有流派", description = "返回音乐和专辑流派")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenresRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = GenresRes.class))
                 }
    )
    @GetMapping({"/getGenres.view", "/getGenres"})
    @ManualSerialize
    public ResponseEntity<String> getGenres(SubsonicCommonReq req) {
        GenresRes res = browsingApi.getGenres(req);
        return res.success(req);
    }
    
    @Operation(summary = "与'/getIndexes'类似，但根据 ID3 标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArtistsRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ArtistsRes.class))
                 }
    )
    @GetMapping({"/getArtists.view", "/getArtists"})
    @ManualSerialize
    public ResponseEntity<String> getArtists(SubsonicCommonReq req, @RequestParam(value = "musicFolderId", required = false) String musicFolderId) {
        ArtistsRes res = browsingApi.getArtists(req);
        return res.success(req);
    }
    
    @Operation(summary = "返回艺术家的详细信息，包括唱片集列表。此方法根据ID3标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArtistRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ArtistRes.class))
                 }
    )
    @GetMapping({"/getArtist.view", "/getArtist"})
    @ManualSerialize
    public ResponseEntity<String> getArtist(SubsonicCommonReq req, @RequestParam(value = "id", required = false) String id) {
        ArtistRes res = browsingApi.getArtist(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "返回专辑的详细信息，包括歌曲列表。此方法根据ID3标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AlbumRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = AlbumRes.class))
                 }
    )
    @GetMapping({"/getAlbum.view", "/getAlbum"})
    @ManualSerialize
    public ResponseEntity<String> getAlbum(SubsonicCommonReq req, @RequestParam("id") String id) {
        AlbumRes res = browsingApi.getAlbum(Long.valueOf(id));
        return res.success(req);
    }
    
    @Operation(summary = "返回歌曲的详细信息")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SongRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = SongRes.class))
                 }
    )
    @GetMapping({"/getSong.view", "/getSong"})
    @ManualSerialize
    public ResponseEntity<String> getSong(SubsonicCommonReq req, @RequestParam("id") Long id) {
        SongRes res = browsingApi.getSong(id);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有视频文件", description = "未实现")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VideosRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = VideosRes.class))
                 }
    )
    @GetMapping({"/getVideos.view", "/getSong"})
    @ManualSerialize
    public ResponseEntity<String> getVideos(SubsonicCommonReq req, @RequestParam("id") Long id) {
        VideosRes res = browsingApi.getVideos(id);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有视频文件", description = "未实现")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = VideoInfoRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = VideoInfoRes.class))
                 }
    )
    @GetMapping({"/getVideoInfo.view", "/getVideoInfo"})
    @ManualSerialize
    public ResponseEntity<String> getVideoInfo(SubsonicCommonReq req, @RequestParam("id") Long id) {
        VideoInfoRes res = browsingApi.getVideoInfo(id);
        return res.success(req);
    }
    
    @Operation(summary = "艺术家信息", description = "返回带有传记、图像 URL 和类似艺术家的艺术家信息。")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArtistInfoRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ArtistInfoRes.class))
                 }
    )
    @GetMapping({"/getArtistInfo.view", "/getArtistInfo"})
    @ManualSerialize
    public ResponseEntity<String> getArtistInfo(SubsonicCommonReq req, @RequestParam("id") Long id, @RequestParam(value = "count", defaultValue = "20", required = false) Integer count, @RequestParam(value = "includeNotPresent", defaultValue = "false", required = false) Boolean includeNotPresent) {
        ArtistInfoRes res = browsingApi.getArtistInfo(req, id, count, includeNotPresent);
        return res.success(req);
    }
    
    @Operation(summary = "类似于 '/getArtistInfo'", description = "类似于 getArtistInfo ，但根据ID3标签组织音乐。")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = ArtistInfo2Res.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = ArtistInfo2Res.class))
                 }
    )
    @GetMapping({"/getArtistInfo2.view", "/getArtistInfo2"})
    @ManualSerialize
    public ResponseEntity<String> getArtistInfo2(SubsonicCommonReq req, @RequestParam("id") Long id, @RequestParam(value = "count", defaultValue = "20", required = false) Integer count, @RequestParam(value = "includeNotPresent", defaultValue = "false", required = false) Boolean includeNotPresent) {
        ArtistInfo2Res res = browsingApi.getArtistInfo2(req, id, count, includeNotPresent);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AlbumInfoRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = AlbumInfoRes.class))
                 }
    )
    @GetMapping({"/getAlbumInfo.view", "/getAlbumInfo"})
    @ManualSerialize
    public ResponseEntity<String> getAlbumInfo(SubsonicCommonReq req, @RequestParam("id") Long id) {
        AlbumInfoRes res = browsingApi.getAlbumInfo(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = AlbumInfo2Res.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = AlbumInfo2Res.class))
                 }
    )
    @GetMapping({"/getAlbumInfo2.view", "/getAlbumInfo2"})
    @ManualSerialize
    public ResponseEntity<String> getAlbumInfo2(SubsonicCommonReq req, @RequestParam("id") Long id) {
        AlbumInfo2Res res = browsingApi.getAlbumInfo2(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SimilarSongsRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = SimilarSongsRes.class))
                 }
    )
    @GetMapping({"/getSimilarSongs.view", "/getSimilarSongs"})
    @ManualSerialize
    public ResponseEntity<String> getSimilarSongs(SubsonicCommonReq req, @RequestParam("id") Long id, @RequestParam(value = "count", defaultValue = "50", required = false) Integer count) {
        SimilarSongsRes res = browsingApi.getSimilarSongs(req, id, count);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = SimilarSongs2Res.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = SimilarSongs2Res.class))
                 }
    )
    @GetMapping({"/getSimilarSongs2.view", "/getSimilarSongs2"})
    @ManualSerialize
    public ResponseEntity<String> getSimilarSongs2(SubsonicCommonReq req, @RequestParam("id") Long id, @RequestParam(value = "count", defaultValue = "50", required = false) Integer count) {
        SimilarSongs2Res res = browsingApi.getSimilarSongs2(req, id, count);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStr.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = TopSongsRes.class)),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE, schema = @Schema(implementation = TopSongsRes.class))
                 }
    )
    @GetMapping({"/getTopSongs.view", "/getTopSongs"})
    @ManualSerialize
    public ResponseEntity<String> getTopSongs(SubsonicCommonReq req, @RequestParam("artist") String artist, @RequestParam(value = "count", defaultValue = "50", required = false) Integer count) {
        TopSongsRes res = browsingApi.getTopSongs(req, artist, count);
        return res.success(req);
    }
}

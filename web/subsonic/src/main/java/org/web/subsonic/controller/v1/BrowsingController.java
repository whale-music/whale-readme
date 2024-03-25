package org.web.subsonic.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
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
import org.core.common.constant.HttpStatusStrConstant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "浏览音乐")
@RestController(SubsonicConfig.SUBSONIC + "BrowsingController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class BrowsingController {
    
    private final BrowsingApi browsingApi;
    
    public BrowsingController(BrowsingApi browsingApi) {
        this.browsingApi = browsingApi;
    }
    
    @Operation(summary = "返回所有已配置的顶级音乐文件夹。不带额外的参数")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getMusicFolders.view", "/getMusicFolders"})
    @ManualSerialize
    public ResponseEntity<String> getMusicFolders(SubsonicCommonReq req) {
        MusicFoldersRes res = browsingApi.getMusicFolders(req);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有艺术家的索引结构", description = "忽略musicFolderId和ifModifiedSince参数")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getIndexes.view", "/getIndexes"})
    @ManualSerialize
    public ResponseEntity<String> getIndexes(SubsonicCommonReq req,
                                             @Parameter(description = "如果指定，则仅返回音乐文件夹中具有给定ID的艺术家。参见 `getMusicFolders`", deprecated = true)
                                             @RequestParam(value = "musicFolderId", required = false) String musicFolderId, @RequestParam(value = "ifModifiedSince", required = false) String ifModifiedSince) {
        IndexesRes res = browsingApi.getIndexes(req, musicFolderId, ifModifiedSince);
        return res.success(req);
    }
    
    @Operation(summary = "获取音乐目录", description = "返回音乐目录中所有文件的列表。通常用于获取艺术家的专辑列表或专辑的歌曲列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getMusicDirectory.view", "/getMusicDirectory"})
    @ManualSerialize
    public ResponseEntity<String> getMusicDirectory(SubsonicCommonReq req,
                                                    @Parameter(description = "可能是专辑ID或者艺术家ID")
                                                    Long id) {
        MusicDirectoryRes res = browsingApi.getMusicDirectory(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有流派", description = "返回音乐和专辑流派")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getGenres.view", "/getGenres"})
    @ManualSerialize
    public ResponseEntity<String> getGenres(SubsonicCommonReq req) {
        GenresRes res = browsingApi.getGenres(req);
        return res.success(req);
    }
    
    @Operation(summary = "与'/getIndexes'类似，但根据 ID3 标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getArtists.view", "/getArtists"})
    @ManualSerialize
    public ResponseEntity<String> getArtists(SubsonicCommonReq req,
                                             @Parameter(description = "如果指定，则仅返回音乐文件夹中具有给定ID的艺术家。参见 `getMusicFolders`", deprecated = true)
                                             @RequestParam(value = "musicFolderId", required = false) String musicFolderId) {
        ArtistsRes res = browsingApi.getArtists(req);
        return res.success(req);
    }
    
    @Operation(summary = "返回艺术家的详细信息，包括唱片集列表。此方法根据ID3标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getArtist.view", "/getArtist"})
    @ManualSerialize
    public ResponseEntity<String> getArtist(SubsonicCommonReq req,
                                            @Parameter(description = "艺术家ID", deprecated = true)
                                            @RequestParam(value = "id", required = false) String id) {
        ArtistRes res = browsingApi.getArtist(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "返回专辑的详细信息，包括歌曲列表。此方法根据ID3标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getAlbum.view", "/getAlbum"})
    @ManualSerialize
    public ResponseEntity<String> getAlbum(SubsonicCommonReq req,
                                           @Parameter(description = "专辑ID")
                                           @RequestParam("id") String id) {
        AlbumRes res = browsingApi.getAlbum(Long.valueOf(id));
        return res.success(req);
    }
    
    @Operation(summary = "返回歌曲的详细信息")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getSong.view", "/getSong"})
    @ManualSerialize
    public ResponseEntity<String> getSong(SubsonicCommonReq req,
                                          @Parameter(description = "音乐ID")
                                          @RequestParam("id") Long id) {
        SongRes res = browsingApi.getSong(id);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有视频文件", description = "未实现")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getVideos.view", "/getSong"})
    @ManualSerialize
    public ResponseEntity<String> getVideos(SubsonicCommonReq req) {
        VideosRes res = browsingApi.getVideos();
        return res.success(req);
    }
    
    @Operation(summary = "返回所有视频文件", description = "未实现")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getVideoInfo.view", "/getVideoInfo"})
    @ManualSerialize
    public ResponseEntity<String> getVideoInfo(SubsonicCommonReq req,
                                               @Parameter(description = "视频ID")
                                               @RequestParam("id") Long id) {
        VideoInfoRes res = browsingApi.getVideoInfo(id);
        return res.success(req);
    }
    
    @Operation(summary = "艺术家信息", description = "返回带有传记、图像 URL 和类似艺术家的艺术家信息。")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getArtistInfo.view", "/getArtistInfo"})
    @ManualSerialize
    public ResponseEntity<String> getArtistInfo(SubsonicCommonReq req,
                                                @Parameter(description = "艺术家、专辑或歌曲ID")
                                                @RequestParam("id") Long id,
                                                
                                                @Parameter(description = "返回的最大类似艺术家数量")
                                                @RequestParam(value = "count", defaultValue = "20", required = false) Integer count,
                                                
                                                @Parameter(description = "是否返回媒体库中不存在的艺术家")
                                                @RequestParam(value = "includeNotPresent", defaultValue = "false", required = false) Boolean includeNotPresent) {
        ArtistInfoRes res = browsingApi.getArtistInfo(req, id, count, includeNotPresent);
        return res.success(req);
    }
    
    @Operation(summary = "类似于 '/getArtistInfo'", description = "类似于 getArtistInfo ，但根据ID3标签组织音乐。")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getArtistInfo2.view", "/getArtistInfo2"})
    @ManualSerialize
    public ResponseEntity<String> getArtistInfo2(SubsonicCommonReq req,
                                                 @Parameter(description = "艺术家、专辑或歌曲ID")
                                                 @RequestParam("id") Long id,
                                                 
                                                 @Parameter(description = "返回的最大类似艺术家数量")
                                                 @RequestParam(value = "count", defaultValue = "20", required = false) Integer count,
                                                 
                                                 
                                                 @Parameter(description = "是否返回媒体库中不存在的艺术家")
                                                 @RequestParam(value = "includeNotPresent", defaultValue = "false", required = false) Boolean includeNotPresent) {
        ArtistInfo2Res res = browsingApi.getArtistInfo2(req, id, count, includeNotPresent);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getAlbumInfo.view", "/getAlbumInfo"})
    @ManualSerialize
    public ResponseEntity<String> getAlbumInfo(SubsonicCommonReq req,
                                               
                                               @Parameter(description = "专辑或歌曲ID")
                                               @RequestParam("id") Long id) {
        AlbumInfoRes res = browsingApi.getAlbumInfo(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "专辑信息", description = "返回专辑注释，图像URL等")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getAlbumInfo2.view", "/getAlbumInfo2"})
    @ManualSerialize
    public ResponseEntity<String> getAlbumInfo2(SubsonicCommonReq req,
                                                
                                                @Parameter(description = "专辑或歌曲ID")
                                                @RequestParam("id") Long id) {
        AlbumInfo2Res res = browsingApi.getAlbumInfo2(req, id);
        return res.success(req);
    }
    
    @Operation(summary = "获取相似歌曲", description = "使用last.fm中的数据返回给定艺术家和类似艺术家的随机歌曲集合。通常用于艺术家广播功能。(该功能未实现，返回的数据只是一个随机的歌曲集合)")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getSimilarSongs.view", "/getSimilarSongs"})
    @ManualSerialize
    public ResponseEntity<String> getSimilarSongs(SubsonicCommonReq req,
                                                  
                                                  @Parameter(description = "艺术家、专辑或歌曲ID", deprecated = true)
                                                  @RequestParam("id") Long id,
                                                  
                                                  @Parameter(description = "要返回的歌曲的最大数量")
                                                  @RequestParam(value = "count", defaultValue = "50", required = false) Integer count) {
        SimilarSongsRes res = browsingApi.getSimilarSongs(req, id, count);
        return res.success(req);
    }
    
    @Operation(summary = "获取相似歌曲", description = "类似于 getSimilarSongs ，但根据ID3标签组织音乐。")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getSimilarSongs2.view", "/getSimilarSongs2"})
    @ManualSerialize
    public ResponseEntity<String> getSimilarSongs2(SubsonicCommonReq req,
                                                   
                                                   @Parameter(description = "艺术家、专辑或歌曲ID", deprecated = true)
                                                   @RequestParam("id") Long id,
                                                   
                                                   @Parameter(description = "要返回的歌曲的最大数量")
                                                   @RequestParam(value = "count", defaultValue = "50", required = false) Integer count) {
        SimilarSongs2Res res = browsingApi.getSimilarSongs2(req, id, count);
        return res.success(req);
    }
    
    @Operation(summary = "获取热门歌曲", description = "使用last.fm中的数据返回给定艺术家的热门歌曲。(实际上是返回歌手最近上传歌曲)")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @GetMapping({"/getTopSongs.view", "/getTopSongs"})
    @ManualSerialize
    public ResponseEntity<String> getTopSongs(SubsonicCommonReq req,
                                              
                                              @Parameter(description = "艺术家的名字")
                                              @RequestParam("artist") String artist,
                                              
                                              @Parameter(description = "要返回的歌曲的最大数量")
                                              @RequestParam(value = "count", defaultValue = "50", required = false) Integer count) {
        TopSongsRes res = browsingApi.getTopSongs(req, artist, count);
        return res.success(req);
    }
}

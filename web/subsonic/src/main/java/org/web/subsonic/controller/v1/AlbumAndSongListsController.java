package org.web.subsonic.controller.v1;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.api.subsonic.aspect.ManualSerialize;
import org.api.subsonic.common.SubsonicCommonReq;
import org.api.subsonic.config.SubsonicConfig;
import org.api.subsonic.model.res.albumlist.AlbumListRes;
import org.api.subsonic.model.res.albumlist2.AlbumList2Res;
import org.api.subsonic.model.res.nowplaying.NowPlayingRes;
import org.api.subsonic.model.res.randomsongs.RandomSongsRes;
import org.api.subsonic.model.res.songsbygenre.SongsByGenreRes;
import org.api.subsonic.model.res.starred.StarredRes;
import org.api.subsonic.model.res.starred2.Starred2Res;
import org.api.subsonic.service.SongListsApi;
import org.core.common.constant.HttpStatusStrConstant;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "专辑/歌曲列表")
@RestController(SubsonicConfig.SUBSONIC + "SongListsController")
@RequestMapping("/rest")
@Slf4j
@CrossOrigin(origins = "*")
public class AlbumAndSongListsController {
    private final SongListsApi songListsApi;
    
    public AlbumAndSongListsController(SongListsApi songListsApi) {
        this.songListsApi = songListsApi;
    }
    
    @Operation(summary = "返回一个随机的，最新的，最高评级等列表")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getAlbumList.view", "/getAlbumList"})
    @ManualSerialize
    public ResponseEntity<String> getAlbumList(SubsonicCommonReq req,
                                               @Parameter(description = "列表类型",
                                                          content = {
                                                                  @Content(schema = @Schema(name = "random", description = "随机播放")),
                                                                  @Content(schema = @Schema(name = "random", description = "随机播放")),
                                                                  @Content(schema = @Schema(name = "newest", description = "最新添加")),
                                                                  @Content(schema = @Schema(name = "frequent", description = "播放最多")),
                                                                  @Content(schema = @Schema(name = "recent", description = "最近播放")),
                                                                  @Content(schema = @Schema(name = "starred", description = "收藏")),
                                                                  @Content(schema = @Schema(name = "alphabeticalByName", description = "按姓名字母顺序排列")),
                                                                  @Content(schema = @Schema(name = "alphabeticalByArtist", description = "按艺术家字母排序")),
                                                          }
                                               )
                                               @RequestParam("type") String type,
                                               
                                               @Parameter(description = "要返回的相册数。最多五百")
                                                   @RequestParam(value = "size", defaultValue = "20", required = false) Long size,
                                               
                                               @Parameter(description = "列表偏移量。例如，如果您想浏览最新专辑列表，则很有用。")
                                                   @RequestParam(value = "offset", defaultValue = "0", required = false) Long offset,
                                               
                                               @Parameter(description = "范围内的第一年。如果 fromYear > toYear ，则返回一个倒序列表。")
                                                   @RequestParam(value = "fromYear", defaultValue = "20", required = false) Long fromYear,
                                               
                                               @Parameter(description = "最后一年在范围内。")
                                                   @RequestParam(value = "toYear", defaultValue = "20", required = false) Long toYear,
                                               
                                               @Parameter(description = "流派的名称，例如，“摇滚”", deprecated = true)
                                                   @RequestParam(value = "genre", defaultValue = "20", required = false) Long genre,
                                               
                                               @Parameter(description = "（自1.11.0起）仅返回音乐文件夹中具有给定ID的专辑。参见 getMusicFolders 。(该参数无效)", deprecated = true)
                                                   @RequestParam(value = "musicFolderId", defaultValue = "20", required = false) Long musicFolderId
    ) {
        AlbumListRes res = songListsApi.getAlbumList(req, type, size, offset, fromYear, toYear, genre, musicFolderId);
        return res.success(req);
    }
    
    @Operation(summary = "类似于 getAlbumList ，但根据ID3标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getAlbumList2.view", "/getAlbumList2"})
    @ManualSerialize
    public ResponseEntity<String> getAlbumList2(SubsonicCommonReq req,
                                                @Parameter(description = "列表类型",
                                                           examples = {
                                                                   @ExampleObject(value = "random", description = "随机播放"),
                                                                   @ExampleObject(value = "newest", description = "最新添加"),
                                                                   @ExampleObject(value = "frequent", description = "播放最多"),
                                                                   @ExampleObject(value = "recent", description = "最近播放"),
                                                                   @ExampleObject(value = "starred", description = "收藏"),
                                                                   @ExampleObject(value = "alphabeticalByName", description = "按姓名字母顺序排列"),
                                                                   @ExampleObject(value = "alphabeticalByArtist", description = "按艺术家字母排序"),
                                                           }
                                                )
                                                @RequestParam("type") String type,
                                                
                                                @Parameter(description = "要返回的相册数。默认20")
                                                    @RequestParam(value = "size", defaultValue = "20", required = false) Long size,
                                                
                                                @Parameter(description = "列表偏移量。例如，如果您想浏览最新专辑列表，则很有用")
                                                    @RequestParam(value = "offset", defaultValue = "0", required = false) Long offset,
                                                
                                                @Parameter(description = "范围内的第一年。如果 fromYear > toYear ，则返回一个倒序列表")
                                                    @RequestParam(value = "fromYear", required = false) Long fromYear,
                                                
                                                @Parameter(description = "最后一年在范围内。")
                                                    @RequestParam(value = "toYear", required = false) Long toYear,
                                                
                                                @Parameter(description = "流派的名称，例如，“摇滚”", deprecated = true)
                                                    @RequestParam(value = "genre", required = false) Long genre,
                                                
                                                @Parameter(description = "（自1.11.0起）仅返回音乐文件夹中具有给定ID的专辑。参见 getMusicFolders ", deprecated = true)
                                                    @RequestParam(value = "musicFolderId", defaultValue = "20", required = false) Long musicFolderId
    ) {
        AlbumList2Res res = songListsApi.getAlbumList2(req, type, size, offset, fromYear, toYear, genre, musicFolderId);
        return res.success(req);
    }
    
    @Operation(summary = "返回符合给定条件的随机歌曲")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getRandomSongs.view", "/getRandomSongs"})
    @ManualSerialize
    public ResponseEntity<String> getRandomSongs(SubsonicCommonReq req,
                                                 @Parameter(description = "要返回的歌曲数。最多10")
                                                 @RequestParam(value = "size", defaultValue = "10", required = false) Long size,
                                                 
                                                 @Parameter(description = "歌曲流派")
                                                 @RequestParam(value = "genre", required = false) String genre,
                                                 
                                                 @Parameter(description = "仅返回今年之后或今年内发布的歌曲。", deprecated = true)
                                                 @RequestParam(value = "fromYear", required = false) Long fromYear,
                                                 
                                                 @Parameter(description = "只返回今年之前或今年出版的歌曲", deprecated = true)
                                                 @RequestParam(value = "toYear", required = false) Long toYear,
                                                 
                                                 @Parameter(description = "仅返回音乐文件夹中具有给定ID的歌曲。参见 getMusicFolders ", deprecated = true)
                                                 @RequestParam(value = "musicFolderId", required = false) Long musicFolderId
    ) {
        RandomSongsRes res = songListsApi.getRandomSongs(req, size, genre, fromYear, toYear, musicFolderId);
        return res.success(req);
    }
    
    @Operation(summary = "返回给定流派的歌曲")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getSongsByGenre.view", "/getSongsByGenre"})
    @ManualSerialize
    public ResponseEntity<String> getSongsByGenre(SubsonicCommonReq req,
                                                  @Parameter(description = "流派的名称，例如，“摇滚”")
                                                  @RequestParam(value = "genre") Long genre,
                                                  
                                                  @Parameter(description = "（自1.11.0起）仅返回音乐文件夹中具有给定ID的歌曲。参见 getMusicFolders 。", deprecated = true)
                                                      @RequestParam(value = "musicFolderId", required = false) Long musicFolderId,
                                                  
                                                  @Parameter(description = "要返回的歌曲数。最多10")
                                                      @RequestParam(value = "count", defaultValue = "10", required = false) Long count,
                                                  
                                                  @Parameter(description = "列表偏移量。例如，如果您想浏览最新专辑列表，则很有用。")
                                                      @RequestParam(value = "offset", required = false) Long offset
    ) {
        SongsByGenreRes res = songListsApi.getSongsByGenre(req, genre, musicFolderId, count, offset);
        return res.success(req);
    }
    
    @Operation(summary = "返回所有用户当前正在播放的内容。不需要额外的参数")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getNowPlaying.view", "/getNowPlaying"})
    @ManualSerialize
    public ResponseEntity<String> getNowPlaying(SubsonicCommonReq req) {
        NowPlayingRes res = songListsApi.getNowPlaying(req);
        return res.success(req);
    }
    
    @Operation(summary = "返回明星歌曲，专辑和艺术家")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getStarred.view", "/getStarred"})
    @ManualSerialize
    public ResponseEntity<String> getStarred(SubsonicCommonReq req,
                                             @Parameter(description = "（自1.11.0起）仅返回音乐文件夹中具有给定ID的歌曲。参见 getMusicFolders 。", deprecated = true)
                                             @RequestParam(value = "musicFolderId", required = false) Long musicFolderId
    ) {
        StarredRes res = songListsApi.getStarred(req, musicFolderId);
        return res.success(req);
    }
    
    @Operation(summary = "类似于 getStarred ，但根据ID3标签组织音乐")
    @ApiResponse(responseCode = HttpStatusStrConstant.OK,
                 content = {
                         @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
                         @Content(mediaType = MediaType.APPLICATION_XML_VALUE)
                 }
    )
    @WebLog(LogNameConstant.SUBSONIC)
    @GetMapping({"/getStarred2.view", "/getStarred2"})
    @ManualSerialize
    public ResponseEntity<String> getStarred2(SubsonicCommonReq req,
                                              @Parameter(description = "（自1.11.0起）仅返回音乐文件夹中具有给定ID的歌曲。参见 getMusicFolders 。", deprecated = true)
                                              @RequestParam(value = "musicFolderId", required = false) Long musicFolderId
    ) {
        Starred2Res res = songListsApi.getStarred2(req, musicFolderId);
        return res.success(req);
    }
}

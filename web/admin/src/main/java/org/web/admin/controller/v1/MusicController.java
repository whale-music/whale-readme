package org.web.admin.controller.v1;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.req.*;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.model.res.MobileMusicDetailRes;
import org.api.admin.model.res.MusicPlayInfoRes;
import org.api.admin.model.res.MusicTabsPageRes;
import org.api.admin.service.MusicFlowApi;
import org.core.common.annotation.AnonymousAccess;
import org.core.common.result.R;
import org.core.common.weblog.annotation.WebLog;
import org.core.common.weblog.constant.LogNameConstant;
import org.core.config.HttpRequestConfig;
import org.core.mybatis.pojo.MusicDetails;
import org.core.mybatis.pojo.TbResourcePojo;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@RestController(AdminConfig.ADMIN + "MusicController")
@RequestMapping("/admin/music")
@Slf4j
@CrossOrigin
@RequiredArgsConstructor
public class MusicController {
    private final MusicFlowApi musicFlowApi;
    
    private final HttpRequestConfig httpRequestConfig;
    
    /**
     * 上传音乐临时文件
     *
     * @param uploadFile 临时文件
     * @return 返回音乐数据
     */
    @AnonymousAccess
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/upload/music/file")
    public R uploadMusicFile(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam(value = "url", required = false) String url) throws CannotReadException, TagException, ReadOnlyFileException, IOException {
        return R.success(musicFlowApi.uploadMusicFile(uploadFile, url));
    }
    
    /**
     * 获取临时上传音乐字节数据
     *
     * @param musicTempFile 临时文件
     * @return 字节数据
     */
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/get/temp/{music}")
    public ResponseEntity<FileSystemResource> getMusicTempFile(@PathVariable("music") String musicTempFile) {
        return musicFlowApi.getMusicTempFile(musicTempFile);
    }
    
    /**
     * 上传音乐信息，包括临时文件名
     *
     * @param dto 音乐信息
     * @return 返回成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/upload/info")
    public R uploadMusicInfo(@Validated @RequestBody AudioInfoReq dto) {
        MusicDetails musicDetails = musicFlowApi.saveMusicInfo(dto);
        return R.success(musicDetails);
    }
    
    /**
     * 获取音乐URL
     *
     * @param musicId 音乐id
     * @param refresh 是否刷新
     * @return url
     */
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/url/{musicId}")
    public R getMusicUrl(@PathVariable("musicId") Set<String> musicId, @RequestParam(value = "refresh", required = false, defaultValue = "false") Boolean refresh) {
        return R.success(musicFlowApi.getMusicUrl(musicId, refresh));
    }
    
    /**
     * 获取歌词
     *
     * @param musicId 音乐ID
     * @return 歌词列表
     */
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/lyric/{musicId}")
    public R getMusicLyric(@PathVariable("musicId") Long musicId) {
        return R.success(musicFlowApi.getMusicLyric(musicId));
    }
    
    
    /**
     * 删除音乐
     *
     * @param musicId 音乐ID
     * @param compel  是否强制删除
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @DeleteMapping("/")
    public R deleteMusic(@RequestBody RemoveMusicReq musicId, @RequestParam(value = "compel", required = false, defaultValue = "false") Boolean compel) {
        musicFlowApi.deleteMusic(musicId.getIds(), compel);
        return R.success();
    }
    
    /**
     * 更新或保存歌词
     *
     * @param musicId 音乐ID
     * @param type    歌词类型
     * @param lyric   歌词
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/lyric/{musicId}")
    public R saveOrUpdateLyric(@PathVariable("musicId") Long musicId, @RequestParam("type") String type, @RequestBody Map<String, String> lyric) {
        musicFlowApi.saveOrUpdateLyric(musicId, type, MapUtil.get(lyric, "lyric", String.class));
        return R.success();
    }
    
    /**
     * 获取音乐信息
     *
     * @param id 歌曲ID
     * @return 歌曲信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/musicInfo/{id}")
    public R getMusicInfo(@PathVariable("id") Long id) {
        return R.success(musicFlowApi.getMusicInfo(id));
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/mobile/detail/{id}")
    public R getMobileMusicDetail(@PathVariable("id") Long id) {
        MobileMusicDetailRes res = musicFlowApi.getMobileMusicDetail(id);
        return R.success(res);
    }
    
    /**
     * 更新音乐信息
     *
     * @param req 音乐信息
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/")
    public R updateMusic(@RequestBody SaveOrUpdateMusicReq req) {
        musicFlowApi.saveOrUpdateMusic(req);
        return R.success();
    }
    
    /**
     * 更新音乐信息
     *
     * @param req 音乐信息
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/save")
    public R saveMusic(@RequestBody SaveMusicReq req) {
        musicFlowApi.saveMusic(req);
        return R.success();
    }
    
    /**
     * 自动上传音乐文件
     *
     * @param userId     用户ID
     * @param uploadFile 上传文件
     * @param musicId    音乐ID
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/auto/upload")
    @AnonymousAccess
    public R uploadAutoMusicFile(@RequestParam("userId") Long userId, @RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam("id") Long musicId) throws IOException {
        File uploadFile1 = FileUtil.writeBytes(uploadFile.getBytes(),
                httpRequestConfig.getTempPathFile(Objects.requireNonNull(uploadFile.getOriginalFilename())));
        musicFlowApi.uploadAutoMusicFile(userId, uploadFile1, musicId);
        return R.success();
    }
    
    /**
     * 手动上传音乐文件
     *
     * @param musicSource 音乐信息
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/manual/upload")
    public R uploadManualMusic(@RequestBody UploadMusicReq musicSource) {
        musicFlowApi.uploadManualMusic(musicSource);
        return R.success();
    }
    
    /**
     * 更新音乐 音源
     *
     * @param source 音源信息
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/update/source")
    public R updateSource(@RequestBody TbResourcePojo source) {
        musicFlowApi.updateSource(source);
        return R.success();
    }
    
    /**
     * 删除音源
     *
     * @param id 音源ID
     * @return 成功信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @DeleteMapping("/delete/source/{id}")
    public R deleteSource(@PathVariable("id") Long id) {
        musicFlowApi.deleteSource(id);
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @GetMapping("/select")
    public R selectResources(@RequestParam(value = "md5", required = false) String md5) {
        List<Map<String, Object>> maps = musicFlowApi.selectResources(md5);
        return R.success(maps);
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/sync/metadata")
    public R syncMetaMusicFile(@RequestBody SyncMusicMetaDataReq req) {
        musicFlowApi.syncMetaMusicFile(req);
        return R.success();
    }
    
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/page")
    public R getMusicPage(@RequestBody MusicTabPageReq req) {
        PageResCommon<MusicTabsPageRes> page = musicFlowApi.getMusicPage(req);
        return R.success(page);
    }
    
    /**
     * 获取音乐播放需要的信息
     *
     * @param req 音乐 ID
     * @return 音乐信息
     */
    @WebLog(LogNameConstant.ADMIN)
    @PostMapping("/play/info")
    public R getMusicPlayInfo(@RequestBody MusicPlayInfoReq req) {
        List<MusicPlayInfoRes> res = musicFlowApi.getMusicPlayInfo(req.getIds(), req.getIsPlayed());
        return R.success(res);
    }
}

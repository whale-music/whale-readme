package org.web.admin.controller.v1;

import cn.hutool.core.map.MapUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.req.RemoveMusicReq;
import org.api.admin.model.req.SaveOrUpdateMusicReq;
import org.api.admin.model.req.UploadMusicReq;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.common.result.R;
import org.core.common.annotation.AnonymousAccess;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController(AdminConfig.ADMIN + "MusicController")
@RequestMapping("/admin/music")
@Slf4j
@CrossOrigin
@AllArgsConstructor
public class MusicController {
    private final MusicFlowApi uploadMusic;
    
    /**
     * 上传音乐临时文件
     *
     * @param uploadFile 临时文件
     * @return 返回音乐数据
     */
    @AnonymousAccess
    @PostMapping("/upload/music/file")
    public R uploadMusicFile(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam(value = "url", required = false) String url) throws CannotReadException, TagException, ReadOnlyFileException, IOException {
        return R.success(uploadMusic.uploadMusicFile(uploadFile, url));
    }
    
    /**
     * 获取临时上传音乐字节数据
     *
     * @param musicTempFile 临时文件
     * @return 字节数据
     */
    @GetMapping("/get/temp/{music}")
    public ResponseEntity<FileSystemResource> getMusicTempFile(@PathVariable("music") String musicTempFile) {
        return uploadMusic.getMusicTempFile(musicTempFile);
    }
    
    /**
     * 上传音乐信息，包括临时文件名
     *
     * @param dto 音乐信息
     * @return 返回成功信息
     */
    @PostMapping("/upload/info")
    public R uploadMusicInfo(@Validated @RequestBody AudioInfoReq dto) {
        MusicDetails musicDetails = uploadMusic.saveMusicInfo(dto);
        return R.success(musicDetails);
    }
    
    /**
     * 获取音乐URL
     *
     * @param musicId 音乐id
     * @param refresh 是否刷新
     * @return url
     */
    @GetMapping("/url/{musicId}")
    public R getMusicUrl(@PathVariable("musicId") Set<String> musicId, @RequestParam(value = "refresh", required = false, defaultValue = "false") Boolean refresh) {
        return R.success(uploadMusic.getMusicUrl(musicId, refresh));
    }
    
    /**
     * 获取歌词
     *
     * @param musicId 音乐ID
     * @return 歌词列表
     */
    @GetMapping("/lyric/{musicId}")
    public R getMusicLyric(@PathVariable("musicId") Long musicId) {
        return R.success(uploadMusic.getMusicLyric(musicId));
    }
    
    
    /**
     * 删除音乐
     *
     * @param musicId 音乐ID
     * @param compel  是否强制删除
     * @return 成功信息
     */
    @DeleteMapping("/")
    public R deleteMusic(@RequestBody RemoveMusicReq musicId, @RequestParam(value = "compel", required = false, defaultValue = "false") Boolean compel) {
        uploadMusic.deleteMusic(musicId.getIds(), compel);
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
    @PostMapping("/lyric/{musicId}")
    public R saveOrUpdateLyric(@PathVariable("musicId") Long musicId, @RequestParam("type") String type, @RequestBody Map<String, String> lyric) {
        uploadMusic.saveOrUpdateLyric(musicId, type, MapUtil.get(lyric, "lyric", String.class));
        return R.success();
    }
    
    /**
     * 获取音乐信息
     *
     * @param id 歌曲ID
     * @return 歌曲信息
     */
    @GetMapping("/musicInfo/{id}")
    public R getMusicInfo(@PathVariable("id") Long id) {
        return R.success(uploadMusic.getMusicInfo(id));
    }
    
    /**
     * 更新音乐信息
     *
     * @param req 音乐信息
     * @return 成功信息
     */
    @PostMapping("/")
    public R updateMusic(@RequestBody SaveOrUpdateMusicReq req) {
        uploadMusic.saveOrUpdateMusic(req);
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
    @PostMapping("/auto/upload")
    @AnonymousAccess
    public R uploadAutoMusicFile(@RequestParam("userId") Long userId, @RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam("id") Long musicId) {
        uploadMusic.uploadAutoMusicFile(userId, uploadFile, musicId);
        return R.success();
    }
    
    /**
     * 手动上传音乐文件
     *
     * @param musicSource 音乐信息
     * @return 成功信息
     */
    @PostMapping("/manual/upload")
    public R uploadManualMusic(@RequestBody UploadMusicReq musicSource) {
        uploadMusic.uploadManualMusic(musicSource);
        return R.success();
    }
    
    /**
     * 更新音乐 音源
     *
     * @param source 音源信息
     * @return 成功信息
     */
    @PostMapping("/update/source")
    public R updateSource(@RequestBody TbResourcePojo source) {
        uploadMusic.updateSource(source);
        return R.success();
    }
    
    /**
     * 删除音源
     *
     * @param id 音源ID
     * @return 成功信息
     */
    @DeleteMapping("/delete/source/{id}")
    public R deleteSource(@PathVariable("id") Long id) {
        uploadMusic.deleteSource(id);
        return R.success();
    }
    
    @GetMapping("/select")
    public R selectResources(@RequestParam(value = "md5", required = false) String md5) {
        List<HashMap<String, String>> maps = uploadMusic.selectResources(md5);
        return R.success(maps);
    }
    
    @AnonymousAccess
    @PostMapping("/pic/upload")
    public R uploadPic(@RequestParam(value = "file", required = false) MultipartFile uploadFile, @RequestParam("id") Long id, @RequestParam("type") String type) throws IOException {
        String picUrl = uploadMusic.uploadPic(uploadFile, id, type);
        return R.success(picUrl);
    }
}

package org.api.admin.service;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.core.common.constant.PicTypeConstant;
import org.core.config.HttpRequestConfig;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.core.utils.LocalFileUtil;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service(AdminConfig.ADMIN + "PicApi")
@Slf4j
@RequiredArgsConstructor
public class PicApi {
    
    private final HttpRequestConfig requestConfig;
    
    private final RemoteStorageService remoteStorageService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    /**
     * 获取临时文件字节
     *
     * @param musicTempFile 临时文件名
     * @return 字节数据
     */
    public ResponseEntity<FileSystemResource> getMusicTempFile(String musicTempFile) {
        LocalFileUtil.checkFileNameLegal(musicTempFile);
        File file = LocalFileUtil.checkFilePath(requestConfig.getTempPath(), musicTempFile);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
        return ResponseEntity.ok()
                             .headers(headers)
                             .contentLength(file.length())
                             .contentType(MediaType.parseMediaType(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                             .body(new FileSystemResource(file));
    }
    
    public String uploadPicFile(MultipartFile uploadFile, String url) throws IOException {
        File file;
        if (StringUtils.isBlank(url)) {
            file = FileUtil.writeBytes(uploadFile.getBytes(),
                    requestConfig.getTempPathFile(Objects.requireNonNull(uploadFile.getResource().getFilename()) + "- " + LocalDateTime.now().getNano()));
        } else {
            byte[] bytes = HttpUtil.downloadBytes(url);
            String name = PathUtil.getName(Paths.get(url));
            String suffix = FileUtil.getSuffix(name);
            if (StringUtils.isBlank(suffix)) {
                File fileBytes = FileUtil.writeBytes(bytes,
                        requestConfig.getTempPathFile(String.valueOf(LocalDateTime.now().getNano() + RandomUtil.randomInt())));
                file = FileUtil.touch(requestConfig.getTempPathFile(LocalDateTime.now().getNano() + StrPool.DOT + FileTypeUtil.getType(fileBytes)));
            } else {
                file = FileUtil.writeBytes(bytes,
                        requestConfig.getTempPathFile(LocalDateTime.now().getNano() + StrPool.DOT + suffix));
            }
        }
        return file.getName();
    }
    
    public String uploadPic(MultipartFile uploadFile, Long id, String type) throws IOException {
        // 下载封面, 保存文件名为md5
        String originalFilename = Optional.ofNullable(uploadFile.getOriginalFilename()).orElse("");
        String randomName = FileUtil.mainName(originalFilename) + "-" + UUID.fastUUID().toString(true) + "." + FileUtil.getSuffix(originalFilename);
        File mkdir = FileUtil.touch(requestConfig.getTempPathFile(randomName));
        File file = FileUtil.writeBytes(uploadFile.getBytes(), mkdir);
        byte tempType;
        switch (type) {
            case "music" -> tempType = PicTypeConstant.MUSIC;
            case "playList" -> tempType = PicTypeConstant.PLAYLIST;
            case "album" -> tempType = PicTypeConstant.ALBUM;
            case "artist" -> tempType = PicTypeConstant.ARTIST;
            case "userAvatar" -> tempType = PicTypeConstant.USER_AVATAR;
            case "userBackground" -> tempType = PicTypeConstant.USER_BACKGROUND;
            case "mv" -> tempType = PicTypeConstant.MV;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }
        remoteStorePicService.saveOrUpdatePicFile(id, tempType, file);
        return remoteStorageService.getPicResourceUrl(remoteStorePicService.getPicPath(id, tempType), false);
    }
}

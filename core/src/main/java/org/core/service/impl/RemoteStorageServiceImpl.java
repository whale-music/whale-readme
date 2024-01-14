package org.core.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.common.exception.BaseException;
import org.core.common.properties.SaveConfig;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.TbMvService;
import org.core.mybatis.pojo.TbMvPojo;
import org.core.oss.model.Resource;
import org.core.oss.service.OSSService;
import org.core.service.RemoteStorageService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteStorageServiceImpl implements RemoteStorageService {
    
    private final OSSService ossService;
    
    private final SaveConfig config;
    
    private final TbMvService tbMvService;
    
    @Override
    
    public Collection<String> getAllMD5(boolean refresh) {
        return ossService.getResourceMD5(refresh);
    }
    
    @Override
    public Collection<String> getResourceMD5(String md5, boolean refresh) {
        return ossService.getResourceMD5(md5, refresh);
    }
    
    @Override
    public Map<String, Map<String, String>> getAddressByMd5(String md5, boolean refresh) {
        return ossService.getAddressByMd5(Collections.singleton(md5), refresh);
    }
    
    @Override
    public Map<String, Map<String, String>> getAddressByMd5(Set<String> md5Set, boolean refresh) {
        return ossService.getAddressByMd5(md5Set, refresh);
    }
    
    public String getAddressesNoRefresh(String path) {
        return this.getAddresses(path, false);
    }
    
    @Override
    public Map<String, Map<String, String>> getAddresses(Collection<String> md5, boolean refresh) {
        return ossService.getAddressByMd5(Set.copyOf(md5), refresh);
    }
    
    @Override
    public Map<String, Map<String, String>> getAddressByMd5(boolean refresh) {
        return ossService.getAddressByMd5(null, refresh);
    }
    
    
    @Override
    public String getAddresses(String path, boolean refresh) {
        try {
            return ossService.getAddresses(path, refresh);
        } catch (BaseException e) {
            if (Objects.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                log.warn("获取下载地址出错: {}", e.getMessage());
                return "";
            }
            log.error(e.getMessage(), e);
            throw new BaseException(e.getCode(), e.getResultMsg());
        }
    }
    
    @Override
    public String uploadPicFile(File path, String md5) {
        return ossService.upload(config.getImgSave(), config.getAssignImgSave(), path, md5);
    }
    
    public String uploadAudioFile(File file, String md5Hex) {
        return ossService.upload(config.getObjectSave(), config.getAssignObjectSave(), file, md5Hex);
    }
    
    @Override
    public String uploadAudioFile(File file) {
        return uploadAudioFile(file, DigestUtil.md5Hex(file));
    }
    
    public String uploadMvFile(File mvFile, String md5) {
        return ossService.upload(config.getMvSave(), config.getAssignMvSave(), mvFile, md5);
    }
    
    public String uploadMvFile(File file) {
        return this.uploadMvFile(file, null);
    }
    
    
    public Set<Resource> listResource(boolean refresh) {
        return ossService.list(refresh);
    }
    
    public Resource getResource(String path, boolean refresh) {
        return ossService.getResourceInfo(path, refresh);
    }
    
    
    public void removeMvStorageFiles(List<Long> ids) {
        List<TbMvPojo> tbMvPojos = tbMvService.listByIds(ids);
        List<String> list = tbMvPojos.parallelStream().map(TbMvPojo::getPath).toList();
        ossService.delete(list);
    }
}


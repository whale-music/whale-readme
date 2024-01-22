package org.core.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.core.common.properties.SaveConfig;
import org.core.mybatis.iservice.TbMvService;
import org.core.mybatis.pojo.TbMvPojo;
import org.core.oss.model.Resource;
import org.core.oss.service.OSSService;
import org.core.oss.service.impl.alist.enums.ResourceEnum;
import org.core.service.RemoteStorageService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RemoteStorageServiceImpl implements RemoteStorageService {
    
    private final OSSService ossService;
    
    private final SaveConfig config;
    
    private final TbMvService tbMvService;
    
    @Override
    public Collection<String> getResourceAllMD5(boolean refresh) {
        Set<Resource> resourceAllItems = ossService.getResourceAllItems(refresh);
        return resourceAllItems.parallelStream().map(Resource::getUrl).collect(Collectors.toSet());
    }
    
    @Override
    public String getMusicResourceUrlByMd5(String md5, boolean refresh) {
        return ossService.getResourceByMd5(md5, refresh, ResourceEnum.MUSIC).getUrl();
    }
    
    @Override
    public String getPicResourceUrlByMd5(String md5, boolean refresh) {
        return ossService.getResourceByMd5(md5, refresh, ResourceEnum.PIC).getUrl();
    }
    
    @Override
    public String getMvResourceUrlByMd5(String md5, boolean refresh) {
        return ossService.getResourceByMd5(md5, refresh, ResourceEnum.MV).getUrl();
    }
    
    @Override
    public Map<String, Resource> getMusicResourceByMd5(String md5, boolean refresh) {
        return ossService.getResourceByMd5ToMap(Set.of(md5), refresh, ResourceEnum.MUSIC);
    }
    
    @Override
    public Map<String, Resource> getMusicResourceByMd5(Set<String> md5Set, boolean refresh) {
        return ossService.getResourceByMd5ToMap(md5Set, refresh, ResourceEnum.MUSIC);
    }
    
    public Map<String, Resource> getPicAddressByMd5(Set<String> md5Set, boolean refresh) {
        return ossService.getResourceByMd5ToMap(md5Set, refresh, ResourceEnum.PIC);
    }
    
    public Map<String, Resource> getMvAddressByMd5(Set<String> md5Set, boolean refresh) {
        return ossService.getResourceByMd5ToMap(md5Set, refresh, ResourceEnum.MV);
    }
    
    @Override
    public Map<String, Resource> getAddresses(Collection<String> md5, boolean refresh, ResourceEnum type) {
        return ossService.getResourceByMd5ToMap(md5, refresh, type);
    }
    
    @Override
    public Map<String, Resource> getResourceByMd5(boolean refresh, ResourceEnum type) {
        return ossService.getResourceByMd5ToMap(null, refresh, type);
    }
    
    @Override
    public String getAddresses(String path, boolean refresh, ResourceEnum type) {
        return ossService.getResourceUrl(path, refresh, type);
    }
    
    public String getMusicResource(String path, boolean refresh) {
        return ossService.getResourceUrl(path, refresh, ResourceEnum.MUSIC);
    }
    
    public String getPicResource(String path, boolean refresh) {
        return ossService.getResourceUrl(path, refresh, ResourceEnum.PIC);
    }
    
    public String getMvResource(String path, boolean refresh) {
        return ossService.getResourceUrl(path, refresh, ResourceEnum.MV);
    }
    
    public String getMvAddressesNoRefresh(String path) {
        return this.getMvResource(path, false);
    }
    
    @Override
    public String uploadPicFile(File path, String md5) {
        return ossService.upload(config.getImgSave(), config.getAssignImgSave(), path, md5, ResourceEnum.PIC);
    }
    
    public String uploadAudioFile(File file, String md5Hex) {
        return ossService.upload(config.getObjectSave(), config.getAssignObjectSave(), file, md5Hex, ResourceEnum.MUSIC);
    }
    
    @Override
    public String uploadAudioFile(File file) {
        return uploadAudioFile(file, DigestUtil.md5Hex(file));
    }
    
    public String uploadMvFile(File mvFile, String md5) {
        return ossService.upload(config.getMvSave(), config.getAssignMvSave(), mvFile, md5, ResourceEnum.MV);
    }
    
    public String uploadMvFile(File file) {
        return this.uploadMvFile(file, null);
    }
    
    public Set<Resource> listResource(boolean refresh) {
        return ossService.getResourceAllItems(refresh);
    }
    
    @Override
    public Resource getMusicResource(String path) {
        return ossService.getResource(path, false, ResourceEnum.MUSIC);
    }
    
    @Override
    public Resource getMvResource(String path) {
        return ossService.getResource(path, false, ResourceEnum.MV);
    }
    
    @Override
    public Resource getPicResource(String path) {
        return ossService.getResource(path, false, ResourceEnum.PIC);
    }
    
    @Override
    public String getMusicResourceUrl(String path, boolean refresh) {
        return ossService.getResource(path, refresh, ResourceEnum.MUSIC).getUrl();
    }
    
    @Override
    public String getMvResourceUrl(String path, boolean refresh) {
        return ossService.getResource(path, false, ResourceEnum.MV).getUrl();
    }
    
    @Override
    public String getPicResourceUrl(String path, boolean refresh) {
        return ossService.getResource(path, false, ResourceEnum.PIC).getUrl();
    }
    
    @Override
    public void deleteMusic(List<String> path) {
        ossService.delete(path, ResourceEnum.MUSIC);
    }
    
    @Override
    public void deleteMusic(String path) {
        ossService.delete(path, ResourceEnum.MUSIC);
    }
    
    @Override
    public void deleteMv(List<String> path) {
        ossService.delete(path, ResourceEnum.MV);
    }
    
    @Override
    public void deletePic(List<String> path) {
        ossService.delete(path, ResourceEnum.PIC);
    }
    
    @Override
    public void deletePic(String path) {
        ossService.delete(path, ResourceEnum.PIC);
    }
    
    @Override
    public void renameMusic(String path, String newName) {
        ossService.rename(path, newName, ResourceEnum.MUSIC);
    }
    
    public void removeMvStorageFiles(List<Long> ids) {
        List<TbMvPojo> mvList = tbMvService.listByIds(ids);
        List<String> list = mvList.parallelStream().map(TbMvPojo::getPath).toList();
        this.deletePic(list);
    }
}


package org.core.service;

import org.core.oss.model.Resource;
import org.core.oss.service.impl.alist.enums.ResourceEnum;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RemoteStorageService {
    Collection<String> getResourceAllMD5(boolean refresh);
    
    String getMusicResourceUrlByMd5(String md5, boolean refresh);
    
    String getPicResourceUrlByMd5(String md5, boolean refresh);
    
    String getMvResourceUrlByMd5(String md5, boolean refresh);
    
    Map<String, Resource> getMusicResourceByMd5(String md5, boolean refresh);
    
    Map<String, Resource> getMusicResourceByMd5(Set<String> md5Set, boolean refresh);
    
    String getMvAddressesNoRefresh(String path);
    
    Map<String, Resource> getAddresses(Collection<String> md5, boolean refresh, ResourceEnum type);
    
    Map<String, Resource> getResourceByMd5(boolean refresh, ResourceEnum type);
    
    String getAddresses(String path, boolean refresh, ResourceEnum type);
    
    String uploadPicFile(File path, String md5);
    
    String uploadMvFile(File mvFile, String md5);
    
    String uploadAudioFile(File file, String md5Hex);
    
    String uploadAudioFile(File file);
    
    String uploadMvFile(File file);
    
    Set<Resource> listResource(boolean refresh);
    
    Resource getMusicResource(String path);
    
    Resource getMvResource(String path);
    
    Resource getPicResource(String path);
    
    String getMusicResourceUrl(String path, boolean refresh);
    
    String getMvResourceUrl(String path, boolean refresh);
    
    String getPicResourceUrl(String path, boolean refresh);
    
    void deleteMusic(List<String> path);
    
    void deleteMusic(String path);
    
    void deleteMv(List<String> path);
    
    void deletePic(List<String> path);
    
    void deletePic(String path);
    
    void renameMusic(String path, String newName);
    
    void removeMvStorageFiles(List<Long> ids);
    
}

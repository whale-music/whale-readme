package org.core.service;

import org.core.oss.model.Resource;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface RemoteStorageService {
    Collection<String> getAllMD5(boolean refresh);
    
    Collection<String> getResourceMD5(String md5, boolean refresh);
    
    Map<String, Map<String, String>> getAddressByMd5(String md5, boolean refresh);
    
    Map<String, Map<String, String>> getAddressByMd5(Set<String> md5Set, boolean refresh);
    
    String getAddresses(String path, boolean refresh);
    
    String getAddressesNoRefresh(String path);
    
    Map<String, Map<String, String>> getAddresses(Collection<String> md5, boolean refresh);
    
    Map<String, Map<String, String>> getAddressByMd5(boolean refresh);
    
    String uploadPicFile(File path, String md5);
    
    String uploadMvFile(File mvFile, String md5);
    
    String uploadAudioFile(File file, String md5Hex);
    
    String uploadAudioFile(File file);
    
    String uploadMvFile(File file);
    
    Set<Resource> listResource(boolean refresh);
    
    Resource getResource(String path, boolean refresh);
    
    void removeMvStorageFiles(List<Long> ids);
    
}

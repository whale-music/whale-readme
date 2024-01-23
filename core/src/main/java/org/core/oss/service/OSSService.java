package org.core.oss.service;

import cn.hutool.core.map.MapUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.result.ResultCode;
import org.core.oss.model.Resource;
import org.core.oss.service.impl.alist.enums.ResourceEnum;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public interface OSSService {
    
    /**
     * 检测是否是当前存储地址
     *
     * @param serviceName 服务名
     * @return 是否相同
     */
    default boolean isCurrentOSS(String serviceName) {
        return StringUtils.equalsIgnoreCase(this.getMode(), serviceName);
    }
    
    /**
     * 返回当前服务名
     *
     * @return 服务名
     */
    String getMode();
    
    /**
     * 检查访问存储地址
     */
    void isConnected();
    
    /**
     * 存储文件是否存在
     *
     * @param path 文件名
     * @param type 查询数据类型
     */
    default void isExist(String path, ResourceEnum type) {
        Resource resource = this.getResource(path, true, type);
        Objects.requireNonNull(resource.getPath(), ResultCode.OSS_DATA_DOES_NOT_EXIST.getResultMsg());
    }
    
    /**
     * 根据路径获取数据
     *
     * @param paths   路径列表
     * @param refresh 是否刷新
     * @param type    查询数据类型
     * @return 资源数据
     */
    Map<String, Resource> getResourceList(Collection<String> paths, boolean refresh, ResourceEnum type);
    
    /**
     * 列出所有文件
     *
     * @param refresh 是否刷新
     * @param type    类型
     */
    default Set<Resource> getResourceList(boolean refresh, ResourceEnum type) {
        Map<String, Resource> resourceList = this.getResourceList(null, refresh, type);
        return resourceList.values().parallelStream().collect(Collectors.toSet());
    }
    
    default Set<Resource> getResourceAllItems(boolean refresh) {
        LinkedHashSet<Resource> resources = new LinkedHashSet<>();
        resources.addAll(this.getResourceList(refresh, ResourceEnum.MUSIC));
        resources.addAll(this.getResourceList(refresh, ResourceEnum.PIC));
        resources.addAll(this.getResourceList(refresh, ResourceEnum.MV));
        
        return resources;
    }
    
    /**
     * 获取音乐地址列表
     *
     * @param path    音乐集合
     * @param refresh 是否刷新缓存
     * @param type    查询数据类型
     * @return 音乐地址集合
     */
    default Map<String, String> getResourceToMapUrl(Collection<String> path, boolean refresh, ResourceEnum type) {
        Map<String, Resource> resourceList = getResourceList(path, refresh, type);
        return Optional.ofNullable(resourceList)
                       .orElse(Collections.emptyMap())
                       .values()
                       .parallelStream()
                       .collect(Collectors.toMap(Resource::getPath, Resource::getUrl));
    }
    
    /**
     * 获取文件信息
     *
     * @param path    文件路径
     * @param refresh 是否刷新
     * @param type    类型
     * @return 文件信息
     */
    default Resource getResource(String path, boolean refresh, ResourceEnum type) {
        Map<String, Resource> resourceList = getResourceList(Set.of(path), refresh, type);
        return MapUtil.get(resourceList, path, Resource.class, new Resource());
    }
    
    /**
     * 获取音乐地址
     *
     * @param path    音乐文件文件地址
     * @param refresh 是否刷新缓存
     * @param type    查询数据类型
     * @return 音乐地址
     */
    default String getResourceUrl(String path, boolean refresh, ResourceEnum type) {
        return this.getResource(path, refresh, type).getUrl();
    }
    
    /**
     * 获取音乐地址
     *
     * @param md5Set  音乐文件文件MD5
     * @param refresh 是否刷新缓存
     * @param type    查询数据类型
     * @return 音乐地址 key md5, value url, size
     */
    Map<String, Resource> getResourceByMd5ToMap(Collection<String> md5Set, boolean refresh, ResourceEnum type);
    
    /**
     * 获取音乐MD5值，为null获取所有md5
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @param type    查询数据类型
     * @return MD5值
     */
    default Resource getResourceByMd5(String md5, boolean refresh, ResourceEnum type) {
        Map<String, Resource> addressByMd5 = getResourceByMd5ToMap(Collections.singleton(md5), refresh, type);
        return MapUtil.get(addressByMd5, md5, Resource.class, new Resource());
    }
    
    /**
     * 获取音乐MD5值，为null获取所有md5
     *
     * @param md5     音乐的md5值
     * @param refresh 是否刷新缓存
     * @param type    查询数据类型
     * @return MD5值
     */
    default String getResourceUrlByMd5(String md5, boolean refresh, ResourceEnum type) {
        return this.getResourceByMd5(md5, refresh, type).getUrl();
    }
    
    /**
     * 上传文件返回地址
     *
     * @param paths   路径
     * @param index   选中上传路径
     * @param srcFile 上传文件
     * @param md5     上传文件md5 非必传, 没有则会读入问的md5
     * @param type    文件类型
     * @return 文件路径相对
     */
    String upload(List<String> paths, Integer index, File srcFile, String md5, ResourceEnum type);
    
    /**
     * 删除文件
     *
     * @param paths 文件名
     * @param type  查询数据类型
     */
    void delete(List<String> paths, ResourceEnum type);
    
    default void delete(String path, ResourceEnum type) {
        delete(Collections.singletonList(path), type);
    }
    
    /**
     * 重命名
     *
     * @param path    旧文件名
     * @param newName 新文件名
     * @param type    文件类型
     * @return 修改后的文件路径
     */
    String rename(String path, String newName, ResourceEnum type);
    
}

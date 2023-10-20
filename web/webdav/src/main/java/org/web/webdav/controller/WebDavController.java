package org.web.webdav.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import io.milton.annotations.*;
import io.milton.resource.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.api.webdav.service.WebdavApi;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.web.webdav.model.WebDavFolder;
import org.web.webdav.model.WebDavResource;

import java.io.InputStream;
import java.util.*;

// @ResourceController
@Component
@Slf4j
public class WebDavController {
    
    private static final String LIKE = "like";
    private static final String RECOMMEND = "recommend";
    private static final String COMMON = "common";
    
    @Autowired
    private WebdavApi webdavApi;
    
    @Nullable
    private static List<? extends Resource> getWebDavFolders(WebDavFolder webDavFolder, String type) {
        if (StringUtils.equals(webDavFolder.getUniqueId(), type)) {
            return webDavFolder.getFolders().parallelStream().map(resource -> new WebDavFolder(resource.getUniqueId(), resource.getName())).toList();
        }
        return null;
    }
    
    /**
     * 获取Webdav根目录
     *
     * @return this
     */
    @Root
    public CollectTypeList getRoot() {
        return webdavApi.getUserPlayList(464931079446661L);
    }
    
    @ChildrenOf
    public List<WebDavFolder> getWebDavFolders(CollectTypeList root) {
        List<WebDavFolder> list = new ArrayList<>();
        Collection<WebDavFolder> like = root.getLikeCollect()
                                            .parallelStream()
                                            .map(tbCollectPojo -> new WebDavFolder(Objects.toString(tbCollectPojo.getId()),
                                                    tbCollectPojo.getPlayListName()))
                                            .toList();
        
        Collection<WebDavFolder> ordinary = root.getOrdinaryCollect()
                                                .parallelStream()
                                                .map(tbCollectPojo -> new WebDavFolder(Objects.toString(tbCollectPojo.getId()),
                                                        tbCollectPojo.getPlayListName()))
                                                .toList();
        
        Collection<WebDavFolder> recommend = root.getRecommendCollect()
                                                 .parallelStream()
                                                 .map(tbCollectPojo -> new WebDavFolder(Objects.toString(tbCollectPojo.getId()),
                                                         tbCollectPojo.getPlayListName()))
                                                 .toList();
        String likeStr = "喜爱歌单";
        list.add(new WebDavFolder(LIKE, likeStr, like, null));
        String common = "普通歌单";
        list.add(new WebDavFolder(COMMON, common, ordinary, null));
        String recommendStr = "推荐歌单";
        list.add(new WebDavFolder(RECOMMEND, recommendStr, recommend, null));
        return list;
    }
    
    @ChildrenOf
    public List<? extends Resource> getWebDavFiles(WebDavFolder webDavFolder) {
        // 普通歌单
        List<? extends Resource> commonList = getWebDavFolders(webDavFolder, COMMON);
        if (commonList != null) {
            return commonList;
        }
        // 喜爱歌单
        List<? extends Resource> likeList = getWebDavFolders(webDavFolder, LIKE);
        if (likeList != null) {
            return likeList;
        }
        // 推荐歌单
        List<? extends Resource> recommendList = getWebDavFolders(webDavFolder, RECOMMEND);
        if (recommendList != null) {
            return recommendList;
        }
        
        ArrayList<Resource> resources = new ArrayList<>();
        List<PlayListRes> playListMusic = webdavApi.getPlayListMusic(Long.parseLong(webDavFolder.getUniqueId()));
        for (PlayListRes playListRes : playListMusic) {
            resources.add(new WebDavResource(playListRes.getMusicName() + "." + FileUtil.getSuffix(playListRes.getPath()),
                    playListRes.getMd5(),
                    playListRes.getPath(),
                    playListRes.getSize(),
                    playListRes.getResourceList()));
        }
        return resources;
    }
    
    @Get
    public InputStream getChild(WebDavResource webDavFolder) {
        String name = webDavFolder.getName();
        log.info("output file: {}", name);
        Map<String, Map<String, String>> address = SpringUtil.getBean(QukuAPI.class).getAddressByMd5(webDavFolder.getMd5(), false);
        String url = address.get(webDavFolder.getPath()).get("url");
        log.info("url download: {}", url);
        try (HttpResponse execute = HttpRequest.get(url).execute()) {
            return execute.bodyStream();
        } catch (HttpException e) {
            throw new BaseException(ResultCode.DOWNLOAD_ERROR);
        }
    }
    
    /**
     * 显示文件名 (必须唯一)
     *
     * @param webDavResource 资源数据
     * @return 文件名
     */
    @Name
    public String getWebDavResource(WebDavResource webDavResource) {
        return webDavResource.getName();
    }
    
    /**
     * 标记返回资源显示名称
     *
     * @param webDavResource 文件资源数据
     * @return 资源名称
     */
    @DisplayName
    public String getDisplayName(WebDavResource webDavResource) {
        return webDavResource.getName();
    }
    
    /**
     * 标记返回该资源的唯一标识符的方法。
     *
     * @param webDavResource 文件资源数据
     * @return 唯一标识
     */
    @UniqueId
    public String getUniqueId(WebDavResource webDavResource) {
        return webDavResource.getUniqueId();
    }
    
    /**
     * 文件修改时间
     *
     * @param webDavResource 文件资源数据
     * @return 文件修改时间
     */
    @ModifiedDate
    public Date getModifiedDate(WebDavResource webDavResource) {
        return webDavResource.getModifiedDate();
    }
    
    /**
     * 文件创建时间
     *
     * @param webDavResource 文件资源数据
     * @return 文件修改时间
     */
    @CreatedDate
    public Date getCreatedDate(WebDavResource webDavResource) {
        return webDavResource.getCreatedDate();
    }
    
}

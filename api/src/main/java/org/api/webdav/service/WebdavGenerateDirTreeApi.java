package org.api.webdav.service;

import cn.hutool.core.date.DateUtil;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.webdav.constant.WebdavCacheConstant;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.core.model.WebDavResource;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.oss.model.Resource;
import org.core.service.RemoteStorageService;
import org.core.utils.i18n.I18nUtil;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebdavGenerateDirTreeApi {
    
    private final Cache<String, WebDavResource> webdavCache;
    
    private final WebdavApi webdavApi;
    
    private final RemoteStorageService remoteStorageService;
    
    private final WebdavCacheApi webdavCacheApi;
    
    private static String i18LikeStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.like_playlist");
    }
    
    private static String i18RecommendStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.recommended_playlist");
    }
    
    private static String i18CommonStr() {
        return "/" + I18nUtil.getMsg("webdav.playlist.saved_playlist");
    }
    
    
    /**
     * 处理音乐名字符串, 防止音乐名出现敏感字符导致前端解析失败
     * 对字符串中有 <code>/</code> 和 <code>\</code> 的进行转移
     *
     * @return 处理后的字符串
     */
    private static String handleName(final String name) {
        final String replacePlaceholderCharacter = "-";
        final String o1 = StringUtils.replace(name, "/", replacePlaceholderCharacter);
        return StringUtils.replace(o1, "\\", replacePlaceholderCharacter);
    }
    
    /**
     * 处理歌单名中出现的各种敏感字符
     *
     * @param name 歌单名
     * @return 处理后的字符串
     */
    private static String handleCollectName(final String name) {
        final String replacePlaceholderCharacter = "-";
        final String o1 = StringUtils.replace(name, "/", replacePlaceholderCharacter);
        final String o2 = StringUtils.replace(o1, "\\", replacePlaceholderCharacter);
        return StringUtils.replace(o2, " ", "·");
    }
    
    @Cacheable(value = WebdavCacheConstant.WEBDAV_RESOURCE, key = "'path-' + #path + '-userId-' + #userId")
    public WebDavResource getResource(String path, Long userId) {
        if (Objects.isNull(userId)) {
            return null;
        }
        if (StringUtils.equals(path, "/")) {
            return handleRoot(userId);
        }
        // 刷新缓存
        if (StringUtils.equalsAnyIgnoreCase(path, WebdavCacheApi.i18RefreshStr())) {
            handleRoot(userId);
            webdavCacheApi.refreshAllCache();
            return webdavCache.asMap().get(path);
        }
        // 该数据中无缓存，尝试刷新该数据
        WebDavResource webDavResource = webdavCache.asMap().get(path);
        if (Objects.isNull(webDavResource)) {
            handleRoot(userId);
            webdavCacheApi.refreshAllCache();
            return webdavCache.asMap().get(path);
        }
        return webDavResource;
    }
    
    private WebDavResource handleRoot(Long userId) {
        List<WebDavResource> resource = new ArrayList<>();
        
        CollectTypeList userPlayList = webdavApi.getUserPlayList(userId);
        
        // 喜欢歌单
        String likeStr = I18nUtil.getMsg("webdav.playlist.like_playlist");
        WebDavResource likeResource = WebDavResource.createSelectCollect(likeStr, i18LikeStr());
        List<TbCollectPojo> likeCollect = userPlayList.getLikeCollect();
        fillCollectMusic(resource, likeCollect, likeResource, Objects.isNull(likeCollect), i18LikeStr());
        // 推荐歌单
        String recommendStr = I18nUtil.getMsg("webdav.playlist.recommended_playlist");
        WebDavResource recommendResource = WebDavResource.createSelectCollect(recommendStr, i18RecommendStr());
        List<TbCollectPojo> recommendCollect = userPlayList.getRecommendCollect();
        fillCollectMusic(resource, recommendCollect, recommendResource, Objects.isNull(recommendCollect), i18RecommendStr());
        
        // 收藏歌单
        String common = I18nUtil.getMsg("webdav.playlist.saved_playlist");
        WebDavResource commonResource = WebDavResource.createSelectCollect(common, i18CommonStr());
        List<TbCollectPojo> ordinaryCollect = userPlayList.getOrdinaryCollect();
        fillCollectMusic(resource, ordinaryCollect, commonResource, Objects.isNull(ordinaryCollect), i18CommonStr());
        
        // 刷新
        WebDavResource refreshResource = new WebDavResource();
        String refreshStr = I18nUtil.getMsg("webdav.playlist.refresh");
        refreshResource.setName(refreshStr);
        refreshResource.setPath(WebdavCacheApi.i18RefreshStr());
        refreshResource.setResource(new ArrayList<>());
        // 添加到缓存，否则读取不到
        webdavCache.put(WebdavCacheApi.i18RefreshStr(), refreshResource);
        resource.add(refreshResource);
        
        return WebDavResource.createRoot(resource);
    }
    
    private void fillCollectMusic(List<WebDavResource> resource, List<TbCollectPojo> collectIds, WebDavResource davResource, boolean b, String path) {
        if (Boolean.FALSE.equals(b)) {
            // 可能会有多个歌单
            ArrayList<WebDavResource> collectResources = new ArrayList<>();
            collectIds.parallelStream().filter(Objects::nonNull).forEach(collectPojo -> {
                List<PlayListRes> playListMusic = webdavApi.getPlayListMusic(collectPojo.getId());
                // 处理歌单名
                String playListName = handleCollectName(collectPojo.getPlayListName());
                
                List<WebDavResource> musicResources = new ArrayList<>(playListMusic.size());
                for (PlayListRes s : playListMusic) {
                    Resource musicResource = remoteStorageService.getMusicResource(s.getPath());
                    if (Objects.isNull(musicResource) || StringUtils.isBlank(musicResource.getPath()) || StringUtils.isBlank(musicResource.getUrl())) {
                        continue;
                    }
                    
                    // 在子目录下不需要填充父目录名,但是在缓存中需要填写父目录名。因为tomcat返回时会自动添加当前文件的父目录路径。
                    // 前端请求时也会自动使用绝对路径，所以缓存需要使用绝对路径
                    final String aliasName = StringUtils.isBlank(s.getAliasName()) ? "" : ' ' + s.getAliasName();
                    final String artistName = StringUtils.isBlank(s.getArtistName()) ? "" : '-' + s.getArtistName();
                    String resourcePathStr = "/%s%s%s-%s.%s".formatted(handleName(s.getMusicName()),
                            aliasName,
                            artistName,
                            s.getRate(),
                            musicResource.getFileExtension());
                    WebDavResource r = WebDavResource.createFile(s.getMusicName(),
                            s.getMd5(),
                            resourcePathStr,
                            s.getPath(),
                            musicResource.getUrl(),
                            DateUtil.date(s.getCreateTime()),
                            DateUtil.date(s.getUpdateTime()),
                            musicResource.getSize());
                    // 缓存音乐填充全路径, 根目录/歌单名/音乐名
                    webdavCache.put(path + "/" + playListName + resourcePathStr, r);
                    musicResources.add(r);
                }
                // 歌单创建并添加音乐
                WebDavResource collectResource = WebDavResource.createCollect(playListName,
                        "/" + playListName,
                        DateUtil.date(collectPojo.getCreateTime()),
                        DateUtil.date(collectPojo.getUpdateTime()),
                        musicResources);
                // 添加歌单
                collectResources.add(collectResource);
                // 填充缓存 路径绝对路径
                webdavCache.put(path + "/" + playListName, collectResource);
            });
            davResource.setResource(collectResources);
        }
        resource.add(davResource);
        webdavCache.put(davResource.getPath(), davResource);
    }
}

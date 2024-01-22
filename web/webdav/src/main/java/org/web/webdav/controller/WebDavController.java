package org.web.webdav.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.github.benmanes.caffeine.cache.Cache;
import io.milton.annotations.*;
import io.milton.http.Auth;
import io.milton.http.HttpManager;
import io.milton.http.Request;
import io.milton.http.http11.auth.DigestGenerator;
import io.milton.http.http11.auth.DigestResponse;
import io.milton.resource.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.webdav.model.CollectTypeList;
import org.api.webdav.model.PlayListRes;
import org.api.webdav.service.WebdavApi;
import org.api.webdav.utils.spring.WebdavResourceReturnStrategyUtil;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.jpa.entity.TbResourceEntity;
import org.core.mybatis.pojo.SysUserPojo;
import org.core.service.RemoteStorageService;
import org.core.utils.i18n.I18nUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.web.webdav.model.WebDavFolder;
import org.web.webdav.model.WebDavResource;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.*;

// @ResourceController
@Component
@Slf4j
public class WebDavController {
    
    private static final String LIKE = "like";
    private static final String RECOMMEND = "recommend";
    private static final String COMMON = "common";
    private static final String REFRESH = "refresh";
    
    private final Cache<String, String> resource;
    
    private final WebdavApi webdavApi;
    
    private final RemoteStorageService remoteStorageService;
    
    private final WebdavResourceReturnStrategyUtil webdavResourceReturnStrategyUtil;
    
    public WebDavController(Cache<String, String> resource, WebdavApi webdavApi, WebdavResourceReturnStrategyUtil webdavResourceReturnStrategyUtil, RemoteStorageService remoteStorageService) {
        this.resource = resource;
        this.webdavApi = webdavApi;
        this.webdavResourceReturnStrategyUtil = webdavResourceReturnStrategyUtil;
        this.remoteStorageService = remoteStorageService;
    }
    
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
        Request request = HttpManager.request();
        Auth authorization = request.getAuthorization();
        if (Objects.isNull(authorization)) {
            return new CollectTypeList();
        }
        if (Objects.equals(Auth.Scheme.BASIC, authorization.getScheme())) {
            String userId = authorization.getUser();
            String password = authorization.getPassword();
            SysUserPojo userByName = webdavApi.getUserByName(userId);
            if (StringUtils.equals(userByName.getPassword(), password)) {
                return webdavApi.getUserPlayList(userByName.getId());
            }
        }
        if (Objects.equals(Auth.Scheme.DIGEST, authorization.getScheme())) {
            DigestResponse digestResponse = new DigestResponse(request.getMethod(),
                    authorization.getUser(),
                    authorization.getRealm(),
                    authorization.getNonce(),
                    authorization.getUri(),
                    authorization.getResponseDigest(),
                    authorization.getQop(),
                    authorization.getNc(),
                    authorization.getCnonce()
            );
            DigestGenerator digestGenerator = new DigestGenerator();
            String user = authorization.getUser();
            SysUserPojo userByName = webdavApi.getUserByName(user);
            if (Objects.isNull(userByName)) {
                return null;
            }
            String serverResponse = digestGenerator.generateDigest(digestResponse, userByName.getPassword());
            String clientResponse = authorization.getResponseDigest();
            
            if (serverResponse.equals(clientResponse)) {
                return webdavApi.getUserPlayList(userByName.getId());
            } else {
                throw new BaseException(ResultCode.USER_NOT_EXIST);
            }
        }
        throw new BaseException(ResultCode.USER_NOT_EXIST);
    }
    
    @ChildrenOf
    public List<WebDavFolder> getWebDavFolders(CollectTypeList root) {
        if (CollUtil.isEmpty(root.getLikeCollect()) && CollUtil.isEmpty(root.getRecommendCollect()) && CollUtil.isEmpty(root.getOrdinaryCollect())) {
            return Collections.emptyList();
        }
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
        String likeStr = I18nUtil.getMsg("webdav.playlist.like_playlist");
        list.add(new WebDavFolder(LIKE, likeStr, like, null));
        String common = I18nUtil.getMsg("webdav.playlist.saved_playlist");
        list.add(new WebDavFolder(COMMON, common, ordinary, null));
        String recommendStr = I18nUtil.getMsg("webdav.playlist.recommended_playlist");
        list.add(new WebDavFolder(RECOMMEND, recommendStr, recommend, null));
        String refreshStr = I18nUtil.getMsg("webdav.playlist.refresh");
        list.add(new WebDavFolder(REFRESH, refreshStr, Collections.emptyList(), null));
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
        if (StringUtils.equals(webDavFolder.getUniqueId(), REFRESH)) {
            webdavApi.refreshAllCache();
            return Collections.emptyList();
        }
        ArrayList<Resource> resources = new ArrayList<>();
        List<PlayListRes> playListMusic = webdavApi.getPlayListMusic(Long.parseLong(webDavFolder.getUniqueId()));
        for (PlayListRes playListRes : playListMusic) {
            // 修复路径读取问题,使用/会造成路径读取问题
            // 使用字符 - 替换 /
            String replace = StringUtils.replace(playListRes.getMusicName(), "/", "-");
            String format = MessageFormat.format("{0}{1}",
                    replace,
                    StringUtils.isBlank(playListRes.getAliasName()) ? "" : " (" + playListRes.getAliasName() + ")");
            String musicName = format + "." + FileUtil.getSuffix(playListRes.getPath());
            
            TbResourceEntity tbResourceEntity = webdavResourceReturnStrategyUtil.handleResourceEntity(ListUtil.toList(playListRes.getTbResourcesById()));
            resource.put(musicName, remoteStorageService.getMusicResourceUrl(tbResourceEntity.getPath(), false));
            resources.add(new WebDavResource(musicName,
                    playListRes.getMd5(),
                    playListRes.getPath(),
                    playListRes.getSize(),
                    ListUtil.toList(playListRes.getTbResourcesById())));
        }
        return resources;
    }
    
    @Get
    public InputStream getChild(WebDavResource webDavFolder) {
        String name = webDavFolder.getName();
        log.info("output file: {}", name);
        Map<String, org.core.oss.model.Resource> address = remoteStorageService.getMusicResourceByMd5(webDavFolder.getMd5(), false);
        String url = address.get(webDavFolder.getPath()).getUrl();
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

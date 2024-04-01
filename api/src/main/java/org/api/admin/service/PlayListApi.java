package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageCommon;
import org.api.admin.model.req.MusicPageReq;
import org.api.admin.model.req.PlayListReq;
import org.api.admin.model.req.UpdatePlayListReq;
import org.api.admin.model.res.CollectInfoRes;
import org.api.admin.model.res.MusicPageRes;
import org.api.admin.model.res.PlayListMusicRes;
import org.api.admin.model.res.PlayListRes;
import org.api.admin.model.res.router.Children;
import org.api.admin.model.res.router.Meta;
import org.api.admin.model.res.router.RouterVo;
import org.api.admin.utils.MyPageUtil;
import org.api.common.service.QukuAPI;
import org.core.common.constant.PlayListTypeConstant;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.RemoteStorePicService;
import org.core.service.TagManagerService;
import org.core.utils.ExceptionUtil;
import org.core.utils.UserUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "PlayListApi")
public class PlayListApi {
    
    /**
     * 音乐表
     */
    private final TbMusicService musicService;
    
    private final QukuAPI qukuService;
    
    /**
     * 专辑表
     */
    private final TbAlbumService albumService;
    
    private final TbCollectService collectService;
    
    /**
     * 歌单与音乐中间表
     */
    private final TbCollectMusicService collectMusicService;
    
    private final PlayListService playListService;
    
    private final TbResourceService musicUrlService;
    
    private final DefaultInfo defaultInfo;
    
    private final RemoteStorePicService remoteStorePicService;
    
    private final AccountService accountService;
    
    private final TagManagerService tagManagerService;
    
    public PlayListApi(TbMusicService musicService, QukuAPI qukuService, TbAlbumService albumService, TbCollectService collectService, TbCollectMusicService collectMusicService, PlayListService playListService, TbResourceService musicUrlService, DefaultInfo defaultInfo, RemoteStorePicService remoteStorePicService, AccountService accountService, TagManagerService tagManagerService) {
        this.musicService = musicService;
        this.qukuService = qukuService;
        this.albumService = albumService;
        this.collectService = collectService;
        this.collectMusicService = collectMusicService;
        this.playListService = playListService;
        this.musicUrlService = musicUrlService;
        this.defaultInfo = defaultInfo;
        this.remoteStorePicService = remoteStorePicService;
        this.accountService = accountService;
        this.tagManagerService = tagManagerService;
    }
    
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<TbMusicPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id" -> musicWrapper.orderBy(true, order, TbMusicPojo::getId);
            case "updateTime" -> musicWrapper.orderBy(true, order, TbMusicPojo::getUpdateTime);
            case "createTime" -> musicWrapper.orderBy(true, order, TbMusicPojo::getCreateTime);
            default -> musicWrapper.orderBy(true, order, TbMusicPojo::getUpdateTime);
        }
    }
    
    
    public Page<PlayListMusicRes> getAllMusic(String playId, MusicPageReq page) {
        page = Optional.ofNullable(page).orElse(new MusicPageReq());
        PageCommon other = new PageCommon();
        other.setPageIndex(0);
        other.setPageNum(10);
        page.setPage(Optional.ofNullable(page.getPage()).orElse(other));
    
        List<Long> musicIdList = new ArrayList<>();
        boolean artistNameFlag = StringUtils.isNotBlank(page.getArtistName());
        if (artistNameFlag) {
            // 查询歌手表
            musicIdList.addAll(getMusicIDByArtistName(page.getArtistName()));
        }
        boolean albumNameFlag = StringUtils.isNotBlank(page.getAlbumName());
        if (albumNameFlag) {
            // 查询专辑表
            musicIdList.addAll(getMusicIDByAlbumName(page.getAlbumName()));
        }
        boolean musicNameFlag = CollUtil.isNotEmpty(page.getMusicIds()) || StringUtils.isNotBlank(page.getMusicName());
        if (musicNameFlag) {
            // 查询歌曲名
            musicIdList.addAll(getMusicIdByMusicName(page.getMusicName(), page.getMusicIds()));
        }
        // 如果搜索值有效，并且无查询数据则返回空
        if ((artistNameFlag || albumNameFlag || musicNameFlag) && CollUtil.isEmpty(musicIdList)) {
            return new Page<>(1L, 10L, 0L);
        }
    
    
        Page<TbCollectMusicPojo> playListMusicResPage = new Page<>(page.getPage().getPageIndex(), page.getPage().getPageNum());
        LambdaQueryWrapper<TbCollectMusicPojo> collectMusicWrapper = Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                             .eq(TbCollectMusicPojo::getCollectId, playId)
                                                                             .in(CollUtil.isNotEmpty(musicIdList),
                                                                                     TbCollectMusicPojo::getMusicId,
                                                                                     musicIdList)
                                                                             .orderByDesc(TbCollectMusicPojo::getSort);
        collectMusicService.page(playListMusicResPage, collectMusicWrapper);
        if (CollUtil.isEmpty(playListMusicResPage.getRecords())) {
            return new Page<>(1L, 10L, 0L);
        }
        List<Long> musicIds = playListMusicResPage.getRecords().stream().map(TbCollectMusicPojo::getMusicId).toList();
    
        List<TbMusicPojo> musicPojoList = musicService.listByIds(musicIds);
        Set<Long> albumIds = musicPojoList.stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
        List<TbAlbumPojo> albumPojoList = albumService.listByIds(albumIds);
        Map<Long, TbAlbumPojo> albumPojoMap = albumPojoList.stream().collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        
        Map<Long, List<ArtistConvert>> artistMaps = qukuService.getArtistByMusicIdToMap(musicIds);
        ArrayList<PlayListMusicRes> playListMusicRes = new ArrayList<>();
        List<MusicConvert> picMusicList = qukuService.getPicMusicList(musicPojoList);
        for (MusicConvert tbMusicPojo : picMusicList) {
            PlayListMusicRes e1 = new PlayListMusicRes();
            BeanUtils.copyProperties(tbMusicPojo, e1, "lyric");
            
            TbAlbumPojo tbAlbumPojo = albumPojoMap.get(tbMusicPojo.getAlbumId());
            if (Objects.nonNull(tbAlbumPojo)) {
                e1.setAlbum(new PlayListMusicRes.PlayListAlbum(tbAlbumPojo.getId(), tbAlbumPojo.getAlbumName()));
            }
            
            List<ArtistConvert> tbArtistPojos = artistMaps.get(tbMusicPojo.getId());
            if (CollUtil.isNotEmpty(tbArtistPojos)) {
                e1.setArtists(tbArtistPojos.stream()
                                           .map(s -> new PlayListMusicRes.PlayListArtist(s.getId(), s.getArtistName(), s.getAliasName()))
                                           .toList());
            }
            
            playListMusicRes.add(e1);
        }
        // 匹配歌单歌曲顺序
        ArrayList<PlayListMusicRes> res = new ArrayList<>();
        for (Long musicId : musicIds) {
            Optional<PlayListMusicRes> first = playListMusicRes.parallelStream()
                                                               .filter(playListMusicRes1 -> Objects.equals(playListMusicRes1.getId(), musicId))
                                                               .findFirst();
            first.ifPresent(res::add);
        }
        Page<PlayListMusicRes> resPage = new Page<>();
        BeanUtils.copyProperties(playListMusicResPage, resPage);
        resPage.setRecords(res);
        return resPage;
    }
    
    /**
     * 获取音乐基本信息
     */
    public Page<MusicPageRes> getMusicPage(MusicPageReq req) {
        req.setMusicName(StringUtils.trim(req.getMusicName()));
        req.setAlbumName(StringUtils.trim(req.getAlbumName()));
        req.setArtistName(StringUtils.trim(req.getArtistName()));
    
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        List<Long> musicIdList = new LinkedList<>();
    
        // 查询歌手表
        musicIdList.addAll(getMusicIDByArtistName(req.getArtistName()));
        // 查询专辑表
        musicIdList.addAll(getMusicIDByAlbumName(req.getAlbumName()));
        // 查询歌曲名
        musicIdList.addAll(getMusicIdByMusicName(req.getMusicName(), req.getMusicIds()));
    
        // 如果前端传入搜索参数，并且没有查询到数据则直接返回空数据库。防止搜索结果混乱
        if ((StringUtils.isNotBlank(req.getMusicName())
                || StringUtils.isNotBlank(req.getAlbumName())
                || StringUtils.isNotBlank(req.getArtistName()))
                && CollUtil.isEmpty(musicIdList)) {
            return new Page<>(0, 50, 0);
        }
    
    
        Page<MusicConvert> page = getPageFilter(req, musicIdList, new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum()));
        if (page == null) {
            return new Page<>(0, 50, 0);
        }
    
        // 专辑信息
        List<Long> albumIds = page.getRecords().stream().map(TbMusicPojo::getAlbumId).toList();
        Map<Long, TbAlbumPojo> albumMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumIds)) {
            albumMap = albumService.listByIds(albumIds)
                                   .stream()
                                   .collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        }
    
        List<Long> musicIds = page.getRecords().parallelStream().map(TbMusicPojo::getId).toList();
        // 歌手信息
        Map<Long, List<ArtistConvert>> musicArtistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicIds);
        // 填充信息
        Map<Long, TbResourcePojo> urlPojoMap = new HashMap<>();
        // 获取音乐地址
        try {
            List<TbResourcePojo> musicUrlByMusicId = qukuService.getMusicUrlByMusicId(musicIds, Boolean.TRUE.equals(req.getRefresh()));
            urlPojoMap = musicUrlByMusicId.parallelStream()
                                          .collect(Collectors.toMap(TbResourcePojo::getMusicId,
                                                  Function.identity(),
                                                  (musicUrlPojo, musicUrlPojo2) -> musicUrlPojo2));
        } catch (BaseException ex) {
            if (!StringUtils.equals(ex.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(ResultCode.SONG_NOT_EXIST);
            }
        }
        
        List<Long> likeMusic = new ArrayList<>();
        List<CollectConvert> userPlayList = qukuService.getUserPlayList(UserUtil.getUser().getId(), Collections.singletonList(PlayListTypeConstant.LIKE));
        if (CollUtil.isNotEmpty(userPlayList) && userPlayList.size() == 1) {
            List<TbCollectMusicPojo> list = collectMusicService.list(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                             .eq(TbCollectMusicPojo::getCollectId, userPlayList.get(0).getId()));
            likeMusic.addAll(list.parallelStream().map(TbCollectMusicPojo::getMusicId).toList());
        }
        List<MusicPageRes> musicPageRes = new ArrayList<>();
        for (MusicConvert musicPojo : page.getRecords()) {
            MusicPageRes e = new MusicPageRes();
            e.setId(musicPojo.getId());
            e.setMusicName(musicPojo.getMusicName());
            e.setMusicNameAlias(musicPojo.getAliasName());
            e.setPic(musicPojo.getPicUrl());
            TbResourcePojo tbMusicUrlPojo = Optional.ofNullable(urlPojoMap.get(musicPojo.getId())).orElse(new TbResourcePojo());
            e.setIsExist(StringUtils.isNotBlank(tbMusicUrlPojo.getPath()));
            e.setMusicRawUrl(tbMusicUrlPojo.getPath());
    
            e.setIsLike(likeMusic.contains(musicPojo.getId()));
    
            // 专辑
            TbAlbumPojo tbAlbumPojo = Optional.ofNullable(albumMap.get(musicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            e.setAlbumId(tbAlbumPojo.getId());
            e.setAlbumName(tbAlbumPojo.getAlbumName());
            e.setPublishTime(tbAlbumPojo.getPublishTime());
    
            // 歌手
            List<ArtistConvert> tbArtistPojos = Optional.ofNullable(musicArtistByMusicIdToMap.get(musicPojo.getId())).orElse(new ArrayList<>());
            e.setArtistIds(new ArrayList<>());
            e.setArtistNames(new ArrayList<>());
            for (TbArtistPojo tbArtistPojo : tbArtistPojos) {
                e.getArtistIds().add(tbArtistPojo.getId());
                e.getArtistNames().add(tbArtistPojo.getArtistName());
            }
            e.setTimeLength(musicPojo.getTimeLength());
            e.setCreateTime(musicPojo.getCreateTime());
            e.setOrder(req.getOrder());
            musicPageRes.add(e);
        }
        Page<MusicPageRes> pageVo = new Page<>();
        BeanUtils.copyProperties(page, pageVo);
        pageVo.setRecords(musicPageRes);
        return pageVo;
    }
    
    @Nullable
    private Page<MusicConvert> getPageFilter(MusicPageReq req, List<Long> musicIdList, Page<TbMusicPojo> page) {
        LambdaQueryWrapper<TbMusicPojo> wrapper;
        if (Boolean.TRUE.equals(req.getIsShowNoExist())) {
            // 无音源音乐
            Collection<Long> union;
            List<Long> collect = new ArrayList<>(musicService.list().parallelStream().map(TbMusicPojo::getId).toList());
            List<TbResourcePojo> musicUrlPojos = musicUrlService.list();
            List<Long> collect1 = musicUrlPojos.parallelStream().map(TbResourcePojo::getMusicId).toList();
            // 数据库中没有音源数据
            collect.removeAll(collect1);
            // 确认OSS音源存在
            List<TbResourcePojo> urls = qukuService.getMusicUrlByMusicUrlList(musicUrlPojos, false);
            List<Long> noMusicSource = urls.parallelStream()
                                           .filter(musicUrlPojo -> StringUtils.isEmpty(musicUrlPojo.getPath()))
                                           .map(TbResourcePojo::getMusicId)
                                           .toList();
            // 是否都是无音源音乐
            union = CollUtil.union(noMusicSource, collect);
            if (CollUtil.isEmpty(union)) {
                return null;
            }
            wrapper = Wrappers.<TbMusicPojo>lambdaQuery()
                              .in(TbMusicPojo::getId, union);
        } else {
            wrapper = Wrappers.<TbMusicPojo>lambdaQuery()
                              .in(CollUtil.isNotEmpty(musicIdList), TbMusicPojo::getId, musicIdList);
        }
        pageOrderBy(req.getOrder(), req.getOrderBy(), wrapper);
        musicService.page(page, wrapper);
        List<MusicConvert> converts = page.getRecords().stream().map(tbMusicPojo -> {
            MusicConvert convert = new MusicConvert();
            BeanUtils.copyProperties(tbMusicPojo, convert);
            convert.setPicUrl(remoteStorePicService.getMusicPicUrl(tbMusicPojo.getId()));
            return convert;
        }).toList();
        Page<MusicConvert> convertPage = new Page<>();
        BeanUtils.copyProperties(page, convertPage);
        convertPage.setRecords(converts);
        return convertPage;
    }
    
    /**
     * 根据歌曲名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIdByMusicName(String musicName, Collection<Long> musicIds) {
        if (StringUtils.isBlank(musicName) && CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        List<TbMusicPojo> list = new ArrayList<>();
        // 音乐名
        list.addAll(musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                              .in(CollUtil.isNotEmpty(musicIds), TbMusicPojo::getId, musicIds)
                                              .like(StringUtils.isNotBlank(musicName), TbMusicPojo::getMusicName, musicName)));
        // 别名
        list.addAll(musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                              .in(CollUtil.isNotEmpty(musicIds), TbMusicPojo::getId, musicIds)
                                              .like(StringUtils.isNotBlank(musicName), TbMusicPojo::getAliasName, musicName)));
    
        return list.stream().map(TbMusicPojo::getId).distinct().toList();
    }
    
    private List<Long> getMusicIDByAlbumName(String albumName) {
        if (StringUtils.isBlank(albumName)) {
            return Collections.emptyList();
        }
        // 获取专辑ID
        LambdaQueryWrapper<TbAlbumPojo> like = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                       .like(TbAlbumPojo::getAlbumName, albumName);
        List<TbAlbumPojo> albumList = albumService.list(like);
        if (CollUtil.isEmpty(albumList)) {
            return Collections.emptyList();
        }
        List<Long> collect = albumList.stream().map(TbAlbumPojo::getId).toList();
        // 获取歌曲ID
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, collect));
        return list.stream().map(TbMusicPojo::getId).toList();
    }
    
    /**
     * 查询歌手名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIDByArtistName(String artistName) {
        List<MusicConvert> musicListBySingerName = qukuService.getMusicByArtistName(artistName);
        if (CollUtil.isEmpty(musicListBySingerName)) {
            return Collections.emptyList();
        }
        return musicListBySingerName.stream().map(TbMusicPojo::getId).toList();
    }
    
    /**
     * 获取歌单动态路由
     *
     * @param uid 用户ID
     * @return 动态路由歌单数据
     */
    public List<RouterVo> getAsyncPlayListRoutes(Long uid) {
        List<RouterVo> routerVos = new ArrayList<>();
        // 标题icon
        Meta meta = new Meta();
        meta.setTitle("menus.playList");
        meta.setIcon("solar:playlist-2-bold");
        meta.setRank(3);
        // 标题路由
        RouterVo routerVo = new RouterVo();
        routerVo.setName("PlayListVew");
        routerVo.setPath("/playlist");
        routerVo.setMeta(meta);
    
        Collection<CollectConvert> list = qukuService.getUserPlayList(uid, Collections.emptyList());
    
        // 子页面路由，(歌单)
        List<Children> children = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : list) {
            Children e = new Children();
            e.setName(String.valueOf(tbCollectPojo.getId()));
            String jumpRoute = String.format("/playlist/%s", tbCollectPojo.getId());
            e.setPath(jumpRoute);
            e.setComponent("/src/views/playlist/index.vue");
    
            Meta playListMeta = new Meta();
            // 歌单icon，包括歌单名
            playListMeta.setTitle(tbCollectPojo.getPlayListName());
            // 普通歌单
            if (tbCollectPojo.getType() == 0) {
                playListMeta.setIcon("solar:playlist-2-bold");
            }
            // 喜爱歌单
            if (tbCollectPojo.getType() == 1) {
                playListMeta.setIcon("heartBold");
            }
            // 推荐歌单
            if (tbCollectPojo.getType() == 2) {
                playListMeta.setIcon("plainBoldDuotone");
            }
    
            e.setMeta(playListMeta);
            children.add(e);
        }
    
        routerVo.setChildren(children);
    
        routerVos.add(routerVo);
        return routerVos;
    }
    
    public CollectInfoRes getPlayListInfo(Long id) {
        TbCollectPojo byId = collectService.getById(id);
        List<TbTagPojo> labelCollectTag = tagManagerService.getLabelCollectTag(id).get(id);
        CollectInfoRes collectInfoRes = new CollectInfoRes();
        BeanUtils.copyProperties(byId, collectInfoRes);
        collectInfoRes.setCollectTag(labelCollectTag.parallelStream().map(TbTagPojo::getTagName).toList());
        String picUrl = remoteStorePicService.getCollectPicUrl(byId.getId());
        collectInfoRes.setPicUrl(StringUtils.isBlank(picUrl) ? defaultInfo.getPic().getDefaultPic() : picUrl);
        
        if (Objects.nonNull(byId.getUserId())) {
            SysUserPojo byId1 = accountService.getById(byId.getUserId());
            collectInfoRes.setNickname(byId1.getNickname());
        }
        return collectInfoRes;
    }
    
    public TbCollectPojo createPlayList(String name) {
        return qukuService.createPlayList(UserUtil.getUser().getId(), name, PlayListTypeConstant.ORDINARY);
    }
    
    public void deletePlayList(Long userId, List<Long> id) {
        qukuService.removePlayList(userId, id);
    }
    
    public List<PlayListRes> getUserPlayList(Long userId) {
        List<CollectConvert> userPlayList = qukuService.getUserPlayList(userId,
                Arrays.asList(PlayListTypeConstant.LIKE, PlayListTypeConstant.ORDINARY, PlayListTypeConstant.RECOMMEND));
        List<PlayListRes> playListRes = new ArrayList<>();
        collectFillUpCount(userPlayList, playListRes);
        return playListRes;
    }
    
    private void collectFillUpCount(List<CollectConvert> userPlayList, List<PlayListRes> playListRes) {
        for (TbCollectPojo collectPojo : userPlayList) {
            PlayListRes e = new PlayListRes();
            Integer collectMusicCount = qukuService.getCollectMusicCount(collectPojo.getId());
            BeanUtils.copyProperties(collectPojo, e);
            e.setCount(collectMusicCount);
            playListRes.add(e);
        }
    }
    
    /**
     * 添加或删除音乐到歌单
     *
     * @param userId 用户ID
     * @param pid    歌单ID
     * @param id     音乐ID
     * @param flag   添加或删除 true 添加 false 删除
     */
    public void addMusicToPlayList(Long userId, Long pid, List<Long> id, Boolean flag) {
        TbCollectPojo byId = collectService.getById(pid);
        ExceptionUtil.isNull(byId == null, ResultCode.PLAY_LIST_LIKE);
        qukuService.addOrRemoveMusicToCollect(userId, byId.getId(), id, flag);
    }
    
    public Page<PlayListRes> getPlayListPage(PlayListReq req) {
        req.setPlayListName(StringUtils.trim(req.getPlayListName()));
    
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        Page<TbCollectPojo> playList = playListService.getPlayList(req,
                req.getPage().getPageIndex().longValue(),
                req.getPage().getPageNum().longValue(),
                req.getType());
    
        List<CollectConvert> converts = playList.getRecords().stream().map(collectPojo -> {
            CollectConvert convert = new CollectConvert();
            BeanUtils.copyProperties(collectPojo, convert);
            convert.setPicUrl(remoteStorePicService.getCollectPicUrl(collectPojo.getId()));
            return convert;
        }).toList();
    
        List<PlayListRes> playListRes = new ArrayList<>();
        collectFillUpCount(converts, playListRes);
    
        Page<PlayListRes> playListResPage = new Page<>();
        BeanUtils.copyProperties(playList, playListResPage);
        playListResPage.setRecords(playListRes);
        return playListResPage;
    }
    
    public TbCollectPojo updatePlayListInfo(UpdatePlayListReq req) {
        if (req.getId() == null) {
            throw new BaseException(ResultCode.PARAM_IS_INVALID);
        }
        Byte type = req.getType();
        // 只允许用户只有一个喜爱歌单
        if (type == 1) {
            Long userId = UserUtil.getUser().getId();
            LambdaQueryWrapper<TbCollectPojo> eq = Wrappers.<TbCollectPojo>lambdaQuery()
                                                           .eq(TbCollectPojo::getUserId, userId)
                                                           .eq(TbCollectPojo::getType, type);
            TbCollectPojo one = collectService.getOne(eq);
            // 如果不是相同歌单，则不允许修改
            if (one != null && !Objects.equals(one.getId(), req.getId())) {
                throw new BaseException(ResultCode.USER_LOVE_ERROR);
            }
        }
        
        TbCollectPojo entity = new TbCollectPojo();
        entity.setId(req.getId());
        entity.setPlayListName(req.getPlayListName());
        entity.setDescription(req.getDescription());
        entity.setType(req.getType());
        
        collectService.saveOrUpdate(entity);
        if (CollUtil.isNotEmpty(req.getCollectTag())) {
            tagManagerService.addCollectLabel(req.getId(), req.getCollectTag().stream().map(StringUtils::trim).toList());
        }
        return collectService.getById(req.getId());
    }
    
    public void like(Long userId, Long id, boolean add) {
        qukuService.collectLike(userId, id, add);
    }
}

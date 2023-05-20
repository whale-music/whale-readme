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
import org.api.admin.model.res.MusicPageRes;
import org.api.admin.model.res.PlayListMusicRes;
import org.api.admin.model.res.PlayListRes;
import org.api.admin.model.res.router.Children;
import org.api.admin.model.res.router.Meta;
import org.api.admin.model.res.router.RouterVo;
import org.api.admin.utils.MyPageUtil;
import org.api.common.service.MusicCommonApi;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.PlayListTypeConfig;
import org.core.iservice.*;
import org.core.pojo.*;
import org.core.service.PlayListService;
import org.core.service.QukuService;
import org.core.utils.ExceptionUtil;
import org.core.utils.UserUtil;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "PlayListApi")
public class PlayListApi {
    
    /**
     * 音乐表
     */
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 专辑表
     */
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbCollectService collectService;
    
    /**
     * 歌单与音乐中间表
     */
    @Autowired
    private TbCollectMusicService collectMusicService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private TbMusicUrlService musicUrlService;
    
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<TbMusicPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, TbMusicPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, TbMusicPojo::getUpdateTime);
                break;
            case "createTime":
                musicWrapper.orderBy(true, order, TbMusicPojo::getCreateTime);
                break;
            case "sort":
            default:
                musicWrapper.orderBy(true, order, TbMusicPojo::getSort);
        }
    }
    
    
    public Page<PlayListMusicRes> getAllMusic(String playId, MusicPageReq page) {
        page = Optional.ofNullable(page).orElse(new MusicPageReq());
        PageCommon other = new PageCommon();
        other.setPageIndex(0);
        other.setPageNum(10);
        page.setPage(Optional.ofNullable(page.getPage()).orElse(other));
        
        Page<TbCollectMusicPojo> playListMusicResPage = new Page<>(page.getPage().getPageIndex(), page.getPage().getPageNum());
        ArrayList<PlayListMusicRes> playListMusicRes = new ArrayList<>();
        collectMusicService.page(playListMusicResPage,
                Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, playId).orderByDesc(TbCollectMusicPojo::getSort));
        if (CollUtil.isEmpty(playListMusicResPage.getRecords())) {
            return new Page<>();
        }
        List<Long> musicIds = playListMusicResPage.getRecords().stream().map(TbCollectMusicPojo::getMusicId).collect(Collectors.toList());
        
        LambdaQueryWrapper<TbMusicPojo> like = Wrappers.<TbMusicPojo>lambdaQuery()
                                                       .in(CollUtil.isNotEmpty(musicIds), TbMusicPojo::getId, musicIds)
                                                       .like(StringUtils.isNotBlank(page.getMusicName()), TbMusicPojo::getMusicName, page.getMusicName())
                                                       .like(StringUtils.isNotBlank(page.getMusicName()), TbMusicPojo::getAliasName, page.getMusicName());
        List<TbMusicPojo> musicPojoList = musicService.list(like);
        Set<Long> albumIds = musicPojoList.stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
        List<TbAlbumPojo> albumPojoList = albumService.listByIds(albumIds);
        Map<Long, TbAlbumPojo> albumPojoMap = albumPojoList.stream().collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        
        HashMap<Long, TbAlbumPojo> map = new HashMap<>();
        musicPojoList.parallelStream().forEach(tbMusicPojo -> {
            if (albumPojoMap.get(tbMusicPojo.getAlbumId()) != null) {
                map.put(tbMusicPojo.getId(), albumPojoMap.get(tbMusicPojo.getAlbumId()));
            }
        });
        Map<Long, List<TbArtistPojo>> artistMaps = qukuService.getAlbumArtistListByMusicIdToMap(map);
        for (TbMusicPojo tbMusicPojo : musicPojoList) {
            PlayListMusicRes e1 = new PlayListMusicRes();
            BeanUtils.copyProperties(tbMusicPojo, e1, "lyric");
    
            TbAlbumPojo tbAlbumPojo = Optional.ofNullable(albumPojoMap.get(tbMusicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            tbAlbumPojo.setDescription("");
            e1.setAlbum(tbAlbumPojo);
    
            List<TbArtistPojo> tbArtistPojos = artistMaps.get(tbMusicPojo.getId());
            e1.setArtists(tbArtistPojos);
    
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
        musicIdList.addAll(getMusicIDByArtistName(req));
        // 查询专辑表
        musicIdList.addAll(getMusicIDByAlbumName(req));
        // 查询歌曲名
        musicIdList.addAll(getMusicIdByMusicName(req));
    
        // 如果前端传入搜索参数，并且没有查询到数据则直接返回空数据库。防止搜索结果混乱
        if ((StringUtils.isNotBlank(req.getMusicName())
                || StringUtils.isNotBlank(req.getAlbumName())
                || StringUtils.isNotBlank(req.getArtistName()))
                && CollUtil.isEmpty(musicIdList)) {
            return new Page<>(0, 50, 0);
        }
    
        Page<TbMusicPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        page = getPageFilter(req, musicIdList, page);
        if (page == null) {
            return new Page<>(0, 50, 0);
        }
    
        // 专辑信息
        List<Long> albumIds = page.getRecords().stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toList());
        Map<Long, TbAlbumPojo> albumMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumIds)) {
            albumMap = albumService.listByIds(albumIds)
                                   .stream()
                                   .collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        }
    
        List<Long> musicIds = page.getRecords().parallelStream().map(TbMusicPojo::getId).collect(Collectors.toList());
        // 歌手信息
        Map<Long, List<TbArtistPojo>> musicArtistByMusicIdToMap = qukuService.getMusicArtistByMusicIdToMap(musicIds);
        // 填充信息
        Map<Long, TbMusicUrlPojo> urlPojoMap = new HashMap<>();
        // 获取音乐地址
        try {
            List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(musicIds, Boolean.TRUE.equals(req.getRefresh()));
            urlPojoMap = musicUrlByMusicId.parallelStream()
                                          .collect(Collectors.toMap(TbMusicUrlPojo::getMusicId,
                                                  Function.identity(),
                                                  (musicUrlPojo, musicUrlPojo2) -> musicUrlPojo2));
        } catch (BaseException ex) {
            if (!StringUtils.equals(ex.getErrorCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(ResultCode.SONG_NOT_EXIST);
            }
        }
    
        List<Long> likeMusic = new ArrayList<>();
        List<TbCollectPojo> userPlayList = qukuService.getUserPlayList(UserUtil.getUser().getId(), Collections.singletonList(PlayListTypeConfig.LIKE));
        if (CollUtil.isNotEmpty(userPlayList) && userPlayList.size() == 1) {
            List<TbCollectMusicPojo> list = collectMusicService.list(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                             .eq(TbCollectMusicPojo::getCollectId, userPlayList.get(0).getId()));
            likeMusic.addAll(list.parallelStream().map(TbCollectMusicPojo::getMusicId).collect(Collectors.toList()));
        }
        List<MusicPageRes> musicPageRes = new ArrayList<>();
        for (TbMusicPojo musicPojo : page.getRecords()) {
            MusicPageRes e = new MusicPageRes();
            e.setId(musicPojo.getId());
            e.setMusicName(musicPojo.getMusicName());
            e.setMusicNameAlias(musicPojo.getAliasName());
            e.setPic(musicPojo.getPic());
            TbMusicUrlPojo tbMusicUrlPojo = Optional.ofNullable(urlPojoMap.get(musicPojo.getId())).orElse(new TbMusicUrlPojo());
            e.setIsExist(StringUtils.isNotBlank(tbMusicUrlPojo.getUrl()));
            e.setMusicRawUrl(tbMusicUrlPojo.getUrl());
        
            e.setIsLike(likeMusic.contains(musicPojo.getId()));
        
            // 专辑
            TbAlbumPojo tbAlbumPojo = Optional.ofNullable(albumMap.get(musicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            e.setAlbumId(tbAlbumPojo.getId());
            e.setAlbumName(tbAlbumPojo.getAlbumName());
            e.setPublishTime(tbAlbumPojo.getPublishTime());
        
            // 歌手
            List<TbArtistPojo> tbArtistPojos = Optional.ofNullable(musicArtistByMusicIdToMap.get(musicPojo.getId())).orElse(new ArrayList<>());
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
    private Page<TbMusicPojo> getPageFilter(MusicPageReq req, List<Long> musicIdList, Page<TbMusicPojo> page) {
        LambdaQueryWrapper<TbMusicPojo> wrapper;
        if (Boolean.TRUE.equals(req.getIsShowNoExist())) {
            // 无音源音乐
            Collection<Long> union;
            List<Long> collect = musicService.list().parallelStream().map(TbMusicPojo::getId).collect(Collectors.toList());
            List<TbMusicUrlPojo> musicUrlPojos = musicUrlService.list();
            List<Long> collect1 = musicUrlPojos.parallelStream().map(TbMusicUrlPojo::getMusicId).collect(Collectors.toList());
            // 数据库中没有音源数据
            collect.removeAll(collect1);
            // 确认OSS音源存在
            List<TbMusicUrlPojo> urls = musicCommonApi.getMusicUrlByMusicUrlList(musicUrlPojos, false);
            List<Long> noMusicSource = urls.parallelStream()
                                           .filter(musicUrlPojo -> StringUtils.isEmpty(musicUrlPojo.getUrl()))
                                           .map(TbMusicUrlPojo::getMusicId)
                                           .collect(Collectors.toList());
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
        return page;
    }
    
    /**
     * 根据歌曲名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIdByMusicName(MusicPageReq req) {
        if (StringUtils.isBlank(req.getMusicName()) && CollUtil.isEmpty(req.getMusicIds())) {
            return Collections.emptyList();
        }
        List<TbMusicPojo> list = new ArrayList<>();
        // 音乐名
        list.addAll(musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                              .in(CollUtil.isNotEmpty(req.getMusicIds()), TbMusicPojo::getId, req.getMusicIds())
                                              .like(StringUtils.isNotBlank(req.getMusicName()), TbMusicPojo::getMusicName, req.getMusicName())));
        // 别名
        list.addAll(musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                              .in(CollUtil.isNotEmpty(req.getMusicIds()), TbMusicPojo::getId, req.getMusicIds())
                                              .like(StringUtils.isNotBlank(req.getMusicName()), TbMusicPojo::getAliasName, req.getMusicName())));
    
        return list.stream().map(TbMusicPojo::getId).distinct().collect(Collectors.toList());
    }
    
    private List<Long> getMusicIDByAlbumName(MusicPageReq req) {
        if (StringUtils.isBlank(req.getAlbumName())) {
            return Collections.emptyList();
        }
        // 获取专辑ID
        LambdaQueryWrapper<TbAlbumPojo> like = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                       .like(TbAlbumPojo::getAlbumName, req.getAlbumName());
        List<TbAlbumPojo> albumList = albumService.list(like);
        if (CollUtil.isEmpty(albumList)) {
            return Collections.emptyList();
        }
        List<Long> collect = albumList.stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
        // 获取歌曲ID
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, collect));
        return list.stream().map(TbMusicPojo::getId).collect(Collectors.toList());
    }
    
    /**
     * 查询歌手名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIDByArtistName(MusicPageReq req) {
        List<TbMusicPojo> musicListBySingerName = qukuService.getMusicListByArtistName(req.getArtistName());
        if (CollUtil.isEmpty(musicListBySingerName)) {
            return Collections.emptyList();
        }
        return musicListBySingerName.stream().map(TbMusicPojo::getId).collect(Collectors.toList());
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
        routerVo.setPath("/playlist");
        routerVo.setMeta(meta);
    
        Collection<TbCollectPojo> list = qukuService.getUserPlayList(uid, Collections.emptyList());
    
        // 子页面路由，(歌单)
        List<Children> children = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : list) {
            Children e = new Children();
            e.setName(String.valueOf(tbCollectPojo.getId()));
            e.setPath("/playlist/" + tbCollectPojo.getId());
            e.setComponent("/src/views/playlist/index.vue");
    
            Meta playListMeta = new Meta();
            // 歌单icon，包括歌单名
            playListMeta.setTitle(tbCollectPojo.getPlayListName());
            // 普通歌单
            if (tbCollectPojo.getType() == 0) {
                playListMeta.setIcon("playlist2Bold");
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
    
    public TbCollectPojo getPlayListInfo(Long id) {
        return collectService.getById(id);
    }
    
    public TbCollectPojo createPlayList(String name) {
        return qukuService.createPlayList(UserUtil.getUser().getId(), name, Short.parseShort("1"));
    }
    
    public void deletePlayList(Long userId, List<Long> id) {
        qukuService.removePlayList(userId, id);
    }
    
    public List<PlayListRes> getUserPlayList(Long userId) {
        List<TbCollectPojo> userPlayList = qukuService.getUserPlayList(userId, Arrays.asList(PlayListTypeConfig.ORDINARY, PlayListTypeConfig.ORDINARY));
        List<PlayListRes> playListRes = new ArrayList<>();
        collectFillUpCount(userPlayList, playListRes);
        return playListRes;
    }
    
    private void collectFillUpCount(List<TbCollectPojo> userPlayList, List<PlayListRes> playListRes) {
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
        ExceptionUtil.isNull(byId == null, ResultCode.PLAT_LIST_EXIST);
        qukuService.addMusicToCollect(userId, byId.getId(), id, flag);
    }
    
    public Page<PlayListRes> getPlayListPage(PlayListReq req) {
        req.setPlayListName(StringUtils.trim(req.getPlayListName()));
    
        req.setPage(MyPageUtil.checkPage(req.getPage()));
        Page<TbCollectPojo> playList = playListService.getPlayList(req,
                req.getPage().getPageIndex().longValue(),
                req.getPage().getPageNum().longValue(),
                req.getType());
        List<PlayListRes> playListRes = new ArrayList<>();
        collectFillUpCount(playList.getRecords(), playListRes);
    
        Page<PlayListRes> playListResPage = new Page<>();
        BeanUtils.copyProperties(playList, playListResPage);
        playListResPage.setRecords(playListRes);
        return playListResPage;
    }
    
    public TbCollectPojo updatePlayListInfo(PlayListReq req) {
        if (req.getId() == null) {
            throw new BaseException(ResultCode.PARAM_IS_INVALID);
        }
        Short type = req.getType();
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
    
        collectService.saveOrUpdate(req);
        return collectService.getById(req.getId());
    }
    
    public void like(Long userId, Long id, boolean add) {
        qukuService.collectLike(userId, id, add);
    }
}

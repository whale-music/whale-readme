package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageCommon;
import org.api.admin.model.req.MusicPageReq;
import org.api.admin.model.res.MusicPageRes;
import org.api.admin.model.res.PlayListMusicRes;
import org.api.admin.model.res.router.Children;
import org.api.admin.model.res.router.Meta;
import org.api.admin.model.res.router.RouterVo;
import org.api.admin.utils.MyPageUtil;
import org.api.common.service.MusicCommonApi;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.iservice.*;
import org.core.pojo.*;
import org.core.service.QukuService;
import org.core.utils.CollectSortUtil;
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
    
    /**
     * 歌手表
     */
    @Autowired
    private TbArtistService artistService;
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 专辑表
     */
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbAlbumArtistService albumSingerService;
    
    @Autowired
    private TbCollectService collectService;
    
    /**
     * 歌单与音乐中间表
     */
    @Autowired
    private TbCollectMusicService collectMusicService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
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
    
    
    public Page<PlayListMusicRes> getPlaylist(String playId, MusicPageReq page) {
        page = Optional.ofNullable(page).orElse(new MusicPageReq());
        PageCommon other = new PageCommon();
        other.setPageIndex(0);
        other.setPageNum(10);
        page.setPage(Optional.ofNullable(page.getPage()).orElse(other));
        
        Page<TbCollectMusicPojo> playListMusicResPage = new Page<>(page.getPage().getPageIndex(), page.getPage().getPageNum());
        ArrayList<PlayListMusicRes> playListMusicRes = new ArrayList<>();
        collectMusicService.page(playListMusicResPage,
                Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, playId).orderByAsc(TbCollectMusicPojo::getSort));
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
        Map<Long, List<TbArtistPojo>> artistMaps = qukuService.getArtistListByMusicIdToMap(map);
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
        Page<PlayListMusicRes> resPage = new Page<>();
        BeanUtils.copyProperties(playListMusicResPage, resPage);
        resPage.setRecords(playListMusicRes);
        return resPage;
    }
    
    /**
     * 获取音乐基本信息
     */
    public Page<MusicPageRes> getMusicPage(MusicPageReq req) {
        req.setPage(MyPageUtil.checkPage(Optional.ofNullable(req.getPage()).orElse(new PageCommon())));
        List<Long> musicIdList = new LinkedList<>();
        
        // 查询歌手表
        musicIdList.addAll(getMusicIDBySingerName(req));
        // 查询专辑表
        musicIdList.addAll(getMusicIDByAlbumName(req));
        // 查询歌曲名
        musicIdList.addAll(getMusicIdByMusicName(req));
        
        Page<TbMusicPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<TbMusicPojo> wrapper = Wrappers.<TbMusicPojo>lambdaQuery()
                                                          .in(CollUtil.isNotEmpty(musicIdList), TbMusicPojo::getId, musicIdList);
        pageOrderBy(req.getOrder(), req.getOrderBy(), wrapper);
        musicService.page(page, wrapper);
    
    
        // 专辑信息
        List<Long> albumIds = page.getRecords().stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toList());
        Map<Long, TbAlbumPojo> albumMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumIds)) {
            albumMap = albumService.listByIds(albumIds)
                                   .stream()
                                   .collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        }
    
        // 歌手信息
        List<TbAlbumArtistPojo> albumSingerPojoList = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                                      .in(TbAlbumArtistPojo::getAlbumId, albumIds));
        // 歌手ID
        Set<Long> singerLongIds = albumSingerPojoList.stream().map(TbAlbumArtistPojo::getArtistId).collect(Collectors.toSet());
        Map<Long, TbArtistPojo> singerMap = new HashMap<>();
        if (CollUtil.isNotEmpty(singerLongIds)) {
            singerMap = artistService.listByIds(singerLongIds)
                                     .stream()
                                     .collect(Collectors.toMap(TbArtistPojo::getId, tbSingerPojo -> tbSingerPojo));
        }
        // 填充信息
        Map<Long, TbMusicUrlPojo> urlPojoMap = new HashMap<>();
        // 获取音乐地址
        try {
            Set<Long> collect = page.getRecords().parallelStream().map(TbMusicPojo::getId).collect(Collectors.toSet());
            List<TbMusicUrlPojo> musicUrlByMusicId = musicCommonApi.getMusicUrlByMusicId(collect, Boolean.TRUE.equals(req.getRefresh()));
            urlPojoMap = musicUrlByMusicId.parallelStream()
                                          .collect(Collectors.toMap(TbMusicUrlPojo::getMusicId,
                                                  Function.identity(),
                                                  (musicUrlPojo, musicUrlPojo2) -> musicUrlPojo2));
        } catch (BaseException ex) {
            if (!StringUtils.equals(ex.getErrorCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                throw new BaseException(ResultCode.SONG_NOT_EXIST);
            }
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
            // 专辑
            TbAlbumPojo tbAlbumPojo = Optional.ofNullable(albumMap.get(musicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            e.setAlbumId(tbAlbumPojo.getId());
            e.setAlbumName(tbAlbumPojo.getAlbumName());
            e.setPublishTime(tbAlbumPojo.getPublishTime());
    
            // 歌手
            // 获取歌手ID
            Set<Long> collect = albumSingerPojoList.stream()
                                                   .filter(tbAlbumSingerPojo -> tbAlbumSingerPojo.getAlbumId().equals(tbAlbumPojo.getId()))
                                                   .map(TbAlbumArtistPojo::getArtistId)
                                                   .collect(Collectors.toSet());
            e.setSingerIds(new ArrayList<>());
            e.setSingerName(new ArrayList<>());
            for (Long aLong : collect) {
                TbArtistPojo tbArtistPojo = singerMap.get(aLong);
                e.getSingerIds().add(tbArtistPojo.getId());
                e.getSingerName().add(tbArtistPojo.getArtistName());
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
    
    /**
     * 根据歌曲名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIdByMusicName(MusicPageReq req) {
        if (StringUtils.isNotBlank(req.getMusicName()) || CollUtil.isNotEmpty(req.getMusicIds())) {
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
        return Collections.emptyList();
    }
    
    private List<Long> getMusicIDByAlbumName(MusicPageReq req) {
        if (StringUtils.isNotBlank(req.getAlbumName())) {
            // 获取专辑ID
            LambdaQueryWrapper<TbAlbumPojo> like = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                           .like(TbAlbumPojo::getAlbumName, req.getAlbumName())
                                                           .like(TbAlbumPojo::getAlbumName, req.getAlbumName());
            List<TbAlbumPojo> albumList = albumService.list(like);
            List<Long> collect = albumList.stream().map(TbAlbumPojo::getId).collect(Collectors.toList());
            
            // 获取歌曲ID
            List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                                               .in(CollUtil.isNotEmpty(collect), TbMusicPojo::getAlbumId, collect));
            return list.stream().map(TbMusicPojo::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    /**
     * 查询歌手名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIDBySingerName(MusicPageReq req) {
        List<TbMusicPojo> musicListBySingerName = qukuService.getMusicListByArtistName(req.getSingerName());
        if (CollUtil.isEmpty(musicListBySingerName)) {
            return musicListBySingerName.stream().map(TbMusicPojo::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
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
        meta.setIcon("solar:playlist-minimalistic-2-linear");
        meta.setRank(3);
        // 标题路由
        RouterVo routerVo = new RouterVo();
        routerVo.setPath("/playlist");
        routerVo.setMeta(meta);
        
        
        LambdaQueryWrapper<TbCollectPojo> queryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                 .eq(TbCollectPojo::getUserId, uid);
        List<TbCollectPojo> list = collectService.list(queryWrapper);
        list = CollectSortUtil.userLikeUserSort(uid, list);
        
        // 子页面路由，(歌单)
        List<Children> children = new ArrayList<>();
        for (TbCollectPojo tbCollectPojo : list) {
            Children e = new Children();
            e.setName(String.valueOf(tbCollectPojo.getId()));
            e.setPath("/playlist/" + tbCollectPojo.getId());
            e.setComponent("() => import('@/views/playlist/index')");
            
            Meta playListMeta = new Meta();
            // 歌单icon，包括歌单名
            playListMeta.setTitle(tbCollectPojo.getPlayListName());
            playListMeta.setIcon("solar:playlist-minimalistic-2-linear");
    
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
}

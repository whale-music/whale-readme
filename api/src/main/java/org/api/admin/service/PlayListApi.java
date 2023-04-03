package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
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
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.page.Wrappers;
import org.core.iservice.*;
import org.core.pojo.*;
import org.core.service.QukuService;
import org.core.utils.CollectSortUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "PlayListApi")
public class PlayListApi {
    
    /**
     * 音乐表
     */
    @Autowired
    private MusicService musicService;
    
    /**
     * 歌手表
     */
    @Autowired
    private ArtistService artistService;
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 专辑表
     */
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AlbumArtistService albumArtistService;
    
    @Autowired
    private CollectService collectService;
    
    /**
     * 歌单与音乐中间表
     */
    @Autowired
    private CollectMusicService collectMusicService;
    
    private static void pageOrderBy(boolean order, String orderBy, LambdaQueryWrapper<MusicPojo> musicWrapper) {
        // sort歌曲添加顺序, createTime创建日期顺序,updateTime修改日期顺序, id歌曲ID顺序
        switch (Optional.ofNullable(orderBy).orElse("")) {
            case "id":
                musicWrapper.orderBy(true, order, MusicPojo::getId);
                break;
            case "updateTime":
                musicWrapper.orderBy(true, order, MusicPojo::getUpdateTime);
                break;
            case "createTime":
                musicWrapper.orderBy(true, order, MusicPojo::getCreateTime);
                break;
            case "sort":
            default:
                musicWrapper.orderBy(true, order, MusicPojo::getSort);
        }
    }
    
    
    public List<PlayListMusicRes> getPlaylist(Long playId) {
        ArrayList<PlayListMusicRes> playListMusicRes = new ArrayList<>();
        List<CollectMusicPojo> collectMusicList = collectMusicService.listByIds(Collections.singletonList(playId));
        if (CollUtil.isEmpty(collectMusicList)) {
            return Collections.emptyList();
        }
        List<Long> musicIds = collectMusicList.stream().map(CollectMusicPojo::getMusicId).collect(Collectors.toList());
        List<MusicPojo> musicPojoList = musicService.listByIds(musicIds);
        Set<Long> albumIds = musicPojoList.stream().map(MusicPojo::getAlbumId).collect(Collectors.toSet());
        Map<Long, AlbumPojo> albumPojoMap = albumService.listByIds(albumIds)
                                                        .stream()
                                                        .collect(Collectors.toMap(AlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        
        for (MusicPojo musicPojo : musicPojoList) {
            PlayListMusicRes e1 = new PlayListMusicRes();
            BeanUtils.copyProperties(musicPojo, e1, "lyric");
            
            AlbumPojo albumPojo = Optional.ofNullable(albumPojoMap.get(musicPojo.getAlbumId())).orElse(new AlbumPojo());
            albumPojo.setDescription("");
            e1.setAlbum(albumPojo);
            
            List<ArtistPojo> collect = qukuService.getSingerByMusicId(musicPojo.getId());
            e1.setSingers(collect);
            
            playListMusicRes.add(e1);
        }
        return playListMusicRes;
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
        
        Page<MusicPojo> page = new Page<>(req.getPage().getPageIndex(), req.getPage().getPageNum());
        LambdaQueryWrapper<MusicPojo> wrapper = Wrappers.<MusicPojo>lambdaQuery()
                                                        .in(CollUtil.isNotEmpty(musicIdList), MusicPojo::getId, musicIdList);
        pageOrderBy(req.getOrder(), req.getOrderBy(), wrapper);
        musicService.page(page, wrapper);
        
        
        // 专辑信息
        List<Long> albumIds = page.getRecords().stream().map(MusicPojo::getAlbumId).collect(Collectors.toList());
        Map<Long, AlbumPojo> albumMap = new HashMap<>();
        if (CollUtil.isNotEmpty(albumIds)) {
            albumMap = albumService.listByIds(albumIds)
                                   .stream()
                                   .collect(Collectors.toMap(AlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        }
        
        // 歌手信息
        List<AlbumArtistPojo> albumSingerPojoList = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery()
                                                                                    .in(AlbumArtistPojo::getAlbumId, albumIds));
        // 歌手ID
        Set<Long> singerLongIds = albumSingerPojoList.stream().map(AlbumArtistPojo::getArtistId).collect(Collectors.toSet());
        Map<Long, ArtistPojo> singerMap = new HashMap<>();
        if (CollUtil.isNotEmpty(singerLongIds)) {
            singerMap = artistService.listByIds(singerLongIds)
                                     .stream()
                                     .collect(Collectors.toMap(ArtistPojo::getId, tbSingerPojo -> tbSingerPojo));
        }
        // 填充信息
        List<MusicPageRes> musicPageRes = new ArrayList<>();
        for (MusicPojo musicPojo : page.getRecords()) {
            MusicPageRes e = new MusicPageRes();
            e.setId(musicPojo.getId());
            e.setMusicName(musicPojo.getMusicName());
            e.setMusicNameAlias(musicPojo.getAliasName());
            
            // 专辑
            AlbumPojo albumPojo = Optional.ofNullable(albumMap.get(musicPojo.getAlbumId())).orElse(new AlbumPojo());
            e.setAlbumId(albumPojo.getId());
            e.setAlbumName(albumPojo.getAlbumName());
            
            // 歌手
            // 获取歌手ID
            Set<Long> collect = albumSingerPojoList.stream()
                                                   .filter(tbAlbumSingerPojo -> tbAlbumSingerPojo.getAlbumId().equals(albumPojo.getId()))
                                                   .map(AlbumArtistPojo::getArtistId)
                                                   .collect(Collectors.toSet());
            e.setSingerIds(new ArrayList<>());
            e.setSingerName(new ArrayList<>());
            for (Long aLong : collect) {
                ArtistPojo artistPojo = singerMap.get(aLong);
                e.getSingerIds().add(artistPojo.getId());
                e.getSingerName().add(artistPojo.getArtistName());
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
        if (StringUtils.isNotBlank(req.getMusicName())) {
            List<MusicPojo> list = new ArrayList<>();
            // 音乐名
            list.addAll(musicService.list(Wrappers.<MusicPojo>lambdaQuery()
                                                  .in(CollUtil.isNotEmpty(req.getMusicIds()), MusicPojo::getId, req.getMusicIds())
                                                  .like(MusicPojo::getMusicName, req.getMusicName())));
            // 别名
            list.addAll(musicService.list(Wrappers.<MusicPojo>lambdaQuery()
                                                  .in(CollUtil.isNotEmpty(req.getMusicIds()), MusicPojo::getId, req.getMusicIds())
                                                  .like(MusicPojo::getAliasName, req.getMusicName())));
            return list.stream().map(MusicPojo::getId).distinct().collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    private List<Long> getMusicIDByAlbumName(MusicPageReq req) {
        if (StringUtils.isNotBlank(req.getAlbumName())) {
            // 获取专辑ID
            LambdaQueryWrapper<AlbumPojo> like = Wrappers.<AlbumPojo>lambdaQuery()
                                                         .like(AlbumPojo::getAlbumName, req.getAlbumName())
                                                         .like(AlbumPojo::getAlbumName, req.getAlbumName());
            List<AlbumPojo> albumList = albumService.list(like);
            List<Long> collect = albumList.stream().map(AlbumPojo::getId).collect(Collectors.toList());
            
            // 获取歌曲ID
            List<MusicPojo> list = musicService.list(Wrappers.<MusicPojo>lambdaQuery()
                                                             .in(CollUtil.isNotEmpty(collect), MusicPojo::getAlbumId, collect));
            return list.stream().map(MusicPojo::getId).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    
    /**
     * 查询歌手名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIDBySingerName(MusicPageReq req) {
        List<MusicPojo> musicListBySingerName = qukuService.getMusicListBySingerName(req.getSingerName());
        if (CollUtil.isEmpty(musicListBySingerName)) {
            return musicListBySingerName.stream().map(MusicPojo::getId).collect(Collectors.toList());
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
        meta.setIcon("ep:menu");
        meta.setRank(3);
        // 标题路由
        RouterVo routerVo = new RouterVo();
        routerVo.setPath("/playlist");
        routerVo.setMeta(meta);
        
        
        LambdaQueryWrapper<CollectPojo> queryWrapper = Wrappers.<CollectPojo>lambdaQuery()
                                                               .eq(CollectPojo::getUserId, uid);
        List<CollectPojo> list = collectService.list(queryWrapper);
        list = CollectSortUtil.userLikeUserSort(uid, list);
        
        // 子页面路由，(歌单)
        List<Children> children = new ArrayList<>();
        for (CollectPojo collectPojo : list) {
            Children e = new Children();
            e.setName(String.valueOf(collectPojo.getId()));
            e.setPath("/playlist/" + collectPojo.getId());
            e.setComponent("() => import('@/views/playlist/index')");
            
            Meta playListMeta = new Meta();
            // 歌单icon，包括歌单名
            playListMeta.setTitle(collectPojo.getPlayListName());
            playListMeta.setIcon("ep:headset");
            
            e.setMeta(playListMeta);
            children.add(e);
        }
        
        routerVo.setChildren(children);
        
        routerVos.add(routerVo);
        return routerVos;
    }
}

package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
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
import org.core.pojo.*;
import org.core.service.*;
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
    private TbMusicService musicService;
    
    /**
     * 歌手表
     */
    @Autowired
    private TbSingerService singerService;
    
    @Autowired
    private TbMusicSingerService musicSingerService;
    
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
    
    
    public List<PlayListMusicRes> getPlaylist(String playId) {
        ArrayList<PlayListMusicRes> playListMusicRes = new ArrayList<>();
        List<TbCollectMusicPojo> collectMusicList = collectMusicService.listByIds(Collections.singletonList(playId));
        if (CollUtil.isEmpty(collectMusicList)) {
            return Collections.emptyList();
        }
        List<Long> musicIds = collectMusicList.stream().map(TbCollectMusicPojo::getMusicId).collect(Collectors.toList());
        List<TbMusicPojo> musicPojoList = musicService.listByIds(musicIds);
        Set<Long> albumIds = musicPojoList.stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
        Map<Long, TbAlbumPojo> albumPojoMap = albumService.listByIds(albumIds)
                                                          .stream()
                                                          .collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> tbAlbumPojo));
        List<TbMusicSingerPojo> tbMusicSingerPojos = musicSingerService.list(Wrappers.<TbMusicSingerPojo>lambdaQuery()
                                                                                     .in(TbMusicSingerPojo::getMusicId, musicIds));
        List<TbSingerPojo> tbSingerPojos = singerService.listByIds(tbMusicSingerPojos.stream()
                                                                                     .map(TbMusicSingerPojo::getSingerId)
                                                                                     .collect(Collectors.toSet()));
        for (TbMusicPojo tbMusicPojo : musicPojoList) {
            PlayListMusicRes e1 = new PlayListMusicRes();
            BeanUtils.copyProperties(tbMusicPojo, e1, "lyric");
            
            TbAlbumPojo tbAlbumPojo = Optional.ofNullable(albumPojoMap.get(tbMusicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            tbAlbumPojo.setDescription("");
            e1.setAlbum(tbAlbumPojo);
            
            Set<Long> singerSet = tbMusicSingerPojos.stream().filter(tbMusicSingerPojo -> Objects.equals(tbMusicSingerPojo.getMusicId(),
                                                            tbMusicPojo.getId()))
                                                    .map(TbMusicSingerPojo::getSingerId)
                                                    .collect(Collectors.toSet());
            List<TbSingerPojo> collect = tbSingerPojos.stream()
                                                      .filter(tbSingerPojo -> singerSet.contains(tbSingerPojo.getId()))
                                                      .collect(Collectors.toList());
            
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
        
        // 音乐ID
        List<Long> musicIds = page.getRecords().stream().map(TbMusicPojo::getId).collect(Collectors.toList());
        // 歌手信息
        List<TbMusicSingerPojo> singerIds = new ArrayList<>();
        if (CollUtil.isNotEmpty(musicIds)) {
            singerIds = musicSingerService.list(Wrappers.<TbMusicSingerPojo>lambdaQuery().in(TbMusicSingerPojo::getMusicId, musicIds));
        }
        List<Long> singerLongIds = singerIds.stream().map(TbMusicSingerPojo::getSingerId).collect(Collectors.toList());
        Map<Long, TbSingerPojo> singerMap = new HashMap<>();
        if (CollUtil.isNotEmpty(singerLongIds)) {
            singerMap = singerService.listByIds(singerLongIds)
                                     .stream()
                                     .collect(Collectors.toMap(TbSingerPojo::getId, tbSingerPojo -> tbSingerPojo));
        }
        
        // 填充信息
        List<MusicPageRes> musicPageRes = new ArrayList<>();
        for (TbMusicPojo musicPojo : page.getRecords()) {
            MusicPageRes e = new MusicPageRes();
            e.setId(musicPojo.getId());
            e.setMusicName(musicPojo.getMusicName());
            e.setMusicNameAlias(musicPojo.getAliaName());
    
            // 专辑
            TbAlbumPojo tbAlbumPojo = Optional.ofNullable(albumMap.get(musicPojo.getAlbumId())).orElse(new TbAlbumPojo());
            e.setAlbumId(tbAlbumPojo.getId());
            e.setAlbumName(tbAlbumPojo.getAlbumName());
    
            // 歌手
            // 获取歌手ID
            List<Long> collect = singerIds.stream()
                                          .filter(tbMusicSingerPojo -> tbMusicSingerPojo.getMusicId().equals(musicPojo.getId()))
                                          .map(TbMusicSingerPojo::getSingerId)
                                          .collect(Collectors.toList());
            e.setSingerIds(new ArrayList<>());
            e.setSingerName(new ArrayList<>());
            for (Long aLong : collect) {
                TbSingerPojo tbSingerPojo = singerMap.get(aLong);
                e.getSingerIds().add(tbSingerPojo.getId());
                e.getSingerName().add(tbSingerPojo.getSingerName());
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
            List<TbMusicPojo> list = new ArrayList<>();
            // 音乐名
            list.addAll(musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                                  .in(CollUtil.isNotEmpty(req.getMusicIds()), TbMusicPojo::getId, req.getMusicIds())
                                                  .like(TbMusicPojo::getMusicName, req.getMusicName())));
            // 别名
            list.addAll(musicService.list(Wrappers.<TbMusicPojo>lambdaQuery()
                                                  .in(CollUtil.isNotEmpty(req.getMusicIds()), TbMusicPojo::getId, req.getMusicIds())
                                                  .like(TbMusicPojo::getAliaName, req.getMusicName())));
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
     * 查询专辑名获取歌曲ID
     *
     * @return 歌曲ID
     */
    private List<Long> getMusicIDBySingerName(MusicPageReq req) {
        if (StringUtils.isNotBlank(req.getSingerName())) {
            LambdaQueryWrapper<TbSingerPojo> wrapper = Wrappers.lambdaQuery();
            List<TbSingerPojo> singerList = singerService.list(wrapper.like(TbSingerPojo::getSingerName, req.getSingerName()));
            List<Long> singerIdsList = singerList.stream().map(TbSingerPojo::getId).collect(Collectors.toList());
            if (IterUtil.isNotEmpty(singerIdsList)) {
                List<TbMusicSingerPojo> musicSingerList = musicSingerService.listByIds(singerIdsList);
                return musicSingerList.stream().map(TbMusicSingerPojo::getMusicId).collect(Collectors.toList());
            }
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
            playListMeta.setIcon("ep:headset");
            
            e.setMeta(playListMeta);
            children.add(e);
        }
        
        routerVo.setChildren(children);
        
        routerVos.add(routerVo);
        return routerVos;
    }
}

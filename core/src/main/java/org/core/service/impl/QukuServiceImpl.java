package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.pojo.*;
import org.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("MusicService")
public class QukuServiceImpl implements QukuService {
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbSingerService singerService;
    
    /**
     * 音乐地址服务
     */
    @Autowired
    private TbMusicUrlService musicUrlService;
    
    @Autowired
    private TbUserAlbumService userAlbumService;
    
    @Autowired
    private TbAlbumSingerService albumSingerService;
    
    @Autowired
    private TbUserSingerService userSingerService;
    
    /**
     * 获取专辑信息
     */
    @Override
    public TbAlbumPojo getAlbumByMusicId(Long musicId) {
        List<TbAlbumPojo> albumListByMusicId = getAlbumListByMusicId(Collections.singletonList(musicId));
        if (CollUtil.isEmpty(albumListByMusicId)) {
            return null;
        }
        if (albumListByMusicId.size() != 1) {
            throw new BaseException(ResultCode.ALBUM_ERROR);
        }
        return albumListByMusicId.get(0);
    }
    
    @Override
    public TbAlbumPojo getAlbumByAlbumId(Long albumIds) {
        List<TbAlbumPojo> albumListByAlbumId = getAlbumListByAlbumId(Collections.singletonList(albumIds));
        if (CollUtil.isEmpty(albumListByAlbumId)) {
            return null;
        }
        if (albumListByAlbumId.size() != 1) {
            throw new BaseException(ResultCode.ALBUM_ERROR);
        }
        return albumListByAlbumId.get(0);
    }
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    @Override
    public List<TbAlbumPojo> getAlbumListByMusicId(List<Long> musicIds) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, musicIds));
        List<Long> albumIds = list.stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toList());
        return getAlbumListByAlbumId(albumIds);
    }
    
    /**
     * 通过专辑ID 获取专辑信息
     */
    @Override
    public List<TbAlbumPojo> getAlbumListByAlbumId(Collection<Long> albumIds) {
        return albumService.listByIds(albumIds);
    }
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    @Override
    public List<TbArtistPojo> getSingerListByMusicId(List<Long> musicIds) {
        List<TbMusicPojo> musicPojoList = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, musicIds));
        if (CollUtil.isEmpty(musicPojoList)) {
            return Collections.emptyList();
        }
        Set<Long> albumIds = musicPojoList.stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
        List<TbAlbumArtistPojo> list = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, albumIds));
        return getTbSingerPojoList(CollUtil.isEmpty(list), list.stream().map(TbAlbumArtistPojo::getArtistId));
    }
    
    /**
     * 获取歌手信息
     */
    @Override
    public List<TbArtistPojo> getSingerByMusicId(Long musicId) {
        return getSingerListByMusicId(Collections.singletonList(musicId));
    }
    
    /**
     * 获取歌曲URL下载地址
     */
    @Override
    public List<TbMusicUrlPojo> getMusicUrl(Long musicId) {
        return getMusicUrl(Set.of(musicId));
    }
    
    @Override
    public List<TbMusicUrlPojo> getMusicUrl(Set<Long> musicId) {
        LambdaQueryWrapper<TbMusicUrlPojo> in = Wrappers.<TbMusicUrlPojo>lambdaQuery().in(TbMusicUrlPojo::getMusicId, musicId);
        return musicUrlService.list(in);
    }
    
    /**
     * 随即获取曲库中的一条数据
     */
    @Override
    public TbMusicPojo randomMusic() {
        long count = musicService.count();
        Page<TbMusicPojo> page = new Page<>(RandomUtil.randomLong(0, count), 1);
        musicService.page(page);
        return Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>()).get(0);
    }
    
    @Override
    public Page<TbAlbumPojo> getAlbumPage(String area, Long offset, Long limit) {
        Page<TbAlbumPojo> page = new Page<>(offset, limit);
        albumService.page(page);
        return page;
    }
    
    /**
     * 查询专辑下音乐数量
     *
     * @param albumId 专辑ID
     */
    @Override
    public Integer getAlbumMusicCountByAlbumId(Long albumId) {
        long count = albumService.count(Wrappers.<TbAlbumPojo>lambdaQuery().eq(TbAlbumPojo::getId, albumId));
        return Integer.valueOf(count + "");
    }
    
    /**
     * 查询专辑下音乐数量
     *
     * @param musicId 歌曲ID
     */
    @Override
    public Integer getAlbumMusicCountByMusicId(Long musicId) {
        TbMusicPojo tbMusicPojo = musicService.getOne(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getId, musicId));
        if (tbMusicPojo == null) {
            return null;
        }
        return getAlbumMusicCountByAlbumId(tbMusicPojo.getAlbumId());
    }
    
    /**
     * 通过歌手获取歌手拥有的音乐数量
     *
     * @param id 歌手ID
     */
    @Override
    public Long getMusicCountBySingerId(Long id) {
        List<TbAlbumArtistPojo> albumSingerPojoList = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                                      .eq(TbAlbumArtistPojo::getArtistId, id));
        Set<Long> albumIds = albumSingerPojoList.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toSet());
        return musicService.count(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, albumIds));
    }
    
    /**
     * 获取专辑歌手列表
     */
    @Override
    public List<TbArtistPojo> getArtistListByAlbumIds(Long albumIds) {
        return getArtistListByAlbumIds(Collections.singletonList(albumIds));
    }
    
    @Override
    public List<TbAlbumPojo> getAlbumListBySingerIds(List<Long> ids) {
        LambdaQueryWrapper<TbAlbumArtistPojo> in = Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getArtistId, ids);
        List<TbAlbumArtistPojo> list = albumSingerService.list(in);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return getAlbumListByAlbumId(list.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toSet()));
    }
    
    /**
     * 通过专辑ID获取歌手列表
     */
    @Override
    public List<TbArtistPojo> getArtistListByAlbumIds(List<Long> albumIds) {
        List<TbAlbumArtistPojo> list = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, albumIds));
        return getTbSingerPojoList(CollUtil.isEmpty(list), list.stream().map(TbAlbumArtistPojo::getArtistId));
    }
    
    /**
     * 通过专辑ID获取歌手
     *
     * @param empty      是否执行
     * @param longStream 专辑ID流
     */
    private List<TbArtistPojo> getTbSingerPojoList(boolean empty, Stream<Long> longStream) {
        if (empty) {
            return Collections.emptyList();
        }
        List<Long> singerIds = longStream.collect(Collectors.toList());
        return singerService.list(Wrappers.<TbArtistPojo>lambdaQuery().in(TbArtistPojo::getId, singerIds));
    }
    
    /**
     * 查询用户收藏专辑
     *
     * @param user    用户数据
     * @param current 当前页数
     * @param size    每页多少数据
     */
    @Override
    public List<TbAlbumPojo> getUserCollectAlbum(SysUserPojo user, Long current, Long size) {
        List<TbUserAlbumPojo> userAlbumPojoList = userAlbumService.list(Wrappers.<TbUserAlbumPojo>lambdaQuery()
                                                                                .eq(TbUserAlbumPojo::getUserId, user.getId()));
        if (CollUtil.isEmpty(userAlbumPojoList)) {
            return Collections.emptyList();
        }
        List<Long> albumIds = userAlbumPojoList.stream().map(TbUserAlbumPojo::getAlbumId).collect(Collectors.toList());
        return getAlbumListByAlbumId(albumIds);
    }
    
    
    /**
     * 获取用户关注歌手
     *
     * @param user 用户信息
     */
    @Override
    public List<TbArtistPojo> getUserLikeSingerList(SysUserPojo user) {
        List<TbUserArtistPojo> userLikeSinger = userSingerService.list(Wrappers.<TbUserArtistPojo>lambdaQuery()
                                                                               .eq(TbUserArtistPojo::getUserId, user.getId()));
        if (CollUtil.isEmpty(userLikeSinger)) {
            return Collections.emptyList();
        }
        List<Long> singerIds = userLikeSinger.stream().map(TbUserArtistPojo::getArtistId).collect(Collectors.toList());
        return singerService.listByIds(singerIds);
    }
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param id 歌手ID
     */
    @Override
    public Integer getAlbumCountBySingerId(Long id) {
        return Math.toIntExact(albumSingerService.count(Wrappers.<TbAlbumArtistPojo>lambdaQuery().eq(TbAlbumArtistPojo::getArtistId, id)));
    }
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param id 专辑ID
     */
    @Override
    public List<TbMusicPojo> getMusicListByAlbumId(Long id) {
        return musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getAlbumId, id));
    }
    
    @Override
    public List<TbMusicPojo> getMusicListByAlbumId(Collection<Long> ids) {
        return musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, ids));
    }
    
    /**
     * 根据歌手名查找音乐
     *
     * @param name 歌手
     */
    @Override
    public List<TbMusicPojo> getMusicListBySingerName(String name) {
        if (StringUtils.isNotBlank(name)) {
            List<TbArtistPojo> singerList = singerService.list(Wrappers.<TbArtistPojo>lambdaQuery().like(TbArtistPojo::getArtistName, name));
            List<Long> singerIdsList = singerList.stream().map(TbArtistPojo::getId).collect(Collectors.toList());
            List<TbAlbumArtistPojo> albumIds = albumSingerService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                               .eq(TbAlbumArtistPojo::getArtistId, singerIdsList));
            if (IterUtil.isNotEmpty(albumIds)) {
                return getMusicListByAlbumId(albumIds.stream()
                                                     .map(TbAlbumArtistPojo::getAlbumId)
                                                     .collect(Collectors.toSet()));
            }
        }
        return Collections.emptyList();
    }
    
    /**
     * 获取歌手下音乐信息
     *
     * @param id 歌手ID
     */
    @Override
    public List<TbMusicPojo> getMusicListBySingerId(Long id) {
        List<TbAlbumPojo> albumListBySingerIds = getAlbumListBySingerIds(Collections.singletonList(id));
        if (CollUtil.isEmpty(albumListBySingerIds)) {
            return Collections.emptyList();
        }
        Set<Long> albumIds = albumListBySingerIds.stream().map(TbAlbumPojo::getId).collect(Collectors.toSet());
        return musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getAlbumId, albumIds));
    }
    
    /**
     * 随机获取歌手
     *
     * @param count 获取数量
     */
    @Override
    public List<TbArtistPojo> randomSinger(int count) {
        long sum = singerService.count();
        ArrayList<TbArtistPojo> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            long randomNum = RandomUtil.randomLong(sum);
            Page<TbArtistPojo> page = singerService.page(new Page<>(randomNum, 1));
            res.addAll(page.getRecords());
        }
        return res;
    }
}

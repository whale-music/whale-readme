package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.page.Wrappers;
import org.core.common.result.ResultCode;
import org.core.iservice.*;
import org.core.pojo.*;
import org.core.service.QukuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service("MusicService")
public class QukuServiceImpl implements QukuService {
    
    @Autowired
    private MusicService musicService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private ArtistService artistService;
    
    /**
     * 音乐地址服务
     */
    @Autowired
    private MusicUrlService musicUrlService;
    
    @Autowired
    private UserAlbumService userAlbumService;
    
    @Autowired
    private AlbumArtistService albumArtistService;
    
    @Autowired
    private UserArtistService userArtistService;
    
    /**
     * 获取专辑信息
     */
    @Override
    public AlbumPojo getAlbumByMusicId(Long musicId) {
        List<AlbumPojo> albumLisyMusicId = getAlbumListByMusicId(Collections.singletonList(musicId));
        if (CollUtil.isEmpty(albumLisyMusicId)) {
            return null;
        }
        if (albumLisyMusicId.size() != 1) {
            throw new BaseException(ResultCode.ALBUM_ERROR);
        }
        return albumLisyMusicId.get(0);
    }
    
    @Override
    public AlbumPojo getAlbumByAlbumId(Long albumIds) {
        List<AlbumPojo> albumLisyAlbumId = getAlbumListByAlbumId(Collections.singletonList(albumIds));
        if (CollUtil.isEmpty(albumLisyAlbumId)) {
            return null;
        }
        if (albumLisyAlbumId.size() != 1) {
            throw new BaseException(ResultCode.ALBUM_ERROR);
        }
        return albumLisyAlbumId.get(0);
    }
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    @Override
    public List<AlbumPojo> getAlbumListByMusicId(List<Long> musicIds) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        List<MusicPojo> list = musicService.list(Wrappers.<MusicPojo>lambdaQuery().in(MusicPojo::getId, musicIds));
        List<Long> albumIds = list.stream().map(MusicPojo::getAlbumId).collect(Collectors.toList());
        return getAlbumListByAlbumId(albumIds);
    }
    
    /**
     * 通过专辑ID 获取专辑信息
     */
    @Override
    public List<AlbumPojo> getAlbumListByAlbumId(Collection<Long> albumIds) {
        return albumService.listByIds(albumIds);
    }
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    @Override
    public List<ArtistPojo> getSingerListByMusicId(List<Long> musicIds) {
        List<MusicPojo> musicPojoList = musicService.list(Wrappers.<MusicPojo>lambdaQuery().in(MusicPojo::getId, musicIds));
        if (CollUtil.isEmpty(musicPojoList)) {
            return Collections.emptyList();
        }
        Set<Long> albumIds = musicPojoList.stream().map(MusicPojo::getAlbumId).collect(Collectors.toSet());
        List<AlbumArtistPojo> list = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery().in(AlbumArtistPojo::getAlbumId, albumIds));
        return getSingerPojoList(CollUtil.isEmpty(list), list.stream().map(AlbumArtistPojo::getArtistId));
    }
    
    /**
     * 获取歌手信息
     */
    @Override
    public List<ArtistPojo> getSingerByMusicId(Long musicId) {
        return getSingerListByMusicId(Collections.singletonList(musicId));
    }
    
    /**
     * 获取歌曲URL下载地址
     */
    @Override
    public List<MusicUrlPojo> getMusicUrl(Long musicId) {
        return getMusicUrl(new HashSet<>(Collections.singletonList(musicId)));
    }
    
    @Override
    public List<MusicUrlPojo> getMusicUrl(Set<Long> musicId) {
        LambdaQueryWrapper<MusicUrlPojo> in = Wrappers.<MusicUrlPojo>lambdaQuery().in(MusicUrlPojo::getMusicId, musicId);
        return musicUrlService.list(in);
    }
    
    /**
     * 随即获取曲库中的一条数据
     */
    @Override
    public MusicPojo randomMusic() {
        long count = musicService.count();
        PageRequest page = PageRequest.of((int) RandomUtil.randomLong(0, count), 1);
        Page<MusicPojo> list = musicService.findAll(null, page);
        return Optional.of(list.getRecords()).orElse(new ArrayList<>()).get(0);
    }
    
    @Override
    public Page<AlbumPojo> getAlbumPage(String area, Integer offset, Integer limit) {
        PageRequest of = PageRequest.of(offset, limit);
        return albumService.findAll(null, of);
    }
    
    /**
     * 查询专辑下音乐数量
     *
     * @param albumId 专辑ID
     */
    @Override
    public Integer getAlbumMusicCountByAlbumId(Long albumId) {
        long count = albumService.count(Wrappers.<AlbumPojo>lambdaQuery().eq(AlbumPojo::getId, albumId));
        return Integer.valueOf(count + "");
    }
    
    /**
     * 查询专辑下音乐数量
     *
     * @param musicId 歌曲ID
     */
    @Override
    public Integer getAlbumMusicCountByMusicId(Long musicId) {
        Optional<MusicPojo> one = musicService.getOne(Wrappers.<MusicPojo>lambdaQuery().eq(MusicPojo::getId, musicId));
        if (one.isPresent()) {
            return null;
        }
        return getAlbumMusicCountByAlbumId(one.get().getAlbumId());
    }
    
    /**
     * 通过歌手获取歌手拥有的音乐数量
     *
     * @param id 歌手ID
     */
    @Override
    public Long getMusicCountBySingerId(Long id) {
        List<AlbumArtistPojo> albumSingerPojoList = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery()
                                                                                      .eq(AlbumArtistPojo::getArtistId, id));
        Set<Long> albumIds = albumSingerPojoList.stream().map(AlbumArtistPojo::getAlbumId).collect(Collectors.toSet());
        return musicService.count(Wrappers.<MusicPojo>lambdaQuery().in(MusicPojo::getAlbumId, albumIds));
    }
    
    /**
     * 获取专辑歌手列表
     */
    @Override
    public List<ArtistPojo> getArtistListByAlbumIds(Long albumIds) {
        return getArtistListByAlbumIds(Collections.singletonList(albumIds));
    }
    
    @Override
    public List<AlbumPojo> getAlbumListBySingerIds(List<Long> ids) {
        LambdaQueryWrapper<AlbumArtistPojo> in = Wrappers.<AlbumArtistPojo>lambdaQuery().in(AlbumArtistPojo::getArtistId, ids);
        List<AlbumArtistPojo> list = albumArtistService.list(in);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return getAlbumListByAlbumId(list.stream().map(AlbumArtistPojo::getAlbumId).collect(Collectors.toSet()));
    }
    
    /**
     * 通过专辑ID获取歌手列表
     */
    @Override
    public List<ArtistPojo> getArtistListByAlbumIds(List<Long> albumIds) {
        List<AlbumArtistPojo> list = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery().in(AlbumArtistPojo::getAlbumId, albumIds));
        return getSingerPojoList(CollUtil.isEmpty(list), list.stream().map(AlbumArtistPojo::getArtistId));
    }
    
    /**
     * 通过专辑ID获取歌手
     *
     * @param empty      是否执行
     * @param longStream 专辑ID流
     */
    private List<ArtistPojo> getSingerPojoList(boolean empty, Stream<Long> longStream) {
        if (empty) {
            return Collections.emptyList();
        }
        List<Long> singerIds = longStream.collect(Collectors.toList());
        return artistService.list(Wrappers.<ArtistPojo>lambdaQuery().in(ArtistPojo::getId, singerIds));
    }
    
    /**
     * 查询用户收藏专辑
     *
     * @param user    用户数据
     * @param current 当前页数
     * @param size    每页多少数据
     */
    @Override
    public List<AlbumPojo> getUserCollectAlbum(SysUserPojo user, Long current, Long size) {
        List<UserAlbumPojo> userAlbumPojoList = userAlbumService.list(Wrappers.<UserAlbumPojo>lambdaQuery()
                                                                                .eq(UserAlbumPojo::getUserId, user.getId()));
        if (CollUtil.isEmpty(userAlbumPojoList)) {
            return Collections.emptyList();
        }
        List<Long> albumIds = userAlbumPojoList.stream().map(UserAlbumPojo::getAlbumId).collect(Collectors.toList());
        return getAlbumListByAlbumId(albumIds);
    }
    
    
    /**
     * 获取用户关注歌手
     *
     * @param user 用户信息
     */
    @Override
    public List<ArtistPojo> getUserLikeSingerList(SysUserPojo user) {
        List<UserArtistPojo> userLikeSinger = userArtistService.list(Wrappers.<UserArtistPojo>lambdaQuery()
                                                                               .eq(UserArtistPojo::getUserId, user.getId()));
        if (CollUtil.isEmpty(userLikeSinger)) {
            return Collections.emptyList();
        }
        List<Long> singerIds = userLikeSinger.stream().map(UserArtistPojo::getArtistId).collect(Collectors.toList());
        return artistService.listByIds(singerIds);
    }
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param id 歌手ID
     */
    @Override
    public Integer getAlbumCountBySingerId(Long id) {
        return Math.toIntExact(albumArtistService.count(Wrappers.<AlbumArtistPojo>lambdaQuery().eq(AlbumArtistPojo::getArtistId, id)));
    }
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param id 专辑ID
     */
    @Override
    public List<MusicPojo> getMusicListByAlbumId(Long id) {
        return musicService.list(Wrappers.<MusicPojo>lambdaQuery().eq(MusicPojo::getAlbumId, id));
    }
    
    @Override
    public List<MusicPojo> getMusicListByAlbumId(Collection<Long> ids) {
        return musicService.list(Wrappers.<MusicPojo>lambdaQuery().in(MusicPojo::getAlbumId, ids));
    }
    
    /**
     * 根据歌手名查找音乐
     *
     * @param name 歌手
     */
    @Override
    public List<MusicPojo> getMusicListBySingerName(String name) {
        if (StringUtils.isNotBlank(name)) {
            List<ArtistPojo> singerList = artistService.list(Wrappers.<ArtistPojo>lambdaQuery().like(ArtistPojo::getArtistName, name));
            List<Long> singerIdsList = singerList.stream().map(ArtistPojo::getId).collect(Collectors.toList());
            List<AlbumArtistPojo> albumIds = albumArtistService.list(Wrappers.<AlbumArtistPojo>lambdaQuery()
                                                                               .eq(AlbumArtistPojo::getArtistId, singerIdsList));
            if (IterUtil.isNotEmpty(albumIds)) {
                return getMusicListByAlbumId(albumIds.stream()
                                                     .map(AlbumArtistPojo::getAlbumId)
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
    public List<MusicPojo> getMusicListBySingerId(Long id) {
        List<AlbumPojo> albumLisySingerIds = getAlbumListBySingerIds(Collections.singletonList(id));
        if (CollUtil.isEmpty(albumLisySingerIds)) {
            return Collections.emptyList();
        }
        Set<Long> albumIds = albumLisySingerIds.stream().map(AlbumPojo::getId).collect(Collectors.toSet());
        return musicService.list(Wrappers.<MusicPojo>lambdaQuery().eq(MusicPojo::getAlbumId, albumIds));
    }
    
    /**
     * 随机获取歌手
     *
     * @param count 获取数量
     */
    @Override
    public List<ArtistPojo> randomSinger(int count) {
        long sum = artistService.count();
        ArrayList<ArtistPojo> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            long randomNum = RandomUtil.randomLong(sum);
            Page<ArtistPojo> all = artistService.findAll(null, PageRequest.of((int) randomNum, 1));
            res.addAll(all.getRecords());
        }
        return res;
    }
}

package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.map.DefaultedMap;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.LyricConstant;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.TargetTagConstant;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.config.PlayListTypeConfig;
import org.core.model.MiddleTypeModel;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.QukuService;
import org.core.service.RemoteStorePicService;
import org.core.utils.CollectSortUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service("qukuService")
@Slf4j
public class QukuServiceImpl implements QukuService {
    private static final Object lock = new Object();
    private static final Object picLock = new Object();
    public static final Object addLabelLock = new Object();
    public static final Object removeLabelLock = new Object();
    
    private final TbMusicService musicService;
    
    private final TbAlbumService albumService;
    
    private final TbArtistService artistService;
    
    private final TbResourceService resourceService;
    
    private final TbUserAlbumService userAlbumService;
    
    private final TbAlbumArtistService albumArtistService;
    
    private final TbMusicArtistService musicArtistService;
    
    private final TbUserArtistService userSingerService;
    
    private final TbCollectMusicService collectMusicService;
    
    private final TbCollectService collectService;
    
    private final TbUserCollectService userCollectService;
    
    private final TbMiddleTagService middleTagService;
    
    private final TbLyricService lyricService;
    
    private final TbTagService tagService;
    
    private final AccountService accountService;
    
    private final TbPicService picService;
    
    private final TbMiddlePicService middlePicService;
    
    private final Cache<Long, TbPicPojo> picCache;
    
    private final Cache<MiddleTypeModel, Long> picMiddleCache;
    
    private final DefaultInfo defaultInfo;
    
    private final TbMvArtistService tbMvArtistService;
    
    private final TbOriginService tbOriginService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    
    private static List<AlbumConvert> getAlbumConvertList(List<TbAlbumPojo> albumPojoList, Map<Long, String> picUrl) {
        return albumPojoList.parallelStream().map(tbAlbumPojo -> {
            AlbumConvert convert = new AlbumConvert();
            BeanUtils.copyProperties(tbAlbumPojo, convert);
            convert.setPicUrl(MapUtil.get(picUrl, tbAlbumPojo.getId(), String.class));
            return convert;
        }).toList();
    }
    
    private static Map<Long, ArtistConvert> getLongArtistConvertMap(List<TbArtistPojo> tbArtistPojos, Map<Long, String> picUrl) {
        return tbArtistPojos.parallelStream().collect(Collectors.toMap(TbArtistPojo::getId, tbArtistPojo -> {
            ArtistConvert convert = new ArtistConvert();
            BeanUtils.copyProperties(tbArtistPojo, convert);
            convert.setPicUrl(MapUtil.get(picUrl, tbArtistPojo.getId(), String.class));
            return convert;
        }));
    }
    
    private static List<ArtistConvert> getArtistConvertList(List<TbArtistPojo> tbArtistPojos, Map<Long, String> picUrl) {
        return tbArtistPojos.parallelStream().map(tbArtistPojo -> {
            ArtistConvert convert = new ArtistConvert();
            BeanUtils.copyProperties(tbArtistPojo, convert);
            convert.setPicUrl(MapUtil.get(picUrl, tbArtistPojo.getId(), String.class));
            return convert;
        }).toList();
    }
    
    private static List<MusicConvert> getMusicConvertList(List<TbMusicPojo> tbMusicPojos, Map<Long, String> picUrl) {
        return tbMusicPojos.parallelStream().map(tbArtistPojo -> {
            MusicConvert convert = new MusicConvert();
            BeanUtils.copyProperties(tbArtistPojo, convert);
            convert.setPicUrl(MapUtil.get(picUrl, tbArtistPojo.getId(), String.class));
            return convert;
        }).toList();
    }
    
    private static List<CollectConvert> getCollectConvertList(List<TbCollectPojo> collectConverts, Map<Long, String> picUrl) {
        return collectConverts.parallelStream().map(tbArtistPojo -> {
            CollectConvert convert = new CollectConvert();
            BeanUtils.copyProperties(tbArtistPojo, convert);
            convert.setPicUrl(MapUtil.get(picUrl, tbArtistPojo.getId(), String.class));
            return convert;
        }).toList();
    }
    
    /**
     * 获取专辑信息
     */
    @Override
    public AlbumConvert getAlbumByMusicId(Long musicId) {
        List<AlbumConvert> albumListByMusicId = getAlbumListByMusicId(Collections.singletonList(musicId));
        if (CollUtil.isEmpty(albumListByMusicId)) {
            return null;
        }
        if (albumListByMusicId.size() != 1) {
            throw new BaseException(ResultCode.ALBUM_ERROR);
        }
        return albumListByMusicId.get(0);
    }
    
    @Override
    public AlbumConvert getAlbumByAlbumId(Long albumIds) {
        List<AlbumConvert> albumListByAlbumId = getAlbumListByAlbumId(Collections.singletonList(albumIds));
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
    public List<AlbumConvert> getAlbumListByMusicId(List<Long> musicIds) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, musicIds));
        List<Long> albumIds = list.stream().map(TbMusicPojo::getAlbumId).toList();
        return getAlbumListByAlbumId(albumIds);
    }
    
    /**
     * 通过专辑ID 获取专辑信息
     */
    @Override
    public List<AlbumConvert> getAlbumListByAlbumId(Collection<Long> albumIds) {
        List<TbAlbumPojo> albumPojoList = albumService.listByIds(albumIds);
        List<Long> collect = albumPojoList.parallelStream().map(TbAlbumPojo::getId).toList();
        Map<Long, String> picUrl = remoteStorePicService.getAlbumPicUrl(collect);
        return getAlbumConvertList(albumPojoList, picUrl);
    }
    
    /**
     * 查询数据歌曲下载地址
     * key music value url
     *
     * @param musicId 音乐ID
     */
    @Override
    public Map<Long, List<TbResourcePojo>> getMusicPathMap(Collection<Long> musicId) {
        if (CollUtil.isEmpty(musicId)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<TbResourcePojo> in = Wrappers.<TbResourcePojo>lambdaQuery().in(TbResourcePojo::getMusicId, musicId);
        List<TbResourcePojo> list = resourceService.list(in);
        return list.parallelStream()
                   .collect(Collectors.toConcurrentMap(TbResourcePojo::getMusicId, ListUtil::toList, (objects, objects2) -> {
                       objects2.addAll(objects);
                       return objects2;
                   }));
    }
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    @Override
    public List<ArtistConvert> getAlbumArtistListByMusicId(List<Long> musicIds) {
        List<TbMusicPojo> musicPojoList = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, musicIds));
        if (CollUtil.isEmpty(musicPojoList)) {
            return Collections.emptyList();
        }
        Set<Long> albumIds = musicPojoList.stream().map(TbMusicPojo::getAlbumId).collect(Collectors.toSet());
        List<TbAlbumArtistPojo> list = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, albumIds));
        return getTbSingerPojoList(CollUtil.isEmpty(list), list.stream().map(TbAlbumArtistPojo::getArtistId));
    }
    
    /**
     * 批量获取歌手信息，优化版本
     * key 值为音乐ID
     * value 为歌手
     *
     * @param albumPojoMap key Music, value ID 专辑信息
     */
    @Override
    public Map<Long, List<ArtistConvert>> getAlbumArtistListByMusicIdToMap(Map<Long, TbAlbumPojo> albumPojoMap) {
        Set<Long> collect = albumPojoMap.values().parallelStream().map(TbAlbumPojo::getId).collect(Collectors.toSet());
        if (CollUtil.isEmpty(collect)) {
            return MapUtil.empty();
        }
        List<TbAlbumArtistPojo> list = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, collect));
        
        Map<Long, List<TbAlbumArtistPojo>> albumArtistMap = new HashMap<>();
        for (TbAlbumArtistPojo tbAlbumArtistPojo : list) {
            List<TbAlbumArtistPojo> pojos = albumArtistMap.get(tbAlbumArtistPojo.getAlbumId());
            if (pojos == null) {
                ArrayList<TbAlbumArtistPojo> value = new ArrayList<>();
                value.add(tbAlbumArtistPojo);
                albumArtistMap.put(tbAlbumArtistPojo.getAlbumId(), value);
            } else {
                pojos.add(tbAlbumArtistPojo);
                albumArtistMap.put(tbAlbumArtistPojo.getAlbumId(), pojos);
            }
        }
        
        Set<Long> artistIds = list.parallelStream().map(TbAlbumArtistPojo::getArtistId).collect(Collectors.toSet());
        List<TbArtistPojo> artistPojoList = artistService.listByIds(artistIds);
        
        Map<Long, String> picUrl = remoteStorePicService.getArtistPicUrl(artistIds);
        Map<Long, ArtistConvert> artistMap = getLongArtistConvertMap(artistPojoList, picUrl);
        
        Map<Long, List<ArtistConvert>> resMap = new HashMap<>();
        for (Map.Entry<Long, TbAlbumPojo> longTbAlbumPojoEntry : albumPojoMap.entrySet()) {
            TbAlbumPojo tbAlbumPojo = longTbAlbumPojoEntry.getValue();
            List<TbAlbumArtistPojo> tbAlbumArtistPojos = albumArtistMap.get(tbAlbumPojo.getId()) == null
                    ? new ArrayList<>()
                    : albumArtistMap.get(tbAlbumPojo.getId());
            List<ArtistConvert> tbArtistPojos = tbAlbumArtistPojos.parallelStream()
                                                                  .map(tbAlbumArtistPojo -> artistMap.get(tbAlbumArtistPojo.getArtistId()))
                                                                  .toList();
            resMap.put(longTbAlbumPojoEntry.getKey(), tbArtistPojos);
        }
        return resMap;
    }
    
    /**
     * 获取歌手信息
     */
    @Override
    public List<ArtistConvert> getAlbumArtistByMusicId(Long musicId) {
        return getAlbumArtistListByMusicId(Collections.singletonList(musicId));
    }
    
    @Override
    public List<TbResourcePojo> getMusicPaths(Collection<Long> musicId) {
        LambdaQueryWrapper<TbResourcePojo> in = Wrappers.<TbResourcePojo>lambdaQuery().in(TbResourcePojo::getMusicId, musicId);
        return resourceService.list(in);
    }
    
    /**
     * 查询专辑下音乐数量
     *
     * @param albumId 专辑ID
     */
    @Override
    public Integer getAlbumMusicCountByAlbumId(Long albumId) {
        long count = musicService.count(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getAlbumId, albumId));
        return Math.toIntExact(count);
    }
    
    /**
     * 专辑下所有音乐数量
     *
     * @param albumIds 专辑ID
     * @return 专辑音乐数量
     */
    @Override
    public Map<Long, Integer> getAlbumMusicCountByMapAlbumId(Collection<Long> albumIds) {
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, albumIds));
        return list.parallelStream().collect(Collectors.toMap(TbMusicPojo::getAlbumId, tbMusicPojo -> 1, Integer::sum));
    }
    
    /**
     * 查询专辑下音乐数量
     *
     * @param musicId 歌曲ID
     */
    @Override
    public Integer getAlbumMusicCountByMusicId(Long musicId) {
        long count = musicService.count(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getId, musicId));
        return Math.toIntExact(count);
    }
    
    /**
     * 通过歌手获取歌手拥有的音乐数量
     *
     * @param id 歌手ID
     */
    @Override
    public Long getMusicCountBySingerId(Long id) {
        List<TbAlbumArtistPojo> albumSingerPojoList = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                                      .eq(TbAlbumArtistPojo::getArtistId, id));
        if (CollUtil.isEmpty(albumSingerPojoList)) {
            return 0L;
        }
        Set<Long> albumIds = albumSingerPojoList.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toSet());
        return musicService.count(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, albumIds));
    }
    
    /**
     * 随即获取曲库中的一条数据
     */
    @Override
    public MusicConvert randomMusic() {
        long count = musicService.count();
        Page<TbMusicPojo> page = new Page<>(RandomUtil.randomLong(0, count), 1);
        musicService.page(page);
        TbMusicPojo musicPojo = Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>()).get(0);
        MusicConvert convert = new MusicConvert();
        BeanUtils.copyProperties(musicPojo, convert);
        convert.setPicUrl(remoteStorePicService.getMusicPicUrl(musicPojo.getId()));
        return convert;
    }
    
    
    /**
     * 随即获取曲库中的多条数据
     *
     * @param count    歌曲数量
     * @param genre    歌曲流派
     * @param fromYear 仅返回今年之后或今年内发布的歌曲。
     * @param toYear   只返回今年之前或今年出版的歌曲
     */
    @Override
    public List<MusicConvert> randomMusicList(int count, String genre, Long fromYear, Long toYear) {
        long sumCount = musicService.count();
        int pageCount = PageUtil.totalPage((int) sumCount, count);
        long randomOffset = RandomUtils.nextLong(0, pageCount);
        Page<TbMusicPojo> page = new Page<>(RandomUtil.randomLong(0, randomOffset), count);
        boolean genreFlag = StringUtils.isNotBlank(genre);
        
        if (genreFlag) {
            LambdaQueryWrapper<TbTagPojo> eq = Wrappers.<TbTagPojo>lambdaQuery().eq(TbTagPojo::getTagName, genre);
            List<TbTagPojo> list = tagService.list(eq);
            List<Long> tagIds = list.parallelStream().map(TbTagPojo::getId).toList();
            if (CollUtil.isNotEmpty(tagIds)) {
                List<TbMiddleTagPojo> middleList = middleTagService.list(Wrappers.<TbMiddleTagPojo>lambdaQuery().in(TbMiddleTagPojo::getTagId, tagIds));
                if (CollUtil.isNotEmpty(middleList)) {
                    List<Long> middleTagIds = middleList.parallelStream().map(TbMiddleTagPojo::getTagId).toList();
                    LambdaQueryWrapper<TbMusicPojo> wrappers = Wrappers.<TbMusicPojo>lambdaQuery()
                                                                       .in(TbMusicPojo::getId, middleTagIds)
                                                                       .le(Objects.nonNull(toYear), TbMusicPojo::getPublishTime, new Date(toYear))
                                                                       .ge(Objects.nonNull(fromYear), TbMusicPojo::getPublishTime, new Date(fromYear));
                    musicService.page(page, wrappers);
                }
            }
        } else {
            LambdaQueryWrapper<TbMusicPojo> wrappers = Wrappers.<TbMusicPojo>lambdaQuery()
                                                               .le(Objects.nonNull(toYear), TbMusicPojo::getPublishTime, new Date(toYear))
                                                               .ge(Objects.nonNull(fromYear), TbMusicPojo::getPublishTime, new Date(fromYear));
            musicService.page(page, wrappers);
        }
        List<TbMusicPojo> tbMusicPojos = Optional.ofNullable(page.getRecords()).orElse(new ArrayList<>());
        return getMusicConvertList(tbMusicPojos, remoteStorePicService.getMusicPicUrl(tbMusicPojos.parallelStream().map(TbMusicPojo::getId).toList()));
    }
    
    @Override
    public Page<AlbumConvert> getRandomAlbum(String area, Long offset, Long limit) {
        Page<TbAlbumPojo> page = new Page<>(offset, limit);
        albumService.page(page);
        return getAlbumConvertPage(page);
    }
    
    /**
     * @param albumPojoPage      专辑分页参数
     * @param lambdaQueryWrapper 查询参数
     * @return 返回数据
     */
    @Override
    public Page<AlbumConvert> getAlbumPage(Page<TbAlbumPojo> albumPojoPage, Wrapper<TbAlbumPojo> lambdaQueryWrapper) {
        albumService.page(albumPojoPage, lambdaQueryWrapper);
        return getAlbumConvertPage(albumPojoPage);
    }
    
    @NotNull
    private Page<AlbumConvert> getAlbumConvertPage(Page<TbAlbumPojo> albumPojoPage) {
        Map<Long, String> picUrl = remoteStorePicService.getAlbumPicUrl(albumPojoPage.getRecords().parallelStream().map(TbAlbumPojo::getId).toList());
        Page<AlbumConvert> convertPage = new Page<>();
        BeanUtils.copyProperties(albumPojoPage, convertPage);
        convertPage.setRecords(getAlbumConvertList(albumPojoPage.getRecords(), picUrl));
        return convertPage;
    }
    
    /**
     * 获取专辑歌手列表
     */
    @Override
    public List<ArtistConvert> getAlbumArtistListByAlbumIds(Long albumId) {
        return getAlbumArtistListByAlbumIds(Collections.singletonList(albumId));
    }
    
    @Override
    public List<AlbumConvert> getAlbumListByArtistIds(List<Long> artistIds) {
        if (CollUtil.isEmpty(artistIds)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbAlbumArtistPojo> in = Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getArtistId, artistIds);
        List<TbAlbumArtistPojo> list = albumArtistService.list(in);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        return getAlbumListByAlbumId(list.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toSet()));
    }
    
    /**
     * 获取Mv歌手
     *
     * @param mvIds 用户信息
     */
    @Override
    public Map<Long, List<ArtistConvert>> getMvArtistByMvIdToMap(List<Long> mvIds) {
        if (CollUtil.isEmpty(mvIds)) {
            return Collections.emptyMap();
        }
        List<TbMvArtistPojo> list = tbMvArtistService.list(Wrappers.<TbMvArtistPojo>lambdaQuery().in(TbMvArtistPojo::getMvId, mvIds));
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<Long, ArrayList<Long>> mvIdArtistIdMap = list.parallelStream()
                                                         .collect(Collectors.toMap(TbMvArtistPojo::getMvId,
                                                                 tbMvArtistPojo -> ListUtil.toList(tbMvArtistPojo.getArtistId()),
                                                                 (objects, objects2) -> {
                                                                     objects2.addAll(objects);
                                                                     return objects2;
                                                                 }));
        List<Long> artistIds = list.parallelStream().map(TbMvArtistPojo::getArtistId).toList();
        List<TbArtistPojo> artistList = artistService.listByIds(artistIds);
        List<ArtistConvert> artistConvertList = getArtistConvertList(artistList, remoteStorePicService.getCollectPicUrl(artistIds));
        Map<Long, ArtistConvert> artistById = artistConvertList.parallelStream()
                                                               .collect(Collectors.toMap(ArtistConvert::getId, artistConvert -> artistConvert));
        HashMap<Long, List<ArtistConvert>> map = new HashMap<>();
        for (Map.Entry<Long, ArrayList<Long>> longLongEntry : mvIdArtistIdMap.entrySet()) {
            Long key = longLongEntry.getKey();
            ArrayList<Long> value = longLongEntry.getValue();
            ArrayList<ArtistConvert> artistListValue = new ArrayList<>(value.size());
            value.forEach(aLong -> artistListValue.add(artistById.get(aLong)));
            map.put(key, artistListValue);
        }
        return map;
    }
    
    /**
     * 通过专辑ID获取歌手列表
     */
    @Override
    public List<ArtistConvert> getAlbumArtistListByAlbumIds(List<Long> albumIds) {
        List<TbAlbumArtistPojo> list = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, albumIds));
        return getTbSingerPojoList(CollUtil.isEmpty(list), list.stream().map(TbAlbumArtistPojo::getArtistId));
    }
    
    /**
     * 获取专辑歌手列表
     * Map
     * key to Album ID
     * value to Artist List
     *
     * @param albumIds 专辑ID
     */
    @Override
    public Map<Long, List<ArtistConvert>> getAlbumArtistMapByAlbumIds(Collection<Long> albumIds) {
        if (CollUtil.isEmpty(albumIds)) {
            return Collections.emptyMap();
        }
        List<TbAlbumArtistPojo> list = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, albumIds));
        if (CollUtil.isEmpty(list)) {
            return new HashMap<>();
        }
        List<Long> artists = list.parallelStream().map(TbAlbumArtistPojo::getArtistId).toList();
        List<TbArtistPojo> tbArtistPojos = artistService.list(Wrappers.<TbArtistPojo>lambdaQuery().in(TbArtistPojo::getId, artists));
        Map<Long, String> picUrl = remoteStorePicService.getArtistPicUrl(artists);
        Map<Long, ArtistConvert> collect = getLongArtistConvertMap(tbArtistPojos, picUrl);
        return list.parallelStream()
                   .collect(Collectors.toMap(TbAlbumArtistPojo::getAlbumId,
                           tbAlbumArtistPojo -> ListUtil.toList(collect.get(tbAlbumArtistPojo.getArtistId())),
                           (tbArtistPojo, tbArtistPojo2) -> {
                               tbArtistPojo2.addAll(tbArtistPojo);
                               return tbArtistPojo2;
                           }));
    }
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    @Override
    public List<ArtistConvert> getMusicArtistByMusicId(Collection<Long> musicId) {
        if (CollUtil.isEmpty(musicId)) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbMusicArtistPojo> eq = Wrappers.<TbMusicArtistPojo>lambdaQuery().in(TbMusicArtistPojo::getMusicId, musicId);
        List<TbMusicArtistPojo> list = musicArtistService.list(eq);
        Collection<Long> collect = list.parallelStream().map(TbMusicArtistPojo::getArtistId).collect(Collectors.toSet());
        if (CollUtil.isEmpty(collect)) {
            return Collections.emptyList();
        }
        List<TbArtistPojo> tbArtistPojos = artistService.listByIds(collect);
        return getArtistConvertList(tbArtistPojos, remoteStorePicService.getCollectPicUrl(collect));
    }
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    @Override
    public Map<Long, List<ArtistConvert>> getMusicArtistByMusicIdToMap(Collection<Long> musicId) {
        if (CollUtil.isEmpty(musicId)) {
            return Collections.emptyMap();
        }
        List<TbMusicArtistPojo> list = musicArtistService.list(Wrappers.<TbMusicArtistPojo>lambdaQuery().in(TbMusicArtistPojo::getMusicId, musicId));
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        List<Long> artistIds = list.parallelStream().map(TbMusicArtistPojo::getArtistId).toList();
        List<TbArtistPojo> tbArtistPojos = artistService.listByIds(artistIds);
        Map<Long, ArtistConvert> artistMap = getLongArtistConvertMap(tbArtistPojos, remoteStorePicService.getArtistPicUrl(artistIds));
        
        HashMap<Long, List<ArtistConvert>> longListHashMap = new HashMap<>();
        for (TbMusicArtistPojo tbMusicArtistPojo : list) {
            Long artistId = tbMusicArtistPojo.getArtistId();
            ArtistConvert tbArtistPojo = artistMap.get(artistId);
            List<ArtistConvert> tbArtistPojos1 = longListHashMap.get(tbMusicArtistPojo.getMusicId());
            if (CollUtil.isEmpty(tbArtistPojos1)) {
                longListHashMap.put(tbMusicArtistPojo.getMusicId(), CollUtil.newArrayList(tbArtistPojo));
            } else {
                tbArtistPojos1.add(tbArtistPojo);
                longListHashMap.put(tbMusicArtistPojo.getMusicId(), tbArtistPojos1);
            }
        }
        
        return longListHashMap;
    }
    
    /**
     * 通过专辑ID获取歌手
     *
     * @param empty      是否执行
     * @param longStream 专辑ID流
     */
    private List<ArtistConvert> getTbSingerPojoList(boolean empty, Stream<Long> longStream) {
        if (empty) {
            return Collections.emptyList();
        }
        List<Long> artistIds = longStream.toList();
        List<TbArtistPojo> list = artistService.list(Wrappers.<TbArtistPojo>lambdaQuery().in(TbArtistPojo::getId, artistIds));
        Map<Long, String> picUrl = remoteStorePicService.getArtistPicUrl(artistIds);
        return getArtistConvertList(list, picUrl);
    }
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param id 歌手ID
     */
    @Override
    public Integer getArtistAlbumCountBySingerId(Long id) {
        return Math.toIntExact(albumArtistService.count(Wrappers.<TbAlbumArtistPojo>lambdaQuery().eq(TbAlbumArtistPojo::getArtistId, id)));
    }
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param artistIds 歌手ID
     * @return 歌手下的所有专辑
     */
    @Override
    public Map<Long, Integer> getArtistAlbumCount(List<Long> artistIds) {
        List<TbAlbumArtistPojo> list = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getArtistId, artistIds));
        return list.parallelStream().collect(Collectors.toMap(TbAlbumArtistPojo::getArtistId, tbAlbumArtistPojo -> 1, Integer::sum));
    }
    
    /**
     * 查询用户收藏专辑
     *
     * @param user    用户数据
     * @param current 当前页数
     * @param size    每页多少数据
     */
    @Override
    public List<AlbumConvert> getUserCollectAlbum(SysUserPojo user, Long current, Long size) {
        List<TbUserAlbumPojo> userAlbumPojoList = userAlbumService.list(Wrappers.<TbUserAlbumPojo>lambdaQuery()
                                                                                .eq(TbUserAlbumPojo::getUserId, user.getId()));
        if (CollUtil.isEmpty(userAlbumPojoList)) {
            return Collections.emptyList();
        }
        List<Long> albumIds = userAlbumPojoList.stream().map(TbUserAlbumPojo::getAlbumId).toList();
        return getAlbumListByAlbumId(albumIds);
    }
    
    /**
     * 获取用户关注歌手
     *
     * @param uid 用户信息
     */
    @Override
    public List<ArtistConvert> getUserLikeSingerList(Long uid) {
        List<TbUserArtistPojo> userLikeSinger = userSingerService.list(Wrappers.<TbUserArtistPojo>lambdaQuery()
                                                                               .eq(TbUserArtistPojo::getUserId, uid));
        if (CollUtil.isEmpty(userLikeSinger)) {
            return Collections.emptyList();
        }
        List<Long> aritstIds = userLikeSinger.stream().map(TbUserArtistPojo::getArtistId).toList();
        return getArtistConvertList(artistService.listByIds(aritstIds), remoteStorePicService.getArtistPicUrl(aritstIds));
    }
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param id 专辑ID
     */
    @Override
    public List<MusicConvert> getMusicListByAlbumId(Long id) {
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getAlbumId, id));
        return getMusicConvertList(list, remoteStorePicService.getMusicPicUrl(list.parallelStream().map(TbMusicPojo::getId).toList()));
    }
    
    @Override
    public List<MusicConvert> getMusicListByAlbumId(Collection<Long> ids) {
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, ids));
        return getMusicConvertList(list, remoteStorePicService.getMusicPicUrl(list.parallelStream().map(TbMusicPojo::getId).toList()));
    }
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param ids 专辑ID
     */
    @Override
    public Map<Long, List<MusicConvert>> getMusicMapByAlbumId(Collection<Long> ids) {
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getAlbumId, ids));
        List<MusicConvert> musicConvertList = getMusicConvertList(list,
                remoteStorePicService.getMusicPicUrl(list.parallelStream().map(TbMusicPojo::getId).toList()));
        return musicConvertList.parallelStream().collect(Collectors.toMap(TbMusicPojo::getAlbumId, ListUtil::toList, (o1, o2) -> {
            o2.addAll(o1);
            return o2;
        }));
    }
    
    /**
     * 根据歌手名查找音乐
     *
     * @param name 歌手
     */
    @Override
    public List<MusicConvert> getMusicListByArtistName(String name) {
        if (StringUtils.isNotBlank(name)) {
            List<TbArtistPojo> singerList = artistService.list(Wrappers.<TbArtistPojo>lambdaQuery().like(TbArtistPojo::getArtistName, name));
            List<Long> singerIdsList = singerList.stream().map(TbArtistPojo::getId).toList();
            if (CollUtil.isEmpty(singerList)) {
                return Collections.emptyList();
            }
            List<TbAlbumArtistPojo> albumIds = albumArtistService.list(Wrappers.<TbAlbumArtistPojo>lambdaQuery()
                                                                               .in(TbAlbumArtistPojo::getArtistId, singerIdsList));
            if (IterUtil.isNotEmpty(albumIds)) {
                return getMusicListByAlbumId(albumIds.stream().map(TbAlbumArtistPojo::getAlbumId).collect(Collectors.toSet()));
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
    public List<MusicConvert> getMusicListByArtistId(Long id) {
        List<TbMusicArtistPojo> musicArtistPojos = musicArtistService.list(Wrappers.<TbMusicArtistPojo>lambdaQuery()
                                                                                   .eq(TbMusicArtistPojo::getArtistId, id));
        if (CollUtil.isEmpty(musicArtistPojos)) {
            return Collections.emptyList();
        }
        List<Long> collect = musicArtistPojos.parallelStream().map(TbMusicArtistPojo::getMusicId).toList();
        
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, collect));
        return getMusicConvertList(list, remoteStorePicService.getMusicPicUrl(list.parallelStream().map(TbMusicPojo::getId).toList()));
    }
    
    /**
     * 获取歌手下的所有音乐
     *
     * @param artistIds 歌手ID
     * @return key:artistId value: musicList
     */
    @Override
    public Map<Long, List<TbMusicPojo>> getMusicMapByArtistId(List<Long> artistIds) {
        List<TbMusicArtistPojo> musicArtistPojos = musicArtistService.list(Wrappers.<TbMusicArtistPojo>lambdaQuery()
                                                                                   .eq(TbMusicArtistPojo::getArtistId, artistIds));
        if (CollUtil.isEmpty(musicArtistPojos)) {
            return Collections.emptyMap();
        }
        List<Long> collect = musicArtistPojos.parallelStream().map(TbMusicArtistPojo::getMusicId).toList();
        Map<Long, Long> musicArtistsMap = musicArtistPojos.parallelStream()
                                                          .collect(Collectors.toMap(TbMusicArtistPojo::getMusicId, TbMusicArtistPojo::getArtistId));
        
        List<TbMusicPojo> list = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, collect));
        List<MusicConvert> musicConvertList = getMusicConvertList(list,
                remoteStorePicService.getMusicPicUrl(list.parallelStream().map(TbMusicPojo::getId).toList()));
        return musicConvertList.parallelStream()
                               .collect(Collectors.toMap(musicConvert -> musicArtistsMap.get(musicConvert.getId()), ListUtil::toList, (o1, o2) -> {
                                   o2.addAll(o1);
                                   return o2;
                               }));
    }
    
    /**
     * 随机获取歌手
     *
     * @param count 获取数量
     */
    @Override
    public List<ArtistConvert> randomSinger(int count) {
        long sum = artistService.count();
        ArrayList<TbArtistPojo> res = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            long randomNum = RandomUtil.randomLong(sum);
            Page<TbArtistPojo> page = artistService.page(new Page<>(randomNum, 1));
            res.addAll(page.getRecords());
        }
        return getArtistConvertList(res, remoteStorePicService.getArtistPicUrl(res.parallelStream().map(TbArtistPojo::getId).toList()));
    }
    
    /**
     * 添加音乐到歌单
     *
     * @param userID    用户ID
     * @param collectId 歌单数据
     * @param songIds   歌曲列表
     * @param flag      删除还是添加
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addOrRemoveMusicToCollect(Long userID, Long collectId, List<Long> songIds, boolean flag) {
        if (CollUtil.isEmpty(songIds)) {
            return;
        }
        if (flag) {
            // 查询歌单内歌曲是否存在, 存在则删除
            List<TbCollectMusicPojo> collectMusicPojoList = collectMusicService.list(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                                             .eq(TbCollectMusicPojo::getCollectId, collectId)
                                                                                             .in(TbCollectMusicPojo::getMusicId, songIds));
            List<Long> collectMusicIds = collectMusicPojoList.parallelStream().map(TbCollectMusicPojo::getMusicId).toList();
            songIds.removeAll(collectMusicIds);
            
            // 添加
            List<TbMusicPojo> tbMusicPojo = musicService.listByIds(songIds);
            
            synchronized (lock) {
                long allCount = getCollectCount(collectId);
                List<TbCollectMusicPojo> collect = new ArrayList<>(tbMusicPojo.size());
                for (TbMusicPojo musicPojo : tbMusicPojo) {
                    TbCollectMusicPojo tbCollectMusicPojo = new TbCollectMusicPojo();
                    tbCollectMusicPojo.setCollectId(collectId);
                    tbCollectMusicPojo.setMusicId(musicPojo.getId());
                    allCount++;
                    tbCollectMusicPojo.setSort(allCount);
                    collect.add(tbCollectMusicPojo);
                }
                collectMusicService.saveBatch(collect);
            }
            log.info("add collect music: {}, music list{}", collectId, CollUtil.join(songIds, ","));
        } else {
            // 删除歌曲
            collectMusicService.remove(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                               .eq(TbCollectMusicPojo::getCollectId, collectId)
                                               .in(TbCollectMusicPojo::getMusicId, songIds));
            log.debug("remove playlist: {}, music list: {}", collectId, CollUtil.join(songIds, ","));
        }
    }
    
    /**
     * 检查用户是否有权限操作歌单
     *
     * @param userId        用户ID
     * @param tbCollectPojo 歌单信息
     */
    public static void checkUserAuth(Long userId, TbCollectPojo tbCollectPojo) {
        // 检查是否有该歌单
        if (tbCollectPojo == null || tbCollectPojo.getUserId() == null) {
            throw new BaseException(ResultCode.SONG_LIST_DOES_NOT_EXIST);
        }
        // 检查用户是否有权限
        if (!userId.equals(tbCollectPojo.getUserId())) {
            log.warn(ResultCode.PERMISSION_NO_ACCESS.getResultMsg());
            throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
        }
    }
    
    /**
     * 添加歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @param type   歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单
     * @return 歌单创建信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CollectConvert createPlayList(Long userId, String name, byte type) {
        TbCollectPojo collectPojo = new TbCollectPojo();
        synchronized (picLock) {
            // 歌单表保存
            collectPojo.setUserId(userId);
            collectPojo.setPlayListName(name);
            collectPojo.setSort(collectService.count() + 1);
            collectPojo.setType(type);
            collectService.save(collectPojo);
        }
        // 保存用户关联表
        TbUserCollectPojo entity = new TbUserCollectPojo();
        entity.setCollectId(collectPojo.getId());
        entity.setUserId(userId);
        userCollectService.save(entity);
        
        remoteStorePicService.saveOrUpdateCollectPicUrl(collectPojo.getId(), defaultInfo.getPic().getPlayListPic());
        
        // 封面查询
        CollectConvert convert = new CollectConvert();
        BeanUtils.copyProperties(collectPojo, convert);
        convert.setPicUrl(remoteStorePicService.getCollectPicUrl(entity.getCollectId()));
        return convert;
    }
    
    /**
     * 删除歌单
     *
     * @param userId     用户ID
     * @param collectIds 删除歌单ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removePlayList(Long userId, Collection<Long> collectIds) {
        if (CollUtil.isEmpty(collectIds)) {
            return;
        }
        List<TbCollectPojo> tbCollectPojos = collectService.listByIds(collectIds);
        for (TbCollectPojo tbCollectPojo : tbCollectPojos) {
            checkUserAuth(userId, tbCollectPojo);
        }
        // 删除歌单关联tag
        collectIds.forEach(this::removeLabelAll);
        // 删除封面
        remoteStorePicService.removePicIds(new ArrayList<>(collectIds), Collections.singletonList(PicTypeConstant.PLAYLIST));
        // 删除歌单关联ID
        collectMusicService.remove(Wrappers.<TbCollectMusicPojo>lambdaQuery().in(TbCollectMusicPojo::getCollectId, collectIds));
        userCollectService.remove(Wrappers.<TbUserCollectPojo>lambdaQuery().in(TbUserCollectPojo::getCollectId, collectIds));
        // 删除歌单ID
        collectService.removeByIds(collectIds);
    }
    
    /**
     * 获取用户所有音乐，包括喜爱歌单
     *
     * @param uid  用户ID
     * @param type 歌单类型
     * @return 返回用户创建歌单
     */
    @Override
    public List<CollectConvert> getUserPlayList(Long uid, Collection<Byte> type) {
        LambdaQueryWrapper<TbCollectPojo> queryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                 .eq(TbCollectPojo::getUserId, uid)
                                                                 .in(CollUtil.isNotEmpty(type), TbCollectPojo::getType, type);
        List<TbCollectPojo> list = collectService.list(queryWrapper);
        List<TbCollectPojo> collectPojoList = CollectSortUtil.userLikeUserSort(uid, list);
        
        if (CollUtil.isEmpty(collectPojoList)) {
            return Collections.emptyList();
        }
        return getCollectConvertList(collectPojoList,
                remoteStorePicService.getCollectPicUrl(collectPojoList.parallelStream().map(TbCollectPojo::getId).toList()));
    }
    
    /**
     * 获取歌单音乐数量
     *
     * @param id 歌单ID
     * @return Long 歌单ID Integer 音乐数量
     */
    @Override
    public Integer getCollectMusicCount(Long id) {
        long count = collectMusicService.count(Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, id));
        return Math.toIntExact(count);
    }
    
    /**
     * 获取歌曲歌词
     *
     * @param musicId 歌词ID
     * @return 歌词列表
     */
    @Override
    public List<TbLyricPojo> getMusicLyric(Long musicId) {
        return lyricService.list(Wrappers.<TbLyricPojo>lambdaQuery().eq(TbLyricPojo::getMusicId, musicId));
    }
    
    /**
     * 获取歌曲歌词
     *
     * @param musicId 歌词ID
     * @return 歌词列表 Long -> music id
     */
    @Override
    public Map<Long, List<TbLyricPojo>> getMusicLyric(Collection<Long> musicId) {
        List<TbLyricPojo> list = lyricService.list(Wrappers.<TbLyricPojo>lambdaQuery().eq(TbLyricPojo::getMusicId, musicId));
        return list.parallelStream().collect(Collectors.toMap(TbLyricPojo::getMusicId, Arrays::asList, (o, o2) -> {
            o2.addAll(o);
            return o2;
        }));
    }
    
    /**
     * 获取tag
     *
     * @param target tag类型 0流派 1歌曲 2歌单
     * @param ids    歌单，音乐，专辑
     * @return tag列表
     */
    @Override
    public Map<Long, List<TbTagPojo>> getLabel(Byte target, Collection<Long> ids, List<String> tagName) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        LambdaQueryWrapper<TbMiddleTagPojo> wrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                              .in(TbMiddleTagPojo::getMiddleId, ids)
                                                              .eq(TbMiddleTagPojo::getType, target);
        List<TbMiddleTagPojo> middleTagList = middleTagService.list(wrapper);
        if (CollUtil.isEmpty(middleTagList)) {
            return ids.parallelStream().collect(Collectors.toMap(Long::longValue, aLong -> Collections.emptyList()));
        }
        Map<Long, TbMiddleTagPojo> collect = middleTagList.parallelStream()
                                                          .collect(Collectors.toMap(TbMiddleTagPojo::getTagId, tbMiddleTagPojo -> tbMiddleTagPojo));
        List<Long> tagIds = middleTagList.parallelStream().map(TbMiddleTagPojo::getTagId).toList();
        List<TbTagPojo> tbTagPojos = tagService.list(Wrappers.<TbTagPojo>lambdaQuery()
                                                             .in(TbTagPojo::getId, tagIds)
                                                             .in(CollUtil.isNotEmpty(tagName), TbTagPojo::getTagName, tagName));
        Map<Long, List<TbTagPojo>> resultMap = tbTagPojos.parallelStream()
                                                         .collect(Collectors.toMap(tbTagPojo -> collect.get(tbTagPojo.getId()).getMiddleId(),
                                                                 tbTagPojo -> {
                                                                     LinkedList<TbTagPojo> add = new LinkedList<>();
                                                                     add.add(tbTagPojo);
                                                                     return add;
                                                                 },
                                                                 (tbTagPojos1, tbTagPojos2) -> {
                                                                     tbTagPojos2.addAll(tbTagPojos1);
                                                                     return tbTagPojos2;
                                                                 }));
        return DefaultedMap.defaultedMap(resultMap, Collections.emptyList());
    }
    
    /**
     * 获取tag Map
     *
     * @param target tag类型 0流派 1歌曲 2歌单
     * @param ids    歌单，音乐，专辑
     * @return tag列表 Long -> id
     */
    @Override
    public Map<Long, List<TbTagPojo>> getLabel(Byte target, Set<Long> ids) {
        LambdaQueryWrapper<TbMiddleTagPojo> wrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                              .in(TbMiddleTagPojo::getMiddleId, ids)
                                                              .eq(TbMiddleTagPojo::getType, target);
        List<TbMiddleTagPojo> middleTagList = middleTagService.list(wrapper);
        if (CollUtil.isEmpty(middleTagList)) {
            return Collections.emptyMap();
        }
        Map<Long, TbTagPojo> tagMap = tagService.listByIds(middleTagList.parallelStream().map(TbMiddleTagPojo::getTagId).toList())
                                                .parallelStream()
                                                .collect(Collectors.toMap(TbTagPojo::getId, tbTagPojo -> tbTagPojo));
        Map<Long, List<Long>> middleTagMap = middleTagList.parallelStream()
                                                          .collect(Collectors.toMap(TbMiddleTagPojo::getMiddleId,
                                                                  tbMiddleTagPojo -> ListUtil.toList(tbMiddleTagPojo.getTagId()),
                                                                  (objects, objects2) -> {
                                                                      objects2.addAll(objects);
                                                                      return objects2;
                                                                  }));
        
        LinkedHashMap<Long, List<TbTagPojo>> map = new LinkedHashMap<>();
        for (Long id : ids) {
            List<Long> longs = Optional.ofNullable(middleTagMap.get(id)).orElse(Collections.emptyList());
            List<TbTagPojo> list = longs.parallelStream().map(tagMap::get).toList();
            map.put(id, list);
        }
        return map;
    }
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param labels 标签名
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addLabel(Byte target, Long id, List<String> labels) {
        if (CollUtil.isEmpty(labels)) {
            return;
        }
        // 新增tag
        List<TbTagPojo> list;
        synchronized (addLabelLock) {
            HashSet<String> hashSet = new HashSet<>(labels);
            list = tagService.list(Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getTagName, hashSet));
            Map<String, TbTagPojo> collect = list.parallelStream().collect(Collectors.toMap(TbTagPojo::getTagName, tbTagPojo -> tbTagPojo));
            for (String label : hashSet) {
                if (StringUtils.isNotBlank(label) && collect.get(label) == null) {
                    TbTagPojo entity = new TbTagPojo();
                    entity.setCount(0);
                    entity.setTagName(label);
                    list.add(entity);
                }
            }
            tagService.saveOrUpdateBatch(list);
        }
        Set<Long> tagIds = list.parallelStream().map(TbTagPojo::getId).collect(Collectors.toSet());
        // 关联到对应ID
        addLabel(target, id, tagIds);
    }
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param tagIds 标签ID
     */
    @Override
    public void addLabel(Byte target, Long id, Set<Long> tagIds) {
        LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.<TbMiddleTagPojo>lambdaQuery().eq(TbMiddleTagPojo::getMiddleId, id);
        
        // 删除重新添加
        List<TbMiddleTagPojo> middleTagPojoList = middleTagService.list(eq);
        if (CollUtil.isNotEmpty(middleTagPojoList)) {
            List<Long> removeTagIds = new ArrayList<>();
            List<Long> middleTagIds = middleTagPojoList.parallelStream().map(TbMiddleTagPojo::getTagId).toList();
            // 删除关联tag后，tag标签自动减一。并且到零时自动删除
            for (TbTagPojo tbTagPojo : tagService.listByIds(middleTagIds)) {
                tbTagPojo.setCount(tbTagPojo.getCount() - 1);
                if (tbTagPojo.getCount() == 0) {
                    removeTagIds.add(tbTagPojo.getId());
                }
            }
            tagService.removeBatchByIds(removeTagIds);
            middleTagService.removeBatchByIds(middleTagIds);
        }
        
        // 添加tag关联
        List<TbMiddleTagPojo> middleTagPojos = tagIds.parallelStream().map(aLong -> new TbMiddleTagPojo(null, id, aLong, target)).toList();
        middleTagService.saveOrUpdateBatch(middleTagPojos);
        
        // 关联后tag关联数自动加1
        List<TbTagPojo> tagPojoList = tagService.listByIds(tagIds);
        for (TbTagPojo tbTagPojo : tagPojoList) {
            tbTagPojo.setCount(tbTagPojo.getCount() + 1);
        }
        tagService.updateBatchById(tagPojoList);
        
    }
    
    /**
     * 删除全部tag
     *
     * @param id 音乐，歌单， 专辑
     * @deprecated 已被启用，请使用 {@link QukuService#removeLabel(List, byte)}
     */
    @Override
    @Deprecated(since = "1.0")
    public void removeLabelAll(Long id) {
        synchronized (removeLabelLock) {
            LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.<TbMiddleTagPojo>lambdaQuery().eq(TbMiddleTagPojo::getMiddleId, id);
            // 查询出所有tag关联数据
            List<TbMiddleTagPojo> tbMiddleTagPojoList = middleTagService.list(eq);
            if (CollUtil.isEmpty(tbMiddleTagPojoList)) {
                return;
            }
            // 根据相同tag分组, 然后查询出对于的tag表, 然后根据每个要删除的数量进行计算, 等于或小于0时删除tag
            voteToRemoveTag(tbMiddleTagPojoList);
        }
    }
    
    /**
     * 根据类型ID, 删除tag
     *
     * @param ids   tag id
     * @param types tag type
     */
    @Override
    public void removeLabel(List<Long> ids, Collection<Byte> types) {
        synchronized (removeLabelLock) {
            LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                             .in(TbMiddleTagPojo::getMiddleId, ids)
                                                             .in(TbMiddleTagPojo::getType, types);
            // 查询出所有tag关联数据
            List<TbMiddleTagPojo> tbMiddleTagPojoList = middleTagService.list(eq);
            if (CollUtil.isEmpty(tbMiddleTagPojoList)) {
                return;
            }
            // 根据相同tag分组, 然后查询出对于的tag表, 然后根据每个要删除的数量进行计算, 等于或小于0时删除tag
            voteToRemoveTag(tbMiddleTagPojoList);
        }
    }
    
    /**
     * 删除关联tag数据，根据投票制
     * 如果关联数据为0时，删除tag
     *
     * @param tbMiddleTagPojoList 关联数据
     */
    private void voteToRemoveTag(List<TbMiddleTagPojo> tbMiddleTagPojoList) {
        Map<Long, Integer> map = tbMiddleTagPojoList.parallelStream()
                                                    .collect(Collectors.toMap(TbMiddleTagPojo::getTagId,
                                                            tbMiddleTagPojo -> 1,
                                                            Integer::sum));
        List<TbTagPojo> tagList = tagService.listByIds(map.keySet());
        ArrayList<Long> tagIds = new ArrayList<>();
        for (TbTagPojo tbTagPojo : tagList) {
            Integer integer = map.get(tbTagPojo.getId());
            int count = tbTagPojo.getCount() - integer;
            // 关联tag数量小于等于0时删除
            if (count <= 0) {
                tagIds.add(tbTagPojo.getId());
            }
        }
        middleTagService.removeBatchByIds(tbMiddleTagPojoList);
        if (CollUtil.isNotEmpty(tagIds)) {
            tagService.removeByIds(tagIds);
        }
    }
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target       指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id           歌单或歌曲前ID
     * @param labelBatchId 需要删除的label ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLabelById(Byte target, Long id, Collection<Long> labelBatchId) {
        synchronized (lock) {
            LambdaQueryWrapper<TbMiddleTagPojo> eq = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                             .eq(TbMiddleTagPojo::getType, target)
                                                             .eq(TbMiddleTagPojo::getMiddleId, id)
                                                             .in(TbMiddleTagPojo::getTagId, labelBatchId);
            List<TbMiddleTagPojo> list = middleTagService.list(eq);
            if (CollUtil.isEmpty(list)) {
                return;
            }
            voteToRemoveTag(list);
        }
    }
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target         指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id             歌单或歌曲前ID
     * @param labelBatchName 需要删除的label ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeLabelByName(Byte target, Long id, Collection<Long> labelBatchName) {
        LambdaQueryWrapper<TbTagPojo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(TbTagPojo::getTagName, labelBatchName);
        List<TbTagPojo> list = tagService.list(queryWrapper);
        Set<Long> collect = list.parallelStream().map(TbTagPojo::getId).collect(Collectors.toSet());
        removeLabelById(target, id, collect);
    }
    
    /**
     * 添加喜欢歌单
     *
     * @param userId          用户
     * @param id              音乐ID
     * @param isAddAndDelLike true添加 false删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void collectLike(Long userId, Long id, Boolean isAddAndDelLike) {
        LambdaQueryWrapper<TbCollectPojo> query = Wrappers.lambdaQuery();
        query.eq(TbCollectPojo::getUserId, userId);
        query.eq(TbCollectPojo::getType, PlayListTypeConfig.LIKE);
        TbCollectPojo collectServiceById = collectService.getOne(query);
        if (collectServiceById == null) {
            // 添加用户喜爱歌单
            TbCollectPojo entity = new TbCollectPojo();
            SysUserPojo userPojo = accountService.getById(userId);
            entity.setPlayListName(userPojo.getNickname() + " 喜欢的音乐");
            entity.setSort(collectService.count());
            entity.setUserId(userId);
            entity.setType(PlayListTypeConfig.LIKE);
            collectService.save(entity);
            collectServiceById = entity;
        }
        TbMusicPojo byId = musicService.getById(id);
        if (byId == null) {
            log.debug("添加歌曲不存在: {}", id);
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        
        // 效验歌单中是否有该歌曲
        LambdaQueryWrapper<TbCollectMusicPojo> wrapper = Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                 .eq(TbCollectMusicPojo::getCollectId, collectServiceById.getId())
                                                                 .eq(TbCollectMusicPojo::getMusicId, id);
        long count = collectMusicService.count(wrapper);
        // 删除还是添加歌曲
        if (Boolean.TRUE.equals(isAddAndDelLike)) {
            // 歌曲已存在
            if (count >= 1) {
                throw new BaseException(ResultCode.SONG_EXIST);
            }
            TbCollectMusicPojo tbLikeMusicPojo = new TbCollectMusicPojo();
            tbLikeMusicPojo.setCollectId(collectServiceById.getId());
            tbLikeMusicPojo.setMusicId(id);
            
            synchronized (lock) {
                Long sort = getCollectCount(collectServiceById.getId());
                tbLikeMusicPojo.setSort(sort);
                collectMusicService.save(tbLikeMusicPojo);
            }
            log.debug("歌单ID: {} 歌曲ID: {}  歌曲保存", tbLikeMusicPojo.getCollectId(), tbLikeMusicPojo.getMusicId());
            
            TbCollectPojo entity = new TbCollectPojo();
            entity.setId(collectServiceById.getId());
            collectService.updateById(entity);
        } else {
            // 歌曲不存在
            if (count == 0) {
                throw new BaseException(ResultCode.SONG_NOT_EXIST);
            }
            collectMusicService.remove(wrapper);
            log.debug("歌单ID: {} 歌曲ID: {}  歌曲已删除", collectServiceById.getId(), id);
        }
        
    }
    
    /**
     * 歌单page排序，倒序查询，查询出最后一条数据，并i+=1
     *
     * @param id 歌单ID
     * @return 歌曲排序ID
     */
    private Long getCollectCount(Long id) {
        LambdaQueryWrapper<TbCollectMusicPojo> eq = Wrappers.<TbCollectMusicPojo>lambdaQuery().
                                                            eq(TbCollectMusicPojo::getCollectId, id)
                                                            .orderByDesc(TbCollectMusicPojo::getSort);
        Page<TbCollectMusicPojo> page = collectMusicService.page(new Page<>(0, 1), eq);
        Long sort = 0L;
        if (CollUtil.isNotEmpty(page.getRecords()) && page.getRecords().size() == 1) {
            sort = page.getRecords().get(0).getSort();
            sort++;
        }
        return sort;
    }
    
    /**
     * 删除音乐
     *
     * @param musicIds 音乐ID
     * @param compel   是否强制删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteMusic(List<Long> musicIds, Boolean compel) {
        if (CollUtil.isEmpty(musicIds)) {
            return;
        }
        List<TbMusicPojo> musicList = musicService.list(Wrappers.<TbMusicPojo>lambdaQuery().in(TbMusicPojo::getId, musicIds));
        if (CollUtil.isEmpty(musicList)) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        // 删除歌单
        LambdaQueryWrapper<TbCollectMusicPojo> queryWrapper1 = Wrappers.lambdaQuery();
        queryWrapper1.in(TbCollectMusicPojo::getMusicId, musicIds);
        List<TbCollectMusicPojo> list = collectMusicService.list(queryWrapper1);
        // 是否强制删除歌单中的音乐
        if (CollUtil.isEmpty(list) || Boolean.TRUE.equals(compel)) {
            collectMusicService.remove(queryWrapper1);
        } else {
            throw new BaseException(ResultCode.COLLECT_MUSIC_ERROR);
        }
        // 删除歌手
        musicArtistService.remove(Wrappers.<TbMusicArtistPojo>lambdaQuery().in(TbMusicArtistPojo::getMusicId, musicIds));
        // 删除流派
        this.removeLabel(musicIds, TargetTagConstant.TARGET_MUSIC_GENRE);
        // 删除tag
        this.removeLabel(musicIds, TargetTagConstant.TARGET_MUSIC_TAG);
        // 删除origin
        tbOriginService.remove(Wrappers.<TbOriginPojo>lambdaQuery().in(TbOriginPojo::getMusicId, musicIds));
        // 删除音源关联数据
        LambdaQueryWrapper<TbResourcePojo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(TbResourcePojo::getMusicId, musicIds);
        resourceService.remove(queryWrapper);
        // 删除封面
        remoteStorePicService.removePicIds(musicIds, Collections.singletonList(PicTypeConstant.MUSIC));
        // 删除歌词
        lyricService.remove(Wrappers.<TbLyricPojo>lambdaQuery().in(TbLyricPojo::getMusicId, musicIds));
        // 删除歌曲
        musicService.removeBatchByIds(musicIds);
    }
    
    
    /**
     * 删除专辑
     * 强制删除会删除歌曲关联数据
     *
     * @param ids    专辑ID 列表
     * @param compel 是否强制删除
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAlbum(List<Long> ids, Boolean compel) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 检测专辑是否包括音乐
        List<MusicConvert> musicListByAlbumId = getMusicListByAlbumId(ids);
        if (CollUtil.isEmpty(musicListByAlbumId) || Boolean.TRUE.equals(compel)) {
            // 删除封面
            remoteStorePicService.removePicIds(ids, Collections.singletonList(PicTypeConstant.ALBUM));
            // 删除用户关注专辑
            userAlbumService.remove(Wrappers.<TbUserAlbumPojo>lambdaQuery().in(TbUserAlbumPojo::getAlbumId, ids));
            // 删除专辑歌手表
            albumArtistService.remove(Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getAlbumId, ids));
            // 删除音乐关联数据
            if (CollUtil.isNotEmpty(musicListByAlbumId)) {
                LambdaUpdateWrapper<TbMusicPojo> musicUpdateLambda = new LambdaUpdateWrapper<>();
                musicUpdateLambda.set(TbMusicPojo::getAlbumId, null);
                musicUpdateLambda.in(TbMusicPojo::getAlbumId, ids);
                musicService.update(musicUpdateLambda);
            }
            // 删除tag
            this.removeLabelAlbum(ids);
            albumService.removeByIds(ids);
        } else {
            throw new BaseException(ResultCode.ALBUM_MUSIC_EXIST_ERROR);
        }
    }
    
    /**
     * 删除歌手
     *
     * @param ids 歌手ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteArtist(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        LambdaQueryWrapper<TbAlbumArtistPojo> in = Wrappers.<TbAlbumArtistPojo>lambdaQuery().in(TbAlbumArtistPojo::getArtistId, ids);
        albumArtistService.remove(in);
        userSingerService.remove(Wrappers.<TbUserArtistPojo>lambdaQuery().in(TbUserArtistPojo::getArtistId, ids));
        musicArtistService.remove(Wrappers.<TbMusicArtistPojo>lambdaQuery().in(TbMusicArtistPojo::getArtistId, ids));
        tbMvArtistService.remove(Wrappers.<TbMvArtistPojo>lambdaQuery().eq(TbMvArtistPojo::getArtistId, ids));
        artistService.removeBatchByIds(ids);
        // 删除封面
        remoteStorePicService.removePicIds(ids, Collections.singletonList(PicTypeConstant.ARTIST));
    }
    
    /**
     * 保存或更新歌词
     *
     * @param musicId 音乐ID
     * @param type    歌词类型
     * @param lyric   歌词
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdateLyric(Long musicId, String type, String lyric) {
        if (!(StringUtils.equals(type, LyricConstant.LYRIC) ||
                StringUtils.equals(type, LyricConstant.K_LYRIC) ||
                StringUtils.equals(type, LyricConstant.T_LYRIC))) {
            throw new BaseException(ResultCode.LYRIC_NO_EXIST_EXISTED);
        }
        TbLyricPojo one = lyricService.getOne(Wrappers.<TbLyricPojo>lambdaQuery()
                                                      .eq(TbLyricPojo::getMusicId, musicId)
                                                      .eq(TbLyricPojo::getType, LyricConstant.LYRIC));
        TbLyricPojo entity = Optional.ofNullable(one).orElse(new TbLyricPojo());
        entity.setMusicId(musicId);
        entity.setType(LyricConstant.LYRIC);
        entity.setLyric(lyric);
        lyricService.saveOrUpdate(entity);
    }
    
    public List<MusicConvert> getPicMusicList(Collection<TbMusicPojo> musicList) {
        Set<Long> collect = musicList.parallelStream().map(TbMusicPojo::getId).collect(Collectors.toSet());
        Map<Long, String> picUrl = remoteStorePicService.getMusicPicUrl(collect);
        return musicList.parallelStream().map(tbMusicPojo -> {
            String url = picUrl.get(tbMusicPojo.getId());
            MusicConvert musicConvert = new MusicConvert();
            musicConvert.setPicUrl(url);
            BeanUtils.copyProperties(tbMusicPojo, musicConvert);
            return musicConvert;
        }).toList();
    }
    
    public List<AlbumConvert> getPicAlbumList(Collection<TbAlbumPojo> musicList) {
        Set<Long> collect = musicList.parallelStream().map(TbAlbumPojo::getId).collect(Collectors.toSet());
        Map<Long, String> picUrl = remoteStorePicService.getAlbumPicUrl(collect);
        return musicList.parallelStream().map(tbMusicPojo -> {
            String url = picUrl.get(tbMusicPojo.getId());
            AlbumConvert musicConvert = new AlbumConvert();
            musicConvert.setPicUrl(url);
            BeanUtils.copyProperties(tbMusicPojo, musicConvert);
            return musicConvert;
        }).toList();
    }
    
    public List<ArtistConvert> getPicArtistList(Collection<TbArtistPojo> artistList) {
        Set<Long> collect = artistList.parallelStream().map(TbArtistPojo::getId).collect(Collectors.toSet());
        Map<Long, String> picUrl = remoteStorePicService.getArtistPicUrl(collect);
        return artistList.parallelStream().map(tbMusicPojo -> {
            String url = picUrl.get(tbMusicPojo.getId());
            ArtistConvert artistConvert = new ArtistConvert();
            artistConvert.setPicUrl(url);
            BeanUtils.copyProperties(tbMusicPojo, artistConvert);
            return artistConvert;
        }).toList();
    }
    
    /**
     * 获取歌曲专辑
     *
     * @param musicIds 歌曲ID
     * @return key 歌曲ID value 专辑信息
     */
    @Override
    public Map<Long, AlbumConvert> getMusicAlbumByMusicIdToMap(List<Long> musicIds) {
        List<TbMusicPojo> musicList = musicService.listByIds(musicIds);
        return this.getMusicAlbumByAlbumIdToMap(musicList.parallelStream().map(TbMusicPojo::getAlbumId).toList());
    }
    
    /**
     * 获取歌曲专辑
     *
     * @param albumIds 专辑ID
     * @return key 歌曲ID value 专辑信息
     */
    @Override
    public Map<Long, AlbumConvert> getMusicAlbumByAlbumIdToMap(Collection<Long> albumIds) {
        List<TbAlbumPojo> tbAlbumPojos = albumService.listByIds(albumIds);
        return tbAlbumPojos.parallelStream().collect(Collectors.toMap(TbAlbumPojo::getId, tbAlbumPojo -> {
            AlbumConvert albumConvert = new AlbumConvert();
            BeanUtils.copyProperties(tbAlbumPojo, albumConvert);
            albumConvert.setPicUrl(remoteStorePicService.getPicUrl(tbAlbumPojo.getId(), PicTypeConstant.ALBUM));
            return albumConvert;
        }));
    }
    
    /**
     * 获取专辑所有音乐时长
     *
     * @param albumIds 专辑ID
     * @return 专辑时长
     */
    @Override
    public Map<Long, Integer> getAlbumDurationCount(List<Long> albumIds) {
        HashMap<Long, Integer> resMap = new HashMap<>();
        Map<Long, List<MusicConvert>> musicMapByAlbumId = getMusicMapByAlbumId(albumIds);
        for (Map.Entry<Long, List<MusicConvert>> longListEntry : musicMapByAlbumId.entrySet()) {
            Integer durationCount = longListEntry.getValue().parallelStream().map(TbMusicPojo::getTimeLength).reduce(0, Integer::sum);
            resMap.put(longListEntry.getKey(), durationCount);
        }
        return resMap;
    }
}

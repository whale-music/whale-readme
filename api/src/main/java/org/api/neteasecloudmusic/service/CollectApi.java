package org.api.neteasecloudmusic.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.playlistdetail.*;
import org.core.common.exception.BaseException;
import org.core.common.page.LambdaQueryWrapper;
import org.core.common.page.Page;
import org.core.common.page.Wrappers;
import org.core.common.result.NeteaseResult;
import org.core.common.result.ResultCode;
import org.core.iservice.*;
import org.core.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.core.utils.IDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 歌单中间层
 */
@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "CollectApi")
public class CollectApi {
    
    @Autowired
    private CollectService collectService;
    
    @Autowired
    private TagService tagService;
    
    @Autowired
    private CollectTagService collectTagService;
    
    @Autowired
    private CollectMusicService collectMusicService;
    
    @Autowired
    private MusicService musicService;
    
    @Autowired
    private MusicUrlService musicUrlService;
    
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private QukuService qukuService;
    
    /**
     * 是否包含tag
     *
     * @param pojoList tag表
     * @param tagName  tag name
     */
    private static TagPojo containsTag(List<TagPojo> pojoList, String tagName) {
        for (TagPojo tagPojo : pojoList) {
            if (tagName.equals(tagPojo.getTagName())) {
                return tagPojo;
            }
        }
        return null;
    }
    
    /**
     * 检查用户是否有权限操作歌单
     *
     * @param userId        用户ID
     * @param collectPojo 歌单信息
     */
    private static void checkUserAuth(Long userId, CollectPojo collectPojo) {
        // 检查是否有该歌单
        if (collectPojo == null || collectPojo.getUserId() == null) {
            throw new BaseException(ResultCode.SONG_LIST_DOES_NOT_EXIST);
        }
        // 检查用户是否有权限
        if (!userId.equals(collectPojo.getUserId())) {
            log.warn(ResultCode.PERMISSION_NO_ACCESS.getResultMsg());
            throw new BaseException(ResultCode.PERMISSION_NO_ACCESS);
        }
    }
    
    /**
     * 查询tag表
     * 根据tag id列表返回歌单tag信息
     *
     * @param tagIds tag ID
     */
    public List<TagPojo> getTagPojoList(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TagPojo> lambdaQueryWrapper = Wrappers.<TagPojo>lambdaQuery().in(TagPojo::getId, tagIds);
        return tagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 根据歌单和tag中间表
     * 获取歌单对各的tag id
     *
     * @param collectId 歌单ID
     * @return 返回中间表tag id列表
     */
    public List<CollectTagPojo> getCollectTagIdList(List<Long> collectId) {
        if (collectId == null || collectId.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<CollectTagPojo> lambdaQueryWrapper = Wrappers.<CollectTagPojo>lambdaQuery().in(CollectTagPojo::getCollectId, collectId);
        return collectTagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 创建歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @return 歌单信息
     */
    public CollectPojo createPlayList(Long userId, String name) {
        CollectPojo collectPojo = new CollectPojo();
        collectPojo.setUserId(userId);
        collectPojo.setPlayListName(name);
        collectPojo.setSort(collectService.count() + 1);
        collectPojo.setPic("https://p1.music.126.net/jWE3OEZUlwdz0ARvyQ9wWw==/109951165474121408.jpg");
        collectPojo.setSubscribed(false);
        collectPojo.setType((byte) 0);
        collectService.save(collectPojo);
        return collectPojo;
    }
    
    /**
     * 修改用户歌单信息,值为null则不修改
     *
     * @param userId      用户ID
     * @param collectPojo 歌单信息
     */
    public void updatePlayList(Long userId, CollectPojo collectPojo) {
        CollectPojo tbCollectPojo = collectService.getById(collectPojo.getId());
        checkUserAuth(userId, tbCollectPojo);
        
        collectService.updateById(collectPojo);
    }
    
    /**
     * 修改歌单tag
     *
     * @param userId    用户ID
     * @param collectId 歌单ID
     * @param split     tag
     */
    public void updatePlayListTag(Long userId, Long collectId, String[] split) {
        CollectPojo collectPojo = new CollectPojo();
        collectPojo.setId(collectId);
        collectPojo.setUserId(userId);
        checkUserAuth(userId, collectPojo);
        
        
        // 如果为0则直接不需要保存操作
        if (split.length == 0) {
            return;
        }
        
        // 先删除歌单的关联tag然后在重新添加
        LambdaQueryWrapper<CollectTagPojo> collectLambdaQueryWrapper = Wrappers.<CollectTagPojo>lambdaQuery()
                                                                               .eq(CollectTagPojo::getCollectId, collectId);
        collectTagService.remove(collectLambdaQueryWrapper);
        
        // tag为空字符串跳过
        if (split.length == 1 && StringUtils.isBlank(split[0])) {
            return;
        }
        
        List<String> splitList = Arrays.asList(split);
        
        // 需要存入的tag list
        List<CollectTagPojo> saveCollectTagPojoList = new ArrayList<>(split.length);
        // 用来批量存放tb tag
        List<TagPojo> saveTbTagList = new ArrayList<>();
        
        LambdaQueryWrapper<TagPojo> tagPojoLambdaQueryWrapper = Wrappers.<TagPojo>lambdaQuery().in(TagPojo::getTagName, splitList);
        // 获取tag表中已经记录的tag id
        List<TagPojo> tagPojos = tagService.list(tagPojoLambdaQueryWrapper);
        
        for (String tagName : split) {
            // 如果不包含tag 则新建tag ,否则直接写入到歌单andtag中间表
            TagPojo tagPojo = containsTag(tagPojos, tagName);
            if (tagPojo == null) {
                // 新建tag
                TagPojo tbTagPojo = new TagPojo();
                tbTagPojo.setId(IDUtil.getID());
                tbTagPojo.setTagName(tagName);
                saveTbTagList.add(tbTagPojo);
                // 添加到中间表
                CollectTagPojo e = new CollectTagPojo();
                e.setTagId(tbTagPojo.getId());
                e.setCollectId(collectId);
                saveCollectTagPojoList.add(e);
            } else {
                // 已有tag信息, 直接使用tag id
                CollectTagPojo e = new CollectTagPojo();
                e.setTagId(tagPojo.getId());
                e.setCollectId(collectId);
                saveCollectTagPojoList.add(e);
            }
        }
        
        // 保存tag表
        tagService.saveBatch(saveTbTagList);
        // 保存tag and 歌单中间表
        collectTagService.saveBatch(saveCollectTagPojoList);
    }
    
    /**
     * 删除歌单
     *
     * @param userId     用户ID
     * @param collectList 歌单ID
     */
    public void removePlayList(Long userId, List<Long> collectList) {
        List<CollectPojo> collectPojos = collectService.listByIds(collectList);
        for (CollectPojo collectPojo : collectPojos) {
            checkUserAuth(userId, collectPojo);
        }
        
        // 删除歌单ID
        collectService.removeByIds(collectList);
        // 删除歌单关联tag
        collectTagService.remove(Wrappers.<CollectTagPojo>lambdaQuery().in(CollectTagPojo::getCollectId, collectList));
    }
    
    /**
     * 收藏/取消歌单
     *
     * @param userId    用户ID
     * @param collectId 歌单ID
     * @param flag      取消/收藏 1:收藏,2:取消收藏
     */
    public void subscribePlayList(Long userId, Long collectId, Integer flag) {
        CollectPojo collectPojo = collectService.getById(collectId);
        // 需要收藏歌单存在，并且用户不一样（防止重复收藏）
        if (collectPojo != null && !Objects.equals(collectPojo.getUserId(), userId) && flag == 1) {
            collectPojo.setId(null);
            // 复制歌单，并更新信息
            collectPojo.setUserId(userId);
            // 收藏
            collectPojo.setSubscribed(true);
            // 增加排序值
            collectPojo.setSort(collectService.count() + 1);
            collectService.save(collectPojo);
            
            // 保存歌单
            List<CollectMusicPojo> collectMusicPojos = collectMusicService.list(Wrappers.<CollectMusicPojo>lambdaQuery()
                                                                                        .eq(CollectMusicPojo::getCollectId, collectId));
            Set<CollectMusicPojo> batch = collectMusicPojos.stream().map(tbCollectMusicPojo -> {
                CollectMusicPojo collectMusicPojo1 = new CollectMusicPojo();
                collectMusicPojo1.setCollectId(collectId);
                collectMusicPojo1.setMusicId(tbCollectMusicPojo.getMusicId());
                return collectMusicPojo1;
            }).collect(Collectors.toSet());
            
            collectMusicService.saveBatch(batch);
        } else if (Boolean.TRUE.equals(collectPojo != null && collectPojo.getSubscribed()) && flag == 2) {
            // 歌单存在并且是收藏状态
            // 删除歌单
            removePlayList(userId, Collections.singletonList(collectId));
        }
    }
    
    /**
     * 获取歌单所有歌曲
     *
     * @param collectId 歌单ID
     * @param pageIndex 当前多少页
     * @param pageSize  每页多少条
     * @return 歌单内所有歌曲
     */
    public Page<MusicPojo> getPlayListAllSong(Long collectId, Long pageIndex, Long pageSize) {
        LambdaQueryWrapper<CollectMusicPojo> wrapper = Wrappers.<CollectMusicPojo>lambdaQuery().in(CollectMusicPojo::getCollectId, collectId);
        Page<CollectMusicPojo> page = collectMusicService.page(new Page<>(pageIndex, pageSize), wrapper);
        if (page.getTotal() == 0) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        List<Long> musicIds = page.getRecords().stream().map(CollectMusicPojo::getMusicId).collect(Collectors.toList());
        List<MusicPojo> musicPojoList = musicService.listByIds(musicIds);
        
        Page<MusicPojo> musicPojoPage = new Page<>();
        musicPojoPage.setRecords(musicPojoList);
        musicPojoPage.setCurrent(page.getCurrent());
        musicPojoPage.setTotal(page.getTotal());
        musicPojoPage.setSize(page.getSize());
        return musicPojoPage;
    }
    
    /**
     * 获取音乐信息
     *
     * @param musicIds 音乐id list
     * @return 音乐信息列表
     */
    public List<MusicUrlPojo> getMusicInfo(List<Long> musicIds) {
        return musicUrlService.list(Wrappers.<MusicUrlPojo>lambdaQuery().in(MusicUrlPojo::getMusicId, musicIds));
    }
    
    /**
     * 添加歌曲到歌单
     *
     * @param userID    用户ID
     * @param collectId 歌单ID
     * @param songIds   歌曲ID
     * @param flag      添加还是删除
     * @return 返回添加歌单后歌单数量
     */
    public NeteaseResult addSongToCollect(Long userID, Long collectId, List<Long> songIds, boolean flag) {
        CollectPojo collectPojo = collectService.getById(collectId);
        checkUserAuth(userID, collectPojo);
        
        if (flag) {
            // 查询歌单内歌曲是否存在
            long count = collectMusicService.count(Wrappers.<CollectMusicPojo>lambdaQuery()
                                                           .eq(CollectMusicPojo::getCollectId, collectId)
                                                           .in(CollectMusicPojo::getMusicId, songIds));
            if (count > 0) {
                NeteaseResult r = new NeteaseResult();
                return r.error("502", "歌单内歌曲重复");
            }
            
            // 添加
            List<MusicPojo> tbMusicPojo = musicService.listByIds(songIds);
            List<CollectMusicPojo> collect = tbMusicPojo.stream()
                                                        .map(tbMusicPojo1 -> {
                                                            CollectMusicPojo collectMusicPojo = new CollectMusicPojo();
                                                            collectMusicPojo.setCollectId(collectId);
                                                            collectMusicPojo.setMusicId(tbMusicPojo1.getId());
                                                            return collectMusicPojo;
                                                        })
                                                        .collect(Collectors.toList());
            collectMusicService.saveBatch(collect);
            Long songId = songIds.get(songIds.size() - 1);
            MusicPojo musicPojo = musicService.getById(songId);
            if (musicPojo != null) {
                collectPojo.setPic(musicPojo.getPic());
            }
            collectService.updateById(collectPojo);
        } else {
            // 删除歌曲
            collectMusicService.remove(Wrappers.<CollectMusicPojo>lambdaQuery()
                                               .eq(CollectMusicPojo::getCollectId, collectId)
                                               .in(CollectMusicPojo::getMusicId, songIds));
        }
        long count = collectMusicService.count(Wrappers.<CollectMusicPojo>lambdaQuery().eq(CollectMusicPojo::getCollectId, collectId));
        NeteaseResult map = new NeteaseResult();
        map.put("trackIds", songIds.toArray());
        map.put("count", count);
        map.put("cloudCount", 0);
        return map.success();
    }
    
    /**
     * 添加喜爱歌曲
     *
     * @param userId          用户ID
     * @param id              歌曲ID
     * @param isAddAndDelLike true添加歌曲，false删除歌曲
     */
    public void like(Long userId, Long id, Boolean isAddAndDelLike) {
        CollectPojo collectServiceById = collectService.getById(userId);
        if (collectServiceById == null) {
            // 添加或用户喜爱歌单
            CollectPojo entity = new CollectPojo();
            entity.setId(userId);
            SysUserPojo userPojo = accountService.getById(userId);
            entity.setPlayListName(userPojo.getNickname() + " 喜欢的音乐");
            entity.setPic(userPojo.getAvatarUrl());
            entity.setSort(collectService.count());
            entity.setUserId(userId);
            entity.setType((byte) 1);
            collectService.save(entity);
        }
        MusicPojo byId = musicService.getById(id);
        if (byId == null) {
            log.debug("添加歌曲不存在");
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        
        // 效验歌单中是否有该歌曲
        LambdaQueryWrapper<CollectMusicPojo> wrapper = Wrappers.<CollectMusicPojo>lambdaQuery()
                                                               .eq(CollectMusicPojo::getCollectId, userId)
                                                               .eq(CollectMusicPojo::getMusicId, id);
        long count = collectMusicService.count(wrapper);
        // 删除还是添加歌曲
        if (Boolean.TRUE.equals(isAddAndDelLike)) {
            // 歌曲已存在
            if (count >= 1) {
                throw new BaseException(ResultCode.SONG_EXIST);
            }
            CollectMusicPojo tbLikeMusicPojo = new CollectMusicPojo();
            tbLikeMusicPojo.setCollectId(userId);
            tbLikeMusicPojo.setMusicId(id);
            collectMusicService.save(tbLikeMusicPojo);
            log.debug("歌单ID: {} 歌曲ID: {}  歌曲保存", tbLikeMusicPojo.getCollectId(), tbLikeMusicPojo.getMusicId());
        } else {
            // 歌曲不存在
            if (count == 0) {
                throw new BaseException(ResultCode.SONG_NOT_EXIST);
            }
            collectMusicService.remove(wrapper);
            log.debug("歌单ID: {} 歌曲ID: {}  歌曲已删除", userId, id);
        }
        
    }
    
    /**
     * 查询用户喜爱歌单
     *
     * @param uid 用户ID
     * @return 返回歌曲数组
     */
    public List<Long> likelist(Long uid) {
        List<CollectMusicPojo> list = collectMusicService.list(Wrappers.<CollectMusicPojo>lambdaQuery().eq(CollectMusicPojo::getCollectId, uid));
        return list.stream().map(CollectMusicPojo::getMusicId).collect(Collectors.toList());
    }
    
    
    /**
     * 查询歌单详细信息
     *
     * @param id 歌单信息
     */
    public PlayListDetailRes playlistDetail(Long id) {
        CollectPojo byId = collectService.getById(id);
        if (byId == null) {
            return new PlayListDetailRes();
        }
        List<MusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        PlayListDetailRes playListRes = new PlayListDetailRes();
        Playlist playlist = new Playlist();
        playlist.setId(byId.getId());
        playlist.setName(byId.getPlayListName());
        playlist.setCoverImgUrl(byId.getPic());
        playlist.setUpdateTime((long) byId.getUpdateTime().getNano());
        playlist.setDescription(byId.getDescription());
        
        // 歌单创建者
        Creator creator = new Creator();
        SysUserPojo userPojo = accountService.getById(byId.getUserId());
        userPojo = Optional.ofNullable(userPojo).orElse(new SysUserPojo());
        creator.setNickname(userPojo.getNickname());
        creator.setBackgroundUrl(userPojo.getBackgroundUrl());
        creator.setAvatarUrl(userPojo.getAvatarUrl());
        creator.setUserId(userPojo.getId());
        playlist.setCreator(creator);
        playlist.setUserId(userPojo.getId());
        
        ArrayList<TracksItem> tracks = new ArrayList<>();
        ArrayList<TrackIdsItem> trackIds = new ArrayList<>();
        for (MusicPojo musicPojo : playListAllMusic) {
            TracksItem e = new TracksItem();
            e.setId(musicPojo.getId());
            e.setName(musicPojo.getMusicName());
            e.setAlia(AliasUtil.getAliasList(musicPojo.getAliasName()));
            e.setPublishTime((long) musicPojo.getCreateTime().getNano());
            ArrayList<ArItem> ar = new ArrayList<>();
            
            // 艺术家数据
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setId(artistPojo.getId());
                e1.setName(artistPojo.getArtistName());
                e1.setAlias(AliasUtil.getAliasList(artistPojo.getAliasName()));
                ar.add(e1);
            }
            e.setAr(ar);
            
            // 专辑数据
            AlbumPojo albumByAlbumId = Optional.ofNullable(qukuService.getAlbumByAlbumId(musicPojo.getAlbumId())).orElse(new AlbumPojo());
            Al al = new Al();
            al.setId(albumByAlbumId.getId());
            al.setName(albumByAlbumId.getAlbumName());
            al.setPicUrl(albumByAlbumId.getPic());
            e.setAl(al);
            
            tracks.add(e);
            
            
            TrackIdsItem trackIdsItem = new TrackIdsItem();
            trackIdsItem.setId(musicPojo.getId());
            trackIds.add(trackIdsItem);
        }
        playlist.setTracks(tracks);
        playlist.setTrackIds(trackIds);
        playlist.setTrackCount(playListAllMusic.size());
        playListRes.setPlaylist(playlist);
        return playListRes;
    }
}

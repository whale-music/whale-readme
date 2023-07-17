package org.api.neteasecloudmusic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.common.service.QukuAPI;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.playlistdetail.*;
import org.core.common.constant.defaultinfo.DefaultInfo;
import org.core.common.exception.BaseException;
import org.core.common.result.NeteaseResult;
import org.core.common.result.ResultCode;
import org.core.config.PlayListTypeConfig;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.PlayListService;
import org.core.service.impl.QukuServiceImpl;
import org.core.utils.AliasUtil;
import org.core.utils.ExceptionUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 歌单中间层
 */
@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "CollectApi")
public class CollectApi {
    
    @Autowired
    private TbCollectService collectService;
    
    @Autowired
    private TbUserCollectService userCollectService;
    
    @Autowired
    private TbTagService tagService;
    
    @Autowired
    private TbMiddleTagService collectMusicTagService;
    
    @Autowired
    private TbCollectMusicService collectMusicService;
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbResourceService musicUrlService;
    
    
    @Autowired
    private PlayListService playListService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private QukuAPI qukuService;
    
    @Autowired
    private DefaultInfo defaultInfo;
    
    /**
     * 是否包含tag
     *
     * @param pojoList tag表
     * @param tagName  tag name
     */
    private static TbTagPojo containsTag(List<TbTagPojo> pojoList, String tagName) {
        for (TbTagPojo tbTagPojo : pojoList) {
            if (tagName.equals(tbTagPojo.getTagName())) {
                return tbTagPojo;
            }
        }
        return null;
    }
    
    
    /**
     * 查询tag表
     * 根据tag id列表返回歌单tag信息
     *
     * @param tagIds tag ID
     */
    public List<TbTagPojo> getTagPojoList(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbTagPojo> lambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getId, tagIds);
        return tagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 根据歌单和tag中间表
     * 获取歌单对各的tag id
     *
     * @param collectId 歌单ID
     * @return 返回中间表tag id列表
     */
    public List<TbMiddleTagPojo> getCollectTagIdList(List<Long> collectId) {
        if (collectId == null || collectId.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbMiddleTagPojo> lambdaQueryWrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                                         .in(TbMiddleTagPojo::getId, collectId);
        return collectMusicTagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 创建歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @return 歌单信息
     */
    public CollectConvert createPlayList(Long userId, String name) {
        return qukuService.createPlayList(userId, name, PlayListTypeConfig.ORDINARY);
    }
    
    /**
     * 修改用户歌单信息,值为null则不修改
     *
     * @param userId      用户ID
     * @param collectPojo 歌单信息
     */
    public void updatePlayList(Long userId, TbCollectPojo collectPojo) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectPojo.getId());
        QukuServiceImpl.checkUserAuth(userId, tbCollectPojo);
        
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
        TbCollectPojo collectPojo = new TbCollectPojo();
        collectPojo.setId(collectId);
        collectPojo.setUserId(userId);
        QukuServiceImpl.checkUserAuth(userId, collectPojo);
    
    
        // 如果为0则直接不需要保存操作
        if (split.length == 0) {
            return;
        }
    
        // 先删除歌单的关联tag然后在重新添加
        LambdaQueryWrapper<TbMiddleTagPojo> collectLambdaQueryWrapper = Wrappers.<TbMiddleTagPojo>lambdaQuery()
                                                                                .eq(TbMiddleTagPojo::getId, collectId);
        collectMusicTagService.remove(collectLambdaQueryWrapper);
    
        // tag为空字符串跳过
        if (split.length == 1 && StringUtils.isBlank(split[0])) {
            return;
        }
    
        List<String> splitList = Arrays.asList(split);
    
        // 需要存入的tag list
        List<TbMiddleTagPojo> saveCollectTagPojoList = new ArrayList<>(split.length);
        // 用来批量存放tb tag
        List<TbTagPojo> saveTbTagList = new ArrayList<>();
    
        LambdaQueryWrapper<TbTagPojo> tagPojoLambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getTagName, splitList);
        // 获取tag表中已经记录的tag id
        List<TbTagPojo> tbTagPojos = tagService.list(tagPojoLambdaQueryWrapper);
        
        for (String tagName : split) {
            // 如果不包含tag 则新建tag ,否则直接写入到歌单andtag中间表
            TbTagPojo tagPojo = containsTag(tbTagPojos, tagName);
            if (tagPojo == null) {
                // 新建tag
                TbTagPojo tbTagPojo = new TbTagPojo();
                tbTagPojo.setId(IdWorker.getId());
                tbTagPojo.setTagName(tagName);
                saveTbTagList.add(tbTagPojo);
                // 添加到中间表
                TbMiddleTagPojo e = new TbMiddleTagPojo();
                e.setTagId(tbTagPojo.getId());
                e.setMiddleId(collectId);
                saveCollectTagPojoList.add(e);
            } else {
                // 已有tag信息, 直接使用tag id
                TbMiddleTagPojo e = new TbMiddleTagPojo();
                e.setTagId(tagPojo.getId());
                e.setMiddleId(collectId);
                saveCollectTagPojoList.add(e);
            }
        }
        
        // 保存tag表
        tagService.saveBatch(saveTbTagList);
        // 保存tag and 歌单中间表
        collectMusicTagService.saveBatch(saveCollectTagPojoList);
    }
    
    /**
     * 删除歌单
     *
     * @param userId     用户ID
     * @param collectIds 歌单ID
     */
    public void removePlayList(Long userId, List<Long> collectIds) {
        qukuService.removePlayList(userId, collectIds);
    }
    
    /**
     * 收藏/取消歌单
     *
     * @param userId    用户ID
     * @param collectId 歌单ID
     * @param flag      取消/收藏 true:收藏,false:取消收藏
     */
    public void subscribePlayList(Long userId, Long collectId, boolean flag) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectId);
        ExceptionUtil.isNull(tbCollectPojo == null, ResultCode.PLAT_LIST_EXIST);
        // 需要收藏歌单存在，并且用户不一样, 不等于为true
        boolean userFlag = !Objects.equals(tbCollectPojo.getUserId(), userId);
        
        LambdaQueryWrapper<TbUserCollectPojo> eq = Wrappers.<TbUserCollectPojo>lambdaQuery()
                                                           .eq(TbUserCollectPojo::getCollectId, collectId)
                                                           .eq(TbUserCollectPojo::getUserId, userId);
        TbUserCollectPojo one = userCollectService.getOne(eq);
        // 防止重复收藏
        ExceptionUtil.isNull(one == null, ResultCode.PLAT_LIST_LIKE);
        // 收藏
        if (userFlag && flag) {
            TbUserCollectPojo entity = new TbUserCollectPojo();
            entity.setCollectId(collectId);
            entity.setUserId(userId);
            userCollectService.save(entity);
        } else {
            userCollectService.remove(eq);
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
    public Page<MusicConvert> getPlayListAllSong(Long collectId, Long pageIndex, Long pageSize) {
        LambdaQueryWrapper<TbCollectMusicPojo> wrapper = Wrappers.<TbCollectMusicPojo>lambdaQuery().in(TbCollectMusicPojo::getCollectId, collectId);
        Page<TbCollectMusicPojo> page = collectMusicService.page(new Page<>(pageIndex, pageSize), wrapper);
        if (page.getTotal() == 0) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        List<Long> musicIds = page.getRecords().stream().map(TbCollectMusicPojo::getMusicId).toList();
        List<TbMusicPojo> tbMusicPojoList = musicService.listByIds(musicIds);
    
        List<MusicConvert> collect = tbMusicPojoList.parallelStream().map(tbMusicPojo -> {
            MusicConvert convert = new MusicConvert();
            BeanUtils.copyProperties(tbMusicPojo, convert);
            convert.setPicUrl(qukuService.getMusicPicUrl(tbMusicPojo.getId()));
            return convert;
        }).toList();
        
        Page<MusicConvert> musicPojoPage = new Page<>();
        musicPojoPage.setRecords(collect);
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
    public List<TbResourcePojo> getMusicInfo(List<Long> musicIds) {
        return musicUrlService.list(Wrappers.<TbResourcePojo>lambdaQuery().in(TbResourcePojo::getMusicId, musicIds));
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
        TbCollectPojo tbCollectPojo = collectService.getById(collectId);
        QukuServiceImpl.checkUserAuth(userID, tbCollectPojo);
        try {
            qukuService.addMusicToCollect(userID, tbCollectPojo.getId(), songIds, flag);
        } catch (BaseException e) {
            if (StringUtils.equals(e.getCode(), ResultCode.SONG_NOT_EXIST.getCode())) {
                NeteaseResult r = new NeteaseResult();
                return r.error("502", "歌单内歌曲重复");
            }
            throw new BaseException();
        }
        long count = collectMusicService.count(Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, collectId));
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
        qukuService.collectLike(userId, id, isAddAndDelLike);
    }
    
    /**
     * 查询用户喜爱歌单
     *
     * @param uid 用户ID
     * @return 返回歌曲数组
     */
    public List<Long> likelist(Long uid) {
        List<TbCollectMusicPojo> list = collectMusicService.list(Wrappers.<TbCollectMusicPojo>lambdaQuery().eq(TbCollectMusicPojo::getCollectId, uid));
        return list.stream().map(TbCollectMusicPojo::getMusicId).toList();
    }
    
    
    /**
     * 查询歌单详细信息
     *
     * @param id 歌单信息
     */
    public PlayListDetailRes playlistDetail(Long id) {
        TbCollectPojo byId = collectService.getById(id);
        if (byId == null) {
            return new PlayListDetailRes();
        }
        List<TbMusicPojo> playListAllMusic = playListService.getPlayListAllMusic(id);
        PlayListDetailRes playListRes = new PlayListDetailRes();
        Playlist playlist = new Playlist();
        playlist.setId(byId.getId());
        playlist.setName(byId.getPlayListName());
        playlist.setCoverImgUrl(qukuService.getCollectPicUrl(byId.getId()));
        playlist.setUpdateTime((long) byId.getUpdateTime().getNano());
        playlist.setDescription(byId.getDescription());
    
        // 歌单创建者
        Creator creator = new Creator();
        SysUserPojo userPojo = accountService.getById(byId.getUserId());
        userPojo = Optional.ofNullable(userPojo).orElse(new SysUserPojo());
        creator.setNickname(userPojo.getNickname());
        creator.setBackgroundUrl(qukuService.getUserBackgroundPicUrl(userPojo.getId()));
        creator.setAvatarUrl(qukuService.getUserAvatarPicUrl(userPojo.getId()));
        creator.setUserId(userPojo.getId());
        playlist.setCreator(creator);
        playlist.setUserId(userPojo.getId());
    
        ArrayList<TracksItem> tracks = new ArrayList<>();
        ArrayList<TrackIdsItem> trackIds = new ArrayList<>();
        for (TbMusicPojo tbMusicPojo : playListAllMusic) {
            TracksItem e = new TracksItem();
            e.setId(tbMusicPojo.getId());
            e.setName(tbMusicPojo.getMusicName());
            e.setAlia(AliasUtil.getAliasList(tbMusicPojo.getAliasName()));
            e.setPublishTime((long) tbMusicPojo.getCreateTime().getNano());
            ArrayList<ArItem> ar = new ArrayList<>();
    
            // 艺术家数据
            List<ArtistConvert> singerByMusicId = qukuService.getAlbumArtistByMusicId(tbMusicPojo.getId());
            for (ArtistConvert tbArtistPojo : singerByMusicId) {
                ArItem e1 = new ArItem();
                e1.setId(tbArtistPojo.getId());
                e1.setName(tbArtistPojo.getArtistName());
                e1.setAlias(AliasUtil.getAliasList(tbMusicPojo.getAliasName()));
                ar.add(e1);
            }
            e.setAr(ar);
    
            // 专辑数据
            AlbumConvert albumByAlbumId = Optional.ofNullable(qukuService.getAlbumByAlbumId(tbMusicPojo.getAlbumId())).orElse(new AlbumConvert());
            Al al = new Al();
            al.setId(albumByAlbumId.getId());
            al.setName(albumByAlbumId.getAlbumName());
            al.setPicUrl(albumByAlbumId.getPicUrl());
            e.setAl(al);
    
            tracks.add(e);
    
    
            TrackIdsItem trackIdsItem = new TrackIdsItem();
            trackIdsItem.setId(tbMusicPojo.getId());
            trackIds.add(trackIdsItem);
        }
        playlist.setTracks(tracks);
        playlist.setTrackIds(trackIds);
        playlist.setTrackCount(playListAllMusic.size());
        playListRes.setPlaylist(playlist);
        return playListRes;
    }
}

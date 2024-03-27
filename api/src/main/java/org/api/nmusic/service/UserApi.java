package org.api.nmusic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.common.service.QukuAPI;
import org.api.nmusic.config.NeteaseCloudConfig;
import org.api.nmusic.model.vo.playlist.Creator;
import org.api.nmusic.model.vo.playlist.PlayListVo;
import org.api.nmusic.model.vo.playlist.PlaylistItem;
import org.api.nmusic.model.vo.user.detail.Profile;
import org.api.nmusic.model.vo.user.detail.ProfileVillageInfo;
import org.api.nmusic.model.vo.user.detail.UserDetailRes;
import org.api.nmusic.model.vo.user.detail.UserPoint;
import org.api.nmusic.model.vo.user.record.Al;
import org.api.nmusic.model.vo.user.record.ArItem;
import org.api.nmusic.model.vo.user.record.Song;
import org.api.nmusic.model.vo.user.record.UserRecordRes;
import org.core.common.constant.PlayListTypeConstant;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.jpa.entity.TbUserCollectEntity;
import org.core.jpa.repository.TbUserCollectEntityRepository;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.UserConvert;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.RemoteStorePicService;
import org.core.utils.AliasUtil;
import org.core.utils.CollectSortUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <p>
 * User 用户服务兼容层
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Slf4j
@Service(NeteaseCloudConfig.NETEASECLOUD + "UserApi")
public class UserApi {
    // 用户服务
    private final AccountService accountService;
    
    // 歌单表
    private final TbCollectService collectService;
    
    private final TbUserCollectEntityRepository userCollectEntityRepository;
    
    private final TbCollectMusicService collectMusicService;
    
    // 用户关注歌手表
    private final TbUserArtistService userSingerService;
    
    private final TbMusicService musicService;
    
    private final QukuAPI qukuService;
    
    private final CollectApi collectApi;
    
    private final TbHistoryService historyService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    
    public UserApi(AccountService accountService, TbCollectService collectService, TbUserCollectEntityRepository userCollectEntityRepository, TbCollectMusicService collectMusicService, TbUserArtistService userSingerService, TbMusicService musicService, QukuAPI qukuService, CollectApi collectApi, TbHistoryService historyService, RemoteStorePicService remoteStorePicService) {
        this.accountService = accountService;
        this.collectService = collectService;
        this.userCollectEntityRepository = userCollectEntityRepository;
        this.collectMusicService = collectMusicService;
        this.userSingerService = userSingerService;
        this.musicService = musicService;
        this.qukuService = qukuService;
        this.collectApi = collectApi;
        this.historyService = historyService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
    /**
     * 创建用户
     *
     * @param user 用户信息
     */
    public void createAccount(SysUserPojo user) {
        accountService.createAccount(user);
    }
    
    /**
     * 获取用户ID信息
     *
     * @param userId 用户ID
     * @return 用户信息
     */
    public UserConvert getAccount(Long userId) {
        SysUserPojo userPojo = accountService.getById(userId);
        if (userPojo == null) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        UserConvert userConvert = new UserConvert();
        BeanUtils.copyProperties(userPojo, userConvert);
        userConvert.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(userPojo.getId()));
        return userConvert;
    }
    
    /**
     * 用户登录
     *
     * @param phone       账号
     * @param password    密码
     * @param md5Password md5密码
     * @return 返回用户信息
     */
    public UserConvert login(String phone, String password, String md5Password) {
        SysUserPojo login = accountService.loginUserOrSubAccount(phone, password, md5Password);
        UserConvert userConvert = new UserConvert();
        BeanUtils.copyProperties(login, userConvert);
        userConvert.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(login.getId()));
        userConvert.setBackgroundPicUrl(remoteStorePicService.getUserBackgroundPicUrl(login.getId()));
        return userConvert;
    }
    
    /**
     * 跟新用户信息
     *
     * @param userPojo 用户信息
     */
    public void updateUserPojo(SysUserPojo userPojo) {
        boolean b = accountService.updateById(userPojo);
        if (b) {
            log.debug("用户名初始化成功");
        } else {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
    }
    
    /**
     * 查询用户所有歌单
     *
     * @param uid 用户ID
     * @return 返回用户所有歌单
     */
    public PlayListVo getPlayList(Long uid, Long pageIndex, Long pageSize) {
        PlayListVo playListVo = new PlayListVo();
        playListVo.setPlaylist(new ArrayList<>());
        playListVo.setVersion("0");
    
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getUserId, uid)
                                                                       .orderByAsc(TbCollectPojo::getSort);
        Page<TbCollectPojo> collectPojoPage = collectService.page(new Page<>(pageIndex, pageSize), lambdaQueryWrapper);
    
        List<TbCollectPojo> last = CollectSortUtil.userLikeUserSort(uid, collectPojoPage.getRecords());
        collectPojoPage.setRecords(last);
    
        // 是否有下一页
        playListVo.setMore(collectPojoPage.hasNext());
        List<TbCollectPojo> collectPojoList = collectPojoPage.getRecords();
        if (CollUtil.isEmpty(collectPojoList)) {
            return new PlayListVo();
        }
        // 导出歌单id
        List<Long> collectIds = collectPojoList.stream().map(TbCollectPojo::getId).toList();
        // 根据歌单和tag的中间表来获取tag id列表
        List<TbMiddleTagPojo> collectIdAndTagsIdList = collectApi.getCollectTagIdList(collectIds);
        // 根据tag id 列表获取tag Name列表
        List<Long> tagIdList = collectIdAndTagsIdList.stream()
                                                     .map(TbMiddleTagPojo::getTagId)
                                                     .toList();
        List<TbTagPojo> collectTagList = collectApi.getTagPojoList(tagIdList);
        
        
        for (TbCollectPojo tbCollectPojo : collectPojoList) {
            PlaylistItem item = new PlaylistItem();
            // 是否订阅
            item.setSubscribed(false);
            // 歌单数量
            item.setTrackCount(collectMusicService.count(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                 .eq(TbCollectMusicPojo::getCollectId, tbCollectPojo.getId())));
            // 创作者
            Creator creator = new Creator();
            SysUserPojo account = Optional.ofNullable(accountService.getById(uid)).orElse(new SysUserPojo());
            creator.setNickname(account.getNickname());
            creator.setUserId(account.getId());
            creator.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(account.getId()));
            creator.setBackgroundUrl(remoteStorePicService.getUserBackgroundPicUrl(account.getId()));
            item.setCreator(creator);
            // 用户ID
            item.setUserId(tbCollectPojo.getUserId());
            // 封面图像ID
            item.setCoverImgUrl(remoteStorePicService.getCollectPicUrl(tbCollectPojo.getId()));
            // 创建时间
            item.setCreateTime(tbCollectPojo.getCreateTime().getNano());
            // 描述
            item.setDescription(tbCollectPojo.getDescription());
            // 判断中间表是否有值
            // 判断tag表是否有值
            if (!collectIdAndTagsIdList.isEmpty() && collectTagList != null && !collectTagList.isEmpty()) {
                // 歌单tag
                // 先查找歌单和tag中间表，再查找tag记录表
                List<String> tags = collectIdAndTagsIdList.stream()
                                                          .filter(tbCollectTagPojo -> tbCollectTagPojo.getId()
                                                                                                      .equals(tbCollectPojo.getId()))
                                                          .map(tbCollectTagPojo -> getTags(tbCollectTagPojo.getTagId(), collectTagList))
                                                          .toList();
                item.setTags(tags);
            }
            // 歌单名
            item.setName(tbCollectPojo.getPlayListName());
            // 歌单ID
            item.setId(tbCollectPojo.getId());
            
            playListVo.getPlaylist().add(item);
        }
        return playListVo;
    }
    
    /**
     * 获取歌单tag
     *
     * @param tagId   tag id
     * @param tagList 风格
     * @return 风格名称
     */
    private String getTags(Long tagId, List<TbTagPojo> tagList) {
        for (TbTagPojo tag : tagList) {
            if (Objects.equals(tag.getId(), tagId)) {
                return tag.getTagName();
            }
        }
        return "";
    }
    
    /**
     * 获取用户创建歌单数量
     *
     * @param userId 用户ID
     * @return 歌单数量
     */
    public Long getCreatedPlaylistCount(Long userId) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getType, PlayListTypeConstant.ORDINARY)
                                                                       .eq(TbCollectPojo::getUserId, userId);
        return collectService.count(lambdaQueryWrapper);
    }
    
    
    /**
     * 获取订阅(收藏)歌单总数
     *
     * @param userId 用户ID
     * @return 订阅歌单总数
     */
    public Long getSubPlaylistCount(Long userId) {
        Stream<TbUserCollectEntity> collectEntityStream = userCollectEntityRepository.findByUserIdEquals(userId);
        // 过滤创建者用户ID
        return collectEntityStream.filter(tbUserCollectEntity -> !Objects.equals(tbUserCollectEntity.getTbCollectByCollectId().getUserId(), userId))
                                  .count();
    }
    
    /**
     * 获取用户关注歌曲家数量
     *
     * @return 关注数量
     */
    public Long getUserBySinger(Long userId) {
        LambdaQueryWrapper<TbUserArtistPojo> lambdaQueryWrapper = Wrappers.<TbUserArtistPojo>lambdaQuery()
                                                                          .eq(TbUserArtistPojo::getUserId, userId);
        return userSingerService.count(lambdaQueryWrapper);
    }
    
    /**
     * 用户播放音乐数量
     *
     * @param uid  用户ID
     */
    public List<UserRecordRes> userRecord(Long uid, Long type) {
        log.debug("type: {}", type);
        ArrayList<UserRecordRes> res = new ArrayList<>();
        List<TbHistoryPojo> list = historyService.list(Wrappers.<TbHistoryPojo>lambdaQuery().eq(TbHistoryPojo::getUserId, uid));
        Map<Long, TbHistoryPojo> rankPojoMap = list.stream().collect(Collectors.toMap(TbHistoryPojo::getId, tbRankPojo -> tbRankPojo));
        
        List<TbMusicPojo> musicPojoList;
        if (CollUtil.isEmpty(list)) {
            Page<TbMusicPojo> page = musicService.page(new Page<>(0, 100L));
            musicPojoList = page.getRecords();
        } else {
            List<Long> musicIds = list.stream().map(TbHistoryPojo::getId).toList();
            musicPojoList = musicService.listByIds(musicIds);
        }
        
        Map<Long, List<ArtistConvert>> artistByMusicIdToMap = qukuService.getArtistByMusicIdToMap(musicPojoList.parallelStream()
                                                                                                               .map(TbMusicPojo::getId)
                                                                                                               .toList());
        Map<Long, AlbumConvert> musicAlbumByAlbumIdToMap = qukuService.getMusicAlbumByAlbumIdToMap(musicPojoList.parallelStream()
                                                                                                                .map(TbMusicPojo::getAlbumId)
                                                                                                                .toList());
        for (TbMusicPojo tbMusicPojo : musicPojoList) {
            UserRecordRes userRecordRes = new UserRecordRes();
            int count = rankPojoMap.get(tbMusicPojo.getId()) == null ? 0 : rankPojoMap.get(tbMusicPojo.getId()).getCount();
            userRecordRes.setPlayCount(count);
            Song song = new Song();
            song.setName(tbMusicPojo.getMusicName());
            song.setId(tbMusicPojo.getId());
            song.setAlia(AliasUtil.getAliasList(tbMusicPojo.getAliasName()));
            
            List<ArtistConvert> singerByMusicId = artistByMusicIdToMap.get(tbMusicPojo.getId());
            ArrayList<ArItem> ar = new ArrayList<>();
            for (ArtistConvert tbArtistPojo : singerByMusicId) {
                ArItem e = new ArItem();
                e.setAlias(AliasUtil.getAliasList(tbArtistPojo.getAliasName()));
                e.setName(tbArtistPojo.getArtistName());
                e.setId(tbArtistPojo.getId());
                ar.add(e);
            }
            song.setAr(ar);
            
            AlbumConvert albumByMusicId = musicAlbumByAlbumIdToMap.get(tbMusicPojo.getAlbumId());
            if (Objects.nonNull(albumByMusicId)) {
                Al al = new Al();
                al.setId(albumByMusicId.getId());
                al.setPicUrl(albumByMusicId.getPicUrl());
                al.setName(albumByMusicId.getAlbumName());
                song.setAl(al);
            }
            userRecordRes.setSong(song);
    
            res.add(userRecordRes);
        }
        
        return res;
    }
    
    /**
     * 检查账户是否存在
     *
     * @param phone       账户
     * @param countrycode 手机号默认86
     */
    public UserConvert checkPhone(Long phone, String countrycode) {
        SysUserPojo one = accountService.getOne(Wrappers.<SysUserPojo>lambdaQuery().eq(SysUserPojo::getUsername, phone));
        UserConvert userConvert = new UserConvert();
        BeanUtils.copyProperties(one, userConvert);
        userConvert.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(one.getId()));
        userConvert.setBackgroundPicUrl(remoteStorePicService.getUserBackgroundPicUrl(one.getId()));
        return userConvert;
    }
    
    /**
     * 获取用户详情
     *
     * @param uid 用户ID
     * @return 用户信息
     */
    public UserDetailRes userDetail(Long uid) {
        UserDetailRes res = new UserDetailRes();
        SysUserPojo accUserPojo = accountService.getById(uid);
        res.setLevel(23333);
        res.setListenSongs(23333);
        UserPoint userPoint = new UserPoint();
        userPoint.setUserId(accUserPojo.getId());
        userPoint.setUpdateTime(accUserPojo.getUpdateTime().getNano());
        userPoint.setStatus(1);
        res.setUserPoint(userPoint);
        
        
        Profile profile = new Profile();
        profile.setUserId(accUserPojo.getId());
        profile.setAvatarUrl(remoteStorePicService.getUserAvatarPicUrl(accUserPojo.getId()));
        profile.setBackgroundUrl(remoteStorePicService.getUserBackgroundPicUrl(accUserPojo.getId()));
        profile.setEventCount(233);
        profile.setFollows(2333);
        profile.setFolloweds(23333);
        res.setProfile(profile);
        
        ProfileVillageInfo profileVillageInfo = new ProfileVillageInfo();
        profileVillageInfo.setTitle("crown");
        profileVillageInfo.setImageUrl(remoteStorePicService.getUserAvatarPicUrl(accUserPojo.getId()));
        profileVillageInfo.setTargetUrl(remoteStorePicService.getUserBackgroundPicUrl(accUserPojo.getId()));
        res.setProfileVillageInfo(profileVillageInfo);
        
        return res;
    }
}

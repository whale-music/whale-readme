package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.config.NeteaseCloudConfig;
import org.api.neteasecloudmusic.model.vo.playlist.Creator;
import org.api.neteasecloudmusic.model.vo.playlist.PlayListVo;
import org.api.neteasecloudmusic.model.vo.playlist.PlaylistItem;
import org.api.neteasecloudmusic.model.vo.user.detail.Profile;
import org.api.neteasecloudmusic.model.vo.user.detail.ProfileVillageInfo;
import org.api.neteasecloudmusic.model.vo.user.detail.UserDetailRes;
import org.api.neteasecloudmusic.model.vo.user.detail.UserPoint;
import org.api.neteasecloudmusic.model.vo.user.record.Al;
import org.api.neteasecloudmusic.model.vo.user.record.ArItem;
import org.api.neteasecloudmusic.model.vo.user.record.Song;
import org.api.neteasecloudmusic.model.vo.user.record.UserRecordRes;
import org.core.common.exception.BaseException;
import org.core.common.page.Page;
import org.core.common.reflection.ReflectionFieldName;
import org.core.common.result.ResultCode;
import org.core.iservice.*;
import org.core.pojo.*;
import org.core.service.AccountService;
import org.core.service.QukuService;
import org.core.utils.AliasUtil;
import org.core.utils.CollectSortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private AccountService accountService;
    
    // 歌单表
    @Autowired
    private CollectService collectService;
    
    @Autowired
    private CollectMusicService collectMusicService;
    
    // 用户关注歌手表
    @Autowired
    private UserArtistService userArtistService;
    
    @Autowired
    private RankService rankService;
    
    @Autowired
    private MusicService musicService;
    
    @Autowired
    private QukuService qukuService;
    
    @Autowired
    private CollectApi collectApi;
    
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
    public SysUserPojo getAccount(Long userId) {
        SysUserPojo userPojo = accountService.getById(userId);
        if (userPojo == null) {
            throw new BaseException(ResultCode.USER_NOT_EXIST);
        }
        return userPojo;
    }
    
    /**
     * 用户登录
     *
     * @param phone    账号
     * @param password 密码
     * @return 返回用户信息
     */
    public SysUserPojo login(String phone, String password) {
        return accountService.login(phone, password);
    }
    
    /**
     * 跟新用户信息
     *
     * @param userPojo 用户信息
     */
    public void updateUserPojo(SysUserPojo userPojo) {
        SysUserPojo pojo = accountService.updateById(userPojo);
        if (pojo != null) {
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
    public PlayListVo getPlayList(Long uid, Integer pageIndex, Integer pageSize) {
        PlayListVo playListVo = new PlayListVo();
        playListVo.setPlaylist(new ArrayList<>());
        playListVo.setVersion("0");
        
        Specification<CollectPojo> collectPojoSpecification = (root, query, criteriaBuilder) -> {
            Predicate equal = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getUserId)), uid);
            Order desc = criteriaBuilder.desc(root.get(ReflectionFieldName.getFieldName(CollectPojo::getSort)));
            query.orderBy(desc, desc);
            return query.where(equal).getRestriction();
        };
        Page<CollectPojo> collectPojoPage = collectService.findAll(collectPojoSpecification, PageRequest.of(pageIndex, pageSize));
        
        List<CollectPojo> last = CollectSortUtil.userLikeUserSort(uid, collectPojoPage.getRecords());
        collectPojoPage.setRecords(last);
        
        // 是否有下一页
        playListVo.setMore(collectPojoPage.hasNext());
        List<CollectPojo> collectPojoList = collectPojoPage.getRecords();
        if (CollUtil.isEmpty(collectPojoList)) {
            return new PlayListVo();
        }
        // 导出歌单id
        List<Long> collectIds = collectPojoList.stream().map(CollectPojo::getId).collect(Collectors.toList());
        // 根据歌单和tag的中间表来获取tag id列表
        List<CollectTagPojo> collectIdAndTagsIdList = collectApi.getCollectTagIdList(collectIds);
        // 根据tag id 列表获取tag Name列表
        List<Long> tagIdList = collectIdAndTagsIdList.stream()
                                                     .map(CollectTagPojo::getTagId)
                                                     .collect(Collectors.toList());
        List<TagPojo> collectTagList = collectApi.getTagPojoList(tagIdList);
        
        
        for (CollectPojo collectPojo : collectPojoList) {
            PlaylistItem item = new PlaylistItem();
            // 是否订阅
            item.setSubscribed(false);
            // 歌单数量
            item.setTrackCount(collectMusicService.countLambda(CollectMusicPojo::getCollectId, collectPojo.getId()));
            // 创作者
            Creator creator = new Creator();
            SysUserPojo account = Optional.ofNullable(accountService.getById(uid)).orElse(new SysUserPojo());
            creator.setNickname(account.getNickname());
            creator.setUserId(account.getId());
            creator.setAvatarUrl(account.getAvatarUrl());
            creator.setBackgroundUrl(account.getBackgroundUrl());
            item.setCreator(creator);
            // 用户ID
            item.setUserId(collectPojo.getUserId());
            // 封面图像ID
            item.setCoverImgUrl(collectPojo.getPic());
            // 创建时间
            item.setCreateTime(collectPojo.getCreateTime().getNano());
            // 描述
            item.setDescription(collectPojo.getDescription());
            // 判断中间表是否有值
            // 判断tag表是否有值
            if (!collectIdAndTagsIdList.isEmpty() && collectTagList != null && !collectTagList.isEmpty()) {
                // 歌单tag
                // 先查找歌单和tag中间表，再查找tag记录表
                List<String> tags = collectIdAndTagsIdList.stream()
                                                          .filter(tbCollectTagPojo -> tbCollectTagPojo.getCollectId()
                                                                                                      .equals(collectPojo.getId()))
                                                          .map(tbCollectTagPojo -> getTags(tbCollectTagPojo.getTagId(), collectTagList))
                                                          .collect(Collectors.toList());
                item.setTags(tags);
            }
            // 歌单名
            item.setName(collectPojo.getPlayListName());
            // 歌单ID
            item.setId(collectPojo.getId());
            
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
    private String getTags(Long tagId, List<TagPojo> tagList) {
        for (TagPojo tag : tagList) {
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
        Specification<CollectPojo> collectPojoSpecification = (root, query, criteriaBuilder) -> {
            Predicate equal = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getType)), Short.valueOf("0"));
            Predicate equal1 = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getUserId)), userId);
            return query.where(equal1, equal).getRestriction();
        };
        return collectService.count(collectPojoSpecification);
    }
    
    
    /**
     * 获取订阅(收藏)歌单总数
     *
     * @param userId 用户ID
     * @return 订阅歌单总数
     */
    public Long getSubPlaylistCount(Long userId) {
        Specification<CollectPojo> collectPojoSpecification = (root, query, criteriaBuilder) -> {
            Predicate equal = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getType)), Short.valueOf("0"));
            Predicate equal1 = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getUserId)), userId);
            Predicate equal2 = criteriaBuilder.equal(root.get(ReflectionFieldName.getFieldName(CollectPojo::getSubscribed)), true);
            return query.where(equal, equal1, equal2).getRestriction();
        };
        return collectService.count(collectPojoSpecification);
    }
    
    /**
     * 获取用户关注歌曲家数量
     *
     * @return 关注数量
     */
    public Long getUserBySinger(Long userId) {
        return userArtistService.countLambda(UserArtistPojo::getUserId, userId);
    }
    
    /**
     * 用户播放音乐数量
     *
     * @param uid 用户ID
     */
    public List<UserRecordRes> userRecord(Long uid, Long type) {
        ArrayList<UserRecordRes> res = new ArrayList<>();
        List<RankPojo> list = rankService.listLambdaEq(RankPojo::getUserId, uid);
        Map<Long, RankPojo> rankPojoMap = list.stream().collect(Collectors.toMap(RankPojo::getId, tbRankPojo -> tbRankPojo));
        
        List<MusicPojo> musicPojoList;
        if (CollUtil.isEmpty(list)) {
            Page<MusicPojo> page = musicService.findAll(null, PageRequest.of(0, 100));
            musicPojoList = page.getRecords();
        } else {
            List<Long> musicIds = list.stream().map(RankPojo::getId).collect(Collectors.toList());
            musicPojoList = musicService.listByIds(musicIds);
        }
        
        for (MusicPojo musicPojo : musicPojoList) {
            UserRecordRes userRecordRes = new UserRecordRes();
            int count = rankPojoMap.get(musicPojo.getId()) == null ? 0 : rankPojoMap.get(musicPojo.getId()).getBroadcastCount();
            userRecordRes.setPlayCount(count);
            Song song = new Song();
            song.setName(musicPojo.getMusicName());
            song.setId(musicPojo.getId());
            song.setAlia(AliasUtil.getAliasList(musicPojo.getAliasName()));
            
            List<ArtistPojo> singerByMusicId = qukuService.getSingerByMusicId(musicPojo.getId());
            ArrayList<ArItem> ar = new ArrayList<>();
            for (ArtistPojo artistPojo : singerByMusicId) {
                ArItem e = new ArItem();
                e.setAlias(AliasUtil.getAliasList(artistPojo.getAliasName()));
                e.setName(artistPojo.getArtistName());
                e.setId(artistPojo.getId());
                ar.add(e);
            }
            song.setAr(ar);
            
            AlbumPojo albumByMusicId = qukuService.getAlbumByMusicId(musicPojo.getId());
            Al al = new Al();
            al.setId(albumByMusicId.getId());
            al.setPicUrl(albumByMusicId.getPic());
            al.setName(albumByMusicId.getAlbumName());
            song.setAl(al);
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
    public SysUserPojo checkPhone(Long phone, String countrycode) {
        return accountService.getOne(SysUserPojo::getUsername, phone).orElseThrow(() -> new BaseException(ResultCode.USER_NOT_EXIST));
    }
    
    /**
     * 获取用户详情
     *
     * @param uid 用户ID
     * @return
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
        profile.setAvatarUrl(accUserPojo.getAvatarUrl());
        profile.setBackgroundUrl(accUserPojo.getBackgroundUrl());
        profile.setEventCount(233);
        profile.setFollows(2333);
        profile.setFolloweds(23333);
        res.setProfile(profile);
        
        ProfileVillageInfo profileVillageInfo = new ProfileVillageInfo();
        profileVillageInfo.setTitle("crown");
        profileVillageInfo.setImageUrl(accUserPojo.getAvatarUrl());
        profileVillageInfo.setTargetUrl(accUserPojo.getBackgroundUrl());
        res.setProfileVillageInfo(profileVillageInfo);
        
        return res;
    }
}

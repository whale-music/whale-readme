package org.api.neteasecloudmusic.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.pojo.*;
import org.core.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 歌单中间层
 */
@Slf4j
@Service
public class CollectApi {
    
    @Autowired
    private TbCollectService collectService;
    
    @Autowired
    private TbTagService tagService;
    
    @Autowired
    private TbCollectTagService collectTagService;
    
    @Autowired
    private TbCollectMusicService collectMusicService;
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbMusicUrlService musicUrlService;
    
    @Autowired
    private TbLikeService likeService;
    
    @Autowired
    private TbLikeMusicService likeMusicService;
    
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
     * 检查用户是否有权限操作歌单
     *
     * @param userId        用户ID
     * @param tbCollectPojo 歌单信息
     */
    private static void checkUserAuth(Long userId, TbCollectPojo tbCollectPojo) {
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
     * 查询tag表
     * 根据tag id列表返回歌单tag信息
     *
     * @param tagIds tag ID
     */
    public List<TbTagPojo> getTagPojoList(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbTagPojo> lambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery()
                                                                   .in(TbTagPojo::getId, tagIds);
        return tagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 根据歌单和tag中间表
     * 获取歌单对各的tag id
     *
     * @param collectId 歌单ID
     * @return 返回中间表tag id列表
     */
    public List<TbCollectTagPojo> getCollectTagIdList(List<Long> collectId) {
        if (collectId == null || collectId.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbCollectTagPojo> lambdaQueryWrapper = Wrappers.<TbCollectTagPojo>lambdaQuery()
                                                                          .in(TbCollectTagPojo::getCollectId,
                                                                                  collectId);
        return collectTagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 创建歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @return 歌单信息
     */
    public TbCollectPojo createPlayList(Long userId, String name) {
        TbCollectPojo collectPojo = new TbCollectPojo();
        collectPojo.setUserId(userId);
        collectPojo.setPlayListName(name);
        collectPojo.setSort(collectService.count() + 1);
        collectPojo.setPic("https://p1.music.126.net/jWE3OEZUlwdz0ARvyQ9wWw==/109951165474121408.jpg");
        collectPojo.setSubscribed(false);
        collectService.save(collectPojo);
        return collectPojo;
    }
    
    /**
     * 修改用户歌单信息,值为null则不修改
     *
     * @param userId      用户ID
     * @param collectPojo 歌单信息
     */
    public void updatePlayList(Long userId, TbCollectPojo collectPojo) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectPojo.getId());
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
        TbCollectPojo collectPojo = new TbCollectPojo();
        collectPojo.setId(collectId);
        collectPojo.setUserId(userId);
        checkUserAuth(userId, collectPojo);
        
        
        // 如果为0则直接不需要保存操作
        if (split.length == 0) {
            return;
        }
        
        // 先删除歌单的关联tag然后在重新添加
        LambdaQueryWrapper<TbCollectTagPojo> collectLambdaQueryWrapper = Wrappers.<TbCollectTagPojo>lambdaQuery()
                                                                                 .eq(TbCollectTagPojo::getCollectId,
                                                                                         collectId);
        collectTagService.remove(collectLambdaQueryWrapper);
        
        // tag为空字符串跳过
        if (split.length == 1 && StringUtils.isBlank(split[0])) {
            return;
        }
        
        List<String> splitList = Arrays.asList(split);
        
        // 需要存入的tag list
        List<TbCollectTagPojo> saveCollectTagPojoList = new ArrayList<>(split.length);
        // 用来批量存放tb tag
        List<TbTagPojo> saveTbTagList = new ArrayList<>();
        
        LambdaQueryWrapper<TbTagPojo> tagPojoLambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery()
                                                                          .in(TbTagPojo::getTagName, splitList);
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
                TbCollectTagPojo e = new TbCollectTagPojo();
                e.setTagId(tbTagPojo.getId());
                e.setCollectId(collectId);
                saveCollectTagPojoList.add(e);
            } else {
                // 已有tag信息, 直接使用tag id
                TbCollectTagPojo e = new TbCollectTagPojo();
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
     * @param collectIds 歌单ID
     */
    public void removePlayList(Long userId, String[] collectIds) {
        List<String> collectList = Arrays.asList(collectIds);
        List<TbCollectPojo> tbCollectPojos = collectService.listByIds(collectList);
        for (TbCollectPojo tbCollectPojo : tbCollectPojos) {
            checkUserAuth(userId, tbCollectPojo);
        }
        
        // 删除歌单ID
        collectService.removeByIds(collectList);
        // 删除歌单关联tag
        collectTagService.remove(Wrappers.<TbCollectTagPojo>lambdaQuery()
                                         .in(TbCollectTagPojo::getCollectId, collectList));
    }
    
    /**
     * 收藏/取消歌单
     *
     * @param userId    用户ID
     * @param collectId 歌单ID
     * @param flag      取消/收藏 1:收藏,2:取消收藏
     */
    public void subscribePlayList(Long userId, String collectId, Integer flag) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectId);
        // 需要收藏歌单存在，并且用户不一样（防止重复收藏）
        if (tbCollectPojo != null && !Objects.equals(tbCollectPojo.getUserId(), userId) && flag == 1) {
            tbCollectPojo.setId(null);
            // 复制歌单，并更新信息
            tbCollectPojo.setUserId(userId);
            // 收藏
            tbCollectPojo.setSubscribed(true);
            // 增加排序值
            tbCollectPojo.setSort(collectService.count() + 1);
            collectService.save(tbCollectPojo);
        } else if (Boolean.TRUE.equals(tbCollectPojo != null && tbCollectPojo.getSubscribed()) && flag == 2) {
            //歌单存在并且是收藏状态
            // 删除歌单
            removePlayList(userId, new String[]{collectId});
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
    public Page<TbMusicPojo> getPlayListAllSong(Long collectId, Long pageIndex, Long pageSize) {
        LambdaQueryWrapper<TbCollectMusicPojo> wrapper = Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                 .in(TbCollectMusicPojo::getCollectId, collectId);
        Page<TbCollectMusicPojo> page = collectMusicService.page(new Page<>(pageIndex, pageSize), wrapper);
        if (page.getTotal() == 0) {
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        List<Long> musicIds = page.getRecords()
                                  .stream()
                                  .map(TbCollectMusicPojo::getMusicId)
                                  .toList();
        List<TbMusicPojo> tbMusicPojoList = musicService.listByIds(musicIds);
    
        Page<TbMusicPojo> musicPojoPage = new Page<>();
        musicPojoPage.setRecords(tbMusicPojoList);
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
    public List<TbMusicUrlPojo> getMusicInfo(List<Long> musicIds) {
        return musicUrlService.list(Wrappers.<TbMusicUrlPojo>lambdaQuery().in(TbMusicUrlPojo::getMusicId, musicIds));
    }
    
    /**
     * 添加歌曲到歌单
     *
     * @param userID    用户ID
     * @param collectId 歌单ID
     * @param songIds   歌曲ID
     * @param flag      添加还是删除
     */
    public void addSongToCollect(Long userID, Long collectId, List<Long> songIds, boolean flag) {
        TbCollectPojo tbCollectPojo = collectService.getById(collectId);
        checkUserAuth(userID, tbCollectPojo);
    
        if (flag) {
            // 添加
            List<TbMusicPojo> tbMusicPojo = musicService.listByIds(songIds);
            List<TbCollectMusicPojo> collect = tbMusicPojo.stream()
                                                          .map(tbMusicPojo1 -> new TbCollectMusicPojo().setCollectId(
                                                                  collectId).setMusicId(tbMusicPojo1.getId()))
                                                          .toList();
            collectMusicService.saveBatch(collect);
        } else {
            // 删除歌曲
            collectMusicService.remove(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                               .eq(TbCollectMusicPojo::getCollectId, collectId)
                                               .in(TbCollectMusicPojo::getMusicId, songIds));
        }
    
    }
    
    /**
     * 添加喜爱歌曲
     *
     * @param id              歌曲ID
     * @param isAddAndDelLike true添加歌曲，false删除歌曲
     */
    public void like(Long userId, Long id, Boolean isAddAndDelLike) {
        TbLikePojo entity = new TbLikePojo();
        entity.setUserId(userId);
        likeService.saveOrUpdate(entity);
        TbMusicPojo byId = musicService.getById(id);
        if (byId == null) {
            log.debug("添加歌曲不存在");
            throw new BaseException(ResultCode.SONG_NOT_EXIST);
        }
        
        // 效验歌单中是否有该歌曲
        LambdaQueryWrapper<TbLikeMusicPojo> wrapper = Wrappers.<TbLikeMusicPojo>lambdaQuery()
                                                              .eq(TbLikeMusicPojo::getLikeId, id);
        long count = likeMusicService.count(wrapper);
        if (Boolean.TRUE.equals(isAddAndDelLike)) {
            // 歌曲已存在
            if (count >= 1) {
                throw new BaseException(ResultCode.SONG_EXIST);
            }
            TbLikeMusicPojo tbLikeMusicPojo = new TbLikeMusicPojo();
            tbLikeMusicPojo.setLikeId(userId);
            tbLikeMusicPojo.setMusicId(id);
            likeMusicService.save(tbLikeMusicPojo);
            log.debug("歌曲保存");
        } else {
            // 歌曲不存在
            if (count == 0) {
                throw new BaseException(ResultCode.SONG_NOT_EXIST);
            }
            likeMusicService.remove(wrapper);
            log.debug("歌曲已删除");
        }
        
    }
    
    /**
     * 查询用户喜爱歌单
     *
     * @param uid 用户ID
     * @return 返回歌曲数组
     */
    public List<Long> likelist(Long uid) {
        List<TbLikeMusicPojo> list = likeMusicService.list(Wrappers.<TbLikeMusicPojo>lambdaQuery()
                                                                   .eq(TbLikeMusicPojo::getLikeId, uid));
        return list.stream().map(TbLikeMusicPojo::getLikeId).toList();
    }
}

package org.api.neteasecloudmusic.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.api.neteasecloudmusic.model.vo.user.record.Al;
import org.api.neteasecloudmusic.model.vo.user.record.ArItem;
import org.api.neteasecloudmusic.model.vo.user.record.Song;
import org.api.neteasecloudmusic.model.vo.user.record.UserRecordRes;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;
import org.core.pojo.*;
import org.core.service.*;
import org.core.utils.AliasUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
@Service("neteasecloudUser")
public class UserApi {
    // 用户服务
    @Autowired
    private AccountService accountService;
    
    // 歌单表
    @Autowired
    private TbCollectService collectService;
    
    
    // 用户关注歌手表
    @Autowired
    private TbUserSingerService userSingerService;
    
    @Autowired
    private TbRankService rankService;
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private QukuService qukuService;
    
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
    public Page<TbCollectPojo> getPlayList(Long uid, Long pageIndex, Long pageSize) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getUserId, uid)
                                                                       .orderByDesc(TbCollectPojo::getSort);
        return collectService.page(new Page<>(pageIndex, pageSize), lambdaQueryWrapper);
    }
    
    /**
     * 获取用户创建歌单数量
     *
     * @param userId 用户ID
     * @return 歌单数量
     */
    public Long getCreatedPlaylistCount(Long userId) {
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
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
        LambdaQueryWrapper<TbCollectPojo> lambdaQueryWrapper = Wrappers.<TbCollectPojo>lambdaQuery()
                                                                       .eq(TbCollectPojo::getUserId, userId)
                                                                       .eq(TbCollectPojo::getSubscribed, true);
        return collectService.count(lambdaQueryWrapper);
    }
    
    /**
     * 获取用户关注歌曲家数量
     *
     * @return 关注数量
     */
    public Long getUserBySinger(Long userId) {
        LambdaQueryWrapper<TbUserSingerPojo> lambdaQueryWrapper = Wrappers.<TbUserSingerPojo>lambdaQuery()
                                                                          .eq(TbUserSingerPojo::getUserId, userId);
        return userSingerService.count(lambdaQueryWrapper);
    }
    
    public List<UserRecordRes> userRecord(Long uid, Long type) {
        ArrayList<UserRecordRes> res = new ArrayList<>();
        List<TbRankPojo> list = rankService.list(Wrappers.<TbRankPojo>lambdaQuery().eq(TbRankPojo::getUserId, uid));
        Map<Long, TbRankPojo> rankPojoMap = list.stream().collect(Collectors.toMap(TbRankPojo::getId, tbRankPojo -> tbRankPojo));
    
        List<TbMusicPojo> musicPojoList;
        if (CollUtil.isEmpty(list)) {
            Page<TbMusicPojo> page = musicService.page(new Page<>(0, 100L));
            musicPojoList = page.getRecords();
        } else {
            List<Long> musicIds = list.stream().map(TbRankPojo::getId).collect(Collectors.toList());
            musicPojoList = musicService.listByIds(musicIds);
        }
        
        for (TbMusicPojo tbMusicPojo : musicPojoList) {
            UserRecordRes userRecordRes = new UserRecordRes();
            int count = rankPojoMap.get(tbMusicPojo.getId()) == null ? 0 : rankPojoMap.get(tbMusicPojo.getId()).getBroadcastCount();
            userRecordRes.setPlayCount(count);
            Song song = new Song();
            song.setName(tbMusicPojo.getMusicName());
            song.setId(tbMusicPojo.getId());
            song.setAlia(AliasUtil.getAliasList(tbMusicPojo.getAliaName()));
            
            List<TbSingerPojo> singerByMusicId = qukuService.getSingerByMusicId(tbMusicPojo.getId());
            ArrayList<ArItem> ar = new ArrayList<>();
            for (TbSingerPojo tbSingerPojo : singerByMusicId) {
                ArItem e = new ArItem();
                e.setAlias(AliasUtil.getAliasList(tbSingerPojo.getAlias()));
                e.setName(tbSingerPojo.getSingerName());
                e.setId(tbSingerPojo.getId());
                ar.add(e);
            }
            song.setAr(ar);
            
            TbAlbumPojo albumByMusicId = qukuService.getAlbumByMusicId(tbMusicPojo.getId());
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
        return accountService.getOne(Wrappers.<SysUserPojo>lambdaQuery().eq(SysUserPojo::getUsername, phone));
    }
}

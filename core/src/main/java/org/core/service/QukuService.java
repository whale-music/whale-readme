package org.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.core.pojo.*;

import java.util.List;
import java.util.Set;

public interface QukuService {
    
    /**
     * 获取专辑信息
     */
    TbAlbumPojo getAlbumByMusicId(Long musicId);
    
    /**
     * 获取专辑数据
     */
    TbAlbumPojo getAlbumByAlbumId(Long albumIds);
    
    /**
     * 批量获取专辑数据
     * Long -> music ID
     */
    List<TbAlbumPojo> getAlbumListByMusicId(List<Long> musicIds);
    
    /**
     * 批量获取专辑数据
     * Long -> Album ID
     */
    List<TbAlbumPojo> getAlbumListByAlbumId(List<Long> albumIds);
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    List<TbSingerPojo> getSingerListByMusicId(List<Long> musicIds);
    
    /**
     * 获取歌手信息
     */
    List<TbSingerPojo> getSingerByMusicId(Long musicId);
    
    /**
     * 获取歌曲下载地址
     */
    List<TbMusicUrlPojo> getMusicUrl(Long musicId);
    
    /**
     * 批量获取歌曲下载地址
     */
    List<TbMusicUrlPojo> getMusicUrl(Set<Long> musicId);
    
    /**
     * 随即获取曲库中的一条数据
     */
    TbMusicPojo randomMusic();
    
    /**
     * 随机获取一条专辑
     */
    Page<TbAlbumPojo> getAlbumPage(String area, Long offset, Long limit);
    
    /**
     * 查询专辑下音乐数量
     */
    Integer getAlbumMusicCountByAlbumId(Long albumId);
    
    /**
     * 查询专辑下音乐数量
     */
    Integer getAlbumMusicCountByMusicId(Long musicId);
    
    /**
     * 获取歌手音乐数量
     *
     * @param id 歌手ID
     */
    Long getMusicCountBySingerId(Long id);
    
    /**
     * 获取专辑歌手列表
     */
    List<TbSingerPojo> getSingerListByAlbumIds(Long albumIds);
    
    /**
     * 获取专辑歌手列表
     */
    List<TbSingerPojo> getSingerListByAlbumIds(List<Long> albumIds);
    
    
    /**
     * 获取用户收藏专辑
     *
     * @param user    用户信息
     * @param current 当前页数
     * @param size    每页数量
     */
    List<TbAlbumPojo> getUserCollectAlbum(SysUserPojo user, Long current, Long size);
    
    /**
     * 获取用户关注歌手
     *
     * @param user 用户信息
     */
    List<TbSingerPojo> getUserLikeSingerList(SysUserPojo user);
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param id 歌手ID
     */
    Integer getAlbumCountBySingerId(Long id);
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param id 专辑ID
     */
    List<TbMusicPojo> getMusicListByAlbumId(Long id);
    
}

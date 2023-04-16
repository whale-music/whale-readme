package org.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.core.pojo.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
    List<TbAlbumPojo> getAlbumListByAlbumId(Collection<Long> albumIds);
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    List<TbArtistPojo> getArtistListByMusicId(List<Long> musicIds);
    
    /**
     * (此接口用于优化歌单请求过慢)
     * 批量获取歌手信息，优化版本
     * key 值为音乐ID
     * value 为歌手
     * 并且查询时增加缓存查找，防止多次访问数据库，造成qps过高
     */
    Map<Long, List<TbArtistPojo>> getArtistListByMusicIdToMap(Map<Long, TbAlbumPojo> albumPojoMap);
    
    /**
     * 获取歌手信息
     */
    List<TbArtistPojo> getArtistByMusicId(Long musicId);
    
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
    List<TbArtistPojo> getArtistListByAlbumIds(Long albumIds);
    
    /**
     * 获取专辑歌手列表
     */
    List<TbArtistPojo> getArtistListByAlbumIds(List<Long> albumIds);
    
    /**
     * 通过歌手ID获取专辑列表
     *
     * @param ids 歌手ID
     */
    List<TbAlbumPojo> getAlbumListByArtistIds(List<Long> ids);
    
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
    List<TbArtistPojo> getUserLikeSingerList(SysUserPojo user);
    
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
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param ids 专辑ID
     */
    List<TbMusicPojo> getMusicListByAlbumId(Collection<Long> ids);
    
    /**
     * 根据歌手名查找音乐
     *
     * @param name 歌手
     */
    List<TbMusicPojo> getMusicListByArtistName(String name);
    
    /**
     * 获取歌手下音乐信息
     *
     * @param id 歌手ID
     */
    List<TbMusicPojo> getMusicListByArtistId(Long id);
    
    /**
     * 随机获取歌手
     *
     * @param count 获取数量
     */
    List<TbArtistPojo> randomSinger(int count);
    
    /**
     * 添加音乐到歌单
     *
     * @param userID        用户ID
     * @param tbCollectPojo 歌单数据
     * @param songIds       歌曲列表
     * @param flag          删除还是添加
     */
    void addMusicToCollect(Long userID, TbCollectPojo tbCollectPojo, List<Long> songIds, boolean flag);
    
    /**
     * 添加歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @param type   歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单
     * @return 歌单创建信息
     */
    TbCollectPojo createPlayList(Long userId, String name, short type);
}

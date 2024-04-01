package org.core.service;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public interface QukuService {
    
    /**
     * 获取专辑信息
     */
    AlbumConvert getAlbumByMusicId(Long musicId);
    
    /**
     * 获取专辑数据
     */
    AlbumConvert getAlbumByAlbumId(Long albumIds);
    
    /**
     * 批量获取专辑数据
     * @param musicIds music ID
     */
    List<AlbumConvert> getAlbumByMusicIds(List<Long> musicIds);
    
    /**
     * 批量获取专辑数据
     *
     * @param musicIds 音乐ID
     * @return 专辑ID
     */
    List<Long> getAlbumIdsByMusicIds(List<Long> musicIds);
    
    /**
     * 批量获取专辑数据
     * Long -> Album ID
     */
    List<AlbumConvert> getAlbumListByAlbumId(Collection<Long> albumIds);
    
    
    /**
     * 查询数据歌曲下载地址
     */
    List<TbResourcePojo> getMusicPaths(Collection<Long> musicId);
    
    /**
     * 查询数据歌曲下载地址
     * key music value url
     *
     * @param musicId 音乐ID
     */
    Map<Long, List<TbResourcePojo>> getMusicPathMap(Collection<Long> musicId);
    
    /**
     * 随即获取曲库中的一条数据
     */
    MusicConvert randomMusic();
    
    /**
     * 随即获取曲库中的多条数据
     */
    default List<MusicConvert> randomMusicList(int count) {
        return randomMusicList(count, null, null, null);
    }
    
    /**
     * 随即获取曲库中的多条数据
     */
    List<MusicConvert> randomMusicList(int count, String genre, Long fromYear, Long toYear);
    
    /**
     * 随机获取一条专辑
     */
    Page<AlbumConvert> getRandomAlbum(String area, Long offset, Long limit);
    
    Page<AlbumConvert> getAlbumPage(Page<TbAlbumPojo> albumPojoPage, Wrapper<TbAlbumPojo> lambdaQueryWrapper);
    
    /**
     * 查询专辑下音乐数量
     */
    Integer getAlbumMusicCountByAlbumId(Long albumId);
    
    /**
     * 专辑下所有音乐数量
     *
     * @param albumIds 专辑ID
     * @return 专辑音乐数量
     */
    Map<Long, Integer> getAlbumMusicCountByAlbumIdToMap(Collection<Long> albumIds);
    
    /**
     * 查询专辑下音乐数量
     */
    Integer getAlbumMusicCountByMusicId(Long musicId);
    
    /**
     * 获取歌手音乐数量
     *
     * @param id 歌手ID
     */
    Long getMusicCountByArtistId(Long id);
    
    /**
     * 根据专辑歌手列表
     */
    List<ArtistConvert> getArtistByAlbumIds(List<Long> albumIds);
    
    /**
     * 根据专辑歌手列表
     */
    default List<ArtistConvert> getArtistByAlbumIds(Long albumIds) {
        return this.getArtistByAlbumIds(Collections.singletonList(albumIds));
    }
    
    /**
     * 获取专辑歌手列表
     * Map
     * key to Album ID
     * value to Artist List
     *
     * @param albumIds 专辑ID
     */
    Map<Long, List<ArtistConvert>> getArtistByAlbumIdsToMap(Collection<Long> albumIds);
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    default List<ArtistConvert> getArtistByMusicIds(Long musicId) {
        return getArtistByMusicIds(Collections.singletonList(musicId));
    }
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    List<ArtistConvert> getArtistByMusicIds(Collection<Long> musicId);
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicIds 歌手ID
     * @return 歌手列表
     */
    Map<Long, List<ArtistConvert>> getArtistByMusicIdToMap(Collection<Long> musicIds);
    
    /**
     * 通过歌手ID获取专辑列表
     *
     * @param artistIds 歌手ID
     */
    List<AlbumConvert> getAlbumByArtistIds(List<Long> artistIds);
    
    /**
     * 通过歌手ID获取专辑ID
     *
     * @param artistIds 歌手ID
     */
    List<Long> getAlbumIdsByArtistIds(List<Long> artistIds);
    
    /**
     * 通过歌手ID获取专辑列表
     *
     * @param artistId 歌手ID
     */
    default List<AlbumConvert> getAlbumByArtistIds(Long artistId) {
        return this.getAlbumByArtistIds(Collections.singletonList(artistId));
    }
    
    /**
     * 获取Mv歌手
     *
     * @param mvIds 用户信息
     */
    Map<Long, List<ArtistConvert>> getMvArtistByMvIdToMap(List<Long> mvIds);
    
    /**
     * 获取MV歌手
     *
     * @param mvId MV id
     * @return 歌手信息
     */
    default Map<Long, List<ArtistConvert>> getMvArtistByMvIdToMap(Long mvId) {
        return getMvArtistByMvIdToMap(Collections.singletonList(mvId));
    }
    
    
    /**
     * 获取用户收藏专辑
     *
     * @param user    用户信息
     * @param current 当前页数
     * @param size    每页数量
     */
    List<AlbumConvert> getUserCollectAlbum(SysUserPojo user, Long current, Long size);
    
    /**
     * 获取用户关注歌手
     *
     * @param uid 用户信息
     */
    List<ArtistConvert> getUserLikeSingerList(Long uid);
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param artistIds 歌手ID
     * @return 歌手下的所有专辑 key: artist id, value: album 数量
     */
    Map<Long, Integer> getArtistAlbumCountByArtistIds(List<Long> artistIds);
    
    /**
     * 获取歌手所有专辑数量
     *
     * @param id 歌手ID
     */
    default Integer getArtistAlbumCountByArtistId(Long id) {
        return MapUtil.get(this.getArtistAlbumCountByArtistIds(Collections.singletonList(id)), id, Integer.class, 0);
    }
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param id 专辑ID
     */
    List<MusicConvert> getMusicListByAlbumId(Long id);
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param ids 专辑ID
     */
    List<MusicConvert> getMusicListByAlbumId(Collection<Long> ids);
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param ids 专辑ID
     * @return key 专辑ID value music list
     */
    Map<Long, List<MusicConvert>> getMusicMapByAlbumId(Collection<Long> ids);
    
    /**
     * 根据歌手名查找音乐
     *
     * @param name 歌手
     */
    List<MusicConvert> getMusicByArtistName(String name);
    
    /**
     * 获取歌手下音乐信息
     *
     * @param id 歌手ID
     */
    List<MusicConvert> getMusicListByArtistId(Long id);
    
    /**
     * 获取歌手下的所有音乐
     *
     * @param artistIds 歌手ID
     * @return key:artistId value: musicList
     */
    Map<Long, List<TbMusicPojo>> getMusicMapByArtistId(List<Long> artistIds);
    
    /**
     * 随机获取歌手
     *
     * @param count 获取数量
     */
    List<ArtistConvert> randomSinger(int count);
    
    /**
     * 添加或删除音乐到歌单
     *
     * @param userID    用户ID
     * @param collectId 歌单数据
     * @param songIds   歌曲列表
     * @param flag      删除还是添加 true add, false remove
     */
    void addOrRemoveMusicToCollect(Long userID, Long collectId, List<Long> songIds, boolean flag);
    
    /**
     * 添加歌单
     *
     * @param userId 用户ID
     * @param name   歌单名
     * @param type   歌单类型，0为普通歌单，1为用户喜爱歌单，2为推荐歌单
     * @return 歌单创建信息
     */
    CollectConvert createPlayList(Long userId, String name, byte type);
    
    /**
     * 删除歌单
     *
     * @param userId      用户ID
     * @param collectList 删除歌单ID
     */
    void removePlayList(Long userId, Collection<Long> collectList);
    
    /**
     * 获取用户所有音乐，包括喜爱歌单
     *
     * @param uid  用户ID
     * @param type 歌单类型
     * @return 返回用户创建歌单
     */
    List<CollectConvert> getUserPlayList(Long uid, Collection<Byte> type);
    
    /**
     * 获取歌单音乐数量
     *
     * @param id 歌单ID
     * @return 音乐数量
     */
    Integer getCollectMusicCount(Long id);
    
    /**
     * 获取歌曲歌词
     *
     * @param musicId 歌词ID
     * @return 歌词列表
     */
    List<TbLyricPojo> getMusicLyric(Long musicId);
    
    /**
     * 获取歌曲歌词
     *
     * @param musicId 歌词ID
     * @return 歌词列表 Long -> music id
     */
    Map<Long, List<TbLyricPojo>> getMusicLyric(Collection<Long> musicId);
    
    /**
     * 添加喜欢歌单
     *
     * @param userId          用户
     * @param id              音乐ID
     * @param isAddAndDelLike true添加 false删除
     */
    void collectLike(Long userId, List<Long> id, Boolean isAddAndDelLike);
    
    default void collectLike(Long userId, Long id, Boolean isAddAndDelLike) {
        collectLike(userId, Collections.singletonList(id), isAddAndDelLike);
    }
    
    /**
     * 删除音乐
     *
     * @param musicId 音乐ID
     * @param compel  是否强制删除
     */
    void deleteMusic(List<Long> musicId, Boolean compel);
    
    /**
     * 删除专辑
     * 强制删除会删除歌曲表
     *
     * @param id     专辑ID 列表
     * @param compel 是否强制删除
     */
    void deleteAlbum(List<Long> id, Boolean compel);
    
    /**
     * 删除歌手
     *
     * @param id 歌手ID
     */
    void deleteArtist(List<Long> id);
    
    /**
     * 保存或更新歌词
     *
     * @param musicId 音乐ID
     * @param type    歌词类型
     * @param lyric   歌词
     */
    void saveOrUpdateLyric(Long musicId, String type, String lyric);
    
    /**
     * 获取歌曲专辑
     *
     * @param musicIds 专辑ID
     * @return key 歌曲ID value 专辑信息
     */
    Map<Long, AlbumConvert> getMusicAlbumByMusicIdToMap(List<Long> musicIds);
    
    /**
     * 获取歌曲专辑
     *
     * @param albumIds 专辑ID
     * @return key 歌曲ID value 专辑信息
     */
    Map<Long, AlbumConvert> getMusicAlbumByAlbumIdToMap(Collection<Long> albumIds);
    
    /**
     * 获取专辑所有音乐时长
     *
     * @param albumIds 专辑ID
     * @return 专辑时长
     */
    Map<Long, Integer> getAlbumDurationCount(Collection<Long> albumIds);
    
    /**
     * 获取歌单音乐时长
     *
     * @param collectIds 专辑ID
     * @return 专辑时长
     */
    Map<Long, Integer> getCollectDurationCount(List<Long> collectIds);
    
    
    /**
     * 用户喜欢专辑
     * @param userId 用户ID
     * @param albumIds 专辑ID
     */
    void likeAlbum(Long userId, Collection<Long> albumIds);
    
    default void likeAlbum(Long userId, Long albumIds) {
        this.likeAlbum(userId, Collections.singletonList(albumIds));
    }
    
    /**
     * 用户取消喜欢专辑
     * @param userId 用户ID
     * @param albumIds 专辑ID
     */
    void unLikeAlbum(Long userId, Collection<Long> albumIds);
    
    default void unLikeAlbum(Long userId, Long albumIds) {
        this.unLikeAlbum(userId, Collections.singletonList(albumIds));
    }
    
    /**
     * 用户喜欢专辑
     * @param userId 用户ID
     * @param artistId 专辑ID
     */
    void likeArtist(Long userId, Collection<Long> artistId);
    
    default void likeArtist(Long userId, Long artistId) {
        this.likeArtist(userId, Collections.singletonList(artistId));
    }
    
    /**
     * 用户取消喜欢专辑
     * @param userId 用户ID
     * @param artistId 专辑ID
     */
    void unLikeArtist(Long userId, Collection<Long> artistId);
    
    default void unLikeArtist(Long userId, Long artistId) {
        this.unLikeAlbum(userId, Collections.singletonList(artistId));
    }
    
}

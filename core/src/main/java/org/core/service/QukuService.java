package org.core.service;

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.TargetTagConstant;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
     * Long -> music ID
     */
    List<AlbumConvert> getAlbumListByMusicId(List<Long> musicIds);
    
    /**
     * 批量获取专辑数据
     * Long -> Album ID
     */
    List<AlbumConvert> getAlbumListByAlbumId(Collection<Long> albumIds);
    
    /**
     * 批量获取歌手信息
     * Long -> music ID
     */
    List<ArtistConvert> getAlbumArtistListByMusicId(List<Long> musicIds);
    
    /**
     * (此接口用于优化歌单请求过慢)
     * 批量获取歌手信息，优化版本
     * key 值为音乐ID
     * value 为歌手
     * 并且查询时增加缓存查找，防止多次访问数据库，造成qps过高
     */
    Map<Long, List<ArtistConvert>> getAlbumArtistListByMusicIdToMap(Map<Long, TbAlbumPojo> albumPojoMap);
    
    /**
     * 获取专辑歌手信息
     */
    List<ArtistConvert> getAlbumArtistByMusicId(Long musicId);
    
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
    Map<Long, List<TbResourcePojo>> getMusicMapUrl(Collection<Long> musicId);
    
    /**
     * 随即获取曲库中的一条数据
     */
    MusicConvert randomMusic();
    
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
    List<ArtistConvert> getAlbumArtistListByAlbumIds(Long albumIds);
    
    /**
     * 获取专辑歌手列表
     */
    List<ArtistConvert> getAlbumArtistListByAlbumIds(List<Long> albumIds);
    
    /**
     * 获取专辑歌手列表
     * Map
     * key to Album ID
     * value to Artist List
     *
     * @param albumIds 专辑ID
     */
    Map<Long, List<ArtistConvert>> getAlbumArtistMapByAlbumIds(Collection<Long> albumIds);
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    default List<ArtistConvert> getMusicArtistByMusicId(Long musicId) {
        return getMusicArtistByMusicId(Collections.singletonList(musicId));
    }
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    List<ArtistConvert> getMusicArtistByMusicId(Collection<Long> musicId);
    
    /**
     * 获取歌曲歌手列表
     *
     * @param musicId 歌手ID
     * @return 歌手列表
     */
    Map<Long, List<ArtistConvert>> getMusicArtistByMusicIdToMap(Collection<Long> musicId);
    
    /**
     * 通过歌手ID获取专辑列表
     *
     * @param ids 歌手ID
     */
    List<AlbumConvert> getAlbumListByArtistIds(List<Long> ids);
    
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
     * @param user 用户信息
     */
    List<ArtistConvert> getUserLikeSingerList(SysUserPojo user);
    
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
    List<MusicConvert> getMusicListByAlbumId(Long id);
    
    /**
     * 根据专辑ID查找音乐
     *
     * @param ids 专辑ID
     */
    List<MusicConvert> getMusicListByAlbumId(Collection<Long> ids);
    
    /**
     * 根据歌手名查找音乐
     *
     * @param name 歌手
     */
    List<MusicConvert> getMusicListByArtistName(String name);
    
    /**
     * 获取歌手下音乐信息
     *
     * @param id 歌手ID
     */
    List<MusicConvert> getMusicListByArtistId(Long id);
    
    /**
     * 随机获取歌手
     *
     * @param count 获取数量
     */
    List<ArtistConvert> randomSinger(int count);
    
    /**
     * 添加音乐到歌单
     *
     * @param userID    用户ID
     * @param collectId 歌单数据
     * @param songIds   歌曲列表
     * @param flag      删除还是添加 true add, false remove
     */
    void addMusicToCollect(Long userID, Long collectId, List<Long> songIds, boolean flag);
    
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
     * 获取tag
     *
     * @param target tag类型 0流派 1歌曲 2歌单
     * @param ids    歌单，音乐，专辑
     * @return tag列表
     */
    Map<Long, List<TbTagPojo>> getLabel(Byte target, Collection<Long> ids);
    
    /**
     * 获取tag Map
     *
     * @param target tag类型 0流派 1歌曲 2歌单
     * @param ids    歌单，音乐，专辑
     * @return tag列表 Long -> id
     */
    Map<Long, List<TbTagPojo>> getLabel(Byte target, Set<Long> ids);
    
    /**
     * 获取tag音乐
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelMusicTag(Collection<Long> ids) {
        return getLabel(TargetTagConstant.TARGET_MUSIC_TAG, ids);
    }
    
    /**
     * 获取tag音乐
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelMusicTag(Set<Long> ids) {
        return getLabel(TargetTagConstant.TARGET_MUSIC_TAG, ids);
    }
    
    /**
     * 获取tag音乐
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelMusicTag(Long ids) {
        return getLabel(TargetTagConstant.TARGET_MUSIC_TAG, Collections.singletonList(ids));
    }
    
    /**
     * 获取音乐流派
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelMusicGenre(Long ids) {
        return getLabel(TargetTagConstant.TARGET_GENRE, Collections.singletonList(ids));
    }
    
    /**
     * 获取音乐流派
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelMusicGenre(Collection<Long> ids) {
        return getLabel(TargetTagConstant.TARGET_GENRE, ids);
    }
    
    /**
     * 获取音乐流派
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelMusicGenre(Set<Long> ids) {
        return getLabel(TargetTagConstant.TARGET_GENRE, ids);
    }
    
    /**
     * 获取tag专辑
     *
     * @param ids 专辑ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelAlbumGenre(Collection<Long> ids) {
        return getLabel(TargetTagConstant.TARGET_ALBUM_GENRE, ids);
    }
    
    /**
     * 获取tag专辑
     *
     * @param id 专辑ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelAlbumGenre(Long id) {
        return getLabel(TargetTagConstant.TARGET_ALBUM_GENRE, Collections.singletonList(id));
    }
    
    /**
     * 获取tag歌单
     *
     * @param ids 歌单ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelCollectTag(Collection<Long> ids) {
        return getLabel(TargetTagConstant.TARGET_COLLECT_TAG, ids);
    }
    
    /**
     * 获取tag歌单
     *
     * @param ids 歌单ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getLabelCollectTag(Long ids) {
        return getLabel(TargetTagConstant.TARGET_COLLECT_TAG, Collections.singletonList(ids));
    }
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param labels 标签名
     */
    void addLabel(Byte target, Long id, List<String> labels);
    
    /**
     * 对歌单tag，音乐添加tag Name， 或者指定音乐流派
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param label  标签名
     */
    default void addLabel(Byte target, Long id, String label) {
        addLabel(target, id, Objects.isNull(label) ? Collections.emptyList() : Collections.singletonList(label));
    }
    
    /**
     * 批量添加tag
     *
     * @param target   指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id       歌单或歌曲前ID
     * @param labelIds 标签ID
     */
    void addLabel(Byte target, Long id, Set<Long> labelIds);
    
    
    /**
     * 对歌单tag，音乐添加tag ID， 或者指定音乐流派
     *
     * @param target  指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id      歌单或歌曲前ID
     * @param labelId 标签ID
     */
    default void addLabel(Byte target, Long id, Long labelId) {
        addLabel(target, id, Set.of(labelId));
    }
    
    /**
     * 删除全部tag
     *
     * @param id 音乐，歌单， 专辑
     */
    void removeLabelAll(Long id);
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target       指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id           歌单或歌曲前ID
     * @param labelBatchId 需要删除的label ID
     */
    void removeLabelById(Byte target, Long id, Collection<Long> labelBatchId);
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target         指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id             歌单或歌曲前ID
     * @param labelBatchName 需要删除的label ID
     */
    void removeLabelByName(Byte target, Long id, Collection<Long> labelBatchName);
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target  指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id      歌单或歌曲前ID
     * @param labelId 需要删除的label ID
     */
    default void removeLabelById(Byte target, Long id, Long labelId) {
        removeLabelById(target, id, Collections.singletonList(labelId));
    }
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target    指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id        歌单或歌曲前ID
     * @param labelName 需要删除的label ID
     */
    default void removeLabelByName(Byte target, Long id, Long labelName) {
        removeLabelByName(target, id, Collections.singletonList(labelName));
    }
    
    /**
     * 删除歌单的tag
     *
     * @param id        歌单或歌曲前ID
     * @param labelName 需要删除的label ID
     */
    default void removeCollectLabelByName(Long id, Long labelName) {
        removeLabelByName(TargetTagConstant.TARGET_COLLECT_TAG, id, Collections.singletonList(labelName));
    }
    
    
    default void addCollectLabel(Long id, Long labelId) {
        this.addLabel(TargetTagConstant.TARGET_COLLECT_TAG, id, labelId);
    }
    
    default void addCollectLabel(Long id, String label) {
        this.addLabel(TargetTagConstant.TARGET_COLLECT_TAG, id, label);
    }
    
    default void addCollectLabel(Long id, List<String> label) {
        this.addLabel(TargetTagConstant.TARGET_COLLECT_TAG, id, label);
    }
    
    default void addMusicLabelTag(Long id, String label) {
        this.addLabel(TargetTagConstant.TARGET_MUSIC_TAG, id, label);
    }
    
    default void addMusicLabelTag(Long id, List<String> labels) {
        this.addLabel(TargetTagConstant.TARGET_MUSIC_TAG, id, labels);
    }
    
    default void addAlbumGenreLabel(Long id, List<String> labels) {
        this.addLabel(TargetTagConstant.TARGET_ALBUM_GENRE, id, labels);
    }
    
    default void addAlbumGenreLabel(Long id, String label) {
        this.addLabel(TargetTagConstant.TARGET_ALBUM_GENRE, id, Objects.isNull(label) ? Collections.emptyList() : Collections.singletonList(label));
    }
    
    default void addMusicLabelTag(Long id, Long labelId) {
        this.addLabel(TargetTagConstant.TARGET_MUSIC_TAG, id, labelId);
    }
    
    default void addMusicGenreLabel(Long id, Long labelId) {
        this.addLabel(TargetTagConstant.TARGET_GENRE, id, labelId);
    }
    
    default void addMusicGenreLabel(Long id, String label) {
        this.addLabel(TargetTagConstant.TARGET_GENRE, id, label);
    }
    
    default void addMusicGenreLabel(Long id, List<String> labels) {
        this.addLabel(TargetTagConstant.TARGET_GENRE, id, labels);
    }
    
    /**
     * 添加喜欢歌单
     *
     * @param userId          用户
     * @param id              音乐ID
     * @param isAddAndDelLike true添加 false删除
     */
    void collectLike(Long userId, Long id, Boolean isAddAndDelLike);
    
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
     * 查询封面地址
     *
     * @param id   封面ID
     * @param type 关联ID类型
     * @return 封面地址
     */
    default String getPicPath(Long id, Byte type) {
        Map<Long, String> picUrl = getPicPath(Collections.singletonList(id), type);
        return MapUtil.get(picUrl, id, String.class);
    }
    
    /**
     * 获取歌音乐封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getMusicPicPath(Long ids) {
        return this.getPicPath(ids, PicTypeConstant.MUSIC);
    }
    
    /**
     * 获取歌单封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getCollectPicPath(Long ids) {
        return this.getPicPath(ids, PicTypeConstant.PLAYLIST);
    }
    
    /**
     * 获取专辑封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getAlbumPicPath(Long ids) {
        return this.getPicPath(ids, PicTypeConstant.ALBUM);
    }
    
    /**
     * 获取歌手封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getArtistPicPath(Long ids) {
        return this.getPicPath(ids, PicTypeConstant.ARTIST);
    }
    
    /**
     * 获取用户头像封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getUserAvatarPicPath(Long ids) {
        return this.getPicPath(ids, PicTypeConstant.USER_AVATAR);
    }
    
    /**
     * 获取用户背景封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getUserBackgroundPicPath(Long ids) {
        return this.getPicPath(ids, PicTypeConstant.USER_BACKGROUND);
    }
    
    /**
     * 获取歌音乐封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getMusicPicPath(Collection<Long> ids) {
        return this.getPicPath(ids, PicTypeConstant.MUSIC);
    }
    
    /**
     * 获取歌单封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getCollectPicPath(Collection<Long> ids) {
        return this.getPicPath(ids, PicTypeConstant.PLAYLIST);
    }
    
    /**
     * 获取专辑封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getAlbumPicPath(Collection<Long> ids) {
        return this.getPicPath(ids, PicTypeConstant.ALBUM);
    }
    
    /**
     * 获取歌手封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getArtistPicPath(Collection<Long> ids) {
        return this.getPicPath(ids, PicTypeConstant.ARTIST);
    }
    
    /**
     * 获取封面地址
     *
     * @param ids  封面关联ID
     * @param type 关联ID类型
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    Map<Long, String> getPicPath(Collection<Long> ids, Byte type);
    
    
    /**
     * 获取用户头像封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getUserAvatarPicPath(Collection<Long> ids) {
        return this.getPicPath(ids, PicTypeConstant.USER_AVATAR);
    }
    
    /**
     * 获取用户背景封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getUserBackgroundPicPath(Collection<Long> ids) {
        return this.getPicPath(ids, PicTypeConstant.USER_BACKGROUND);
    }
    
    /**
     * 保存封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param pojo 封面数据
     */
    void saveOrUpdatePic(Long id, Byte type, TbPicPojo pojo);
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateAlbumPic(Long id, TbPicPojo pojo) {
        this.saveOrUpdatePic(id, PicTypeConstant.ALBUM, pojo);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateArtistPic(Long id, TbPicPojo pojo) {
        this.saveOrUpdatePic(id, PicTypeConstant.ARTIST, pojo);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateMusicPic(Long id, TbPicPojo pojo) {
        this.saveOrUpdatePic(id, PicTypeConstant.MUSIC, pojo);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateAvatarPic(Long id, TbPicPojo pojo) {
        this.saveOrUpdatePic(id, PicTypeConstant.USER_AVATAR, pojo);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateBackgroundPic(Long id, TbPicPojo pojo) {
        this.saveOrUpdatePic(id, PicTypeConstant.USER_BACKGROUND, pojo);
    }
    
    /**
     * 用户继承类实现, 从地址或base64保存数据到数据库
     *
     * @param id   封面ID
     * @param type 封面类型
     * @param url  地址或base64
     */
    default void saveOrUpdatePicUrl(Long id, Byte type, String url) {
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateMusicPicUrl(Long id, String url) {
        this.saveOrUpdatePicUrl(id, PicTypeConstant.MUSIC, url);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateAlbumPicUrl(Long id, String url) {
        this.saveOrUpdatePicUrl(id, PicTypeConstant.ALBUM, url);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateArtistPicUrl(Long id, String url) {
        this.saveOrUpdatePicUrl(id, PicTypeConstant.ARTIST, url);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateCollectPicUrl(Long id, String url) {
        this.saveOrUpdatePicUrl(id, PicTypeConstant.PLAYLIST, url);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateUserAvatarUrl(Long id, String url) {
        this.saveOrUpdatePicUrl(id, PicTypeConstant.USER_AVATAR, url);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateUserBackgroundUrl(Long id, String url) {
        this.saveOrUpdatePicUrl(id, PicTypeConstant.USER_BACKGROUND, url);
    }
    
    
    default Map<Long, String> getPicUrl(Collection<Long> middleIds, Byte type) {
        return Collections.emptyMap();
    }
    
    default String getPicUrl(Long id, Byte type) {
        return this.getPicUrl(Collections.singletonList(id), type).get(id);
    }
    
    /**
     * 获取歌音乐封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getMusicPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.MUSIC);
    }
    
    /**
     * 获取歌单封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getCollectPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.PLAYLIST);
    }
    
    /**
     * 获取专辑封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getAlbumPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.ALBUM);
    }
    
    /**
     * 获取歌手封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getArtistPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.ARTIST);
    }
    
    /**
     * 获取用户头像封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getUserAvatarPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.USER_AVATAR);
    }
    
    /**
     * 获取用户背景封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getUserBackgroundPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.USER_BACKGROUND);
    }
    
    /**
     * 获取歌音乐封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getMusicPicUrl(Collection<Long> ids) {
        return this.getPicUrl(ids, PicTypeConstant.MUSIC);
    }
    
    /**
     * 获取歌单封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getCollectPicUrl(Collection<Long> ids) {
        return this.getPicUrl(ids, PicTypeConstant.PLAYLIST);
    }
    
    /**
     * 获取专辑封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getAlbumPicUrl(Collection<Long> ids) {
        return this.getPicUrl(ids, PicTypeConstant.ALBUM);
    }
    
    /**
     * 获取歌手封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getArtistPicUrl(Collection<Long> ids) {
        return this.getPicUrl(ids, PicTypeConstant.ARTIST);
    }
    
    
    /**
     * 删除封面数据, 包括文件和数据库
     */
    void removePic(Long id);
    
    /**
     * 批量根据ID删除封面数据
     */
    void removePicIds(List<Long> picIds);
}

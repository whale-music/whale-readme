package org.core.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.core.common.constant.PicTypeConstant;
import org.core.common.constant.TargetTagConstant;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.CollectConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;

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
    List<TbMusicUrlPojo> getMusicPaths(Collection<Long> musicId);
    
    /**
     * 查询数据歌曲下载地址
     * key music value url
     *
     * @param musicId 音乐ID
     */
    Map<Long, List<TbMusicUrlPojo>> getMusicMapUrl(Collection<Long> musicId);
    
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
    CollectConvert createPlayList(Long userId, String name, short type);
    
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
    List<CollectConvert> getUserPlayList(Long uid, Collection<Short> type);
    
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
     * 对歌单tag，音乐添加tag Name， 或者指定音乐流派
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param label  标签名
     */
    void addLabel(Short target, Long id, String label);
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param labels 标签名
     */
    void addLabel(Short target, Long id, List<String> labels);
    
    /**
     * 对歌单tag，音乐添加tag ID， 或者指定音乐流派
     *
     * @param target  指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id      歌单或歌曲前ID
     * @param labelId 标签ID
     */
    void addLabel(Short target, Long id, Long labelId);
    
    /**
     * 批量添加tag
     *
     * @param target   指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id       歌单或歌曲前ID
     * @param labelIds 标签ID
     */
    void addLabel(Short target, Long id, Set<Long> labelIds);
    
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
    void removeLabelById(Short target, Long id, Collection<Long> labelBatchId);
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target         指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id             歌单或歌曲前ID
     * @param labelBatchName 需要删除的label ID
     */
    void removeLabelByName(Short target, Long id, Collection<Long> labelBatchName);
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target  指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id      歌单或歌曲前ID
     * @param labelId 需要删除的label ID
     */
    default void removeLabelById(Short target, Long id, Long labelId) {
        removeLabelById(target, id, Collections.singletonList(labelId));
    }
    
    /**
     * 删除歌单或音乐中的tag
     *
     * @param target    指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id        歌单或歌曲前ID
     * @param labelName 需要删除的label ID
     */
    default void removeLabelByName(Short target, Long id, Long labelName) {
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
    
    default void addMusicLabel(Long id, String label) {
        this.addLabel(TargetTagConstant.TARGET_MUSIC_TAG, id, label);
    }
    
    default void addMusicLabel(Long id, List<String> labels) {
        this.addLabel(TargetTagConstant.TARGET_MUSIC_TAG, id, labels);
    }
    
    default void addAlbumLabel(Long id, List<String> labels) {
        this.addLabel(TargetTagConstant.TARGET_ALBUM_GENRE, id, labels);
    }
    
    default void addMusicLabel(Long id, Long labelId) {
        this.addLabel(TargetTagConstant.TARGET_MUSIC_TAG, id, labelId);
    }
    
    default void addMusicGenreLabel(Long id, Long labelId) {
        this.addLabel(TargetTagConstant.TARGET_GENRE, id, labelId);
    }
    
    default void addMusicGenreLabel(Long id, String label) {
        this.addLabel(TargetTagConstant.TARGET_GENRE, id, label);
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
     * @param id 封面ID
     * @return 封面地址
     */
    String getPicUrl(Long id);
    
    Map<Long, String> getPicUrl(Collection<Long> ids);
    
    /**
     * 保存封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param pojo 封面数据
     */
    void saveOrUpdatePic(Long id, Short type, TbPicPojo pojo);
    
    default void saveOrUpdatePic(Long id, Short type, String url) {
        TbPicPojo pojo = new TbPicPojo();
        pojo.setUrl(url);
        this.saveOrUpdatePic(id, type, pojo);
    }
    
    default void saveOrUpdateMusicPic(Long id, String url) {
        TbPicPojo pojo = new TbPicPojo();
        pojo.setUrl(url);
        this.saveOrUpdatePic(id, PicTypeConstant.MUSIC, pojo);
    }
    
    default void saveOrUpdateAlbumPic(Long id, String url) {
        TbPicPojo pojo = new TbPicPojo();
        pojo.setUrl(url);
        this.saveOrUpdatePic(id, PicTypeConstant.ALBUM, pojo);
    }
    
    default void saveOrUpdateArtistPic(Long id, String url) {
        TbPicPojo pojo = new TbPicPojo();
        pojo.setUrl(url);
        this.saveOrUpdatePic(id, PicTypeConstant.ARTIST, pojo);
    }
    
    default void saveOrUpdateCollectPic(Long id, String url) {
        TbPicPojo pojo = new TbPicPojo();
        pojo.setUrl(url);
        this.saveOrUpdatePic(id, PicTypeConstant.COLLECT, pojo);
    }
    
    default void saveOrUpdateUserPic(Long id, String url) {
        TbPicPojo pojo = new TbPicPojo();
        pojo.setUrl(url);
        this.saveOrUpdatePic(id, PicTypeConstant.USER, pojo);
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

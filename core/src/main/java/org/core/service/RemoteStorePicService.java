package org.core.service;

import cn.hutool.core.map.MapUtil;
import org.core.common.constant.PicTypeConstant;
import org.core.mybatis.pojo.TbPicPojo;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public interface RemoteStorePicService {
    
    
    Map<Long, String> getPicUrl(Collection<Long> middleIds, Byte type);
    
    default String getPicUrl(Long id, Byte type) {
        return this.getPicUrl(Collections.singletonList(id), type).get(id);
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
     * 获取mv封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default String getMvPicUrl(Long ids) {
        return this.getPicUrl(ids, PicTypeConstant.MV);
    }
    
    /**
     * 获取歌单封面地址
     *
     * @param ids 封面关联ID
     * @return 封面地址map long -> 关联ID, String -> 封面地址
     */
    default Map<Long, String> getMvPicUrl(Collection<Long> ids) {
        return this.getPicUrl(ids, PicTypeConstant.MV);
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
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateMvPic(Long id, TbPicPojo pojo) {
        this.saveOrUpdatePic(id, PicTypeConstant.MV, pojo);
    }
    
    
    /**
     * 用户继承类实现, 从地址或base64保存数据到数据库
     *
     * @param id   封面ID
     * @param type 封面类型
     * @param url  地址或base64
     */
    void saveOrUpdatePicUrl(Long id, Byte type, String url);
    
    
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
    
    /**
     * 删除图片
     *
     * @param ids 封面Id
     */
    void removePicById(List<Long> ids);
    
    default void removePicById(Long ids) {
        removePicById(Collections.singletonList(ids));
    }
    
    /**
     * 删除封面数据, 包括文件和数据库
     */
    default void removePicMiddle(Long id, byte type) {
        removePicMiddleIds(Collections.singletonList(id), Collections.singletonList(type));
    }
    
    /**
     * 批量根据ID删除封面数据
     *
     * @param middleIds 封面
     * @param types     封面类型
     */
    void removePicMiddleIds(List<Long> middleIds, Collection<Byte> types);
    
    /**
     * 批量删除封面文件
     *
     * @param middleIds 封面
     * @param types     封面类型
     * @param consumer  删除文件
     */
    void removePicMiddleFile(Collection<Long> middleIds, Collection<Byte> types, Consumer<List<String>> consumer);
    
    /**
     * 保存或更新封面
     *
     * @param id   添加封面关联ID,
     * @param type 添加ID类型 歌曲，专辑，歌单，歌手
     * @param file 封面数据
     */
    void saveOrUpdatePicFile(Long id, Byte type, File file);
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateAlbumPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.ALBUM, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateArtistPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.ARTIST, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateMusicPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.MUSIC, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateAvatarPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.USER_AVATAR, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateBackgroundPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.USER_BACKGROUND, file);
    }
    
    @Transactional(rollbackFor = Exception.class)
    default void saveOrUpdateMvPicFile(Long id, File file) {
        this.saveOrUpdatePicFile(id, PicTypeConstant.MV, file);
    }
    
    
}

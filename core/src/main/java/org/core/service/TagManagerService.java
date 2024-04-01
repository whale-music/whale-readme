package org.core.service;

import org.core.common.constant.TargetTagConstant;
import org.core.model.TagMiddleTypeModel;
import org.core.mybatis.pojo.TbTagPojo;

import java.util.*;

public interface TagManagerService {
    /**
     * 根据ID获取tag
     *
     * @param type tag类型 0流派 1歌曲 2歌单
     * @param ids  歌单，音乐，专辑
     * @return tag列表
     */
    Map<Long, List<TbTagPojo>> getTag(Byte type, Collection<Long> ids);
    
    /**
     * 根据tag名获取 Tag
     *
     * @param type tag类型
     * @param tags tag名
     * @return tag列表
     */
    Map<Long, List<TbTagPojo>> getTag(Byte type, Iterator<String> tags);
    
    /**
     * 获取tag音乐
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMusicTag(Collection<Long> ids) {
        return getTag(TargetTagConstant.TARGET_MUSIC_TAG, ids);
    }
    
    /**
     * 获取tag音乐
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMusicTag(Set<Long> ids) {
        return getTag(TargetTagConstant.TARGET_MUSIC_TAG, ids);
    }
    
    /**
     * 获取tag音乐
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMusicTag(Long ids) {
        return getTag(TargetTagConstant.TARGET_MUSIC_TAG, Collections.singletonList(ids));
    }
    
    /**
     * 获取音乐流派
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMusicGenre(Long ids) {
        return getTag(TargetTagConstant.TARGET_MUSIC_GENRE, Collections.singletonList(ids));
    }
    
    /**
     * 获取音乐流派
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMusicGenre(Collection<Long> ids) {
        return getTag(TargetTagConstant.TARGET_MUSIC_GENRE, ids);
    }
    
    /**
     * 获取音乐流派
     *
     * @param ids 音乐ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMusicGenre(Set<Long> ids) {
        return getTag(TargetTagConstant.TARGET_MUSIC_GENRE, ids);
    }
    
    /**
     * 获取tag专辑
     *
     * @param ids 专辑ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getAlbumGenre(Collection<Long> ids) {
        return getTag(TargetTagConstant.TARGET_ALBUM_GENRE, ids);
    }
    
    /**
     * 获取tag专辑
     *
     * @param id 专辑ID
     * @return tag 列表
     */
    default List<TbTagPojo> getAlbumGenre(Long id) {
        return getTag(TargetTagConstant.TARGET_ALBUM_GENRE, Collections.singletonList(id)).get(id);
    }
    
    /**
     * 获取tag歌单
     *
     * @param ids 歌单ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getCollectTag(Collection<Long> ids) {
        return getTag(TargetTagConstant.TARGET_COLLECT_TAG, ids);
    }
    
    /**
     * 获取tag歌单
     *
     * @param ids 歌单ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getCollectTag(Long ids) {
        return getTag(TargetTagConstant.TARGET_COLLECT_TAG, Collections.singletonList(ids));
    }
    
    /**
     * 获取tag mv
     *
     * @param ids mv ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMvTag(List<Long> ids) {
        return getTag(TargetTagConstant.TARGET_MV_TAG, ids);
    }
    
    /**
     * 获取tag mv
     *
     * @param id mv ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMvTag(Long id) {
        return getTag(TargetTagConstant.TARGET_MV_TAG, Collections.singletonList(id));
    }
    
    /**
     * 获取tag MV
     *
     * @param tags MV ID
     * @return tag 列表
     */
    default Map<Long, List<TbTagPojo>> getMvTag(Iterator<String> tags) {
        return getTag(TargetTagConstant.TARGET_MV_TAG, tags);
    }
    
    /**
     * 批量添加tag
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param labels 标签名
     */
    void addTag(Byte target, Long id, List<String> labels);
    
    /**
     * 对歌单tag，音乐添加tag Name， 或者指定音乐流派
     *
     * @param target 指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id     歌单或歌曲前ID
     * @param label  标签名
     */
    default void addTag(Byte target, Long id, String label) {
        addTag(target, id, Objects.isNull(label) ? Collections.emptyList() : Collections.singletonList(label));
    }
    
    /**
     * 批量添加tag
     *
     * @param target   指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id       歌单或歌曲前ID
     * @param labelIds 标签ID
     */
    void addTag(Byte target, Long id, Set<Long> labelIds);
    
    
    /**
     * 对歌单tag，音乐添加tag ID， 或者指定音乐流派
     *
     * @param target  指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id      歌单或歌曲前ID
     * @param labelId 标签ID
     */
    default void addTag(Byte target, Long id, Long labelId) {
        addTag(target, id, Set.of(labelId));
    }
    
    default void addCollectLabel(Long id, Long labelId) {
        this.addTag(TargetTagConstant.TARGET_COLLECT_TAG, id, labelId);
    }
    
    default void addCollectLabel(Long id, String label) {
        this.addTag(TargetTagConstant.TARGET_COLLECT_TAG, id, label);
    }
    
    default void addCollectLabel(Long id, List<String> label) {
        this.addTag(TargetTagConstant.TARGET_COLLECT_TAG, id, label);
    }
    
    default void addMusicLabelTag(Long id, String label) {
        this.addTag(TargetTagConstant.TARGET_MUSIC_TAG, id, label);
    }
    
    default void addMusicLabelTag(Long id, List<String> labels) {
        this.addTag(TargetTagConstant.TARGET_MUSIC_TAG, id, labels);
    }
    
    default void addAlbumGenreLabel(Long id, List<String> labels) {
        this.addTag(TargetTagConstant.TARGET_ALBUM_GENRE, id, labels);
    }
    
    default void addAlbumGenreLabel(Long id, String label) {
        this.addTag(TargetTagConstant.TARGET_ALBUM_GENRE, id, Objects.isNull(label) ? Collections.emptyList() : Collections.singletonList(label));
    }
    
    default void addMusicLabelTag(Long id, Long labelId) {
        this.addTag(TargetTagConstant.TARGET_MUSIC_TAG, id, labelId);
    }
    
    default void addMusicGenreLabel(Long id, Long labelId) {
        this.addTag(TargetTagConstant.TARGET_MUSIC_GENRE, id, labelId);
    }
    
    default void addMusicGenreLabel(Long id, String label) {
        this.addTag(TargetTagConstant.TARGET_MUSIC_GENRE, id, label);
    }
    
    default void addMusicGenreLabel(Long id, List<String> labels) {
        this.addTag(TargetTagConstant.TARGET_MUSIC_GENRE, id, labels);
    }
    
    default void addMvGenreLabel(Long id, List<String> labels) {
        this.addTag(TargetTagConstant.TARGET_MV_TAG, id, labels);
    }
    
    /**
     * 根据类型ID, 删除tag
     *
     * @param ids  tag id
     * @param type tag type
     */
    default void removeLabel(Collection<Long> ids, byte type) {
        this.removeLabel(ids.parallelStream().map(aLong -> new TagMiddleTypeModel(aLong, type)).toList());
    }
    
    /**
     * 根据类型ID, 删除tag列表
     *
     * @param list tag 数据
     */
    void removeLabel(Collection<TagMiddleTypeModel> list);
    
    /**
     * 移除专辑tag
     *
     * @param ids album id
     */
    default void removeLabelAlbum(List<Long> ids) {
        removeLabel(ids, TargetTagConstant.TARGET_ALBUM_GENRE);
    }
    
    /**
     * 移除MV tag
     *
     * @param ids mv ids
     */
    default void removeLabelMv(List<Long> ids) {
        removeLabel(ids, TargetTagConstant.TARGET_MV_TAG);
    }
    
    
    /**
     * 移除MV tag
     *
     * @param id mv id
     */
    default void removeLabelMv(Long id) {
        removeLabel(Collections.singletonList(id), TargetTagConstant.TARGET_MV_TAG);
    }
    
    /**
     * 移除MV tag
     *
     * @param id 歌单 id
     */
    default void removeLabelPlaylist(Long id) {
        removeLabel(Collections.singletonList(id), TargetTagConstant.TARGET_COLLECT_TAG);
    }
    
    /**
     * 移除MV tag
     *
     * @param ids 歌单 id
     */
    default void removeLabelPlaylist(Collection<Long> ids) {
        removeLabel(ids, TargetTagConstant.TARGET_COLLECT_TAG);
    }
    
    /**
     * 删除歌单或音乐中的tag, 根据ID
     *
     * @param target       指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id           歌单或歌曲前ID
     * @param labelBatchId 需要删除的label ID
     */
    void removeLabelById(Byte target, Long id, Collection<Long> labelBatchId);
    
    /**
     * 删除歌单或音乐中的tag, 根据tag name
     *
     * @param target         指定歌单tag，或者音乐tag，音乐流派 0流派 1歌曲 2歌单
     * @param id             歌单或歌曲前ID
     * @param labelBatchName 需要删除的label ID
     */
    void removeLabelByName(Byte target, Long id, Collection<String> labelBatchName);
    
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
    default void removeLabelByName(Byte target, Long id, String labelName) {
        removeLabelByName(target, id, Collections.singletonList(labelName));
    }
    
    /**
     * 删除歌单的tag
     *
     * @param id        歌单或歌曲前ID
     * @param labelName 需要删除的label ID
     */
    default void removeCollectLabelByName(Long id, String labelName) {
        removeLabelByName(TargetTagConstant.TARGET_COLLECT_TAG, id, Collections.singletonList(labelName));
    }
}

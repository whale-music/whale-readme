package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.common.constant.HistoryConstant;
import org.core.mybatis.pojo.TbHistoryPojo;

import java.util.List;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbHistoryService extends IService<TbHistoryPojo> {
    
    /**
     * 获取播放最多音乐ID
     *
     * @param userId 用户ID
     * @param type   播放历史类型, 如果为null则全部查询
     * @param offset 分页参数 当前页码
     * @param size   分页参数 每页数量
     * @return 音乐ID
     */
    List<TbHistoryPojo> getFrequent(Long userId, Byte type, Long offset, Long size);
    
    default List<TbHistoryPojo> getFrequentMusic(Long userId, Long offset, Long size) {
        return this.getFrequent(userId, HistoryConstant.MUSIC, offset, size);
    }
    
    default List<TbHistoryPojo> getFrequentAlbum(Long userId, Long offset, Long size) {
        return this.getFrequent(userId, HistoryConstant.ALBUM, offset, size);
    }
    
    default List<TbHistoryPojo> getFrequentArtist(Long userId, Long offset, Long size) {
        return this.getFrequent(userId, HistoryConstant.ARTIST, offset, size);
    }
    
    default List<TbHistoryPojo> getFrequentPlayList(Long userId, Long offset, Long size) {
        return this.getFrequent(userId, HistoryConstant.PLAYLIST, offset, size);
    }
    
    default List<TbHistoryPojo> getFrequentMv(Long userId, Long offset, Long size) {
        return this.getFrequent(userId, HistoryConstant.MV, offset, size);
    }
    
    /**
     * 获取最近播放音乐
     *
     * @param userId 用户ID
     * @param type   播放历史类型, 如果为null则全部查询
     * @param offset 分页参数 当前页码
     * @param size   分页参数 每页数量
     * @return 音乐ID
     */
    List<TbHistoryPojo> getRecent(Long userId, Byte type, Long offset, Long size);
    
    
    default List<TbHistoryPojo> getRecentMusic(Long userId, Long offset, Long size) {
        return this.getRecent(userId, HistoryConstant.MUSIC, offset, size);
    }
    
    default List<TbHistoryPojo> getRecentAlbum(Long userId, Long offset, Long size) {
        return this.getRecent(userId, HistoryConstant.ALBUM, offset, size);
    }
    
    default List<TbHistoryPojo> getRecentArtist(Long userId, Long offset, Long size) {
        return this.getRecent(userId, HistoryConstant.ARTIST, offset, size);
    }
    
    default List<TbHistoryPojo> getRecentPlayList(Long userId, Long offset, Long size) {
        return this.getRecent(userId, HistoryConstant.PLAYLIST, offset, size);
    }
    
    default List<TbHistoryPojo> getRecentMv(Long userId, Long offset, Long size) {
        return this.getRecent(userId, HistoryConstant.MV, offset, size);
    }
}

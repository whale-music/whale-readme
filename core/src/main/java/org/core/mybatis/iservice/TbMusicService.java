package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbMusicPojo;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 所有音乐列表 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbMusicService extends IService<TbMusicPojo> {
    
    
    /**
     * 通过音乐名获取音乐
     * 获取时必须保证音乐名和数据库中音乐一致，并且只有一条数据
     *
     * @param name  音乐名
     * @param alias 音乐别名, 可为空
     * @return 音乐数据
     */
    TbMusicPojo getMusicByName(String name, String alias);
    
    /**
     * 获取音乐列表
     *
     * @param ids music id list
     * @return 音乐列表
     */
    Map<Long, TbMusicPojo> getMusicList(List<Long> ids);
    
    /**
     * 根据专辑ID获取音乐ID
     *
     * @param albumIds 专辑ID
     * @return 音乐ID
     */
    List<Long> getMusicIdsByAlbumIds(List<Long> albumIds);
    
    /**
     * 获取音乐ID
     *
     * @param albumIds 专辑ID
     * @return key 专辑ID value 音乐ID list
     */
    Map<Long, List<Long>> getAlbumMusicMapByAlbumIds(Collection<Long> albumIds);
    
    /**
     * 获取专辑ID
     *
     * @param musicIds 音乐ID
     * @return 专辑ID
     */
    List<Long> getAlbumIdsByMusicIds(List<Long> musicIds);
}

package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbCollectMusicPojo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 歌单和音乐的中间表，用于记录歌单中的每一个音乐 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbCollectMusicService extends IService<TbCollectMusicPojo> {
    
    /**
     * 查询歌单ID
     *
     * @param collectIds 歌单ID
     * @return 歌单音乐列表
     */
    List<TbCollectMusicPojo> getCollectIds(Collection<Long> collectIds);
    
    /**
     * 查询音乐ID
     *
     * @param musicIds 音乐ID
     * @return 歌单音乐列表
     */
    List<TbCollectMusicPojo> getMusicIds(Collection<Long> musicIds);
    
}

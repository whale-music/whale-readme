package org.core.mybatis.iservice;

import com.baomidou.mybatisplus.extension.service.IService;
import org.core.mybatis.pojo.TbMusicArtistPojo;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 音乐与歌手中间表 服务类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
public interface TbMusicArtistService extends IService<TbMusicArtistPojo> {
    
    /**
     * 获取音乐ID
     *
     * @param artistIds 歌手Id
     * @return 音乐ID
     */
    List<Long> getMusicIdsByArtistIds(List<Long> artistIds);
    
    /**
     * 根据音乐关联歌手
     *
     * @param musicIds 音乐id
     * @return {@link List<TbMusicArtistPojo>}
     */
    List<TbMusicArtistPojo> getMusicArtistByMusicIds(Collection<Long> musicIds);
}

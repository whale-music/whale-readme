package org.core.mybatis.iservice.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbMusicArtistService;
import org.core.mybatis.mapper.TbMusicArtistMapper;
import org.core.mybatis.pojo.TbMusicArtistPojo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 音乐与歌手中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbMusicArtistServiceImpl extends ServiceImpl<TbMusicArtistMapper, TbMusicArtistPojo> implements TbMusicArtistService {
    
    /**
     * 获取音乐ID
     *
     * @param artistIds 歌手Id
     * @return 音乐ID
     */
    @Override
    public List<Long> getMusicIdsByArtistIds(List<Long> artistIds) {
        return this.listObjs(Wrappers.<TbMusicArtistPojo>lambdaQuery()
                                     .select(TbMusicArtistPojo::getMusicId)
                                     .in(TbMusicArtistPojo::getArtistId, artistIds));
    }
    
    
    /**
     * 根据音乐关联歌手
     *
     * @param musicIds 音乐id
     * @return {@link List<TbMusicArtistPojo>}
     */
    @Override
    public List<TbMusicArtistPojo> getMusicArtistByMusicIds(Collection<Long> musicIds) {
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        return this.list(Wrappers.<TbMusicArtistPojo>lambdaQuery().in(TbMusicArtistPojo::getMusicId, musicIds));
    }
}

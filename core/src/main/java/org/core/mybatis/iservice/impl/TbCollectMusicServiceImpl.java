package org.core.mybatis.iservice.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbCollectMusicService;
import org.core.mybatis.mapper.TbCollectMusicMapper;
import org.core.mybatis.pojo.TbCollectMusicPojo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * 歌单和音乐的中间表，用于记录歌单中的每一个音乐 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbCollectMusicServiceImpl extends ServiceImpl<TbCollectMusicMapper, TbCollectMusicPojo> implements TbCollectMusicService {
    
    
    /**
     * 查询歌单ID
     *
     * @param collectIds 歌单ID
     * @return 歌单音乐列表
     */
    @Override
    public List<TbCollectMusicPojo> getCollectIds(Collection<Long> collectIds) {
        if (CollUtil.isEmpty(collectIds)) {
            return Collections.emptyList();
        }
        return list(Wrappers.<TbCollectMusicPojo>lambdaQuery().in(TbCollectMusicPojo::getCollectId, collectIds));
    }
    
    /**
     * 查询音乐ID
     *
     * @param musicIds 音乐ID
     * @return 歌单音乐列表
     */
    @Override
    public List<TbCollectMusicPojo> getMusicIds(Collection<Long> musicIds) {
        return list(Wrappers.<TbCollectMusicPojo>lambdaQuery().in(TbCollectMusicPojo::getMusicId, musicIds));
    }
}

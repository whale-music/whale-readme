package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbCollectMusicService;
import org.core.mybatis.mapper.TbCollectMusicMapper;
import org.core.mybatis.pojo.TbCollectMusicPojo;
import org.springframework.stereotype.Service;

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

}

package org.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mapper.TbCollectMusicMapper;
import org.core.pojo.TbCollectMusicPojo;
import org.core.service.TbCollectMusicService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌单和音乐的中间表，用于记录歌单中的每一个音乐 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Service
public class TbCollectMusicServiceImpl extends ServiceImpl<TbCollectMusicMapper, TbCollectMusicPojo> implements TbCollectMusicService {

}

package org.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mapper.TbMusicSingerMapper;
import org.core.pojo.TbMusicSingerPojo;
import org.core.service.TbMusicSingerService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌曲和歌手的中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-10-28
 */
@Service
public class TbMusicSingerServiceImpl extends ServiceImpl<TbMusicSingerMapper, TbMusicSingerPojo> implements TbMusicSingerService {

}

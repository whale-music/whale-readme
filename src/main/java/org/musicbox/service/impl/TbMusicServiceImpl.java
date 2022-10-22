package org.musicbox.service.impl;

import org.musicbox.pojo.TbMusicPojo;
import org.musicbox.mapper.TbMusicMapper;
import org.musicbox.service.TbMusicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 所有音乐列表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Service
public class TbMusicServiceImpl extends ServiceImpl<TbMusicMapper, TbMusicPojo> implements TbMusicService {

}

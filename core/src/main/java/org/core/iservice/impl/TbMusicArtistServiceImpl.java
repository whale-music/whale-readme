package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbMusicArtistService;
import org.core.mapper.TbMusicArtistMapper;
import org.core.pojo.TbMusicArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐与歌手中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-05-11
 */
@Service
public class TbMusicArtistServiceImpl extends ServiceImpl<TbMusicArtistMapper, TbMusicArtistPojo> implements TbMusicArtistService {

}

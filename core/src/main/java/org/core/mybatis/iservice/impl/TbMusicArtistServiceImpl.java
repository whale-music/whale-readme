package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbMusicArtistService;
import org.core.mybatis.mapper.TbMusicArtistMapper;
import org.core.mybatis.pojo.TbMusicArtistPojo;
import org.springframework.stereotype.Service;

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

}

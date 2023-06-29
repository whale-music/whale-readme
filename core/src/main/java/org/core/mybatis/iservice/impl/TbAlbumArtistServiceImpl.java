package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbAlbumArtistService;
import org.core.mybatis.mapper.TbAlbumArtistMapper;
import org.core.mybatis.pojo.TbAlbumArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手和专辑中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbAlbumArtistServiceImpl extends ServiceImpl<TbAlbumArtistMapper, TbAlbumArtistPojo> implements TbAlbumArtistService {

}

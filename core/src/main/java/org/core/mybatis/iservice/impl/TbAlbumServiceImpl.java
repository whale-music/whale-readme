package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbAlbumService;
import org.core.mybatis.mapper.TbAlbumMapper;
import org.core.mybatis.pojo.TbAlbumPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌曲专辑表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbAlbumServiceImpl extends ServiceImpl<TbAlbumMapper, TbAlbumPojo> implements TbAlbumService {

}

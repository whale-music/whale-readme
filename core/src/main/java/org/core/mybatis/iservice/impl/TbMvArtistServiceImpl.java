package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbMvArtistService;
import org.core.mybatis.mapper.TbMvArtistMapper;
import org.core.mybatis.pojo.TbMvArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * mv和歌手中间表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbMvArtistServiceImpl extends ServiceImpl<TbMvArtistMapper, TbMvArtistPojo> implements TbMvArtistService {

}

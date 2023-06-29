package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbArtistService;
import org.core.mybatis.mapper.TbArtistMapper;
import org.core.mybatis.pojo.TbArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbArtistServiceImpl extends ServiceImpl<TbArtistMapper, TbArtistPojo> implements TbArtistService {

}

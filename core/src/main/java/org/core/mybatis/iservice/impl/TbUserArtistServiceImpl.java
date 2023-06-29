package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbUserArtistService;
import org.core.mybatis.mapper.TbUserArtistMapper;
import org.core.mybatis.pojo.TbUserArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户关注歌曲家 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbUserArtistServiceImpl extends ServiceImpl<TbUserArtistMapper, TbUserArtistPojo> implements TbUserArtistService {

}

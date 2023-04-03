package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbUserArtistService;
import org.core.mapper.TbUserArtistMapper;
import org.core.pojo.TbUserArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户关注歌曲家 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbUserArtistServiceImpl extends ServiceImpl<TbUserArtistMapper, TbUserArtistPojo> implements TbUserArtistService {

}

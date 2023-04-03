package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbArtistService;
import org.core.mapper.TbArtistMapper;
import org.core.pojo.TbArtistPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌手表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbArtistServiceImpl extends ServiceImpl<TbArtistMapper, TbArtistPojo> implements TbArtistService {

}

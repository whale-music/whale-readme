package org.core.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.iservice.TbUserAlbumService;
import org.core.mapper.TbUserAlbumMapper;
import org.core.pojo.TbUserAlbumPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户收藏专辑表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-03-14
 */
@Service
public class TbUserAlbumServiceImpl extends ServiceImpl<TbUserAlbumMapper, TbUserAlbumPojo> implements TbUserAlbumService {

}

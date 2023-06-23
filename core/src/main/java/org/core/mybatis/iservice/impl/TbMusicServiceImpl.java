package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.mapper.TbMusicMapper;
import org.core.mybatis.pojo.TbMusicPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 所有音乐列表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbMusicServiceImpl extends ServiceImpl<TbMusicMapper, TbMusicPojo> implements TbMusicService {

}

package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbMusicUrlService;
import org.core.mybatis.mapper.TbMusicUrlMapper;
import org.core.mybatis.pojo.TbMusicUrlPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐下载地址 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbMusicUrlServiceImpl extends ServiceImpl<TbMusicUrlMapper, TbMusicUrlPojo> implements TbMusicUrlService {

}

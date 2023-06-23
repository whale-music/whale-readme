package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbLyricService;
import org.core.mybatis.mapper.TbLyricMapper;
import org.core.mybatis.pojo.TbLyricPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 歌词表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-04-05
 */
@Service
public class TbLyricServiceImpl extends ServiceImpl<TbLyricMapper, TbLyricPojo> implements TbLyricService {

}

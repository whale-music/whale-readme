package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbHistoryService;
import org.core.mybatis.mapper.TbHistoryMapper;
import org.core.mybatis.pojo.TbHistoryPojo;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbHistoryServiceImpl extends ServiceImpl<TbHistoryMapper, TbHistoryPojo> implements TbHistoryService {

}

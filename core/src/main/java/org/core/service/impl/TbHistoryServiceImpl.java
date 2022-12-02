package org.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mapper.TbHistoryMapper;
import org.core.pojo.TbHistoryPojo;
import org.core.service.TbHistoryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbHistoryServiceImpl extends ServiceImpl<TbHistoryMapper, TbHistoryPojo> implements TbHistoryService {

}

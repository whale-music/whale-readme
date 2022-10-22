package org.musicbox.service.impl;

import org.musicbox.pojo.TbHistoryPojo;
import org.musicbox.mapper.TbHistoryMapper;
import org.musicbox.service.TbHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐播放历史(包括歌单，音乐，专辑） 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-10-22
 */
@Service
public class TbHistoryServiceImpl extends ServiceImpl<TbHistoryMapper, TbHistoryPojo> implements TbHistoryService {

}

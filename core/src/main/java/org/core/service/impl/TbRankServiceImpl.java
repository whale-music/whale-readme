package org.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mapper.TbRankMapper;
import org.core.pojo.TbRankPojo;
import org.core.service.TbRankService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 音乐播放排行榜 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2022-12-02
 */
@Service
public class TbRankServiceImpl extends ServiceImpl<TbRankMapper, TbRankPojo> implements TbRankService {

}

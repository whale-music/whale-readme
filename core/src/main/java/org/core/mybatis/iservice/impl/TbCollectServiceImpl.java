package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.mapper.TbCollectMapper;
import org.core.mybatis.pojo.TbCollectPojo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 歌单列表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbCollectServiceImpl extends ServiceImpl<TbCollectMapper, TbCollectPojo> implements TbCollectService {
    
    
    /**
     * 获取用户所有歌单
     *
     * @param userId 用户ID
     * @param type   歌单类型
     * @return 歌单列表
     */
    @Override
    public List<TbCollectPojo> getUserCollect(Long userId, byte type) {
        return this.list(Wrappers.<TbCollectPojo>lambdaQuery().eq(TbCollectPojo::getUserId, userId).eq(TbCollectPojo::getType, type));
    }
}

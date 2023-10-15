package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.mapper.TbCollectMapper;
import org.core.mybatis.pojo.TbCollectPojo;
import org.springframework.stereotype.Service;

import java.util.Collection;
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
    
    /**
     * 分页获取用户歌单
     *
     * @param userId  用户 id
     * @param type    歌单类型
     * @param current 分页参数, 当前页数
     * @param size    每页大小
     * @return 返回分页数据
     */
    @Override
    public Page<TbCollectPojo> getUserCollect(Long userId, Collection<Byte> type, long current, long size) {
        LambdaQueryWrapper<TbCollectPojo> wrapper = Wrappers.<TbCollectPojo>lambdaQuery().eq(TbCollectPojo::getUserId, userId)
                                                            .in(TbCollectPojo::getType, type);
        return this.page(new Page<>(current, size), wrapper);
    }
    
    
}

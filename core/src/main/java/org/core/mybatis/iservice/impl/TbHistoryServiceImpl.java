package org.core.mybatis.iservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbHistoryService;
import org.core.mybatis.mapper.TbHistoryMapper;
import org.core.mybatis.pojo.TbHistoryPojo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

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
    
    /**
     * 获取播放最多音乐ID
     *
     * @param userId 用户ID
     * @param type   播放历史类型, 如果为null则全部查询
     * @param offset 分页参数 当前页码
     * @param size   分页参数 每页数量
     * @return 音乐ID
     */
    @Override
    public List<TbHistoryPojo> getFrequent(Long userId, Byte type, Long offset, Long size) {
        LambdaQueryWrapper<TbHistoryPojo> eq = Wrappers.lambdaQuery();
        eq.eq(TbHistoryPojo::getUserId, userId);
        eq.eq(Objects.nonNull(type), TbHistoryPojo::getType, type);
        Page<TbHistoryPojo> page = this.page(new Page<>(offset, size), eq);
        return page.getRecords();
    }
    
    /**
     * 获取最近播放音乐
     *
     * @param userId 用户ID
     * @param type   播放历史类型, 如果为null则全部查询
     * @param offset 分页参数 当前页码
     * @param size   分页参数 每页数量
     * @return 音乐ID
     */
    @Override
    public List<TbHistoryPojo> getRecent(Long userId, Byte type, Long offset, Long size) {
        LambdaQueryWrapper<TbHistoryPojo> eq = Wrappers.lambdaQuery();
        eq.eq(TbHistoryPojo::getUserId, userId);
        eq.eq(Objects.nonNull(type), TbHistoryPojo::getType, type);
        eq.orderByDesc(TbHistoryPojo::getCreateTime);
        Page<TbHistoryPojo> page = this.page(new Page<>(offset, size), eq);
        return page.getRecords();
    }
    
}

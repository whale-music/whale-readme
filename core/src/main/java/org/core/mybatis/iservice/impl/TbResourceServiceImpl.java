package org.core.mybatis.iservice.impl;

import cn.hutool.core.collection.ListUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.core.mybatis.iservice.TbResourceService;
import org.core.mybatis.mapper.TbResourceMapper;
import org.core.mybatis.pojo.TbResourcePojo;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 存储地址 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbResourceServiceImpl extends ServiceImpl<TbResourceMapper, TbResourcePojo> implements TbResourceService {
    
    
    /**
     * 获取音乐资源
     *
     * @param musicIds 音乐ID
     * @return 音乐地址
     */
    @Override
    public Map<Long, List<TbResourcePojo>> getResourceMap(Collection<Long> musicIds) {
        List<TbResourcePojo> list = this.list(Wrappers.<TbResourcePojo>lambdaQuery().in(TbResourcePojo::getMusicId, musicIds));
        return list.parallelStream().collect(Collectors.toMap(TbResourcePojo::getMusicId, ListUtil::toList, (o1, o2) -> {
            o2.addAll(o1);
            return o2;
        }));
    }
    
    /**
     * 根据文件名获取音乐数据
     *
     * @param path 文件名
     * @return 音乐数据
     */
    @Override
    public TbResourcePojo getResourceByPath(String path) {
        return this.getOne(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getPath, path));
    }
    
    /**
     * 获取音源列表
     *
     * @param ids 音乐id
     * @return 音乐数据
     */
    @Override
    public List<TbResourcePojo> getResources(Long ids) {
        if (Objects.isNull(ids)) {
            return Collections.emptyList();
        }
        return this.list(Wrappers.<TbResourcePojo>lambdaQuery().eq(TbResourcePojo::getMusicId, ids));
    }
}

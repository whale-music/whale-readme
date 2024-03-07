package org.core.mybatis.iservice.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.mapper.TbMusicMapper;
import org.core.mybatis.pojo.TbMusicPojo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 所有音乐列表 服务实现类
 * </p>
 *
 * @author Sakura
 * @since 2023-06-28
 */
@Service
public class TbMusicServiceImpl extends ServiceImpl<TbMusicMapper, TbMusicPojo> implements TbMusicService {
    
    /**
     * 通过音乐名获取音乐
     * 获取时必须保证音乐名和数据库中音乐一致，并且只有一条数据
     *
     * @param name  音乐名
     * @param alias 音乐别名, 可为空
     * @return 音乐数据
     */
    @Override
    public TbMusicPojo getMusicByName(String name, String alias) {
        LambdaQueryWrapper<TbMusicPojo> eq = Wrappers.<TbMusicPojo>lambdaQuery().
                                                     eq(TbMusicPojo::getMusicName, name)
                                                     .eq(StringUtils.isNoneBlank(alias), TbMusicPojo::getAliasName, alias);
        return this.getOne(eq);
    }
    
    /**
     * 获取音乐列表
     *
     * @param ids music id list
     * @return 音乐列表
     */
    @Override
    public Map<Long, TbMusicPojo> getMusicList(List<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        List<TbMusicPojo> list = this.listByIds(ids);
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        return list.parallelStream().collect(Collectors.toMap(TbMusicPojo::getId, s -> s));
    }
}

package org.musicbox.compatibility;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.musicbox.pojo.TbCollectTagPojo;
import org.musicbox.pojo.TbTagPojo;
import org.musicbox.service.TbCollectTagService;
import org.musicbox.service.TbTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 歌单中间层
 */
@Slf4j
@Service
public class CollectCompatibility {
    @Autowired
    private TbTagService tagService;
    
    @Autowired
    private TbCollectTagService collectTagService;
    
    /**
     * 查询tag表
     * 根据tag id列表返回歌单tag信息
     *
     * @param tagIds tag ID
     */
    public List<TbTagPojo> getTagPojoList(List<Long> tagIds) {
        if (tagIds == null || tagIds.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbTagPojo> lambdaQueryWrapper = Wrappers.<TbTagPojo>lambdaQuery().in(TbTagPojo::getId, tagIds);
        return tagService.list(lambdaQueryWrapper);
    }
    
    /**
     * 获取歌单对各的tag id
     *
     * @param collectId 歌单ID
     * @return 返回中间表tag id列表
     */
    public List<TbCollectTagPojo> getCollectTagIdList(List<Long> collectId) {
        if (collectId == null || collectId.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<TbCollectTagPojo> lambdaQueryWrapper = Wrappers.<TbCollectTagPojo>lambdaQuery().in(TbCollectTagPojo::getCollectId, collectId);
        return collectTagService.list(lambdaQueryWrapper);
    }
}

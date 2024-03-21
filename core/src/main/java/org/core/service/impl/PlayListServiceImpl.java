package org.core.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.PlayListTypeConstant;
import org.core.mybatis.iservice.TbCollectMusicService;
import org.core.mybatis.iservice.TbCollectService;
import org.core.mybatis.iservice.TbMusicService;
import org.core.mybatis.pojo.TbCollectMusicPojo;
import org.core.mybatis.pojo.TbCollectPojo;
import org.core.mybatis.pojo.TbMusicPojo;
import org.core.service.PlayListService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service("playListServiceImpl")
public class PlayListServiceImpl implements PlayListService {
    
    private final TbCollectService collectService;
    
    private final TbCollectMusicService collectMusicService;
    
    private final TbMusicService musicService;
    
    public PlayListServiceImpl(TbCollectService collectService, TbCollectMusicService collectMusicService, TbMusicService musicService) {
        this.collectService = collectService;
        this.collectMusicService = collectMusicService;
        this.musicService = musicService;
    }
    
    public Page<TbCollectPojo> getPlayList(TbCollectPojo collectPojo, Long current, Long size, Byte type) {
        collectPojo = Optional.ofNullable(collectPojo).orElse(new TbCollectPojo());
        Page<TbCollectPojo> page = new Page<>(current, size);
        LambdaQueryWrapper<TbCollectPojo> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(collectPojo.getId() != null, TbCollectPojo::getId, collectPojo.getId());
        queryWrapper.like(StringUtils.isNotBlank(collectPojo.getPlayListName()), TbCollectPojo::getPlayListName, collectPojo.getPlayListName());
        // 只查询普通歌单
        queryWrapper.eq(type != null && type != -1, TbCollectPojo::getType, type);
        collectService.page(page, queryWrapper);
        return page;
    }
    
    public List<TbCollectPojo> randomPlayList(Long limit) {
        List<TbCollectPojo> tbCollectPojos = new ArrayList<>();
        long count = collectService.count();
        if (count == 0) {
            return new ArrayList<>();
        }
        for (int i = 0; i < limit; i++) {
            long randomNum = RandomUtil.randomLong(count);
            Page<TbCollectPojo> playList = getPlayList(null, randomNum, 1L, PlayListTypeConstant.ORDINARY);
            tbCollectPojos.addAll(playList.getRecords());
        }
        Set<TbCollectPojo> playerSet = new TreeSet<>(Comparator.comparing(TbCollectPojo::getId));
        playerSet.addAll(tbCollectPojos);
        return new ArrayList<>(playerSet);
    }
    
    /**
     * 获取歌单下所有音乐
     *
     * @param id 歌单ID
     */
    public List<TbMusicPojo> getPlayListAllMusic(Long id) {
        List<Long> musicIds = collectMusicService.listObjs(Wrappers.<TbCollectMusicPojo>lambdaQuery()
                                                                   .select(TbCollectMusicPojo::getMusicId)
                                                                   .eq(TbCollectMusicPojo::getCollectId, id));
        if (CollUtil.isEmpty(musicIds)) {
            return Collections.emptyList();
        }
        return musicService.listByIds(musicIds);
    }
    
    /**
     * 获取歌单下的音乐
     *
     * @param ids 歌单ID
     * @return 歌单数据 key id, value playlist
     */
    @Override
    public Map<Long, List<TbMusicPojo>> getPlayListMusicMap(List<Long> ids) {
        List<TbCollectMusicPojo> list = collectMusicService.list(Wrappers.<TbCollectMusicPojo>lambdaQuery().in(TbCollectMusicPojo::getCollectId, ids));
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyMap();
        }
        Map<Long, List<Long>> collectMusicMaps = list.parallelStream()
                                                     .collect(Collectors.toMap(TbCollectMusicPojo::getCollectId,
                                                             tbCollectMusicPojo -> ListUtil.toList(tbCollectMusicPojo.getMusicId()),
                                                             (o1, o2) -> {
                                                                 o2.addAll(o1);
                                                                 return o2;
                                                             }));
        List<Long> musicIds = list.stream().map(TbCollectMusicPojo::getMusicId).toList();
        List<TbMusicPojo> tbMusicPojos = musicService.listByIds(musicIds);
        if (CollUtil.isEmpty(tbMusicPojos)) {
            return Collections.emptyMap();
        }
        Map<Long, TbMusicPojo> musicMaps = tbMusicPojos.parallelStream().collect(Collectors.toMap(TbMusicPojo::getId, s -> s));
        
        HashMap<Long, List<TbMusicPojo>> map = new HashMap<>();
        for (Map.Entry<Long, List<Long>> collectMusic : collectMusicMaps.entrySet()) {
            List<TbMusicPojo> musicList = collectMusic.getValue().parallelStream().map(musicMaps::get).toList();
            map.put(collectMusic.getKey(), musicList);
        }
        return map;
    }
}

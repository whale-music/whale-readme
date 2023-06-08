package org.api.admin.task.timer;


import cn.hutool.core.collection.CollUtil;
import org.api.admin.common.constant.TimeDeleteTask;
import org.api.common.service.QukuAPI;
import org.core.iservice.*;
import org.core.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PicTimeDeleteTask {
    private final QukuAPI qukuAPI;
    
    private final TbPicService picService;
    
    private final TbMusicService musicService;
    
    private final TbAlbumService albumService;
    
    private final TbArtistService artistService;
    
    private final TbCollectService collectService;
    
    @Autowired
    private TbMiddleTagService middleTagService;
    
    @Autowired
    private TbTagService tagService;
    
    public PicTimeDeleteTask(QukuAPI qukuAPI, TbPicService picService, TbMusicService musicService, TbAlbumService albumService, TbArtistService artistService, TbCollectService collectService) {
        this.qukuAPI = qukuAPI;
        this.picService = picService;
        this.musicService = musicService;
        this.albumService = albumService;
        this.artistService = artistService;
        this.collectService = collectService;
    }
    
    @Scheduled(cron = TimeDeleteTask.CRON)   // 每天00:00执行一次
    public void autoDeletePic() {
        List<TbMusicPojo> musicList = musicService.list();
        List<TbAlbumPojo> albumList = albumService.list();
        List<TbArtistPojo> artistList = artistService.list();
        List<TbCollectPojo> collectList = collectService.list();
        
        List<TbPicPojo> list = picService.list();
        List<Long> picIds = list.parallelStream().map(TbPicPojo::getId).collect(Collectors.toList());
        Set<Long> musicPicIds = musicList.parallelStream().map(TbMusicPojo::getPicId).collect(Collectors.toSet());
        Set<Long> albumPicIds = albumList.parallelStream().map(TbAlbumPojo::getPicId).collect(Collectors.toSet());
        Set<Long> artistPicIds = artistList.parallelStream().map(TbArtistPojo::getPicId).collect(Collectors.toSet());
        Set<Long> collectPicIds = collectList.parallelStream().map(TbCollectPojo::getPicId).collect(Collectors.toSet());
        
        HashSet<Long> tempPicAll = new HashSet<>();
        tempPicAll.addAll(musicPicIds);
        tempPicAll.addAll(albumPicIds);
        tempPicAll.addAll(artistPicIds);
        tempPicAll.addAll(collectPicIds);
        // 获取没有关联的封面，然后删除
        List<Long> subtract = CollUtil.subtractToList(picIds, tempPicAll);
        if (CollUtil.isNotEmpty(subtract)) {
            qukuAPI.removePicIds(subtract);
        }
    }
    
    @Scheduled(cron = TimeDeleteTask.CRON)   // 每天00:00执行一次
    public void autoDeleteTag() {
        List<TbTagPojo> tagList = tagService.list();
        List<TbMiddleTagPojo> middleTagList = middleTagService.list();
        List<Long> tagIds = tagList.parallelStream().map(TbTagPojo::getId).collect(Collectors.toList());
        List<Long> middleTagIds = middleTagList.parallelStream().map(TbMiddleTagPojo::getMiddleId).collect(Collectors.toList());
        List<Long> subtract = CollUtil.subtractToList(tagIds, middleTagIds);
        if (CollUtil.isNotEmpty(subtract)) {
            subtract.parallelStream().forEach(qukuAPI::removeLabelAll);
        }
    }
}

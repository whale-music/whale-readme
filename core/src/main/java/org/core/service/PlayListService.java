package org.core.service;

import org.core.common.page.Page;
import org.core.pojo.CollectPojo;
import org.core.pojo.MusicPojo;

import java.util.List;

public interface PlayListService {
    
    /**
     * 歌单查询
     */
    Page<CollectPojo> getPlayList(CollectPojo collectPojo, Long current, Long size);
    
    /**
     * 随即推荐歌单
     */
    List<CollectPojo> randomPlayList(Long limit);
    
    
    /**
     * 获取歌单下所有音乐
     *
     * @param id 歌单ID
     */
    public List<MusicPojo> getPlayListAllMusic(Long id);
}

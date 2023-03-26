package org.core.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.core.pojo.TbCollectPojo;
import org.core.pojo.TbMusicPojo;

import java.util.List;

public interface PlayListService {
    
    /**
     * 歌单查询
     */
    Page<TbCollectPojo> getPlayList(TbCollectPojo collectPojo, Long current, Long size);
    
    /**
     * 随即推荐歌单
     */
    List<TbCollectPojo> randomPlayList(Long limit);
    
    
    /**
     * 获取歌单下所有音乐
     *
     * @param id 歌单ID
     */
    public List<TbMusicPojo> getPlayListAllMusic(Long id);
}

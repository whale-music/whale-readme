package org.api.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.res.MusicStatisticsRes;
import org.api.common.service.MusicCommonApi;
import org.core.iservice.TbAlbumService;
import org.core.iservice.TbArtistService;
import org.core.iservice.TbMusicService;
import org.core.iservice.TbMusicUrlService;
import org.core.pojo.TbAlbumPojo;
import org.core.pojo.TbMusicPojo;
import org.core.pojo.TbMusicUrlPojo;
import org.core.pojo.TbPluginTaskPojo;
import org.core.service.TbPluginTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service(AdminConfig.ADMIN + "HoneApi")
public class HoneApi {
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbArtistService artistService;
    
    @Autowired
    private MusicCommonApi musicCommonApi;
    
    @Autowired
    private TbMusicUrlService musicUrlService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    public List<TbMusicPojo> getMusicTop() {
        LambdaQueryWrapper<TbMusicPojo> queryWrapper = Wrappers.<TbMusicPojo>lambdaQuery().orderByDesc(TbMusicPojo::getCreateTime);
        Page<TbMusicPojo> objectPage = new Page<>(0, 15);
        return musicService.page(objectPage, queryWrapper).getRecords();
    }
    
    public List<TbAlbumPojo> getAlbumTop() {
        LambdaQueryWrapper<TbAlbumPojo> wrapper = Wrappers.<TbAlbumPojo>lambdaQuery().orderByDesc(TbAlbumPojo::getCreateTime);
        Page<TbAlbumPojo> objectPage = new Page<>(0, 15);
        return albumService.page(objectPage, wrapper).getRecords();
    }
    
    public int getMusicCount() {
        return Math.toIntExact(musicService.count());
    }
    
    public int getAlbumCount() {
        return Math.toIntExact(albumService.count());
    }
    
    public int getArtistCount() {
        return Math.toIntExact(artistService.count());
    }
    
    public List<MusicStatisticsRes> getMusicStatistics() {
        ArrayList<MusicStatisticsRes> res = new ArrayList<>();
        List<TbMusicUrlPojo> musicUrlList = musicUrlService.list();
        List<TbMusicPojo> musicList = musicService.list();
        List<TbMusicUrlPojo> musicUrlByMusicUrlList = musicCommonApi.getMusicUrlByMusicUrlList(musicUrlList, false);
        Collection<String> musicMD5 = musicCommonApi.getMusicMD5(false);
        // 有效音乐
        // 音乐数据对比存储地址，查找对应的音乐地址是否存在
        long musicEffectiveCount = musicList.parallelStream().filter(tbMusicPojo ->
                musicUrlByMusicUrlList.parallelStream().anyMatch(musicUrlPojo -> Objects.equals(tbMusicPojo.getId(), musicUrlPojo.getMusicId()))
        ).count();
        MusicStatisticsRes e = new MusicStatisticsRes();
        e.setValue(musicEffectiveCount);
        e.setName("effectiveMusic");
        res.add(e);
        // 无音源
        long noSoundSourceCount = musicList.parallelStream().filter(tbMusicUrlPojo1 ->
                musicUrlByMusicUrlList.parallelStream()
                                      .anyMatch(musicUrlPojo2 ->
                                              StringUtils.isNotBlank(musicUrlPojo2.getUrl()) &&
                                                      Objects.equals(tbMusicUrlPojo1.getId(), musicUrlPojo2.getMusicId()))
        ).count();
        MusicStatisticsRes e11 = new MusicStatisticsRes();
        e11.setValue(musicList.size() - noSoundSourceCount);
        e11.setName("noSoundSourceCount");
        res.add(e11);
    
        // 失效音源
        // 查询音乐地址数据在音乐存储地址中是否存在
        long invalidMusicOriginCount = musicUrlList.parallelStream().filter(tbMusicUrlPojo1 ->
                musicUrlByMusicUrlList.parallelStream().anyMatch(musicUrlPojo2 -> Objects.equals(tbMusicUrlPojo1.getMusicId(), musicUrlPojo2.getMusicId()))
        ).count();
        MusicStatisticsRes e1 = new MusicStatisticsRes();
        e1.setValue(musicUrlList.size() - invalidMusicOriginCount);
        e1.setName("invalidMusicOrigin");
        res.add(e1);
        
        // 废弃音源
        // 存储地址没有在数据库中记录的数据
        long discardMusicOriginCount = musicMD5.parallelStream().filter(md5 ->
                musicUrlByMusicUrlList.parallelStream().anyMatch(musicUrlPojo2 -> Objects.equals(md5, musicUrlPojo2.getMd5()))
        ).count();
        MusicStatisticsRes e2 = new MusicStatisticsRes();
        e2.setValue(musicMD5.size() - discardMusicOriginCount);
        e2.setName("discardMusicOrigin");
        res.add(e2);
    
        return res;
    }
    
    public List<TbPluginTaskPojo> getPluginTask(Long id) {
        LambdaQueryWrapper<TbPluginTaskPojo> eq = Wrappers.<TbPluginTaskPojo>lambdaQuery()
                                                          .eq(TbPluginTaskPojo::getUserId, id);
        Page<TbPluginTaskPojo> page = pluginTaskService.page(new Page<>(0, 15), eq);
        return page.getRecords();
    }
}

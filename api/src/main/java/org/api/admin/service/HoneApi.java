package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.convert.Count;
import org.api.admin.model.res.MusicStatisticsRes;
import org.api.admin.model.res.PluginTaskRes;
import org.api.common.service.QukuAPI;
import org.core.mybatis.iservice.*;
import org.core.mybatis.model.convert.AlbumConvert;
import org.core.mybatis.model.convert.ArtistConvert;
import org.core.mybatis.model.convert.MusicConvert;
import org.core.mybatis.pojo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "HoneApi")
public class HoneApi {
    
    @Autowired
    private TbMusicService musicService;
    
    @Autowired
    private TbAlbumService albumService;
    
    @Autowired
    private TbArtistService artistService;
    
    @Autowired
    private TbResourceService musicUrlService;
    
    @Autowired
    private TbPluginTaskService pluginTaskService;
    
    @Autowired
    private TbPluginService pluginService;
    
    @Autowired
    private QukuAPI qukuAPI;
    
    /**
     * **计算月增长率**
     * sameMonth 本月
     * lastMonth 上月
     */
    public static Float getAnalysisData(int sameMonth, int lastMonth) {
        int growthNum;
        if (lastMonth > 0 && sameMonth > 0) {
            if (lastMonth < sameMonth) {
                // 如果下个数大于上个数，则增长率 为 正
                growthNum = sameMonth - lastMonth;
                return (float) growthNum / (float) lastMonth * 100;
            } else if (lastMonth > sameMonth) {
                // 如果下个数小于上个数，则增长率 为 负
                growthNum = lastMonth - sameMonth;
                float v = (float) growthNum / (float) sameMonth * 100;
                return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).floatValue();
            } else {
                // 如果相等，增长率为 0
                return 0F;
            }
        } else if (lastMonth > 0 && sameMonth == 0) {
            // 如果上个数大于0，下个数为0，增长率为 0
            return 0F;
        } else if (lastMonth == 0 && sameMonth > 0) {
            // 如果下个数大于0，上个数为0，增长率为 0
            growthNum = sameMonth - lastMonth;
            float v = (float) growthNum / (float) sameMonth * 100;
            return BigDecimal.valueOf(v).setScale(2, RoundingMode.HALF_UP).floatValue();
        } else {
            // 如果相等，增长率为 0
            return 0F;
        }
    }
    
    public List<MusicConvert> getMusicTop() {
        LambdaQueryWrapper<TbMusicPojo> queryWrapper = Wrappers.<TbMusicPojo>lambdaQuery().orderByDesc(TbMusicPojo::getCreateTime);
        Page<TbMusicPojo> objectPage = new Page<>(0, 15);
        List<TbMusicPojo> records = musicService.page(objectPage, queryWrapper).getRecords();
        return qukuAPI.getPicMusicList(records);
    }
    
    public List<AlbumConvert> getAlbumTop() {
        LambdaQueryWrapper<TbAlbumPojo> wrapper = Wrappers.<TbAlbumPojo>lambdaQuery()
                                                          .isNotNull(TbAlbumPojo::getAlbumName)
                                                          .orderByDesc(TbAlbumPojo::getCreateTime);
        Page<TbAlbumPojo> objectPage = new Page<>(1, 15);
        List<TbAlbumPojo> records = albumService.page(objectPage, wrapper).getRecords();
        return qukuAPI.getPicAlbumList(records);
    }
    
    public List<ArtistConvert> getArtist() {
        LambdaQueryWrapper<TbArtistPojo> wrapper = Wrappers.<TbArtistPojo>lambdaQuery()
                                                           .orderByDesc(TbArtistPojo::getCreateTime);
        Page<TbArtistPojo> page = artistService.page(new Page<>(0, 15), wrapper);
        return qukuAPI.getPicArtistList(page.getRecords());
    }
    
    public Count getMusicCount() {
        long count = musicService.count();
        // 本月
        Date date = new Date();
        LambdaQueryWrapper<TbMusicPojo> ge = Wrappers.<TbMusicPojo>lambdaQuery()
                                                     .ge(TbMusicPojo::getCreateTime, date)
                                                     .lt(TbMusicPojo::getCreateTime, DateUtil.offsetMonth(date, 1));
        long localMonth = musicService.count(ge);
        
        // 上一个月
        LambdaQueryWrapper<TbMusicPojo> last = Wrappers.<TbMusicPojo>lambdaQuery()
                                                       .ge(TbMusicPojo::getCreateTime, DateUtil.offsetMonth(date, -2))
                                                       .lt(TbMusicPojo::getCreateTime, DateUtil.offsetMonth(date, -1));
        long lastMonth = musicService.count(last);
        
        Float analysisData = getAnalysisData(Math.toIntExact(lastMonth), Math.toIntExact(localMonth));
        Boolean fluctuate = analysisData == 0 ? null : analysisData > 0;
        return new Count(count, analysisData, fluctuate);
    }
    
    public Count getAlbumCount() {
        long count = albumService.count();
        // 本月
        Date date = new Date();
        Wrapper<TbAlbumPojo> ge = Wrappers.<TbAlbumPojo>lambdaQuery()
                                          .ge(TbAlbumPojo::getCreateTime, date)
                                          .lt(TbAlbumPojo::getCreateTime, DateUtil.offsetMonth(date, 1));
        long localMonth = albumService.count(ge);
        
        // 上一个月
        Wrapper<TbAlbumPojo> last = Wrappers.<TbAlbumPojo>lambdaQuery()
                                            .ge(TbAlbumPojo::getCreateTime, DateUtil.offsetMonth(date, -2))
                                            .lt(TbAlbumPojo::getCreateTime, DateUtil.offsetMonth(date, -1));
        long lastMonth = albumService.count(last);
        
        Float analysisData = getAnalysisData(Math.toIntExact(lastMonth), Math.toIntExact(localMonth));
        Boolean fluctuate = analysisData == 0 ? null : analysisData > 0;
        return new Count(count, analysisData, fluctuate);
    }
    
    public Count getArtistCount() {
        long count = artistService.count();
        // 本月
        Date date = new Date();
        Wrapper<TbArtistPojo> ge = Wrappers.<TbArtistPojo>lambdaQuery()
                                           .ge(TbArtistPojo::getCreateTime, date)
                                           .lt(TbArtistPojo::getCreateTime, DateUtil.offsetMonth(date, 1));
        long localMonth = artistService.count(ge);
        
        // 上一个月
        Wrapper<TbArtistPojo> last = Wrappers.<TbArtistPojo>lambdaQuery()
                                             .ge(TbArtistPojo::getCreateTime, DateUtil.offsetMonth(date, -2))
                                             .lt(TbArtistPojo::getCreateTime, DateUtil.offsetMonth(date, -1));
        long lastMonth = artistService.count(last);
        
        Float analysisData = getAnalysisData(Math.toIntExact(lastMonth), Math.toIntExact(localMonth));
        Boolean fluctuate = analysisData == 0 ? null : analysisData > 0;
        return new Count(count, analysisData, fluctuate);
    }
    
    public List<MusicStatisticsRes> getMusicStatistics() {
        ArrayList<MusicStatisticsRes> res = new ArrayList<>();
        List<TbResourcePojo> musicUrlList = musicUrlService.list();
        List<TbMusicPojo> musicList = musicService.list();
        List<TbResourcePojo> musicUrlByMusicUrlList = qukuAPI.getMusicUrlByMusicUrlList(musicUrlList, false);
        Collection<String> musicMD5 = qukuAPI.getMD5(false);
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
                                              StringUtils.isNotBlank(musicUrlPojo2.getPath()) &&
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
    
    public List<PluginTaskRes> getPluginTask(Long id) {
        LambdaQueryWrapper<TbPluginTaskPojo> eq = Wrappers.<TbPluginTaskPojo>lambdaQuery()
                                                          .eq(TbPluginTaskPojo::getUserId, id)
                                                          .orderByDesc(TbPluginTaskPojo::getCreateTime);
        Page<TbPluginTaskPojo> page = pluginTaskService.page(new Page<>(0, 15), eq);
        if (CollUtil.isEmpty(page.getRecords())) {
            return Collections.emptyList();
        }
        Set<Long> collect = page.getRecords().parallelStream().map(TbPluginTaskPojo::getPluginId).collect(Collectors.toSet());
        List<TbPluginPojo> pluginPojoList = pluginService.listByIds(collect);
        Map<Long, TbPluginPojo> map = pluginPojoList.parallelStream().collect(Collectors.toMap(TbPluginPojo::getId, o -> o));
        ArrayList<PluginTaskRes> res = new ArrayList<>();
        for (TbPluginTaskPojo taskPojo : page.getRecords()) {
            TbPluginPojo tbPluginPojo = map.get(taskPojo.getPluginId());
            PluginTaskRes e = new PluginTaskRes();
            BeanUtils.copyProperties(taskPojo, e);
            e.setPluginName(tbPluginPojo.getPluginName());
            res.add(e);
        }
        return res;
    }
}

package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.convert.Count;
import org.api.admin.model.res.LastMusicRes;
import org.api.admin.model.res.MusicStatisticsRes;
import org.api.admin.model.res.PluginTaskRes;
import org.api.admin.model.res.UsersUploadRes;
import org.api.admin.utils.LineGraphUtil;
import org.api.common.service.QukuAPI;
import org.core.common.constant.HistoryConstant;
import org.core.common.constant.PlayListTypeConstant;
import org.core.jpa.entity.TbCollectEntity;
import org.core.jpa.entity.TbCollectMusicEntity;
import org.core.jpa.entity.TbMusicEntity;
import org.core.jpa.service.TbCollectMusicEntityService;
import org.core.jpa.service.TbMusicEntityService;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.*;
import org.core.service.AccountService;
import org.core.service.RemoteStorageService;
import org.core.service.RemoteStorePicService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "HoneApi")
public class HoneApi {
    
    private final TbMusicService musicService;
    @Autowired
    private TbMusicEntityService tbMusicEntityService;
    
    private final TbAlbumService albumService;
    
    private final TbArtistService artistService;
    
    private final TbMvInfoService mvInfoService;
    
    private final TbResourceService musicUrlService;
    
    private final TbPluginTaskService pluginTaskService;
    
    private final TbPluginService pluginService;
    
    private final QukuAPI qukuAPI;
    
    private final TbCollectService tbCollectService;
    
    private final AccountService accountService;
    
    private final RemoteStorageService remoteStorageService;
    
    private final RemoteStorePicService remoteStorePicService;
    
    @Autowired
    private TbHistoryService tbHistoryService;
    
    @Autowired
    private TbCollectMusicService tbCollectMusicService;
    
    @Autowired
    private TbCollectMusicEntityService tbCollectMusicEntityService;
    
    public HoneApi(TbMusicService musicService, TbAlbumService albumService, TbArtistService artistService, TbResourceService musicUrlService, TbPluginTaskService pluginTaskService, TbPluginService pluginService, QukuAPI qukuAPI, RemoteStorageService remoteStorageService, TbMvInfoService mvInfoService, AccountService accountService, TbCollectService tbCollectService, RemoteStorePicService remoteStorePicService) {
        this.musicService = musicService;
        this.albumService = albumService;
        this.artistService = artistService;
        this.musicUrlService = musicUrlService;
        this.pluginTaskService = pluginTaskService;
        this.pluginService = pluginService;
        this.qukuAPI = qukuAPI;
        this.remoteStorageService = remoteStorageService;
        this.mvInfoService = mvInfoService;
        this.accountService = accountService;
        this.tbCollectService = tbCollectService;
        this.remoteStorePicService = remoteStorePicService;
    }
    
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
    
    public Count getMusicCount() {
        long count = musicService.count();
        // 本月
        Date monthDate = new Date();
        LambdaQueryWrapper<TbMusicPojo> ge = Wrappers.<TbMusicPojo>lambdaQuery()
                                                     .ge(TbMusicPojo::getCreateTime, monthDate)
                                                     .lt(TbMusicPojo::getCreateTime, DateUtil.offsetMonth(monthDate, 1));
        long localMonth = musicService.count(ge);
        
        // 上一个月
        LambdaQueryWrapper<TbMusicPojo> last = Wrappers.<TbMusicPojo>lambdaQuery()
                                                       .ge(TbMusicPojo::getCreateTime, DateUtil.offsetMonth(monthDate, -2))
                                                       .lt(TbMusicPojo::getCreateTime, DateUtil.offsetMonth(monthDate, -1));
        long lastMonth = musicService.count(last);
        
        // 最近七天上传数量
        List<Map<String, Object>> maps = musicService.listMaps(
                Wrappers.<TbMusicPojo>lambdaQuery()
                        .between(TbMusicPojo::getCreateTime, LocalDate.now().minusDays(6), LocalDate.now())
                        .groupBy(TbMusicPojo::getCreateTime));
        return getCount(count, localMonth, lastMonth, maps);
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
        
        // 最近七天上传数量
        List<Map<String, Object>> maps = albumService.listMaps(
                Wrappers.<TbAlbumPojo>lambdaQuery()
                        .between(TbAlbumPojo::getCreateTime, LocalDate.now().minusDays(6), LocalDate.now())
                        .groupBy(TbAlbumPojo::getCreateTime));
        return getCount(count, localMonth, lastMonth, maps);
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
        
        // 最近七天上传数量
        List<Map<String, Object>> maps = artistService.listMaps(
                Wrappers.<TbArtistPojo>lambdaQuery()
                        .between(TbArtistPojo::getCreateTime, LocalDate.now().minusDays(6), LocalDate.now())
                        .groupBy(TbArtistPojo::getCreateTime));
        return getCount(count, localMonth, lastMonth, maps);
    }
    
    public Count getMvCount() {
        long count = mvInfoService.count();
        // 本月
        Date date = new Date();
        Wrapper<TbMvInfoPojo> ge = Wrappers.<TbMvInfoPojo>lambdaQuery()
                                           .ge(TbMvInfoPojo::getCreateTime, date)
                                           .lt(TbMvInfoPojo::getCreateTime, DateUtil.offsetMonth(date, 1));
        long localMonth = mvInfoService.count(ge);
        
        // 上一个月
        Wrapper<TbMvInfoPojo> last = Wrappers.<TbMvInfoPojo>lambdaQuery()
                                             .ge(TbMvInfoPojo::getCreateTime, DateUtil.offsetMonth(date, -2))
                                             .lt(TbMvInfoPojo::getCreateTime, DateUtil.offsetMonth(date, -1));
        long lastMonth = mvInfoService.count(last);
        
        // 最近七天上传数量
        List<Map<String, Object>> maps = mvInfoService.listMaps(
                Wrappers.<TbMvInfoPojo>lambdaQuery()
                        .between(TbMvInfoPojo::getCreateTime, LocalDate.now().minusDays(6), LocalDate.now())
                        .groupBy(TbMvInfoPojo::getCreateTime));
        return getCount(count, localMonth, lastMonth, maps);
    }
    
    @NotNull
    private Count getCount(long count, long localMonth, long lastMonth, List<Map<String, Object>> maps) {
        List<Integer> integers = LineGraphUtil.calculateTheLineGraph(maps);
        
        Float analysisData = getAnalysisData(Math.toIntExact(lastMonth), Math.toIntExact(localMonth));
        Boolean fluctuate = analysisData == 0 ? null : analysisData > 0;
        return new Count(count, analysisData, fluctuate, integers);
    }
    
    public List<MusicStatisticsRes> getMusicStatistics() {
        ArrayList<MusicStatisticsRes> res = new ArrayList<>();
        List<TbResourcePojo> musicUrlList = musicUrlService.list();
        List<TbMusicPojo> musicList = musicService.list();
        List<TbResourcePojo> musicUrlByMusicUrlList = qukuAPI.getMusicUrlByMusicUrlList(musicUrlList, false);
        Collection<String> musicMD5 = remoteStorageService.getResourceAllMD5(false);
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
    
    public List<UsersUploadRes> getUsersUpload() {
        List<UsersUploadRes> usersUploadRes = new ArrayList<>();
        Page<SysUserPojo> page = accountService.page(Page.of(1, 8), Wrappers.<SysUserPojo>lambdaQuery().orderByDesc(SysUserPojo::getCreateTime));
        List<SysUserPojo> records = page.getRecords();
        for (SysUserPojo user : records) {
            UsersUploadRes e = new UsersUploadRes();
            String userAvatarPicPath = remoteStorePicService.getUserAvatarPicPath(user.getId());
            String avatarContent = String.format("<img class=\"rounded-full\" width=\"50\" height=\"50\" src=\"%s\" alt=\"%s\">",
                    userAvatarPicPath,
                    user.getUsername());
            e.setAvatarUrl(avatarContent);
            
            String userNameContent = String.format("<span class='font-bold'>%s</span>", user.getUsername());
            e.setUsername(userNameContent);
            long artistCount = artistService.count(Wrappers.<TbArtistPojo>lambdaQuery().eq(TbArtistPojo::getUserId, user.getId()));
            e.setArtistCount(artistCount);
            
            long albumCount = albumService.count(Wrappers.<TbAlbumPojo>lambdaQuery().eq(TbAlbumPojo::getUserId, user.getId()));
            e.setAlbumCount(albumCount);
            
            long musicCount = musicService.count(Wrappers.<TbMusicPojo>lambdaQuery().eq(TbMusicPojo::getUserId, user.getId()));
            e.setMusicCount(musicCount);
            
            long collectCount = tbCollectService.count(Wrappers.<TbCollectPojo>lambdaQuery().eq(TbCollectPojo::getUserId, user.getId()));
            e.setPlaylistCount(collectCount);
            
            usersUploadRes.add(e);
        }
        return usersUploadRes;
    }
    
    public List<LastMusicRes> getLastMusic() {
        ArrayList<LastMusicRes> res = new ArrayList<>();
        Page<TbHistoryPojo> page = tbHistoryService.page(Page.of(1, 10),
                Wrappers.<TbHistoryPojo>lambdaQuery().eq(TbHistoryPojo::getType, HistoryConstant.MUSIC).orderByDesc(TbHistoryPojo::getCount));
        List<TbHistoryPojo> records = page.getRecords();
        
        Map<Long, Integer> playCountMap = records.parallelStream()
                                                 .collect(Collectors.toMap(TbHistoryPojo::getMiddleId, TbHistoryPojo::getCount, Integer::sum));
        List<Long> list = records.parallelStream().map(TbHistoryPojo::getMiddleId).toList();
        
        Set<TbMusicEntity> musicSet = tbMusicEntityService.list(list);
        
        List<TbCollectMusicEntity> tbCollectMusicEntities = tbCollectMusicEntityService.listByMusicIds(list);
        Map<Long, List<TbCollectMusicEntity>> map = tbCollectMusicEntities.parallelStream()
                                                                          .collect(Collectors.toMap(TbCollectMusicEntity::getMusicId,
                                                                                  ListUtil::toList, (objects, objects2) -> {
                                                                                      objects2.addAll(objects);
                                                                                      return objects2;
                                                                                  }));
        for (TbMusicEntity tbMusicPojo : musicSet) {
            LastMusicRes e = new LastMusicRes();
            e.setMusicId(tbMusicPojo.getId());
            e.setMusicName(tbMusicPojo.getMusicName());
            e.setMusicNameAlias(tbMusicPojo.getAliasName());
            e.setMusicPic(remoteStorePicService.getMusicPicUrl(tbMusicPojo.getId()));
            
            List<LastMusicRes.ArtistNameId> list1 = tbMusicPojo.getTbMusicArtistsById()
                                                               .parallelStream()
                                                               .map(s -> new LastMusicRes.ArtistNameId(s.getTbArtistByArtistId().getId(),
                                                                       s.getTbArtistByArtistId()
                                                                        .getArtistName())).toList();
            e.setArtists(list1);
            e.setCreateDate(tbMusicPojo.getCreateTime().toLocalDateTime().toLocalDate());
            
            // 计算音乐所有用户喜欢, 收藏数
            List<TbCollectMusicEntity> tbCollectMusicEntities1 = map.get(tbMusicPojo.getId());
            int loveCount = 0;
            int ordinaryCount = 0;
            for (TbCollectMusicEntity tbCollectMusicEntity : tbCollectMusicEntities1) {
                TbCollectEntity tbCollectByCollectId = tbCollectMusicEntity.getTbCollectByCollectId();
                if (Objects.equals(tbCollectByCollectId.getType(), PlayListTypeConstant.LIKE)) {
                    loveCount++;
                }
                if (Objects.equals(tbCollectByCollectId.getType(), PlayListTypeConstant.ORDINARY)) {
                    ordinaryCount++;
                }
            }
            e.setLoveTheData(loveCount);
            e.setNumberOfFavorites(ordinaryCount);
            
            e.setPlayCount(playCountMap.get(tbMusicPojo.getId()));
            res.add(e);
        }
        return res;
    }
}

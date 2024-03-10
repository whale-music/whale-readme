package org.api.admin.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.config.AdminConfig;
import org.api.admin.model.common.PageResCommon;
import org.api.admin.model.res.MusicHistoryRes;
import org.core.common.constant.HistoryConstant;
import org.core.jpa.entity.SysUserEntity;
import org.core.jpa.entity.TbHistoryEntity;
import org.core.jpa.service.TbHistoryEntityService;
import org.core.mybatis.iservice.*;
import org.core.mybatis.pojo.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service(AdminConfig.ADMIN + "HistoryApi")
@Slf4j
@AllArgsConstructor
public class HistoryApi {
    
    private final TbHistoryEntityService tbHistoryEntityService;
    
    private final TbMusicService musicService;
    
    private final TbAlbumService albumService;
    
    private final TbCollectService collectService;
    
    private final TbArtistService artistService;
    
    private final TbMvInfoService mvInfoService;
    
    public PageResCommon<MusicHistoryRes> getPageHistory(String name, Integer current, Integer size) {
        org.springframework.data.domain.Page<TbHistoryEntity> tbHistoryEntities;
        PageRequest page = PageRequest.of(current - 1, size);
        if (StringUtils.isBlank(name)) {
            tbHistoryEntities = tbHistoryEntityService.list(page);
        } else {
            tbHistoryEntities = tbHistoryEntityService.listByNikeName(name,
                    page);
        }
        if (CollUtil.isEmpty(tbHistoryEntities)) {
            return new PageResCommon<>();
        }
        Map<Byte, List<TbHistoryEntity>> collect = tbHistoryEntities.stream()
                                                                    .collect(Collectors.toMap(TbHistoryEntity::getType,
                                                                            ListUtil::toList,
                                                                            (o1, o2) -> {
                                                                                o2.addAll(o1);
                                                                                return o2;
                                                                            }));
        
        Map<Byte, Map<Long, String>> maps = new HashMap<>();
        // 音乐
        List<Long> musicIds = Optional.ofNullable(collect.get(HistoryConstant.MUSIC))
                                      .orElse(new ArrayList<>())
                                      .stream()
                                      .map(TbHistoryEntity::getMiddleId)
                                      .toList();
        if (CollUtil.isNotEmpty(musicIds)) {
            List<TbMusicPojo> tbMusicPojos = musicService.listByIds(musicIds);
            Map<Long, String> musicMaps = tbMusicPojos.parallelStream().collect(Collectors.toMap(TbMusicPojo::getId, TbMusicPojo::getMusicName));
            maps.put(HistoryConstant.MUSIC, musicMaps);
        }
        // 专辑
        List<Long> albumIds = Optional.ofNullable(collect.get(HistoryConstant.ALBUM))
                                      .orElse(new ArrayList<>())
                                      .stream()
                                      .map(TbHistoryEntity::getMiddleId)
                                      .toList();
        if (CollUtil.isNotEmpty(albumIds)) {
            List<TbAlbumPojo> tbAlbumPojos = albumService.listByIds(albumIds);
            Map<Long, String> albumMaps = tbAlbumPojos.parallelStream().collect(Collectors.toMap(TbAlbumPojo::getId, TbAlbumPojo::getAlbumName));
            maps.put(HistoryConstant.ALBUM, albumMaps);
        }
        // 歌单
        List<Long> playListIds = Optional.ofNullable(collect.get(HistoryConstant.PLAYLIST))
                                         .orElse(new ArrayList<>())
                                         .stream()
                                         .map(TbHistoryEntity::getMiddleId)
                                         .toList();
        if (CollUtil.isNotEmpty(playListIds)) {
            List<TbCollectPojo> tbCollectPojos = collectService.listByIds(playListIds);
            Map<Long, String> playListMaps = tbCollectPojos.parallelStream()
                                                           .collect(Collectors.toMap(TbCollectPojo::getId, TbCollectPojo::getPlayListName));
            maps.put(HistoryConstant.PLAYLIST, playListMaps);
        }
        // 歌手
        List<Long> artistIds = Optional.ofNullable(collect.get(HistoryConstant.ARTIST))
                                       .orElse(new ArrayList<>())
                                       .stream()
                                       .map(TbHistoryEntity::getMiddleId)
                                       .toList();
        if (CollUtil.isNotEmpty(artistIds)) {
            List<TbArtistPojo> tbArtistPojos = artistService.listByIds(artistIds);
            Map<Long, String> artistMaps = tbArtistPojos.parallelStream().collect(Collectors.toMap(TbArtistPojo::getId, TbArtistPojo::getArtistName));
            maps.put(HistoryConstant.ARTIST, artistMaps);
        }
        // mv
        List<Long> mvIds = Optional.ofNullable(collect.get(HistoryConstant.MV))
                                   .orElse(new ArrayList<>())
                                   .stream()
                                   .map(TbHistoryEntity::getMiddleId)
                                   .toList();
        if (CollUtil.isNotEmpty(mvIds)) {
            List<TbMvInfoPojo> tbMvInfoPojos = mvInfoService.listByIds(mvIds);
            Map<Long, String> mvMaps = tbMvInfoPojos.parallelStream().collect(Collectors.toMap(TbMvInfoPojo::getId, TbMvInfoPojo::getTitle));
            maps.put(HistoryConstant.MV, mvMaps);
        }
        List<MusicHistoryRes> list = tbHistoryEntities.stream()
                                                      .map(s -> {
                                                          SysUserEntity user = s.getSysUserByUserId();
                                                          Byte type = s.getType();
                                                          Map<Long, String> longStringMap = maps.get(type);
                                                          return new MusicHistoryRes(s.getId(),
                                                                  longStringMap.get(s.getMiddleId()),
                                                                  s.getMiddleId(),
                                                                  type,
                                                                  s.getCount(),
                                                                  s.getPlayedTime(),
                                                                  user.getId(),
                                                                  user.getNickname(),
                                                                  s.getUpdateTime().toLocalDateTime());
                                                      }).toList();
        return new PageResCommon<>(tbHistoryEntities.getNumber() + 1, tbHistoryEntities.getSize(), tbHistoryEntities.getTotalElements(), list);
    }
    
    public void deletePageHistory(List<Long> ids) {
        tbHistoryEntityService.delete(ids);
    }
}

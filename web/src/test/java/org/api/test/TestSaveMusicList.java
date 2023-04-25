package org.api.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.service.MusicFlowApi;
import org.api.neteasecloudmusic.service.CollectApi;
import org.api.utils.RequestMusic163;
import org.core.pojo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.MusicBoxSpringBoot;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = MusicBoxSpringBoot.class)
@Slf4j
class TestSaveMusicList {
    
    static String cookie = "MUSIC_U=d33658da9213990dece8c775a34a34c50a72fdf0cc97532e1e2f6d7efc8affd3519e07624a9f00535f3dd833cb266a5025ff223deb3065a43726809422c6334bdebf8de6ed45b634d4dbf082a8813684";
    // 本地用户音乐
    long localUserId = 403648304906373L;
    @Autowired
    private MusicFlowApi musicFlowApi;
    @Autowired
    private CollectApi collectApi;
    
    /**
     * 添加歌单到数据库
     */
    @Test
    void testUploadUserPlayListMusic() {
        // 第三方用户音乐
        String playId = "7234346265";
        saveUserPlayListMusicList(playId, localUserId);
    }
    
    /**
     * 添加喜欢歌单到数据库(无顺序)
     */
    @Test
    void testUploadUserLikeMusic() {
        // 第三方用户ID
        String playId = "1815109509";
        saveUserLikeMusicList(playId, localUserId);
    }
    
    /**
     * 添加音乐曲库
     */
    @Test
    void testUploadMusic() {
        // 第三方音乐ID
        Long musicId = 190072L;
        List<Map<String, Object>> songUrlMap = RequestMusic163.getSongUrl(Collections.singletonList(musicId), cookie, 1);
        Map<Long, Map<String, Object>> musicUrl = songUrlMap.parallelStream()
                                                         .collect(Collectors.toMap(stringObjectMap -> MapUtil.getLong(stringObjectMap, "id"),
                                                                 stringObjectMap -> stringObjectMap));
        List<Map<String, Object>> songDetail = RequestMusic163.getSongDetail(Collections.singletonList(musicId), cookie);
        for (Map<String, Object> song : songDetail) {
            MusicDetails musicDetails = TestUploadMusicApi.saveMusicInfo(musicUrl, song, cookie, musicFlowApi, localUserId);
            log.info(musicDetails.toString());
        }
    }
    
    
    /**
     * 添加音乐到歌单
     *
     * @param playListId 用户ID
     */
    private void saveUserPlayListMusicList(String playListId, Long localUserId) {
        List<Long> playDetail = RequestMusic163.getPlayDetail(playListId, cookie);
        // 反转歌曲ID顺序，保持添加歌曲顺序
        Collections.reverse(playDetail);
        // 保存音乐到本地数据库，并返回保存的音乐信息
        List<MusicDetails> musicPojoList = TestUploadMusicApi.saveMusicInfoList(playDetail, cookie, musicFlowApi, localUserId);
        // 创建歌单
        TbCollectPojo collectApiPlayList = collectApi.createPlayList(localUserId, "导入歌单");
        List<Long> musicIds = musicPojoList.stream().map(MusicDetails::getMusic).map(TbMusicPojo::getId).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(musicIds)) {
            collectApi.addSongToCollect(localUserId, collectApiPlayList.getId(), musicIds, true);
            log.info("添加到用户歌单成功");
        }
        for (MusicDetails tbMusicPojo : musicPojoList) {
            // 防止空指针
            tbMusicPojo.setMusic(Optional.ofNullable(tbMusicPojo.getMusic()).orElse(new TbMusicPojo()));
            tbMusicPojo.setAlbum(Optional.ofNullable(tbMusicPojo.getAlbum()).orElse(new TbAlbumPojo()));
            tbMusicPojo.setSinger(Optional.ofNullable(tbMusicPojo.getSinger()).orElse(new ArrayList<>()));
            tbMusicPojo.setMusicUrl(Optional.ofNullable(tbMusicPojo.getMusicUrl()).orElse(new TbMusicUrlPojo()));
            
            log.info("添加音乐：{}\tID:{}", tbMusicPojo.getMusic().getMusicName(), tbMusicPojo.getMusic().getId());
            log.info("添加专辑：{}\tID:{}", tbMusicPojo.getAlbum().getAlbumName(), tbMusicPojo.getAlbum().getId());
            log.info("添加音乐下载地址：{}\tID:{}", tbMusicPojo.getMusicUrl().getUrl(), tbMusicPojo.getMusicUrl().getId());
            for (TbArtistPojo tbArtistPojo : tbMusicPojo.getSinger()) {
                log.info("添加歌手：{}\tID:{}", tbArtistPojo.getArtistName(), tbArtistPojo.getId());
            }
        }
        log.info("一共存储{}个音乐信息", musicPojoList.size());
    }
    
    /**
     * 添加喜欢音乐歌单
     *
     * @param userID 用户ID
     */
    private void saveUserLikeMusicList(String userID, Long localUserId) {
        List<Long> like = RequestMusic163.like(userID, cookie);
        List<MusicDetails> musicPojoList = TestUploadMusicApi.saveMusicInfoList(like, cookie, musicFlowApi, localUserId);
        for (MusicDetails tbMusicPojo : musicPojoList) {
            if (tbMusicPojo.getMusicUrl() != null && tbMusicPojo.getMusic().getId() != null) {
                try {
                    collectApi.like(localUserId, tbMusicPojo.getMusic().getId(), true);
                } catch (Exception e) {
                    if (StringUtils.equals(e.getMessage(), "20010")) {
                        log.info("歌曲已保存");
                    } else {
                        throw new RuntimeException(e.getMessage());
                    }
                }
                log.info("添加到用户喜欢歌单成功");
            }
            // 防止空指针
            tbMusicPojo.setMusic(Optional.ofNullable(tbMusicPojo.getMusic()).orElse(new TbMusicPojo()));
            tbMusicPojo.setAlbum(Optional.ofNullable(tbMusicPojo.getAlbum()).orElse(new TbAlbumPojo()));
            tbMusicPojo.setSinger(Optional.ofNullable(tbMusicPojo.getSinger()).orElse(new ArrayList<>()));
            tbMusicPojo.setMusicUrl(Optional.ofNullable(tbMusicPojo.getMusicUrl()).orElse(new TbMusicUrlPojo()));
            
            log.info("添加音乐：{}\tID:{}", tbMusicPojo.getMusic().getMusicName(), tbMusicPojo.getMusic().getId());
            log.info("添加专辑：{}\tID:{}", tbMusicPojo.getAlbum().getAlbumName(), tbMusicPojo.getAlbum().getId());
            log.info("添加音乐下载地址：{}\tID:{}", tbMusicPojo.getMusicUrl().getUrl(), tbMusicPojo.getMusicUrl().getId());
            for (TbArtistPojo tbArtistPojo : tbMusicPojo.getSinger()) {
                log.info("添加歌手：{}\tID:{}", tbArtistPojo.getArtistName(), tbArtistPojo.getId());
            }
        }
        log.info("一共存储{}个音乐信息以下是音乐名", musicPojoList.size());
        musicPojoList.stream().map(MusicDetails::getMusic).map(TbMusicPojo::getMusicName).forEach(System.out::println);
    }
}

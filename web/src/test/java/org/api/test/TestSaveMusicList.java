package org.api.test;

import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.service.MusicFlowApi;
import org.api.model.LikePlay;
import org.api.model.playlistdetail.PlayListDetailRes;
import org.api.model.playlistdetail.TrackIdsItem;
import org.api.model.song.SongDetail;
import org.api.model.song.SongsItem;
import org.api.model.url.DataItem;
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
     * 添加音乐到歌单
     */
    @Test
    void testUploadUserPlayListMusic() {
        // 第三方用户音乐
        Long playId = 7234346265L;
        saveUserPlayListMusicList(playId, localUserId);
    }
    
    /**
     * 添加音乐到用户喜欢表
     */
    @Test
    void testUploadUserLikeMusic() {
        // 第三方用户音乐
        String playId = "6389917304";
        saveUserLikeMusicList(playId, localUserId);
    }
    
    /**
     * 添加音乐曲库
     */
    @Test
    void testUploadMusic() {
        // 第三方音乐ID
        Long musicId = 31365604L;
        Map<Integer, DataItem> songUrlMap = new HashMap<>();
        SongDetail songDetail = RequestMusic163.getSongDetail(List.of(musicId), cookie);
        for (SongsItem song : songDetail.getSongs()) {
            MusicDetails musicDetails = new TestUploadMusicApi().saveMusicInfo(songUrlMap, song, cookie, musicFlowApi, localUserId);
            log.info(musicDetails.toString());
        }
    }
    
    
    /**
     * 添加音乐到歌单
     *
     * @param playListId 用户ID
     */
    private void saveUserPlayListMusicList(Long playListId, Long localUserId) {
        PlayListDetailRes playDetail = RequestMusic163.getPlayDetail(String.valueOf(playListId), cookie);
        List<Long> collect = playDetail.getPlaylist().getTrackIds().stream().map(TrackIdsItem::getId).collect(Collectors.toList());
        // 反转歌曲ID顺序，保持添加歌曲顺序
        Collections.reverse(collect);
        // 保存音乐到本地数据库，并返回保存的音乐信息
        List<MusicDetails> musicPojoList = new TestUploadMusicApi().saveMusicInfoList(collect, cookie, musicFlowApi, localUserId);
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
        LikePlay like = RequestMusic163.like(userID, cookie);
        List<MusicDetails> musicPojoList = new TestUploadMusicApi().saveMusicInfoList(like.getIds(), cookie, musicFlowApi, localUserId);
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

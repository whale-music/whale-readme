package org.api.test;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.api.admin.UploadMusicApi;
import org.api.admin.dto.AlbumDto;
import org.api.admin.dto.AudioInfoDto;
import org.api.admin.dto.SingerDto;
import org.api.model.LikePlay;
import org.api.model.song.ArItem;
import org.api.model.song.SongDetail;
import org.api.model.song.SongsItem;
import org.api.utils.RequestMusic163;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.MusicBoxSpringBoot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = MusicBoxSpringBoot.class)
@Slf4j
public class TestUploadMusicApiTest {
    @Autowired
    private UploadMusicApi uploadMusicApi;
    
    @Test
    public void testUploadMusic() throws Exception {
        String playId = "290155277";
        String cookie = "MUSIC_U=afceb1d1edc22023fb24b900e2d84b1a3356f40b4c95cdc3332c107e08cf9238993166e004087dd3beb58a6aba726f0f3ce603cf8cd7f4cb6a1b92b9f0e5594e079514a26eb961c1a0d2166338885bd7;Cookie=afceb1d1edc22023fb24b900e2d84b1a333548a2160f671af20326e301054352993166e004087dd3be7a66a39c9296f7340dcce28358eb276a1b92b9f0e5594e079514a26eb961c1a89fe7c55eac81f3; MUSIC_U=afceb1d1edc22023fb24b900e2d84b1a333548a2160f671af20326e301054352993166e004087dd3be7a66a39c9296f7340dcce28358eb276a1b92b9f0e5594e079514a26eb961c1a89fe7c55eac81f3; __csrf=44f2af698fda832550f8933f7323e62f; __remember_me=true";
        LikePlay like = RequestMusic163.like(playId, cookie);
        int allPageIndex = PageUtil.totalPage(like.getIds().size(), 20);
    
        for (int i = 0; i < allPageIndex; i++) {
            List<Integer> page = ListUtil.page(i, 20, like.getIds());
            SongDetail songDetail = RequestMusic163.getSongDetail(page, cookie);
            for (SongsItem song : songDetail.getSongs()) {
                AudioInfoDto dto = new AudioInfoDto();
                AlbumDto album = new AlbumDto();
                album.setAlbumName(song.getAl().getName());
                album.setPic(song.getAl().getPicUrl());
                dto.setAlbum(album);
            
                dto.setMusicName(song.getName());
                dto.setAliaName(song.getAlia());
                dto.setTimeLength(song.getDt());
                ArrayList<SingerDto> singer = new ArrayList<>();
                for (ArItem arItem : song.getAr()) {
                    SingerDto e = new SingerDto();
                    e.setSingerName(arItem.getName());
                    singer.add(e);
                }
                dto.setSinger(singer);
                try {
                    uploadMusicApi.saveMusicInfo(dto);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
            }
        }
    }
}

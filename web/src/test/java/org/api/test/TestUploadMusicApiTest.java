package org.api.test;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.dto.AlbumDto;
import org.api.admin.model.dto.AudioInfoDto;
import org.api.admin.model.dto.SingerDto;
import org.api.admin.service.UploadMusicApi;
import org.api.model.LikePlay;
import org.api.model.album.Album;
import org.api.model.album.AlbumRes;
import org.api.model.lyric.Lyric;
import org.api.model.singer.Artist;
import org.api.model.singer.Data;
import org.api.model.singer.SingerRes;
import org.api.model.song.ArItem;
import org.api.model.song.SongDetail;
import org.api.model.song.SongsItem;
import org.api.model.url.DataItem;
import org.api.model.url.SongUrl;
import org.api.utils.RequestMusic163;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.web.MusicBoxSpringBoot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest(classes = MusicBoxSpringBoot.class)
@Slf4j
class TestUploadMusicApiTest {
    @Autowired
    private UploadMusicApi uploadMusicApi;
    
    @Test
    void testUploadMusic() {
        String playId = "6389917304";
        String cookie = "MUSIC_U=d33658da9213990dece8c775a34a34c50a72fdf0cc97532e1e2f6d7efc8affd3519e07624a9f00535f3dd833cb266a5025ff223deb3065a43726809422c6334bdebf8de6ed45b634d4dbf082a8813684";
        LikePlay like = RequestMusic163.like(playId, cookie);
        int allPageIndex = PageUtil.totalPage(like.getIds().size(), 20);
        
        for (int i = 0; i < allPageIndex; i++) {
            List<Integer> page = ListUtil.page(i, 20, like.getIds());
            // 获取分页后的音乐数据
            SongDetail songDetail = RequestMusic163.getSongDetail(page, cookie);
            SongUrl songUrl = RequestMusic163.getSongUrl(page, cookie, 1);
            Map<Integer, DataItem> songUrlMap = songUrl.getData().stream().collect(Collectors.toMap(DataItem::getId, dataItem -> dataItem));
            for (SongsItem song : songDetail.getSongs()) {
                AudioInfoDto dto = new AudioInfoDto();
                AlbumDto album = new AlbumDto();
                album.setAlbumName(song.getAl().getName());
                album.setPic(song.getAl().getPicUrl());
                AlbumRes albumDto = RequestMusic163.getAlbumDto(song.getAl().getId(), cookie);
                Optional<Album> dtoAlbumOpt = Optional.ofNullable(albumDto.getAlbum());
                Album dtoAlbum = dtoAlbumOpt.orElse(new Album());
                album.setDescription(StringUtils.isNotBlank(dtoAlbum.getDescription()) ? dtoAlbum.getDescription() : null);
                
                dto.setAlbum(album);
                dto.setMusicName(song.getName());
                dto.setAliaName(song.getAlia());
                dto.setTimeLength(song.getDt());
                dto.setPic(dtoAlbum.getPicUrl());
                
                // 歌手
                ArrayList<SingerDto> singer = new ArrayList<>();
                for (ArItem arItem : song.getAr()) {
                    SingerDto singerDto = new SingerDto();
                    SingerRes singerInfo = RequestMusic163.getSingerInfo(arItem.getId(), cookie);
                    // 歌手名
                    singerDto.setSingerName(arItem.getName());
                    // 歌手别名
                    Data data = Optional.ofNullable(singerInfo.getData()).orElse(new Data());
                    // 歌手别名
                    Artist artist = Optional.ofNullable(data.getArtist()).orElse(new Artist());
                    singerDto.setAlias(StringUtils.join(artist.getAlias(), ","));
                    // 歌手封面
                    singerDto.setPic(artist.getCover());
                    // 歌手描述
                    singerDto.setIntroduction(artist.getBriefDesc());
                    singer.add(singerDto);
                }
                // 歌词
                try {
                    Lyric lyric = RequestMusic163.getLyric(song.getId(), cookie);
                    // 歌词
                    dto.setLyric(StringUtils.isNotBlank(lyric.getLrc().getLyric()) ? lyric.getLrc().getLyric() : null);
                    // 逐字歌词
                    dto.setKLyric(StringUtils.isNotBlank(lyric.getKlyric().getLyric()) ? lyric.getKlyric().getLyric() : null);
                } catch (Exception e) {
                    dto.setLyric("");
                    dto.setKLyric("");
                    log.warn(e.getMessage() + "网络错误");
                }
                dto.setSinger(singer);
                dto.setOrigin("163Music");
                // 获取歌曲md5值
                DataItem dataItem = songUrlMap.get(song.getId());
                if (dataItem != null && StringUtils.isNotBlank(dataItem.getUrl())) {
                    dto.setRate(dataItem.getBr());
                    dto.setType(dataItem.getType());
                    dto.setMd5(dataItem.getMd5());
                    dto.setLevel(dataItem.getLevel());
                    // 上传md5值
                    dto.setMusicTemp(dataItem.getMd5());
                    dto.setSize((long) dataItem.getSize());
                }
                dto.setUploadFlag(true);
                try {
                    uploadMusicApi.saveMusicInfo(dto);
                } catch (IOException e) {
                    log.warn(e.getMessage(), e);
                }
                log.info("上传成功{}:{}", song.getId(), dto.getMusicName());
            }
        }
    }
}

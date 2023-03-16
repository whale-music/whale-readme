package org.api.test;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.PageUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.model.req.SingerReq;
import org.api.admin.service.UploadMusicApi;
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
import org.core.pojo.MusicDetails;
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
class TestUploadMusicApi {
    
    
    /**
     * 保存音乐List
     *
     * @param musicIds 音乐ID
     */
    public List<MusicDetails> saveMusicInfoList(List<Integer> musicIds, String cookie, UploadMusicApi uploadMusicApi, Long userId) {
        int allPageIndex = PageUtil.totalPage(musicIds.size(), 20);
        List<MusicDetails> tbMusicPojos = new ArrayList<>();
        for (int i = 0; i < allPageIndex; i++) {
            List<Integer> page = ListUtil.page(i, 20, musicIds);
            // 获取分页后的音乐数据
            SongDetail songDetail = RequestMusic163.getSongDetail(page, cookie);
            // 获取歌曲下载地址数据
            SongUrl songUrl = RequestMusic163.getSongUrl(page, cookie, 1);
            
            // 歌曲下载地址信息
            Map<Integer, DataItem> songUrlMap = songUrl.getData().stream().collect(Collectors.toMap(DataItem::getId, dataItem -> dataItem));
            for (SongsItem song : songDetail.getSongs()) {
                MusicDetails musicDetails = saveMusicInfo(songUrlMap, song, cookie, uploadMusicApi, userId);
                tbMusicPojos.add(musicDetails);
            }
        }
        return tbMusicPojos;
    }
    
    private MusicDetails saveMusicInfo(Map<Integer, DataItem> songUrlMap, SongsItem song, String cookie, UploadMusicApi uploadMusicApi, Long userId) {
        AudioInfoReq dto = new AudioInfoReq();
        // 测试时使用用户ID
        dto.setUserId(userId);
        
        // 专辑
        AlbumReq album = new AlbumReq();
        album.setAlbumName(song.getAl().getName());
        album.setPic(song.getAl().getPicUrl());
        AlbumRes albumDto = RequestMusic163.getAlbumDto(song.getAl().getId(), cookie);
        Optional<Album> dtoAlbumOpt = Optional.ofNullable(albumDto.getAlbum());
        Album dtoAlbum = dtoAlbumOpt.orElse(new Album());
        album.setCompany(dtoAlbum.getCompany());
        if (dtoAlbum.getPublishTime() != null && dtoAlbum.getPublishTime() != 0) {
            album.setPublishTime(LocalDateTimeUtil.of(dtoAlbum.getPublishTime()));
        }
        album.setDescription(StringUtils.isNotBlank(dtoAlbum.getDescription()) ? dtoAlbum.getDescription() : null);
        dto.setAlbum(album);
        
        dto.setMusicName(song.getName());
        dto.setAliaName(song.getAlia());
        dto.setTimeLength(song.getDt());
        dto.setPic(dtoAlbum.getPicUrl());
        
        // 歌手
        ArrayList<SingerReq> singer = new ArrayList<>();
        for (ArItem arItem : song.getAr()) {
            SingerReq singerReq = new SingerReq();
            SingerRes singerInfo = RequestMusic163.getSingerInfo(arItem.getId(), TestSaveMusicList.cookie);
            // 歌手名
            singerReq.setSingerName(arItem.getName());
            // 歌手别名
            Data data = Optional.ofNullable(singerInfo.getData()).orElse(new Data());
            // 歌手别名
            Artist artist = Optional.ofNullable(data.getArtist()).orElse(new Artist());
            singerReq.setAlias(StringUtils.join(artist.getAlias(), ","));
            // 歌手封面
            singerReq.setPic(artist.getCover());
            // 歌手描述
            singerReq.setIntroduction(artist.getBriefDesc());
            singer.add(singerReq);
        }
        // 歌词
        try {
            Lyric lyric = RequestMusic163.getLyric(song.getId(), TestSaveMusicList.cookie);
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
            return uploadMusicApi.saveMusicInfo(dto);
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        log.info("上传成功{}:{}", song.getId(), dto.getMusicName());
        return null;
    }
}

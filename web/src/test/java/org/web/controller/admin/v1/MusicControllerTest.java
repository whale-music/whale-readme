package org.web.controller.admin.v1;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.req.ArtistReq;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.core.common.result.R;
import org.core.iservice.TbAlbumService;
import org.core.iservice.TbArtistService;
import org.core.iservice.TbMusicService;
import org.core.model.convert.PicConvert;
import org.core.pojo.*;
import org.core.service.AccountService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@SpringBootTest
class MusicControllerTest {
    public static final String ADMIN = "admin";
    @Autowired
    MusicFlowApi uploadMusic;
    @Autowired
    MusicController musicController;
    @Autowired
    private TbMusicService musicService;
    @Autowired
    private TbAlbumService albumService;
    @Autowired
    private TbArtistService artistService;
    @Autowired
    private AccountService accountService;
    
    @Test
    @BeforeEach
    void cleanAllData() {
        artistService.removeByIds(artistService.list().parallelStream().map(TbArtistPojo::getId).collect(Collectors.toList()));
        musicService.removeByIds(musicService.list().parallelStream().map(TbMusicPojo::getId).collect(Collectors.toList()));
        albumService.removeByIds(albumService.list().parallelStream().map(TbAlbumPojo::getId).collect(Collectors.toList()));
        
        long count = musicService.count();
        long count1 = artistService.count();
        long count2 = albumService.count();
        Assertions.assertEquals(0L, count);
        Assertions.assertEquals(0L, count1);
        Assertions.assertEquals(0L, count2);
    }
    
    @Test
    void testUploadMusicInfo() throws InterruptedException {
        SysUserPojo user = accountService.getUser(ADMIN);
        
        final int number = 100;
        ExecutorService executorService = ThreadUtil.newFixedExecutor(10, 1000, "test-upload-music-info", true);
        Faker faker = new Faker(Locale.SIMPLIFIED_CHINESE);
        ArrayList<Callable<MusicDetails>> tasks = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            tasks.add(new Callable<MusicDetails>() {
                /**
                 * Computes a result, or throws an exception if unable to do so.
                 *
                 * @return computed result
                 */
                @Override
                public MusicDetails call() {
                    return saveMusicInfo(user, faker);
                }
            });
        }
        executorService.invokeAll(tasks);
    }
    
    private MusicDetails saveMusicInfo(SysUserPojo user, Faker faker) {
        AudioInfoReq dto = new AudioInfoReq();
        dto.setMusicName(faker.name().name());
        dto.setAliaName(Arrays.asList(faker.name().name(), faker.name().name()));
        PicConvert tbPicPojo = new PicConvert();
        tbPicPojo.setUrl(faker.avatar().image());
        dto.setPic(tbPicPojo);
        dto.setType(faker.beer().style());
        dto.setTimeLength(1000000);
        
        ArrayList<ArtistReq> artists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ArtistReq e = new ArtistReq();
            e.setSex(RandomUtils.nextBoolean() ? "男" : "女");
            e.setBirth(faker.date().birthday().toLocalDateTime().toLocalDate());
            e.setLocation(faker.address().cityName());
            e.setAliasName(faker.artist().name());
            e.setArtistName(faker.artist().name());
            e.setIntroduction(faker.text().text());
            PicConvert artistPicPojo = new PicConvert();
            artistPicPojo.setUrl(faker.avatar().image());
            e.setPic(artistPicPojo);
            artists.add(e);
        }
        dto.setArtists(artists);
        
        AlbumReq album = new AlbumReq();
        PicConvert albumPic = new PicConvert();
        albumPic.setUrl(faker.avatar().image());
        album.setPic(albumPic);
        album.setAlbumName(faker.name().name());
        album.setCompany(faker.company().name());
        album.setPublishTime(faker.date().birthday().toLocalDateTime());
        album.setDescription(faker.text().text());
        dto.setAlbum(album);
        
        dto.setUploadFlag(true);
        dto.setUserId(user.getId());
        R result = musicController.uploadMusicInfo(dto);
        MusicDetails data = (MusicDetails) result.getData();
        TbMusicPojo music = data.getMusic();
        Assertions.assertEquals(music.getMusicName(), dto.getMusicName());
        Assertions.assertEquals(music.getAliasName(), CollUtil.join(dto.getAliaName(), ","));
        Assertions.assertEquals(music.getTimeLength(), dto.getTimeLength());
        
        TbAlbumPojo album1 = data.getAlbum();
        Assertions.assertEquals(album1.getAlbumName(), dto.getAlbum().getAlbumName());
        Assertions.assertEquals(album1.getCompany(), dto.getAlbum().getCompany());
        Assertions.assertEquals(album1.getDescription(), dto.getAlbum().getDescription());
        Assertions.assertEquals(album1.getSubType(), dto.getAlbum().getSubType());
        
        List<TbArtistPojo> singer = data.getSinger();
        List<ArtistReq> artists1 = dto.getArtists();
        for (TbArtistPojo tbArtistPojo : singer) {
            boolean aliasNameFlag = false;
            boolean artistNameFlag = false;
            boolean introductionFlag = false;
            boolean locationFlag = false;
            boolean sexFlag = false;
            for (ArtistReq artistReq : artists1) {
                if (StringUtils.equals(artistReq.getAliasName(), tbArtistPojo.getAliasName())) {
                    aliasNameFlag = true;
                }
                if (StringUtils.equals(artistReq.getArtistName(), tbArtistPojo.getArtistName())) {
                    artistNameFlag = true;
                }
                if (StringUtils.equals(artistReq.getIntroduction(), tbArtistPojo.getIntroduction())) {
                    introductionFlag = true;
                }
                if (StringUtils.equals(artistReq.getLocation(), tbArtistPojo.getLocation())) {
                    locationFlag = true;
                }
                if (StringUtils.equals(artistReq.getSex(), tbArtistPojo.getSex())) {
                    sexFlag = true;
                }
            }
            Assertions.assertTrue(aliasNameFlag);
            Assertions.assertTrue(artistNameFlag);
            Assertions.assertTrue(introductionFlag);
            Assertions.assertTrue(locationFlag);
            Assertions.assertTrue(sexFlag);
        }
        return data;
    }
}
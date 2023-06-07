package org.web.controller.admin.v1;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.thread.ThreadUtil;
import net.datafaker.Faker;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.upload.AlbumInfoReq;
import org.api.admin.model.req.upload.ArtistInfoReq;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.service.AlbumApi;
import org.api.admin.service.ArtistApi;
import org.api.admin.service.MusicFlowApi;
import org.api.admin.service.PlayListApi;
import org.core.common.result.R;
import org.core.iservice.*;
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
    private TbCollectService collectService;
    
    @Autowired
    private MusicFlowApi musicFlowApi;
    
    @Autowired
    private ArtistApi artistApi;
    
    @Autowired
    private AlbumApi albumApi;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private PlayListApi playListApi;
    
    @Autowired
    private TbPicService picService;
    
    @Autowired
    private TbTagService tagService;
    
    @Test
    @BeforeEach
    void cleanAllData() {
        
        cleanMusicData();
        
        cleanAlbumData();
        
        cleanArtistData();
        
        cleanPlayList();
        
        long count = musicService.count();
        long count1 = artistService.count();
        long count2 = albumService.count();
        Assertions.assertEquals(0L, count);
        Assertions.assertEquals(0L, count1);
        Assertions.assertEquals(0L, count2);
        
        long tagCount = tagService.count();
        Assertions.assertEquals(0L, tagCount);
        
        long picCount = picService.count();
        Assertions.assertEquals(0L, picCount);
    }
    
    private void cleanPlayList() {
        SysUserPojo user = accountService.getUser(ADMIN);
        List<Long> collect = collectService.list().stream().map(TbCollectPojo::getId).collect(Collectors.toList());
        playListApi.deletePlayList(user.getId(), collect);
    }
    
    private void cleanArtistData() {
        List<Long> artistIds = artistService.list().parallelStream().map(TbArtistPojo::getId).collect(Collectors.toList());
        artistApi.deleteArtist(artistIds);
    }
    
    private void cleanAlbumData() {
        List<Long> albumIds = albumService.list().parallelStream().map(TbAlbumPojo::getId).collect(Collectors.toList());
        albumApi.deleteAlbum(albumIds, true);
    }
    
    private void cleanMusicData() {
        List<Long> musicIds = musicService.list().parallelStream().map(TbMusicPojo::getId).collect(Collectors.toList());
        musicFlowApi.deleteMusic(musicIds, true);
    }
    
    @Test
    void testUploadMusicInfo() throws InterruptedException {
        SysUserPojo user = accountService.getUser(ADMIN);
        
        final int number = 1_000;
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
    
        ArrayList<ArtistInfoReq> artists = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ArtistInfoReq e = new ArtistInfoReq();
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
    
        AlbumInfoReq album = new AlbumInfoReq();
        PicConvert albumPic = new PicConvert();
        albumPic.setUrl(faker.avatar().image());
        album.setPic(albumPic);
        album.setAlbumName(faker.name().name());
        album.setCompany(faker.company().name());
        album.setPublishTime(faker.date().birthday().toLocalDateTime());
        album.setDescription(faker.text().text());
        ArrayList<String> tags = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tags.add(faker.beer().style());
        }
        album.setTags(tags);
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
        List<ArtistInfoReq> artists1 = dto.getArtists();
        for (TbArtistPojo tbArtistPojo : singer) {
            boolean aliasNameFlag = false;
            boolean artistNameFlag = false;
            boolean introductionFlag = false;
            boolean locationFlag = false;
            boolean sexFlag = false;
            for (ArtistInfoReq artistReq : artists1) {
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
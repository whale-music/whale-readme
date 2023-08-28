package org.api.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.api.neteasecloudmusic.service.CollectApi;
import org.api.utils.RequestMusic163;
import org.core.mybatis.model.convert.PicConvert;
import org.core.mybatis.pojo.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;
import org.web.admin.AdminSpringBootApplication;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@SpringBootTest(classes = AdminSpringBootApplication.class)
@Slf4j
class TestSaveMusicList {
    
    static String COOKIE = "MUSIC_U=d33658da9213990dece8c775a34a34c50a72fdf0cc97532e1e2f6d7efc8affd3519e07624a9f00535f3dd833cb266a5025ff223deb3065a43726809422c6334bdebf8de6ed45b634d4dbf082a8813684";
    // 本地用户音乐
    long localUserId = 403648304906373L;
    @Autowired
    private MusicFlowApi musicFlowApi;
    @Autowired
    private CollectApi collectApi;
    
    /**
     * 保存音乐List
     *
     * @param musicIds 音乐ID
     */
    public static List<MusicDetails> saveMusicInfoList(List<Long> musicIds, String cookie, MusicFlowApi musicFlowApi, Long userId) {
        int allPageIndex = PageUtil.totalPage(musicIds.size(), 20);
        List<MusicDetails> tbMusicPojos = new ArrayList<>();
        for (int i = 0; i < allPageIndex; i++) {
            List<Long> page = ListUtil.page(i, 20, musicIds);
            // 获取分页后的音乐数据
            List<Map<String, Object>> songDetail = RequestMusic163.getSongDetail(page, cookie);
            // 获取歌曲下载地址数据
            List<Map<String, Object>> songUrl = RequestMusic163.getSongUrl(page, cookie, 1);
            Map<Long, Map<String, Object>> musicUrlMaps = new HashMap<>();
            for (Map<String, Object> stringObjectMap : songUrl) {
                if (CollUtil.isNotEmpty(stringObjectMap)) {
                    Long id = MapUtil.get(stringObjectMap, "id", Long.class);
                    musicUrlMaps.put(id, stringObjectMap);
                }
            }
            
            // 歌曲下载地址信息
            for (Map<String, Object> song : songDetail) {
                MusicDetails musicDetails = saveMusicInfo(musicUrlMaps, song, cookie, musicFlowApi, userId);
                tbMusicPojos.add(musicDetails);
            }
        }
        return tbMusicPojos;
    }
    
    /**
     * 保存音乐信息
     *
     * @param songUrlMap   歌曲URL
     * @param song         歌曲信息
     * @param cookie       cookie
     * @param musicFlowApi 服务类
     * @param userId       音乐信息
     * @return 保存成功的音乐信息
     */
    public static MusicDetails saveMusicInfo(Map<Long, Map<String, Object>> songUrlMap, Map<String, Object> song, String cookie, MusicFlowApi musicFlowApi, Long userId) {
        AudioInfoReq dto = new AudioInfoReq();
        // 测试时使用用户ID
        dto.setUserId(userId);
        
        // 专辑
        AudioInfoReq.AudioAlbum album = new AudioInfoReq.AudioAlbum();
        JSONObject albumMap = MapUtil.get(song, "al", JSONObject.class);
        Map<String, Object> albumDto = RequestMusic163.getAlbumDto(MapUtil.getInt(albumMap, "id"), cookie);
        album.setAlbumName(MapUtil.get(albumDto, "name", String.class));
        
        String blurPicUrl = MapUtil.getStr(albumDto, "blurPicUrl");
        PicConvert pic = new PicConvert();
        pic.setPath(blurPicUrl);
        album.setPic(pic);
        album.setSubType(MapUtil.getStr(albumDto, "subType"));
        album.setCompany(MapUtil.getStr(albumDto, "company"));
        Long publishTime = MapUtil.getLong(albumDto, "publishTime");
        if (publishTime != null && publishTime != 0) {
            album.setPublishTime(LocalDateTimeUtil.of(publishTime));
        }
        album.setDescription(MapUtil.getStr(albumDto, "description"));
        dto.setAlbum(album);
        
        dto.setMusic(new AudioInfoReq.AudioMusic());
        dto.getMusic().setMusicName(MapUtil.getStr(song, "name"));
        JSONArray alia = MapUtil.get(song, "alias", JSONArray.class, new JSONArray());
        dto.getMusic().setAliaName(alia.toList(String.class));
        dto.getMusic().setTimeLength(MapUtil.getInt(song, "dt"));
        
        PicConvert blurPic = new PicConvert();
        blurPic.setPath(blurPicUrl);
        dto.getMusic().setPic(blurPic);
        
        // 歌手
        ArrayList<AudioInfoReq.AudioArtist> singer = new ArrayList<>();
        JSONArray ar = MapUtil.get(song, "ar", JSONArray.class);
        for (Object arItem : ar) {
            JSONObject arItemMap = (JSONObject) arItem;
            AudioInfoReq.AudioArtist artistPojo = new AudioInfoReq.AudioArtist();
            Map<String, Object> data = new HashMap<>();
            Long id = MapUtil.getLong(arItemMap, "id");
            if (id != null && id != 0) {
                data = RequestMusic163.getSingerInfo(id, cookie);
            }
            JSONObject artist = MapUtil.get(data, "artist", JSONObject.class);
            JSONObject user = MapUtil.get(data, "user", JSONObject.class);
            // 歌手名
            artistPojo.setArtistName(MapUtil.getStr(arItemMap, "name"));
            // 歌手别名
            JSONArray transNames1 = MapUtil.get(artist, "transNames", JSONArray.class, new JSONArray());
            JSONArray transNames2 = MapUtil.get(artist, "alias", JSONArray.class, new JSONArray());
            List<String> alias = new ArrayList<>();
            alias.addAll(transNames1.stream().map(String::valueOf).toList());
            alias.addAll(transNames2.stream().map(String::valueOf).toList());
            artistPojo.setAliasName(CollUtil.join(alias, ","));
            // 歌手封面
            String avatar = MapUtil.getStr(artist, "avatar");
            PicConvert artistPic = new PicConvert();
            artistPic.setPath(avatar);
            artistPojo.setPic(artistPic);
            // 歌手描述
            artistPojo.setIntroduction(MapUtil.getStr(artist, "briefDesc"));
            Long birthday = MapUtil.getLong(user, "birthday");
            if (birthday != null && birthday > 0) {
                artistPojo.setBirth(LocalDateTimeUtil.of(birthday).toLocalDate());
            }
            // 地区 只有城市编码
            artistPojo.setLocation(CityPojo.city.get(MapUtil.getStr(user, "city")));
            // 性别
            Integer gender = MapUtil.getInt(user, "gender");
            if (gender != null) {
                if (gender == 1) {
                    artistPojo.setSex("男");
                }
                if (gender == 2) {
                    artistPojo.setSex("女");
                }
            }
            singer.add(artistPojo);
        }
        Long musicId = MapUtil.getLong(song, "id");
        // 歌词
        Map<String, String> lyricMap = RequestMusic163.getLyric(musicId, cookie);
        // 歌词
        dto.getMusic().setLyric(MapUtil.getStr(lyricMap, "lrc"));
        
        // 逐字歌词
        dto.getMusic().setKLyric(MapUtil.getStr(lyricMap, "klyric"));
        dto.setArtists(singer);
        
        // 获取歌曲md5值
        Map<String, Object> musicUrlMap = songUrlMap.get(musicId);
        String url = MapUtil.getStr(musicUrlMap, "url");
        dto.setUploadFlag(true);
        if (musicUrlMap != null && StringUtils.isNotBlank(url)) {
            ArrayList<AudioInfoReq.AudioSource> sources = new ArrayList<>();
            AudioInfoReq.AudioSource audioSource = new AudioInfoReq.AudioSource();
            
            audioSource.setRate(MapUtil.getInt(musicUrlMap, "br"));
            String md5 = MapUtil.getStr(musicUrlMap, "md5");
            String type = MapUtil.getStr(musicUrlMap, "type");
            audioSource.setEncodeType(type);
            audioSource.setMd5(md5);
            audioSource.setLevel(MapUtil.getStr(musicUrlMap, "level"));
            // 上传md5值，或音乐文件
            audioSource.setPathTemp(dto.getUploadFlag() ? md5 + "." + type : MapUtil.getStr(musicUrlMap, "url"));
            audioSource.setSize(MapUtil.getLong(musicUrlMap, "size"));
            
            TbOriginPojo origin = new TbOriginPojo();
            origin.setMusicId(musicId);
            origin.setOriginUrl("https://music.163.com/#/song?id=" + musicId);
            origin.setOrigin("163Music");
            
            audioSource.setOrigin(origin);
            sources.add(audioSource);
            dto.setSources(sources);
        }
        // true: 只存储到数据库，不上传
        // false: 读取本地数据或网络数据上传到数据库
        try {
            MusicDetails musicDetails = musicFlowApi.saveMusicInfo(dto);
            log.info("上传成功{}:{}", musicId, dto.getMusic().getMusicName());
            return musicDetails;
        } catch (Exception e) {
            log.warn(e.getMessage(), e);
        }
        throw new NullPointerException();
    }
    
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
     * 添加喜欢歌单到数据库(无顺序)
     */
    @Test
    void testDownloadMusicPic() {
        String savePath = "D:\\temp\\cover";
        // 第三方用户ID
        Long userId = 6389917304L;
        List<Long> likeIds = RequestMusic163.like(String.valueOf(userId), COOKIE);
        int pageTotal = PageUtil.totalPage(likeIds.size(), 20);
        for (int i = 0; i < pageTotal; i++) {
            List<Long> page = ListUtil.page(i, 20, likeIds);
            List<Map<String, Object>> songDetail = RequestMusic163.getSongDetail(page, COOKIE);
            songDetail.parallelStream().forEach(stringObjectMap -> {
                // 音乐
                LinkedHashMap o = (LinkedHashMap) stringObjectMap.get("al");
                String picUrl = o.get("picUrl") + "";
                String suffix = FileUtil.getSuffix(picUrl);
                try {
                    log.info("pic url {}", picUrl);
                    byte[] bytes = HttpUtil.downloadBytes(picUrl);
                    if (ArrayUtil.isNotEmpty(bytes)) {
                        String md5 = DigestUtils.md5DigestAsHex(bytes);
                        FileUtil.writeBytes(bytes, new File(savePath, md5 + "." + suffix));
                    }
                } catch (HttpException e) {
                    log.error(e.getMessage(), e);
                    // throw new RuntimeException(e);
                }
                
                // 歌手
                net.minidev.json.JSONArray ar = (net.minidev.json.JSONArray) (stringObjectMap.get("ar"));
                for (Object o1 : ar) {
                    LinkedHashMap artist = (LinkedHashMap) o1;
                    String artistId = artist.get("id") + "";
                    if (StringUtils.isNotBlank(artistId)) {
                        Map<String, Object> singerInfo = RequestMusic163.getSingerInfo(Long.valueOf(artistId), COOKIE);
                        if (CollUtil.isNotEmpty(singerInfo)) {
                            LinkedHashMap artistJson = (LinkedHashMap) singerInfo.get("artist");
                            String cover = artistJson.get("cover") + "";
                            String artistCoverSuffix = FileUtil.getSuffix(cover);
                            byte[] artistCover = HttpUtil.downloadBytes(cover);
                            String artistCoverMd5 = DigestUtils.md5DigestAsHex(artistCover);
                            FileUtil.writeBytes(artistCover, new File(savePath, artistCoverMd5 + "." + artistCoverSuffix));
                        }
                    }
                }
                
                // 专辑
            });
        }
        
        
    }
    
    /**
     * 添加音乐到歌单
     *
     * @param playListId 用户ID
     */
    private void saveUserPlayListMusicList(String playListId, Long localUserId) {
        List<Long> playDetail = RequestMusic163.getPlayDetail(playListId, COOKIE);
        // 反转歌曲ID顺序，保持添加歌曲顺序
        Collections.reverse(playDetail);
        // 保存音乐到本地数据库，并返回保存的音乐信息
        List<MusicDetails> musicPojoList = TestSaveMusicList.saveMusicInfoList(playDetail, COOKIE, musicFlowApi, localUserId);
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
            tbMusicPojo.setResource(Optional.ofNullable(tbMusicPojo.getResource()).orElse(new ArrayList<>()));
            
            log.info("添加音乐：{}\tID:{}", tbMusicPojo.getMusic().getMusicName(), tbMusicPojo.getMusic().getId());
            log.info("添加专辑：{}\tID:{}", tbMusicPojo.getAlbum().getAlbumName(), tbMusicPojo.getAlbum().getId());
            tbMusicPojo.getResource().forEach(tbResourcePojo -> {
                log.info("添加音乐下载地址：{}\tID:{}", tbResourcePojo.getPath(), tbResourcePojo.getId());
            });
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
        List<Long> like = RequestMusic163.like(userID, COOKIE);
        List<MusicDetails> musicPojoList = TestSaveMusicList.saveMusicInfoList(like, COOKIE, musicFlowApi, localUserId);
        for (MusicDetails tbMusicPojo : musicPojoList) {
            if (tbMusicPojo.getResource() != null && tbMusicPojo.getMusic().getId() != null) {
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
            tbMusicPojo.setResource(Optional.ofNullable(tbMusicPojo.getResource()).orElse(new ArrayList<>()));
            
            log.info("添加音乐：{}\tID:{}", tbMusicPojo.getMusic().getMusicName(), tbMusicPojo.getMusic().getId());
            log.info("添加专辑：{}\tID:{}", tbMusicPojo.getAlbum().getAlbumName(), tbMusicPojo.getAlbum().getId());
            tbMusicPojo.getResource().forEach(tbResourcePojo -> {
                log.info("添加音乐下载地址：{}\tID:{}", tbResourcePojo.getPath(), tbResourcePojo.getId());
            });
            for (TbArtistPojo tbArtistPojo : tbMusicPojo.getSinger()) {
                log.info("添加歌手：{}\tID:{}", tbArtistPojo.getArtistName(), tbArtistPojo.getId());
            }
        }
        log.info("一共存储{}个音乐信息以下是音乐名", musicPojoList.size());
        musicPojoList.stream().map(MusicDetails::getMusic).map(TbMusicPojo::getMusicName).forEach(System.out::println);
    }
    
    /**
     * 添加音乐曲库
     */
    @Test
    void testUploadMusic() {
        // 第三方音乐ID
        Long musicId = 190072L;
        List<Map<String, Object>> songUrlMap = RequestMusic163.getSongUrl(Collections.singletonList(musicId), COOKIE, 1);
        Map<Long, Map<String, Object>> musicUrl = songUrlMap.parallelStream()
                                                            .collect(Collectors.toMap(stringObjectMap -> MapUtil.getLong(stringObjectMap, "id"),
                                                                    stringObjectMap -> stringObjectMap));
        List<Map<String, Object>> songDetail = RequestMusic163.getSongDetail(Collections.singletonList(musicId), COOKIE);
        for (Map<String, Object> song : songDetail) {
            MusicDetails musicDetails = TestSaveMusicList.saveMusicInfo(musicUrl, song, COOKIE, musicFlowApi, localUserId);
            log.info(musicDetails.toString());
        }
    }
}

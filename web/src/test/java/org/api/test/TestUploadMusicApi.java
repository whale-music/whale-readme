package org.api.test;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.jayway.jsonpath.JsonPath;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.req.ArtistReq;
import org.api.admin.model.req.AudioInfoReq;
import org.api.admin.service.MusicFlowApi;
import org.api.utils.RequestMusic163;
import org.core.pojo.MusicDetails;
import org.springframework.cglib.beans.BeanMap;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
class TestUploadMusicApi {
    
    
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
        AlbumReq album = new AlbumReq();
        JSONObject albumMap = MapUtil.get(song, "al", JSONObject.class);
        Map<String, Object> albumDto = RequestMusic163.getAlbumDto(MapUtil.getInt(albumMap, "id"), cookie);
        album.setAlbumName(MapUtil.get(albumDto, "name", String.class));
        album.setPic(MapUtil.getStr(albumDto, "blurPicUrl"));
        album.setSubType(MapUtil.getStr(albumDto, "subType"));
        album.setCompany(MapUtil.getStr(albumDto, "company"));
        Long publishTime = MapUtil.getLong(albumDto, "publishTime");
        if (publishTime != null && publishTime != 0) {
            album.setPublishTime(LocalDateTimeUtil.of(publishTime));
        }
        album.setDescription(MapUtil.getStr(albumDto, "description"));
        dto.setAlbum(album);
        
        dto.setMusicName(MapUtil.getStr(song, "name"));
        JSONArray alia = MapUtil.get(song, "alias", JSONArray.class, new JSONArray());
        dto.setAliaName(alia.toList(String.class));
        dto.setTimeLength(MapUtil.getInt(song, "dt"));
        dto.setPic(MapUtil.getStr(albumDto, "blurPicUrl"));
        
        // 歌手
        ArrayList<ArtistReq> singer = new ArrayList<>();
        JSONArray ar = MapUtil.get(song, "ar", JSONArray.class);
        for (Object arItem : ar) {
            JSONObject arItemMap = (JSONObject) arItem;
            ArtistReq artistPojo = new ArtistReq();
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
            JSONArray transNames1 = MapUtil.get(artist, "transNames", JSONArray.class,new JSONArray());
            JSONArray transNames2 = MapUtil.get(artist, "alias", JSONArray.class,new JSONArray());
            List<String> alias = new ArrayList<>();
            alias.addAll(transNames1.stream().map(String::valueOf).collect(Collectors.toList()));
            alias.addAll(transNames2.stream().map(String::valueOf).collect(Collectors.toList()));
            artistPojo.setAliasName(CollUtil.join(alias, ","));
            // 歌手封面
            artistPojo.setPic(MapUtil.getStr(artist, "avatar"));
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
        dto.setLyric(MapUtil.getStr(lyricMap, "lrc"));
        // 逐字歌词
        dto.setKLyric(MapUtil.getStr(lyricMap, "klyric"));
        dto.setArtists(singer);
        dto.setOrigin("163Music");
        // 获取歌曲md5值
        Map<String, Object> musicUrlMap = songUrlMap.get(musicId);
        String url = MapUtil.getStr(musicUrlMap, "url");
        dto.setUploadFlag(true);
        if (musicUrlMap != null && StringUtils.isNotBlank(url)) {
            dto.setRate(MapUtil.getInt(musicUrlMap, "br"));
            String md5 = MapUtil.getStr(musicUrlMap, "md5");
            String type = MapUtil.getStr(musicUrlMap, "type");
            dto.setType(type);
            dto.setMd5(md5);
            dto.setLevel(MapUtil.getStr(musicUrlMap, "level"));
            // 上传md5值，或音乐文件
            dto.setMusicTemp(dto.getUploadFlag() ? md5 + "." + type : MapUtil.getStr(musicUrlMap, "url"));
            dto.setSize(MapUtil.getLong(musicUrlMap, "size"));
        }
        // true: 只存储到数据库，不上传
        // false: 读取本地数据或网络数据上传到数据库
        try {
            MusicDetails musicDetails = musicFlowApi.saveMusicInfo(dto);
            log.info("上传成功{}:{}", musicId, dto.getMusicName());
            return musicDetails;
        } catch (IOException e) {
            log.warn(e.getMessage(), e);
        }
        throw new NullPointerException();
    }
}

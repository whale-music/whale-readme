package org.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.AlbumReq;
import org.api.admin.model.req.ArtistReq;
import org.api.admin.model.req.AudioInfoReq;
import org.core.pojo.MusicDetails;
import org.plugin.common.Func;
import org.plugin.converter.PluginLabelValue;

import java.util.*;
import java.util.stream.Collectors;


class SyncPlugin implements Func {
    
    public static final String MUSIC_ID_KEY = "musicId";
    public static final String HOST_KEY = "host";
    public static final String USER_ID_KEY = "userId";
    public static final String COOKIE_KEY = "cookie";
    private static String host = "";
    private static String cookie = "";
    
    private static String getValue(List<PluginLabelValue> values, String key) {
        Optional<PluginLabelValue> first = values.parallelStream()
                                                 .filter(pluginLabelValue -> StringUtils.equals(key, pluginLabelValue.getKey()))
                                                 .findFirst();
        return first.orElseThrow().getValue();
        
    }
    
    /**
     * 通用请求
     */
    private static String req(String host, String cookie) {
        try (HttpResponse execute = HttpUtil.createGet(host).header("Cookie", cookie).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static Map<String, Map<String, Object>> getSongUrl(List<String> musicIds, String cookie, int flag) {
        if (flag == 0) {
            return getSongUrlV1(musicIds);
        }
        if (flag == 1) {
            return getSongUrlV2(musicIds);
        }
        throw new RuntimeException();
    }
    
    /**
     * 获取歌曲详情
     */
    public static Map<String, Map<String, Object>> getSongDetail(List<String> musicIds, String cookie) {
        String request = req(host + "/song/detail?ids=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        List<LinkedHashMap<String, Object>> read = JsonPath.read(request, "$.songs[*].['id','name','alia','al','ar','dt','publishTime']");
        HashMap<String, Map<String, Object>> maps = new HashMap<>();
        for (LinkedHashMap<String, Object> hashMap : read) {
            maps.put(String.valueOf(hashMap.get("id")), hashMap);
        }
        return maps;
    }
    
    /**
     * 获取专辑信息
     */
    public static Map<String, String> getAlbumDto(Integer albumId) {
        String request = req(host + "/album?id=" + albumId, cookie);
        return JsonPath.read(request, "$.album.['id','name','publishTime','subType','company','description']");
    }
    //
    
    /**
     * 获取歌曲下载地址
     */
    public static Map<String, Map<String, Object>> getSongUrlV1(List<String> musicIds) {
        String request = req(host + "/song/url?id=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        List<LinkedHashMap<String, Object>> read = JsonPath.read(request, "$.data[*].['id','md5','url','br','type','level','size']");
        return getMapKeyId(read);
    }
    
    /**
     * 获取歌曲下载地址
     */
    public static Map<String, Map<String, Object>> getSongUrlV2(List<String> musicIds) {
        String request = req(host + "/song/url/v1?id=" + ArrayUtil.join(musicIds.toArray(), ",") + "&level=hires", cookie);
        List<LinkedHashMap<String, Object>> read = JsonPath.read(request, "$.data[*].['id','md5','url','br','type','level','size']");
        return getMapKeyId(read);
    }
    
    private static HashMap<String, Map<String, Object>> getMapKeyId(List<LinkedHashMap<String, Object>> request) {
        HashMap<String, Map<String, Object>> maps = new HashMap<>();
        for (LinkedHashMap<String, Object> stringStringLinkedHashMap : request) {
            maps.put(String.valueOf(stringStringLinkedHashMap.get("id")), stringStringLinkedHashMap);
        }
        return maps;
    }
    
    /**
     * 获取歌曲作者信息
     */
    public static Map<String, Object> getSingerInfo(String singerId) {
        String request = req(host + "/artist/detail?id=" + singerId, cookie);
        return JsonPath.read(request, "$.data.artist.['id','name','alias','avatar','briefDesc']");
    }
    
    /**
     * 获取歌词
     */
    public static Map<String, String> getLyric(String musicId, String cookie) {
        String request = req(host + "/lyric/url?id=" + musicId, cookie);
        String lrc = JsonPath.read(request, "$.lrc.lyric");
        String klyric = JsonPath.read(request, "$.klyric.lyric");
        String tlyric = JsonPath.read(request, "$.tlyric.lyric");
        Map<String, String> hashMap = new HashMap();
        hashMap.put("lrc", lrc);
        hashMap.put("klyric", klyric);
        hashMap.put("tlyric", tlyric);
        return hashMap;
    }
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    @Override
    public List<PluginLabelValue> getParams() {
        List<PluginLabelValue> pluginLabelValues = new ArrayList<>();
        PluginLabelValue e = new PluginLabelValue();
        e.setLabel("Cookie");
        e.setKey(COOKIE_KEY);
        e.setValue("");
        pluginLabelValues.add(e);
        
        PluginLabelValue e1 = new PluginLabelValue();
        e1.setLabel("音乐ID");
        e1.setKey(MUSIC_ID_KEY);
        e1.setValue("");
        pluginLabelValues.add(e1);
        
        PluginLabelValue e2 = new PluginLabelValue();
        e2.setLabel(HOST_KEY);
        e2.setKey(HOST_KEY);
        e2.setValue("");
        pluginLabelValues.add(e2);
        
        PluginLabelValue e3 = new PluginLabelValue();
        e3.setLabel("本地用户ID");
        e3.setKey(USER_ID_KEY);
        e3.setValue("");
        pluginLabelValues.add(e3);
        return pluginLabelValues;
    }
    
    /**
     * 执行方法
     *
     * @param values        方法自定参数
     * @param pluginPackage 插件调用服务
     */
    @Override
    public void apply(List<PluginLabelValue> values, PluginPackage pluginPackage) {
        cookie = getValue(values, COOKIE_KEY);
        host = getValue(values, HOST_KEY);
        String musicId = getValue(values, MUSIC_ID_KEY);
        String userId = getValue(values, USER_ID_KEY);
        
        assert musicId != null;
        assert cookie != null;
        assert host != null;
        assert userId != null;
        // 获取歌曲下载地址数据
        List<String> musicIds = Collections.singletonList(musicId);
        Map<String, Map<String, Object>> songUrl = getSongUrl(musicIds, cookie, 1);
        
        // 歌曲下载地址信息
        Map<String, Map<String, Object>> songDetail = getSongDetail(musicIds, cookie);
        for (Map.Entry<String, Map<String, Object>> songDetailMap : songDetail.entrySet()) {
            MusicDetails musicDetails = saveMusicInfo(songUrl, songDetailMap.getValue(), pluginPackage, Long.valueOf(userId));
            pluginPackage.log(musicDetails.toString());
        }
    }
    
    public MusicDetails saveMusicInfo(Map<String, Map<String, Object>> songUrlMaps, Map<String, Object> song, PluginPackage pluginPackage, Long userId) {
        AudioInfoReq dto = new AudioInfoReq();
        // 测试时使用用户ID
        dto.setUserId(userId);
        
        // 专辑
        AlbumReq album = new AlbumReq();
        album.setAlbumName(song.get("name").toString());
        Map al = (Map) song.get("al");
        album.setPic(al.get("picUrl").toString());
        Map<String, String> albumDto = getAlbumDto((Integer) al.get("id"));
        album.setSubType(albumDto.get("subType"));
        album.setCompany(albumDto.get("company"));
        if (albumDto.get("publishTime") != null) {
            String publishTime1 = String.valueOf(albumDto.get("publishTime"));
            long publishTime = Long.parseLong(publishTime1);
            album.setPublishTime(LocalDateTimeUtil.of(publishTime));
        }
        album.setDescription(albumDto.get("description"));
        dto.setAlbum(album);
        
        dto.setMusicName(song.get("name").toString());
        
        String alia = JSON.toJSONString(song.get("alia"));
        JSONArray objects = JSON.parseArray(alia);
        List<String> collect = objects.stream().map(String::valueOf).collect(Collectors.toList());
        dto.setAliaName(collect);
        dto.setTimeLength(Integer.valueOf(String.valueOf(song.get("dt"))));
        dto.setPic(album.getPic());
        
        // 歌手
        ArrayList<ArtistReq> singer = new ArrayList<>();
        List ar = (List) song.get("ar");
        for (Object arItem : ar) {
            Map item = (Map) arItem;
            ArtistReq singerReq = new ArtistReq();
            Map<String, Object> artistMap = getSingerInfo(String.valueOf(item.get("id")));
            // 歌手名
            singerReq.setArtistName(String.valueOf(item.get("name")));
            // 歌手别名
            List alias = (List) artistMap.get("alias");
            // Object collect1 = alias.stream().map(String::valueOf).collect(Collectors.toList());
            singerReq.setAliasName(CollUtil.join(alias, ","));
            // 歌手封面
            singerReq.setPic(String.valueOf(artistMap.get("avatar")));
            // 歌手描述
            singerReq.setIntroduction(String.valueOf(artistMap.get("briefDesc")));
            singer.add(singerReq);
        }
        // 歌词
        Map<String, String> lyric = getLyric(String.valueOf(song.get("id")), cookie);
        // 歌词
        dto.setLyric(String.valueOf(lyric.get("lrc")));
        // 逐字歌词
        dto.setKLyric(String.valueOf(lyric.get("klyric")));
        // 未知歌词
        dto.setTLyric(String.valueOf(lyric.get("tlyric")));
        
        dto.setArtists(singer);
        dto.setOrigin("163Music");
        // 获取歌曲md5值
        Map<String, Object> songUrlMap = songUrlMaps.get(String.valueOf(song.get("id")));
        if (songUrlMap != null) {
            dto.setRate(Integer.valueOf(String.valueOf(songUrlMap.get("br"))));
            dto.setType(String.valueOf(songUrlMap.get("type")));
            // 上传md5值
            dto.setMd5(String.valueOf(songUrlMap.get("md5")));
            dto.setLevel(String.valueOf(songUrlMap.get("level")));
            dto.setMusicTemp(String.valueOf(songUrlMap.get("md5")));
            dto.setSize(Long.valueOf(String.valueOf(songUrlMap.get("size"))));
        }
        dto.setUploadFlag(true);
        try {
            return pluginPackage.saveMusic(dto);
        } catch (Exception e) {
            pluginPackage.log(e.getMessage(), e);
        }
        pluginPackage.log("上传成功{}:{}", String.valueOf(song.get("id")), dto.getMusicName());
        throw new IllegalStateException();
    }
}

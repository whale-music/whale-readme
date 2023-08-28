package org.plugin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.core.config.PluginType;
import org.core.mybatis.model.convert.PicConvert;
import org.core.mybatis.pojo.MusicDetails;
import org.core.mybatis.pojo.TbOriginPojo;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.plugin.common.CommonPlugin;
import org.plugin.converter.PluginLabelValue;

import java.util.*;
import java.util.stream.Collectors;


class SyncOneMusicPlugin implements CommonPlugin {
    
    
    public static final String MUSIC_ID_KEY = "musicId";
    public static final String HOST_KEY = "host";
    public static final String USER_ID_KEY = "userId";
    public static final String COOKIE_KEY = "cookie";
    public static final String IS_DOWNLOAD_UPLOAD = "is_download_upload";
    
    
    private PluginPackage pluginPackage;
    
    private String host = "";
    
    private boolean downloadUpload = false;
    
    /**
     * 获取插件类型
     * 普通插件
     * 交互插件
     * 聚合插件
     *
     * @return 插件类型
     */
    @Override
    public String getType() {
        return PluginType.COMMON;
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
        
        PluginLabelValue e4 = new PluginLabelValue();
        e4.setLabel("是否自动下载歌曲并上传(0: 否 1: 是)");
        e4.setKey(IS_DOWNLOAD_UPLOAD);
        e4.setValue("");
        pluginLabelValues.add(e4);
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
        this.pluginPackage = pluginPackage;
        String cookie = getValue(values, COOKIE_KEY);
        host = getValue(values, HOST_KEY);
        String musicId = getValue(values, MUSIC_ID_KEY);
        String userId = getValue(values, USER_ID_KEY);
        downloadUpload = StringUtils.equals(getValue(values, IS_DOWNLOAD_UPLOAD), "0");
        
        assert musicId != null;
        assert cookie != null;
        assert host != null;
        assert userId != null;
        List<Long> musicIds = Collections.singletonList(Long.valueOf(musicId));
        List<Map<String, Object>> songUrlMap = getSongUrlV2(musicIds, cookie);
        Map<Long, Map<String, Object>> musicUrl = songUrlMap.parallelStream()
                                                            .collect(Collectors.toMap(stringObjectMap -> MapUtil.getLong(stringObjectMap, "id"),
                                                                    stringObjectMap -> stringObjectMap));
        List<Map<String, Object>> songDetail = getSongDetail(musicIds, cookie);
        for (Map<String, Object> song : songDetail) {
            MusicDetails musicDetails = saveMusicInfo(musicUrl, song, cookie, Long.valueOf(userId));
            Assertions.assertNotNull(musicDetails);
        }
    }
    
    /**
     * 保存音乐信息
     *
     * @param songUrlMap 歌曲URL
     * @param song       歌曲信息
     * @param cookie     cookie
     * @param userId     音乐信息
     * @return 保存成功的音乐信息
     */
    public MusicDetails saveMusicInfo(Map<Long, Map<String, Object>> songUrlMap, Map<String, Object> song, String cookie, Long userId) {
        AudioInfoReq dto = new AudioInfoReq();
        // 测试时使用用户ID
        dto.setUserId(userId);
        
        // 专辑
        AudioInfoReq.AudioAlbum album = new AudioInfoReq.AudioAlbum();
        JSONObject albumMap = MapUtil.get(song, "al", JSONObject.class);
        Map<String, Object> albumDto = getAlbumDto(MapUtil.getInt(albumMap, "id"), cookie);
        album.setAlbumName(MapUtil.get(albumDto, "name", String.class));
        
        String blurPicUrl = MapUtil.getStr(albumDto, "blurPicUrl");
        PicConvert blurPic = new PicConvert();
        blurPic.setPath(blurPicUrl);
        album.setPic(blurPic);
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
        JSONArray alia = MapUtil.get(song, "alia", JSONArray.class, new JSONArray());
        dto.getMusic().setAliaName(alia.toList(String.class));
        dto.getMusic().setTimeLength(MapUtil.getInt(song, "dt"));
        
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
                data = getSingerInfo(id, cookie);
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
            artistPojo.setLocation(CityOneMusicPojo.city.get(MapUtil.getStr(user, "city")));
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
        Map<String, String> lyricMap = getLyric(musicId, cookie);
        // 歌词
        dto.getMusic().setLyric(MapUtil.getStr(lyricMap, "lrc"));
        // 逐字歌词
        dto.getMusic().setKLyric(MapUtil.getStr(lyricMap, "klyric"));
        dto.setArtists(singer);
        
        // 获取歌曲md5值
        Map<String, Object> musicUrlMap = songUrlMap.get(musicId);
        String url = MapUtil.getStr(musicUrlMap, "url");
        dto.setUploadFlag(downloadUpload);
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
            MusicDetails musicDetails = pluginPackage.saveMusic(dto);
            pluginPackage.logInfo("上传成功{}:{}", musicId, dto.getMusic().getMusicName());
            return musicDetails;
        } catch (Exception e) {
            pluginPackage.logError(e.getMessage(), e);
        }
        throw new NullPointerException();
    }
    
    /**
     * 通用请求
     */
    @NotNull
    private String req(String host, String cookie) {
        try (HttpResponse execute = HttpUtil.createGet(host).header("Cookie", cookie).execute()) {
            return execute.body();
        } catch (HttpException e) {
            throw new HttpException("http请求失败" + e);
        }
    }
    
    /**
     * 获取歌曲详情
     */
    public List<Map<String, Object>> getSongDetail(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/detail?ids=" + ArrayUtil.join(musicIds.toArray(), ","), cookie);
        List<Map<String, Object>> list = null;
        try {
            list = JsonPath.read(request, "$.songs");
        } catch (PathNotFoundException e) {
            pluginPackage.logError("歌曲详情: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return CollUtil.isEmpty(list) ? Collections.emptyList() : list;
    }
    
    /**
     * 获取专辑信息
     */
    public Map<String, Object> getAlbumDto(Integer albumId, String cookie) {
        String request = req(host + "/album?id=" + albumId, cookie);
        Map<String, Object> map = null;
        try {
            map = JsonPath.read(request, "$.album");
        } catch (PathNotFoundException e) {
            pluginPackage.logError("无专辑信息: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return map != null ? map : new HashMap<>();
    }
    
    /**
     * 获取歌曲下载地址
     */
    public List<Map<String, Object>> getSongUrlV2(List<Long> musicIds, String cookie) {
        String request = req(host + "/song/url/v1?id=" + ArrayUtil.join(musicIds.toArray(), ",") + "&level=hires", cookie);
        List<Map<String, Object>> map = null;
        try {
            map = JsonPath.read(request, "$.data");
        } catch (PathNotFoundException e) {
            pluginPackage.logError("获取音乐失败: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return CollUtil.isEmpty(map) ? Collections.emptyList() : map;
    }
    
    /**
     * 获取歌词
     */
    public Map<String, String> getLyric(Long musicId, String cookie) {
        String request = req(host + "/lyric?id=" + musicId, cookie);
        com.alibaba.fastjson2.JSONObject jsonObject = JSON.parseObject(request);
        com.alibaba.fastjson2.JSONObject lrc = com.alibaba.fastjson2.JSONObject.from(jsonObject.get("lrc"));
        String lyricStr = lrc.getString("lyric");
        
        com.alibaba.fastjson2.JSONObject klyric = com.alibaba.fastjson2.JSONObject.from(jsonObject.get("klyric"));
        String klyricStr = klyric.getString("lyric");
        
        // String lrc = JsonPath.read(request, "$.lrc.lyric");
        // String klyric = JsonPath.read(request, "$.klyric.lyric");
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("lrc", lyricStr);
        stringStringHashMap.put("klyric", klyricStr);
        return stringStringHashMap;
    }
    
    /**
     * 获取歌曲作者信息
     */
    public Map<String, Object> getSingerInfo(Long singerId, String cookie) {
        String request = req(host + "/artist/detail?id=" + singerId, cookie);
        Map<String, Object> map = null;
        try {
            map = JsonPath.read(request, "$.data");
        } catch (PathNotFoundException e) {
            pluginPackage.logError("无作者信息: {}", request);
        } catch (Exception e) {
            throw new RuntimeException();
        }
        return map != null ? map : new HashMap<>();
    }
    
}


class CityOneMusicPojo {
    public static Map<String, String> city = new HashMap<>();
    
    static {
        city.put("110000", "直辖市-北京市");
        city.put("120000", "直辖市-天津市");
        city.put("310000", "直辖市-上海市");
        city.put("500000", "直辖市-重庆市");
        
        city.put("810000", "特别行政区-香港");
        city.put("820000", "特别行政区-澳门");
        
        
        city.put("130100", "河北省-石家庄市");
        city.put("130200", "河北省-唐山市");
        city.put("130300", "河北省-秦皇岛市");
        city.put("130400", "河北省-邯郸市");
        city.put("130500", "河北省-邢台市");
        city.put("130600", "河北省-保定市");
        city.put("130700", "河北省-张家口市");
        city.put("130800", "河北省-承德市");
        city.put("130900", "河北省-沧州市");
        city.put("131000", "河北省-廊坊市");
        city.put("131100", "河北省-衡水市");
        
        
        city.put("140100", "山西省-太原市");
        city.put("140200", "山西省-大同市");
        city.put("140300", "山西省-阳泉市");
        city.put("140400", "山西省-长治市");
        city.put("140500", "山西省-晋城市");
        city.put("140600", "山西省-朔州市");
        city.put("140700", "山西省-晋中市");
        city.put("140800", "山西省-运城市");
        city.put("140900", "山西省-忻州市");
        city.put("141000", "山西省-临汾市");
        city.put("141100", "山西省-吕梁市");
        
        
        city.put("150100", "内蒙古-呼和浩特市");
        city.put("150200", "内蒙古-包头市");
        city.put("150300", "内蒙古-乌海市");
        city.put("150400", "内蒙古-赤峰市");
        city.put("150500", "内蒙古-通辽市");
        city.put("150600", "内蒙古-鄂尔多斯市");
        city.put("150700", "内蒙古-呼伦贝尔市");
        city.put("150800", "内蒙古-巴彦淖尔市");
        city.put("150900", "内蒙古-乌兰察布市");
        city.put("152200", "内蒙古-兴安盟");
        city.put("152500", "内蒙古-锡林郭勒盟");
        city.put("152900", "内蒙古-阿拉善盟");
        
        
        city.put("210100", "辽宁省-沈阳市");
        city.put("210200", "辽宁省-大连市");
        city.put("210300", "辽宁省-鞍山市");
        city.put("210400", "辽宁省-抚顺市");
        city.put("210500", "辽宁省-本溪市");
        city.put("210600", "辽宁省-丹东市");
        city.put("210700", "辽宁省-锦州市");
        city.put("210800", "辽宁省-营口市");
        city.put("210900", "辽宁省-阜新市");
        city.put("211000", "辽宁省-辽阳市");
        city.put("211100", "辽宁省-盘锦市");
        city.put("211200", "辽宁省-铁岭市");
        city.put("211300", "辽宁省-朝阳市");
        city.put("211400", "辽宁省-葫芦岛市");
        
        
        city.put("220100", "吉林省-长春市");
        city.put("220200", "吉林省-吉林市");
        city.put("220300", "吉林省-四平市");
        city.put("220400", "吉林省-辽源市");
        city.put("220500", "吉林省-通化市");
        city.put("220600", "吉林省-白山市");
        city.put("220700", "吉林省-松原市");
        city.put("220800", "吉林省-白城市");
        city.put("222400", "吉林省-延边朝鲜族自治州");
        
        
        city.put("230100", "黑龙江省-哈尔滨市");
        city.put("230200", "黑龙江省-齐齐哈尔市");
        city.put("230300", "黑龙江省-鸡西市");
        city.put("230400", "黑龙江省-鹤岗市");
        city.put("230500", "黑龙江省-双鸭山市");
        city.put("230600", "黑龙江省-大庆市");
        city.put("230700", "黑龙江省-伊春市");
        city.put("230800", "黑龙江省-佳木斯市");
        city.put("230900", "黑龙江省-七台河市");
        city.put("231000", "黑龙江省-牡丹江市");
        city.put("231100", "黑龙江省-黑河市");
        city.put("231200", "黑龙江省-绥化市");
        city.put("232700", "黑龙江省-大兴安岭地区");
        
        
        city.put("320100", "江苏省-南京市");
        city.put("320200", "江苏省-无锡市");
        city.put("320300", "江苏省-徐州市");
        city.put("320400", "江苏省-常州市");
        city.put("320500", "江苏省-苏州市");
        city.put("320600", "江苏省-南通市");
        city.put("320700", "江苏省-连云港市");
        city.put("320800", "江苏省-淮安市");
        city.put("320900", "江苏省-盐城市");
        city.put("321000", "江苏省-扬州市");
        city.put("321100", "江苏省-镇江市");
        city.put("321200", "江苏省-泰州市");
        city.put("321300", "江苏省-宿迁市");
        
        
        city.put("330100", "浙江省-杭州市");
        city.put("330200", "浙江省-宁波市");
        city.put("330300", "浙江省-温州市");
        city.put("330400", "浙江省-嘉兴市");
        city.put("330500", "浙江省-湖州市");
        city.put("330600", "浙江省-绍兴市");
        city.put("330700", "浙江省-金华市");
        city.put("330800", "浙江省-衢州市");
        city.put("330900", "浙江省-舟山市");
        city.put("331000", "浙江省-台州市");
        city.put("331100", "浙江省-丽水市");
        
        
        city.put("340100", "安徽省-合肥市");
        city.put("340200", "安徽省-芜湖市");
        city.put("340300", "安徽省-蚌埠市");
        city.put("340400", "安徽省-淮南市");
        city.put("340500", "安徽省-马鞍山市");
        city.put("340600", "安徽省-淮北市");
        city.put("340700", "安徽省-铜陵市");
        city.put("340800", "安徽省-安庆市");
        city.put("341000", "安徽省-黄山市");
        city.put("341100", "安徽省-滁州市");
        city.put("341200", "安徽省-阜阳市");
        city.put("341300", "安徽省-宿州市");
        city.put("341500", "安徽省-六安市");
        city.put("341600", "安徽省-亳州市");
        city.put("341700", "安徽省-池州市");
        city.put("341800", "安徽省-宣城市");
        
        
        city.put("350100", "福建省-福州市");
        city.put("350200", "福建省-厦门市");
        city.put("350300", "福建省-莆田市");
        city.put("350400", "福建省-三明市");
        city.put("350500", "福建省-泉州市");
        city.put("350600", "福建省-漳州市");
        city.put("350700", "福建省-南平市");
        city.put("350800", "福建省-龙岩市");
        city.put("350900", "福建省-宁德市");
        
        
        city.put("360100", "江西省-南昌市");
        city.put("360200", "江西省-景德镇市");
        city.put("360300", "江西省-萍乡市");
        city.put("360400", "江西省-九江市");
        city.put("360500", "江西省-新余市");
        city.put("360600", "江西省-鹰潭市");
        city.put("360700", "江西省-赣州市");
        city.put("360800", "江西省-吉安市");
        city.put("360900", "江西省-宜春市");
        city.put("361000", "江西省-抚州市");
        city.put("361100", "江西省-上饶市");
        
        
        city.put("370100", "山东省-济南市");
        city.put("370200", "山东省-青岛市");
        city.put("370300", "山东省-淄博市");
        city.put("370400", "山东省-枣庄市");
        city.put("370500", "山东省-东营市");
        city.put("370600", "山东省-烟台市");
        city.put("370700", "山东省-潍坊市");
        city.put("370800", "山东省-济宁市");
        city.put("370900", "山东省-泰安市");
        city.put("371000", "山东省-威海市");
        city.put("371100", "山东省-日照市");
        city.put("371200", "山东省-莱芜市");
        city.put("371300", "山东省-临沂市");
        city.put("371400", "山东省-德州市");
        city.put("371500", "山东省-聊城市");
        city.put("371600", "山东省-滨州市");
        city.put("371700", "山东省-菏泽市");
        
        
        city.put("410100", "河南省-郑州市");
        city.put("410200", "河南省-开封市");
        city.put("410300", "河南省-洛阳市");
        city.put("410400", "河南省-平顶山市");
        city.put("410500", "河南省-安阳市");
        city.put("410600", "河南省-鹤壁市");
        city.put("410700", "河南省-新乡市");
        city.put("410800", "河南省-焦作市");
        city.put("410900", "河南省-濮阳市");
        city.put("411000", "河南省-许昌市");
        city.put("411100", "河南省-漯河市");
        city.put("411200", "河南省-三门峡市");
        city.put("411300", "河南省-南阳市");
        city.put("411400", "河南省-商丘市");
        city.put("411500", "河南省-信阳市");
        city.put("411600", "河南省-周口市");
        city.put("411700", "河南省-驻马店市");
        city.put("419001", "河南省-济源市");
        
        
        city.put("420100", "湖北省-武汉市");
        city.put("420200", "湖北省-黄石市");
        city.put("420300", "湖北省-十堰市");
        city.put("420500", "湖北省-宜昌市");
        city.put("420600", "湖北省-襄阳市");
        city.put("420700", "湖北省-鄂州市");
        city.put("420800", "湖北省-荆门市");
        city.put("420900", "湖北省-孝感市");
        city.put("421000", "湖北省-荆州市");
        city.put("421100", "湖北省-黄冈市");
        city.put("421200", "湖北省-咸宁市");
        city.put("421300", "湖北省-随州市");
        city.put("422800", "湖北省-恩施土家族苗族自治州");
        city.put("429004", "湖北省-仙桃市");
        city.put("429005", "湖北省-潜江市");
        city.put("429006", "湖北省-天门市");
        city.put("429021", "湖北省-神农架林区");
        
        
        city.put("430100", "湖南省-长沙市");
        city.put("430200", "湖南省-株洲市");
        city.put("430300", "湖南省-湘潭市");
        city.put("430400", "湖南省-衡阳市");
        city.put("430500", "湖南省-邵阳市");
        city.put("430600", "湖南省-岳阳市");
        city.put("430700", "湖南省-常德市");
        city.put("430800", "湖南省-张家界市");
        city.put("430900", "湖南省-益阳市");
        city.put("431000", "湖南省-郴州市");
        city.put("431100", "湖南省-永州市");
        city.put("431200", "湖南省-怀化市");
        city.put("431300", "湖南省-娄底市");
        city.put("433100", "湖南省-湘西土家族苗族自治州");
        
        
        city.put("440100", "广东省-广州市");
        city.put("440200", "广东省-韶关市");
        city.put("440300", "广东省-深圳市");
        city.put("440400", "广东省-珠海市");
        city.put("440500", "广东省-汕头市");
        city.put("440600", "广东省-佛山市");
        city.put("440700", "广东省-江门市");
        city.put("440800", "广东省-湛江市");
        city.put("440900", "广东省-茂名市");
        city.put("441200", "广东省-肇庆市");
        city.put("441300", "广东省-惠州市");
        city.put("441400", "广东省-梅州市");
        city.put("441500", "广东省-汕尾市");
        city.put("441600", "广东省-河源市");
        city.put("441700", "广东省-阳江市");
        city.put("441800", "广东省-清远市");
        city.put("441900", "广东省-东莞市");
        city.put("442000", "广东省-中山市");
        city.put("445100", "广东省-潮州市");
        city.put("445200", "广东省-揭阳市");
        city.put("445300", "广东省-云浮市");
        
        
        city.put("450100", "广西-南宁市");
        city.put("450200", "广西-柳州市");
        city.put("450300", "广西-桂林市");
        city.put("450400", "广西-梧州市");
        city.put("450500", "广西-北海市");
        city.put("450600", "广西-防城港市");
        city.put("450700", "广西-钦州市");
        city.put("450800", "广西-贵港市");
        city.put("450900", "广西-玉林市");
        city.put("451000", "广西-百色市");
        city.put("451100", "广西-贺州市");
        city.put("451200", "广西-河池市");
        city.put("451300", "广西-来宾市");
        city.put("451400", "广西-崇左市");
        
        
        city.put("460100", "海南省-海口市");
        city.put("460200", "海南省-三亚市");
        city.put("460300", "海南省-三沙市");
        city.put("460400", "海南省-儋州市");
        city.put("469001", "海南省-五指山市");
        city.put("469002", "海南省-琼海市");
        city.put("469005", "海南省-文昌市");
        city.put("469006", "海南省-万宁市");
        city.put("469007", "海南省-东方市");
        city.put("469021", "海南省-定安县");
        city.put("469022", "海南省-屯昌县");
        city.put("469023", "海南省-澄迈县");
        city.put("469024", "海南省-临高县");
        city.put("469025", "海南省-白沙黎族自治县");
        city.put("469026", "海南省-昌江黎族自治县");
        city.put("469027", "海南省-乐东黎族自治县");
        city.put("469028", "海南省-陵水黎族自治县");
        city.put("469029", "海南省-保亭黎族苗族自治县");
        city.put("469030", "海南省-琼中黎族苗族自治县");
        
        
        city.put("510100", "四川省-成都市");
        city.put("510300", "四川省-自贡市");
        city.put("510400", "四川省-攀枝花市");
        city.put("510500", "四川省-泸州市");
        city.put("510600", "四川省-德阳市");
        city.put("510700", "四川省-绵阳市");
        city.put("510800", "四川省-广元市");
        city.put("510900", "四川省-遂宁市");
        city.put("511000", "四川省-内江市");
        city.put("511100", "四川省-乐山市");
        city.put("511300", "四川省-南充市");
        city.put("511400", "四川省-眉山市");
        city.put("511500", "四川省-宜宾市");
        city.put("511600", "四川省-广安市");
        city.put("511700", "四川省-达州市");
        city.put("511800", "四川省-雅安市");
        city.put("511900", "四川省-巴中市");
        city.put("512000", "四川省-资阳市");
        city.put("513200", "四川省-阿坝藏族羌族自治州");
        city.put("513300", "四川省-甘孜藏族自治州");
        city.put("513400", "四川省-凉山彝族自治州");
        
        
        city.put("520100", "贵州省-贵阳市");
        city.put("520200", "贵州省-六盘水市");
        city.put("520300", "贵州省-遵义市");
        city.put("520400", "贵州省-安顺市");
        city.put("520500", "贵州省-毕节市");
        city.put("520600", "贵州省-铜仁市");
        city.put("522300", "贵州省-黔西南布依族苗族自治州");
        city.put("522600", "贵州省-黔东南苗族侗族自治州");
        city.put("522700", "贵州省-黔南布依族苗族自治州");
        
        
        city.put("530100", "云南省-昆明市");
        city.put("530300", "云南省-曲靖市");
        city.put("530400", "云南省-玉溪市");
        city.put("530500", "云南省-保山市");
        city.put("530600", "云南省-昭通市");
        city.put("530700", "云南省-丽江市");
        city.put("530800", "云南省-普洱市");
        city.put("530900", "云南省-临沧市");
        city.put("532300", "云南省-楚雄彝族自治州");
        city.put("532500", "云南省-红河哈尼族彝族自治州");
        city.put("532600", "云南省-文山壮族苗族自治州");
        city.put("532800", "云南省-西双版纳傣族自治州");
        city.put("532900", "云南省-大理白族自治州");
        city.put("533100", "云南省-德宏傣族景颇族自治州");
        city.put("533300", "云南省-怒江傈僳族自治州");
        city.put("533400", "云南省-迪庆藏族自治州");
        
        
        city.put("540100", "西藏-拉萨市");
        city.put("540200", "西藏-日喀则市");
        city.put("540300", "西藏-昌都市");
        city.put("540400", "西藏-林芝市");
        city.put("540500", "西藏-山南市");
        city.put("540600", "西藏-那曲市");
        city.put("542500", "西藏-阿里地区");
        
        
        city.put("610100", "陕西省-西安市");
        city.put("610200", "陕西省-铜川市");
        city.put("610300", "陕西省-宝鸡市");
        city.put("610400", "陕西省-咸阳市");
        city.put("610500", "陕西省-渭南市");
        city.put("610600", "陕西省-延安市");
        city.put("610700", "陕西省-汉中市");
        city.put("610800", "陕西省-榆林市");
        city.put("610900", "陕西省-安康市");
        city.put("611000", "陕西省-商洛市");
        
        
        city.put("620100", "甘肃省-兰州市");
        city.put("620200", "甘肃省-嘉峪关市");
        city.put("620300", "甘肃省-金昌市");
        city.put("620400", "甘肃省-白银市");
        city.put("620500", "甘肃省-天水市");
        city.put("620600", "甘肃省-武威市");
        city.put("620700", "甘肃省-张掖市");
        city.put("620800", "甘肃省-平凉市");
        city.put("620900", "甘肃省-酒泉市");
        city.put("621000", "甘肃省-庆阳市");
        city.put("621100", "甘肃省-定西市");
        city.put("621200", "甘肃省-陇南市");
        city.put("622900", "甘肃省-临夏回族自治州");
        city.put("623000", "甘肃省-甘南藏族自治州");
        
        
        city.put("630100", "青海省-西宁市");
        city.put("630200", "青海省-海东市");
        city.put("632200", "青海省-海北藏族自治州");
        city.put("632300", "青海省-黄南藏族自治州");
        city.put("632500", "青海省-海南藏族自治州");
        city.put("632600", "青海省-果洛藏族自治州");
        city.put("632700", "青海省-玉树藏族自治州");
        city.put("632800", "青海省-海西蒙古族藏族自治州");
        
        
        city.put("640100", "宁夏-银川市");
        city.put("640200", "宁夏-石嘴山市");
        city.put("640300", "宁夏-吴忠市");
        city.put("640400", "宁夏-固原市");
        city.put("640500", "宁夏-中卫市");
        
        
        city.put("650100", "新疆-乌鲁木齐市");
        city.put("650200", "新疆-克拉玛依市");
        city.put("650400", "新疆-吐鲁番市");
        city.put("650500", "新疆-哈密市");
        city.put("652300", "新疆-昌吉回族自治州");
        city.put("652700", "新疆-博尔塔拉蒙古自治州");
        city.put("652800", "新疆-巴音郭楞蒙古自治州");
        city.put("652900", "新疆-阿克苏地区");
        city.put("653000", "新疆-克孜勒苏柯尔克孜自治州");
        city.put("653100", "新疆-喀什地区");
        city.put("653200", "新疆-和田地区");
        city.put("654000", "新疆-伊犁哈萨克自治州");
        city.put("654200", "新疆-塔城地区");
        city.put("654300", "新疆-阿勒泰地区");
        city.put("659001", "新疆-石河子市");
        city.put("659002", "新疆-阿拉尔市");
        city.put("659003", "新疆-图木舒克市");
        city.put("659004", "新疆-五家渠市");
        city.put("659005", "新疆-北屯市");
        city.put("659006", "新疆-铁门关市");
        city.put("659007", "新疆-双河市");
        city.put("659008", "新疆-可克达拉市");
        city.put("659009", "新疆-昆玉市");
        
        
        city.put("710100", "台湾省-台北市");
        city.put("710200", "台湾省-高雄市");
        city.put("710300", "台湾省-基隆市");
        city.put("710400", "台湾省-台中市");
        city.put("710500", "台湾省-台南市");
        city.put("710600", "台湾省-新竹市");
        city.put("710700", "台湾省-嘉义市");
        city.put("710900", "台湾省-台北县");
        city.put("711000", "台湾省-宜兰县");
        city.put("711100", "台湾省-桃园县");
        city.put("711200", "台湾省-新竹县");
        city.put("711300", "台湾省-苗栗县");
        city.put("711400", "台湾省-台中县");
        city.put("711500", "台湾省-彰化县");
        city.put("711600", "台湾省-南投县");
        city.put("711700", "台湾省-云林县");
        city.put("711800", "台湾省-嘉义县");
        city.put("711900", "台湾省-台南县");
        city.put("712000", "台湾省-高雄县");
        city.put("712100", "台湾省-屏东县");
        city.put("712200", "台湾省-澎湖县");
        city.put("712300", "台湾省-台东县");
        city.put("712400", "台湾省-花莲县");
        
        
        city.put("1000100", "海外-阿尔及利亚");
        city.put("1000200", "海外-阿根廷");
        city.put("1000300", "海外-阿联酋");
        city.put("1000400", "海外-埃及");
        city.put("1000500", "海外-爱尔兰");
        city.put("1000600", "海外-奥地利");
        city.put("1000700", "海外-澳大利亚");
        city.put("1000800", "海外-巴哈马");
        city.put("1000900", "海外-巴基斯坦");
        city.put("1001000", "海外-巴西");
        city.put("1001100", "海外-白俄罗斯");
        city.put("1001200", "海外-比利时");
        city.put("1001300", "海外-冰岛");
        city.put("1001400", "海外-波兰");
        city.put("1001500", "海外-玻利维亚");
        city.put("1001600", "海外-伯利兹");
        city.put("1001700", "海外-朝鲜");
        city.put("1001800", "海外-丹麦");
        city.put("1001900", "海外-德国");
        city.put("1002000", "海外-俄罗斯");
        city.put("1002100", "海外-厄瓜多尔");
        city.put("1002200", "海外-法国");
        city.put("1002300", "海外-菲律宾");
        city.put("1002400", "海外-芬兰");
        city.put("1002500", "海外-哥伦比亚");
        city.put("1002600", "海外-古巴");
        city.put("1002700", "海外-关岛");
        city.put("1002800", "海外-哈萨克斯坦");
        city.put("1002900", "海外-韩国");
        city.put("1003000", "海外-荷兰");
        city.put("1003100", "海外-加拿大");
        city.put("1003200", "海外-加纳");
        city.put("1003300", "海外-柬埔寨");
        city.put("1003400", "海外-捷克");
        city.put("1003500", "海外-卡塔尔");
        city.put("1003600", "海外-科威特");
        city.put("1003700", "海外-克罗地亚");
        city.put("1003800", "海外-肯尼亚");
        city.put("1003900", "海外-老挝");
        city.put("1004000", "海外-卢森堡");
        city.put("1004100", "海外-罗马尼亚");
        city.put("1004200", "海外-马尔代夫");
        city.put("1004300", "海外-马来西亚");
        city.put("1004400", "海外-美国");
        city.put("1004500", "海外-蒙古");
        city.put("1004600", "海外-孟加拉");
        city.put("1004700", "海外-秘鲁");
        city.put("1004800", "海外-缅甸");
        city.put("1004900", "海外-摩洛哥");
        city.put("1005000", "海外-墨西哥");
        city.put("1005100", "海外-南非");
        city.put("1005200", "海外-尼日利亚");
        city.put("1005300", "海外-挪威");
        city.put("1005400", "海外-葡萄牙");
        city.put("1005500", "海外-日本");
        city.put("1005600", "海外-瑞典");
        city.put("1005700", "海外-瑞士");
        city.put("1005800", "海外-沙特阿拉伯");
        city.put("1005900", "海外-斯里兰卡");
        city.put("1006000", "海外-苏丹");
        city.put("1006100", "海外-泰国");
        city.put("1006200", "海外-坦桑尼亚");
        city.put("1006300", "海外-土耳其");
        city.put("1006400", "海外-委内瑞拉");
        city.put("1006500", "海外-乌克兰");
        city.put("1006600", "海外-西班牙");
        city.put("1006700", "海外-希腊");
        city.put("1006800", "海外-新加坡");
        city.put("1006900", "海外-新西兰");
        city.put("1007000", "海外-匈牙利");
        city.put("1007100", "海外-伊拉克");
        city.put("1007200", "海外-伊朗");
        city.put("1007300", "海外-以色列");
        city.put("1007400", "海外-意大利");
        city.put("1007500", "海外-印度");
        city.put("1007600", "海外-印度尼西亚");
        city.put("1007700", "海外-英国");
        city.put("1007800", "海外-越南");
        city.put("1007900", "海外-智利");
        city.put("1010000", "海外-其他");
        
    }
}

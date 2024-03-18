package org.plugin.service.impl.test;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import okhttp3.*;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.core.mybatis.model.convert.PicConvert;
import org.core.mybatis.pojo.TbOriginPojo;
import org.plugin.common.ComboSearchPlugin;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class InteractivePluginTest implements ComboSearchPlugin {
    public final static String FILTER = "filter";
    
    public final static String TYPE = "type";
    
    public final static String PAGE = "page";
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    @Override
    public List<PluginLabelValue> getParams() {
        List<PluginLabelValue> values = new ArrayList<>(3);
        PluginLabelValue e1 = new PluginLabelValue();
        e1.setLabel("搜索类型(name)");
        e1.setKey(FILTER);
        e1.setValue("");
        values.add(e1);
        
        PluginLabelValue e2 = new PluginLabelValue();
        e2.setLabel("来源(qq)");
        e2.setKey(TYPE);
        e2.setValue("");
        values.add(e2);
        
        PluginLabelValue e3 = new PluginLabelValue();
        e3.setLabel("页面(1)");
        e3.setKey(PAGE);
        e3.setValue("");
        values.add(e3);
        return values;
    }
    
    /**
     * 搜索
     * 返回值label已html解析
     *
     * @param params 运行参数
     * @param name   关键词
     * @return 搜索到的数据
     */
    @Override
    public List<PluginLabelValue> search(List<PluginLabelValue> params, String name) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                                                .build();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded; charset=UTF-8");
        String context = String.format("input=%s&filter=%s&type=%s&page=%s",
                name,
                getValue(params, FILTER),
                getValue(params, TYPE),
                getValue(params, PAGE));
        RequestBody reqBody = RequestBody.create(context, mediaType);
        Request request = new Request.Builder()
                .url("https://music.liuzhijin.cn/")
                .post(reqBody)
                .addHeader("authority", "music.liuzhijin.cn")
                .addHeader("accept", "application/json, text/javascript, */*; q=0.01")
                .addHeader("accept-language", "zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6")
                .addHeader("origin", "https://music.liuzhijin.cn")
                .addHeader("referer", "https://music.liuzhijin.cn/?name=%E6%8C%BD%E7%A7%8B%E6%80%9D&type=netease")
                .addHeader("sec-ch-ua", "\"Not A(Brand\";v=\"99\", \"Microsoft Edge\";v=\"121\", \"Chromium\";v=\"121\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("sec-ch-ua-platform", "\"Windows\"")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("sec-fetch-site", "same-origin")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36 Edg/121.0.0.0")
                .addHeader("x-requested-with", "XMLHttpRequest")
                .addHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                .build();
        try (Response response = client.newCall(request).execute()){
            final String body = UnicodeUtil.toString(Objects.requireNonNull(response.body()).string());
            JSONObject jsonObject = JSONUtil.parseObj(body);
            Integer code = MapUtil.get(jsonObject, "code", Integer.class);
            if (code == null || code != 200) {
                return Collections.emptyList();
            }
            JSONArray data = MapUtil.get(jsonObject, "data", JSONArray.class);
            // String type = MapUtil.get(data, "type", String.class);
            // String link = MapUtil.get(data, "link", String.class);
            // String songid = MapUtil.get(data, "songid", String.class);
            // String title = MapUtil.get(data, "title", String.class);
            // String author = MapUtil.get(data, "author", String.class);
            // String lrc = MapUtil.get(data, "lrc", String.class);
            // String url = MapUtil.get(data, "url", String.class);
            // String pic = MapUtil.get(data, "pic", String.class);
            
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
            ArrayList<PluginLabelValue> pluginLabelValues = new ArrayList<>();
            for (Object datum : data) {
                Template template = engine.getTemplate(
                        "<div class='flex items-center justify-between'><div class='flex items-center'><img src='${pic}' style='width: 3rem;height: 3rem' alt='${title}' width='100' height='100'/><b class='ml-4'>${title}</b> <p class='ml-4'>${author}</p> </div><audio src='${url}' controls/></div>");
                PluginLabelValue e = new PluginLabelValue();
                JSONObject object = (JSONObject) datum;
                String render = template.render(object);
                e.setValue(object.toJSONString(0));
                e.setLabel(render);
                pluginLabelValues.add(e);
            }
            return pluginLabelValues;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 需要同步的数据
     * 返回结果以html解析
     *
     * @param data          数据
     * @param type          ID类型 可能没有。需要自行判断,类型可能是Music ID Album ID Artist ID
     * @param id            需要同步的ID。可能没有,需要自行判断。
     * @param pluginPackage 插件服务
     * @return 返回成功或失败数据。
     */
    @Override
    public String sync(List<PluginLabelValue> data, String type, Long id, PluginPackage pluginPackage) {
        int successCount = 0;
        int failCount = 0;
        
        for (PluginLabelValue datum : data) {
            pluginPackage.logInfo(datum.getKey(), datum.getValue());
            JSONObject jsonObject = JSONUtil.parseObj(datum.getValue());
            
            AudioInfoReq dto = new AudioInfoReq();
            AudioInfoReq.AudioMusic music = new AudioInfoReq.AudioMusic();
            
            music.setMusicName(jsonObject.getStr("title"));
            ArrayList<AudioInfoReq.AudioArtist> artists = new ArrayList<>();
            AudioInfoReq.AudioArtist artistReq = new AudioInfoReq.AudioArtist();
            artistReq.setArtistName(jsonObject.getStr("author"));
            artists.add(artistReq);
            dto.setArtists(artists);
            
            music.setLyric(jsonObject.getStr("lrc"));
            String pic = jsonObject.getStr("pic");
            PicConvert musicPic = new PicConvert();
            musicPic.setPath(pic);
            music.setPic(musicPic);
            
            AudioInfoReq.AudioSource audioSource = new AudioInfoReq.AudioSource();
            ArrayList<AudioInfoReq.AudioSource> sources = new ArrayList<>();
            audioSource.setPathTemp(jsonObject.getStr("url"));
            TbOriginPojo origin = new TbOriginPojo();
            origin.setOrigin("search");
            audioSource.setOrigin(origin);
            sources.add(audioSource);
            dto.setSources(sources);
            
            dto.setMusic(music);
            dto.setUploadFlag(false);
            try {
                pluginPackage.saveMusic(dto);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }
        return String.format("<b>上传成功</b>一共上传%d，失败%d", successCount, failCount);
    }
}

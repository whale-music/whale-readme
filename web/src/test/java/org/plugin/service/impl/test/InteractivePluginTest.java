package org.plugin.service.impl.test;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.UnicodeUtil;
import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import org.api.admin.model.req.upload.ArtistInfoReq;
import org.api.admin.model.req.upload.AudioInfoReq;
import org.core.model.convert.PicConvert;
import org.plugin.common.ComboSearchPlugin;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        List<PluginLabelValue> values = new ArrayList<>();
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
        int timeout = 60 * 60 * 10000;
        Unirest.setTimeouts(timeout, timeout);
        try {
            HttpResponse<String> response = Unirest.post("https://music.liuzhijin.cn/")
                                                   .header("authority", "music.liuzhijin.cn")
                                                   .header("accept", "application/json, text/javascript, */*; q=0.01")
                                                   .header("accept-language", "zh-CN,zh;q=0.9,en;q=0.8")
                                                   .header("origin", "https://music.liuzhijin.cn")
                                                   .header("referer", "https://music.liuzhijin.cn/?name=%E6%9C%89%E4%BD%95%E4%B8%8D%E5%8F%AF&type=netease")
                                                   .header("sec-ch-ua", "\"Chromium\";v=\"112\", \"Google Chrome\";v=\"112\", \"Not:A-Brand\";v=\"99\"")
                                                   .header("sec-ch-ua-mobile", "?0")
                                                   .header("sec-ch-ua-platform", "\"Windows\"")
                                                   .header("sec-fetch-dest", "empty")
                                                   .header("sec-fetch-mode", "cors")
                                                   .header("sec-fetch-site", "same-origin")
                                                   .header("user-agent",
                                                           "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/112.0.0.0 Safari/537.36")
                                                   .header("x-requested-with", "XMLHttpRequest")
                                                   .header("Cookie",
                                                           "Hm_lvt_50027a9c88cdde04a70f5272a88a10fa=1680694423,1682143355,1683131056; Hm_lpvt_50027a9c88cdde04a70f5272a88a10fa=1683131079")
                                                   .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                                                   .field("input", name)
                                                   .field("filter", getValue(params, FILTER))
                                                   .field("type", getValue(params, TYPE))
                                                   .field("page", getValue(params, PAGE))
                                                   .asString();
            
            
            String body = response.getBody();
            body = UnicodeUtil.toString(body);
            JSONObject jsonObject = JSON.parseObject(body);
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
                e.setValue(object.toJSONString(JSONWriter.Feature.BrowserSecure));
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
            JSONObject jsonObject = JSON.parseObject(datum.getValue());
    
            AudioInfoReq dto = new AudioInfoReq();
            dto.setMusicName(jsonObject.getObject("title", String.class));
            ArrayList<ArtistInfoReq> artists = new ArrayList<>();
            ArtistInfoReq artistReq = new ArtistInfoReq();
            artistReq.setArtistName(jsonObject.getObject("author", String.class));
            artists.add(artistReq);
            dto.setArtists(artists);
            dto.setLyric(jsonObject.getObject("lrc", String.class));
            PicConvert picConvert = new PicConvert();
            picConvert.setUrl(jsonObject.getObject("pic", String.class));
            dto.setPic(picConvert);
            dto.setMusicTemp(jsonObject.getObject("url", String.class));
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

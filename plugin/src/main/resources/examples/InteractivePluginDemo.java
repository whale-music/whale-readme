package org.plugin;

import cn.hutool.extra.template.Template;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateEngine;
import cn.hutool.extra.template.TemplateUtil;
import org.plugin.common.ComboSearchPlugin;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.*;

public class InteractivePluginDemo implements ComboSearchPlugin {
    public final static String FILTER = "filter";
    
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    @Override
    public List<PluginLabelValue> getParams() {
        List<PluginLabelValue> values = new ArrayList<>(3);
        PluginLabelValue e1 = new PluginLabelValue();
        e1.setLabel("测试");
        e1.setKey(FILTER);
        e1.setValue("");
        values.add(e1);
        
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
        try {
            TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
            ArrayList<PluginLabelValue> values = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                Template template = engine.getTemplate(
                        "<div class='flex items-center justify-between'><div class='flex items-center'><img src='${pic}' style='width: 3rem;height: 3rem' alt='${title}' width='100' height='100'/><b class='ml-4'>${title}</b> <p class='ml-4'>${author}</p> </div><audio src='${url}' controls/></div>");
                PluginLabelValue e = new PluginLabelValue();
                Map<String, String> hashMap = new HashMap<>();
                hashMap.put("title", name + i);
                hashMap.put("author", name);
                hashMap.put("pic", "https://marketplace.canva.cn/2wxwE/MAE-aB2wxwE/1/s2/canva-%E8%83%8C%E6%99%AF-MAE-aB2wxwE.png");
                hashMap.put("url", "https://www.runoob.com/try/demo_source/horse.mp3");
                String render = template.render(hashMap);
                e.setValue("""
                           {
                             "number": 123213,
                             "string": "31231313123"
                           }
                           """);
                e.setKey(FILTER);
                e.setLabel(render);
                values.add(e);
            }
            return values;
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
        System.out.println("data = " + Arrays.toString(data.toArray()));
        String value = getValue(data, FILTER);
        return String.format("<b>你已点击</b>%s", value);
    }
}

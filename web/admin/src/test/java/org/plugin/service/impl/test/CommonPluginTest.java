package org.plugin.service.impl.test;

import org.plugin.common.CommonPlugin;
import org.plugin.converter.PluginLabelValue;
import org.plugin.service.impl.PluginPackage;

import java.util.ArrayList;
import java.util.List;

public class CommonPluginTest implements CommonPlugin {
    
    private final static String COUNT = "count";
    
    private final static String SLEEP = "sleep";
    
    /**
     * 获取插件调用参数
     *
     * @return 参数
     */
    @Override
    public List<PluginLabelValue> getParams() {
        List<PluginLabelValue> pluginLabelValues = new ArrayList<>();
        PluginLabelValue e1 = new PluginLabelValue();
        e1.setLabel("循环");
        e1.setKey(COUNT);
        e1.setValue("");
        pluginLabelValues.add(e1);
        
        PluginLabelValue e3 = new PluginLabelValue();
        e3.setLabel("睡眠时间");
        e3.setKey(SLEEP);
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
        String count = getValue(values, COUNT);
        String sleep = getValue(values, SLEEP);
        int countValue = Integer.parseInt(count);
        int sleepValue = Integer.parseInt(sleep);
        for (int i = 0; i < countValue; i++) {
            try {
                Thread.sleep(sleepValue);
                pluginPackage.logInfo("测试{}", i);
                pluginPackage.logError("测试{}", i);
                pluginPackage.logWarn("测试{}", i);
                pluginPackage.logDebug("测试{}", i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

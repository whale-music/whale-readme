package org.plugin.sync.impl;

import cn.hutool.http.HttpUtil;
import org.plugin.sync.SyncPlugin;

import java.util.List;

public class SyncPluginImpl implements SyncPlugin {
    String host = "localhost";
    
    @Override
    public void before() {
    
    }
    
    @Override
    public String start(String json) {
        return null;
    }
    
    @Override
    public void after() {
    
    }
    
    @Override
    public List<String> sync(String playId, String targetPlayId) {
        String playList = HttpUtil.get(host + "?uid=" + playId);
        return null;
    }
}

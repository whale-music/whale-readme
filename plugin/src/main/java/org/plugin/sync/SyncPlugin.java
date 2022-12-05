package org.plugin.sync;

import org.plugin.Plugin;

import java.util.List;

public interface SyncPlugin extends Plugin {
    
    List<String> sync(String playId, String targetPlayId);
}

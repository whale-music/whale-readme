package org.plugin.service.sync;

import org.plugin.service.Plugin;

import java.io.IOException;
import java.util.List;

public interface SyncPlugin extends Plugin {
    
    List<String> sync(String playId, String targetPlayId,String cookie) throws IOException;
}

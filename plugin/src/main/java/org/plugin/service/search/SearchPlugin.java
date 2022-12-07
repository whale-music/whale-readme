package org.plugin.service.search;

import org.plugin.service.Plugin;

public interface SearchPlugin extends Plugin {
    
    String search(String id, String name, String singer, String album);
}

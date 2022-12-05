package org.plugin.search;

import org.plugin.Plugin;

public interface SearchPlugin extends Plugin {
    
    String search(String id, String name, String singer, String album);
}

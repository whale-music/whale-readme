package org.plugin.search;

import org.plugin.Plugin;

public interface SearchPlugin extends Plugin {
    
    void search(String name, String singer, String album);
}

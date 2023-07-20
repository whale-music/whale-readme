package org.core.common.constant;

import java.util.Set;

public class RoleConstant {
    public static final String ROLE_SEPARATOR = ":";
    
    public static final String ROLE_MUSIC_WRITE = "music-write";
    
    public static final String ROLE_MUSIC_READ = "music-read";
    
    public static final String ROLE_PLUGIN_WRITE_READ = "plugin-write-read";
    
    public static final String ROLE_PLUGIN_START = "plugin-start";
    
    public static final String ROLE_READ_ONLY = "read_only";
    
    private RoleConstant() {
    }
    
    public static Set<String> getExampleRole() {
        return Set.of(ROLE_MUSIC_WRITE, ROLE_MUSIC_READ, ROLE_PLUGIN_WRITE_READ, ROLE_PLUGIN_START, ROLE_READ_ONLY);
    }
    
}

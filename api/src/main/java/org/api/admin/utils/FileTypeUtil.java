package org.api.admin.utils;

import java.util.Optional;

public class FileTypeUtil {
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    
    private FileTypeUtil() {
    }
    
    public static String getTypeCategorization(String suffix) {
        return switch (Optional.ofNullable(suffix).orElse("")) {
            case "jpg", "jpeg", "png" -> IMAGE;
            case "mp4", "avi", "mkv", "mov" -> VIDEO;
            case "mp3", "flac", "wav", "aac", "ogg" -> AUDIO;
            default -> "unknown";
        };
    }
}

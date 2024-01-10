package org.api.admin.utils;

public class FileTypeUtil {
    public static final String IMAGE = "image";
    public static final String VIDEO = "video";
    public static final String AUDIO = "audio";
    
    private FileTypeUtil() {
    }
    
    public static String getTypeCategorization(String suffix) {
        return switch (suffix) {
            case "jpg", "jpeg", "png" -> IMAGE;
            case "mp4", "avi", "mkv", "mov" -> VIDEO;
            case "mp3", "flac", "wav", "aac", "ogg" -> AUDIO;
            default -> "unknown";
        };
    }
}

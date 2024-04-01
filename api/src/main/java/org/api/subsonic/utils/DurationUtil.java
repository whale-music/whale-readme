package org.api.subsonic.utils;

import java.util.Objects;

public class DurationUtil {
    private DurationUtil() {}
    
    public static Long getDuration(Long duration) {
        if (Objects.isNull(duration) || duration == 0L) {
            return 0L;
        }
        return duration / 1000L;
    }
}

package org.api.subsonic.utils;

import java.util.Objects;

public class DurationUtil {
    private DurationUtil() {}
    
    public static Integer getDuration(Integer duration) {
        if (Objects.isNull(duration) || duration == 0L) {
            return 0;
        }
        return duration / 1000;
    }
}

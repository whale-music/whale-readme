package org.api.subsonic.utils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LocalDateUtil {
    private LocalDateUtil() {
    }
    
    public static String formatUTC(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        // 创建一个 DateTimeFormatter 对象，并指定日期时间格式以及时区偏移量
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        // 格式化 LocalDateTime 为指定格式的日期时间字符串，包括时区偏移量
        return localDateTime.atOffset(ZoneOffset.UTC).format(formatter);
    }
}

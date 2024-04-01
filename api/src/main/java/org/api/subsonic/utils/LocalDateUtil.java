package org.api.subsonic.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@Slf4j
public class LocalDateUtil {
    private LocalDateUtil() {
    }
    
    public static String formatUTCZ(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return "";
        }
        // 创建一个 DateTimeFormatter 对象，并指定日期时间格式以及时区偏移量
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
        // 格式化 LocalDateTime 为指定格式的日期时间字符串，包括时区偏移量
        return localDateTime.atOffset(ZoneOffset.UTC).format(formatter);
    }
    
    // public static String formatUTCZ(LocalDateTime localDateTime) {
    //     return LocalDateTimeUtil.format(localDateTime, DatePattern.UTC_PATTERN);
    // }
    
    public static Integer getYear(LocalDateTime localDateTime) {
        if (Objects.isNull(localDateTime)) {
            return 0;
        }
        return localDateTime.getYear();
    }
 
    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        String s = formatUTCZ(now);
        log.info("UTC Z: {}",s);
    }
}

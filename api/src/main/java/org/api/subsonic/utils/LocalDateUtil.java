package org.api.subsonic.utils;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.LocalDateTimeUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
public class LocalDateUtil {
    private LocalDateUtil() {
    }
    
    public static String formatUTCZ(LocalDateTime localDateTime) {
        return LocalDateTimeUtil.format(localDateTime, DatePattern.UTC_PATTERN);
    }
    
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

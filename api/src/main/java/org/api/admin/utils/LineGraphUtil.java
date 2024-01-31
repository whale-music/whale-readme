package org.api.admin.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.func.LambdaUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.mybatis.pojo.TbMusicPojo;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

public class LineGraphUtil {
    private static final CaseInsensitiveAndUnderscoreInsensitiveComparator COMPARATOR = new CaseInsensitiveAndUnderscoreInsensitiveComparator();
    
    private LineGraphUtil() {
    }
    
    public static List<Integer> calculateTheLineGraph(List<Map<String, Object>> maps) {
        // 转换为输出格式
        List<Integer> result = new LinkedList<>();
        for (LocalDate date = LocalDate.now().minusDays(6); date.isBefore(LocalDate.now().plusDays(1)); date = date.plusDays(1)) {
            boolean found = false;
            for (Map<String, Object> uploadStat : maps) {
                Map<String, Object> caseInsensitiveMap = new TreeMap<>(COMPARATOR);
                caseInsensitiveMap.putAll(uploadStat);
                
                String fieldName = LambdaUtil.getFieldName(TbMusicPojo::getCreateTime);
                Object o = caseInsensitiveMap.get(fieldName);
                
                Date jdkDate = DateUtil.parseDate(o.toString()).toJdkDate();
                Instant instant = date.atStartOfDay().toInstant(ZoneOffset.UTC);
                boolean sameDay = DateUtil.isSameDay(jdkDate, Date.from(instant));
                if (sameDay) {
                    result.add(caseInsensitiveMap.size());
                    found = true;
                    break;
                }
            }
            if (!found) {
                // 如果某天没有上传数据，将数量设为0
                result.add(0);
            }
        }
        return result;
    }
    
    private static class CaseInsensitiveAndUnderscoreInsensitiveComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            // 忽略大小写并移除下划线后比较
            return removeUnderscores(str1).compareToIgnoreCase(removeUnderscores(str2));
        }
        
        private String removeUnderscores(String input) {
            return StringUtils.replace(input, "_", "");
        }
    }
}

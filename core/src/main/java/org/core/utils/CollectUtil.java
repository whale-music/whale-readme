package org.core.utils;

import cn.hutool.core.collection.CollUtil;

import java.util.Collection;
import java.util.Objects;

public class CollectUtil {
    private CollectUtil() {}
    
    /**
     * 排除列表元素中有Null
     * @param collection 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (Objects.isNull(collection)) {
            return false;
        }
        return CollUtil.isEmpty(collection.stream().filter(Objects::nonNull).toList());
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}

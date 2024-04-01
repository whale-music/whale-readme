package org.core.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;

import java.util.Collection;
import java.util.Iterator;
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
    
    /**
     * 排除列表元素中有Null
     *
     * @param iterable 集合
     * @return 是否为空
     */
    public static boolean isEmpty(Iterator<?> iterable) {
        if (Objects.isNull(iterable)) {
            return false;
        }
        return CollUtil.isEmpty(ListUtil.toList(iterable).stream().filter(Objects::nonNull).toList());
    }
    
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }
}

package org.core.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AliasUtil {
    
    private AliasUtil() {
    }
    
    public static List<String> getAliasList(String alias) {
        return StringUtils.isBlank(alias) ? Collections.emptyList() : Arrays.asList(alias.split(","));
    }
}

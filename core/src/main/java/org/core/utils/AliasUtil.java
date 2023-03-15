package org.core.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AliasUtil {
    
    private AliasUtil() {
    }
    
    public static List<String> getAliasList(String alias) {
        alias = Optional.ofNullable(alias).orElse("");
        return Arrays.asList(alias.split(","));
    }
}

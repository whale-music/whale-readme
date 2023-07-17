package org.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.core.common.constant.RoleConstant;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleUtil {
    private RoleUtil() {
    }
    
    public static Set<String> getRoleNames(String role) {
        String[] split = StringUtils.split(role, RoleConstant.ROLE_SEPARATOR);
        return new HashSet<>(Arrays.asList(split));
    }
}

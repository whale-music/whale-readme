package org.core.common.constant;

import java.util.HashMap;
import java.util.Map;

public class AccountTypeConstant {
    public static final Integer ADMIN = 0;
    public static final Integer GUEST = 1;
    public static final Integer COMMON = 2;
    private static final Map<Integer, String> TYPES = new HashMap<>();
    
    static {
        TYPES.put(ADMIN, "ADMIN");
        TYPES.put(GUEST, "GUEST");
        TYPES.put(COMMON, "COMMON");
    }
    
    private AccountTypeConstant() {
    }
    
    public static String getAccountType(Integer type) {
        return TYPES.get(type);
    }
}

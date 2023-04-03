package org.core.utils;

import com.github.yitter.idgen.YitIdHelper;

public class IDUtil {
    private IDUtil() {}
    
    public static long getID(){
        return YitIdHelper.nextId();
    }
}

package org.core.utils;

import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;

public class ExceptionUtil {
    private ExceptionUtil() {}
    
    public static void isNull(boolean flag, ResultCode code) {
        if (flag) {
            throw new BaseException(code);
        }
    }
    public static void isNull(boolean flag,Throwable e) {
        if (flag) {
            throw new RuntimeException(e);
        }
    }
}

package org.common.utils;

import org.common.exception.BaseException;
import org.common.result.ResultCode;

public class ExceptionUtil {
    private ExceptionUtil() {}
    
    public static void isNull(boolean flag, ResultCode code) {
        if (flag) {
            throw new BaseException(code.getResultMsg());
        }
    }
    
    public static void isNull(boolean flag, ResultCode code, Throwable e) {
        if (flag) {
            throw new BaseException(code, e);
        }
    }
}

package org.core.utils;

import cn.hutool.core.io.FileUtil;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;

public class LocalFileUtil {
    private LocalFileUtil() {
    }
    
    public static void checkFileNameLegal(String musicTempFile) {
        if (musicTempFile.contains("/") || musicTempFile.contains("\\")) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
    }
    
    public static void checkFilePath(String path) {
        // 无文件
        if (!FileUtil.isFile(path)) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
    }
}

package org.core.utils;

import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;

import java.io.File;

public class LocalFileUtil {
    private LocalFileUtil() {
    }
    
    public static void checkFileNameLegal(String musicTempFile) {
        if (musicTempFile.contains("/") || musicTempFile.contains("\\")) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
    }
    
    public static File checkFilePath(String path, String filename) {
        // 无文件
        File file = new File(path, filename);
        if (!file.isFile()) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        return file;
    }
}

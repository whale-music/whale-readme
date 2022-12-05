package org.core.utils;

import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;

import java.io.File;
import java.util.List;

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
    
    public static String getFileSuffix(String URL, List<String> fileType) {
        String[] split;
        int indexOf = URL.lastIndexOf('.');
        split = URL.split(String.valueOf(new char[]{'\\', URL.charAt(indexOf)}));
        if (split.length < 1) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        // 检测文件类型是否有效
        if (!fileType.contains(split[split.length - 1])) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        return split[split.length - 1];
    }
}

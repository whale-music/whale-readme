package org.core.utils;

import cn.hutool.core.io.FileUtil;
import org.apache.commons.lang3.StringUtils;
import org.core.common.exception.BaseException;
import org.core.common.result.ResultCode;

import java.io.File;
import java.util.List;

public class LocalFileUtil {
    private LocalFileUtil() {
    }
    
    /**
     * 检查音乐文件是否非法
     *
     * @param musicTempFile 音乐文件
     */
    public static void checkFileNameLegal(String musicTempFile) {
        if (musicTempFile.contains("/") || musicTempFile.contains("\\")) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
    }
    
    /**
     * 检查文件目录是否正确
     *
     * @param path     路径
     * @param filename 文件
     * @return File
     */
    public static File checkFilePath(String path, String filename) {
        // 无文件
        File file = new File(path, filename);
        if (!file.isFile()) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        return file;
    }
    
    /**
     * 获取文件后缀
     *
     * @param fileName 文件名
     * @param fileType 文件类型
     * @return 文件后缀名
     */
    public static String getFileSuffix(String fileName, List<String> fileType) {
        String suffix = FileUtil.getSuffix(fileName);
        if (StringUtils.isBlank(suffix)) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        // 检测文件类型是否有效
        if (!fileType.contains(suffix)) {
            throw new BaseException(ResultCode.FILENAME_INVALID);
        }
        return suffix;
    }
}

package org.core.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class ImageTypeUtils {
    /**
     * 常用文件的文件头如下：(以前六位为准)
     * JPEG (jpg)，文件头：FFD8FF
     * PNG (png)，文件头：89504E47
     * GIF (gif)，文件头：47494638
     * TIFF (tif)，文件头：49492A00
     * Windows Bitmap (bmp)，文件头：424D
     * CAD (dwg)，文件头：41433130
     * Adobe Photoshop (psd)，文件头：38425053
     * Rich Text Format (rtf)，文件头：7B5C727466
     * XML (xml)，文件头：3C3F786D6C
     * HTML (html)，文件头：68746D6C3E
     * Email [thorough only] (eml)，文件头：44656C69766572792D646174653A
     * Outlook Express (dbx)，文件头：CFAD12FEC5FD746F
     * Outlook (pst)，文件头：2142444E
     * MS Word/Excel (xls.or.doc)，文件头：D0CF11E0
     * MS Access (mdb)，文件头：5374616E64617264204A
     * WordPerfect (wpd)，文件头：FF575043
     * Postscript (eps.or.ps)，文件头：252150532D41646F6265
     * Adobe Acrobat (pdf)，文件头：255044462D312E
     * Quicken (qdf)，文件头：AC9EBD8F
     * Windows Password (pwl)，文件头：E3828596
     * ZIP Archive (zip)，文件头：504B0304
     * RAR Archive (rar)，文件头：52617221
     * Wave (wav)，文件头：57415645
     * AVI (avi)，文件头：41564920
     * Real Audio (ram)，文件头：2E7261FD
     * Real Media (rm)，文件头：2E524D46
     * MPEG (mpg)，文件头：000001BA
     * MPEG (mpg)，文件头：000001B3
     * Quicktime (mov)，文件头：6D6F6F76
     * Windows Media (asf)，文件头：3026B2758E66CF11
     * MIDI (mid)，文件头：4D546864
     */
    
    
    public static final String TYPE_JPG = ".jpg";
    public static final String TYPE_GIF = ".gif";
    public static final String TYPE_PNG = ".png";
    public static final String TYPE_BMP = ".bmp";
    public static final String TYPE_WEBP = ".webp";
    public static final String TYPE_TIF = ".tif";
    public static final String TYPE_UNKNOWN = "unknown";
    
    
    public static void main(String[] args) {
        String rootPath = System.getProperty("user.dir");
        List<String> list = new ArrayList<>();
        list.add(rootPath + "/image-demo/file/input/1.jpg");
        // 修改过后缀名 jiang webp 修改为 jpg
        list.add(rootPath + "/image-demo/file/input/2.jpg");
        list.add(rootPath + "/image-demo/file/input/1111.png");
        list.add(rootPath + "/image-demo/file/input/100.webp");
        list.forEach(filename -> {
            try {
                File pdfFile = new File(filename);
                // test code
                log.debug("图片格式：" + getPicType(new FileInputStream(pdfFile)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    
    /**
     * byte数组转换成16进制字符串
     *
     * @param src 字节数组
     * @return 16进制字符串
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length == 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    
    /**
     * 根据文件流判断图片类型
     *
     * @param fis 图片数据流
     * @return jpg/png/gif/bmp
     */
    public static String getPicType(FileInputStream fis) {
        // 读取文件前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = Objects.requireNonNull(bytesToHexString(b)).toUpperCase();
            if (type.contains("FFD8FF")) {
                return TYPE_JPG;
            } else if (type.contains("89504E47")) {
                return TYPE_PNG;
            } else if (type.contains("47494638")) {
                return TYPE_GIF;
            } else if (type.contains("424D")) {
                return TYPE_BMP;
            } else if (type.contains("52494646")) {
                return TYPE_WEBP;
            } else if (type.contains("49492A00")) {
                return TYPE_TIF;
            } else {
                return TYPE_JPG;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException();
    }
    
    /**
     * 根据文件流判断图片类型
     *
     * @param fis 图片流
     * @return jpg/png/gif/bmp
     */
    public static String getPicType2(InputStream fis) {
        // 读取文件前几个字节来判断图片格式
        byte[] b = new byte[4];
        try {
            fis.read(b, 0, b.length);
            String type = Objects.requireNonNull(bytesToHexString(b)).toUpperCase();
            if (type.contains("FFD8FF")) {
                return TYPE_JPG;
            } else if (type.contains("89504E47")) {
                return TYPE_PNG;
            } else if (type.contains("47494638")) {
                return TYPE_GIF;
            } else if (type.contains("424D")) {
                return TYPE_BMP;
            } else if (type.contains("52494646")) {
                return TYPE_WEBP;
            } else if (type.contains("49492A00")) {
                return TYPE_TIF;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new IllegalStateException();
    }
}
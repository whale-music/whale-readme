package org.api.admin.model.res;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class ExportExcelRes {
    
    private ExportExcelRes() {
        throw new IllegalStateException("No construction parameters are required.");
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MusicInfo {
        
        @ColumnWidth(14)
        @ExcelProperty(value = "歌曲名", index = 0)
        private String musicName;
        
        @ColumnWidth(18)
        @ExcelProperty(value = "歌曲别名", index = 1)
        private String musicAlias;
        
        @ColumnWidth(10)
        @ExcelProperty(value = "音乐封面Base64", index = 2)
        private String musicPicBase64;
        
        @ColumnWidth(15)
        @ExcelProperty(value = "音乐流派", index = 3)
        private String musicGenre;
        
        @ColumnWidth(15)
        @ExcelProperty(value = "音乐TAG", index = 4)
        private String musicTag;
        
        @ColumnWidth(12)
        @ExcelProperty(value = "音乐时常", index = 5)
        private String duration;
        
        @ColumnWidth(35)
        @ExcelProperty(value = "普通歌词", index = 6)
        private String commonLyrics;
        
        @ColumnWidth(35)
        @ExcelProperty(value = "逐字歌词", index = 7)
        private String verbatimLyrics;
        
        @ColumnWidth(36)
        @ExcelProperty(value = "歌曲艺术家, 使用逗号分割", index = 8)
        private String musicArtist;
        
        @ColumnWidth(24)
        @ExcelProperty(value = "专辑ID", index = 9)
        private String album;
        
        @ColumnWidth(24)
        @ExcelProperty(value = "音源ID,逗号分割", index = 10)
        private String source;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AlbumInfo {
        @ColumnWidth(15)
        @ExcelProperty(value = "专辑ID", index = 0)
        private String albumId;
        
        @ColumnWidth(20)
        @ExcelProperty(value = "专辑", index = 1)
        private String albumName;
        
        @ColumnWidth(12)
        @ExcelProperty(value = "专辑封面Base64", index = 2)
        private String albumPicBase64;
        
        @ColumnWidth(15)
        @ExcelProperty(value = "专辑流派", index = 3)
        private String albumGenre;
        
        @ColumnWidth(55)
        @ExcelProperty(value = "专辑艺术家", index = 4)
        private String albumArtist;
        
        @ColumnWidth(24)
        @ExcelProperty(value = "专辑版本（比如录音室版，现场版）", index = 5)
        private String albumSubType;
        
        @ColumnWidth(24)
        @ExcelProperty(value = "发行公司", index = 6)
        private String albumCompany;
        
        @ColumnWidth(30)
        @ExcelProperty(value = "专辑发布时间", index = 7)
        private String albumPublicTime;
        
        @ColumnWidth(100)
        @ExcelProperty(value = "专辑描述", index = 8)
        private String albumDescribe;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ArtistInfo {
        @ColumnWidth(14)
        @ExcelProperty(value = "艺术家ID", index = 0)
        private String artistId;
        
        @ColumnWidth(14)
        @ExcelProperty(value = "艺术家名", index = 1)
        private String artistName;
        
        @ColumnWidth(14)
        @ExcelProperty(value = "歌手别名", index = 2)
        private String artistAliasName;
        
        @ColumnWidth(14)
        @ExcelProperty(value = "艺术家性别", index = 3)
        private String artistSex;
        
        @ColumnWidth(10)
        @ExcelProperty(value = "艺术家头像Base64", index = 4)
        private String artistPicBase64;
        
        @ColumnWidth(18)
        @ExcelProperty(value = "出生年月", index = 5)
        private String artistBirth;
        
        @ColumnWidth(20)
        @ExcelProperty(value = "艺术家居住地", index = 6)
        private String artistLocation;
        
        @ColumnWidth(50)
        @ExcelProperty(value = "艺术家介绍", index = 7)
        private String artistDescribe;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SourceInfo {
        @ColumnWidth(24)
        @ExcelProperty(value = "音源ID", index = 0)
        private String sourceId;
        
        @ColumnWidth(30)
        @ExcelProperty(value = "音源MD5", index = 1)
        private String sourceMd5;
        
        @ColumnWidth(30)
        @ExcelProperty(value = "音源音乐地址, 存储相对路径", index = 2)
        private String sourcePath;
        
        @ColumnWidth(10)
        @ExcelProperty(value = "音源比特率", index = 3)
        private String sourceBitRate;
        
        @ColumnWidth(10)
        @ExcelProperty(value = "音乐质量", index = 4)
        private String musicLevel;
        
        @ColumnWidth(10)
        @ExcelProperty(value = "文件格式类型", index = 5)
        private String encodeType;
        
        @ColumnWidth(10)
        @ExcelProperty(value = "音源文件大小", index = 6)
        private String sourceSize;
    }
}


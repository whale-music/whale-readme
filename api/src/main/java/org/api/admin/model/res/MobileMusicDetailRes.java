package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbResourcePojo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MobileMusicDetailRes {
    @Schema(name = "音乐ID")
    private Long id;
    
    @Schema(name = "音乐名")
    private String musicName;
    
    @Schema(name = "音乐别名")
    private String aliasName;
    
    @Schema(name = "封面")
    private String picUrl;
    
    @Schema(name = "音源")
    private List<MusicSources> sources;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class MusicSources extends TbResourcePojo implements Serializable {
        private String url;
        
        public MusicSources(Long id, Long musicId, Integer rate, String path, String md5, String level, String encodeType, Long size, Long userId, LocalDateTime createTime, LocalDateTime updateTime) {
            super(id, musicId, rate, path, md5, level, encodeType, size, userId, createTime, updateTime);
        }
        
        public MusicSources(TbResourcePojo pojo) {
            this(pojo.getId(), pojo.getMusicId(), pojo.getRate(), pojo.getPath(), pojo.getMd5(),
                    pojo.getLevel(), pojo.getEncodeType(), pojo.getSize(), pojo.getUserId(), pojo.getCreateTime(), pojo.getUpdateTime());
        }
    }
}

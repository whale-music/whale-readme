package org.core.jpa.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
@Schema(title = "保存 歌手表")
public class TbArtistEntityVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    /**
     * 歌手ID
     */
    @NotNull(message = "id can not null")
    @Schema(name = "歌手ID")
    private Long id;
    
    
    /**
     * 歌手名
     */
    @NotNull(message = "artistName can not null")
    @Schema(name = "歌手名")
    private String artistName;
    
    
    /**
     * 歌手别名
     */
    @Schema(name = "歌手别名")
    private String aliasName;
    
    
    /**
     * 歌手性别
     */
    @Schema(name = "歌手性别")
    private String sex;
    
    
    /**
     * 出生年月
     */
    @Schema(name = "出生年月")
    private LocalDate birth;
    
    
    /**
     * 所在国家
     */
    @Schema(name = "所在国家")
    private String location;
    
    
    /**
     * 歌手介绍
     */
    @Schema(name = "歌手介绍")
    private String introduction;
    
    
    /**
     * 上传用户ID
     */
    @Schema(name = "上传用户ID")
    private Long userId;
    
    
    /**
     * 创建时间
     */
    @Schema(name = "创建时间")
    private LocalDateTime createTime;
    
    
    /**
     * 修改时间
     */
    @Schema(name = "修改时间")
    private LocalDateTime updateTime;
    
}

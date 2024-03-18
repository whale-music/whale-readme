package org.api.admin.model.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbResourcePojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicFileRes extends TbResourcePojo {
    
    @Schema(name = "文件下载地址")
    private String rawUrl;
    
    @Schema(name = "文件下载地址")
    private Boolean exists;
}

package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbMvPojo;
import org.core.oss.model.Resource;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResourceVideoInfoRes extends Resource {
    
    private LinkData linkData;
    
    private MvResource mvResource;
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LinkData {
        /**
         * 关联数据ID
         */
        private Long id;
        /**
         * 关联数据名
         */
        private String name;
        /**
         * 关联类型
         */
        private String type;
        /**
         * 前端需要字段，作为输入文字联想出ID, 返回null即可
         */
        private String value;
    }
    
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    public static class MvResource extends TbMvPojo {
        private String fileExtension;
    }
}

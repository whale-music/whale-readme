package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbPicPojo;
import org.core.oss.model.Resource;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ResourcePicInfoRes extends Resource {
    
    private List<LinkData> linkData;
    
    private TbPicPojo picResource;
    
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
    }
    
}

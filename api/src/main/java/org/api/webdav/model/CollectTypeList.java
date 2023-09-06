package org.api.webdav.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbCollectPojo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CollectTypeList {
    private List<TbCollectPojo> ordinaryCollect;
    private List<TbCollectPojo> likeCollect;
    private List<TbCollectPojo> recommendCollect;
}


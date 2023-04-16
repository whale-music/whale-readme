package org.api.admin.model.res;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbCollectPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListRes extends TbCollectPojo {
    private Integer count;
}

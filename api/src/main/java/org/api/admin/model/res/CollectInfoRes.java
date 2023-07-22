package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.model.convert.CollectConvert;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CollectInfoRes extends CollectConvert {
    private String collectTag;
}

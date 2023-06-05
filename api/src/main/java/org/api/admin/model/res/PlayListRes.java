package org.api.admin.model.res;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.model.convert.CollectConvert;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PlayListRes extends CollectConvert {
    private Integer count;
}

package org.api.admin.model.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbResourcePojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class MusicSourceRes extends TbResourcePojo {
    private Boolean isExist;
}

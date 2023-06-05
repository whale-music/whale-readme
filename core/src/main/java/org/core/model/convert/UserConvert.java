package org.core.model.convert;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.SysUserPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserConvert extends SysUserPojo {
    private String avatarUrl;
}

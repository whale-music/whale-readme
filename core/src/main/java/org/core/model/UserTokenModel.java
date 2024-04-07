package org.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.mybatis.pojo.SysUserPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserTokenModel extends SysUserPojo {
}

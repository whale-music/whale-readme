package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.SysUserPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserReq extends SysUserPojo {
}

package org.api.admin.model.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.admin.model.common.PageCommon;
import org.core.mybatis.pojo.SysUserPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class PageUserReq extends SysUserPojo {
    private PageCommon page;
}

package org.api.admin.model.req;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbSingerPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SingerReq extends TbSingerPojo {
}

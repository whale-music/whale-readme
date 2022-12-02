package org.api.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.pojo.TbSingerPojo;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class SingerDto extends TbSingerPojo {
}

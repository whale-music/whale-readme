package org.plugin.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbScheduleTaskPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ScheduleRes extends TbScheduleTaskPojo {
    private String pluginName;
    private Boolean onLine;
}

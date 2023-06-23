package org.plugin.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.core.mybatis.pojo.TbPluginMsgPojo;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PluginMsgRes extends TbPluginMsgPojo {
}

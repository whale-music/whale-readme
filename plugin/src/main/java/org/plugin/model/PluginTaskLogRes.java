package org.plugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.mybatis.pojo.TbPluginMsgPojo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginTaskLogRes {
    private List<TbPluginMsgPojo> pluginMsg;
    private String html;
}

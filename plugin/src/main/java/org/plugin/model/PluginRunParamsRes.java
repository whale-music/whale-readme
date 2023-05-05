package org.plugin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.plugin.converter.PluginLabelValue;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginRunParamsRes {
    private String pluginType;
    private List<PluginLabelValue> params;
}

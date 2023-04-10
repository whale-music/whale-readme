package org.plugin.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginLabelValueListRes {
    List<PluginLabelValue> params;
}

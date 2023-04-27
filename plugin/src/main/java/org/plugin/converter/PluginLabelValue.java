package org.plugin.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginLabelValue {
    private String label;
    private String key;
    private String value;
}

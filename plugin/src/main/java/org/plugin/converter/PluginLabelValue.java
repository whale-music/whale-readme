package org.plugin.converter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PluginLabelValue {
    // 搜索框提交后，回显提示
    // 搜索框提示
    private String label;
    // key值，用于获取value值
    private String key;
    // 输入框默认值
    private String value;
}

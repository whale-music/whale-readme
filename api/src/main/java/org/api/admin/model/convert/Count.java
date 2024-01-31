package org.api.admin.model.convert;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Count {
    // 数据总量
    private Long sumCount;
    // 数据增减比例
    private Float percent;
    // 数据是增还是减，null则无变化
    private Boolean fluctuate;
    // 折线图展示数据
    private List<Integer> lines;
}

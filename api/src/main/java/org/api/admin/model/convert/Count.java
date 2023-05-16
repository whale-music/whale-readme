package org.api.admin.model.convert;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Count {
    private Long sumCount;
    private Float percent;
    private Boolean fluctuate;
}

package org.api.admin.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AutoCompleteCommon {
    
    private String value;
    private Long link;
}

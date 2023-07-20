package org.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiddleTypeModel {
    private Long middleId;
    private Byte type;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        MiddleTypeModel that = (MiddleTypeModel) o;
        
        return new EqualsBuilder().append(getMiddleId(), that.getMiddleId()).append(getType(), that.getType()).isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(getMiddleId()).append(getType()).toHashCode();
    }
}

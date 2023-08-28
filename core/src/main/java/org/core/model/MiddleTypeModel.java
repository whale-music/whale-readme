package org.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        
        if (getMiddleId() != null ? !getMiddleId().equals(that.getMiddleId()) : that.getMiddleId() != null) {
            return false;
        }
        return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
    }
    
    @Override
    public int hashCode() {
        int result = getMiddleId() != null ? getMiddleId().hashCode() : 0;
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }
}

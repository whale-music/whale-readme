package org.core.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "tb_middle_tag")
public class TbMiddleTagEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 7501414934794658903L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "middle_id", nullable = false)
    private Long middleId;
    @Basic
    @Column(name = "tag_id", nullable = false)
    private Long tagId;
    @Basic
    @Column(name = "type", nullable = false)
    private Byte type;
    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbTagEntity tbTagByTagId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMiddleTagEntity that = (TbMiddleTagEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(middleId, that.middleId)
                                  .append(tagId, that.tagId)
                                  .append(type, that.type)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(middleId).append(tagId).append(type).toHashCode();
    }
}

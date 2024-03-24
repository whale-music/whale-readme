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
@Table(name = "tb_middle_pic")
public class TbMiddlePicEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 5853627265539648374L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "middle_id", nullable = false)
    private Long middleId;
    @Basic
    @Column(name = "pic_id", nullable = false)
    private Long picId;
    @Basic
    @Column(name = "type", nullable = false)
    private Byte type;
    @ManyToOne
    @JoinColumn(name = "pic_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbPicEntity tbPicByPicId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMiddlePicEntity that = (TbMiddlePicEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(middleId, that.middleId)
                                  .append(picId, that.picId)
                                  .append(type, that.type)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(middleId).append(picId).append(type).toHashCode();
    }
}

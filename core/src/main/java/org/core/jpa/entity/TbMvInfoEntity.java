package org.core.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name = "tb_mv_info")
public class TbMvInfoEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @NotNull
    @Size(max = 512)
    @Column(name = "title", nullable = false, length = 512)
    private String title;
    
    @Basic
    @Column(name = "description", length = -1)
    private String description;
    
    @Column(name = "publish_time")
    private Timestamp publishTime;
    
    @NotNull
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    
    @Column(name = "update_time")
    private Timestamp updateTime;
    
    @OneToMany(mappedBy = "tbMvInfoEntity", fetch = FetchType.EAGER)
    private Collection<TbMvEntity> tbMvInfoEntities;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMvInfoEntity that = (TbMvInfoEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(title, that.title)
                                  .append(description, that.description)
                                  .append(publishTime, that.publishTime)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(title)
                                          .append(description)
                                          .append(publishTime)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}
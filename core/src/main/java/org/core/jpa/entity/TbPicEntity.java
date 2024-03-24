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
import java.sql.Timestamp;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "tb_pic")
public class TbPicEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3918770574972264909L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "path", nullable = false, length = 512)
    private String path;
    @Basic
    @Column(name = "md5", nullable = false, length = 32)
    private String md5;
    @Column(name = "count", nullable = false)
    private Integer count;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @OneToMany(mappedBy = "tbPicByPicId")
    private Collection<TbMiddlePicEntity> tbMiddlePicsById;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbPicEntity that = (TbPicEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(path, that.path)
                                  .append(md5, that.md5)
                                  .append(count, that.count)
                                  .append(updateTime, that.updateTime)
                                  .append(createTime, that.createTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(path).append(md5).append(count).append(updateTime).append(createTime).toHashCode();
    }
}

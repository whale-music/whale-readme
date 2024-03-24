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

@Setter
@Getter
@Entity
@Table(name = "sys_dict_type")
public class SysDictTypeEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "dict_name", nullable = true, length = 100)
    private String dictName;
    @Basic
    @Column(name = "dict_type", nullable = true, length = 100)
    private String dictType;
    @Basic
    @Column(name = "status", nullable = true, length = 1)
    private String status;
    @Basic
    @Column(name = "remark", nullable = true, length = 500)
    private String remark;
    @Basic
    @Column(name = "create_by", nullable = true, length = 64)
    private String createBy;
    @Basic
    @Column(name = "update_by", nullable = true, length = 64)
    private String updateBy;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        SysDictTypeEntity that = (SysDictTypeEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(dictName, that.dictName)
                                  .append(dictType, that.dictType)
                                  .append(status, that.status)
                                  .append(remark, that.remark)
                                  .append(createBy, that.createBy)
                                  .append(updateBy, that.updateBy)
                                  .append(updateTime, that.updateTime)
                                  .append(createTime, that.createTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(dictName)
                                          .append(dictType)
                                          .append(status)
                                          .append(remark)
                                          .append(createBy)
                                          .append(updateBy)
                                          .append(updateTime)
                                          .append(createTime)
                                          .toHashCode();
    }
}

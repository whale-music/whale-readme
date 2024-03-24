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
@Table(name = "sys_dict_data")
public class SysDictDataEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "dict_sort", nullable = true)
    private Integer dictSort;
    @Basic
    @Column(name = "dict_label", nullable = true, length = 100)
    private String dictLabel;
    @Basic
    @Column(name = "dict_value", nullable = true, length = 100)
    private String dictValue;
    @Basic
    @Column(name = "dict_type", nullable = true, length = 100)
    private String dictType;
    @Basic
    @Column(name = "status", nullable = true, length = 1)
    private String status;
    @Basic
    @Column(name = "create_by", nullable = true, length = 64)
    private String createBy;
    @Basic
    @Column(name = "update_by", nullable = true, length = 64)
    private String updateBy;
    @Basic
    @Column(name = "remark", nullable = true, length = 500)
    private String remark;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        SysDictDataEntity that = (SysDictDataEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(dictSort, that.dictSort)
                                  .append(dictLabel, that.dictLabel)
                                  .append(dictValue, that.dictValue)
                                  .append(dictType, that.dictType)
                                  .append(status, that.status)
                                  .append(createBy, that.createBy)
                                  .append(updateBy, that.updateBy)
                                  .append(remark, that.remark)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(dictSort)
                                          .append(dictLabel)
                                          .append(dictValue)
                                          .append(dictType)
                                          .append(status)
                                          .append(createBy)
                                          .append(updateBy)
                                          .append(remark)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sys_dict_type")
public class SysDictTypeEntity implements Serializable {
    public static final long serialVersionUID = 2405432543551807L;
    
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDictName() {
        return dictName;
    }
    
    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
    
    public String getDictType() {
        return dictType;
    }
    
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public String getCreateBy() {
        return createBy;
    }
    
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
    
    public String getUpdateBy() {
        return updateBy;
    }
    
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }
    
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysDictTypeEntity that = (SysDictTypeEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(dictName, that.dictName) && Objects.equals(dictType,
                that.dictType) && Objects.equals(status, that.status) && Objects.equals(remark, that.remark) && Objects.equals(
                createBy,
                that.createBy) && Objects.equals(updateBy, that.updateBy) && Objects.equals(updateTime,
                that.updateTime) && Objects.equals(createTime, that.createTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, dictName, dictType, status, remark, createBy, updateBy, updateTime, createTime);
    }
}

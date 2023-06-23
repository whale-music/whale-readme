package org.core.jpa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "sys_dict_data")
public class SysDictDataEntity {
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", strategy = "org.core.jpa.config.ManualInsertGenerator")
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getDictSort() {
        return dictSort;
    }
    
    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }
    
    public String getDictLabel() {
        return dictLabel;
    }
    
    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }
    
    public String getDictValue() {
        return dictValue;
    }
    
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
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
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysDictDataEntity that = (SysDictDataEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(dictSort, that.dictSort) && Objects.equals(dictLabel,
                that.dictLabel) && Objects.equals(dictValue, that.dictValue) && Objects.equals(dictType,
                that.dictType) && Objects.equals(status, that.status) && Objects.equals(createBy,
                that.createBy) && Objects.equals(updateBy, that.updateBy) && Objects.equals(remark,
                that.remark) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, dictSort, dictLabel, dictValue, dictType, status, createBy, updateBy, remark, createTime, updateTime);
    }
}

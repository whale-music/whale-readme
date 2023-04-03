package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sys_dict_type", schema = "whale_music")
public class SysDictTypePojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "dict_name")
    private String dictName;
    @Basic
    @Column(name = "dict_type")
    private String dictType;
    @Basic
    @Column(name = "status")
    private String status;
    @Basic
    @Column(name = "remark")
    private String remark;
    @Basic
    @Column(name = "create_by")
    private String createBy;
    @Basic
    @Column(name = "update_by")
    private String updateBy;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    
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
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
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
        SysDictTypePojo that = (SysDictTypePojo) o;
        return id == that.id && Objects.equals(dictName, that.dictName) && Objects.equals(dictType,
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

package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_pic")
public class TbPicEntity implements Serializable {
    public static final long serialVersionUID = 3918770574972264909L;
    
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    public String getMd5() {
        return md5;
    }
    
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    public Integer getCount() {
        return count;
    }
    
    public void setCount(Integer count) {
        this.count = count;
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
        TbPicEntity that = (TbPicEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(path, that.path) && Objects.equals(md5,
                that.md5) && Objects.equals(updateTime, that.updateTime) && Objects.equals(createTime, that.createTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, path, md5, updateTime, createTime);
    }
    
    public Collection<TbMiddlePicEntity> getTbMiddlePicsById() {
        return tbMiddlePicsById;
    }
    
    public void setTbMiddlePicsById(Collection<TbMiddlePicEntity> tbMiddlePicsById) {
        this.tbMiddlePicsById = tbMiddlePicsById;
    }
}

package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_middle_pic")
public class TbMiddlePicEntity implements Serializable {
    public static final long serialVersionUID = 5853627265539648374L;
    
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMiddleId() {
        return middleId;
    }
    
    public void setMiddleId(Long middleId) {
        this.middleId = middleId;
    }
    
    public Long getPicId() {
        return picId;
    }
    
    public void setPicId(Long picId) {
        this.picId = picId;
    }
    
    public Byte getType() {
        return type;
    }
    
    public void setType(Byte type) {
        this.type = type;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbMiddlePicEntity that = (TbMiddlePicEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(middleId, that.middleId) && Objects.equals(picId,
                that.picId) && Objects.equals(type, that.type);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, middleId, picId, type);
    }
    
    public TbPicEntity getTbPicByPicId() {
        return tbPicByPicId;
    }
    
    public void setTbPicByPicId(TbPicEntity tbPicByPicId) {
        this.tbPicByPicId = tbPicByPicId;
    }
}

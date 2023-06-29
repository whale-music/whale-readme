package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_user_collect")
@IdClass(TbUserCollectEntityPK.class)
public class TbUserCollectEntity implements Serializable {
    public static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "collect_id", nullable = false)
    private Long collectId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @ManyToOne
    @JoinColumn(name = "collect_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbCollectEntity tbCollectByCollectId;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getCollectId() {
        return collectId;
    }
    
    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbUserCollectEntity that = (TbUserCollectEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(collectId, that.collectId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, collectId);
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public TbCollectEntity getTbCollectByCollectId() {
        return tbCollectByCollectId;
    }
    
    public void setTbCollectByCollectId(TbCollectEntity tbCollectByCollectId) {
        this.tbCollectByCollectId = tbCollectByCollectId;
    }
}

package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_user_mv")
@IdClass(TbUserMvEntityPK.class)
public class TbUserMvEntity implements Serializable {
    public static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "mv_id", nullable = false)
    private Long mvId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @ManyToOne
    @JoinColumn(name = "mv_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMvEntity tbMvByMvId;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getMvId() {
        return mvId;
    }
    
    public void setMvId(Long mvId) {
        this.mvId = mvId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbUserMvEntity that = (TbUserMvEntity) o;
        return Objects.equals(userId, that.userId) && Objects.equals(mvId, that.mvId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, mvId);
    }
    
    public SysUserEntity getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserEntity sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public TbMvEntity getTbMvByMvId() {
        return tbMvByMvId;
    }
    
    public void setTbMvByMvId(TbMvEntity tbMvByMvId) {
        this.tbMvByMvId = tbMvByMvId;
    }
}

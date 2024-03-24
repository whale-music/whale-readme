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
@Table(name = "tb_history")
public class TbHistoryEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852131611450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "middle_id", nullable = false)
    private Long middleId;
    @Basic
    @Column(name = "type", nullable = true)
    private Byte type;
    @Basic
    @Column(name = "count", nullable = true)
    private Integer count;
    @Basic
    @Column(name = "played_time", nullable = true)
    private Long playedTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbHistoryEntity that = (TbHistoryEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(userId, that.userId)
                                  .append(middleId, that.middleId)
                                  .append(type, that.type)
                                  .append(count, that.count)
                                  .append(playedTime, that.playedTime)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(userId)
                                          .append(middleId)
                                          .append(type)
                                          .append(count)
                                          .append(playedTime)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

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
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "tb_plugin_task")
public class TbPluginTaskEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1634466859314138296L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "plugin_id", nullable = false)
    private Long pluginId;
    @Basic
    @Column(name = "status", nullable = false)
    private Byte status;
    @Basic
    @Column(name = "params", length = Integer.MAX_VALUE)
    private String params;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "tbPluginTaskByTaskId")
    private Collection<TbPluginMsgEntity> tbPluginMsgsById;
    @ManyToOne
    @JoinColumn(name = "plugin_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbPluginEntity tbPluginByPluginId;
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
        
        TbPluginTaskEntity that = (TbPluginTaskEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(pluginId, that.pluginId)
                                  .append(status, that.status)
                                  .append(params, that.params)
                                  .append(userId, that.userId)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(pluginId)
                                          .append(status)
                                          .append(params)
                                          .append(userId)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

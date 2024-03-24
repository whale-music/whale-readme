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
@Table(name = "tb_schedule_task")
public class TbScheduleTaskEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 4618675273554807285L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 128)
    private String name;
    @Basic
    @Column(name = "plugin_id", nullable = false)
    private Long pluginId;
    @Basic
    @Column(name = "cron", nullable = false, length = 128)
    private String cron;
    @Basic
    @Column(name = "params", length = Integer.MAX_VALUE)
    private String params;
    @Basic
    @Column(name = "status")
    private Byte status;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
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
        
        TbScheduleTaskEntity that = (TbScheduleTaskEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(name, that.name)
                                  .append(pluginId, that.pluginId)
                                  .append(cron, that.cron)
                                  .append(params, that.params)
                                  .append(status, that.status)
                                  .append(userId, that.userId)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(name)
                                          .append(pluginId)
                                          .append(cron)
                                          .append(params)
                                          .append(status)
                                          .append(userId)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

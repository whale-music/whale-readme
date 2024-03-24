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
@Table(name = "tb_plugin_msg")
public class TbPluginMsgEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 949660729920259853L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "plugin_id", nullable = false)
    private Long pluginId;
    @Basic
    @Column(name = "task_id", nullable = false)
    private Long taskId;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "level", nullable = true)
    private Byte level;
    @Basic
    @Column(name = "msg", nullable = true, length = Integer.MAX_VALUE)
    private String msg;
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
    @JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbPluginTaskEntity tbPluginTaskByTaskId;
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
        
        TbPluginMsgEntity that = (TbPluginMsgEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(pluginId, that.pluginId)
                                  .append(taskId, that.taskId)
                                  .append(userId, that.userId)
                                  .append(level, that.level)
                                  .append(msg, that.msg)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(pluginId)
                                          .append(taskId)
                                          .append(userId)
                                          .append(level)
                                          .append(msg)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

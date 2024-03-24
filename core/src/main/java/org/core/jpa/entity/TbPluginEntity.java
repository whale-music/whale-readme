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
@Table(name = "tb_plugin")
public class TbPluginEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3020670714848213039L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "plugin_name", nullable = false, length = 255)
    private String pluginName;
    @Basic
    @Column(name = "create_name", nullable = true, length = 255)
    private String createName;
    @Basic
    @Column(name = "type", nullable = false, length = 255)
    private String type;
    @Basic
    @Column(name = "code", nullable = true, length = Integer.MAX_VALUE)
    private String code;
    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Basic
    @Column(name = "description", nullable = true, length = Integer.MAX_VALUE)
    private String description;
    @Basic
    @Column(name = "create_time", nullable = false)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbPluginByPluginId")
    private Collection<TbPluginMsgEntity> tbPluginMsgsById;
    @OneToMany(mappedBy = "tbPluginByPluginId")
    private Collection<TbPluginTaskEntity> tbPluginTasksById;
    @OneToMany(mappedBy = "tbPluginByPluginId")
    private Collection<TbScheduleTaskEntity> tbScheduleTasksById;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbPluginEntity that = (TbPluginEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(pluginName, that.pluginName)
                                  .append(createName, that.createName)
                                  .append(type, that.type)
                                  .append(code, that.code)
                                  .append(userId, that.userId)
                                  .append(description, that.description)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(pluginName)
                                          .append(createName)
                                          .append(type)
                                          .append(code)
                                          .append(userId)
                                          .append(description)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

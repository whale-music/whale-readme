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
import java.util.Optional;

@Setter
@Getter
@Entity
@Table(name = "sys_user")
public class SysUserEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "username", nullable = false, length = 128)
    private String username;
    @Basic
    @Column(name = "nickname", nullable = false, length = 128)
    private String nickname;
    @Basic
    @Column(name = "password", nullable = true, length = 20)
    private String password;
    @Basic
    @Column(name = "signature", nullable = true, length = 50)
    private String signature;
    @Basic
    @Column(name = "account_type", nullable = true)
    private Integer accountType;
    @Basic
    @Column(name = "status", nullable = true)
    private Boolean status;
    @Basic
    @Column(name = "last_login_ip", nullable = true, length = 20)
    private String lastLoginIp;
    @Basic
    @Column(name = "last_login_time", nullable = true)
    private Timestamp lastLoginTime;
    @Basic
    @Column(name = "login_device", nullable = true)
    private String loginDevice;
    @Basic
    @Column(name = "role_name", nullable = true, length = 512)
    private String roleName;
    @Basic
    @Column(name = "sub_account_password", nullable = true, length = -1)
    private String subAccountPassword;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbAlbumEntity> tbAlbumsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbArtistEntity> tbArtistsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbCollectEntity> tbCollectsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbHistoryEntity> tbHistoriesById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbMusicEntity> tbMusicsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbMvEntity> tbMvsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbPluginEntity> tbPluginsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbPluginMsgEntity> tbPluginMsgsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbPluginTaskEntity> tbPluginTasksById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbResourceEntity> tbResourcesById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbScheduleTaskEntity> tbScheduleTasksById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbUserAlbumEntity> tbUserAlbumsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbUserArtistEntity> tbUserArtistsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbUserCollectEntity> tbUserCollectsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbUserMvEntity> tbUserMvsById;
    
    public boolean getIsAdmin() {
        return Optional.ofNullable(getAccountType()).orElse(-1) == 0;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        SysUserEntity that = (SysUserEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(username, that.username)
                                  .append(nickname, that.nickname)
                                  .append(password, that.password)
                                  .append(signature, that.signature)
                                  .append(accountType, that.accountType)
                                  .append(status, that.status)
                                  .append(lastLoginIp, that.lastLoginIp)
                                  .append(lastLoginTime, that.lastLoginTime)
                                  .append(loginDevice, that.loginDevice)
                                  .append(roleName, that.roleName)
                                  .append(subAccountPassword, that.subAccountPassword)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(username)
                                          .append(nickname)
                                          .append(password)
                                          .append(signature)
                                          .append(accountType)
                                          .append(status)
                                          .append(lastLoginIp)
                                          .append(lastLoginTime)
                                          .append(loginDevice)
                                          .append(roleName)
                                          .append(subAccountPassword)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

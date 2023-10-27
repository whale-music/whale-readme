package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

@Entity
@Table(name = "sys_user")
public class SysUserEntity implements Serializable {
    public static final long serialVersionUID = 2405432543551807L;
    
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
    @Column(name = "role_name", nullable = true)
    private String roleName;
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
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getSignature() {
        return signature;
    }
    
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    public Integer getAccountType() {
        return accountType;
    }
    
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
    
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }
    
    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    public String getRoleName() {
        return roleName;
    }
    
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    
    public String getLoginDevice() {
        return loginDevice;
    }
    
    public void setLoginDevice(String loginDevice) {
        this.loginDevice = loginDevice;
    }
    
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
        
        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getUsername() != null ? !getUsername().equals(that.getUsername()) : that.getUsername() != null) {
            return false;
        }
        if (getNickname() != null ? !getNickname().equals(that.getNickname()) : that.getNickname() != null) {
            return false;
        }
        if (getPassword() != null ? !getPassword().equals(that.getPassword()) : that.getPassword() != null) {
            return false;
        }
        if (getSignature() != null ? !getSignature().equals(that.getSignature()) : that.getSignature() != null) {
            return false;
        }
        if (getAccountType() != null ? !getAccountType().equals(that.getAccountType()) : that.getAccountType() != null) {
            return false;
        }
        if (getStatus() != null ? !getStatus().equals(that.getStatus()) : that.getStatus() != null) {
            return false;
        }
        if (getLastLoginIp() != null ? !getLastLoginIp().equals(that.getLastLoginIp()) : that.getLastLoginIp() != null) {
            return false;
        }
        if (getLastLoginTime() != null ? !getLastLoginTime().equals(that.getLastLoginTime()) : that.getLastLoginTime() != null) {
            return false;
        }
        if (getLoginDevice() != null ? !getLoginDevice().equals(that.getLoginDevice()) : that.getLoginDevice() != null) {
            return false;
        }
        if (getRoleName() != null ? !getRoleName().equals(that.getRoleName()) : that.getRoleName() != null) {
            return false;
        }
        if (getCreateTime() != null ? !getCreateTime().equals(that.getCreateTime()) : that.getCreateTime() != null) {
            return false;
        }
        if (getUpdateTime() != null ? !getUpdateTime().equals(that.getUpdateTime()) : that.getUpdateTime() != null) {
            return false;
        }
        if (getTbAlbumsById() != null ? !getTbAlbumsById().equals(that.getTbAlbumsById()) : that.getTbAlbumsById() != null) {
            return false;
        }
        if (getTbArtistsById() != null ? !getTbArtistsById().equals(that.getTbArtistsById()) : that.getTbArtistsById() != null) {
            return false;
        }
        if (getTbCollectsById() != null ? !getTbCollectsById().equals(that.getTbCollectsById()) : that.getTbCollectsById() != null) {
            return false;
        }
        if (getTbHistoriesById() != null ? !getTbHistoriesById().equals(that.getTbHistoriesById()) : that.getTbHistoriesById() != null) {
            return false;
        }
        if (getTbMusicsById() != null ? !getTbMusicsById().equals(that.getTbMusicsById()) : that.getTbMusicsById() != null) {
            return false;
        }
        if (getTbMvsById() != null ? !getTbMvsById().equals(that.getTbMvsById()) : that.getTbMvsById() != null) {
            return false;
        }
        if (getTbPluginsById() != null ? !getTbPluginsById().equals(that.getTbPluginsById()) : that.getTbPluginsById() != null) {
            return false;
        }
        if (getTbPluginMsgsById() != null ? !getTbPluginMsgsById().equals(that.getTbPluginMsgsById()) : that.getTbPluginMsgsById() != null) {
            return false;
        }
        if (getTbPluginTasksById() != null ? !getTbPluginTasksById().equals(that.getTbPluginTasksById()) : that.getTbPluginTasksById() != null) {
            return false;
        }
        if (getTbResourcesById() != null ? !getTbResourcesById().equals(that.getTbResourcesById()) : that.getTbResourcesById() != null) {
            return false;
        }
        if (getTbScheduleTasksById() != null ? !getTbScheduleTasksById().equals(that.getTbScheduleTasksById()) : that.getTbScheduleTasksById() != null) {
            return false;
        }
        if (getTbUserAlbumsById() != null ? !getTbUserAlbumsById().equals(that.getTbUserAlbumsById()) : that.getTbUserAlbumsById() != null) {
            return false;
        }
        if (getTbUserArtistsById() != null ? !getTbUserArtistsById().equals(that.getTbUserArtistsById()) : that.getTbUserArtistsById() != null) {
            return false;
        }
        if (getTbUserCollectsById() != null ? !getTbUserCollectsById().equals(that.getTbUserCollectsById()) : that.getTbUserCollectsById() != null) {
            return false;
        }
        return getTbUserMvsById() != null ? getTbUserMvsById().equals(that.getTbUserMvsById()) : that.getTbUserMvsById() == null;
    }
    
    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getUsername() != null ? getUsername().hashCode() : 0);
        result = 31 * result + (getNickname() != null ? getNickname().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        result = 31 * result + (getSignature() != null ? getSignature().hashCode() : 0);
        result = 31 * result + (getAccountType() != null ? getAccountType().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getLastLoginIp() != null ? getLastLoginIp().hashCode() : 0);
        result = 31 * result + (getLastLoginTime() != null ? getLastLoginTime().hashCode() : 0);
        result = 31 * result + (getLoginDevice() != null ? getLoginDevice().hashCode() : 0);
        result = 31 * result + (getRoleName() != null ? getRoleName().hashCode() : 0);
        result = 31 * result + (getCreateTime() != null ? getCreateTime().hashCode() : 0);
        result = 31 * result + (getUpdateTime() != null ? getUpdateTime().hashCode() : 0);
        result = 31 * result + (getTbAlbumsById() != null ? getTbAlbumsById().hashCode() : 0);
        result = 31 * result + (getTbArtistsById() != null ? getTbArtistsById().hashCode() : 0);
        result = 31 * result + (getTbCollectsById() != null ? getTbCollectsById().hashCode() : 0);
        result = 31 * result + (getTbHistoriesById() != null ? getTbHistoriesById().hashCode() : 0);
        result = 31 * result + (getTbMusicsById() != null ? getTbMusicsById().hashCode() : 0);
        result = 31 * result + (getTbMvsById() != null ? getTbMvsById().hashCode() : 0);
        result = 31 * result + (getTbPluginsById() != null ? getTbPluginsById().hashCode() : 0);
        result = 31 * result + (getTbPluginMsgsById() != null ? getTbPluginMsgsById().hashCode() : 0);
        result = 31 * result + (getTbPluginTasksById() != null ? getTbPluginTasksById().hashCode() : 0);
        result = 31 * result + (getTbResourcesById() != null ? getTbResourcesById().hashCode() : 0);
        result = 31 * result + (getTbScheduleTasksById() != null ? getTbScheduleTasksById().hashCode() : 0);
        result = 31 * result + (getTbUserAlbumsById() != null ? getTbUserAlbumsById().hashCode() : 0);
        result = 31 * result + (getTbUserArtistsById() != null ? getTbUserArtistsById().hashCode() : 0);
        result = 31 * result + (getTbUserCollectsById() != null ? getTbUserCollectsById().hashCode() : 0);
        result = 31 * result + (getTbUserMvsById() != null ? getTbUserMvsById().hashCode() : 0);
        return result;
    }
    
    public Collection<TbAlbumEntity> getTbAlbumsById() {
        return tbAlbumsById;
    }
    
    public void setTbAlbumsById(Collection<TbAlbumEntity> tbAlbumsById) {
        this.tbAlbumsById = tbAlbumsById;
    }
    
    public Collection<TbArtistEntity> getTbArtistsById() {
        return tbArtistsById;
    }
    
    public void setTbArtistsById(Collection<TbArtistEntity> tbArtistsById) {
        this.tbArtistsById = tbArtistsById;
    }
    
    public Collection<TbCollectEntity> getTbCollectsById() {
        return tbCollectsById;
    }
    
    public void setTbCollectsById(Collection<TbCollectEntity> tbCollectsById) {
        this.tbCollectsById = tbCollectsById;
    }
    
    public Collection<TbMusicEntity> getTbMusicsById() {
        return tbMusicsById;
    }
    
    public void setTbMusicsById(Collection<TbMusicEntity> tbMusicsById) {
        this.tbMusicsById = tbMusicsById;
    }
    
    public Collection<TbMvEntity> getTbMvsById() {
        return tbMvsById;
    }
    
    public void setTbMvsById(Collection<TbMvEntity> tbMvsById) {
        this.tbMvsById = tbMvsById;
    }
    
    public Collection<TbPluginEntity> getTbPluginsById() {
        return tbPluginsById;
    }
    
    public void setTbPluginsById(Collection<TbPluginEntity> tbPluginsById) {
        this.tbPluginsById = tbPluginsById;
    }
    
    public Collection<TbPluginMsgEntity> getTbPluginMsgsById() {
        return tbPluginMsgsById;
    }
    
    public void setTbPluginMsgsById(Collection<TbPluginMsgEntity> tbPluginMsgsById) {
        this.tbPluginMsgsById = tbPluginMsgsById;
    }
    
    public Collection<TbPluginTaskEntity> getTbPluginTasksById() {
        return tbPluginTasksById;
    }
    
    public void setTbPluginTasksById(Collection<TbPluginTaskEntity> tbPluginTasksById) {
        this.tbPluginTasksById = tbPluginTasksById;
    }
    
    public Collection<TbResourceEntity> getTbResourcesById() {
        return tbResourcesById;
    }
    
    public void setTbResourcesById(Collection<TbResourceEntity> tbResourcesById) {
        this.tbResourcesById = tbResourcesById;
    }
    
    public Collection<TbScheduleTaskEntity> getTbScheduleTasksById() {
        return tbScheduleTasksById;
    }
    
    public void setTbScheduleTasksById(Collection<TbScheduleTaskEntity> tbScheduleTasksById) {
        this.tbScheduleTasksById = tbScheduleTasksById;
    }
    
    public Collection<TbUserAlbumEntity> getTbUserAlbumsById() {
        return tbUserAlbumsById;
    }
    
    public void setTbUserAlbumsById(Collection<TbUserAlbumEntity> tbUserAlbumsById) {
        this.tbUserAlbumsById = tbUserAlbumsById;
    }
    
    public Collection<TbUserArtistEntity> getTbUserArtistsById() {
        return tbUserArtistsById;
    }
    
    public void setTbUserArtistsById(Collection<TbUserArtistEntity> tbUserArtistsById) {
        this.tbUserArtistsById = tbUserArtistsById;
    }
    
    public Collection<TbUserCollectEntity> getTbUserCollectsById() {
        return tbUserCollectsById;
    }
    
    public void setTbUserCollectsById(Collection<TbUserCollectEntity> tbUserCollectsById) {
        this.tbUserCollectsById = tbUserCollectsById;
    }
    
    public Collection<TbUserMvEntity> getTbUserMvsById() {
        return tbUserMvsById;
    }
    
    public void setTbUserMvsById(Collection<TbUserMvEntity> tbUserMvsById) {
        this.tbUserMvsById = tbUserMvsById;
    }
    
    public Collection<TbHistoryEntity> getTbHistoriesById() {
        return tbHistoriesById;
    }
    
    public void setTbHistoriesById(Collection<TbHistoryEntity> tbHistoriesById) {
        this.tbHistoriesById = tbHistoriesById;
    }
    
}

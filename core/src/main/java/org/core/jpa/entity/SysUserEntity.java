package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;
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
    @Column(name = "role_name", nullable = true)
    private Timestamp roleName;
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
    
    public Timestamp getRoleName() {
        return roleName;
    }
    
    public void setRoleName(Timestamp roleName) {
        this.roleName = roleName;
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
        return Objects.equals(id, that.id) && Objects.equals(username, that.username) && Objects.equals(nickname,
                that.nickname) && Objects.equals(password, that.password) && Objects.equals(signature,
                that.signature) && Objects.equals(accountType, that.accountType) && Objects.equals(lastLoginIp,
                that.lastLoginIp) && Objects.equals(lastLoginTime, that.lastLoginTime) && Objects.equals(createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username, nickname, password, signature, accountType, lastLoginIp, lastLoginTime, createTime, updateTime);
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

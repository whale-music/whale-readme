package org.core.jpa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "sys_user")
public class SysUserEntity implements Serializable {
    public static final long serialVersionUID = 2405432543551807L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", strategy = "org.core.jpa.config.ManualInsertGenerator")
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
    @Column(name = "last_login_ip", nullable = true, length = 20)
    private String lastLoginIp;
    @Basic
    @Column(name = "last_login_time", nullable = true)
    private Timestamp lastLoginTime;
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
    private Collection<TbMusicUrlEntity> tbMusicUrlsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbPluginEntity> tbPluginsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbPluginMsgEntity> tbPluginMsgsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbPluginTaskEntity> tbPluginTasksById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbScheduleTaskEntity> tbScheduleTasksById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbUserAlbumEntity> tbUserAlbumsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<TbUserArtistEntity> tbUserArtistsById;
    
    
    public SysUserEntity() {
    }
    
    public SysUserEntity(Long id, String username, String nickname, String password, String signature, Integer accountType, String lastLoginIp, Timestamp lastLoginTime, Timestamp createTime, Timestamp updateTime, Collection<TbAlbumEntity> tbAlbumsById, Collection<TbArtistEntity> tbArtistsById, Collection<TbCollectEntity> tbCollectsById, Collection<TbHistoryEntity> tbHistoriesById, Collection<TbMusicEntity> tbMusicsById, Collection<TbMusicUrlEntity> tbMusicUrlsById, Collection<TbPluginEntity> tbPluginsById, Collection<TbPluginMsgEntity> tbPluginMsgsById, Collection<TbPluginTaskEntity> tbPluginTasksById, Collection<TbScheduleTaskEntity> tbScheduleTasksById, Collection<TbUserAlbumEntity> tbUserAlbumsById, Collection<TbUserArtistEntity> tbUserArtistsById) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        this.signature = signature;
        this.accountType = accountType;
        this.lastLoginIp = lastLoginIp;
        this.lastLoginTime = lastLoginTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.tbAlbumsById = tbAlbumsById;
        this.tbArtistsById = tbArtistsById;
        this.tbCollectsById = tbCollectsById;
        this.tbHistoriesById = tbHistoriesById;
        this.tbMusicsById = tbMusicsById;
        this.tbMusicUrlsById = tbMusicUrlsById;
        this.tbPluginsById = tbPluginsById;
        this.tbPluginMsgsById = tbPluginMsgsById;
        this.tbPluginTasksById = tbPluginTasksById;
        this.tbScheduleTasksById = tbScheduleTasksById;
        this.tbUserAlbumsById = tbUserAlbumsById;
        this.tbUserArtistsById = tbUserArtistsById;
    }
    
    /**
     * 获取
     *
     * @return id
     */
    public Long getId() {
        return id;
    }
    
    /**
     * 设置
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * 获取
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * 设置
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取
     *
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }
    
    /**
     * 设置
     *
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    /**
     * 获取
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * 设置
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取
     *
     * @return signature
     */
    public String getSignature() {
        return signature;
    }
    
    /**
     * 设置
     *
     * @param signature
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }
    
    /**
     * 获取
     *
     * @return accountType
     */
    public Integer getAccountType() {
        return accountType;
    }
    
    /**
     * 设置
     *
     * @param accountType
     */
    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
    
    /**
     * 获取
     *
     * @return lastLoginIp
     */
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    /**
     * 设置
     *
     * @param lastLoginIp
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
    
    /**
     * 获取
     *
     * @return lastLoginTime
     */
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }
    
    /**
     * 设置
     *
     * @param lastLoginTime
     */
    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    /**
     * 获取
     *
     * @return createTime
     */
    public Timestamp getCreateTime() {
        return createTime;
    }
    
    /**
     * 设置
     *
     * @param createTime
     */
    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
    
    /**
     * 获取
     *
     * @return updateTime
     */
    public Timestamp getUpdateTime() {
        return updateTime;
    }
    
    /**
     * 设置
     *
     * @param updateTime
     */
    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
    
    /**
     * 获取
     *
     * @return tbAlbumsById
     */
    public Collection<TbAlbumEntity> getTbAlbumsById() {
        return tbAlbumsById;
    }
    
    /**
     * 设置
     *
     * @param tbAlbumsById
     */
    public void setTbAlbumsById(Collection<TbAlbumEntity> tbAlbumsById) {
        this.tbAlbumsById = tbAlbumsById;
    }
    
    /**
     * 获取
     *
     * @return tbArtistsById
     */
    public Collection<TbArtistEntity> getTbArtistsById() {
        return tbArtistsById;
    }
    
    /**
     * 设置
     *
     * @param tbArtistsById
     */
    public void setTbArtistsById(Collection<TbArtistEntity> tbArtistsById) {
        this.tbArtistsById = tbArtistsById;
    }
    
    /**
     * 获取
     *
     * @return tbCollectsById
     */
    public Collection<TbCollectEntity> getTbCollectsById() {
        return tbCollectsById;
    }
    
    /**
     * 设置
     *
     * @param tbCollectsById
     */
    public void setTbCollectsById(Collection<TbCollectEntity> tbCollectsById) {
        this.tbCollectsById = tbCollectsById;
    }
    
    /**
     * 获取
     *
     * @return tbHistoriesById
     */
    public Collection<TbHistoryEntity> getTbHistoriesById() {
        return tbHistoriesById;
    }
    
    /**
     * 设置
     *
     * @param tbHistoriesById
     */
    public void setTbHistoriesById(Collection<TbHistoryEntity> tbHistoriesById) {
        this.tbHistoriesById = tbHistoriesById;
    }
    
    /**
     * 获取
     *
     * @return tbMusicsById
     */
    public Collection<TbMusicEntity> getTbMusicsById() {
        return tbMusicsById;
    }
    
    /**
     * 设置
     *
     * @param tbMusicsById
     */
    public void setTbMusicsById(Collection<TbMusicEntity> tbMusicsById) {
        this.tbMusicsById = tbMusicsById;
    }
    
    /**
     * 获取
     *
     * @return tbMusicUrlsById
     */
    public Collection<TbMusicUrlEntity> getTbMusicUrlsById() {
        return tbMusicUrlsById;
    }
    
    /**
     * 设置
     *
     * @param tbMusicUrlsById
     */
    public void setTbMusicUrlsById(Collection<TbMusicUrlEntity> tbMusicUrlsById) {
        this.tbMusicUrlsById = tbMusicUrlsById;
    }
    
    /**
     * 获取
     *
     * @return tbPluginsById
     */
    public Collection<TbPluginEntity> getTbPluginsById() {
        return tbPluginsById;
    }
    
    /**
     * 设置
     *
     * @param tbPluginsById
     */
    public void setTbPluginsById(Collection<TbPluginEntity> tbPluginsById) {
        this.tbPluginsById = tbPluginsById;
    }
    
    /**
     * 获取
     *
     * @return tbPluginMsgsById
     */
    public Collection<TbPluginMsgEntity> getTbPluginMsgsById() {
        return tbPluginMsgsById;
    }
    
    /**
     * 设置
     *
     * @param tbPluginMsgsById
     */
    public void setTbPluginMsgsById(Collection<TbPluginMsgEntity> tbPluginMsgsById) {
        this.tbPluginMsgsById = tbPluginMsgsById;
    }
    
    /**
     * 获取
     *
     * @return tbPluginTasksById
     */
    public Collection<TbPluginTaskEntity> getTbPluginTasksById() {
        return tbPluginTasksById;
    }
    
    /**
     * 设置
     *
     * @param tbPluginTasksById
     */
    public void setTbPluginTasksById(Collection<TbPluginTaskEntity> tbPluginTasksById) {
        this.tbPluginTasksById = tbPluginTasksById;
    }
    
    /**
     * 获取
     *
     * @return tbScheduleTasksById
     */
    public Collection<TbScheduleTaskEntity> getTbScheduleTasksById() {
        return tbScheduleTasksById;
    }
    
    /**
     * 设置
     *
     * @param tbScheduleTasksById
     */
    public void setTbScheduleTasksById(Collection<TbScheduleTaskEntity> tbScheduleTasksById) {
        this.tbScheduleTasksById = tbScheduleTasksById;
    }
    
    /**
     * 获取
     *
     * @return tbUserAlbumsById
     */
    public Collection<TbUserAlbumEntity> getTbUserAlbumsById() {
        return tbUserAlbumsById;
    }
    
    /**
     * 设置
     *
     * @param tbUserAlbumsById
     */
    public void setTbUserAlbumsById(Collection<TbUserAlbumEntity> tbUserAlbumsById) {
        this.tbUserAlbumsById = tbUserAlbumsById;
    }
    
    /**
     * 获取
     *
     * @return tbUserArtistsById
     */
    public Collection<TbUserArtistEntity> getTbUserArtistsById() {
        return tbUserArtistsById;
    }
    
    /**
     * 设置
     *
     * @param tbUserArtistsById
     */
    public void setTbUserArtistsById(Collection<TbUserArtistEntity> tbUserArtistsById) {
        this.tbUserArtistsById = tbUserArtistsById;
    }
    
    public String toString() {
        return "SysUserEntity{serialVersionUID = " + serialVersionUID + ", id = " + id + ", username = " + username + ", nickname = " + nickname + ", password = " + password + ", signature = " + signature + ", accountType = " + accountType + ", lastLoginIp = " + lastLoginIp + ", lastLoginTime = " + lastLoginTime + ", createTime = " + createTime + ", updateTime = " + updateTime + ", tbAlbumsById = " + tbAlbumsById + ", tbArtistsById = " + tbArtistsById + ", tbCollectsById = " + tbCollectsById + ", tbHistoriesById = " + tbHistoriesById + ", tbMusicsById = " + tbMusicsById + ", tbMusicUrlsById = " + tbMusicUrlsById + ", tbPluginsById = " + tbPluginsById + ", tbPluginMsgsById = " + tbPluginMsgsById + ", tbPluginTasksById = " + tbPluginTasksById + ", tbScheduleTasksById = " + tbScheduleTasksById + ", tbUserAlbumsById = " + tbUserAlbumsById + ", tbUserArtistsById = " + tbUserArtistsById + "}";
    }
}

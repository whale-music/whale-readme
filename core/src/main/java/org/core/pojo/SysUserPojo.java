package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "sys_user", schema = "whale_music")
public class SysUserPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "username")
    private String username;
    @Basic
    @Column(name = "nickname")
    private String nickname;
    @Basic
    @Column(name = "password")
    private String password;
    @Basic
    @Column(name = "avatar_url")
    private String avatarUrl;
    @Basic
    @Column(name = "background_url")
    private String backgroundUrl;
    @Basic
    @Column(name = "signature")
    private String signature;
    @Basic
    @Column(name = "account_type")
    private Integer accountType;
    @Basic
    @Column(name = "last_login_ip")
    private String lastLoginIp;
    @Basic
    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<CollectPojo> tbCollectsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<RankPojo> tbRanksById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<UserAlbumPojo> tbUserAlbumsById;
    @OneToMany(mappedBy = "sysUserByUserId")
    private Collection<UserArtistPojo> tbUserArtistsById;
    
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
    
    public String getAvatarUrl() {
        return avatarUrl;
    }
    
    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
    
    public String getBackgroundUrl() {
        return backgroundUrl;
    }
    
    public void setBackgroundUrl(String backgroundUrl) {
        this.backgroundUrl = backgroundUrl;
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
    
    public String getLastLoginIp() {
        return lastLoginIp;
    }
    
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }
    
    public LocalDateTime getLastLoginTime() {
        return lastLoginTime;
    }
    
    public void setLastLoginTime(LocalDateTime lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SysUserPojo that = (SysUserPojo) o;
        return id == that.id && Objects.equals(username, that.username) && Objects.equals(nickname,
                that.nickname) && Objects.equals(password, that.password) && Objects.equals(avatarUrl,
                that.avatarUrl) && Objects.equals(backgroundUrl, that.backgroundUrl) && Objects.equals(signature,
                that.signature) && Objects.equals(accountType, that.accountType) && Objects.equals(lastLoginIp,
                that.lastLoginIp) && Objects.equals(lastLoginTime, that.lastLoginTime) && Objects.equals(createTime,
                that.createTime) && Objects.equals(updateTime, that.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id,
                username,
                nickname,
                password,
                avatarUrl,
                backgroundUrl,
                signature,
                accountType,
                lastLoginIp,
                lastLoginTime,
                createTime,
                updateTime);
    }
    
    public Collection<CollectPojo> getTbCollectsById() {
        return tbCollectsById;
    }
    
    public void setTbCollectsById(Collection<CollectPojo> tbCollectsById) {
        this.tbCollectsById = tbCollectsById;
    }
    
    public Collection<RankPojo> getTbRanksById() {
        return tbRanksById;
    }
    
    public void setTbRanksById(Collection<RankPojo> tbRanksById) {
        this.tbRanksById = tbRanksById;
    }
    
    public Collection<UserAlbumPojo> getTbUserAlbumsById() {
        return tbUserAlbumsById;
    }
    
    public void setTbUserAlbumsById(Collection<UserAlbumPojo> tbUserAlbumsById) {
        this.tbUserAlbumsById = tbUserAlbumsById;
    }
    
    public Collection<UserArtistPojo> getTbUserArtistsById() {
        return tbUserArtistsById;
    }
    
    public void setTbUserArtistsById(Collection<UserArtistPojo> tbUserArtistsById) {
        this.tbUserArtistsById = tbUserArtistsById;
    }
}

package org.core.pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_user_album", schema = "whale_music")
@IdClass(UserAlbumPojoPK.class)
public class UserAlbumPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;
    
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "album_id", insertable = false, updatable = false)
    private Long albumId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private SysUserPojo sysUserByUserId;
    
    @ManyToOne
    @JoinColumn(name = "album_id", referencedColumnName = "id", nullable = false)
    private AlbumPojo tbAlbumByAlbumId;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getAlbumId() {
        return albumId;
    }
    
    public void setAlbumId(Long albumId) {
        this.albumId = albumId;
    }
    
    public SysUserPojo getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserPojo sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public AlbumPojo getTbAlbumByAlbumId() {
        return tbAlbumByAlbumId;
    }
    
    public void setTbAlbumByAlbumId(AlbumPojo tbAlbumByAlbumId) {
        this.tbAlbumByAlbumId = tbAlbumByAlbumId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserAlbumPojo that = (UserAlbumPojo) o;
        return Objects.equals(userId, that.userId) && Objects.equals(albumId, that.albumId) && Objects.equals(sysUserByUserId,
                that.sysUserByUserId) && Objects.equals(tbAlbumByAlbumId, that.tbAlbumByAlbumId);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, albumId, sysUserByUserId, tbAlbumByAlbumId);
    }
    
    @Override
    public String toString() {
        return "UserAlbumPojo{" +
                "userId=" + userId +
                ", albumId=" + albumId +
                ", sysUserByUserId=" + sysUserByUserId +
                ", tbAlbumByAlbumId=" + tbAlbumByAlbumId +
                '}';
    }
}

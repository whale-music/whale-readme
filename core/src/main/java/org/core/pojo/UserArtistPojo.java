package org.core.pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_user_artist", schema = "whale_music")
@IdClass(UserArtistPojoPK.class)
public class UserArtistPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "user_id")
    private Long userId;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "artist_id")
    private Long artistId;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private SysUserPojo sysUserByUserId;
    @ManyToOne
    @JoinColumn(name = "artist_id", referencedColumnName = "id", nullable = false)
    private ArtistPojo tbArtistByArtistId;
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Long getArtistId() {
        return artistId;
    }
    
    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserArtistPojo that = (UserArtistPojo) o;
        return userId == that.userId && artistId == that.artistId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(userId, artistId);
    }
    
    public SysUserPojo getSysUserByUserId() {
        return sysUserByUserId;
    }
    
    public void setSysUserByUserId(SysUserPojo sysUserByUserId) {
        this.sysUserByUserId = sysUserByUserId;
    }
    
    public ArtistPojo getTbArtistByArtistId() {
        return tbArtistByArtistId;
    }
    
    public void setTbArtistByArtistId(ArtistPojo tbArtistByArtistId) {
        this.tbArtistByArtistId = tbArtistByArtistId;
    }
}

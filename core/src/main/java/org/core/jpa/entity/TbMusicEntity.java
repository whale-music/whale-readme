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
@Table(name = "tb_music")
public class TbMusicEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852731638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_name", nullable = true, length = 128)
    private String musicName;
    @Basic
    @Column(name = "alias_name", nullable = true, length = 512)
    private String aliasName;
    @Basic
    @Column(name = "album_id", nullable = true)
    private Long albumId;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "time_length", nullable = true)
    private Integer timeLength;
    @Basic
    @Column(name = "comment", nullable = true)
    private String comment;
    @Basic
    @Column(name = "language", nullable = true)
    private String language;
    @Basic
    @Column(name = "publish_time", nullable = true)
    private Timestamp publishTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbCollectMusicEntity> tbCollectMusicsById;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbLyricEntity> tbLyricsById;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "album_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbAlbumEntity tbAlbumByAlbumId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbMusicArtistEntity> tbMusicArtistsById;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbOriginEntity> tbOriginsById;
    @OneToMany(mappedBy = "tbMusicByMusicId", fetch = FetchType.EAGER)
    private Collection<TbResourceEntity> tbResourcesById;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbMusicEntity that = (TbMusicEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(musicName, that.musicName)
                                  .append(aliasName, that.aliasName)
                                  .append(albumId, that.albumId)
                                  .append(userId, that.userId)
                                  .append(timeLength, that.timeLength)
                                  .append(comment, that.comment)
                                  .append(language, that.language)
                                  .append(publishTime, that.publishTime)
                                  .append(updateTime, that.updateTime)
                                  .append(createTime, that.createTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(musicName)
                                          .append(aliasName)
                                          .append(albumId)
                                          .append(userId)
                                          .append(timeLength)
                                          .append(comment)
                                          .append(language)
                                          .append(publishTime)
                                          .append(updateTime)
                                          .append(createTime)
                                          .toHashCode();
    }
}

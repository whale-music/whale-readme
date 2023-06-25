package org.core.jpa.entity;

import jakarta.persistence.*;
import org.core.jpa.config.ManualInsertGenerator;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_origin")
public class TbOriginEntity implements Serializable {
    public static final long serialVersionUID = 1036972429906703454L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Basic
    @Column(name = "music_url_id", nullable = false)
    private Long musicUrlId;
    @Basic
    @Column(name = "origin", nullable = false, length = 256)
    private String origin;
    @Basic
    @Column(name = "origin_url", nullable = true, length = 256)
    private String originUrl;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    @ManyToOne
    @JoinColumn(name = "music_url_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicUrlEntity tbMusicUrlByMusicUrlId;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getMusicId() {
        return musicId;
    }
    
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    
    public Long getMusicUrlId() {
        return musicUrlId;
    }
    
    public void setMusicUrlId(Long musicUrlId) {
        this.musicUrlId = musicUrlId;
    }
    
    public String getOrigin() {
        return origin;
    }
    
    public void setOrigin(String origin) {
        this.origin = origin;
    }
    
    public String getOriginUrl() {
        return originUrl;
    }
    
    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbOriginEntity that = (TbOriginEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(musicId, that.musicId) && Objects.equals(musicUrlId,
                that.musicUrlId) && Objects.equals(origin, that.origin) && Objects.equals(originUrl, that.originUrl);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, musicId, musicUrlId, origin, originUrl);
    }
    
    public TbMusicEntity getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(TbMusicEntity tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
    
    public TbMusicUrlEntity getTbMusicUrlByMusicUrlId() {
        return tbMusicUrlByMusicUrlId;
    }
    
    public void setTbMusicUrlByMusicUrlId(TbMusicUrlEntity tbMusicUrlByMusicUrlId) {
        this.tbMusicUrlByMusicUrlId = tbMusicUrlByMusicUrlId;
    }
}

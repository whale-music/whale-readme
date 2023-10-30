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
    @Column(name = "music_id", nullable = true)
    private Long musicId;
    @Basic
    @Column(name = "resource_id", nullable = true)
    private Long resourceId;
    @Basic
    @Column(name = "origin", nullable = false, length = 256)
    private String origin;
    @Basic
    @Column(name = "origin_url", nullable = true, length = 256)
    private String originUrl;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    @ManyToOne
    @JoinColumn(name = "resource_id", referencedColumnName = "id", insertable = false, updatable = false)
    private TbResourceEntity tbResourceByResourceId;
    
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
    
    public Long getResourceId() {
        return resourceId;
    }
    
    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
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
        return Objects.equals(id, that.id) && Objects.equals(musicId, that.musicId) && Objects.equals(resourceId,
                that.resourceId) && Objects.equals(origin, that.origin) && Objects.equals(originUrl, that.originUrl);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, musicId, resourceId, origin, originUrl);
    }
    
    public TbMusicEntity getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(TbMusicEntity tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
    
    public TbResourceEntity getTbResourceByResourceId() {
        return tbResourceByResourceId;
    }
    
    public void setTbResourceByResourceId(TbResourceEntity tbResourceByResourceId) {
        this.tbResourceByResourceId = tbResourceByResourceId;
    }
}

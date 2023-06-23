package org.core.jpa.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_collect_music")
@IdClass(TbCollectMusicEntityPK.class)
public class TbCollectMusicEntity implements Serializable {
    public static final long serialVersionUID = 385211638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", strategy = "org.core.jpa.config.ManualInsertGenerator")
    @Column(name = "collect_id", nullable = false)
    private Long collectId;
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", strategy = "org.core.jpa.config.ManualInsertGenerator")
    @Column(name = "music_id", nullable = false)
    private Long musicId;
    @Basic
    @Column(name = "sort", nullable = false)
    private Long sort;
    @ManyToOne
    @JoinColumn(name = "collect_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbCollectEntity tbCollectByCollectId;
    @ManyToOne
    @JoinColumn(name = "music_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private TbMusicEntity tbMusicByMusicId;
    
    public Long getCollectId() {
        return collectId;
    }
    
    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
    
    public Long getMusicId() {
        return musicId;
    }
    
    public void setMusicId(Long musicId) {
        this.musicId = musicId;
    }
    
    public Long getSort() {
        return sort;
    }
    
    public void setSort(Long sort) {
        this.sort = sort;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TbCollectMusicEntity that = (TbCollectMusicEntity) o;
        return Objects.equals(collectId, that.collectId) && Objects.equals(musicId, that.musicId) && Objects.equals(sort,
                that.sort);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(collectId, musicId, sort);
    }
    
    public TbCollectEntity getTbCollectByCollectId() {
        return tbCollectByCollectId;
    }
    
    public void setTbCollectByCollectId(TbCollectEntity tbCollectByCollectId) {
        this.tbCollectByCollectId = tbCollectByCollectId;
    }
    
    public TbMusicEntity getTbMusicByMusicId() {
        return tbMusicByMusicId;
    }
    
    public void setTbMusicByMusicId(TbMusicEntity tbMusicByMusicId) {
        this.tbMusicByMusicId = tbMusicByMusicId;
    }
}

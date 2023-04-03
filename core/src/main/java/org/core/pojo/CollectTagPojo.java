package org.core.pojo;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tb_collect_tag", schema = "whale_music")
@IdClass(CollectTagPojoPK.class)
public class CollectTagPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "collect_id")
    private Long collectId;
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "tag_id")
    private Long tagId;
    @ManyToOne
    @JoinColumn(name = "collect_id", referencedColumnName = "id", nullable = false)
    private CollectPojo tbCollectByCollectId;
    @ManyToOne
    @JoinColumn(name = "tag_id", referencedColumnName = "id", nullable = false)
    private TagPojo tbTagByTagId;
    
    public Long getCollectId() {
        return collectId;
    }
    
    public void setCollectId(Long collectId) {
        this.collectId = collectId;
    }
    
    public Long getTagId() {
        return tagId;
    }
    
    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CollectTagPojo that = (CollectTagPojo) o;
        return collectId == that.collectId && tagId == that.tagId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(collectId, tagId);
    }
    
    public CollectPojo getTbCollectByCollectId() {
        return tbCollectByCollectId;
    }
    
    public void setTbCollectByCollectId(CollectPojo tbCollectByCollectId) {
        this.tbCollectByCollectId = tbCollectByCollectId;
    }
    
    public TagPojo getTbTagByTagId() {
        return tbTagByTagId;
    }
    
    public void setTbTagByTagId(TagPojo tbTagByTagId) {
        this.tbTagByTagId = tbTagByTagId;
    }
}

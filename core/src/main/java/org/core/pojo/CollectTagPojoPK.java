package org.core.pojo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class CollectTagPojoPK implements Serializable {
    @Column(name = "collect_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long collectId;
    @Column(name = "tag_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tagId;
    
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
        CollectTagPojoPK that = (CollectTagPojoPK) o;
        return collectId == that.collectId && tagId == that.tagId;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(collectId, tagId);
    }
}

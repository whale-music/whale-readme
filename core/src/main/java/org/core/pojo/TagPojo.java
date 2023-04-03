package org.core.pojo;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "tb_tag", schema = "whale_music")
public class TagPojo {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;
    @Basic
    @Column(name = "tag_name")
    private String tagName;
    @Basic
    @Column(name = "create_time")
    @CreatedDate
    private LocalDateTime createTime;
    @Basic
    @Column(name = "update_time")
    @LastModifiedDate
    private LocalDateTime updateTime;
    @OneToMany(mappedBy = "tbTagByTagId")
    private Collection<CollectTagPojo> tbCollectTagsById;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTagName() {
        return tagName;
    }
    
    public void setTagName(String tagName) {
        this.tagName = tagName;
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
        TagPojo tagPojo = (TagPojo) o;
        return id == tagPojo.id && Objects.equals(tagName, tagPojo.tagName) && Objects.equals(createTime,
                tagPojo.createTime) && Objects.equals(updateTime, tagPojo.updateTime);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, tagName, createTime, updateTime);
    }
    
    public Collection<CollectTagPojo> getTbCollectTagsById() {
        return tbCollectTagsById;
    }
    
    public void setTbCollectTagsById(Collection<CollectTagPojo> tbCollectTagsById) {
        this.tbCollectTagsById = tbCollectTagsById;
    }
}

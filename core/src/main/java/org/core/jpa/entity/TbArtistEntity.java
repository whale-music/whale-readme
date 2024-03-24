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
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collection;

@Setter
@Getter
@Entity
@Table(name = "tb_artist")
public class TbArtistEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 3852731638450316352L;
    
    @Id
    @GeneratedValue(generator = "IdGenerator", strategy = GenerationType.AUTO)
    @GenericGenerator(name = "IdGenerator", type = ManualInsertGenerator.class)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "artist_name", nullable = false, length = 128)
    private String artistName;
    @Basic
    @Column(name = "alias_name", nullable = true, length = 255)
    private String aliasName;
    @Basic
    @Column(name = "sex", nullable = true, length = 64)
    private String sex;
    @Basic
    @Column(name = "birth", nullable = true)
    private Date birth;
    @Basic
    @Column(name = "location", nullable = true, length = 64)
    private String location;
    @Basic
    @Column(name = "introduction", nullable = true, length = Integer.MAX_VALUE)
    private String introduction;
    @Basic
    @Column(name = "user_id", nullable = true)
    private Long userId;
    @Basic
    @Column(name = "create_time", nullable = true)
    private Timestamp createTime;
    @Basic
    @Column(name = "update_time", nullable = true)
    private Timestamp updateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", insertable = false, updatable = false)
    private SysUserEntity sysUserByUserId;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<TbMusicArtistEntity> tbMusicArtistsById;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<TbMvArtistEntity> tbMvArtistsById;
    @OneToMany(mappedBy = "tbArtistByArtistId")
    private Collection<TbUserArtistEntity> tbUserArtistsById;
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        TbArtistEntity that = (TbArtistEntity) o;
        
        return new EqualsBuilder().append(id, that.id)
                                  .append(artistName, that.artistName)
                                  .append(aliasName, that.aliasName)
                                  .append(sex, that.sex)
                                  .append(birth, that.birth)
                                  .append(location, that.location)
                                  .append(introduction, that.introduction)
                                  .append(userId, that.userId)
                                  .append(createTime, that.createTime)
                                  .append(updateTime, that.updateTime)
                                  .isEquals();
    }
    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id)
                                          .append(artistName)
                                          .append(aliasName)
                                          .append(sex)
                                          .append(birth)
                                          .append(location)
                                          .append(introduction)
                                          .append(userId)
                                          .append(createTime)
                                          .append(updateTime)
                                          .toHashCode();
    }
}

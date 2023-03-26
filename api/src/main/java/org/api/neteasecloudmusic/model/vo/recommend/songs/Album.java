package org.api.neteasecloudmusic.model.vo.recommend.songs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Album {
    private String name;
    private Long id;
    private String type;
    private Integer size;
    private Long picId;
    private String blurPicUrl;
    private Long companyId;
    private Long pic;
    private String picUrl;
    private Long publishTime;
    private String description;
    private String tags;
    private String company;
    private String briefDesc;
    private Artist artist;
    private List<String> songs;
    private List<String> alias;
    private Long status;
    private Long copyrightId;
    private String commentThreadId;
    private List<Artist> artists;
    private String subType;
    private Boolean onSale;
    private Long mark;
    private Long gapless;
    @JsonProperty("picId_str")
    private String picIdStr;
}
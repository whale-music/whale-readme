package org.api.subsonic.model.req.albumlist2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.api.subsonic.common.SubsonicResult;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AlbumReq extends SubsonicResult {
    
    @NotBlank
    private String type;
    
    private Long size;
    
    private Long offset;
    
    private String fromYear;
    
    private String toYear;
    
    private String genre;
    
    private String musicFolderId;
    
}

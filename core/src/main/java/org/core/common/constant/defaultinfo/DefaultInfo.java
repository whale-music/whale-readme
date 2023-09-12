package org.core.common.constant.defaultinfo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "default-info")
public class DefaultInfo implements Serializable {
    public static final long serialVersionUID = 1905122041950251207L;
    
    private Name name;
    private Pic pic;
    
    
    @Data
    public static class Name implements Serializable {
        public static final long serialVersionUID = 1905122241950251207L;
        
        private EnumNameType artist = EnumNameType.DEFAULT;
        private EnumNameType music = EnumNameType.DEFAULT;
        private EnumNameType album = EnumNameType.DEFAULT;
    }
    
    @Data
    public static class Pic implements Serializable {
        public static final long serialVersionUID = 1902222041950251207L;
        
        private String defaultPic;
        private String musicPic;
        private String playListPic;
        private String albumPic;
        private String artistPic;
        private String userAvatarPic;
        private String userBackgroundPic;
    }
    
}


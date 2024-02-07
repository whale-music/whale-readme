package org.core.common.constant.defaultinfo;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.utils.ServletUtils;
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
    
    public static class Pic implements Serializable {
        public static final long serialVersionUID = 1902222041950251207L;
        
        private String defaultPic;
        private String musicPic;
        private String playListPic;
        private String albumPic;
        private String artistPic;
        private String userAvatarPic;
        private String userBackgroundPic;
        
        private String getPic(String pic) {
            if (CharSequenceUtil.startWithIgnoreCase(pic, "http")) {
                return pic;
            }
            return ServletUtils.paresPath(pic);
        }
        
        public String getDefaultPic() {
            return getPic(defaultPic);
        }
        
        public String getMusicPic() {
            return getPic(musicPic);
        }
        
        public String getPlayListPic() {
            return getPic(playListPic);
        }
        
        public String getAlbumPic() {
            return getPic(albumPic);
        }
        
        public String getArtistPic() {
            return getPic(artistPic);
        }
        
        public String getUserAvatarPic() {
            return getPic(userAvatarPic);
        }
        
        public String getUserBackgroundPic() {
            return getPic(userBackgroundPic);
        }
        
        public void setDefaultPic(String defaultPic) {
            this.defaultPic = defaultPic;
        }
        
        public void setMusicPic(String musicPic) {
            this.musicPic = musicPic;
        }
        
        public void setPlayListPic(String playListPic) {
            this.playListPic = playListPic;
        }
        
        public void setAlbumPic(String albumPic) {
            this.albumPic = albumPic;
        }
        
        public void setArtistPic(String artistPic) {
            this.artistPic = artistPic;
        }
        
        public void setUserAvatarPic(String userAvatarPic) {
            this.userAvatarPic = userAvatarPic;
        }
        
        public void setUserBackgroundPic(String userBackgroundPic) {
            this.userBackgroundPic = userBackgroundPic;
        }
    }
    
}


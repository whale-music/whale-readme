package org.core.config;

import cn.hutool.core.text.StrFormatter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "upload")
public class UploadConfig {
    private String musicNameTemplate;
    
    public String getMusicNameTemplate(String musicName, String musicAlias, String albumName, String artistName, String rate) {
        Map<String, Object> placeholders = new HashMap<>();
        placeholders.put("music", musicName + " " + musicAlias);
        placeholders.put("album", albumName);
        placeholders.put("artist", artistName);
        placeholders.put("rate", rate);
        return StrFormatter.format(musicNameTemplate, placeholders, false);
    }
}

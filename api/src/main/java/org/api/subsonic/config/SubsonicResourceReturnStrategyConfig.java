package org.api.subsonic.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.common.enums.EnumResourceReturnStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(prefix = "subsonic")
public class SubsonicResourceReturnStrategyConfig implements Serializable {
    public static final long serialVersionUID = 1902222041950251207L;
    
    private Resource resource;
    
    @Data
    public static class Resource implements Serializable {
        public static final long serialVersionUID = 1905122241950251207L;
        
        private EnumResourceReturnStrategy returnPlan = EnumResourceReturnStrategy.FIRST;
    }
}

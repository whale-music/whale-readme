package org.api.config;

import cn.hutool.core.text.UnicodeUtil;
import org.springframework.boot.Banner;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public interface ApplicationStartup {
    
    default void start(SpringApplicationBuilder builder, String[] args) throws IOException {
        URL resource = getClass().getResource("banner");
        SpringApplicationBuilder application = builder.child(this.getClass());
        if (Objects.nonNull(resource)) {
            String unicodeBannerStr = StreamUtils.copyToString(resource.openStream(), StandardCharsets.UTF_8);
            Banner banner = (environment, sourceClass, out) -> out.println(UnicodeUtil.toString(unicodeBannerStr));
            application.banner(banner);
        }
        application.run(args);
    }
}

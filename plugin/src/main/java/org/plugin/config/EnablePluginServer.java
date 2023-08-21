
package org.plugin.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(PluginAutoConfiguration.class)
public @interface EnablePluginServer {
}

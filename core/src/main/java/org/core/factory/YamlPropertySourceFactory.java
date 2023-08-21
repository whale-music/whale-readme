package org.core.factory;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class YamlPropertySourceFactory implements PropertySourceFactory {
    
    /**
     * Create a {@link PropertySource} that wraps the given resource.
     *
     * @param name     the name of the property source
     * @param resource the resource (potentially encoded) to wrap
     * @return the new {@link PropertySource} (never {@code null})
     * @throws IOException if resource resolution failed
     */
    @NotNull
    @Override
    public PropertySource<?> createPropertySource(@Nullable String name, @NotNull EncodedResource resource) throws IOException {
        String configName = Objects.isNull(name) ? Objects.requireNonNull(resource.getResource().getFilename(), "Some error message") : name;
        return new PropertiesPropertySource(configName, load(resource));
    }
    
    /**
     * Load properties from the YAML file.
     *
     * @param resource Instance of {@link EncodedResource}
     * @return instance of properties
     */
    private Properties load(EncodedResource resource) throws FileNotFoundException {
        try {
            YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
            factory.setResources(resource.getResource());
            factory.afterPropertiesSet();
            
            return factory.getObject();
        } catch (IllegalStateException ex) {
            /*
             * Ignore resource not found.
             */
            Throwable cause = ex.getCause();
            if (cause instanceof FileNotFoundException causeThrow) {
                throw causeThrow;
            }
            throw ex;
        }
    }
}
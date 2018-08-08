/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.env;

import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

/**
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
public class EncodedPropertiesPropertySourceLoader implements PropertySourceLoader {
    @Override
    public String[] getFileExtensions() {
        return new String[]{"properties"};
    }

    @Override
    public List<PropertySource<?>> load(String name, Resource resource) throws IOException {
        Properties properties = PropertiesLoaderUtils.loadProperties(new EncodedResource(resource, Charset.defaultCharset()));
        if (properties.isEmpty()) {
            return Collections.emptyList();
        }
        return Collections
                .singletonList(new OriginTrackedMapPropertySource(name, properties));
    }
}

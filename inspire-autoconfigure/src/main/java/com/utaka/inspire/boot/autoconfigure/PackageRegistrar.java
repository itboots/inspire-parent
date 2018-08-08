/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.autoconfigure;

import com.google.common.collect.Lists;
import com.google.common.reflect.Reflection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
class PackageRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger log = LoggerFactory.getLogger(PackageRegistrar.class);

    private boolean processed = false;

    /**
     * {@link ImportBeanDefinitionRegistrar} to store the base package from the importing
     * configuration.
     */

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata,
                                        BeanDefinitionRegistry registry) {

        Collection<String> packages = getBasePackages(metadata, PackageScan.class.getName());
        packages.addAll(getBasePackages(metadata, SpringAutoConfiguration.class.getName()));

        String[] basePackages = packages.toArray(new String[packages.size()]);
        log.info("Register packages: {}", Arrays.toString(basePackages));
        AutoConfigurationPackages.register(registry, basePackages);
    }

    private Collection<String> getBasePackages(AnnotationMetadata metadata, String annotationName) {
        AnnotationAttributes attributes = AnnotationAttributes.fromMap(metadata
                .getAnnotationAttributes(annotationName, false));
        if (attributes == null) {
            return Lists.newArrayList();
        }

        List<String> packages = Lists.newArrayList();
        packages.addAll(Arrays.asList(attributes.getStringArray("basePackages")));

        Class<?>[] basePackageClasses = attributes.getClassArray("basePackageClasses");
        if (basePackageClasses != null && basePackageClasses.length > 0) {
            for (int i = 0; i < basePackageClasses.length; i++) {
                packages.add(Reflection.getPackageName(basePackageClasses[i]));
            }
        }
        packages.add(Reflection.getPackageName(metadata.getClassName()));
        return packages;
    }


}

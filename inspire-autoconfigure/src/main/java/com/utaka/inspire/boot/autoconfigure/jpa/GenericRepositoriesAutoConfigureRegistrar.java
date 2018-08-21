/*
 * Copyright (c) 2015, Inspireso and/or its affiliates. All rights reserved.
 */

package com.utaka.inspire.boot.autoconfigure.jpa;

import com.utaka.inspire.jpa.repository.support.GenericRepositorySupport;
import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
import org.springframework.data.repository.config.RepositoryConfigurationExtension;

import java.lang.annotation.Annotation;

class GenericRepositoriesAutoConfigureRegistrar extends
        AbstractRepositoryConfigurationSourceSupport {

    @Override
    protected Class<? extends Annotation> getAnnotation() {
        return EnableJpaRepositories.class;
    }

    @Override
    protected Class<?> getConfiguration() {
        return EnableJpaRepositoriesConfiguration.class;
    }

    @Override
    protected RepositoryConfigurationExtension getRepositoryConfigurationExtension() {
        return new JpaRepositoryConfigExtension();
    }

    @EnableJpaRepositories(repositoryBaseClass = GenericRepositorySupport.class)
    private static class EnableJpaRepositoriesConfiguration {
    }
}

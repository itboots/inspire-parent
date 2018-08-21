/*
 * Copyright (c) 2015, Inspireso and/or its affiliates. All rights reserved.
 */

package com.utaka.inspire.boot.autoconfigure.jpa;

import com.utaka.inspire.jpa.repository.GenericRepository;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

/**
 * @author LANXE
 */
@Configuration
@ConditionalOnBean(DataSource.class)
@ConditionalOnClass(GenericRepository.class)
@ConditionalOnProperty(
        prefix = "spring.data.jpa.repositories",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true)
@Import({
        SimpleRepositoryAutoConfiguration.class,
        GenericRepositoriesAutoConfigureRegistrar.class
})
@AutoConfigureBefore(JpaRepositoriesAutoConfiguration.class)
@AutoConfigureAfter(HibernateJpaAutoConfiguration.class)
public class GenericRepositoriesAutoConfiguration {

}
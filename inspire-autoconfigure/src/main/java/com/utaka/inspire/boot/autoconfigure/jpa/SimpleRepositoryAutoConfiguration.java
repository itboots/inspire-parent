/*
 * Copyright (c) 2015, Inspireso and/or its affiliates. All rights reserved.
 */

package com.utaka.inspire.boot.autoconfigure.jpa;

import com.utaka.inspire.jpa.repository.SimpleRepository;
import com.utaka.inspire.jpa.repository.support.SimpleRepositorySupport;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@ConditionalOnClass({SimpleRepository.class})
@ConditionalOnMissingBean(SimpleRepository.class)
class SimpleRepositoryAutoConfiguration {

    @Bean
    public SimpleRepository simpleRepository(EntityManager em) {
        SimpleRepositorySupport simpleRepository = new SimpleRepositorySupport();
        simpleRepository.setEntityManager(em);
        return simpleRepository;
    }
}

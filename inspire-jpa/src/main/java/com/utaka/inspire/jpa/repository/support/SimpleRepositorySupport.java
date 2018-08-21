/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository.support;

import com.utaka.inspire.data.SchemaAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * 基于JPA实现简单的持久化操作。
 *
 * @author XINEN
 */
@NoRepositoryBean
@Transactional(readOnly = true, rollbackFor = Throwable.class)
public class SimpleRepositorySupport extends InternalRepository {

    /**
     * Default constructor.
     */
    public SimpleRepositorySupport() {

    }

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    @Autowired(required = false)
    public void setSchemaAware(SchemaAware schemaAware) {
        this.schemaAware = schemaAware;
    }

}

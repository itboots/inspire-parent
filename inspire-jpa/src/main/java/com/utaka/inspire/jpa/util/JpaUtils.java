/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.util;

import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import javax.persistence.EntityManager;

/**
 * @author XINEN
 */
public final class JpaUtils {

    /**
     * get {@link Session} from {@link EntityManager}
     */
    public static Session getSession(EntityManager em) {
        return em.unwrap(Session.class);
    }

    public static SessionFactoryImplementor getSessionFactoryImplementor(EntityManager em) {
        return (SessionFactoryImplementor) (getSession(em)).getSessionFactory();
    }
}

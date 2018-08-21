/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.jpa.repository.support;

import com.utaka.inspire.jpa.repository.DynamicSchemaSupport;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;

/**
 * @author XINEN
 */
public class DynamicSchemaRepositoryProxyPostProcessor implements RepositoryProxyPostProcessor {
    /*
     * (non-Javadoc)
     *
     * @see
     * org.springframework.data.repository.core.support.RepositoryProxyPostProcessor#postProcess
     * (org.springframework.aop.framework.ProxyFactory)
     */
    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(new AlterSchemaMethodIntercceptor());
    }


    static class AlterSchemaMethodIntercceptor implements MethodInterceptor {

        AlterSchemaMethodIntercceptor() {

        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation
         * )
         */
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            if (invocation.getThis() instanceof DynamicSchemaSupport) {
                DynamicSchemaSupport dss = (DynamicSchemaSupport) invocation.getThis();
                dss.alterCurrentSchema();
            }

            return invocation.proceed();

        }
    }

}

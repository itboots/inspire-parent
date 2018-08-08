/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.security;

import com.google.common.collect.Sets;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Set;

/**
 * @author lanxe
 */
public class DefaultCredentialManager implements CredentialManager, ApplicationContextAware {

    private Set<CredentialProvider<?>> providers = Sets.newConcurrentHashSet();

    @Override
    public Set<CredentialProvider<?>> getCredential(String user) {
        return providers;
    }

    @Override
    public <T> T getCredentialProvider(Class<T> providerClass) {
        for (CredentialProvider<?> cp : providers) {
            if (providerClass.isInstance(cp)) {
                return (T) cp;
            }
        }
        return null;

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, CredentialProvider> result = applicationContext.getBeansOfType(CredentialProvider.class);
        if (result != null && result.size() > 0) {
            for (CredentialProvider cp : result.values()) {
                providers.add(cp);
            }
        }
    }


}

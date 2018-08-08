/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.autoconfigure.ctx;

import com.utaka.inspire.context.ContextHolder;
import com.utaka.inspire.event.EventBusService;
import com.utaka.inspire.security.CredentialManager;
import com.utaka.inspire.security.DefaultCredentialManager;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * @author LANXE
 */
@Configuration
@ConditionalOnClass({
        ContextHolder.class
        , EventBusService.class
})
@ComponentScan(basePackages = {
        "com.utaka.inspire.context"
        , "com.utaka.inspire.event"
})
@EnableConfigurationProperties(EventProperties.class)
@Import({
        RocketMqConfiguration.class,
        EventConfiguration.class
})
public class ContextAutoConfiguration {
    /**
     * 凭证管理，支持一个应用多种登录方式
     */
    @Bean
    @ConditionalOnClass(DefaultCredentialManager.class)
    @ConditionalOnMissingBean(CredentialManager.class)
    public CredentialManager credentialManager() {
        return new DefaultCredentialManager();
    }
}

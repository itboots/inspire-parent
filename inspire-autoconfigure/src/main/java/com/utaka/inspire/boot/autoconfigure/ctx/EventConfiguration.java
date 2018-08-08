/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.autoconfigure.ctx;

import com.utaka.inspire.event.DefaultEventBusService;
import com.utaka.inspire.event.EventBusService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class EventConfiguration {

    @Bean
    @ConditionalOnClass({DefaultEventBusService.class})
    @ConditionalOnMissingBean(EventBusService.class)
    public EventBusService eventBusService() {
        return new DefaultEventBusService();
    }

}

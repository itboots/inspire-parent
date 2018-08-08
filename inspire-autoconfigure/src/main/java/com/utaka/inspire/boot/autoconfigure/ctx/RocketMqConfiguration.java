/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.autoconfigure.ctx;

import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.event.EventBusService;
import com.utaka.inspire.event.rocketmq.RocketMqAsyncMessageListener;
import com.utaka.inspire.event.rocketmq.RocketMqMessageDelegate;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@AutoConfigureBefore(EventConfiguration.class)
@ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "rocketmq.namesrv-addr", matchIfMissing = false)
class RocketMqConfiguration {

    @Autowired
    private EventProperties properties;

    @Bean
    @ConditionalOnClass({EventBusManager.class})
    @ConditionalOnMissingBean(EventBusService.class)
    public EventBusService eventBusService() {
        return new EventBusManager();
    }

    @Bean
    @ConditionalOnClass({RocketMqAsyncMessageListener.class, DefaultMQProducer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "rocketmq.producer-name", matchIfMissing = false)
    public RocketMqAsyncMessageListener rocketMqAsyncMessageListener(DefaultMQProducer producer) {
        RocketMqAsyncMessageListener listener = new RocketMqAsyncMessageListener();
        listener.setTopic(properties.getRocketmq().getTopic());
        listener.setProducer(producer);
        return listener;
    }

    @Bean
    @ConditionalOnClass({RocketMqMessageDelegate.class, DefaultMQPushConsumer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "rocketmq.consumer-name", matchIfMissing = false)
    public RocketMqMessageDelegate rocketMqMessageDelegate(DefaultMQPushConsumer consumer) {
        RocketMqMessageDelegate delegate = new RocketMqMessageDelegate();
        delegate.setConsumer(consumer);
        delegate.setTopic(properties.getRocketmq().getTopic());
        return delegate;
    }

    @Bean
    @ConditionalOnClass({DefaultMQPushConsumer.class})
    @ConditionalOnMissingBean({DefaultMQPushConsumer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "rocketmq.consumer-name", matchIfMissing = false)
    public DefaultMQPushConsumer defaultMQPushConsumer(EventProperties properties) {
        EventProperties.Rocketmq props = properties.getRocketmq();
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(props.getConsumerGroupName());
        consumer.setNamesrvAddr(props.getNamesrvAddr());
        consumer.setInstanceName(props.getConsumerName());
        return consumer;
    }

    @Bean
    @ConditionalOnClass({DefaultMQProducer.class})
    @ConditionalOnMissingBean({DefaultMQProducer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "rocketmq.producer-name", matchIfMissing = false)
    public DefaultMQProducer defaultMQProducer(EventProperties properties) {
        EventProperties.Rocketmq props = properties.getRocketmq();
        final DefaultMQProducer producer = new DefaultMQProducer(props.getProducerGroupName());
        producer.setNamesrvAddr(props.getNamesrvAddr());
        producer.setInstanceName(props.getProducerName());
        return producer;
    }
}

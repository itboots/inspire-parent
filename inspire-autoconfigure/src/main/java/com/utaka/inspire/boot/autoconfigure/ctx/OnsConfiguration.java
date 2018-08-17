/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.autoconfigure.ctx;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.event.EventBusService;
import com.utaka.inspire.event.ons.AliyunOnsAsyncMessageListener;
import com.utaka.inspire.event.ons.AliyunOnsMessageDelegate;
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

import java.util.Properties;


@Configuration
@AutoConfigureBefore(EventConfiguration.class)
@ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "ons.namesrv-addr", matchIfMissing = false)
class OnsConfiguration {

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
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "ons.producer-name", matchIfMissing = false)
    public AliyunOnsAsyncMessageListener aliyunOnsAsyncMessageListener(OrderProducer producer) {
        AliyunOnsAsyncMessageListener listener = new AliyunOnsAsyncMessageListener();
        listener.setTopic(properties.getRocketmq().getTopic());
        listener.setProducer(producer);
        return listener;
    }

    @Bean
    @ConditionalOnClass({RocketMqMessageDelegate.class, DefaultMQPushConsumer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "ons.consumer-name", matchIfMissing = false)
    public AliyunOnsMessageDelegate aliyunOnsMessageDelegate(OrderConsumer consumer) {
        AliyunOnsMessageDelegate delegate = new AliyunOnsMessageDelegate();
        delegate.setConsumer(consumer);
        delegate.setTopic(properties.getRocketmq().getTopic());
        delegate.setTag(properties.getRocketmq().getTag());
        return delegate;
    }

    @Bean
    @ConditionalOnClass({DefaultMQPushConsumer.class})
    @ConditionalOnMissingBean({DefaultMQPushConsumer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "ons.consumer-name", matchIfMissing = false)
    public OrderConsumer orderConsumer(EventProperties properties) {
        EventProperties.AliyunOns aliyunOns = properties.getOns();
        Properties props = new Properties();
        props.put(PropertyKeyConst.ONSAddr, aliyunOns.getNamesrvAddr());
        props.put(PropertyKeyConst.AccessKey, aliyunOns.getAccessKey());
        props.put(PropertyKeyConst.SecretKey, aliyunOns.getSecretKey());
        props.put(PropertyKeyConst.ConsumerId, aliyunOns.getProducerGroupName());
        props.put(PropertyKeyConst.InstanceName, aliyunOns.getProducerName());
        return ONSFactory.createOrderedConsumer(props);
    }

    @Bean
    @ConditionalOnClass({DefaultMQProducer.class})
    @ConditionalOnMissingBean({DefaultMQProducer.class})
    @ConditionalOnProperty(prefix = EventProperties.PREFIX, name = "ons.producer-name", matchIfMissing = false)
    public OrderProducer orderProducer(EventProperties properties) {
        EventProperties.AliyunOns aliyunOns = properties.getOns();
        Properties props = new Properties();
        props.put(PropertyKeyConst.ONSAddr, aliyunOns.getNamesrvAddr());
        props.put(PropertyKeyConst.AccessKey, aliyunOns.getAccessKey());
        props.put(PropertyKeyConst.SecretKey, aliyunOns.getSecretKey());
        props.put(PropertyKeyConst.ProducerId, aliyunOns.getProducerGroupName());
        props.put(PropertyKeyConst.InstanceName, aliyunOns.getProducerName());
        return ONSFactory.createOrderProducer(props);
    }

}

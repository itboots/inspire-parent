/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.rocketmq;

import com.google.common.base.Charsets;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.utaka.inspire.event.DestinationNameResolver;
import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.event.KeyResolver;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Serializing;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;


/**
 * @author LANXE
 */
public class RocketMqAsyncMessageListener implements InitializingBean, DisposableBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private EventBusManager bus;

    private DefaultMQProducer producer;

    private String topic;

    public void setProducer(DefaultMQProducer producer) {
        this.producer = producer;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onEnqueue(Object event) {
        try {
            if (event != null) {
                this.enqueue(event);
                LOG.info("###enqueue with ({}) success!", event);
            }
        } catch (Exception e) {
            LOG.error("###enqueue with ({}) fail!", event);
            throw new Error(e);
        }
    }

    private void enqueue(Object event) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        String topic = this.topic;
        String body = Serializing.json().toString(event);
        if (event instanceof DestinationNameResolver) {
            topic = ((DestinationNameResolver) event).getDesttinationName();
        }

        if (StringUtils.isEmpty(topic)) {
            topic = this.topic;
        }

        Message msg = new Message(
                topic,
                event.getClass().getName(),
                body.getBytes(Charsets.UTF_8)
        );

        if (event instanceof KeyResolver) {
            msg.setKeys(((KeyResolver) event).getKeys());
        }

        SendResult sendResult = producer.send(msg);
        LOG.info("###enqueue with ({}) result: {}", sendResult);

    }

    @Override
    public void destroy() throws Exception {
        bus.unregisterAsync(this);
        producer.shutdown();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (bus == null) {
            bus = EventBusManager.INSTANCE;
        }
        bus.registerAsync(this);
        producer.start();

    }
}

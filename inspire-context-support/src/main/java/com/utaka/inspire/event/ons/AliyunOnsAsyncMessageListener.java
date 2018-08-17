/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageConst;
import com.google.common.base.Charsets;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.utaka.inspire.event.DestinationNameResolver;
import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.event.KeyResolver;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Serializing;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;


/**
 * @author LANXE
 */
public class AliyunOnsAsyncMessageListener implements InitializingBean, DisposableBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    @Autowired
    private Environment env;

    @Autowired(required = false)
    private EventBusManager bus;

    private OrderProducer producer;

    private String topic;

    public void setProducer(OrderProducer producer) {
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

    private void enqueue(Object event) {
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
            StringBuffer sb = new StringBuffer();
            for (String k : ((KeyResolver) event).getKeys()) {
                sb.append(k);
                sb.append(MessageConst.KEY_SEPARATOR);
            }
            msg.setKey(sb.toString().trim());
        }

        SendResult sendResult = producer.send(msg, event.getClass().getName());
        LOG.info("###enqueue with ({}) result: {}", sendResult);

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (bus == null) {
            bus = EventBusManager.INSTANCE;
        }
        bus.registerAsync(this);
        producer.start();

    }

    @Override
    public void destroy() throws Exception {
        bus.unregisterAsync(this);
        producer.shutdown();
    }

}

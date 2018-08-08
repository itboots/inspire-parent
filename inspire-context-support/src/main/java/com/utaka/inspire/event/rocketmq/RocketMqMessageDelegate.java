/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.rocketmq;

import com.google.common.base.Charsets;
import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Serializing;
import com.utaka.inspire.util.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * @author LANXE
 */
public class RocketMqMessageDelegate implements MessageListenerConcurrently, InitializingBean, DisposableBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    @Autowired(required = false)
    protected EventBusManager bus;

    private DefaultMQPushConsumer consumer;

    private String topic;

    public void setConsumer(DefaultMQPushConsumer consumer) {
        this.consumer = consumer;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> messages, ConsumeConcurrentlyContext context) {
        System.out.println(Thread.currentThread().getName() + " Receive New Messages: " + messages.size());
        try {
            for (MessageExt message : messages) {
                this.handleMessage(message);
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        } catch (Exception e) {
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }


    public void handleMessage(MessageExt message) {

        LOG.info("###receive from queue message ({}) success!", message);

        try {
            String className = message.getTags();

            Object event = null;
            if (StringUtils.isNotEmpty(className)) {
                event = Serializing.json().toObject(new String(message.getBody(), Charsets.UTF_8), className);
            }
            this.bus.post(event);

            LOG.info("###handle the message ({}) success!", message);
        } catch (Exception e) {
            LOG.error("###handle the message ({}) fail!", message);
            throw new Error(e);
        }

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (this.bus == null) {
            this.bus = new EventBusManager();
        }
        this.bus.registerAsync(this);

        /**
         * 订阅指定topic下所有消息<br>
         * 注意：一个consumer对象可以订阅多个topic
         */
        consumer.subscribe(topic, "*");
        consumer.registerMessageListener(this);
        consumer.start();

    }


    @Override
    public void destroy() throws Exception {
        this.bus.unregisterAsync(this);
        consumer.shutdown();
    }
}

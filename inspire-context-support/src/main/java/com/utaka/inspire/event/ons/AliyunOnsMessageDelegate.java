/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.ons;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.google.common.base.Charsets;
import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Serializing;
import com.utaka.inspire.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author LANXE
 */
public class AliyunOnsMessageDelegate implements MessageOrderListener, InitializingBean, DisposableBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    @Autowired(required = false)
    protected EventBusManager bus;

    private OrderConsumer consumer;

    private String topic;

    private String tag = "*";

    public void setConsumer(OrderConsumer consumer) {
        this.consumer = consumer;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public OrderAction consume(Message message, ConsumeOrderContext context) {
        try {
            this.handleMessage(message);
            return OrderAction.Success;
        } catch (Exception e) {
            return OrderAction.Suspend;
        }

    }


    public void handleMessage(Message message) {

        LOG.info("###receive from queue message ({}) success!", message);

        try {
            String className = message.getTag();

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
    public void afterPropertiesSet() {
        if (this.bus == null) {
            this.bus = new EventBusManager();
        }
        this.bus.registerAsync(this);

        /**
         * 订阅指定topic下所有消息<br>
         * 注意：一个consumer对象可以订阅多个topic
         */
        consumer.subscribe(topic, tag, this);
        consumer.start();

    }


    @Override
    public void destroy() throws Exception {
        this.bus.unregisterAsync(this);
        consumer.shutdown();
    }
}

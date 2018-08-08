/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.jms;

import com.google.common.collect.Maps;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;
import com.google.common.util.concurrent.Atomics;
import com.utaka.inspire.event.DeliveryDelaySupport;
import com.utaka.inspire.event.DestinationNameResolver;
import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.util.AtomicSerialNumber;
import com.utaka.inspire.util.GZips;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Serializing;
import org.slf4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.util.StringUtils;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author lanxe
 */
public class JmsAsyncMessageListener implements InitializingBean, DisposableBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    private final ThreadLocal<DateFormat> df = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyyMMdd");
        }
    };

    private AtomicReference<AtomicSerialNumber> currentSerialNumber;

    @Autowired
    private Environment env;

    @Autowired
    private JmsTemplate jms;

    @Autowired(required = false)
    private EventBusManager bus;

    private String destination;

    public void setDestination(String destination) {
        this.destination = destination;
    }

    @Subscribe
    @AllowConcurrentEvents
    public void onEnqueue(Object event) throws JmsException {
        try {
            if (event != null) {
                String id = this.enqueue(event);
                LOG.info("###[{}]enqueue with ({}) success!", id, event);
            }
        } catch (Exception e) {
            LOG.error("###enqueue with ({}) fail!", event);
            throw new Error(e);
        }
    }

    private String enqueue(final Object event) throws JmsException {
        String destinationName = this.destination;
        if (event instanceof DestinationNameResolver) {
            destinationName = ((DestinationNameResolver) event).getDesttinationName();
        }

        if (StringUtils.isEmpty(destinationName)) {
            destinationName = this.destination;
        }

        JmsMessage message = new JmsMessage();
        Map<String, String> header = Maps.newHashMap();
        String id = nextSerialNumber(destinationName);
        header.put(JmsMessage.SERIAL_NUMBER, id);
        header.put(JmsMessage.BODY_CLASS, event.getClass().getName());
        header.put(JmsMessage.TIMESTAMP, String.valueOf(System.currentTimeMillis()));
        header.put(JmsMessage.CHANNEL, destinationName);


        if (event != null) {
            String json = Serializing.json().toString(event);
            if (json.length() > 2000) {
                try {
                    json = GZips.gzip(json);
                    header.put(JmsMessage.CONTENT_ENCODING, "gzip");
                } catch (IOException e) {
                    LOG.error("gzip fail!", e);
                }
            }
            message.setBody(json);
        }
        message.setHeader(Serializing.json().toString(header));

        if (event instanceof DeliveryDelaySupport) {
            final int delay = ((DeliveryDelaySupport) event).getDeliveryDelay();
            jms.convertAndSend(destinationName, message, new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws JMSException {
                    message.setIntProperty("JMS_OracleDelay", delay);
                    return message;
                }
            });
        } else {
            jms.convertAndSend(destinationName, message);
        }

        return id;
    }

    @Override
    public void destroy() throws Exception {
        this.bus.unregisterAsync(this);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (StringUtils.isEmpty(this.destination)) {
            this.destination = this.env.getProperty("jms.queue.name", "event_queue");
        }

        if (this.currentSerialNumber == null) {
            this.currentSerialNumber = Atomics.newReference(new AtomicSerialNumber(getPrefix()));
        }

        if (this.bus == null) {
            this.bus = new EventBusManager();
        }
        this.bus.registerAsync(this);

    }


    private String nextSerialNumber(String topic) {
        String prefix = this.getPrefix();
        AtomicSerialNumber currentNumber = currentSerialNumber.get();
        while (!prefix.equals(currentNumber.getPrefix())) {
            boolean updated = currentSerialNumber.compareAndSet(currentNumber, new AtomicSerialNumber(prefix));
            if (updated) {
                break;
            }
        }

        return topic + "-" + currentSerialNumber.get().incrementAndGet();
    }

    private String getPrefix() {
        return df.get().format(new Date()) + "#";
    }

}

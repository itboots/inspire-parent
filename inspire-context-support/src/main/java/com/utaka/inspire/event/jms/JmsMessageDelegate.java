/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

/**
 *
 */
package com.utaka.inspire.event.jms;

import com.utaka.inspire.event.EventBusManager;
import com.utaka.inspire.util.GZips;
import com.utaka.inspire.util.LogManager;
import com.utaka.inspire.util.Serializing;
import com.utaka.inspire.util.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import java.sql.SQLException;
import java.util.Map;


/**
 * @author LANXE
 */
public class JmsMessageDelegate implements InitializingBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    @Autowired(required = false)
    protected EventBusManager bus;

    @Transactional(rollbackFor = Throwable.class)
    public void handleMessage(JmsMessage message)
            throws JMSException, SQLException, ClassNotFoundException {
        long receivedMillis = System.currentTimeMillis();

        LOG.info("###receive from queue message ({}) success!", message);

        try {
            Map<String, String> header =
                    Serializing.json().toObject(message.getHeader(), Map.class);

            String className = header.get(JmsMessage.BODY_CLASS);
            long timestamp = header.containsKey(JmsMessage.TIMESTAMP)
                    ? Long.valueOf(header.get(JmsMessage.TIMESTAMP))
                    : receivedMillis;
            String channel = header.containsKey(JmsMessage.CHANNEL)
                    ? header.get(JmsMessage.CHANNEL)
                    : "default";

            String id = header.containsKey(JmsMessage.SERIAL_NUMBER)
                    ? header.get(JmsMessage.SERIAL_NUMBER)
                    : channel + "-0";


            Object event = null;
            if (StringUtils.isNotEmpty(className)) {
                String body = message.getBody();
                if (header.containsKey(JmsMessage.CONTENT_ENCODING)) {
                    String encoding = header.get(JmsMessage.CONTENT_ENCODING);
                    if ("gzip".equals(encoding)) {
                        body = GZips.gunzip(body);
                    }
                }
                event = Serializing.json().toObject(body, className);
            }

            this.bus.post(event);

            long completedMillis = System.currentTimeMillis();

            LOG.info("###[{}:{}+{}ms]->handle the message ({})success!",
                    id, receivedMillis - timestamp, completedMillis - receivedMillis, message);
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

    }
}

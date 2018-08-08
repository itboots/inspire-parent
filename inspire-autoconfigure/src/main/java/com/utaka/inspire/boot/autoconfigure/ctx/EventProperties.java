/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.boot.autoconfigure.ctx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = EventProperties.PREFIX)
class EventProperties {
    private static final Logger LOG = LoggerFactory.getLogger(EventProperties.class);
    static final String PREFIX = "inspire.event";

    private Aq aq = new Aq();

    public Aq getAq() {
        return aq;
    }

    public void setAq(Aq aq) {
        this.aq = aq;
    }

    public static class Aq {
        private String queueName;

        public String getQueueName() {
            return queueName;
        }

        public void setQueueName(String queueName) {
            this.queueName = queueName;
        }
    }

    private Jms jms = new Jms();

    public Jms getJms() {
        return jms;
    }

    public void setJms(Jms jms) {
        this.jms = jms;
    }

    public static class Jms {

        /**
         * JMS 的目标.
         */
        private String destination;


        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }
    }

    /**
     * RocketMQ配置.
     */
    private Rocketmq rocketmq = new Rocketmq();

    public Rocketmq getRocketmq() {
        return rocketmq;
    }

    public void setRocketmq(Rocketmq rocketmq) {
        this.rocketmq = rocketmq;
    }

    public static class Rocketmq {
        /**
         * 消息消费者组名.
         */
        private String consumerGroupName = "ConsumerGroupName";

        /**
         * 消息生产者组名.
         */
        private String producerGroupName = "ProducerGroupName";

        /**
         * 消息主题.
         */
        private String topic = "*";

        /**
         * 消息名称服务地址.
         */
        private String namesrvAddr;

        /**
         * 消息消费者名称.
         */
        private String consumerName = "Consumber";

        /**
         * 消息生产者名称.
         */
        private String producerName = "Producer";

        public String getConsumerGroupName() {
            return consumerGroupName;
        }

        public void setConsumerGroupName(String consumerGroupName) {
            this.consumerGroupName = consumerGroupName;
        }

        public String getProducerGroupName() {
            return producerGroupName;
        }

        public void setProducerGroupName(String producerGroupName) {
            this.producerGroupName = producerGroupName;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getNamesrvAddr() {
            return namesrvAddr;
        }

        public void setNamesrvAddr(String namesrvAddr) {
            this.namesrvAddr = namesrvAddr;
        }

        public String getConsumerName() {
            return consumerName;
        }

        public void setConsumerName(String consumerName) {
            this.consumerName = consumerName;
        }

        public String getProducerName() {
            return producerName;
        }

        public void setProducerName(String producerName) {
            this.producerName = producerName;
        }
    }
}

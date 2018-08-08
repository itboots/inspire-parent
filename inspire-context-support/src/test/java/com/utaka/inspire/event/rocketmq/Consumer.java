/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.google.common.base.Charsets;

import java.util.List;

/**
 */
public class Consumer {
    /**
     * 当前例子是PushConsumer用法，使用方式给用户感觉是消息从RocketMQ服务器推到了应用客户端。<br>
     * 但是实际PushConsumer内部是使用长轮询Pull方式从MetaQ服务器拉消息，然后再回调用户Listener方法<br>
     */
    public static void main(String[] args) throws InterruptedException,
            MQClientException {
        /**
         * 一个应用创建一个Consumer，由应用来维护此对象，可以设置为全局对象或者单例<br>
         * 注意：ConsumerGroupName需要由应用来保证唯一
         */
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(
                "ConsumerGroupName");
        consumer.setNamesrvAddr("192.168.254.10:9876");
        consumer.setInstanceName("Consumber");

        /**
         * 订阅指定topic下tags分别等于TagA或TagC或TagD
         */
        consumer.subscribe("PAY_NOTIFY", "*");
        /**
         * 订阅指定topic下所有消息<br>
         * 注意：一个consumer对象可以订阅多个topic
         */
//        consumer.subscribe("TopicTest2", "*");

        consumer.registerMessageListener(new MessageListenerConcurrently() {

            public ConsumeConcurrentlyStatus consumeMessage(
                    List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                System.out.println(Thread.currentThread().getName()
                        + " Receive New Messages: " + msgs.size());

                for (MessageExt msg : msgs) {
                    System.out.println(new String(msg.getBody(), Charsets.UTF_8));

                }
//                MessageExt msg = msgs.get(0);
//                System.out.println(new String(msg.getBody(), Charsets.UTF_8));
//                if (msg.getTopic().equals("PAY_NOTIFY_QUEUE")) {
//                    //执行TopicTest1的消费逻辑
//                    if (msg.getTags() != null && msg.getTags().equals("TagA")) {
//                        //执行TagA的消费
//                        System.out.println(new String(msg.getBody()));
//                    } else if (msg.getTags() != null
//                            && msg.getTags().equals("TagC")) {
//                        //执行TagC的消费
//                        System.out.println(new String(msg.getBody()));
//                    } else if (msg.getTags() != null
//                            && msg.getTags().equals("TagD")) {
//                        //执行TagD的消费
//                        System.out.println(new String(msg.getBody()));
//                    }
//                } else if (msg.getTopic().equals("TopicTest2")) {
//
//                }

                return ConsumeConcurrentlyStatus.RECONSUME_LATER;

            }
        });

        /**
         * Consumer对象在使用之前必须要调用start初始化，初始化一次即可<br>
         */
        consumer.start();

        System.out.println("ConsumerStarted.");
    }
}

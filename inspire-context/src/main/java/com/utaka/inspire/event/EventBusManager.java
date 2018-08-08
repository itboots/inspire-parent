/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import com.google.common.collect.Maps;
import com.google.common.eventbus.EventBus;
import com.utaka.inspire.util.LogManager;
import org.slf4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.concurrent.ConcurrentMap;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 提供把异步消息单独一个总线，由应用决定异步消息处理的机制；比如可以通过队列进行处理。
 * 在需要处理异步消息的地方调用 {@link EventBusManager#registerAsync(Object)}方法类截获异步的消息，然后进行处理。
 * <b>保证异步事务的完整性</b>
 *
 * @author LANXE
 */
public class EventBusManager implements EventBusService, InitializingBean {

    private static final Logger LOG = LogManager.getCurrentClassLogger();

    private final ConcurrentMap<Class<?>, EventBus> subscribersByType = Maps.newConcurrentMap();

    protected final EventBus asyncBus;

    private boolean debug = false;

    @Autowired
    private Environment env;

    public EventBusManager() {
        this(new EventBus("__async__" + EventBusManager.class.getName()));
    }

    protected EventBusManager(EventBus asyncBus) {
        this.asyncBus = asyncBus;
    }


    @Override
    public void registerAsync(Object handler) {
        this.asyncBus.register(handler);
    }

    @Override
    public void unregisterAsync(Object handler) {
        this.asyncBus.unregister(handler);
    }

    @Override
    public void register(Object object) {
        checkNotNull(object);
        if (this.subscribersByType.containsKey(object.getClass())) {
            this.subscribersByType.get(object.getClass()).register(object);
        } else {
            EventBus bus = new EventBus(object.getClass().getName());
            bus.register(object);
            this.subscribersByType.putIfAbsent(object.getClass(), bus);
        }
    }

    @Override
    public void unregister(Object object) {
        checkNotNull(object);
        if (this.subscribersByType.containsKey(object.getClass())) {
            this.subscribersByType.get(object.getClass()).unregister(object);
        }
    }


    @Override
    public void post(Object event) {
        checkNotNull(event);
        for (EventBus bus : subscribersByType.values()) {
            bus.post(event);
        }

    }

    @Override
    public void asyncPost(Object event) {
        checkNotNull(event);
        if (debug) {
            this.post(event);
        } else {
            this.asyncBus.post(event);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.debug = Boolean.parseBoolean(this.env.getProperty("debug", "false"));

    }

    public static EventBusManager INSTANCE = new EventBusManager();

}

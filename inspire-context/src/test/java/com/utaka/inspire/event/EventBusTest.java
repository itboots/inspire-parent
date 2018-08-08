/*
 * Copyright (c) 2018. utaka and/or its affiliates.
 */

package com.utaka.inspire.event;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.junit.Test;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.atIndex;


/**
 * @author lanxe
 */
public class EventBusTest {

    @Test
    public void testPostEvent1ThenPostEvent2() {
        final ArrayList<String> tracers = Lists.newArrayList();
        final EventBus bus = new EventBus();

        bus.register(new Object() {
            @Subscribe
            public void handle(Event1 event) {
                tracers.add("Step1");
                Event2 e = new Event2(event.id + 1);
                bus.post(e);
                System.out.println("[AfterPostEvent2]" + e.toString());
            }
        });
        bus.register(new Object() {
            @Subscribe
            public void handle(Event2 event) {
                tracers.add("Step2");
            }
        });


        Event e = new Event1(1);
        bus.post(e);
        System.out.println(e.toString());
        assertThat(tracers)
                .isNotEmpty()
                .hasSize(2)
                .contains("Step1", atIndex(0))
                .contains("Step2", atIndex(1))
        ;
    }

    @Test
    public void testLoopPostEvent1AndPostEvent2() {
        final ArrayList<String> tracers = Lists.newArrayList();
        final EventBus bus = new EventBus();

        bus.register(new Object() {
            @Subscribe
            public void handle(Event1 event) {
                tracers.add("Step1");
                if (tracers.size() < 5) {
                    Event e = new Event2(event.id + 1);
                    bus.post(e);
                    System.out.println("[AfterPostEvent2]" + e.toString());
                }

            }
        });
        bus.register(new Object() {
            @Subscribe
            public void handle(Event2 event) {
                tracers.add("Step2");
                if (tracers.size() < 5) {
                    Event e = new Event1(event.id + 1);
                    bus.post(e);
                    System.out.println("[AfterPostEvent1]" + e.toString());
                }
            }
        });

        Event e = new Event1(1);
        bus.post(e);
        System.out.println(e.toString());
        assertThat(tracers)
                .isNotEmpty()
                .hasSize(5)
                .contains("Step1", atIndex(0))
                .contains("Step2", atIndex(1))
                .contains("Step1", atIndex(2))
                .contains("Step2", atIndex(3))
                .contains("Step1", atIndex(4))
        ;
    }


    @Test
    public void testLoopPostEvent5() {
        final ArrayList<String> tracers = Lists.newArrayList();
        final EventBus bus = new EventBus();

        bus.register(new Object() {
            @Subscribe
            public void handle(Event1 event) {
                tracers.add("Step1");
                if (tracers.size() < 5) {
                    Event e = new Event1(event.id + 1);
                    bus.post(e);
                    System.out.println("[AfterPostEvent1]" + e.toString());
                }

            }
        });

        Event e = new Event1(1);
        bus.post(e);
        System.out.println(e.toString());
        assertThat(tracers)
                .isNotEmpty()
                .hasSize(5)
                .contains("Step1", atIndex(0))
                .contains("Step1", atIndex(1))
                .contains("Step1", atIndex(2))
                .contains("Step1", atIndex(3))
                .contains("Step1", atIndex(4))
        ;
    }


    private static class Event {
        protected final int id;

        Event(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("thread", Thread.currentThread().getName())
                    .add("id", this.id)
                    .toString();
        }
    }

    private static class Event1 extends Event {

        Event1(int id) {
            super(id);
        }


    }

    private static class Event2 extends Event {
        Event2(int id) {
            super(id);
        }

    }

    private static class Event3 extends Event {
        Event3(int id) {
            super(id);
        }

    }
}

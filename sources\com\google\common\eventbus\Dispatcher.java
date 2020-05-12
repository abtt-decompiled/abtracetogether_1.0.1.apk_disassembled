package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

abstract class Dispatcher {

    private static final class ImmediateDispatcher extends Dispatcher {
        /* access modifiers changed from: private */
        public static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();

        private ImmediateDispatcher() {
        }

        /* access modifiers changed from: 0000 */
        public void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            while (it.hasNext()) {
                ((Subscriber) it.next()).dispatchEvent(obj);
            }
        }
    }

    private static final class LegacyAsyncDispatcher extends Dispatcher {
        private final ConcurrentLinkedQueue<EventWithSubscriber> queue;

        private static final class EventWithSubscriber {
            /* access modifiers changed from: private */
            public final Object event;
            /* access modifiers changed from: private */
            public final Subscriber subscriber;

            private EventWithSubscriber(Object obj, Subscriber subscriber2) {
                this.event = obj;
                this.subscriber = subscriber2;
            }
        }

        private LegacyAsyncDispatcher() {
            this.queue = Queues.newConcurrentLinkedQueue();
        }

        /* access modifiers changed from: 0000 */
        public void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            while (it.hasNext()) {
                this.queue.add(new EventWithSubscriber(obj, (Subscriber) it.next()));
            }
            while (true) {
                EventWithSubscriber eventWithSubscriber = (EventWithSubscriber) this.queue.poll();
                if (eventWithSubscriber != null) {
                    eventWithSubscriber.subscriber.dispatchEvent(eventWithSubscriber.event);
                } else {
                    return;
                }
            }
        }
    }

    private static final class PerThreadQueuedDispatcher extends Dispatcher {
        private final ThreadLocal<Boolean> dispatching;
        private final ThreadLocal<Queue<Event>> queue;

        private static final class Event {
            /* access modifiers changed from: private */
            public final Object event;
            /* access modifiers changed from: private */
            public final Iterator<Subscriber> subscribers;

            private Event(Object obj, Iterator<Subscriber> it) {
                this.event = obj;
                this.subscribers = it;
            }
        }

        private PerThreadQueuedDispatcher() {
            this.queue = new ThreadLocal<Queue<Event>>() {
                /* access modifiers changed from: protected */
                public Queue<Event> initialValue() {
                    return Queues.newArrayDeque();
                }
            };
            this.dispatching = new ThreadLocal<Boolean>() {
                /* access modifiers changed from: protected */
                public Boolean initialValue() {
                    return Boolean.valueOf(false);
                }
            };
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:13:0x0053 A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:6:0x0037 A[Catch:{ all -> 0x005e }, LOOP:1: B:6:0x0037->B:8:0x0041, LOOP_START] */
        public void dispatch(Object obj, Iterator<Subscriber> it) {
            Preconditions.checkNotNull(obj);
            Preconditions.checkNotNull(it);
            Queue queue2 = (Queue) this.queue.get();
            queue2.offer(new Event(obj, it));
            if (!((Boolean) this.dispatching.get()).booleanValue()) {
                this.dispatching.set(Boolean.valueOf(true));
                while (true) {
                    try {
                        Event event = (Event) queue2.poll();
                        if (event == null) {
                            while (true) {
                                if (event.subscribers.hasNext()) {
                                    ((Subscriber) event.subscribers.next()).dispatchEvent(event.event);
                                }
                            }
                            Event event2 = (Event) queue2.poll();
                            if (event2 == null) {
                                return;
                            }
                        }
                    } finally {
                        this.dispatching.remove();
                        this.queue.remove();
                    }
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public abstract void dispatch(Object obj, Iterator<Subscriber> it);

    Dispatcher() {
    }

    static Dispatcher perThreadDispatchQueue() {
        return new PerThreadQueuedDispatcher();
    }

    static Dispatcher legacyAsync() {
        return new LegacyAsyncDispatcher();
    }

    static Dispatcher immediate() {
        return ImmediateDispatcher.INSTANCE;
    }
}

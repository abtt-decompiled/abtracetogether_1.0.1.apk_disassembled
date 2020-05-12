package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.internal.util.NotificationLite;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublish<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, FlowablePublishClassic<T> {
    static final long CANCELLED = Long.MIN_VALUE;
    final int bufferSize;
    final AtomicReference<PublishSubscriber<T>> current;
    final Publisher<T> onSubscribe;
    final Flowable<T> source;

    static final class FlowablePublisher<T> implements Publisher<T> {
        private final int bufferSize;
        private final AtomicReference<PublishSubscriber<T>> curr;

        FlowablePublisher(AtomicReference<PublishSubscriber<T>> atomicReference, int i) {
            this.curr = atomicReference;
            this.bufferSize = i;
        }

        public void subscribe(Subscriber<? super T> subscriber) {
            PublishSubscriber<T> publishSubscriber;
            InnerSubscriber innerSubscriber = new InnerSubscriber(subscriber);
            subscriber.onSubscribe(innerSubscriber);
            while (true) {
                publishSubscriber = (PublishSubscriber) this.curr.get();
                if (publishSubscriber == null || publishSubscriber.isDisposed()) {
                    PublishSubscriber<T> publishSubscriber2 = new PublishSubscriber<>(this.curr, this.bufferSize);
                    if (!this.curr.compareAndSet(publishSubscriber, publishSubscriber2)) {
                        continue;
                    } else {
                        publishSubscriber = publishSubscriber2;
                    }
                }
                if (publishSubscriber.add(innerSubscriber)) {
                    break;
                }
            }
            if (innerSubscriber.get() == Long.MIN_VALUE) {
                publishSubscriber.remove(innerSubscriber);
            } else {
                innerSubscriber.parent = publishSubscriber;
            }
            publishSubscriber.dispatch();
        }
    }

    static final class InnerSubscriber<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = -4453897557930727610L;
        final Subscriber<? super T> child;
        long emitted;
        volatile PublishSubscriber<T> parent;

        InnerSubscriber(Subscriber<? super T> subscriber) {
            this.child = subscriber;
        }

        public void request(long j) {
            if (SubscriptionHelper.validate(j)) {
                BackpressureHelper.addCancel(this, j);
                PublishSubscriber<T> publishSubscriber = this.parent;
                if (publishSubscriber != null) {
                    publishSubscriber.dispatch();
                }
            }
        }

        public void cancel() {
            if (get() != Long.MIN_VALUE && getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                PublishSubscriber<T> publishSubscriber = this.parent;
                if (publishSubscriber != null) {
                    publishSubscriber.remove(this);
                    publishSubscriber.dispatch();
                }
            }
        }
    }

    static final class PublishSubscriber<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscriber[] EMPTY = new InnerSubscriber[0];
        static final InnerSubscriber[] TERMINATED = new InnerSubscriber[0];
        private static final long serialVersionUID = -202316842419149694L;
        final int bufferSize;
        final AtomicReference<PublishSubscriber<T>> current;
        volatile SimpleQueue<T> queue;
        final AtomicBoolean shouldConnect;
        int sourceMode;
        final AtomicReference<InnerSubscriber<T>[]> subscribers = new AtomicReference<>(EMPTY);
        volatile Object terminalEvent;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        PublishSubscriber(AtomicReference<PublishSubscriber<T>> atomicReference, int i) {
            this.current = atomicReference;
            this.shouldConnect = new AtomicBoolean();
            this.bufferSize = i;
        }

        public void dispose() {
            Object obj = this.subscribers.get();
            Object obj2 = TERMINATED;
            if (obj != obj2 && ((InnerSubscriber[]) this.subscribers.getAndSet(obj2)) != TERMINATED) {
                this.current.compareAndSet(this, null);
                SubscriptionHelper.cancel(this.upstream);
            }
        }

        public boolean isDisposed() {
            return this.subscribers.get() == TERMINATED;
        }

        public void onSubscribe(Subscription subscription) {
            if (SubscriptionHelper.setOnce(this.upstream, subscription)) {
                if (subscription instanceof QueueSubscription) {
                    QueueSubscription queueSubscription = (QueueSubscription) subscription;
                    int requestFusion = queueSubscription.requestFusion(7);
                    if (requestFusion == 1) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        this.terminalEvent = NotificationLite.complete();
                        dispatch();
                        return;
                    } else if (requestFusion == 2) {
                        this.sourceMode = requestFusion;
                        this.queue = queueSubscription;
                        subscription.request((long) this.bufferSize);
                        return;
                    }
                }
                this.queue = new SpscArrayQueue(this.bufferSize);
                subscription.request((long) this.bufferSize);
            }
        }

        public void onNext(T t) {
            if (this.sourceMode != 0 || this.queue.offer(t)) {
                dispatch();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        public void onError(Throwable th) {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.error(th);
                dispatch();
                return;
            }
            RxJavaPlugins.onError(th);
        }

        public void onComplete() {
            if (this.terminalEvent == null) {
                this.terminalEvent = NotificationLite.complete();
                dispatch();
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerSubscriber<T> innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                if (innerSubscriberArr == TERMINATED) {
                    return false;
                }
                int length = innerSubscriberArr.length;
                innerSubscriberArr2 = new InnerSubscriber[(length + 1)];
                System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr2, 0, length);
                innerSubscriberArr2[length] = innerSubscriber;
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerSubscriber<T> innerSubscriber) {
            InnerSubscriber[] innerSubscriberArr;
            InnerSubscriber[] innerSubscriberArr2;
            do {
                innerSubscriberArr = (InnerSubscriber[]) this.subscribers.get();
                int length = innerSubscriberArr.length;
                if (length == 0) {
                    break;
                }
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (innerSubscriberArr[i2].equals(innerSubscriber)) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        innerSubscriberArr2 = EMPTY;
                    } else {
                        InnerSubscriber[] innerSubscriberArr3 = new InnerSubscriber[(length - 1)];
                        System.arraycopy(innerSubscriberArr, 0, innerSubscriberArr3, 0, i);
                        System.arraycopy(innerSubscriberArr, i + 1, innerSubscriberArr3, i, (length - i) - 1);
                        innerSubscriberArr2 = innerSubscriberArr3;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(innerSubscriberArr, innerSubscriberArr2));
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(Object obj, boolean z) {
            int i = 0;
            if (obj != null) {
                if (!NotificationLite.isComplete(obj)) {
                    Throwable error = NotificationLite.getError(obj);
                    this.current.compareAndSet(this, null);
                    InnerSubscriber[] innerSubscriberArr = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    if (innerSubscriberArr.length != 0) {
                        int length = innerSubscriberArr.length;
                        while (i < length) {
                            innerSubscriberArr[i].child.onError(error);
                            i++;
                        }
                    } else {
                        RxJavaPlugins.onError(error);
                    }
                    return true;
                } else if (z) {
                    this.current.compareAndSet(this, null);
                    InnerSubscriber[] innerSubscriberArr2 = (InnerSubscriber[]) this.subscribers.getAndSet(TERMINATED);
                    int length2 = innerSubscriberArr2.length;
                    while (i < length2) {
                        innerSubscriberArr2[i].child.onComplete();
                        i++;
                    }
                    return true;
                }
            }
            return false;
        }

        /* access modifiers changed from: 0000 */
        /* JADX WARNING: Code restructure failed: missing block: B:74:0x013b, code lost:
            if (r11 == 0) goto L_0x014e;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:75:0x013d, code lost:
            r3 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x0140, code lost:
            if (r1.sourceMode == 1) goto L_0x014f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:77:0x0142, code lost:
            ((org.reactivestreams.Subscription) r1.upstream.get()).request(r12);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x014e, code lost:
            r3 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:0x0153, code lost:
            if (r14 == 0) goto L_0x0159;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:81:0x0155, code lost:
            if (r8 != false) goto L_0x0159;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:94:0x0014, code lost:
            continue;
         */
        public void dispatch() {
            Object obj;
            InnerSubscriber[] innerSubscriberArr;
            boolean z;
            SimpleQueue<T> simpleQueue;
            Object obj2;
            if (getAndIncrement() == 0) {
                AtomicReference<InnerSubscriber<T>[]> atomicReference = this.subscribers;
                boolean z2 = true;
                InnerSubscriber[] innerSubscriberArr2 = (InnerSubscriber[]) atomicReference.get();
                int i = 1;
                while (true) {
                    Object obj3 = this.terminalEvent;
                    SimpleQueue<T> simpleQueue2 = this.queue;
                    boolean z3 = (simpleQueue2 == null || simpleQueue2.isEmpty()) ? z2 : false;
                    if (!checkTerminated(obj3, z3)) {
                        if (!z3) {
                            int length = innerSubscriberArr2.length;
                            int i2 = 0;
                            long j = LongCompanionObject.MAX_VALUE;
                            for (InnerSubscriber innerSubscriber : innerSubscriberArr2) {
                                long j2 = innerSubscriber.get();
                                if (j2 != Long.MIN_VALUE) {
                                    j = Math.min(j, j2 - innerSubscriber.emitted);
                                } else {
                                    i2++;
                                }
                            }
                            if (length == i2) {
                                Object obj4 = this.terminalEvent;
                                try {
                                    obj2 = simpleQueue2.poll();
                                } catch (Throwable th) {
                                    Throwable th2 = th;
                                    Exceptions.throwIfFatal(th2);
                                    ((Subscription) this.upstream.get()).cancel();
                                    obj4 = NotificationLite.error(th2);
                                    this.terminalEvent = obj4;
                                    obj2 = null;
                                }
                                if (!checkTerminated(obj4, obj2 == null ? z2 : false)) {
                                    if (this.sourceMode != z2) {
                                        ((Subscription) this.upstream.get()).request(1);
                                    }
                                } else {
                                    return;
                                }
                            } else {
                                int i3 = 0;
                                while (true) {
                                    long j3 = (long) i3;
                                    if (j3 >= j) {
                                        break;
                                    }
                                    Object obj5 = this.terminalEvent;
                                    try {
                                        obj = simpleQueue2.poll();
                                    } catch (Throwable th3) {
                                        Throwable th4 = th3;
                                        Exceptions.throwIfFatal(th4);
                                        ((Subscription) this.upstream.get()).cancel();
                                        obj5 = NotificationLite.error(th4);
                                        this.terminalEvent = obj5;
                                        obj = null;
                                    }
                                    boolean z4 = obj == null ? z2 : false;
                                    if (!checkTerminated(obj5, z4)) {
                                        if (z4) {
                                            z3 = z4;
                                            break;
                                        }
                                        Object value = NotificationLite.getValue(obj);
                                        int length2 = innerSubscriberArr2.length;
                                        int i4 = 0;
                                        boolean z5 = false;
                                        while (i4 < length2) {
                                            InnerSubscriber innerSubscriber2 = innerSubscriberArr2[i4];
                                            long j4 = innerSubscriber2.get();
                                            if (j4 != Long.MIN_VALUE) {
                                                if (j4 != LongCompanionObject.MAX_VALUE) {
                                                    simpleQueue = simpleQueue2;
                                                    z = z4;
                                                    innerSubscriber2.emitted++;
                                                } else {
                                                    simpleQueue = simpleQueue2;
                                                    z = z4;
                                                }
                                                innerSubscriber2.child.onNext(value);
                                            } else {
                                                simpleQueue = simpleQueue2;
                                                z = z4;
                                                z5 = true;
                                            }
                                            i4++;
                                            simpleQueue2 = simpleQueue;
                                            z4 = z;
                                        }
                                        SimpleQueue<T> simpleQueue3 = simpleQueue2;
                                        boolean z6 = z4;
                                        i3++;
                                        innerSubscriberArr = (InnerSubscriber[]) atomicReference.get();
                                        if (!z5 && innerSubscriberArr == innerSubscriberArr2) {
                                            simpleQueue2 = simpleQueue3;
                                            z3 = z6;
                                            z2 = true;
                                        } else if (!(i3 == 0 || this.sourceMode == 1)) {
                                            ((Subscription) this.upstream.get()).request((long) i3);
                                        }
                                    } else {
                                        return;
                                    }
                                }
                                ((Subscription) this.upstream.get()).request((long) i3);
                                innerSubscriberArr2 = innerSubscriberArr;
                                z2 = true;
                            }
                        }
                        i = addAndGet(-i);
                        if (i != 0) {
                            innerSubscriberArr2 = (InnerSubscriber[]) atomicReference.get();
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }
    }

    public static <T> ConnectableFlowable<T> create(Flowable<T> flowable, int i) {
        AtomicReference atomicReference = new AtomicReference();
        return RxJavaPlugins.onAssembly((ConnectableFlowable<T>) new FlowablePublish<T>(new FlowablePublisher(atomicReference, i), flowable, atomicReference, i));
    }

    private FlowablePublish(Publisher<T> publisher, Flowable<T> flowable, AtomicReference<PublishSubscriber<T>> atomicReference, int i) {
        this.onSubscribe = publisher;
        this.source = flowable;
        this.current = atomicReference;
        this.bufferSize = i;
    }

    public Publisher<T> source() {
        return this.source;
    }

    public int publishBufferSize() {
        return this.bufferSize;
    }

    public Publisher<T> publishSource() {
        return this.source;
    }

    /* access modifiers changed from: protected */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        this.onSubscribe.subscribe(subscriber);
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    public void connect(Consumer<? super Disposable> consumer) {
        PublishSubscriber publishSubscriber;
        while (true) {
            publishSubscriber = (PublishSubscriber) this.current.get();
            if (publishSubscriber != null && !publishSubscriber.isDisposed()) {
                break;
            }
            PublishSubscriber publishSubscriber2 = new PublishSubscriber(this.current, this.bufferSize);
            if (this.current.compareAndSet(publishSubscriber, publishSubscriber2)) {
                publishSubscriber = publishSubscriber2;
                break;
            }
        }
        boolean z = true;
        if (publishSubscriber.shouldConnect.get() || !publishSubscriber.shouldConnect.compareAndSet(false, true)) {
            z = false;
        }
        try {
            consumer.accept(publishSubscriber);
            if (z) {
                this.source.subscribe((FlowableSubscriber<? super T>) publishSubscriber);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }
}

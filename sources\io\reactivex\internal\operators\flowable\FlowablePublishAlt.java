package io.reactivex.internal.operators.flowable;

import io.reactivex.FlowableSubscriber;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.exceptions.MissingBackpressureException;
import io.reactivex.flowables.ConnectableFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.internal.disposables.ResettableConnectable;
import io.reactivex.internal.fuseable.HasUpstreamPublisher;
import io.reactivex.internal.fuseable.QueueSubscription;
import io.reactivex.internal.fuseable.SimpleQueue;
import io.reactivex.internal.queue.SpscArrayQueue;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import io.reactivex.internal.util.ExceptionHelper;
import io.reactivex.plugins.RxJavaPlugins;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public final class FlowablePublishAlt<T> extends ConnectableFlowable<T> implements HasUpstreamPublisher<T>, ResettableConnectable {
    final int bufferSize;
    final AtomicReference<PublishConnection<T>> current = new AtomicReference<>();
    final Publisher<T> source;

    static final class InnerSubscription<T> extends AtomicLong implements Subscription {
        private static final long serialVersionUID = 2845000326761540265L;
        final Subscriber<? super T> downstream;
        long emitted;
        final PublishConnection<T> parent;

        InnerSubscription(Subscriber<? super T> subscriber, PublishConnection<T> publishConnection) {
            this.downstream = subscriber;
            this.parent = publishConnection;
        }

        public void request(long j) {
            BackpressureHelper.addCancel(this, j);
            this.parent.drain();
        }

        public void cancel() {
            if (getAndSet(Long.MIN_VALUE) != Long.MIN_VALUE) {
                this.parent.remove(this);
                this.parent.drain();
            }
        }

        public boolean isCancelled() {
            return get() == Long.MIN_VALUE;
        }
    }

    static final class PublishConnection<T> extends AtomicInteger implements FlowableSubscriber<T>, Disposable {
        static final InnerSubscription[] EMPTY = new InnerSubscription[0];
        static final InnerSubscription[] TERMINATED = new InnerSubscription[0];
        private static final long serialVersionUID = -1672047311619175801L;
        final int bufferSize;
        final AtomicBoolean connect = new AtomicBoolean();
        int consumed;
        final AtomicReference<PublishConnection<T>> current;
        volatile boolean done;
        Throwable error;
        volatile SimpleQueue<T> queue;
        int sourceMode;
        final AtomicReference<InnerSubscription<T>[]> subscribers;
        final AtomicReference<Subscription> upstream = new AtomicReference<>();

        PublishConnection(AtomicReference<PublishConnection<T>> atomicReference, int i) {
            this.current = atomicReference;
            this.bufferSize = i;
            this.subscribers = new AtomicReference<>(EMPTY);
        }

        public void dispose() {
            this.subscribers.getAndSet(TERMINATED);
            this.current.compareAndSet(this, null);
            SubscriptionHelper.cancel(this.upstream);
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
                        this.done = true;
                        drain();
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
                drain();
            } else {
                onError(new MissingBackpressureException("Prefetch queue is full?!"));
            }
        }

        public void onError(Throwable th) {
            if (this.done) {
                RxJavaPlugins.onError(th);
                return;
            }
            this.error = th;
            this.done = true;
            drain();
        }

        public void onComplete() {
            this.done = true;
            drain();
        }

        /* access modifiers changed from: 0000 */
        public void drain() {
            if (getAndIncrement() == 0) {
                SimpleQueue<T> simpleQueue = this.queue;
                int i = this.consumed;
                int i2 = this.bufferSize;
                int i3 = i2 - (i2 >> 2);
                boolean z = this.sourceMode != 1;
                int i4 = 1;
                int i5 = i;
                SimpleQueue<T> simpleQueue2 = simpleQueue;
                int i6 = i5;
                while (true) {
                    if (simpleQueue2 != null) {
                        long j = LongCompanionObject.MAX_VALUE;
                        InnerSubscription[] innerSubscriptionArr = (InnerSubscription[]) this.subscribers.get();
                        boolean z2 = false;
                        for (InnerSubscription innerSubscription : innerSubscriptionArr) {
                            long j2 = innerSubscription.get();
                            if (j2 != Long.MIN_VALUE) {
                                j = Math.min(j2 - innerSubscription.emitted, j);
                                z2 = true;
                            }
                        }
                        long j3 = 0;
                        if (!z2) {
                            j = 0;
                        }
                        while (true) {
                            if (j == j3) {
                                break;
                            }
                            boolean z3 = this.done;
                            try {
                                Object poll = simpleQueue2.poll();
                                boolean z4 = poll == null;
                                if (!checkTerminated(z3, z4)) {
                                    if (z4) {
                                        break;
                                    }
                                    int length = innerSubscriptionArr.length;
                                    for (int i7 = 0; i7 < length; i7++) {
                                        InnerSubscription innerSubscription2 = innerSubscriptionArr[i7];
                                        if (!innerSubscription2.isCancelled()) {
                                            innerSubscription2.downstream.onNext(poll);
                                            innerSubscription2.emitted++;
                                        }
                                    }
                                    if (z) {
                                        i6++;
                                        if (i6 == i3) {
                                            ((Subscription) this.upstream.get()).request((long) i3);
                                            i6 = 0;
                                        }
                                    }
                                    j--;
                                    if (innerSubscriptionArr != this.subscribers.get()) {
                                        break;
                                    }
                                    j3 = 0;
                                } else {
                                    return;
                                }
                            } catch (Throwable th) {
                                Throwable th2 = th;
                                Exceptions.throwIfFatal(th2);
                                ((Subscription) this.upstream.get()).cancel();
                                simpleQueue2.clear();
                                this.done = true;
                                signalError(th2);
                                return;
                            }
                        }
                        if (checkTerminated(this.done, simpleQueue2.isEmpty())) {
                            return;
                        }
                    }
                    this.consumed = i6;
                    i4 = addAndGet(-i4);
                    if (i4 != 0) {
                        if (simpleQueue2 == null) {
                            simpleQueue2 = this.queue;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean checkTerminated(boolean z, boolean z2) {
            InnerSubscription[] innerSubscriptionArr;
            if (!z || !z2) {
                return false;
            }
            Throwable th = this.error;
            if (th != null) {
                signalError(th);
            } else {
                for (InnerSubscription innerSubscription : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                    if (!innerSubscription.isCancelled()) {
                        innerSubscription.downstream.onComplete();
                    }
                }
            }
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void signalError(Throwable th) {
            InnerSubscription[] innerSubscriptionArr;
            for (InnerSubscription innerSubscription : (InnerSubscription[]) this.subscribers.getAndSet(TERMINATED)) {
                if (!innerSubscription.isCancelled()) {
                    innerSubscription.downstream.onError(th);
                }
            }
        }

        /* access modifiers changed from: 0000 */
        public boolean add(InnerSubscription<T> innerSubscription) {
            InnerSubscription[] innerSubscriptionArr;
            InnerSubscription[] innerSubscriptionArr2;
            do {
                innerSubscriptionArr = (InnerSubscription[]) this.subscribers.get();
                if (innerSubscriptionArr == TERMINATED) {
                    return false;
                }
                int length = innerSubscriptionArr.length;
                innerSubscriptionArr2 = new InnerSubscription[(length + 1)];
                System.arraycopy(innerSubscriptionArr, 0, innerSubscriptionArr2, 0, length);
                innerSubscriptionArr2[length] = innerSubscription;
            } while (!this.subscribers.compareAndSet(innerSubscriptionArr, innerSubscriptionArr2));
            return true;
        }

        /* access modifiers changed from: 0000 */
        public void remove(InnerSubscription<T> innerSubscription) {
            InnerSubscription<T>[] innerSubscriptionArr;
            InnerSubscription[] innerSubscriptionArr2;
            do {
                innerSubscriptionArr = (InnerSubscription[]) this.subscribers.get();
                int length = innerSubscriptionArr.length;
                if (length == 0) {
                    break;
                }
                int i = -1;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    } else if (innerSubscriptionArr[i2] == innerSubscription) {
                        i = i2;
                        break;
                    } else {
                        i2++;
                    }
                }
                if (i >= 0) {
                    if (length == 1) {
                        innerSubscriptionArr2 = EMPTY;
                    } else {
                        InnerSubscription[] innerSubscriptionArr3 = new InnerSubscription[(length - 1)];
                        System.arraycopy(innerSubscriptionArr, 0, innerSubscriptionArr3, 0, i);
                        System.arraycopy(innerSubscriptionArr, i + 1, innerSubscriptionArr3, i, (length - i) - 1);
                        innerSubscriptionArr2 = innerSubscriptionArr3;
                    }
                } else {
                    return;
                }
            } while (!this.subscribers.compareAndSet(innerSubscriptionArr, innerSubscriptionArr2));
        }
    }

    public FlowablePublishAlt(Publisher<T> publisher, int i) {
        this.source = publisher;
        this.bufferSize = i;
    }

    public Publisher<T> source() {
        return this.source;
    }

    public int publishBufferSize() {
        return this.bufferSize;
    }

    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    public void connect(Consumer<? super Disposable> consumer) {
        PublishConnection publishConnection;
        while (true) {
            publishConnection = (PublishConnection) this.current.get();
            if (publishConnection != null && !publishConnection.isDisposed()) {
                break;
            }
            PublishConnection publishConnection2 = new PublishConnection(this.current, this.bufferSize);
            if (this.current.compareAndSet(publishConnection, publishConnection2)) {
                publishConnection = publishConnection2;
                break;
            }
        }
        boolean z = true;
        if (publishConnection.connect.get() || !publishConnection.connect.compareAndSet(false, true)) {
            z = false;
        }
        try {
            consumer.accept(publishConnection);
            if (z) {
                this.source.subscribe(publishConnection);
            }
        } catch (Throwable th) {
            Exceptions.throwIfFatal(th);
            throw ExceptionHelper.wrapOrThrow(th);
        }
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:0:0x0000 A[LOOP_START] */
    public void subscribeActual(Subscriber<? super T> subscriber) {
        PublishConnection publishConnection;
        while (true) {
            publishConnection = (PublishConnection) this.current.get();
            if (publishConnection != null) {
                break;
            }
            PublishConnection publishConnection2 = new PublishConnection(this.current, this.bufferSize);
            if (this.current.compareAndSet(publishConnection, publishConnection2)) {
                publishConnection = publishConnection2;
                break;
            }
        }
        InnerSubscription innerSubscription = new InnerSubscription(subscriber, publishConnection);
        subscriber.onSubscribe(innerSubscription);
        if (publishConnection.add(innerSubscription)) {
            if (innerSubscription.isCancelled()) {
                publishConnection.remove(innerSubscription);
            } else {
                publishConnection.drain();
            }
            return;
        }
        Throwable th = publishConnection.error;
        if (th != null) {
            subscriber.onError(th);
        } else {
            subscriber.onComplete();
        }
    }

    public void resetIf(Disposable disposable) {
        this.current.compareAndSet((PublishConnection) disposable, null);
    }
}

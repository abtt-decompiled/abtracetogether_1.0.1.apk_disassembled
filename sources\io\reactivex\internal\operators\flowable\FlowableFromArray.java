package io.reactivex.internal.operators.flowable;

import io.reactivex.Flowable;
import io.reactivex.internal.functions.ObjectHelper;
import io.reactivex.internal.fuseable.ConditionalSubscriber;
import io.reactivex.internal.subscriptions.BasicQueueSubscription;
import io.reactivex.internal.subscriptions.SubscriptionHelper;
import io.reactivex.internal.util.BackpressureHelper;
import kotlin.jvm.internal.LongCompanionObject;
import org.reactivestreams.Subscriber;

public final class FlowableFromArray<T> extends Flowable<T> {
    final T[] array;

    static final class ArrayConditionalSubscription<T> extends BaseArraySubscription<T> {
        private static final long serialVersionUID = 2587302975077663557L;
        final ConditionalSubscriber<? super T> downstream;

        ArrayConditionalSubscription(ConditionalSubscriber<? super T> conditionalSubscriber, T[] tArr) {
            super(tArr);
            this.downstream = conditionalSubscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Object[] objArr = this.array;
            int length = objArr.length;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
            int i = this.index;
            while (i != length) {
                if (!this.cancelled) {
                    Object obj = objArr[i];
                    if (obj == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("The element at index ");
                        sb.append(i);
                        sb.append(" is null");
                        conditionalSubscriber.onError(new NullPointerException(sb.toString()));
                        return;
                    }
                    conditionalSubscriber.tryOnNext(obj);
                    i++;
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                conditionalSubscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Object[] objArr = this.array;
            int length = objArr.length;
            int i = this.index;
            ConditionalSubscriber<? super T> conditionalSubscriber = this.downstream;
            do {
                long j2 = 0;
                while (true) {
                    if (j2 == j || i == length) {
                        if (i == length) {
                            if (!this.cancelled) {
                                conditionalSubscriber.onComplete();
                            }
                            return;
                        }
                        j = get();
                        if (j2 == j) {
                            this.index = i;
                            j = addAndGet(-j2);
                        }
                    } else if (!this.cancelled) {
                        Object obj = objArr[i];
                        if (obj == null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("The element at index ");
                            sb.append(i);
                            sb.append(" is null");
                            conditionalSubscriber.onError(new NullPointerException(sb.toString()));
                            return;
                        }
                        if (conditionalSubscriber.tryOnNext(obj)) {
                            j2++;
                        }
                        i++;
                    } else {
                        return;
                    }
                }
            } while (j != 0);
        }
    }

    static final class ArraySubscription<T> extends BaseArraySubscription<T> {
        private static final long serialVersionUID = 2587302975077663557L;
        final Subscriber<? super T> downstream;

        ArraySubscription(Subscriber<? super T> subscriber, T[] tArr) {
            super(tArr);
            this.downstream = subscriber;
        }

        /* access modifiers changed from: 0000 */
        public void fastPath() {
            Object[] objArr = this.array;
            int length = objArr.length;
            Subscriber<? super T> subscriber = this.downstream;
            int i = this.index;
            while (i != length) {
                if (!this.cancelled) {
                    Object obj = objArr[i];
                    if (obj == null) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("The element at index ");
                        sb.append(i);
                        sb.append(" is null");
                        subscriber.onError(new NullPointerException(sb.toString()));
                        return;
                    }
                    subscriber.onNext(obj);
                    i++;
                } else {
                    return;
                }
            }
            if (!this.cancelled) {
                subscriber.onComplete();
            }
        }

        /* access modifiers changed from: 0000 */
        public void slowPath(long j) {
            Object[] objArr = this.array;
            int length = objArr.length;
            int i = this.index;
            Subscriber<? super T> subscriber = this.downstream;
            do {
                long j2 = 0;
                while (true) {
                    if (j2 == j || i == length) {
                        if (i == length) {
                            if (!this.cancelled) {
                                subscriber.onComplete();
                            }
                            return;
                        }
                        j = get();
                        if (j2 == j) {
                            this.index = i;
                            j = addAndGet(-j2);
                        }
                    } else if (!this.cancelled) {
                        Object obj = objArr[i];
                        if (obj == null) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("The element at index ");
                            sb.append(i);
                            sb.append(" is null");
                            subscriber.onError(new NullPointerException(sb.toString()));
                            return;
                        }
                        subscriber.onNext(obj);
                        j2++;
                        i++;
                    } else {
                        return;
                    }
                }
            } while (j != 0);
        }
    }

    static abstract class BaseArraySubscription<T> extends BasicQueueSubscription<T> {
        private static final long serialVersionUID = -2252972430506210021L;
        final T[] array;
        volatile boolean cancelled;
        int index;

        /* access modifiers changed from: 0000 */
        public abstract void fastPath();

        public final int requestFusion(int i) {
            return i & 1;
        }

        /* access modifiers changed from: 0000 */
        public abstract void slowPath(long j);

        BaseArraySubscription(T[] tArr) {
            this.array = tArr;
        }

        public final T poll() {
            int i = this.index;
            T[] tArr = this.array;
            if (i == tArr.length) {
                return null;
            }
            this.index = i + 1;
            return ObjectHelper.requireNonNull(tArr[i], "array element is null");
        }

        public final boolean isEmpty() {
            return this.index == this.array.length;
        }

        public final void clear() {
            this.index = this.array.length;
        }

        public final void request(long j) {
            if (SubscriptionHelper.validate(j) && BackpressureHelper.add(this, j) == 0) {
                if (j == LongCompanionObject.MAX_VALUE) {
                    fastPath();
                } else {
                    slowPath(j);
                }
            }
        }

        public final void cancel() {
            this.cancelled = true;
        }
    }

    public FlowableFromArray(T[] tArr) {
        this.array = tArr;
    }

    public void subscribeActual(Subscriber<? super T> subscriber) {
        if (subscriber instanceof ConditionalSubscriber) {
            subscriber.onSubscribe(new ArrayConditionalSubscription((ConditionalSubscriber) subscriber, this.array));
        } else {
            subscriber.onSubscribe(new ArraySubscription(subscriber, this.array));
        }
    }
}

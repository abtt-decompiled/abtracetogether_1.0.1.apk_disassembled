package kotlinx.coroutines.sync;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.internal.SegmentQueue;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0010\t\n\u0002\b\b\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0002\u0018\u00002\u00020\u00012\b\u0012\u0004\u0012\u00020\u00030\u0002B\u0017\u0012\u0006\u0010\u0005\u001a\u00020\u0004\u0012\u0006\u0010\u0006\u001a\u00020\u0004¢\u0006\u0004\b\u0007\u0010\bJ\u0013\u0010\n\u001a\u00020\tH@ø\u0001\u0000¢\u0006\u0004\b\n\u0010\u000bJ\u0013\u0010\f\u001a\u00020\tH@ø\u0001\u0000¢\u0006\u0004\b\f\u0010\u000bJ\r\u0010\r\u001a\u00020\u0004¢\u0006\u0004\b\r\u0010\u000eJ!\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0010\u001a\u00020\u000f2\b\u0010\u0011\u001a\u0004\u0018\u00010\u0003H\u0016¢\u0006\u0004\b\u0012\u0010\u0013J\u000f\u0010\u0014\u001a\u00020\tH\u0016¢\u0006\u0004\b\u0014\u0010\u0015J\u000f\u0010\u0017\u001a\u00020\tH\u0000¢\u0006\u0004\b\u0016\u0010\u0015J\u000f\u0010\u0019\u001a\u00020\u0018H\u0016¢\u0006\u0004\b\u0019\u0010\u001aR\u0016\u0010\u001c\u001a\u00020\u00048V@\u0016X\u0004¢\u0006\u0006\u001a\u0004\b\u001b\u0010\u000eR\u0016\u0010\u0005\u001a\u00020\u00048\u0002@\u0002X\u0004¢\u0006\u0006\n\u0004\b\u0005\u0010\u001d\u0002\u0004\n\u0002\b\u0019¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/sync/SemaphoreImpl;", "Lkotlinx/coroutines/sync/Semaphore;", "Lkotlinx/coroutines/internal/SegmentQueue;", "Lkotlinx/coroutines/sync/SemaphoreSegment;", "", "permits", "acquiredPermits", "<init>", "(II)V", "", "acquire", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "addToQueueAndSuspend", "incPermits", "()I", "", "id", "prev", "newSegment", "(JLkotlinx/coroutines/sync/SemaphoreSegment;)Lkotlinx/coroutines/sync/SemaphoreSegment;", "release", "()V", "resumeNextFromQueue$kotlinx_coroutines_core", "resumeNextFromQueue", "", "tryAcquire", "()Z", "getAvailablePermits", "availablePermits", "I", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: Semaphore.kt */
final class SemaphoreImpl extends SegmentQueue<SemaphoreSegment> implements Semaphore {
    private static final AtomicIntegerFieldUpdater _availablePermits$FU;
    private static final AtomicLongFieldUpdater deqIdx$FU;
    static final AtomicLongFieldUpdater enqIdx$FU;
    private volatile int _availablePermits;
    private volatile long deqIdx;
    volatile long enqIdx;
    private final int permits;

    static {
        Class<SemaphoreImpl> cls = SemaphoreImpl.class;
        _availablePermits$FU = AtomicIntegerFieldUpdater.newUpdater(cls, "_availablePermits");
        enqIdx$FU = AtomicLongFieldUpdater.newUpdater(cls, "enqIdx");
        deqIdx$FU = AtomicLongFieldUpdater.newUpdater(cls, "deqIdx");
    }

    public static final /* synthetic */ SemaphoreSegment access$getSegment(SemaphoreImpl semaphoreImpl, SemaphoreSegment semaphoreSegment, long j) {
        return (SemaphoreSegment) semaphoreImpl.getSegment(semaphoreSegment, j);
    }

    public static final /* synthetic */ SemaphoreSegment access$getTail$p(SemaphoreImpl semaphoreImpl) {
        return (SemaphoreSegment) semaphoreImpl.getTail();
    }

    public SemaphoreImpl(int i, int i2) {
        this.permits = i;
        boolean z = true;
        if (i > 0) {
            int i3 = this.permits;
            if (i2 < 0 || i3 < i2) {
                z = false;
            }
            if (z) {
                this._availablePermits = this.permits - i2;
                this.enqIdx = 0;
                this.deqIdx = 0;
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("The number of acquired permits should be in 0..");
            sb.append(this.permits);
            throw new IllegalArgumentException(sb.toString().toString());
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Semaphore should have at least 1 permit, but had ");
        sb2.append(this.permits);
        throw new IllegalArgumentException(sb2.toString().toString());
    }

    public SemaphoreSegment newSegment(long j, SemaphoreSegment semaphoreSegment) {
        return new SemaphoreSegment(j, semaphoreSegment);
    }

    public int getAvailablePermits() {
        return Math.max(this._availablePermits, 0);
    }

    public Object acquire(Continuation<? super Unit> continuation) {
        if (_availablePermits$FU.getAndDecrement(this) > 0) {
            return Unit.INSTANCE;
        }
        return addToQueueAndSuspend(continuation);
    }

    public void release() {
        if (incPermits() < 0) {
            resumeNextFromQueue$kotlinx_coroutines_core();
        }
    }

    public final void resumeNextFromQueue$kotlinx_coroutines_core() {
        while (true) {
            SemaphoreSegment semaphoreSegment = (SemaphoreSegment) getHead();
            long andIncrement = deqIdx$FU.getAndIncrement(this);
            SemaphoreSegment semaphoreSegment2 = (SemaphoreSegment) getSegmentAndMoveHead(semaphoreSegment, andIncrement / ((long) SemaphoreKt.SEGMENT_SIZE));
            if (semaphoreSegment2 != null) {
                Object andSet = semaphoreSegment2.acquirers.getAndSet((int) (andIncrement % ((long) SemaphoreKt.SEGMENT_SIZE)), SemaphoreKt.RESUMED);
                if (andSet != null) {
                    if (andSet != SemaphoreKt.CANCELLED) {
                        Continuation continuation = (CancellableContinuation) andSet;
                        Unit unit = Unit.INSTANCE;
                        Companion companion = Result.Companion;
                        continuation.resumeWith(Result.m3constructorimpl(unit));
                        return;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public boolean tryAcquire() {
        int i;
        do {
            i = this._availablePermits;
            if (i <= 0) {
                return false;
            }
        } while (!_availablePermits$FU.compareAndSet(this, i, i - 1));
        return true;
    }

    public final int incPermits() {
        int i;
        do {
            i = this._availablePermits;
            if (!(i < this.permits)) {
                StringBuilder sb = new StringBuilder();
                sb.append("The number of released permits cannot be greater than ");
                sb.append(this.permits);
                throw new IllegalStateException(sb.toString().toString());
            }
        } while (!_availablePermits$FU.compareAndSet(this, i, i + 1));
        return i;
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Object addToQueueAndSuspend(Continuation<? super Unit> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        CancellableContinuation cancellableContinuation = cancellableContinuationImpl;
        SemaphoreSegment access$getTail$p = access$getTail$p(this);
        long andIncrement = enqIdx$FU.getAndIncrement(this);
        SemaphoreSegment access$getSegment = access$getSegment(this, access$getTail$p, andIncrement / ((long) SemaphoreKt.SEGMENT_SIZE));
        int access$getSEGMENT_SIZE$p = (int) (andIncrement % ((long) SemaphoreKt.SEGMENT_SIZE));
        if (access$getSegment == null || access$getSegment.acquirers.get(access$getSEGMENT_SIZE$p) == SemaphoreKt.RESUMED || !access$getSegment.acquirers.compareAndSet(access$getSEGMENT_SIZE$p, null, cancellableContinuation)) {
            Continuation continuation2 = cancellableContinuation;
            Unit unit = Unit.INSTANCE;
            Companion companion = Result.Companion;
            continuation2.resumeWith(Result.m3constructorimpl(unit));
        } else {
            cancellableContinuation.invokeOnCancellation(new CancelSemaphoreAcquisitionHandler(this, access$getSegment, access$getSEGMENT_SIZE$p));
        }
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }
}

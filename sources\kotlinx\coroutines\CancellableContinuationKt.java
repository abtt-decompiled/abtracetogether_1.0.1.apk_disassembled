package kotlinx.coroutines;

import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.ReplaceWith;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.InlineMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.LockFreeLinkedListNode;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000*\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a3\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001a\b\u0004\u0010\u0002\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0004\u0012\u00020\u00050\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0006\u001a=\u0010\u0000\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\b\b\u0002\u0010\u0007\u001a\u00020\b2\u001a\b\u0004\u0010\u0002\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0004\u0012\u00020\u00050\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\t\u001a3\u0010\n\u001a\u0002H\u0001\"\u0004\b\u0000\u0010\u00012\u001a\b\u0004\u0010\u0002\u001a\u0014\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00010\u0004\u0012\u0004\u0012\u00020\u00050\u0003HHø\u0001\u0000¢\u0006\u0002\u0010\u0006\u001a\u0018\u0010\u000b\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010\f\u001a\u00020\rH\u0007\u001a\u0018\u0010\u000e\u001a\u00020\u0005*\u0006\u0012\u0002\b\u00030\u00042\u0006\u0010\u000f\u001a\u00020\u0010H\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0011"}, d2 = {"suspendAtomicCancellableCoroutine", "T", "block", "Lkotlin/Function1;", "Lkotlinx/coroutines/CancellableContinuation;", "", "(Lkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "holdCancellability", "", "(ZLkotlin/jvm/functions/Function1;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "suspendCancellableCoroutine", "disposeOnCancellation", "handle", "Lkotlinx/coroutines/DisposableHandle;", "removeOnCancellation", "node", "Lkotlinx/coroutines/internal/LockFreeLinkedListNode;", "kotlinx-coroutines-core"}, k = 2, mv = {1, 1, 15})
/* compiled from: CancellableContinuation.kt */
public final class CancellableContinuationKt {
    private static final Object suspendCancellableCoroutine$$forInline(Function1 function1, Continuation continuation) {
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        InlineMarker.mark(1);
        return result;
    }

    public static final <T> Object suspendCancellableCoroutine(Function1<? super CancellableContinuation<? super T>, Unit> function1, Continuation<? super T> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    private static final Object suspendAtomicCancellableCoroutine$$forInline(Function1 function1, Continuation continuation) {
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        InlineMarker.mark(1);
        return result;
    }

    public static final <T> Object suspendAtomicCancellableCoroutine(Function1<? super CancellableContinuation<? super T>, Unit> function1, Continuation<? super T> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public static final void removeOnCancellation(CancellableContinuation<?> cancellableContinuation, LockFreeLinkedListNode lockFreeLinkedListNode) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "$this$removeOnCancellation");
        Intrinsics.checkParameterIsNotNull(lockFreeLinkedListNode, "node");
        cancellableContinuation.invokeOnCancellation(new RemoveOnCancel(lockFreeLinkedListNode));
    }

    public static final void disposeOnCancellation(CancellableContinuation<?> cancellableContinuation, DisposableHandle disposableHandle) {
        Intrinsics.checkParameterIsNotNull(cancellableContinuation, "$this$disposeOnCancellation");
        Intrinsics.checkParameterIsNotNull(disposableHandle, "handle");
        cancellableContinuation.invokeOnCancellation(new DisposeOnCancel(disposableHandle));
    }

    @Deprecated(message = "holdCancellability parameter is deprecated and is no longer used", replaceWith = @ReplaceWith(expression = "suspendAtomicCancellableCoroutine(block)", imports = {}))
    private static final Object suspendAtomicCancellableCoroutine$$forInline(boolean z, Function1 function1, Continuation continuation) {
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        InlineMarker.mark(1);
        return result;
    }

    @Deprecated(message = "holdCancellability parameter is deprecated and is no longer used", replaceWith = @ReplaceWith(expression = "suspendAtomicCancellableCoroutine(block)", imports = {}))
    public static final <T> Object suspendAtomicCancellableCoroutine(boolean z, Function1<? super CancellableContinuation<? super T>, Unit> function1, Continuation<? super T> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public static /* synthetic */ Object suspendAtomicCancellableCoroutine$default(boolean z, Function1 function1, Continuation continuation, int i, Object obj) {
        int i2 = i & 1;
        InlineMarker.mark(0);
        CancellableContinuationImpl cancellableContinuationImpl = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 0);
        function1.invoke(cancellableContinuationImpl);
        Object result = cancellableContinuationImpl.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        InlineMarker.mark(1);
        return result;
    }
}

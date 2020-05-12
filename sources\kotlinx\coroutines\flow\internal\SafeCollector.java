package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.flow.FlowCollector;
import kotlinx.coroutines.internal.ScopeCoroutine;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0000\u0018\u0000*\u0004\b\u0000\u0010\u00012\b\u0012\u0004\u0012\u0002H\u00010\u0002B\u001b\u0012\f\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u0005H\u0002J\u0019\u0010\r\u001a\u00020\u000b2\u0006\u0010\u000e\u001a\u00028\u0000H@ø\u0001\u0000¢\u0006\u0002\u0010\u000fJ\u001b\u0010\u0010\u001a\u0004\u0018\u00010\u0011*\u0004\u0018\u00010\u00112\b\u0010\u0012\u001a\u0004\u0018\u00010\u0011H\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00028\u00000\u0002X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0005X\u000e¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006\u0013"}, d2 = {"Lkotlinx/coroutines/flow/internal/SafeCollector;", "T", "Lkotlinx/coroutines/flow/FlowCollector;", "collector", "collectContext", "Lkotlin/coroutines/CoroutineContext;", "(Lkotlinx/coroutines/flow/FlowCollector;Lkotlin/coroutines/CoroutineContext;)V", "collectContextSize", "", "lastEmissionContext", "checkContext", "", "currentContext", "emit", "value", "(Ljava/lang/Object;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "transitiveCoroutineParent", "Lkotlinx/coroutines/Job;", "collectJob", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: SafeCollector.kt */
public final class SafeCollector<T> implements FlowCollector<T> {
    /* access modifiers changed from: private */
    public final CoroutineContext collectContext;
    private final int collectContextSize;
    private final FlowCollector<T> collector;
    private CoroutineContext lastEmissionContext;

    public SafeCollector(FlowCollector<? super T> flowCollector, CoroutineContext coroutineContext) {
        Intrinsics.checkParameterIsNotNull(flowCollector, "collector");
        Intrinsics.checkParameterIsNotNull(coroutineContext, "collectContext");
        this.collector = flowCollector;
        this.collectContext = coroutineContext;
        this.collectContextSize = ((Number) coroutineContext.fold(Integer.valueOf(0), SafeCollector$collectContextSize$1.INSTANCE)).intValue();
    }

    public Object emit(T t, Continuation<? super Unit> continuation) {
        CoroutineContext context = continuation.getContext();
        if (this.lastEmissionContext != context) {
            checkContext(context);
            this.lastEmissionContext = context;
        }
        return this.collector.emit(t, continuation);
    }

    private final void checkContext(CoroutineContext coroutineContext) {
        if (((Number) coroutineContext.fold(Integer.valueOf(0), new SafeCollector$checkContext$result$1(this))).intValue() != this.collectContextSize) {
            StringBuilder sb = new StringBuilder();
            sb.append("Flow invariant is violated:\n");
            sb.append("\t\tFlow was collected in ");
            sb.append(this.collectContext);
            sb.append(",\n");
            sb.append("\t\tbut emission happened in ");
            sb.append(coroutineContext);
            sb.append(".\n");
            sb.append("\t\tPlease refer to 'flow' documentation or use 'flowOn' instead");
            throw new IllegalStateException(sb.toString().toString());
        }
    }

    /* access modifiers changed from: private */
    public final Job transitiveCoroutineParent(Job job, Job job2) {
        while (job != null) {
            if (job == job2 || !(job instanceof ScopeCoroutine)) {
                return job;
            }
            job = ((ScopeCoroutine) job).getParent$kotlinx_coroutines_core();
        }
        return null;
    }
}

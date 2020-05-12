package kotlinx.coroutines;

import java.util.concurrent.CancellationException;
import kotlin.ExceptionsKt;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.ResultKt;
import kotlin.TypeCastException;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.internal.StackTraceRecoveryKt;
import kotlinx.coroutines.internal.ThreadContextKt;
import kotlinx.coroutines.scheduling.Task;
import kotlinx.coroutines.scheduling.TaskContext;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0003\n\u0002\b\u000e\b \u0018\u0000*\u0006\b\u0000\u0010\u0001 \u00002\u00060\u0002j\u0002`\u0003B\r\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u001f\u0010\u000b\u001a\u00020\f2\b\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0010¢\u0006\u0002\b\u0011J\u0019\u0010\u0012\u001a\u0004\u0018\u00010\u00102\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0000¢\u0006\u0002\b\u0013J\u001f\u0010\u0014\u001a\u0002H\u0001\"\u0004\b\u0001\u0010\u00012\b\u0010\r\u001a\u0004\u0018\u00010\u000eH\u0010¢\u0006\u0004\b\u0015\u0010\u0016J!\u0010\u0017\u001a\u00020\f2\b\u0010\u0018\u001a\u0004\u0018\u00010\u00102\b\u0010\u0019\u001a\u0004\u0018\u00010\u0010H\u0000¢\u0006\u0002\b\u001aJ\u0006\u0010\u001b\u001a\u00020\fJ\u000f\u0010\u001c\u001a\u0004\u0018\u00010\u000eH ¢\u0006\u0002\b\u001dR\u0018\u0010\u0007\u001a\b\u0012\u0004\u0012\u00028\u00000\bX \u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u0004\u001a\u00020\u00058\u0006@\u0006X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001e"}, d2 = {"Lkotlinx/coroutines/DispatchedTask;", "T", "Lkotlinx/coroutines/scheduling/Task;", "Lkotlinx/coroutines/SchedulerTask;", "resumeMode", "", "(I)V", "delegate", "Lkotlin/coroutines/Continuation;", "getDelegate$kotlinx_coroutines_core", "()Lkotlin/coroutines/Continuation;", "cancelResult", "", "state", "", "cause", "", "cancelResult$kotlinx_coroutines_core", "getExceptionalResult", "getExceptionalResult$kotlinx_coroutines_core", "getSuccessfulResult", "getSuccessfulResult$kotlinx_coroutines_core", "(Ljava/lang/Object;)Ljava/lang/Object;", "handleFatalException", "exception", "finallyException", "handleFatalException$kotlinx_coroutines_core", "run", "takeState", "takeState$kotlinx_coroutines_core", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: Dispatched.kt */
public abstract class DispatchedTask<T> extends Task {
    public int resumeMode;

    public void cancelResult$kotlinx_coroutines_core(Object obj, Throwable th) {
        Intrinsics.checkParameterIsNotNull(th, "cause");
    }

    public abstract Continuation<T> getDelegate$kotlinx_coroutines_core();

    public <T> T getSuccessfulResult$kotlinx_coroutines_core(Object obj) {
        return obj;
    }

    public abstract Object takeState$kotlinx_coroutines_core();

    public DispatchedTask(int i) {
        this.resumeMode = i;
    }

    public final Throwable getExceptionalResult$kotlinx_coroutines_core(Object obj) {
        if (!(obj instanceof CompletedExceptionally)) {
            obj = null;
        }
        CompletedExceptionally completedExceptionally = (CompletedExceptionally) obj;
        if (completedExceptionally != null) {
            return completedExceptionally.cause;
        }
        return null;
    }

    public final void run() {
        Object obj;
        CoroutineContext context;
        Object updateThreadContext;
        Object obj2;
        TaskContext taskContext = this.taskContext;
        Job job = null;
        Throwable th = null;
        try {
            Continuation delegate$kotlinx_coroutines_core = getDelegate$kotlinx_coroutines_core();
            if (delegate$kotlinx_coroutines_core != null) {
                DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) delegate$kotlinx_coroutines_core;
                Continuation<T> continuation = dispatchedContinuation.continuation;
                context = continuation.getContext();
                Object takeState$kotlinx_coroutines_core = takeState$kotlinx_coroutines_core();
                updateThreadContext = ThreadContextKt.updateThreadContext(context, dispatchedContinuation.countOrElement);
                Throwable exceptionalResult$kotlinx_coroutines_core = getExceptionalResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core);
                if (ResumeModeKt.isCancellableMode(this.resumeMode)) {
                    job = (Job) context.get(Job.Key);
                }
                if (exceptionalResult$kotlinx_coroutines_core == null && job != null && !job.isActive()) {
                    CancellationException cancellationException = job.getCancellationException();
                    cancelResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core, cancellationException);
                    Companion companion = Result.Companion;
                    continuation.resumeWith(Result.m3constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(cancellationException, continuation))));
                } else if (exceptionalResult$kotlinx_coroutines_core != null) {
                    Companion companion2 = Result.Companion;
                    continuation.resumeWith(Result.m3constructorimpl(ResultKt.createFailure(StackTraceRecoveryKt.recoverStackTrace(exceptionalResult$kotlinx_coroutines_core, continuation))));
                } else {
                    Object successfulResult$kotlinx_coroutines_core = getSuccessfulResult$kotlinx_coroutines_core(takeState$kotlinx_coroutines_core);
                    Companion companion3 = Result.Companion;
                    continuation.resumeWith(Result.m3constructorimpl(successfulResult$kotlinx_coroutines_core));
                }
                Unit unit = Unit.INSTANCE;
                ThreadContextKt.restoreThreadContext(context, updateThreadContext);
                try {
                    Companion companion4 = Result.Companion;
                    DispatchedTask dispatchedTask = this;
                    taskContext.afterTask();
                    obj2 = Result.m3constructorimpl(Unit.INSTANCE);
                } catch (Throwable th2) {
                    Companion companion5 = Result.Companion;
                    obj2 = Result.m3constructorimpl(ResultKt.createFailure(th2));
                }
                handleFatalException$kotlinx_coroutines_core(th, Result.m6exceptionOrNullimpl(obj2));
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type kotlinx.coroutines.DispatchedContinuation<T>");
        } catch (Throwable th3) {
            try {
                Companion companion6 = Result.Companion;
                DispatchedTask dispatchedTask2 = this;
                taskContext.afterTask();
                obj = Result.m3constructorimpl(Unit.INSTANCE);
            } catch (Throwable th4) {
                Companion companion7 = Result.Companion;
                obj = Result.m3constructorimpl(ResultKt.createFailure(th4));
            }
            handleFatalException$kotlinx_coroutines_core(th3, Result.m6exceptionOrNullimpl(obj));
        }
    }

    public final void handleFatalException$kotlinx_coroutines_core(Throwable th, Throwable th2) {
        if (th != null || th2 != null) {
            if (!(th == null || th2 == null)) {
                ExceptionsKt.addSuppressed(th, th2);
            }
            if (th == null) {
                th = th2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Fatal exception in coroutines machinery for ");
            sb.append(this);
            sb.append(". ");
            sb.append("Please read KDoc to 'handleFatalException' method and report this incident to maintainers");
            String sb2 = sb.toString();
            if (th == null) {
                Intrinsics.throwNpe();
            }
            CoroutineExceptionHandlerKt.handleCoroutineException(getDelegate$kotlinx_coroutines_core().getContext(), new CoroutinesInternalError(sb2, th));
        }
    }
}

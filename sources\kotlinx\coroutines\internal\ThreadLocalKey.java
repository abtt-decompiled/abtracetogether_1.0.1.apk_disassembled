package kotlinx.coroutines.internal;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext.Key;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\b\u0018\u00002\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00020\u0001B\u0011\u0012\n\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004¢\u0006\u0002\u0010\u0005J\r\u0010\u0006\u001a\u0006\u0012\u0002\b\u00030\u0004HÂ\u0003J\u0017\u0010\u0007\u001a\u00020\u00002\f\b\u0002\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004HÆ\u0001J\u0013\u0010\b\u001a\u00020\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bHÖ\u0003J\t\u0010\f\u001a\u00020\rHÖ\u0001J\t\u0010\u000e\u001a\u00020\u000fHÖ\u0001R\u0012\u0010\u0003\u001a\u0006\u0012\u0002\b\u00030\u0004X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lkotlinx/coroutines/internal/ThreadLocalKey;", "Lkotlin/coroutines/CoroutineContext$Key;", "Lkotlinx/coroutines/internal/ThreadLocalElement;", "threadLocal", "Ljava/lang/ThreadLocal;", "(Ljava/lang/ThreadLocal;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "kotlinx-coroutines-core"}, k = 1, mv = {1, 1, 15})
/* compiled from: ThreadContext.kt */
public final class ThreadLocalKey implements Key<ThreadLocalElement<?>> {
    private final ThreadLocal<?> threadLocal;

    private final ThreadLocal<?> component1() {
        return this.threadLocal;
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.ThreadLocal, code=java.lang.ThreadLocal<?>, for r1v0, types: [java.lang.ThreadLocal] */
    public static /* synthetic */ ThreadLocalKey copy$default(ThreadLocalKey threadLocalKey, ThreadLocal<?> threadLocal2, int i, Object obj) {
        if ((i & 1) != 0) {
            threadLocal2 = threadLocalKey.threadLocal;
        }
        return threadLocalKey.copy(threadLocal2);
    }

    public final ThreadLocalKey copy(ThreadLocal<?> threadLocal2) {
        Intrinsics.checkParameterIsNotNull(threadLocal2, "threadLocal");
        return new ThreadLocalKey(threadLocal2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0010, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r1.threadLocal, (java.lang.Object) ((kotlinx.coroutines.internal.ThreadLocalKey) r2).threadLocal) != false) goto L_0x0015;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof ThreadLocalKey) {
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        ThreadLocal<?> threadLocal2 = this.threadLocal;
        if (threadLocal2 != null) {
            return threadLocal2.hashCode();
        }
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ThreadLocalKey(threadLocal=");
        sb.append(this.threadLocal);
        sb.append(")");
        return sb.toString();
    }

    public ThreadLocalKey(ThreadLocal<?> threadLocal2) {
        Intrinsics.checkParameterIsNotNull(threadLocal2, "threadLocal");
        this.threadLocal = threadLocal2;
    }
}

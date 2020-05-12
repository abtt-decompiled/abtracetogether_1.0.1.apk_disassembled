package ca.albertahealthservices.contacttracing.api.auth;

import com.worklight.wlclient.api.WLAuthorizationManager;
import com.worklight.wlclient.api.WLClient;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.SafeContinuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001b\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\nH@ø\u0001\u0000¢\u0006\u0002\u0010\u000bR\u0012\u0010\u0003\u001a\u00020\u0004X\u0005¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006\u0002\u0004\n\u0002\b\u0019¨\u0006\f"}, d2 = {"Lca/albertahealthservices/contacttracing/api/auth/AuthRequests;", "Lkotlinx/coroutines/CoroutineScope;", "()V", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "obtainAccessToken", "Lca/albertahealthservices/contacttracing/api/auth/AuthResponse;", "scope", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: AuthRequests.kt */
public final class AuthRequests implements CoroutineScope {
    public static final AuthRequests INSTANCE = new AuthRequests();
    private final /* synthetic */ CoroutineScope $$delegate_0 = CoroutineScopeKt.MainScope();

    public CoroutineContext getCoroutineContext() {
        return this.$$delegate_0.getCoroutineContext();
    }

    static {
        WLClient.getInstance().registerChallengeHandler(new SmsCodeChallengeHandler());
    }

    private AuthRequests() {
    }

    public static /* synthetic */ Object obtainAccessToken$default(AuthRequests authRequests, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = "smsOTP";
        }
        return authRequests.obtainAccessToken(str, continuation);
    }

    public final Object obtainAccessToken(String str, Continuation<? super AuthResponse> continuation) {
        SafeContinuation safeContinuation = new SafeContinuation(IntrinsicsKt.intercepted(continuation));
        WLAuthorizationManager.getInstance().obtainAccessToken(str, new AuthRequests$obtainAccessToken$2$1(safeContinuation));
        Object orThrow = safeContinuation.getOrThrow();
        if (orThrow == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return orThrow;
    }
}

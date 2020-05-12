package ca.albertahealthservices.contacttracing.api;

import ca.albertahealthservices.contacttracing.api.auth.SmsCodeChallengeHandler;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponseListener;
import java.net.URI;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.SafeContinuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000>\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002Jm\u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\b\b\u0002\u0010\u0013\u001a\u00020\u00142\n\b\u0002\u0010\u0015\u001a\u0004\u0018\u00010\u00042\n\b\u0002\u0010\u0016\u001a\u0004\u0018\u00010\u00172(\b\u0002\u0010\u0018\u001a\"\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u0019j\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020\u0004\u0018\u0001`\u001aH@ø\u0001\u0000¢\u0006\u0002\u0010\u001bR\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u0012\u0010\u000b\u001a\u00020\fX\u0005¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u0002\u0004\n\u0002\b\u0019¨\u0006\u001c"}, d2 = {"Lca/albertahealthservices/contacttracing/api/Request;", "Lkotlinx/coroutines/CoroutineScope;", "()V", "DELETE", "", "GET", "HEAD", "OPTIONS", "POST", "PUT", "TRACE", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "runRequest", "Lca/albertahealthservices/contacttracing/api/Response;", "path", "method", "timeout", "", "scope", "data", "Lorg/json/JSONObject;", "queryParams", "Ljava/util/HashMap;", "Lkotlin/collections/HashMap;", "(Ljava/lang/String;Ljava/lang/String;ILjava/lang/String;Lorg/json/JSONObject;Ljava/util/HashMap;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: Request.kt */
public final class Request implements CoroutineScope {
    public static final String DELETE = "DELETE";
    public static final String GET = "GET";
    public static final String HEAD = "HEAD";
    public static final Request INSTANCE = new Request();
    public static final String OPTIONS = "OPTIONS";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String TRACE = "TRACE";
    private final /* synthetic */ CoroutineScope $$delegate_0 = CoroutineScopeKt.MainScope();

    public CoroutineContext getCoroutineContext() {
        return this.$$delegate_0.getCoroutineContext();
    }

    static {
        WLClient.getInstance().registerChallengeHandler(new SmsCodeChallengeHandler());
    }

    private Request() {
    }

    public static /* synthetic */ Object runRequest$default(Request request, String str, String str2, int i, String str3, JSONObject jSONObject, HashMap hashMap, Continuation continuation, int i2, Object obj) {
        return request.runRequest(str, str2, (i2 & 4) != 0 ? 3000 : i, (i2 & 8) != 0 ? null : str3, (i2 & 16) != 0 ? null : jSONObject, (i2 & 32) != 0 ? null : hashMap, continuation);
    }

    public final Object runRequest(String str, String str2, int i, String str3, JSONObject jSONObject, HashMap<String, String> hashMap, Continuation<? super Response> continuation) {
        SafeContinuation safeContinuation = new SafeContinuation(IntrinsicsKt.intercepted(continuation));
        Continuation continuation2 = safeContinuation;
        WLResourceRequest wLResourceRequest = new WLResourceRequest(new URI(str), str2, i, str3);
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("method=");
        sb.append(wLResourceRequest.getMethod());
        sb.append(" - url=");
        sb.append(wLResourceRequest.getUrl());
        companion.d("Request", sb.toString());
        if (hashMap != null) {
            wLResourceRequest.setQueryParameters(hashMap);
        }
        Request$runRequest$2$listener$1 request$runRequest$2$listener$1 = new Request$runRequest$2$listener$1(wLResourceRequest, continuation2);
        if (jSONObject == null) {
            wLResourceRequest.send(request$runRequest$2$listener$1);
        } else {
            wLResourceRequest.send(jSONObject, (WLResponseListener) request$runRequest$2$listener$1);
        }
        Object orThrow = safeContinuation.getOrThrow();
        if (orThrow == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return orThrow;
    }
}

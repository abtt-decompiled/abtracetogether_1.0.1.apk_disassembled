package ca.albertahealthservices.contacttracing.api;

import android.content.Context;
import ca.albertahealthservices.contacttracing.TracerApp;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.logging.WFLog;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResourceRequest;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.coroutines.Continuation;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0012\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005H\u0016J\u0012\u0010\u0006\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u0007H\u0016¨\u0006\b"}, d2 = {"ca/albertahealthservices/contacttracing/api/Request$runRequest$2$listener$1", "Lcom/worklight/wlclient/api/WLResponseListener;", "onFailure", "", "response", "Lcom/worklight/wlclient/api/WLFailResponse;", "onSuccess", "Lcom/worklight/wlclient/api/WLResponse;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: Request.kt */
public final class Request$runRequest$2$listener$1 implements WLResponseListener {
    final /* synthetic */ Continuation $cont;
    final /* synthetic */ WLResourceRequest $request;

    Request$runRequest$2$listener$1(WLResourceRequest wLResourceRequest, Continuation continuation) {
        this.$request = wLResourceRequest;
        this.$cont = continuation;
    }

    public void onSuccess(WLResponse wLResponse) {
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("Request.onSuccess url=");
        sb.append(this.$request.getUrl());
        sb.append(" - response=");
        JSONObject jSONObject = null;
        sb.append(wLResponse != null ? wLResponse.getResponseJSON() : null);
        companion.d("Request", sb.toString());
        Continuation continuation = this.$cont;
        Integer valueOf = wLResponse != null ? Integer.valueOf(wLResponse.getStatus()) : null;
        String responseText = wLResponse != null ? wLResponse.getResponseText() : null;
        if (wLResponse != null) {
            jSONObject = wLResponse.getResponseJSON();
        }
        Response response = new Response(valueOf, responseText, jSONObject, null, null, 24, null);
        Result.Companion companion2 = Result.Companion;
        continuation.resumeWith(Result.m3constructorimpl(response));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:17:0x009d, code lost:
        if (r8 != null) goto L_0x00a1;
     */
    public void onFailure(WLFailResponse wLFailResponse) {
        String str;
        Companion companion = CentralLog.Companion;
        StringBuilder sb = new StringBuilder();
        sb.append("Request.onFailure url=");
        sb.append(this.$request.getUrl());
        String str2 = " -  response=";
        sb.append(str2);
        sb.append(wLFailResponse);
        String str3 = "Request";
        companion.d(str3, sb.toString());
        WFLog.Companion companion2 = WFLog.Companion;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("Request.onFailure request=");
        sb2.append(this.$request);
        sb2.append(str2);
        sb2.append(wLFailResponse);
        companion2.logError(sb2.toString());
        if (!(wLFailResponse == null || wLFailResponse.getErrorMsg() == null)) {
            Companion companion3 = CentralLog.Companion;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(wLFailResponse.getErrorStatusCode());
            sb3.append(" - ");
            sb3.append(wLFailResponse.getErrorMsg());
            companion3.d(str3, sb3.toString());
        }
        Continuation continuation = this.$cont;
        JSONObject jSONObject = null;
        Integer valueOf = wLFailResponse != null ? Integer.valueOf(wLFailResponse.getStatus()) : null;
        String responseText = wLFailResponse != null ? wLFailResponse.getResponseText() : null;
        if (wLFailResponse != null) {
            jSONObject = wLFailResponse.getResponseJSON();
        }
        JSONObject jSONObject2 = jSONObject;
        ErrorCode errorCode = ErrorCode.INSTANCE;
        Context appContext = TracerApp.Companion.getAppContext();
        String str4 = "";
        if (wLFailResponse != null) {
            str = wLFailResponse.getErrorStatusCode();
        }
        str = str4;
        String stringForErrorCode = errorCode.getStringForErrorCode(appContext, str);
        if (wLFailResponse != null) {
            String errorStatusCode = wLFailResponse.getErrorStatusCode();
            if (errorStatusCode != null) {
                str4 = errorStatusCode;
            }
        }
        Response response = new Response(valueOf, responseText, jSONObject2, stringForErrorCode, str4);
        Result.Companion companion4 = Result.Companion;
        continuation.resumeWith(Result.m3constructorimpl(response));
    }
}

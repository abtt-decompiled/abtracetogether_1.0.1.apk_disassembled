package ca.albertahealthservices.contacttracing.api.auth;

import android.content.Context;
import ca.albertahealthservices.contacttracing.TracerApp;
import ca.albertahealthservices.contacttracing.api.ErrorCode;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.WFLog;
import com.worklight.wlclient.api.WLAccessTokenListener;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.auth.AccessToken;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Result.Companion;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001f\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\u0010\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\bH\u0016¨\u0006\t"}, d2 = {"ca/albertahealthservices/contacttracing/api/auth/AuthRequests$obtainAccessToken$2$1", "Lcom/worklight/wlclient/api/WLAccessTokenListener;", "onFailure", "", "response", "Lcom/worklight/wlclient/api/WLFailResponse;", "onSuccess", "accessToken", "Lcom/worklight/wlclient/auth/AccessToken;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: AuthRequests.kt */
public final class AuthRequests$obtainAccessToken$2$1 implements WLAccessTokenListener {
    final /* synthetic */ Continuation $cont;

    AuthRequests$obtainAccessToken$2$1(Continuation continuation) {
        this.$cont = continuation;
    }

    public void onSuccess(AccessToken accessToken) {
        Intrinsics.checkParameterIsNotNull(accessToken, "accessToken");
        Continuation continuation = this.$cont;
        AuthResponse authResponse = new AuthResponse(accessToken, null, null, 6, null);
        Companion companion = Result.Companion;
        continuation.resumeWith(Result.m3constructorimpl(authResponse));
    }

    public void onFailure(WLFailResponse wLFailResponse) {
        Intrinsics.checkParameterIsNotNull(wLFailResponse, "response");
        WFLog.Companion.logError("Error retrieving Access Token");
        if (wLFailResponse.getErrorMsg() != null) {
            CentralLog.Companion companion = CentralLog.Companion;
            StringBuilder sb = new StringBuilder();
            sb.append(wLFailResponse.getErrorStatusCode());
            sb.append(" - ");
            sb.append(wLFailResponse.getErrorMsg());
            companion.d("Request", sb.toString());
        }
        Continuation continuation = this.$cont;
        String errorStatusCode = wLFailResponse.getErrorStatusCode();
        ErrorCode errorCode = ErrorCode.INSTANCE;
        Context appContext = TracerApp.Companion.getAppContext();
        String errorStatusCode2 = wLFailResponse.getErrorStatusCode();
        if (errorStatusCode2 == null) {
            errorStatusCode2 = "";
        }
        AuthResponse authResponse = new AuthResponse(null, errorStatusCode, errorCode.getStringForErrorCode(appContext, errorStatusCode2));
        Companion companion2 = Result.Companion;
        continuation.resumeWith(Result.m3constructorimpl(authResponse));
    }
}

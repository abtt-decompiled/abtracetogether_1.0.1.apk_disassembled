package ca.albertahealthservices.contacttracing.api;

import android.content.Context;
import ca.albertahealthservices.contacttracing.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u001a\n\u0002\u0018\u0002\n\u0002\b\u0002\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u001a\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u001f2\n\b\u0002\u0010 \u001a\u0004\u0018\u00010\u0004R\u0014\u0010\u0003\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u0014\u0010\u0007\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\u0006R\u0014\u0010\t\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u0006R\u0014\u0010\u000b\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u0006R\u0014\u0010\r\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u0006R\u0014\u0010\u000f\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0006R\u0014\u0010\u0011\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0006R\u0014\u0010\u0013\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0006R\u0014\u0010\u0015\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0006R\u0014\u0010\u0017\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0006R\u0014\u0010\u0019\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u0006R\u0014\u0010\u001b\u001a\u00020\u0004XD¢\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u0006¨\u0006!"}, d2 = {"Lca/albertahealthservices/contacttracing/api/ErrorCode;", "", "()V", "ADAPTER_DOES_NOT_EXIST", "", "getADAPTER_DOES_NOT_EXIST", "()Ljava/lang/String;", "APPLICATION_NOT_REGISTERED", "getAPPLICATION_NOT_REGISTERED", "AUTHORIZATION_FAILURE", "getAUTHORIZATION_FAILURE", "CHALLENGE_HANDLING_CANCELED", "getCHALLENGE_HANDLING_CANCELED", "ILLEGAL_ARGUMENT_EXCEPTION", "getILLEGAL_ARGUMENT_EXCEPTION", "LOGIN_ALREADY_IN_PROCESS", "getLOGIN_ALREADY_IN_PROCESS", "LOGOUT_ALREADY_IN_PROCESS", "getLOGOUT_ALREADY_IN_PROCESS", "MINIMUM_SERVER", "getMINIMUM_SERVER", "MISSING_CHALLENGE_HANDLER", "getMISSING_CHALLENGE_HANDLER", "REQUEST_TIMEOUT", "getREQUEST_TIMEOUT", "SERVER_ERROR", "getSERVER_ERROR", "UNEXPECTED_ERROR", "getUNEXPECTED_ERROR", "getStringForErrorCode", "context", "Landroid/content/Context;", "errorCode", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: ErrorCode.kt */
public final class ErrorCode {
    private static final String ADAPTER_DOES_NOT_EXIST = ADAPTER_DOES_NOT_EXIST;
    private static final String APPLICATION_NOT_REGISTERED = APPLICATION_NOT_REGISTERED;
    private static final String AUTHORIZATION_FAILURE = AUTHORIZATION_FAILURE;
    private static final String CHALLENGE_HANDLING_CANCELED = CHALLENGE_HANDLING_CANCELED;
    private static final String ILLEGAL_ARGUMENT_EXCEPTION = ILLEGAL_ARGUMENT_EXCEPTION;
    public static final ErrorCode INSTANCE = new ErrorCode();
    private static final String LOGIN_ALREADY_IN_PROCESS = LOGIN_ALREADY_IN_PROCESS;
    private static final String LOGOUT_ALREADY_IN_PROCESS = LOGOUT_ALREADY_IN_PROCESS;
    private static final String MINIMUM_SERVER = MINIMUM_SERVER;
    private static final String MISSING_CHALLENGE_HANDLER = MISSING_CHALLENGE_HANDLER;
    private static final String REQUEST_TIMEOUT = REQUEST_TIMEOUT;
    private static final String SERVER_ERROR = SERVER_ERROR;
    private static final String UNEXPECTED_ERROR = UNEXPECTED_ERROR;

    private ErrorCode() {
    }

    public final String getAPPLICATION_NOT_REGISTERED() {
        return APPLICATION_NOT_REGISTERED;
    }

    public final String getAUTHORIZATION_FAILURE() {
        return AUTHORIZATION_FAILURE;
    }

    public final String getCHALLENGE_HANDLING_CANCELED() {
        return CHALLENGE_HANDLING_CANCELED;
    }

    public final String getILLEGAL_ARGUMENT_EXCEPTION() {
        return ILLEGAL_ARGUMENT_EXCEPTION;
    }

    public final String getLOGIN_ALREADY_IN_PROCESS() {
        return LOGIN_ALREADY_IN_PROCESS;
    }

    public final String getLOGOUT_ALREADY_IN_PROCESS() {
        return LOGOUT_ALREADY_IN_PROCESS;
    }

    public final String getMINIMUM_SERVER() {
        return MINIMUM_SERVER;
    }

    public final String getMISSING_CHALLENGE_HANDLER() {
        return MISSING_CHALLENGE_HANDLER;
    }

    public final String getREQUEST_TIMEOUT() {
        return REQUEST_TIMEOUT;
    }

    public final String getSERVER_ERROR() {
        return SERVER_ERROR;
    }

    public final String getUNEXPECTED_ERROR() {
        return UNEXPECTED_ERROR;
    }

    public final String getADAPTER_DOES_NOT_EXIST() {
        return ADAPTER_DOES_NOT_EXIST;
    }

    public static /* synthetic */ String getStringForErrorCode$default(ErrorCode errorCode, Context context, String str, int i, Object obj) {
        if ((i & 2) != 0) {
            str = "";
        }
        return errorCode.getStringForErrorCode(context, str);
    }

    public final String getStringForErrorCode(Context context, String str) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        if (Intrinsics.areEqual((Object) str, (Object) AUTHORIZATION_FAILURE)) {
            String string = context.getString(R.string.auth_error);
            Intrinsics.checkExpressionValueIsNotNull(string, "context.getString(R.string.auth_error)");
            return string;
        } else if (Intrinsics.areEqual((Object) str, (Object) REQUEST_TIMEOUT)) {
            String string2 = context.getString(R.string.timeout_error);
            Intrinsics.checkExpressionValueIsNotNull(string2, "context.getString(R.string.timeout_error)");
            return string2;
        } else {
            String str2 = "context.getString(R.string.unknown_error)";
            if (Intrinsics.areEqual((Object) str, (Object) SERVER_ERROR)) {
                String string3 = context.getString(R.string.unknown_error);
                Intrinsics.checkExpressionValueIsNotNull(string3, str2);
                return string3;
            } else if (!Intrinsics.areEqual((Object) str, (Object) UNEXPECTED_ERROR) && !Intrinsics.areEqual((Object) str, (Object) CHALLENGE_HANDLING_CANCELED) && !Intrinsics.areEqual((Object) str, (Object) LOGIN_ALREADY_IN_PROCESS) && !Intrinsics.areEqual((Object) str, (Object) LOGOUT_ALREADY_IN_PROCESS)) {
                String string4 = context.getString(R.string.unexpected_error);
                Intrinsics.checkExpressionValueIsNotNull(string4, "context.getString(R.string.unexpected_error)");
                return string4;
            } else {
                String string5 = context.getString(R.string.unknown_error);
                Intrinsics.checkExpressionValueIsNotNull(string5, str2);
                return string5;
            }
        }
    }
}

package ca.albertahealthservices.contacttracing.api.auth;

import com.worklight.wlclient.auth.AccessToken;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\f\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0003\b\b\u0018\u00002\u00020\u0001B%\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0005¢\u0006\u0002\u0010\u0007J\u000b\u0010\r\u001a\u0004\u0018\u00010\u0003HÆ\u0003J\u000b\u0010\u000e\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\t\u0010\u000f\u001a\u00020\u0005HÆ\u0003J+\u0010\u0010\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\b\u0002\u0010\u0006\u001a\u00020\u0005HÆ\u0001J\u0013\u0010\u0011\u001a\u00020\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u0014\u001a\u00020\u0015HÖ\u0001J\u0006\u0010\u0016\u001a\u00020\u0012J\t\u0010\u0017\u001a\u00020\u0005HÖ\u0001R\u0013\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\b\n\u0000\u001a\u0004\b\b\u0010\tR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\u000b¨\u0006\u0018"}, d2 = {"Lca/albertahealthservices/contacttracing/api/auth/AuthResponse;", "", "accessToken", "Lcom/worklight/wlclient/auth/AccessToken;", "errorCode", "", "error", "(Lcom/worklight/wlclient/auth/AccessToken;Ljava/lang/String;Ljava/lang/String;)V", "getAccessToken", "()Lcom/worklight/wlclient/auth/AccessToken;", "getError", "()Ljava/lang/String;", "getErrorCode", "component1", "component2", "component3", "copy", "equals", "", "other", "hashCode", "", "isSuccess", "toString", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: AuthResponse.kt */
public final class AuthResponse {
    private final AccessToken accessToken;
    private final String error;
    private final String errorCode;

    public static /* synthetic */ AuthResponse copy$default(AuthResponse authResponse, AccessToken accessToken2, String str, String str2, int i, Object obj) {
        if ((i & 1) != 0) {
            accessToken2 = authResponse.accessToken;
        }
        if ((i & 2) != 0) {
            str = authResponse.errorCode;
        }
        if ((i & 4) != 0) {
            str2 = authResponse.error;
        }
        return authResponse.copy(accessToken2, str, str2);
    }

    public final AccessToken component1() {
        return this.accessToken;
    }

    public final String component2() {
        return this.errorCode;
    }

    public final String component3() {
        return this.error;
    }

    public final AuthResponse copy(AccessToken accessToken2, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str2, "error");
        return new AuthResponse(accessToken2, str, str2);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0024, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.error, (java.lang.Object) r3.error) != false) goto L_0x0029;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof AuthResponse) {
                AuthResponse authResponse = (AuthResponse) obj;
                if (Intrinsics.areEqual((Object) this.accessToken, (Object) authResponse.accessToken)) {
                    if (Intrinsics.areEqual((Object) this.errorCode, (Object) authResponse.errorCode)) {
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        AccessToken accessToken2 = this.accessToken;
        int i = 0;
        int hashCode = (accessToken2 != null ? accessToken2.hashCode() : 0) * 31;
        String str = this.errorCode;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        String str2 = this.error;
        if (str2 != null) {
            i = str2.hashCode();
        }
        return hashCode2 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AuthResponse(accessToken=");
        sb.append(this.accessToken);
        sb.append(", errorCode=");
        sb.append(this.errorCode);
        sb.append(", error=");
        sb.append(this.error);
        sb.append(")");
        return sb.toString();
    }

    public AuthResponse(AccessToken accessToken2, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(str2, "error");
        this.accessToken = accessToken2;
        this.errorCode = str;
        this.error = str2;
    }

    public /* synthetic */ AuthResponse(AccessToken accessToken2, String str, String str2, int i, DefaultConstructorMarker defaultConstructorMarker) {
        if ((i & 2) != 0) {
            str = null;
        }
        if ((i & 4) != 0) {
            str2 = "";
        }
        this(accessToken2, str, str2);
    }

    public final AccessToken getAccessToken() {
        return this.accessToken;
    }

    public final String getError() {
        return this.error;
    }

    public final String getErrorCode() {
        return this.errorCode;
    }

    public final boolean isSuccess() {
        return this.errorCode == null;
    }
}

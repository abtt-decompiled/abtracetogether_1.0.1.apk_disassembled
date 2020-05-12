package ca.albertahealthservices.contacttracing.api;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0010\u000b\n\u0002\b\u0005\b\b\u0018\u00002\u00020\u0001B7\u0012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003\u0012\b\u0010\u0004\u001a\u0004\u0018\u00010\u0005\u0012\b\u0010\u0006\u001a\u0004\u0018\u00010\u0007\u0012\b\b\u0002\u0010\b\u001a\u00020\u0005\u0012\b\b\u0002\u0010\t\u001a\u00020\u0005¢\u0006\u0002\u0010\nJ\u0010\u0010\u0014\u001a\u0004\u0018\u00010\u0003HÆ\u0003¢\u0006\u0002\u0010\u0011J\u000b\u0010\u0015\u001a\u0004\u0018\u00010\u0005HÆ\u0003J\u000b\u0010\u0016\u001a\u0004\u0018\u00010\u0007HÆ\u0003J\t\u0010\u0017\u001a\u00020\u0005HÆ\u0003J\t\u0010\u0018\u001a\u00020\u0005HÆ\u0003JF\u0010\u0019\u001a\u00020\u00002\n\b\u0002\u0010\u0002\u001a\u0004\u0018\u00010\u00032\n\b\u0002\u0010\u0004\u001a\u0004\u0018\u00010\u00052\n\b\u0002\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\b\u0002\u0010\b\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\u0005HÆ\u0001¢\u0006\u0002\u0010\u001aJ\u0013\u0010\u001b\u001a\u00020\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u0001HÖ\u0003J\t\u0010\u001e\u001a\u00020\u0003HÖ\u0001J\u0006\u0010\u001f\u001a\u00020\u001cJ\t\u0010 \u001a\u00020\u0005HÖ\u0001R\u0013\u0010\u0006\u001a\u0004\u0018\u00010\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\b\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\t\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u000eR\u0015\u0010\u0002\u001a\u0004\u0018\u00010\u0003¢\u0006\n\n\u0002\u0010\u0012\u001a\u0004\b\u0010\u0010\u0011R\u0013\u0010\u0004\u001a\u0004\u0018\u00010\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u000e¨\u0006!"}, d2 = {"Lca/albertahealthservices/contacttracing/api/Response;", "", "status", "", "text", "", "data", "Lorg/json/JSONObject;", "error", "errorCode", "(Ljava/lang/Integer;Ljava/lang/String;Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/String;)V", "getData", "()Lorg/json/JSONObject;", "getError", "()Ljava/lang/String;", "getErrorCode", "getStatus", "()Ljava/lang/Integer;", "Ljava/lang/Integer;", "getText", "component1", "component2", "component3", "component4", "component5", "copy", "(Ljava/lang/Integer;Ljava/lang/String;Lorg/json/JSONObject;Ljava/lang/String;Ljava/lang/String;)Lca/albertahealthservices/contacttracing/api/Response;", "equals", "", "other", "hashCode", "isSuccess", "toString", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: Response.kt */
public final class Response {
    private final JSONObject data;
    private final String error;
    private final String errorCode;
    private final Integer status;
    private final String text;

    public static /* synthetic */ Response copy$default(Response response, Integer num, String str, JSONObject jSONObject, String str2, String str3, int i, Object obj) {
        if ((i & 1) != 0) {
            num = response.status;
        }
        if ((i & 2) != 0) {
            str = response.text;
        }
        String str4 = str;
        if ((i & 4) != 0) {
            jSONObject = response.data;
        }
        JSONObject jSONObject2 = jSONObject;
        if ((i & 8) != 0) {
            str2 = response.error;
        }
        String str5 = str2;
        if ((i & 16) != 0) {
            str3 = response.errorCode;
        }
        return response.copy(num, str4, jSONObject2, str5, str3);
    }

    public final Integer component1() {
        return this.status;
    }

    public final String component2() {
        return this.text;
    }

    public final JSONObject component3() {
        return this.data;
    }

    public final String component4() {
        return this.error;
    }

    public final String component5() {
        return this.errorCode;
    }

    public final Response copy(Integer num, String str, JSONObject jSONObject, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(str2, "error");
        Intrinsics.checkParameterIsNotNull(str3, "errorCode");
        Response response = new Response(num, str, jSONObject, str2, str3);
        return response;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0038, code lost:
        if (kotlin.jvm.internal.Intrinsics.areEqual((java.lang.Object) r2.errorCode, (java.lang.Object) r3.errorCode) != false) goto L_0x003d;
     */
    public boolean equals(Object obj) {
        if (this != obj) {
            if (obj instanceof Response) {
                Response response = (Response) obj;
                if (Intrinsics.areEqual((Object) this.status, (Object) response.status)) {
                    if (Intrinsics.areEqual((Object) this.text, (Object) response.text)) {
                        if (Intrinsics.areEqual((Object) this.data, (Object) response.data)) {
                            if (Intrinsics.areEqual((Object) this.error, (Object) response.error)) {
                            }
                        }
                    }
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        Integer num = this.status;
        int i = 0;
        int hashCode = (num != null ? num.hashCode() : 0) * 31;
        String str = this.text;
        int hashCode2 = (hashCode + (str != null ? str.hashCode() : 0)) * 31;
        JSONObject jSONObject = this.data;
        int hashCode3 = (hashCode2 + (jSONObject != null ? jSONObject.hashCode() : 0)) * 31;
        String str2 = this.error;
        int hashCode4 = (hashCode3 + (str2 != null ? str2.hashCode() : 0)) * 31;
        String str3 = this.errorCode;
        if (str3 != null) {
            i = str3.hashCode();
        }
        return hashCode4 + i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Response(status=");
        sb.append(this.status);
        sb.append(", text=");
        sb.append(this.text);
        sb.append(", data=");
        sb.append(this.data);
        sb.append(", error=");
        sb.append(this.error);
        sb.append(", errorCode=");
        sb.append(this.errorCode);
        sb.append(")");
        return sb.toString();
    }

    public Response(Integer num, String str, JSONObject jSONObject, String str2, String str3) {
        Intrinsics.checkParameterIsNotNull(str2, "error");
        Intrinsics.checkParameterIsNotNull(str3, "errorCode");
        this.status = num;
        this.text = str;
        this.data = jSONObject;
        this.error = str2;
        this.errorCode = str3;
    }

    public /* synthetic */ Response(Integer num, String str, JSONObject jSONObject, String str2, String str3, int i, DefaultConstructorMarker defaultConstructorMarker) {
        String str4 = "";
        this(num, str, jSONObject, (i & 8) != 0 ? str4 : str2, (i & 16) != 0 ? str4 : str3);
    }

    public final JSONObject getData() {
        return this.data;
    }

    public final String getError() {
        return this.error;
    }

    public final String getErrorCode() {
        return this.errorCode;
    }

    public final Integer getStatus() {
        return this.status;
    }

    public final String getText() {
        return this.text;
    }

    public final boolean isSuccess() {
        return Intrinsics.areEqual((Object) this.error, (Object) "") && this.status != null;
    }
}

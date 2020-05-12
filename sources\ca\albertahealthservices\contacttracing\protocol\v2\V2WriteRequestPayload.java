package ca.albertahealthservices.contacttracing.protocol.v2;

import ca.albertahealthservices.contacttracing.streetpass.CentralDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.worklight.common.WLConfig;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u000b\n\u0002\u0010\u0012\n\u0002\b\u0002\u0018\u0000 \u00152\u00020\u0001:\u0001\u0015B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\u0003¢\u0006\u0002\u0010\nJ\u0006\u0010\u0013\u001a\u00020\u0014R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\r\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\fR\u0011\u0010\t\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0010\u0010\u0011R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0011¨\u0006\u0016"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/V2WriteRequestPayload;", "", "v", "", "id", "", "o", "central", "Lca/albertahealthservices/contacttracing/streetpass/CentralDevice;", "rs", "(ILjava/lang/String;Ljava/lang/String;Lca/albertahealthservices/contacttracing/streetpass/CentralDevice;I)V", "getId", "()Ljava/lang/String;", "mc", "getMc", "getO", "getRs", "()I", "getV", "getPayload", "", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: V2WriteRequestPayload.kt */
public final class V2WriteRequestPayload {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final Gson gson;
    private final String id;
    private final String mc;
    private final String o;
    private final int rs;
    private final int v;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/V2WriteRequestPayload$Companion;", "", "()V", "gson", "Lcom/google/gson/Gson;", "getGson", "()Lcom/google/gson/Gson;", "fromPayload", "Lca/albertahealthservices/contacttracing/protocol/v2/V2WriteRequestPayload;", "dataBytes", "", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: V2WriteRequestPayload.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Gson getGson() {
            return V2WriteRequestPayload.gson;
        }

        public final V2WriteRequestPayload fromPayload(byte[] bArr) {
            Intrinsics.checkParameterIsNotNull(bArr, "dataBytes");
            Object fromJson = getGson().fromJson(new String(bArr, Charsets.UTF_8), V2WriteRequestPayload.class);
            Intrinsics.checkExpressionValueIsNotNull(fromJson, "gson.fromJson(dataString…questPayload::class.java)");
            return (V2WriteRequestPayload) fromJson;
        }
    }

    public V2WriteRequestPayload(int i, String str, String str2, CentralDevice centralDevice, int i2) {
        Intrinsics.checkParameterIsNotNull(str, WLConfig.APP_ID);
        Intrinsics.checkParameterIsNotNull(str2, "o");
        Intrinsics.checkParameterIsNotNull(centralDevice, "central");
        this.v = i;
        this.id = str;
        this.o = str2;
        this.rs = i2;
        this.mc = centralDevice.getModelC();
    }

    public final int getV() {
        return this.v;
    }

    public final String getId() {
        return this.id;
    }

    public final String getO() {
        return this.o;
    }

    public final int getRs() {
        return this.rs;
    }

    public final String getMc() {
        return this.mc;
    }

    public final byte[] getPayload() {
        String json = gson.toJson((Object) this);
        Intrinsics.checkExpressionValueIsNotNull(json, "gson.toJson(this)");
        Charset charset = Charsets.UTF_8;
        if (json != null) {
            byte[] bytes = json.getBytes(charset);
            Intrinsics.checkExpressionValueIsNotNull(bytes, "(this as java.lang.String).getBytes(charset)");
            return bytes;
        }
        throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
    }

    static {
        Gson create = new GsonBuilder().disableHtmlEscaping().create();
        Intrinsics.checkExpressionValueIsNotNull(create, "GsonBuilder()\n          …leHtmlEscaping().create()");
        gson = create;
    }
}

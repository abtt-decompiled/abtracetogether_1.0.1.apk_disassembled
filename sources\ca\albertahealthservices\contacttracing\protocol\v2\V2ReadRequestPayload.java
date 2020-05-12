package ca.albertahealthservices.contacttracing.protocol.v2;

import ca.albertahealthservices.contacttracing.streetpass.PeripheralDevice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.worklight.common.WLConfig;
import java.nio.charset.Charset;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Charsets;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u0012\n\u0002\b\u0002\u0018\u0000 \u00132\u00020\u0001:\u0001\u0013B%\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\u0006\u0010\u0011\u001a\u00020\u0012R\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000bR\u0011\u0010\u0006\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000bR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010¨\u0006\u0014"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/V2ReadRequestPayload;", "", "v", "", "id", "", "o", "peripheral", "Lca/albertahealthservices/contacttracing/streetpass/PeripheralDevice;", "(ILjava/lang/String;Ljava/lang/String;Lca/albertahealthservices/contacttracing/streetpass/PeripheralDevice;)V", "getId", "()Ljava/lang/String;", "mp", "getMp", "getO", "getV", "()I", "getPayload", "", "Companion", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: V2ReadRequestPayload.kt */
public final class V2ReadRequestPayload {
    public static final Companion Companion = new Companion(null);
    /* access modifiers changed from: private */
    public static final Gson gson;
    private final String id;
    private final String mp;
    private final String o;
    private final int v;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nR\u0011\u0010\u0003\u001a\u00020\u0004¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/V2ReadRequestPayload$Companion;", "", "()V", "gson", "Lcom/google/gson/Gson;", "getGson", "()Lcom/google/gson/Gson;", "fromPayload", "Lca/albertahealthservices/contacttracing/protocol/v2/V2ReadRequestPayload;", "dataBytes", "", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: V2ReadRequestPayload.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final Gson getGson() {
            return V2ReadRequestPayload.gson;
        }

        public final V2ReadRequestPayload fromPayload(byte[] bArr) {
            Intrinsics.checkParameterIsNotNull(bArr, "dataBytes");
            Object fromJson = getGson().fromJson(new String(bArr, Charsets.UTF_8), V2ReadRequestPayload.class);
            Intrinsics.checkExpressionValueIsNotNull(fromJson, "gson.fromJson(dataString…questPayload::class.java)");
            return (V2ReadRequestPayload) fromJson;
        }
    }

    public V2ReadRequestPayload(int i, String str, String str2, PeripheralDevice peripheralDevice) {
        Intrinsics.checkParameterIsNotNull(str, WLConfig.APP_ID);
        Intrinsics.checkParameterIsNotNull(str2, "o");
        Intrinsics.checkParameterIsNotNull(peripheralDevice, "peripheral");
        this.v = i;
        this.id = str;
        this.o = str2;
        this.mp = peripheralDevice.getModelP();
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

    public final String getMp() {
        return this.mp;
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

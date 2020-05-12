package ca.albertahealthservices.contacttracing.protocol.v2;

import ca.albertahealthservices.contacttracing.TracerApp;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.protocol.CentralInterface;
import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import ca.albertahealthservices.contacttracing.streetpass.PeripheralDevice;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J'\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\bH\u0016¢\u0006\u0002\u0010\u000bJ1\u0010\f\u001a\u0004\u0018\u00010\r2\u0006\u0010\u000e\u001a\u00020\u00062\u0006\u0010\u000f\u001a\u00020\u00042\u0006\u0010\t\u001a\u00020\b2\b\u0010\n\u001a\u0004\u0018\u00010\bH\u0016¢\u0006\u0002\u0010\u0010R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/V2Central;", "Lca/albertahealthservices/contacttracing/protocol/CentralInterface;", "()V", "TAG", "", "prepareWriteRequestData", "", "protocolVersion", "", "rssi", "txPower", "(IILjava/lang/Integer;)[B", "processReadRequestDataReceived", "Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "dataRead", "peripheralAddress", "([BLjava/lang/String;ILjava/lang/Integer;)Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTraceV2.kt */
public final class V2Central implements CentralInterface {
    private final String TAG = "V2Central";

    public byte[] prepareWriteRequestData(int i, int i2, Integer num) {
        int i3 = i;
        V2WriteRequestPayload v2WriteRequestPayload = new V2WriteRequestPayload(i3, TracerApp.Companion.thisDeviceMsg(), "CA_AB", TracerApp.Companion.asCentralDevice(), i2);
        return v2WriteRequestPayload.getPayload();
    }

    public ConnectionRecord processReadRequestDataReceived(byte[] bArr, String str, int i, Integer num) {
        Intrinsics.checkParameterIsNotNull(bArr, "dataRead");
        Intrinsics.checkParameterIsNotNull(str, "peripheralAddress");
        try {
            V2ReadRequestPayload fromPayload = V2ReadRequestPayload.Companion.fromPayload(bArr);
            ConnectionRecord connectionRecord = new ConnectionRecord(fromPayload.getV(), fromPayload.getId(), fromPayload.getO(), new PeripheralDevice(fromPayload.getMp(), str), TracerApp.Companion.asCentralDevice(), i, num);
            return connectionRecord;
        } catch (Throwable th) {
            Companion companion = CentralLog.Companion;
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to deserialize read payload ");
            sb.append(th.getMessage());
            companion.e(str2, sb.toString());
            return null;
        }
    }
}

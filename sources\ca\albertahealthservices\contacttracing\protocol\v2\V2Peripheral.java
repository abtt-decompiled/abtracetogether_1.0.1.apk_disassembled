package ca.albertahealthservices.contacttracing.protocol.v2;

import ca.albertahealthservices.contacttracing.TracerApp;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.protocol.PeripheralInterface;
import ca.albertahealthservices.contacttracing.streetpass.CentralDevice;
import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\bH\u0016J\u001a\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\u0004H\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\r"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/V2Peripheral;", "Lca/albertahealthservices/contacttracing/protocol/PeripheralInterface;", "()V", "TAG", "", "prepareReadRequestData", "", "protocolVersion", "", "processWriteRequestDataReceived", "Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "dataReceived", "centralAddress", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTraceV2.kt */
public final class V2Peripheral implements PeripheralInterface {
    private final String TAG = "V2Peripheral";

    public byte[] prepareReadRequestData(int i) {
        return new V2ReadRequestPayload(i, TracerApp.Companion.thisDeviceMsg(), "CA_AB", TracerApp.Companion.asPeripheralDevice()).getPayload();
    }

    public ConnectionRecord processWriteRequestDataReceived(byte[] bArr, String str) {
        Intrinsics.checkParameterIsNotNull(bArr, "dataReceived");
        Intrinsics.checkParameterIsNotNull(str, "centralAddress");
        try {
            V2WriteRequestPayload fromPayload = V2WriteRequestPayload.Companion.fromPayload(bArr);
            ConnectionRecord connectionRecord = new ConnectionRecord(fromPayload.getV(), fromPayload.getId(), fromPayload.getO(), TracerApp.Companion.asPeripheralDevice(), new CentralDevice(fromPayload.getMc(), str), fromPayload.getRs(), null);
            return connectionRecord;
        } catch (Throwable th) {
            Companion companion = CentralLog.Companion;
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to deserialize write payload ");
            sb.append(th.getMessage());
            companion.e(str2, sb.toString());
            return null;
        }
    }
}

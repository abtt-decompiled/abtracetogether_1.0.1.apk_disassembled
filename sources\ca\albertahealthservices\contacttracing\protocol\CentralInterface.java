package ca.albertahealthservices.contacttracing.protocol;

import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\bf\u0018\u00002\u00020\u0001J'\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005H&¢\u0006\u0002\u0010\bJ1\u0010\t\u001a\u0004\u0018\u00010\n2\u0006\u0010\u000b\u001a\u00020\u00032\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u0006\u001a\u00020\u00052\b\u0010\u0007\u001a\u0004\u0018\u00010\u0005H&¢\u0006\u0002\u0010\u000e¨\u0006\u000f"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/CentralInterface;", "", "prepareWriteRequestData", "", "protocolVersion", "", "rssi", "txPower", "(IILjava/lang/Integer;)[B", "processReadRequestDataReceived", "Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "dataRead", "peripheralAddress", "", "([BLjava/lang/String;ILjava/lang/Integer;)Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTraceProtocol.kt */
public interface CentralInterface {
    byte[] prepareWriteRequestData(int i, int i2, Integer num);

    ConnectionRecord processReadRequestDataReceived(byte[] bArr, String str, int i, Integer num);
}

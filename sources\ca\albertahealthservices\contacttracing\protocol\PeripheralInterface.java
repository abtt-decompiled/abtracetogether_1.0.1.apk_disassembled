package ca.albertahealthservices.contacttracing.protocol;

import ca.albertahealthservices.contacttracing.streetpass.ConnectionRecord;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&J\u001a\u0010\u0006\u001a\u0004\u0018\u00010\u00072\u0006\u0010\b\u001a\u00020\u00032\u0006\u0010\t\u001a\u00020\nH&¨\u0006\u000b"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/PeripheralInterface;", "", "prepareReadRequestData", "", "protocolVersion", "", "processWriteRequestDataReceived", "Lca/albertahealthservices/contacttracing/streetpass/ConnectionRecord;", "dataWritten", "centralAddress", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTraceProtocol.kt */
public interface PeripheralInterface {
    byte[] prepareReadRequestData(int i);

    ConnectionRecord processWriteRequestDataReceived(byte[] bArr, String str);
}

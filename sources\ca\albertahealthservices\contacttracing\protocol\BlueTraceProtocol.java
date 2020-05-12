package ca.albertahealthservices.contacttracing.protocol;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\b\b\u0016\u0018\u00002\u00020\u0001B\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bR\u0011\u0010\u0004\u001a\u00020\u0005¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0006\u001a\u00020\u0007¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000e¨\u0006\u000f"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/BlueTraceProtocol;", "", "versionInt", "", "central", "Lca/albertahealthservices/contacttracing/protocol/CentralInterface;", "peripheral", "Lca/albertahealthservices/contacttracing/protocol/PeripheralInterface;", "(ILca/albertahealthservices/contacttracing/protocol/CentralInterface;Lca/albertahealthservices/contacttracing/protocol/PeripheralInterface;)V", "getCentral", "()Lca/albertahealthservices/contacttracing/protocol/CentralInterface;", "getPeripheral", "()Lca/albertahealthservices/contacttracing/protocol/PeripheralInterface;", "getVersionInt", "()I", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTraceProtocol.kt */
public class BlueTraceProtocol {
    private final CentralInterface central;
    private final PeripheralInterface peripheral;
    private final int versionInt;

    public BlueTraceProtocol(int i, CentralInterface centralInterface, PeripheralInterface peripheralInterface) {
        Intrinsics.checkParameterIsNotNull(centralInterface, "central");
        Intrinsics.checkParameterIsNotNull(peripheralInterface, "peripheral");
        this.versionInt = i;
        this.central = centralInterface;
        this.peripheral = peripheralInterface;
    }

    public final int getVersionInt() {
        return this.versionInt;
    }

    public final CentralInterface getCentral() {
        return this.central;
    }

    public final PeripheralInterface getPeripheral() {
        return this.peripheral;
    }
}

package ca.albertahealthservices.contacttracing.protocol.v2;

import ca.albertahealthservices.contacttracing.protocol.BlueTraceProtocol;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002¨\u0006\u0003"}, d2 = {"Lca/albertahealthservices/contacttracing/protocol/v2/BlueTraceV2;", "Lca/albertahealthservices/contacttracing/protocol/BlueTraceProtocol;", "()V", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: BlueTraceV2.kt */
public final class BlueTraceV2 extends BlueTraceProtocol {
    public BlueTraceV2() {
        super(2, new V2Central(), new V2Peripheral());
    }
}

package kotlinx.coroutines.flow.internal;

import kotlin.Metadata;
import kotlin.coroutines.CoroutineContext.Element;
import kotlin.coroutines.CoroutineContext.Key;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import kotlinx.coroutines.Job;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u0006\u0010\u0003\u001a\u00020\u00012\u0006\u0010\u0004\u001a\u00020\u0005H\n¢\u0006\u0002\b\u0006"}, d2 = {"<anonymous>", "", "T", "count", "element", "Lkotlin/coroutines/CoroutineContext$Element;", "invoke"}, k = 3, mv = {1, 1, 15})
/* compiled from: SafeCollector.kt */
final class SafeCollector$checkContext$result$1 extends Lambda implements Function2<Integer, Element, Integer> {
    final /* synthetic */ SafeCollector this$0;

    SafeCollector$checkContext$result$1(SafeCollector safeCollector) {
        this.this$0 = safeCollector;
        super(2);
    }

    public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
        return Integer.valueOf(invoke(((Number) obj).intValue(), (Element) obj2));
    }

    public final int invoke(int i, Element element) {
        Intrinsics.checkParameterIsNotNull(element, "element");
        Key key = element.getKey();
        Element element2 = this.this$0.collectContext.get(key);
        if (key != Job.Key) {
            return element != element2 ? Integer.MIN_VALUE : i + 1;
        }
        Job job = (Job) element2;
        Job access$transitiveCoroutineParent = this.this$0.transitiveCoroutineParent((Job) element, job);
        if (access$transitiveCoroutineParent == job) {
            if (job != null) {
                i++;
            }
            return i;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Flow invariant is violated:\n\t\tEmission from another coroutine is detected.\n");
        sb.append("\t\tChild of ");
        sb.append(access$transitiveCoroutineParent);
        sb.append(", expected child of ");
        sb.append(job);
        sb.append(".\n");
        sb.append("\t\tFlowCollector is not thread-safe and concurrent emissions are prohibited.\n");
        sb.append("\t\tTo mitigate this restriction please use 'channelFlow' builder instead of 'flow'");
        throw new IllegalStateException(sb.toString().toString());
    }
}

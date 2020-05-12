package ca.albertahealthservices.contacttracing.onboarding;

import android.text.style.ClickableSpan;
import android.view.View;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0017\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016¨\u0006\u0006"}, d2 = {"ca/albertahealthservices/contacttracing/onboarding/TOUFragment$onViewCreated$emailClickableSpan$1", "Landroid/text/style/ClickableSpan;", "onClick", "", "textView", "Landroid/view/View;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: TOUFragment.kt */
public final class TOUFragment$onViewCreated$emailClickableSpan$1 extends ClickableSpan {
    final /* synthetic */ TOUFragment this$0;

    TOUFragment$onViewCreated$emailClickableSpan$1(TOUFragment tOUFragment) {
        this.this$0 = tOUFragment;
    }

    public void onClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "textView");
        CentralLog.Companion.d(this.this$0.TAG, "Starting send email intent");
        this.this$0.sendEmailIntent();
    }
}

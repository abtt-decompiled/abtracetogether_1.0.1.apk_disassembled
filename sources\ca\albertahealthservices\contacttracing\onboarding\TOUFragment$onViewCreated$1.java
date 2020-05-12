package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 16})
/* compiled from: TOUFragment.kt */
final class TOUFragment$onViewCreated$1 implements OnClickListener {
    final /* synthetic */ TOUFragment this$0;

    TOUFragment$onViewCreated$1(TOUFragment tOUFragment) {
        this.this$0 = tOUFragment;
    }

    public final void onClick(View view) {
        CentralLog.Companion.d(this.this$0.TAG, "clicked view privacy");
        Intent intent = new Intent(TOUFragment.access$getMainContext$p(this.this$0), WebViewActivity.class);
        intent.putExtra("type", 0);
        this.this$0.startActivity(intent);
    }
}

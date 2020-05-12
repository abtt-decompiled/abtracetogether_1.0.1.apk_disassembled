package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 16})
/* compiled from: PreOnboardingActivity.kt */
final class PreOnboardingActivity$onCreate$1 implements OnClickListener {
    final /* synthetic */ PreOnboardingActivity this$0;

    PreOnboardingActivity$onCreate$1(PreOnboardingActivity preOnboardingActivity) {
        this.this$0 = preOnboardingActivity;
    }

    public final void onClick(View view) {
        this.this$0.startActivity(new Intent(this.this$0, HowItWorksActivity.class));
    }
}

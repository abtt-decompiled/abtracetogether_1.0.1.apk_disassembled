package ca.albertahealthservices.contacttracing.onboarding;

import android.view.View;
import android.view.View.OnClickListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005¨\u0006\u0006"}, d2 = {"<anonymous>", "", "buttonView", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick", "ca/albertahealthservices/contacttracing/onboarding/OnboardingFragmentInterface$setupButton$2$1"}, k = 3, mv = {1, 1, 16})
/* compiled from: OnboardingFragmentInterface.kt */
final class OnboardingFragmentInterface$setupButton$$inlined$let$lambda$2 implements OnClickListener {
    final /* synthetic */ OnboardingFragmentInterface this$0;

    OnboardingFragmentInterface$setupButton$$inlined$let$lambda$2(OnboardingFragmentInterface onboardingFragmentInterface) {
        this.this$0 = onboardingFragmentInterface;
    }

    public final void onClick(View view) {
        OnboardingFragmentInterface onboardingFragmentInterface = this.this$0;
        Intrinsics.checkExpressionValueIsNotNull(view, "buttonView");
        onboardingFragmentInterface.onBackButtonClick(view);
    }
}

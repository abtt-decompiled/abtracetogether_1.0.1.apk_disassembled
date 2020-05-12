package ca.albertahealthservices.contacttracing.onboarding;

import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\b\n\u0000\n\u0002\u0010\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001H\n¢\u0006\u0002\b\u0002"}, d2 = {"<anonymous>", "", "run"}, k = 3, mv = {1, 1, 16})
/* compiled from: OnboardingActivity.kt */
final class OnboardingActivity$onActivityResult$1 implements Runnable {
    final /* synthetic */ OnboardingActivity this$0;

    OnboardingActivity$onActivityResult$1(OnboardingActivity onboardingActivity) {
        this.this$0 = onboardingActivity;
    }

    public final void run() {
        this.this$0.navigateToNextPage();
    }
}

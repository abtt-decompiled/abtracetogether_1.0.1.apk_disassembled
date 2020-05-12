package ca.albertahealthservices.contacttracing.onboarding;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ca.albertahealthservices.contacttracing.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u001a\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"ca/albertahealthservices/contacttracing/onboarding/OnboardingActivity$registerForSmsCodeChallengeBroadcasts$3", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: OnboardingActivity.kt */
public final class OnboardingActivity$registerForSmsCodeChallengeBroadcasts$3 extends BroadcastReceiver {
    final /* synthetic */ OnboardingActivity this$0;

    OnboardingActivity$registerForSmsCodeChallengeBroadcasts$3(OnboardingActivity onboardingActivity) {
        this.this$0 = onboardingActivity;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        OnboardingActivity onboardingActivity = this.this$0;
        String string = onboardingActivity.getString(R.string.verification_failed);
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.verification_failed)");
        onboardingActivity.updateOTPError(string);
    }
}

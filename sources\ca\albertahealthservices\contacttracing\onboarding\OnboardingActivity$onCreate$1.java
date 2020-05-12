package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Context;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000!\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0007\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J \u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u00052\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u0005H\u0016J\u0010\u0010\u000b\u001a\u00020\u00032\u0006\u0010\u0007\u001a\u00020\u0005H\u0016¨\u0006\f"}, d2 = {"ca/albertahealthservices/contacttracing/onboarding/OnboardingActivity$onCreate$1", "Landroidx/viewpager/widget/ViewPager$OnPageChangeListener;", "onPageScrollStateChanged", "", "state", "", "onPageScrolled", "position", "positionOffset", "", "positionOffsetPixels", "onPageSelected", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: OnboardingActivity.kt */
public final class OnboardingActivity$onCreate$1 implements OnPageChangeListener {
    final /* synthetic */ OnboardingActivity this$0;

    OnboardingActivity$onCreate$1(OnboardingActivity onboardingActivity) {
        this.this$0 = onboardingActivity;
    }

    public void onPageScrollStateChanged(int i) {
        CentralLog.Companion.d(this.this$0.TAG, "OnPageScrollStateChanged");
    }

    public void onPageScrolled(int i, float f, int i2) {
        CentralLog.Companion.d(this.this$0.TAG, "OnPageScrolled");
    }

    public void onPageSelected(int i) {
        Companion companion = CentralLog.Companion;
        String access$getTAG$p = this.this$0.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("position: ");
        sb.append(i);
        companion.d(access$getTAG$p, sb.toString());
        ScreenSlidePagerAdapter access$getPagerAdapter$p = this.this$0.pagerAdapter;
        if (access$getPagerAdapter$p == null) {
            Intrinsics.throwNpe();
        }
        access$getPagerAdapter$p.getItem(i).becomesVisible();
        String str = "baseContext";
        if (i == 0) {
            Preference preference = Preference.INSTANCE;
            Context baseContext = this.this$0.getBaseContext();
            Intrinsics.checkExpressionValueIsNotNull(baseContext, str);
            preference.putCheckpoint(baseContext, i);
        } else if (i == 1) {
            Preference preference2 = Preference.INSTANCE;
            Context baseContext2 = this.this$0.getBaseContext();
            Intrinsics.checkExpressionValueIsNotNull(baseContext2, str);
            preference2.putCheckpoint(baseContext2, i);
        } else if (i == 3) {
            Preference preference3 = Preference.INSTANCE;
            Context baseContext3 = this.this$0.getBaseContext();
            Intrinsics.checkExpressionValueIsNotNull(baseContext3, str);
            preference3.putCheckpoint(baseContext3, i);
        } else if (i == 4) {
            Preference preference4 = Preference.INSTANCE;
            Context baseContext4 = this.this$0.getBaseContext();
            Intrinsics.checkExpressionValueIsNotNull(baseContext4, str);
            preference4.putCheckpoint(baseContext4, i);
        }
    }
}

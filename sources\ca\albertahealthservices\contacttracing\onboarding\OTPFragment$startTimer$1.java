package ca.albertahealthservices.contacttracing.onboarding;

import android.os.CountDownTimer;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.text.HtmlCompat;
import ca.albertahealthservices.contacttracing.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0019\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\b\u0010\u0002\u001a\u00020\u0003H\u0016J\u0010\u0010\u0004\u001a\u00020\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\u0016¨\u0006\u0007"}, d2 = {"ca/albertahealthservices/contacttracing/onboarding/OTPFragment$startTimer$1", "Landroid/os/CountDownTimer;", "onFinish", "", "onTick", "millisUntilFinished", "", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: OTPFragment.kt */
public final class OTPFragment$startTimer$1 extends CountDownTimer {
    final /* synthetic */ OTPFragment this$0;

    OTPFragment$startTimer$1(OTPFragment oTPFragment, long j, long j2) {
        this.this$0 = oTPFragment;
        super(j, j2);
    }

    public void onTick(long j) {
        String str;
        double d = (double) j;
        int floor = (int) Math.floor((1.0d * d) / ((double) 60000));
        int floor2 = (int) Math.floor((d / 1000.0d) % ((double) 60));
        if (floor2 < 10) {
            StringBuilder sb = new StringBuilder();
            sb.append('0');
            sb.append(floor2);
            str = sb.toString();
        } else {
            str = String.valueOf(floor2);
        }
        AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.timer);
        if (appCompatTextView != null) {
            OTPFragment oTPFragment = this.this$0;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("<b>");
            sb2.append(floor);
            sb2.append(':');
            sb2.append(str);
            sb2.append("</b>");
            appCompatTextView.setText(HtmlCompat.fromHtml(oTPFragment.getString(R.string.otp_countdown, sb2.toString()), 0));
        }
    }

    public void onFinish() {
        AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.timer);
        if (appCompatTextView != null) {
            appCompatTextView.setText(this.this$0.getString(R.string.otp_countdown_expired));
        }
        ((AppCompatTextView) this.this$0._$_findCachedViewById(R.id.timer)).setTextColor(this.this$0.colorError);
        OTPFragment oTPFragment = this.this$0;
        String string = oTPFragment.getString(R.string.resend_button);
        Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.resend_button)");
        oTPFragment.setButtonText(string);
        this.this$0.enableButton();
        this.this$0.timerHasFinished = true;
    }
}

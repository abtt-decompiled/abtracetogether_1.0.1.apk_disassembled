package ca.albertahealthservices.contacttracing.fragment;

import android.view.View;
import android.view.View.OnClickListener;
import androidx.appcompat.widget.AppCompatTextView;
import ca.albertahealthservices.contacttracing.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 16})
/* compiled from: EnterPinFragment.kt */
final class EnterPinFragment$onViewCreated$1 implements OnClickListener {
    final /* synthetic */ EnterPinFragment this$0;

    EnterPinFragment$onViewCreated$1(EnterPinFragment enterPinFragment) {
        this.this$0 = enterPinFragment;
    }

    public final void onClick(View view) {
        String access$getUploadToken$p = this.this$0.uploadToken;
        String str = "enterPinFragmentErrorMessage";
        if (access$getUploadToken$p == null || !access$getUploadToken$p.equals(this.this$0.getOtp())) {
            this.this$0.clearInputs();
            AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, str);
            appCompatTextView.setText(this.this$0.getString(R.string.invalid_pin));
            AppCompatTextView appCompatTextView2 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, str);
            appCompatTextView2.setVisibility(0);
            return;
        }
        AppCompatTextView appCompatTextView3 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.enterPinFragmentErrorMessage);
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView3, str);
        appCompatTextView3.setVisibility(4);
        this.this$0.uploadData();
    }
}

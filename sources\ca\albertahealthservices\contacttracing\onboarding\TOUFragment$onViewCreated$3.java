package ca.albertahealthservices.contacttracing.onboarding;

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import ca.albertahealthservices.contacttracing.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "buttonView", "Landroid/widget/CompoundButton;", "kotlin.jvm.PlatformType", "isChecked", "", "onCheckedChanged"}, k = 3, mv = {1, 1, 16})
/* compiled from: TOUFragment.kt */
final class TOUFragment$onViewCreated$3 implements OnCheckedChangeListener {
    final /* synthetic */ TOUFragment this$0;

    TOUFragment$onViewCreated$3(TOUFragment tOUFragment) {
        this.this$0 = tOUFragment;
    }

    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        Switch switchR = (Switch) this.this$0._$_findCachedViewById(R.id.checkbox_agreement);
        Intrinsics.checkExpressionValueIsNotNull(switchR, "checkbox_agreement");
        if (switchR.isChecked()) {
            this.this$0.enableButton();
        } else {
            this.this$0.disableButton();
        }
    }
}

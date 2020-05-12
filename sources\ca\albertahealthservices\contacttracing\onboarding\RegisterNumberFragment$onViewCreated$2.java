package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import ca.albertahealthservices.contacttracing.Utils;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001c\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u0006\u0010\u0005\u001a\u00020\u00062\u000e\u0010\u0007\u001a\n \u0004*\u0004\u0018\u00010\b0\bH\n¢\u0006\u0002\b\t"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/widget/TextView;", "kotlin.jvm.PlatformType", "actionId", "", "<anonymous parameter 2>", "Landroid/view/KeyEvent;", "onEditorAction"}, k = 3, mv = {1, 1, 16})
/* compiled from: RegisterNumberFragment.kt */
final class RegisterNumberFragment$onViewCreated$2 implements OnEditorActionListener {
    final /* synthetic */ View $view;
    final /* synthetic */ RegisterNumberFragment this$0;

    RegisterNumberFragment$onViewCreated$2(RegisterNumberFragment registerNumberFragment, View view) {
        this.this$0 = registerNumberFragment;
        this.$view = view;
    }

    public final boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i != 2) {
            return false;
        }
        Utils utils = Utils.INSTANCE;
        Context context = this.$view.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "view.context");
        utils.hideKeyboardFrom(context, this.$view);
        this.this$0.disableButtonAndRequestOTP();
        return true;
    }
}

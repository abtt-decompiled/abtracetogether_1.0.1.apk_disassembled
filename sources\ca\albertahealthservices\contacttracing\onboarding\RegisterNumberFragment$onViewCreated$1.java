package ca.albertahealthservices.contacttracing.onboarding;

import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatTextView;
import ca.albertahealthservices.contacttracing.R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J(\u0010\u0006\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\t2\u0006\u0010\u000b\u001a\u00020\tH\u0016J(\u0010\f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\r\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\tH\u0016¨\u0006\u000e"}, d2 = {"ca/albertahealthservices/contacttracing/onboarding/RegisterNumberFragment$onViewCreated$1", "Landroid/telephony/PhoneNumberFormattingTextWatcher;", "afterTextChanged", "", "s", "Landroid/text/Editable;", "beforeTextChanged", "", "start", "", "count", "after", "onTextChanged", "before", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: RegisterNumberFragment.kt */
public final class RegisterNumberFragment$onViewCreated$1 extends PhoneNumberFormattingTextWatcher {
    final /* synthetic */ RegisterNumberFragment this$0;

    RegisterNumberFragment$onViewCreated$1(RegisterNumberFragment registerNumberFragment) {
        this.this$0 = registerNumberFragment;
    }

    public void afterTextChanged(Editable editable) {
        Intrinsics.checkParameterIsNotNull(editable, "s");
        this.this$0.applyMask(editable.toString());
    }

    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
        RegisterNumberFragment registerNumberFragment = this.this$0;
        int length = charSequence.length();
        EditText editText = (EditText) this.this$0._$_findCachedViewById(R.id.phone_number);
        Intrinsics.checkExpressionValueIsNotNull(editText, "phone_number");
        registerNumberFragment.selectionPointer = length - editText.getSelectionStart();
        this.this$0.backspaceFlag = i2 > i3;
    }

    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        Intrinsics.checkParameterIsNotNull(charSequence, "s");
        AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.phone_number_error);
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, "phone_number_error");
        appCompatTextView.setVisibility(8);
        if (this.this$0.validateNumber(charSequence.toString())) {
            this.this$0.enableButton();
        } else {
            this.this$0.disableButton();
        }
    }
}

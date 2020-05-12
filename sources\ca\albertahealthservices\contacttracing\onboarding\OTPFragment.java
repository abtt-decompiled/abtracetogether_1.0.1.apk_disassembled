package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.FragmentActivity;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000l\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0010\u0018\u00002\u00020\u0001:\u000289B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u0006H\u0002J\b\u0010\u001a\u001a\u00020\u0017H\u0002J\b\u0010\u001b\u001a\u00020\u0006H\u0002J\u0010\u0010\u001c\u001a\u00020\u00172\u0006\u0010\u001d\u001a\u00020\u001eH\u0016J\u0010\u0010\u001f\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!H\u0016J\u0010\u0010\"\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!H\u0016J\u0012\u0010#\u001a\u00020\u00172\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J&\u0010&\u001a\u0004\u0018\u00010!2\u0006\u0010'\u001a\u00020(2\b\u0010)\u001a\u0004\u0018\u00010*2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u0010+\u001a\u00020\u0017H\u0016J\b\u0010,\u001a\u00020\u0017H\u0016J\u0010\u0010-\u001a\u00020\u00172\u0006\u0010.\u001a\u00020\u0006H\u0016J\u0010\u0010/\u001a\u00020\u00172\u0006\u00100\u001a\u00020\u0006H\u0016J\u001a\u00101\u001a\u00020\u00172\u0006\u0010 \u001a\u00020!2\b\u0010$\u001a\u0004\u0018\u00010%H\u0016J\b\u00102\u001a\u00020\u0017H\u0002J\b\u00103\u001a\u00020\u0017H\u0002J\u0010\u00104\u001a\u00020\u00172\u0006\u00105\u001a\u00020\u0015H\u0016J\b\u00106\u001a\u00020\u0017H\u0002J\u0010\u00107\u001a\u00020\u00152\u0006\u00100\u001a\u00020\u0006H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\u000e0\rX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u000f\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0010\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u0010\u0010\u0012\u001a\u0004\u0018\u00010\u0013X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u000e¢\u0006\u0002\n\u0000¨\u0006:"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/OTPFragment;", "Lca/albertahealthservices/contacttracing/onboarding/OnboardingFragmentInterface;", "()V", "COUNTDOWN_DURATION", "", "TAG", "", "colorError", "", "colorText", "listener", "Lca/albertahealthservices/contacttracing/onboarding/OTPFragment$OnFragmentInteractionListener;", "otpInputs", "", "Landroid/widget/EditText;", "param1", "param2", "phoneNumber", "stopWatch", "Landroid/os/CountDownTimer;", "timerHasFinished", "", "becomesVisible", "", "buildPhoneString", "phone", "clearInputs", "getOtp", "onAttach", "context", "Landroid/content/Context;", "onBackButtonClick", "view", "Landroid/view/View;", "onButtonClick", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroy", "onDetach", "onError", "error", "onUpdatePhoneNumber", "num", "onViewCreated", "resendCodeAndStartTimer", "resetTimer", "setUserVisibleHint", "isVisibleToUser", "startTimer", "validateNumber", "OTPTextWatcher", "OnFragmentInteractionListener", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: OTPFragment.kt */
public final class OTPFragment extends OnboardingFragmentInterface {
    private final long COUNTDOWN_DURATION = 180;
    /* access modifiers changed from: private */
    public final String TAG = "OTPFragment";
    private HashMap _$_findViewCache;
    /* access modifiers changed from: private */
    public int colorError;
    private int colorText;
    private OnFragmentInteractionListener listener;
    private List<EditText> otpInputs = new ArrayList();
    private String param1;
    private String param2;
    private String phoneNumber;
    private CountDownTimer stopWatch;
    /* access modifiers changed from: private */
    public boolean timerHasFinished;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0004\u0018\u00002\u00020\u0001B\u001b\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\f\u0010\u0004\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005¢\u0006\u0002\u0010\u0007J\u0010\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eH\u0016J(\u0010\u000f\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0013H\u0016J(\u0010\u0016\u001a\u00020\f2\u0006\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00132\u0006\u0010\u0015\u001a\u00020\u0013H\u0016R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00060\u0005X\u000e¢\u0006\u0002\n\u0000R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0017"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/OTPFragment$OTPTextWatcher;", "Landroid/text/TextWatcher;", "view", "Landroid/view/View;", "otpInputs", "", "Landroid/widget/EditText;", "(Lca/albertahealthservices/contacttracing/onboarding/OTPFragment;Landroid/view/View;Ljava/util/List;)V", "inputs", "getView", "()Landroid/view/View;", "afterTextChanged", "", "editable", "Landroid/text/Editable;", "beforeTextChanged", "arg0", "", "arg1", "", "arg2", "arg3", "onTextChanged", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: OTPFragment.kt */
    public final class OTPTextWatcher implements TextWatcher {
        private List<EditText> inputs;
        final /* synthetic */ OTPFragment this$0;
        private final View view;

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "arg0");
        }

        public OTPTextWatcher(OTPFragment oTPFragment, View view2, List<EditText> list) {
            Intrinsics.checkParameterIsNotNull(view2, "view");
            Intrinsics.checkParameterIsNotNull(list, "otpInputs");
            this.this$0 = oTPFragment;
            this.view = view2;
            this.inputs = list;
        }

        public final View getView() {
            return this.view;
        }

        public void afterTextChanged(Editable editable) {
            Object obj;
            Object obj2;
            Object obj3;
            Object obj4;
            Intrinsics.checkParameterIsNotNull(editable, "editable");
            String obj5 = editable.toString();
            boolean z = false;
            boolean z2 = true;
            switch (this.view.getId()) {
                case R.id.otp_et1 /*2131296506*/:
                    if (obj5.length() == 1) {
                        ((EditText) this.inputs.get(1)).requestFocus();
                        return;
                    }
                    return;
                case R.id.otp_et2 /*2131296507*/:
                    if (obj5.length() == 1) {
                        obj = this.inputs.get(2);
                    } else {
                        if (obj5.length() != 0) {
                            z2 = false;
                        }
                        if (z2) {
                            obj = this.inputs.get(0);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj).requestFocus();
                    return;
                case R.id.otp_et3 /*2131296508*/:
                    if (obj5.length() == 1) {
                        obj2 = this.inputs.get(3);
                    } else {
                        if (obj5.length() == 0) {
                            z = true;
                        }
                        if (z) {
                            obj2 = this.inputs.get(1);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj2).requestFocus();
                    return;
                case R.id.otp_et4 /*2131296509*/:
                    if (obj5.length() == 1) {
                        obj3 = this.inputs.get(4);
                    } else {
                        if (obj5.length() == 0) {
                            z = true;
                        }
                        if (z) {
                            obj3 = this.inputs.get(2);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj3).requestFocus();
                    return;
                case R.id.otp_et5 /*2131296510*/:
                    if (obj5.length() == 1) {
                        obj4 = this.inputs.get(5);
                    } else {
                        if (obj5.length() == 0) {
                            z = true;
                        }
                        if (z) {
                            obj4 = this.inputs.get(3);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj4).requestFocus();
                    return;
                case R.id.otp_et6 /*2131296511*/:
                    if (obj5.length() == 0) {
                        z = true;
                    }
                    if (z) {
                        ((EditText) this.inputs.get(4)).requestFocus();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "arg0");
            OTPFragment oTPFragment = this.this$0;
            if (oTPFragment.validateNumber(oTPFragment.getOtp())) {
                Utils utils = Utils.INSTANCE;
                Context context = this.view.getContext();
                Intrinsics.checkExpressionValueIsNotNull(context, "view.context");
                utils.hideKeyboardFrom(context, this.view);
                this.this$0.enableButton();
                OTPFragment oTPFragment2 = this.this$0;
                String string = oTPFragment2.getString(R.string.submit_button);
                Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.submit_button)");
                oTPFragment2.setButtonText(string);
                return;
            }
            this.this$0.disableButton();
            OTPFragment oTPFragment3 = this.this$0;
            String string2 = oTPFragment3.getString(R.string.next_button);
            Intrinsics.checkExpressionValueIsNotNull(string2, "getString(R.string.next_button)");
            oTPFragment3.setButtonText(string2);
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/OTPFragment$OnFragmentInteractionListener;", "", "onFragmentInteraction", "", "uri", "Landroid/net/Uri;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: OTPFragment.kt */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void _$_clearFindViewByIdCache() {
        HashMap hashMap = this._$_findViewCache;
        if (hashMap != null) {
            hashMap.clear();
        }
    }

    public View _$_findCachedViewById(int i) {
        if (this._$_findViewCache == null) {
            this._$_findViewCache = new HashMap();
        }
        View view = (View) this._$_findViewCache.get(Integer.valueOf(i));
        if (view == null) {
            View view2 = getView();
            if (view2 == null) {
                return null;
            }
            view = view2.findViewById(i);
            this._$_findViewCache.put(Integer.valueOf(i), view);
        }
        return view;
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.param1 = arguments.getString("param1");
            this.param2 = arguments.getString("param2");
        }
    }

    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (z) {
            startTimer();
        } else {
            resetTimer();
        }
    }

    private final void resetTimer() {
        CountDownTimer countDownTimer = this.stopWatch;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void becomesVisible() {
        CentralLog.Companion.d(this.TAG, "becomes visible");
        clearInputs();
    }

    public void onButtonClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        CentralLog.Companion.d(this.TAG, "OnButtonClick 3B");
        String otp = getOtp();
        Context context = getContext();
        if (context != null) {
            OnboardingActivity onboardingActivity = (OnboardingActivity) context;
            if (this.timerHasFinished) {
                resendCodeAndStartTimer();
            } else {
                onboardingActivity.validateOTP(otp);
            }
        } else {
            throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity");
        }
    }

    public void onBackButtonClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        Context context = getContext();
        if (context != null) {
            ((OnboardingActivity) context).onBackPressed();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity");
    }

    /* access modifiers changed from: private */
    public final String getOtp() {
        String str = "";
        for (EditText editText : this.otpInputs) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(editText.getText());
            str = sb.toString();
        }
        return str;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(R.layout.fragment_otp, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.sent_to);
        if (appCompatTextView != null) {
            Object[] objArr = new Object[1];
            Preference preference = Preference.INSTANCE;
            Context context = getContext();
            if (context == null) {
                Intrinsics.throwNpe();
            }
            Intrinsics.checkExpressionValueIsNotNull(context, "context!!");
            objArr[0] = String.valueOf(buildPhoneString(preference.getPhoneNumber(context)));
            appCompatTextView.setText(HtmlCompat.fromHtml(getString(R.string.otp_sent, objArr), 0));
        }
        Context context2 = getContext();
        if (context2 == null) {
            Intrinsics.throwNpe();
        }
        this.colorError = ContextCompat.getColor(context2, R.color.error);
        Context context3 = getContext();
        if (context3 == null) {
            Intrinsics.throwNpe();
        }
        this.colorText = ContextCompat.getColor(context3, R.color.grey_3);
        List<EditText> list = this.otpInputs;
        EditText editText = (EditText) _$_findCachedViewById(R.id.otp_et1);
        String str = "otp_et1";
        Intrinsics.checkExpressionValueIsNotNull(editText, str);
        list.add(editText);
        List<EditText> list2 = this.otpInputs;
        EditText editText2 = (EditText) _$_findCachedViewById(R.id.otp_et2);
        String str2 = "otp_et2";
        Intrinsics.checkExpressionValueIsNotNull(editText2, str2);
        list2.add(editText2);
        List<EditText> list3 = this.otpInputs;
        EditText editText3 = (EditText) _$_findCachedViewById(R.id.otp_et3);
        String str3 = "otp_et3";
        Intrinsics.checkExpressionValueIsNotNull(editText3, str3);
        list3.add(editText3);
        List<EditText> list4 = this.otpInputs;
        EditText editText4 = (EditText) _$_findCachedViewById(R.id.otp_et4);
        String str4 = "otp_et4";
        Intrinsics.checkExpressionValueIsNotNull(editText4, str4);
        list4.add(editText4);
        List<EditText> list5 = this.otpInputs;
        EditText editText5 = (EditText) _$_findCachedViewById(R.id.otp_et5);
        String str5 = "otp_et5";
        Intrinsics.checkExpressionValueIsNotNull(editText5, str5);
        list5.add(editText5);
        List<EditText> list6 = this.otpInputs;
        EditText editText6 = (EditText) _$_findCachedViewById(R.id.otp_et6);
        String str6 = "otp_et6";
        Intrinsics.checkExpressionValueIsNotNull(editText6, str6);
        list6.add(editText6);
        EditText editText7 = (EditText) _$_findCachedViewById(R.id.otp_et1);
        if (editText7 != null) {
            EditText editText8 = (EditText) _$_findCachedViewById(R.id.otp_et1);
            Intrinsics.checkExpressionValueIsNotNull(editText8, str);
            editText7.addTextChangedListener(new OTPTextWatcher(this, editText8, this.otpInputs));
        }
        EditText editText9 = (EditText) _$_findCachedViewById(R.id.otp_et2);
        if (editText9 != null) {
            EditText editText10 = (EditText) _$_findCachedViewById(R.id.otp_et2);
            Intrinsics.checkExpressionValueIsNotNull(editText10, str2);
            editText9.addTextChangedListener(new OTPTextWatcher(this, editText10, this.otpInputs));
        }
        EditText editText11 = (EditText) _$_findCachedViewById(R.id.otp_et3);
        if (editText11 != null) {
            EditText editText12 = (EditText) _$_findCachedViewById(R.id.otp_et3);
            Intrinsics.checkExpressionValueIsNotNull(editText12, str3);
            editText11.addTextChangedListener(new OTPTextWatcher(this, editText12, this.otpInputs));
        }
        EditText editText13 = (EditText) _$_findCachedViewById(R.id.otp_et4);
        if (editText13 != null) {
            EditText editText14 = (EditText) _$_findCachedViewById(R.id.otp_et4);
            Intrinsics.checkExpressionValueIsNotNull(editText14, str4);
            editText13.addTextChangedListener(new OTPTextWatcher(this, editText14, this.otpInputs));
        }
        EditText editText15 = (EditText) _$_findCachedViewById(R.id.otp_et5);
        if (editText15 != null) {
            EditText editText16 = (EditText) _$_findCachedViewById(R.id.otp_et5);
            Intrinsics.checkExpressionValueIsNotNull(editText16, str5);
            editText15.addTextChangedListener(new OTPTextWatcher(this, editText16, this.otpInputs));
        }
        EditText editText17 = (EditText) _$_findCachedViewById(R.id.otp_et6);
        if (editText17 != null) {
            EditText editText18 = (EditText) _$_findCachedViewById(R.id.otp_et6);
            Intrinsics.checkExpressionValueIsNotNull(editText18, str6);
            editText17.addTextChangedListener(new OTPTextWatcher(this, editText18, this.otpInputs));
        }
        AppCompatTextView appCompatTextView2 = (AppCompatTextView) _$_findCachedViewById(R.id.resendCode);
        if (appCompatTextView2 != null) {
            appCompatTextView2.setOnClickListener(new OTPFragment$onViewCreated$1(this));
        }
        EditText editText19 = (EditText) _$_findCachedViewById(R.id.otp_et6);
        if (editText19 != null) {
            editText19.setOnEditorActionListener(new OTPFragment$onViewCreated$2(this, view));
        }
    }

    public void onUpdatePhoneNumber(String str) {
        Intrinsics.checkParameterIsNotNull(str, "num");
        Companion companion = CentralLog.Companion;
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onUpdatePhoneNumber ");
        sb.append(str);
        companion.d(str2, sb.toString());
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.sent_to);
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, "sent_to");
        appCompatTextView.setText(HtmlCompat.fromHtml(getString(R.string.otp_sent, String.valueOf(buildPhoneString(str))), 0));
        this.phoneNumber = str;
    }

    private final void startTimer() {
        this.timerHasFinished = false;
        OTPFragment$startTimer$1 oTPFragment$startTimer$1 = new OTPFragment$startTimer$1(this, ((long) 1000) * this.COUNTDOWN_DURATION, 1000);
        CountDownTimer countDownTimer = oTPFragment$startTimer$1;
        this.stopWatch = countDownTimer;
        if (countDownTimer != null) {
            countDownTimer.start();
        }
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.timer);
        if (appCompatTextView != null) {
            appCompatTextView.setTextColor(this.colorText);
        }
    }

    public void onError(String str) {
        Intrinsics.checkParameterIsNotNull(str, "error");
        Context context = getContext();
        if (context != null) {
            ((OnboardingActivity) context).onBackPressed();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity");
    }

    public void onAttach(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) context;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(context.toString());
        sb.append(" must implement OnFragmentInteractionListener");
        throw new RuntimeException(sb.toString());
    }

    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    public void onDestroy() {
        super.onDestroy();
        CountDownTimer countDownTimer = this.stopWatch;
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private final String buildPhoneString(String str) {
        if (str.length() < 6) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("+1 ");
        String str2 = "null cannot be cast to non-null type java.lang.String";
        if (str != null) {
            String substring = str.substring(0, 3);
            String str3 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
            Intrinsics.checkExpressionValueIsNotNull(substring, str3);
            sb.append(substring);
            String str4 = "-";
            sb.append(str4);
            if (str != null) {
                String substring2 = str.substring(3, 6);
                Intrinsics.checkExpressionValueIsNotNull(substring2, str3);
                sb.append(substring2);
                sb.append(str4);
                if (str != null) {
                    String substring3 = str.substring(6);
                    Intrinsics.checkExpressionValueIsNotNull(substring3, "(this as java.lang.String).substring(startIndex)");
                    sb.append(substring3);
                    return sb.toString();
                }
                throw new TypeCastException(str2);
            }
            throw new TypeCastException(str2);
        }
        throw new TypeCastException(str2);
    }

    /* access modifiers changed from: private */
    public final boolean validateNumber(String str) {
        return str.length() >= 6;
    }

    private final void clearInputs() {
        EditText editText = (EditText) _$_findCachedViewById(R.id.otp_et6);
        String str = "";
        if (editText != null) {
            editText.setText(str);
        }
        EditText editText2 = (EditText) _$_findCachedViewById(R.id.otp_et5);
        if (editText2 != null) {
            editText2.setText(str);
        }
        EditText editText3 = (EditText) _$_findCachedViewById(R.id.otp_et4);
        if (editText3 != null) {
            editText3.setText(str);
        }
        EditText editText4 = (EditText) _$_findCachedViewById(R.id.otp_et3);
        if (editText4 != null) {
            editText4.setText(str);
        }
        EditText editText5 = (EditText) _$_findCachedViewById(R.id.otp_et2);
        if (editText5 != null) {
            editText5.setText(str);
        }
        EditText editText6 = (EditText) _$_findCachedViewById(R.id.otp_et1);
        if (editText6 != null) {
            editText6.setText(str);
        }
    }

    /* access modifiers changed from: private */
    public final void resendCodeAndStartTimer() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            OnboardingActivity onboardingActivity = (OnboardingActivity) activity;
            clearInputs();
            OnboardingActivity.resendCode$default(onboardingActivity, false, 1, null);
            resetTimer();
            startTimer();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity");
    }
}

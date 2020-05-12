package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.appcompat.widget.AppCompatTextView;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.TracerApp;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000P\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0001)B\u0005¢\u0006\u0002\u0010\u0002J\u0010\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004H\u0002J\b\u0010\u0011\u001a\u00020\u000fH\u0016J\b\u0010\u0012\u001a\u00020\u000fH\u0002J\u0010\u0010\u0013\u001a\u00020\u00042\u0006\u0010\u0010\u001a\u00020\u0004H\u0002J\u0010\u0010\u0014\u001a\u00020\u000f2\u0006\u0010\u0015\u001a\u00020\u0016H\u0016J\u0010\u0010\u0017\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u000bH\u0016J\u0010\u0010\u0019\u001a\u00020\u000f2\u0006\u0010\u001a\u001a\u00020\u000bH\u0016J$\u0010\u001b\u001a\u00020\u000b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u000fH\u0016J\u0010\u0010#\u001a\u00020\u000f2\u0006\u0010$\u001a\u00020\u0004H\u0016J\u0010\u0010%\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0004H\u0016J\u001a\u0010&\u001a\u00020\u000f2\u0006\u0010\u0018\u001a\u00020\u000b2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010'\u001a\u00020\u000fH\u0002J\u0010\u0010(\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u000e¢\u0006\u0002\n\u0000¨\u0006*"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/RegisterNumberFragment;", "Lca/albertahealthservices/contacttracing/onboarding/OnboardingFragmentInterface;", "()V", "TAG", "", "backspaceFlag", "", "editFlag", "listener", "Lca/albertahealthservices/contacttracing/onboarding/RegisterNumberFragment$OnFragmentInteractionListener;", "mView", "Landroid/view/View;", "selectionPointer", "", "applyMask", "", "num", "becomesVisible", "disableButtonAndRequestOTP", "getUnmaskedNumber", "onAttach", "context", "Landroid/content/Context;", "onBackButtonClick", "view", "onButtonClick", "buttonView", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDetach", "onError", "error", "onUpdatePhoneNumber", "onViewCreated", "requestOTP", "validateNumber", "OnFragmentInteractionListener", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: RegisterNumberFragment.kt */
public final class RegisterNumberFragment extends OnboardingFragmentInterface {
    private final String TAG = "RegisterNumberFragment";
    private HashMap _$_findViewCache;
    /* access modifiers changed from: private */
    public boolean backspaceFlag;
    private boolean editFlag;
    private OnFragmentInteractionListener listener;
    private View mView;
    /* access modifiers changed from: private */
    public int selectionPointer;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/RegisterNumberFragment$OnFragmentInteractionListener;", "", "onFragmentInteraction", "", "uri", "Landroid/net/Uri;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: RegisterNumberFragment.kt */
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

    public void becomesVisible() {
        CentralLog.Companion.d(this.TAG, "becomes visible");
        OnboardingFragmentInterface onboardingFragmentInterface = this;
        EditText editText = (EditText) _$_findCachedViewById(R.id.phone_number);
        if (validateNumber(String.valueOf(editText != null ? editText.getText() : null))) {
            onboardingFragmentInterface.enableButton();
        } else {
            onboardingFragmentInterface.disableButton();
        }
    }

    public void onButtonClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "buttonView");
        CentralLog.Companion.d(this.TAG, "OnButtonClick");
        disableButtonAndRequestOTP();
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
    public final void disableButtonAndRequestOTP() {
        disableButton();
        requestOTP();
    }

    private final void requestOTP() {
        if (this.mView != null) {
            AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.phone_number_error);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, "phone_number_error");
            appCompatTextView.setVisibility(4);
            EditText editText = (EditText) _$_findCachedViewById(R.id.phone_number);
            Intrinsics.checkExpressionValueIsNotNull(editText, "phone_number");
            String unmaskedNumber = getUnmaskedNumber(editText.getText().toString());
            Companion companion = CentralLog.Companion;
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("The value retrieved: ");
            sb.append(unmaskedNumber);
            companion.d(str, sb.toString());
            Context context = getContext();
            if (context != null) {
                OnboardingActivity onboardingActivity = (OnboardingActivity) context;
                Preference.INSTANCE.putPhoneNumber(TracerApp.Companion.getAppContext(), unmaskedNumber);
                onboardingActivity.updatePhoneNumber(unmaskedNumber);
                OnboardingActivity.requestForOTP$default(onboardingActivity, unmaskedNumber, false, 2, null);
                return;
            }
            throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity");
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        CentralLog.Companion.i(this.TAG, "View created");
        this.mView = view;
        EditText editText = (EditText) _$_findCachedViewById(R.id.phone_number);
        if (editText != null) {
            editText.addTextChangedListener(new RegisterNumberFragment$onViewCreated$1(this));
        }
        EditText editText2 = (EditText) _$_findCachedViewById(R.id.phone_number);
        if (editText2 != null) {
            editText2.setOnEditorActionListener(new RegisterNumberFragment$onViewCreated$2(this, view));
        }
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.tv_app_version);
        if (appCompatTextView != null) {
            appCompatTextView.setText("App Version: 1.0.1");
        }
        disableButton();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        super.onCreateView(layoutInflater, viewGroup, bundle);
        CentralLog.Companion.i(this.TAG, "Making view");
        View inflate = layoutInflater.inflate(R.layout.fragment_register_number, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "inflater.inflate(R.layou…number, container, false)");
        return inflate;
    }

    public void onUpdatePhoneNumber(String str) {
        Intrinsics.checkParameterIsNotNull(str, "num");
        Companion companion = CentralLog.Companion;
        String str2 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onUpdatePhoneNumber ");
        sb.append(str);
        companion.d(str2, sb.toString());
    }

    public void onError(String str) {
        Intrinsics.checkParameterIsNotNull(str, "error");
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.phone_number_error);
        AppCompatTextView appCompatTextView2 = (AppCompatTextView) _$_findCachedViewById(R.id.phone_number_error);
        String str2 = "phone_number_error";
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, str2);
        appCompatTextView2.setVisibility(0);
        AppCompatTextView appCompatTextView3 = (AppCompatTextView) _$_findCachedViewById(R.id.phone_number_error);
        Intrinsics.checkExpressionValueIsNotNull(appCompatTextView3, str2);
        appCompatTextView3.setText(str);
        Companion companion = CentralLog.Companion;
        String str3 = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("error: ");
        sb.append(str.toString());
        companion.e(str3, sb.toString());
    }

    public void onAttach(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) context;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(context);
        sb.append(" must implement OnFragmentInteractionListener");
        throw new RuntimeException(sb.toString());
    }

    public void onDetach() {
        super.onDetach();
        this.listener = null;
        this.mView = null;
        CentralLog.Companion.i(this.TAG, "Detached??");
    }

    /* access modifiers changed from: private */
    public final boolean validateNumber(String str) {
        return getUnmaskedNumber(str).length() == 10;
    }

    private final String getUnmaskedNumber(String str) {
        return new Regex("[^\\d]").replace((CharSequence) str, "");
    }

    /* access modifiers changed from: private */
    public final void applyMask(String str) {
        String unmaskedNumber = getUnmaskedNumber(str);
        int length = unmaskedNumber.length();
        if (!this.editFlag) {
            String str2 = "phone_number";
            String str3 = "(this as java.lang.String).substring(startIndex)";
            String str4 = ") ";
            String str5 = "(";
            String str6 = "(this as java.lang.Strin…ing(startIndex, endIndex)";
            String str7 = "null cannot be cast to non-null type java.lang.String";
            if (length >= 6 && !this.backspaceFlag) {
                this.editFlag = true;
                StringBuilder sb = new StringBuilder();
                sb.append(str5);
                if (unmaskedNumber != null) {
                    String substring = unmaskedNumber.substring(0, 3);
                    Intrinsics.checkExpressionValueIsNotNull(substring, str6);
                    sb.append(substring);
                    sb.append(str4);
                    if (unmaskedNumber != null) {
                        String substring2 = unmaskedNumber.substring(3, 6);
                        Intrinsics.checkExpressionValueIsNotNull(substring2, str6);
                        sb.append(substring2);
                        sb.append("-");
                        if (unmaskedNumber != null) {
                            String substring3 = unmaskedNumber.substring(6);
                            Intrinsics.checkExpressionValueIsNotNull(substring3, str3);
                            sb.append(substring3);
                            ((EditText) _$_findCachedViewById(R.id.phone_number)).setText(sb.toString());
                            EditText editText = (EditText) _$_findCachedViewById(R.id.phone_number);
                            EditText editText2 = (EditText) _$_findCachedViewById(R.id.phone_number);
                            Intrinsics.checkExpressionValueIsNotNull(editText2, str2);
                            editText.setSelection(editText2.getText().length() - this.selectionPointer);
                            return;
                        }
                        throw new TypeCastException(str7);
                    }
                    throw new TypeCastException(str7);
                }
                throw new TypeCastException(str7);
            } else if (length >= 3 && !this.backspaceFlag) {
                this.editFlag = true;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str5);
                if (unmaskedNumber != null) {
                    String substring4 = unmaskedNumber.substring(0, 3);
                    Intrinsics.checkExpressionValueIsNotNull(substring4, str6);
                    sb2.append(substring4);
                    sb2.append(str4);
                    if (unmaskedNumber != null) {
                        String substring5 = unmaskedNumber.substring(3);
                        Intrinsics.checkExpressionValueIsNotNull(substring5, str3);
                        sb2.append(substring5);
                        ((EditText) _$_findCachedViewById(R.id.phone_number)).setText(sb2.toString());
                        EditText editText3 = (EditText) _$_findCachedViewById(R.id.phone_number);
                        EditText editText4 = (EditText) _$_findCachedViewById(R.id.phone_number);
                        Intrinsics.checkExpressionValueIsNotNull(editText4, str2);
                        editText3.setSelection(editText4.getText().length() - this.selectionPointer);
                        return;
                    }
                    throw new TypeCastException(str7);
                }
                throw new TypeCastException(str7);
            }
        } else {
            this.editFlag = false;
        }
    }
}

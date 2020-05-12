package ca.albertahealthservices.contacttracing.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.Utils;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.SafeContinuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Job;
import org.json.JSONObject;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000d\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0004\u0018\u0000 &2\u00020\u00012\u00020\u0002:\u0002&'B\u0005¢\u0006\u0002\u0010\u0003J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u0011\u0010\u0011\u001a\u00020\u0012H@ø\u0001\u0000¢\u0006\u0002\u0010\u0013J\b\u0010\u0014\u001a\u00020\u000eH\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0002J&\u0010\u0017\u001a\u0004\u0018\u00010\u00182\u0006\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u001c2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\u001f\u001a\u00020\u0010H\u0016J\u001a\u0010 \u001a\u00020\u00102\u0006\u0010!\u001a\u00020\u00182\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010\"\u001a\u00020\u0016H\u0002J\u0010\u0010#\u001a\u00020$2\u0006\u0010%\u001a\u00020\u000eH\u0002R\u0012\u0010\u0004\u001a\u00020\u0005X\u0005¢\u0006\u0006\u001a\u0004\b\u0006\u0010\u0007R\u0010\u0010\b\u001a\u0004\u0018\u00010\tX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\r\u001a\u0004\u0018\u00010\u000eX\u000e¢\u0006\u0002\n\u0000\u0002\u0004\n\u0002\b\u0019¨\u0006("}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/EnterPinFragment;", "Landroidx/fragment/app/Fragment;", "Lkotlinx/coroutines/CoroutineScope;", "()V", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "disposeObj", "Lio/reactivex/disposables/Disposable;", "otpInputs", "", "Landroid/widget/EditText;", "uploadToken", "", "clearInputs", "", "getEncounterJSON", "Lorg/json/JSONObject;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getOtp", "getUploadToken", "Lkotlinx/coroutines/Job;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onViewCreated", "view", "uploadData", "validateOtp", "", "otp", "Companion", "OTPTextWatcher", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: EnterPinFragment.kt */
public final class EnterPinFragment extends Fragment implements CoroutineScope {
    public static final Companion Companion = new Companion(null);
    public static final String TAG = "UploadFragment";
    public static final String TEMP_UPLOAD_FILE_NAME = "StreetPassRecord.json";
    private final /* synthetic */ CoroutineScope $$delegate_0 = CoroutineScopeKt.MainScope();
    private HashMap _$_findViewCache;
    private Disposable disposeObj;
    /* access modifiers changed from: private */
    public List<EditText> otpInputs = new ArrayList();
    /* access modifiers changed from: private */
    public String uploadToken;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0006"}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/EnterPinFragment$Companion;", "", "()V", "TAG", "", "TEMP_UPLOAD_FILE_NAME", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: EnterPinFragment.kt */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\r\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0016J(\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000fH\u0016J(\u0010\u0012\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u000f2\u0006\u0010\u0011\u001a\u00020\u000fH\u0016R\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006¨\u0006\u0013"}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/EnterPinFragment$OTPTextWatcher;", "Landroid/text/TextWatcher;", "view", "Landroid/widget/EditText;", "(Lca/albertahealthservices/contacttracing/fragment/EnterPinFragment;Landroid/widget/EditText;)V", "getView", "()Landroid/widget/EditText;", "afterTextChanged", "", "editable", "Landroid/text/Editable;", "beforeTextChanged", "arg0", "", "arg1", "", "arg2", "arg3", "onTextChanged", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: EnterPinFragment.kt */
    public final class OTPTextWatcher implements TextWatcher {
        final /* synthetic */ EnterPinFragment this$0;
        private final EditText view;

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "arg0");
        }

        public OTPTextWatcher(EnterPinFragment enterPinFragment, EditText editText) {
            Intrinsics.checkParameterIsNotNull(editText, "view");
            this.this$0 = enterPinFragment;
            this.view = editText;
        }

        public final EditText getView() {
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
                        ((EditText) this.this$0.otpInputs.get(1)).requestFocus();
                        return;
                    }
                    return;
                case R.id.otp_et2 /*2131296507*/:
                    if (obj5.length() == 1) {
                        obj = this.this$0.otpInputs.get(2);
                    } else {
                        if (obj5.length() != 0) {
                            z2 = false;
                        }
                        if (z2) {
                            obj = this.this$0.otpInputs.get(0);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj).requestFocus();
                    return;
                case R.id.otp_et3 /*2131296508*/:
                    if (obj5.length() == 1) {
                        obj2 = this.this$0.otpInputs.get(3);
                    } else {
                        if (obj5.length() == 0) {
                            z = true;
                        }
                        if (z) {
                            obj2 = this.this$0.otpInputs.get(1);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj2).requestFocus();
                    return;
                case R.id.otp_et4 /*2131296509*/:
                    if (obj5.length() == 1) {
                        obj3 = this.this$0.otpInputs.get(4);
                    } else {
                        if (obj5.length() == 0) {
                            z = true;
                        }
                        if (z) {
                            obj3 = this.this$0.otpInputs.get(2);
                        } else {
                            return;
                        }
                    }
                    ((EditText) obj3).requestFocus();
                    return;
                case R.id.otp_et5 /*2131296510*/:
                    if (obj5.length() == 1) {
                        obj4 = this.this$0.otpInputs.get(5);
                    } else {
                        if (obj5.length() == 0) {
                            z = true;
                        }
                        if (z) {
                            obj4 = this.this$0.otpInputs.get(3);
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
                        ((EditText) this.this$0.otpInputs.get(4)).requestFocus();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            Intrinsics.checkParameterIsNotNull(charSequence, "arg0");
            EnterPinFragment enterPinFragment = this.this$0;
            String str = "enterPinButtonText";
            String str2 = "enterPinActionButton";
            if (enterPinFragment.validateOtp(enterPinFragment.getOtp())) {
                Utils utils = Utils.INSTANCE;
                Context context = this.view.getContext();
                Intrinsics.checkExpressionValueIsNotNull(context, "view.context");
                utils.hideKeyboardFrom(context, this.view);
                LinearLayout linearLayout = (LinearLayout) this.this$0._$_findCachedViewById(R.id.enterPinActionButton);
                Intrinsics.checkExpressionValueIsNotNull(linearLayout, str2);
                linearLayout.setEnabled(true);
                TextView textView = (TextView) this.this$0._$_findCachedViewById(R.id.enterPinButtonText);
                Intrinsics.checkExpressionValueIsNotNull(textView, str);
                textView.setText(this.this$0.getString(R.string.upload_button));
                return;
            }
            LinearLayout linearLayout2 = (LinearLayout) this.this$0._$_findCachedViewById(R.id.enterPinActionButton);
            Intrinsics.checkExpressionValueIsNotNull(linearLayout2, str2);
            linearLayout2.setEnabled(false);
            TextView textView2 = (TextView) this.this$0._$_findCachedViewById(R.id.enterPinButtonText);
            Intrinsics.checkExpressionValueIsNotNull(textView2, str);
            textView2.setText(this.this$0.getString(R.string.submit_button));
        }
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

    public CoroutineContext getCoroutineContext() {
        return this.$$delegate_0.getCoroutineContext();
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(R.layout.fragment_upload_enterpin, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        clearInputs();
        LinearLayout linearLayout = (LinearLayout) _$_findCachedViewById(R.id.enterPinActionButton);
        if (linearLayout != null) {
            linearLayout.setEnabled(false);
        }
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
            editText7.addTextChangedListener(new OTPTextWatcher(this, editText8));
        }
        EditText editText9 = (EditText) _$_findCachedViewById(R.id.otp_et2);
        if (editText9 != null) {
            EditText editText10 = (EditText) _$_findCachedViewById(R.id.otp_et2);
            Intrinsics.checkExpressionValueIsNotNull(editText10, str2);
            editText9.addTextChangedListener(new OTPTextWatcher(this, editText10));
        }
        EditText editText11 = (EditText) _$_findCachedViewById(R.id.otp_et3);
        if (editText11 != null) {
            EditText editText12 = (EditText) _$_findCachedViewById(R.id.otp_et3);
            Intrinsics.checkExpressionValueIsNotNull(editText12, str3);
            editText11.addTextChangedListener(new OTPTextWatcher(this, editText12));
        }
        EditText editText13 = (EditText) _$_findCachedViewById(R.id.otp_et4);
        if (editText13 != null) {
            EditText editText14 = (EditText) _$_findCachedViewById(R.id.otp_et4);
            Intrinsics.checkExpressionValueIsNotNull(editText14, str4);
            editText13.addTextChangedListener(new OTPTextWatcher(this, editText14));
        }
        EditText editText15 = (EditText) _$_findCachedViewById(R.id.otp_et5);
        if (editText15 != null) {
            EditText editText16 = (EditText) _$_findCachedViewById(R.id.otp_et5);
            Intrinsics.checkExpressionValueIsNotNull(editText16, str5);
            editText15.addTextChangedListener(new OTPTextWatcher(this, editText16));
        }
        EditText editText17 = (EditText) _$_findCachedViewById(R.id.otp_et6);
        if (editText17 != null) {
            EditText editText18 = (EditText) _$_findCachedViewById(R.id.otp_et6);
            Intrinsics.checkExpressionValueIsNotNull(editText18, str6);
            editText17.addTextChangedListener(new OTPTextWatcher(this, editText18));
        }
        if (this.uploadToken == null) {
            getUploadToken();
        }
        LinearLayout linearLayout2 = (LinearLayout) _$_findCachedViewById(R.id.enterPinActionButton);
        if (linearLayout2 != null) {
            linearLayout2.setOnClickListener(new EnterPinFragment$onViewCreated$1(this));
        }
        AppCompatImageView appCompatImageView = (AppCompatImageView) _$_findCachedViewById(R.id.enterPinBackButton);
        if (appCompatImageView != null) {
            appCompatImageView.setOnClickListener(new EnterPinFragment$onViewCreated$2(this));
        }
    }

    public void onDestroy() {
        CoroutineScopeKt.cancel$default(this, null, 1, null);
        super.onDestroy();
        Disposable disposable = this.disposeObj;
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private final Job getUploadToken() {
        return BuildersKt__Builders_commonKt.launch$default(this, null, null, new EnterPinFragment$getUploadToken$1(this, null), 3, null);
    }

    /* access modifiers changed from: private */
    public final Job uploadData() {
        return BuildersKt__Builders_commonKt.launch$default(this, null, null, new EnterPinFragment$uploadData$1(this, null), 3, null);
    }

    /* access modifiers changed from: 0000 */
    public final /* synthetic */ Object getEncounterJSON(Continuation<? super JSONObject> continuation) {
        SafeContinuation safeContinuation = new SafeContinuation(IntrinsicsKt.intercepted(continuation));
        Continuation continuation2 = safeContinuation;
        Observable create = Observable.create(EnterPinFragment$getEncounterJSON$2$observableStreetRecords$1.INSTANCE);
        String str = "Observable.create<List<S…result)\n                }";
        Intrinsics.checkExpressionValueIsNotNull(create, str);
        Observable create2 = Observable.create(EnterPinFragment$getEncounterJSON$2$observableStatusRecords$1.INSTANCE);
        Intrinsics.checkExpressionValueIsNotNull(create2, str);
        this.disposeObj = Observable.zip(create, create2, EnterPinFragment$getEncounterJSON$2$1.INSTANCE).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe((Consumer<? super T>) new EnterPinFragment$getEncounterJSON$$inlined$suspendCoroutine$lambda$1<Object>(continuation2, this));
        Object orThrow = safeContinuation.getOrThrow();
        if (orThrow == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return orThrow;
    }

    /* access modifiers changed from: private */
    public final void clearInputs() {
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

    /* access modifiers changed from: private */
    public final boolean validateOtp(String str) {
        return str.length() >= 6;
    }
}

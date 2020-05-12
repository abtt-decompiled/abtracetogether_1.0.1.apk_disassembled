package ca.albertahealthservices.contacttracing.onboarding;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.Regex;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000Z\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\u0018\u00002\u00020\u0001:\u0001+B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0016J \u0010\u000f\u001a\u00020\u00102\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u00042\u0006\u0010\u0013\u001a\u00020\u0014H\u0002J\u0010\u0010\u0015\u001a\u00020\u000e2\u0006\u0010\u0016\u001a\u00020\bH\u0016J\u0010\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u0019H\u0016J\u0010\u0010\u001a\u001a\u00020\u000e2\u0006\u0010\u001b\u001a\u00020\u0019H\u0016J\u0012\u0010\u001c\u001a\u00020\u000e2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J&\u0010\u001f\u001a\u0004\u0018\u00010\u00192\u0006\u0010 \u001a\u00020!2\b\u0010\"\u001a\u0004\u0018\u00010#2\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010$\u001a\u00020\u000eH\u0016J\u0010\u0010%\u001a\u00020\u000e2\u0006\u0010&\u001a\u00020\u0004H\u0016J\u0010\u0010'\u001a\u00020\u000e2\u0006\u0010(\u001a\u00020\u0004H\u0016J\u001a\u0010)\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u00192\b\u0010\u001d\u001a\u0004\u0018\u00010\u001eH\u0016J\b\u0010*\u001a\u00020\u000eH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0006X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX.¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX.¢\u0006\u0002\n\u0000¨\u0006,"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/TOUFragment;", "Lca/albertahealthservices/contacttracing/onboarding/OnboardingFragmentInterface;", "()V", "TAG", "", "listener", "Lca/albertahealthservices/contacttracing/onboarding/TOUFragment$OnFragmentInteractionListener;", "mainContext", "Landroid/content/Context;", "param1", "param2", "privacyTextView", "Landroid/widget/TextView;", "becomesVisible", "", "createSpannableString", "Landroid/text/SpannableString;", "string", "span", "clickableSpan", "Landroid/text/style/ClickableSpan;", "onAttach", "context", "onBackButtonClick", "view", "Landroid/view/View;", "onButtonClick", "buttonView", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onCreateView", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDetach", "onError", "error", "onUpdatePhoneNumber", "num", "onViewCreated", "sendEmailIntent", "OnFragmentInteractionListener", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: TOUFragment.kt */
public final class TOUFragment extends OnboardingFragmentInterface {
    /* access modifiers changed from: private */
    public final String TAG = "TOUFragment";
    private HashMap _$_findViewCache;
    private OnFragmentInteractionListener listener;
    /* access modifiers changed from: private */
    public Context mainContext;
    private String param1;
    private String param2;
    private TextView privacyTextView;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/TOUFragment$OnFragmentInteractionListener;", "", "onFragmentInteraction", "", "uri", "Landroid/net/Uri;", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: TOUFragment.kt */
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

    public void becomesVisible() {
    }

    public void onBackButtonClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "view");
    }

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public void onError(String str) {
        Intrinsics.checkParameterIsNotNull(str, "error");
    }

    public void onUpdatePhoneNumber(String str) {
        Intrinsics.checkParameterIsNotNull(str, "num");
    }

    public static final /* synthetic */ Context access$getMainContext$p(TOUFragment tOUFragment) {
        Context context = tOUFragment.mainContext;
        if (context == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mainContext");
        }
        return context;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.param1 = arguments.getString("param1");
            this.param2 = arguments.getString("param2");
        }
    }

    public void onButtonClick(View view) {
        Intrinsics.checkParameterIsNotNull(view, "buttonView");
        CentralLog.Companion.d(this.TAG, "OnButtonClick 4");
        Context context = getContext();
        if (context != null) {
            ((OnboardingActivity) context).navigateToNextPage();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.onboarding.OnboardingActivity");
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(R.layout.fragment_tou, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        Button button = (Button) _$_findCachedViewById(R.id.privacy_button);
        if (button != null) {
            button.setOnClickListener(new TOUFragment$onViewCreated$1(this));
        }
        Button button2 = (Button) _$_findCachedViewById(R.id.faq_button);
        if (button2 != null) {
            button2.setOnClickListener(new TOUFragment$onViewCreated$2(this));
        }
        disableButton();
        ClickableSpan tOUFragment$onViewCreated$privacyClickableSpan$1 = new TOUFragment$onViewCreated$privacyClickableSpan$1(this);
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc4);
        if (appCompatTextView != null) {
            AppCompatTextView appCompatTextView2 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc4);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, "privacy_desc4");
            appCompatTextView.setText(createSpannableString(appCompatTextView2.getText().toString(), "Privacy Statement", tOUFragment$onViewCreated$privacyClickableSpan$1));
        }
        ClickableSpan tOUFragment$onViewCreated$emailClickableSpan$1 = new TOUFragment$onViewCreated$emailClickableSpan$1(this);
        AppCompatTextView appCompatTextView3 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc7);
        String str = "HiaHelpDesk@gov.ab.ca";
        if (appCompatTextView3 != null) {
            AppCompatTextView appCompatTextView4 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc7);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView4, "privacy_desc7");
            appCompatTextView3.setText(createSpannableString(appCompatTextView4.getText().toString(), str, tOUFragment$onViewCreated$emailClickableSpan$1));
        }
        AppCompatTextView appCompatTextView5 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc8);
        if (appCompatTextView5 != null) {
            AppCompatTextView appCompatTextView6 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc8);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView6, "privacy_desc8");
            appCompatTextView5.setText(createSpannableString(appCompatTextView6.getText().toString(), str, tOUFragment$onViewCreated$emailClickableSpan$1));
        }
        Switch switchR = (Switch) _$_findCachedViewById(R.id.checkbox_agreement);
        if (switchR != null) {
            switchR.setOnCheckedChangeListener(new TOUFragment$onViewCreated$3(this));
        }
        AppCompatTextView appCompatTextView7 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc4);
        if (appCompatTextView7 != null) {
            appCompatTextView7.setMovementMethod(LinkMovementMethod.getInstance());
        }
        AppCompatTextView appCompatTextView8 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc7);
        if (appCompatTextView8 != null) {
            appCompatTextView8.setMovementMethod(LinkMovementMethod.getInstance());
        }
        AppCompatTextView appCompatTextView9 = (AppCompatTextView) _$_findCachedViewById(R.id.privacy_desc8);
        if (appCompatTextView9 != null) {
            appCompatTextView9.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public void onAttach(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        super.onAttach(context);
        this.mainContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            this.listener = (OnFragmentInteractionListener) context;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(context);
        sb.append(" must  implement OnFragmentInteractionListener");
        throw new RuntimeException(sb.toString());
    }

    public void onDetach() {
        super.onDetach();
        this.listener = null;
    }

    /* access modifiers changed from: private */
    public final void sendEmailIntent() {
        String[] strArr = {"HiaHelpDesk@gov.ab.ca"};
        Intent intent = new Intent("android.intent.action.SENDTO");
        intent.setType("text/plain");
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra("android.intent.extra.EMAIL", strArr);
        Context context = getContext();
        if (context == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(context, "context!!");
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private final SpannableString createSpannableString(String str, String str2, ClickableSpan clickableSpan) {
        CharSequence charSequence = str;
        int indexOf$default = StringsKt.indexOf$default(charSequence, "%s", 0, false, 6, (Object) null);
        SpannableString spannableString = new SpannableString(new Regex("\\%s\\b").replaceFirst(charSequence, str2));
        spannableString.setSpan(clickableSpan, indexOf$default, str2.length() + indexOf$default, 33);
        return spannableString;
    }
}

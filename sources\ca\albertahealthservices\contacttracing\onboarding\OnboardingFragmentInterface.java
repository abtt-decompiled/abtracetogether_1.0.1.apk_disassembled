package ca.albertahealthservices.contacttracing.onboarding;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import ca.albertahealthservices.contacttracing.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0004\b&\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\u0006\u001a\u00020\u0007H&J\u0006\u0010\b\u001a\u00020\u0007J\u0006\u0010\t\u001a\u00020\u0007J\u0010\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0004H&J\u0010\u0010\f\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\u0004H&J\u0010\u0010\r\u001a\u00020\u00072\u0006\u0010\u000e\u001a\u00020\u000fH&J\u0010\u0010\u0010\u001a\u00020\u00072\u0006\u0010\u0011\u001a\u00020\u000fH&J\u001a\u0010\u0012\u001a\u00020\u00072\u0006\u0010\u0013\u001a\u00020\u00042\b\u0010\u0014\u001a\u0004\u0018\u00010\u0015H\u0016J\u000e\u0010\u0016\u001a\u00020\u00072\u0006\u0010\u0017\u001a\u00020\u0018J\u000e\u0010\u0019\u001a\u00020\u00072\u0006\u0010\u001a\u001a\u00020\u000fJ\b\u0010\u001b\u001a\u00020\u0007H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u0004\u0018\u00010\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001c"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/OnboardingFragmentInterface;", "Landroidx/fragment/app/Fragment;", "()V", "actionButton", "Landroid/view/View;", "backButton", "becomesVisible", "", "disableButton", "enableButton", "onBackButtonClick", "buttonView", "onButtonClick", "onError", "error", "", "onUpdatePhoneNumber", "num", "onViewCreated", "view", "savedInstanceState", "Landroid/os/Bundle;", "setButtonIcon", "id", "", "setButtonText", "string", "setupButton", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: OnboardingFragmentInterface.kt */
public abstract class OnboardingFragmentInterface extends Fragment {
    private HashMap _$_findViewCache;
    private View actionButton;
    private View backButton;

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

    public abstract void becomesVisible();

    public abstract void onBackButtonClick(View view);

    public abstract void onButtonClick(View view);

    public /* synthetic */ void onDestroyView() {
        super.onDestroyView();
        _$_clearFindViewByIdCache();
    }

    public abstract void onError(String str);

    public abstract void onUpdatePhoneNumber(String str);

    private final void setupButton() {
        LinearLayout linearLayout = (LinearLayout) _$_findCachedViewById(R.id.onboardingButton);
        if (linearLayout != null) {
            this.actionButton = linearLayout;
            linearLayout.setOnClickListener(new OnboardingFragmentInterface$setupButton$$inlined$let$lambda$1(this));
        }
        AppCompatImageView appCompatImageView = (AppCompatImageView) _$_findCachedViewById(R.id.onboardingBackButton);
        if (appCompatImageView != null) {
            this.backButton = appCompatImageView;
            appCompatImageView.setOnClickListener(new OnboardingFragmentInterface$setupButton$$inlined$let$lambda$2(this));
        }
    }

    public final void enableButton() {
        View view = this.actionButton;
        if (view != null) {
            view.setEnabled(true);
        }
    }

    public final void disableButton() {
        View view = this.actionButton;
        if (view != null) {
            view.setEnabled(false);
        }
    }

    public final void setButtonText(String str) {
        Intrinsics.checkParameterIsNotNull(str, "string");
        TextView textView = (TextView) _$_findCachedViewById(R.id.onboardingButtonText);
        if (textView != null) {
            textView.setText(str);
        }
    }

    public final void setButtonIcon(int i) {
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.onboardingButtonIcon);
        if (imageView != null) {
            imageView.setImageResource(i);
        }
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        setupButton();
    }
}

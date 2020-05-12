package ca.albertahealthservices.contacttracing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import ca.albertahealthservices.contacttracing.MainActivity;
import ca.albertahealthservices.contacttracing.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0006\u0010\u0003\u001a\u00020\u0004J\u0006\u0010\u0005\u001a\u00020\u0004J\u0006\u0010\u0006\u001a\u00020\u0004J\u0006\u0010\u0007\u001a\u00020\u0004J&\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000b2\b\u0010\f\u001a\u0004\u0018\u00010\r2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u001a\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0016J\u0006\u0010\u0012\u001a\u00020\u0004J\u0006\u0010\u0013\u001a\u00020\u0004J\u0006\u0010\u0014\u001a\u00020\u0004¨\u0006\u0015"}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/UploadPageFragment;", "Landroidx/fragment/app/Fragment;", "()V", "goBackToHome", "", "navigateToOTCFragment", "navigateToUploadComplete", "navigateToUploadPin", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "view", "popStack", "turnOffLoadingProgress", "turnOnLoadingProgress", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: UploadPageFragment.kt */
public final class UploadPageFragment extends Fragment {
    private HashMap _$_findViewCache;

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

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        return layoutInflater.inflate(R.layout.fragment_upload_page, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(childFragmentManager, "childFragmentManager");
        FragmentTransaction beginTransaction = childFragmentManager.beginTransaction();
        Intrinsics.checkExpressionValueIsNotNull(beginTransaction, "childFragMan.beginTransaction()");
        beginTransaction.add((int) R.id.fragment_placeholder, (Fragment) new VerifyCallerFragment());
        beginTransaction.addToBackStack("VerifyCaller");
        beginTransaction.commit();
    }

    public final void turnOnLoadingProgress() {
        FrameLayout frameLayout = (FrameLayout) _$_findCachedViewById(R.id.uploadPageFragmentLoadingProgressBarFrame);
        Intrinsics.checkExpressionValueIsNotNull(frameLayout, "uploadPageFragmentLoadingProgressBarFrame");
        frameLayout.setVisibility(0);
    }

    public final void turnOffLoadingProgress() {
        FrameLayout frameLayout = (FrameLayout) _$_findCachedViewById(R.id.uploadPageFragmentLoadingProgressBarFrame);
        Intrinsics.checkExpressionValueIsNotNull(frameLayout, "uploadPageFragmentLoadingProgressBarFrame");
        frameLayout.setVisibility(4);
    }

    public final void navigateToUploadPin() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(childFragmentManager, "childFragmentManager");
        FragmentTransaction beginTransaction = childFragmentManager.beginTransaction();
        Intrinsics.checkExpressionValueIsNotNull(beginTransaction, "childFragMan.beginTransaction()");
        beginTransaction.add((int) R.id.fragment_placeholder, (Fragment) new EnterPinFragment());
        beginTransaction.addToBackStack("C");
        beginTransaction.commit();
    }

    public final void goBackToHome() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            ((MainActivity) activity).goToHome();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.MainActivity");
    }

    public final void navigateToUploadComplete() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(childFragmentManager, "childFragmentManager");
        FragmentTransaction beginTransaction = childFragmentManager.beginTransaction();
        Intrinsics.checkExpressionValueIsNotNull(beginTransaction, "childFragMan.beginTransaction()");
        beginTransaction.add((int) R.id.fragment_placeholder, (Fragment) new UploadCompleteFragment());
        beginTransaction.addToBackStack("UploadComplete");
        beginTransaction.commit();
    }

    public final void navigateToOTCFragment() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            MainActivity mainActivity = (MainActivity) activity;
            int layout_main_id = mainActivity.getLAYOUT_MAIN_ID();
            Fragment forUseByOTCFragment = new ForUseByOTCFragment();
            String name = ForUseByOTCFragment.class.getName();
            Intrinsics.checkExpressionValueIsNotNull(name, "ForUseByOTCFragment::class.java.name");
            mainActivity.openFragment(layout_main_id, forUseByOTCFragment, name, 0);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.MainActivity");
    }

    public final void popStack() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(childFragmentManager, "childFragmentManager");
        childFragmentManager.popBackStack();
    }
}

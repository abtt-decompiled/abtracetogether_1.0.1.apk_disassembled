package ca.albertahealthservices.contacttracing.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;
import ca.albertahealthservices.contacttracing.BuildConfig;
import ca.albertahealthservices.contacttracing.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J&\u0010\u0007\u001a\u0004\u0018\u00010\b2\u0006\u0010\u0005\u001a\u00020\u00062\b\u0010\t\u001a\u0004\u0018\u00010\n2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016J\u001a\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\b2\b\u0010\u000b\u001a\u0004\u0018\u00010\fH\u0016R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000¨\u0006\u0010"}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/HelpFragment;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "inflater", "Landroid/view/LayoutInflater;", "onCreateView", "Landroid/view/View;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onViewCreated", "", "view", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: HelpFragment.kt */
public final class HelpFragment extends Fragment {
    /* access modifiers changed from: private */
    public final String TAG = "HomeFragment";
    private HashMap _$_findViewCache;
    private LayoutInflater inflater;

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
        this.inflater = layoutInflater;
        return layoutInflater.inflate(R.layout.fragment_help, viewGroup, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        WebView webView = (WebView) _$_findCachedViewById(R.id.help_webview);
        if (webView != null) {
            webView.setWebViewClient(new WebViewClient());
        }
        WebView webView2 = (WebView) _$_findCachedViewById(R.id.help_webview);
        WebSettings webSettings = null;
        WebSettings settings = webView2 != null ? webView2.getSettings() : null;
        if (settings == null) {
            Intrinsics.throwNpe();
        }
        settings.setJavaScriptEnabled(true);
        WebView webView3 = (WebView) _$_findCachedViewById(R.id.help_webview);
        if (webView3 != null) {
            webSettings = webView3.getSettings();
        }
        if (webSettings == null) {
            Intrinsics.throwNpe();
        }
        webSettings.setLoadWithOverviewMode(true);
        WebView webView4 = (WebView) _$_findCachedViewById(R.id.help_webview);
        if (webView4 != null) {
            webView4.loadUrl(BuildConfig.FAQ_URL);
        }
        WebChromeClient helpFragment$onViewCreated$wbc$1 = new HelpFragment$onViewCreated$wbc$1(this);
        WebView webView5 = (WebView) _$_findCachedViewById(R.id.help_webview);
        if (webView5 != null) {
            webView5.setWebChromeClient(helpFragment$onViewCreated$wbc$1);
        }
    }
}

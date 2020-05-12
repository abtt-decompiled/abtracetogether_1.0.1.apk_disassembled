package ca.albertahealthservices.contacttracing.onboarding;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import ca.albertahealthservices.contacttracing.BuildConfig;
import ca.albertahealthservices.contacttracing.R;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0005\u001a\u00020\u00062\b\u0010\u0007\u001a\u0004\u0018\u00010\bH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000¨\u0006\t"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/WebViewActivity;", "Landroidx/fragment/app/FragmentActivity;", "()V", "TAG", "", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: WebViewActivity.kt */
public final class WebViewActivity extends FragmentActivity {
    /* access modifiers changed from: private */
    public final String TAG = "WebViewActivity";
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
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(R.layout.webview);
        WebView webView = (WebView) _$_findCachedViewById(R.id.webview);
        if (webView != null) {
            webView.setWebViewClient(new WebViewClient());
        }
        if (getIntent().getIntExtra("type", 0) == 1) {
            AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.tv_title);
            if (appCompatTextView != null) {
                appCompatTextView.setText(getString(R.string.faq_webview_title));
            }
            str = BuildConfig.FAQ_URL;
        } else {
            AppCompatTextView appCompatTextView2 = (AppCompatTextView) _$_findCachedViewById(R.id.tv_title);
            if (appCompatTextView2 != null) {
                appCompatTextView2.setText(getString(R.string.privacy_policy_webview_title));
            }
            str = BuildConfig.PRIVACY_URL;
        }
        WebView webView2 = (WebView) _$_findCachedViewById(R.id.webview);
        WebSettings webSettings = null;
        WebSettings settings = webView2 != null ? webView2.getSettings() : null;
        if (settings == null) {
            Intrinsics.throwNpe();
        }
        settings.setJavaScriptEnabled(true);
        WebView webView3 = (WebView) _$_findCachedViewById(R.id.webview);
        if (webView3 != null) {
            webSettings = webView3.getSettings();
        }
        if (webSettings == null) {
            Intrinsics.throwNpe();
        }
        webSettings.setLoadWithOverviewMode(true);
        WebView webView4 = (WebView) _$_findCachedViewById(R.id.webview);
        if (webView4 != null) {
            webView4.loadUrl(str);
        }
        WebChromeClient webViewActivity$onCreate$wbc$1 = new WebViewActivity$onCreate$wbc$1(this);
        WebView webView5 = (WebView) _$_findCachedViewById(R.id.webview);
        if (webView5 != null) {
            webView5.setWebChromeClient(webViewActivity$onCreate$wbc$1);
        }
        AppCompatImageView appCompatImageView = (AppCompatImageView) _$_findCachedViewById(R.id.webviewBackButton);
        if (appCompatImageView != null) {
            appCompatImageView.setOnClickListener(new WebViewActivity$onCreate$$inlined$let$lambda$1(this));
        }
    }
}

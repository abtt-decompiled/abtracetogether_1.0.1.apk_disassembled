package ca.albertahealthservices.contacttracing;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import ca.albertahealthservices.contacttracing.onboarding.PreOnboardingActivity;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0012\u0010\u000f\u001a\u00020\u000e2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0011H\u0014J\b\u0010\u0012\u001a\u00020\u000eH\u0014J\b\u0010\u0013\u001a\u00020\u000eH\u0014R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X.¢\u0006\u0002\n\u0000R\u001a\u0010\u0007\u001a\u00020\bX\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\t\u0010\n\"\u0004\b\u000b\u0010\f¨\u0006\u0014"}, d2 = {"Lca/albertahealthservices/contacttracing/SplashActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "SPLASH_TIME", "", "mHandler", "Landroid/os/Handler;", "needToUpdateApp", "", "getNeedToUpdateApp", "()Z", "setNeedToUpdateApp", "(Z)V", "goToNextScreen", "", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onPause", "onResume", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: SplashActivity.kt */
public final class SplashActivity extends AppCompatActivity {
    private final long SPLASH_TIME = 2000;
    private HashMap _$_findViewCache;
    private Handler mHandler;
    private boolean needToUpdateApp;

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

    public final boolean getNeedToUpdateApp() {
        return this.needToUpdateApp;
    }

    public final void setNeedToUpdateApp(boolean z) {
        this.needToUpdateApp = z;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);
        this.mHandler = new Handler();
        Intent intent = getIntent();
        String str = "intent";
        Intrinsics.checkExpressionValueIsNotNull(intent, str);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            String string = extras.getString(NotificationCompat.CATEGORY_EVENT, null);
            if (string != null && string.equals("update")) {
                this.needToUpdateApp = true;
                setIntent(new Intent("android.intent.action.VIEW"));
                Intent intent2 = getIntent();
                Intrinsics.checkExpressionValueIsNotNull(intent2, str);
                intent2.setData(Uri.parse(BuildConfig.STORE_URL));
                startActivity(getIntent());
                finish();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        Handler handler = this.mHandler;
        if (handler == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mHandler");
        }
        handler.removeCallbacksAndMessages(null);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (!this.needToUpdateApp) {
            Handler handler = this.mHandler;
            if (handler == null) {
                Intrinsics.throwUninitializedPropertyAccessException("mHandler");
            }
            handler.postDelayed(new SplashActivity$onResume$1(this), this.SPLASH_TIME);
        }
    }

    /* access modifiers changed from: private */
    public final void goToNextScreen() {
        Context context = this;
        if (!Preference.INSTANCE.isOnBoarded(context)) {
            startActivity(new Intent(context, PreOnboardingActivity.class));
        } else {
            startActivity(new Intent(context, MainActivity.class));
        }
    }
}

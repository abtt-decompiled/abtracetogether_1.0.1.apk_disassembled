package ca.albertahealthservices.contacttracing;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0003\u001a\u00020\u00042\b\u0010\u0005\u001a\u0004\u0018\u00010\u0006H\u0014J\b\u0010\u0007\u001a\u00020\u0004H\u0002¨\u0006\b"}, d2 = {"Lca/albertahealthservices/contacttracing/RestartActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "onCreate", "", "savedInstanceState", "Landroid/os/Bundle;", "restartApp", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: RestartActivity.kt */
public final class RestartActivity extends AppCompatActivity {
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
        super.onCreate(bundle);
        Context context = this;
        Preference.INSTANCE.putIsOnBoarded(context, false);
        Preference.INSTANCE.putUUIDRetryAttempts(context, Preference.INSTANCE.getUUIDRetryAttempts(context) + 1);
        Builder builder = new Builder(context);
        builder.setMessage(getString(R.string.restart_msg));
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new RestartActivity$onCreate$1(this));
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public final void restartApp() {
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(32768);
        intent.addFlags(268435456);
        startActivity(intent);
    }
}

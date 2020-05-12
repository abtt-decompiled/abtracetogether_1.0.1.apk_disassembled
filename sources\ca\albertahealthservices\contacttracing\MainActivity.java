package ca.albertahealthservices.contacttracing;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.HashMap;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\b\u0010\r\u001a\u00020\u000eH\u0002J\u0006\u0010\u000f\u001a\u00020\u000eJ\u0014\u0010\u0010\u001a\u00020\u00112\n\u0010\u0012\u001a\u0006\u0012\u0002\b\u00030\u0013H\u0002J\u0012\u0010\u0014\u001a\u00020\u000e2\b\u0010\u0015\u001a\u0004\u0018\u00010\u0016H\u0014J&\u0010\u0017\u001a\u00020\u000e2\u0006\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\n2\u0006\u0010\u001c\u001a\u00020\u0004R\u001a\u0010\u0003\u001a\u00020\u0004X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b\u0005\u0010\u0006\"\u0004\b\u0007\u0010\bR\u000e\u0010\t\u001a\u00020\nXD¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0004X\u000e¢\u0006\u0002\n\u0000¨\u0006\u001d"}, d2 = {"Lca/albertahealthservices/contacttracing/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "()V", "LAYOUT_MAIN_ID", "", "getLAYOUT_MAIN_ID", "()I", "setLAYOUT_MAIN_ID", "(I)V", "TAG", "", "mNavigationLevel", "selected", "getFCMToken", "", "goToHome", "isMyServiceRunning", "", "serviceClass", "Ljava/lang/Class;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "openFragment", "containerViewId", "fragment", "Landroidx/fragment/app/Fragment;", "tag", "title", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: MainActivity.kt */
public final class MainActivity extends AppCompatActivity {
    private int LAYOUT_MAIN_ID;
    private final String TAG = "MainActivity";
    private HashMap _$_findViewCache;
    private int mNavigationLevel;
    /* access modifiers changed from: private */
    public int selected;

    private final void getFCMToken() {
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
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public final int getLAYOUT_MAIN_ID() {
        return this.LAYOUT_MAIN_ID;
    }

    public final void setLAYOUT_MAIN_ID(int i) {
        this.LAYOUT_MAIN_ID = i;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main_new);
        Utils.INSTANCE.startBluetoothMonitoringService(this);
        this.LAYOUT_MAIN_ID = R.id.content;
        ((BottomNavigationView) _$_findCachedViewById(R.id.nav_view)).setOnNavigationItemSelectedListener(new MainActivity$onCreate$mOnNavigationItemSelectedListener$1(this));
        goToHome();
        getFCMToken();
    }

    private final boolean isMyServiceRunning(Class<?> cls) {
        Object systemService = getSystemService("activity");
        if (systemService != null) {
            for (RunningServiceInfo runningServiceInfo : ((ActivityManager) systemService).getRunningServices(Integer.MAX_VALUE)) {
                String name = cls.getName();
                ComponentName componentName = runningServiceInfo.service;
                Intrinsics.checkExpressionValueIsNotNull(componentName, "service.service");
                if (Intrinsics.areEqual((Object) name, (Object) componentName.getClassName())) {
                    return true;
                }
            }
            return false;
        }
        throw new TypeCastException("null cannot be cast to non-null type android.app.ActivityManager");
    }

    public final void goToHome() {
        BottomNavigationView bottomNavigationView = (BottomNavigationView) _$_findCachedViewById(R.id.nav_view);
        Intrinsics.checkExpressionValueIsNotNull(bottomNavigationView, "nav_view");
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    public final void openFragment(int i, Fragment fragment, String str, int i2) {
        Intrinsics.checkParameterIsNotNull(fragment, "fragment");
        Intrinsics.checkParameterIsNotNull(str, "tag");
        try {
            getSupportFragmentManager().popBackStackImmediate(this.LAYOUT_MAIN_ID, 1);
            this.mNavigationLevel = 0;
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            Intrinsics.checkExpressionValueIsNotNull(beginTransaction, "supportFragmentManager.beginTransaction()");
            beginTransaction.replace(i, fragment, str);
            beginTransaction.commit();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}

package ca.albertahealthservices.contacttracing.fragment;

import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewKt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import ca.albertahealthservices.contacttracing.BuildConfig;
import ca.albertahealthservices.contacttracing.MainActivity;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.TracerApp;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecord;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecordDatabase;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecordDatabase.Companion;
import com.airbnb.lottie.LottieAnimationView;
import java.util.Arrays;
import java.util.HashMap;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000v\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\n\u0018\u00002\u00020\u0001B\u0005¢\u0006\u0002\u0010\u0002J\u0012\u0010\u0019\u001a\u00020\u001a2\b\u0010\u001b\u001a\u0004\u0018\u00010\u0004H\u0002J\b\u0010\u001c\u001a\u00020\u0016H\u0002J\b\u0010\u001d\u001a\u00020\u001aH\u0002J\b\u0010\u001e\u001a\u00020\u001aH\u0002J\b\u0010\u001f\u001a\u00020\u0016H\u0002J\u0012\u0010 \u001a\u00020\u001a2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\"\u0010#\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020\u00062\u0006\u0010%\u001a\u00020\u00062\b\u0010&\u001a\u0004\u0018\u00010'H\u0016J&\u0010(\u001a\u0004\u0018\u00010)2\u0006\u0010*\u001a\u00020+2\b\u0010,\u001a\u0004\u0018\u00010-2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u0010.\u001a\u00020\u001aH\u0016J\b\u0010/\u001a\u00020\u001aH\u0016J-\u00100\u001a\u00020\u001a2\u0006\u0010$\u001a\u00020\u00062\u000e\u00101\u001a\n\u0012\u0006\b\u0001\u0012\u00020\u0004022\u0006\u00103\u001a\u000204H\u0016¢\u0006\u0002\u00105J\b\u00106\u001a\u00020\u001aH\u0016J\u001a\u00107\u001a\u00020\u001a2\u0006\u00108\u001a\u00020)2\b\u0010!\u001a\u0004\u0018\u00010\"H\u0016J\b\u00109\u001a\u00020\u001aH\u0007J\b\u0010:\u001a\u00020\u001aH\u0002J\b\u0010;\u001a\u00020\u001aH\u0002J\u0006\u0010<\u001a\u00020\u001aJ\b\u0010=\u001a\u00020\u001aH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XD¢\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u001d\u0010\u0007\u001a\u0004\u0018\u00010\b8BX\u0002¢\u0006\f\n\u0004\b\u000b\u0010\f\u001a\u0004\b\t\u0010\nR\u000e\u0010\r\u001a\u00020\u0006X\u000e¢\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u000fX.¢\u0006\u0002\n\u0000R\u000e\u0010\u0011\u001a\u00020\u0012X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0013\u001a\u00020\u0014X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\u0016X\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u0017\u001a\u00020\u0016*\u00020\b8BX\u0004¢\u0006\u0006\u001a\u0004\b\u0017\u0010\u0018¨\u0006>"}, d2 = {"Lca/albertahealthservices/contacttracing/fragment/HomeFragment;", "Landroidx/fragment/app/Fragment;", "()V", "TAG", "", "animationWindow", "", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "getBluetoothAdapter", "()Landroid/bluetooth/BluetoothAdapter;", "bluetoothAdapter$delegate", "Lkotlin/Lazy;", "counter", "lastKnownScanningStarted", "Landroidx/lifecycle/LiveData;", "Lca/albertahealthservices/contacttracing/status/persistence/StatusRecord;", "listener", "Landroid/content/SharedPreferences$OnSharedPreferenceChangeListener;", "mBroadcastListener", "Landroid/content/BroadcastReceiver;", "mIsBroadcastListenerRegistered", "", "isDisabled", "(Landroid/bluetooth/BluetoothAdapter;)Z", "alertDialog", "", "desc", "canRequestBatteryOptimizerExemption", "clearAndHideAnnouncement", "enableBluetooth", "isShowRestartSetup", "onActivityCreated", "savedInstanceState", "Landroid/os/Bundle;", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "onDestroyView", "onPause", "onRequestPermissionsResult", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "onViewCreated", "view", "setupPermissionsAndSettings", "shareThisApp", "showNonEmptyAnnouncement", "showSetup", "updatedPremLabels", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: HomeFragment.kt */
public final class HomeFragment extends Fragment {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(HomeFragment.class), "bluetoothAdapter", "getBluetoothAdapter()Landroid/bluetooth/BluetoothAdapter;"))};
    /* access modifiers changed from: private */
    public final String TAG = "HomeFragment";
    private HashMap _$_findViewCache;
    /* access modifiers changed from: private */
    public int animationWindow = 10000;
    private final Lazy bluetoothAdapter$delegate = LazyKt.lazy(LazyThreadSafetyMode.NONE, (Function0) new HomeFragment$bluetoothAdapter$2(this));
    /* access modifiers changed from: private */
    public int counter;
    private LiveData<StatusRecord> lastKnownScanningStarted;
    private OnSharedPreferenceChangeListener listener = new HomeFragment$listener$1(this);
    private final BroadcastReceiver mBroadcastListener = new HomeFragment$mBroadcastListener$1(this);
    private boolean mIsBroadcastListenerRegistered;

    private final BluetoothAdapter getBluetoothAdapter() {
        Lazy lazy = this.bluetoothAdapter$delegate;
        KProperty kProperty = $$delegatedProperties[0];
        return (BluetoothAdapter) lazy.getValue();
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

    public void onViewCreated(View view, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(view, "view");
        super.onViewCreated(view, bundle);
        Companion companion = StreetPassRecordDatabase.Companion;
        Context context = view.getContext();
        Intrinsics.checkExpressionValueIsNotNull(context, "view.context");
        LiveData<StatusRecord> mostRecentRecord = companion.getDatabase(context).statusDao().getMostRecentRecord("Scanning Started");
        this.lastKnownScanningStarted = mostRecentRecord;
        if (mostRecentRecord == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastKnownScanningStarted");
        }
        mostRecentRecord.observe(getViewLifecycleOwner(), new HomeFragment$onViewCreated$1(this));
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.tv_app_version);
        if (appCompatTextView != null) {
            appCompatTextView.setText("App Version: 1.0.1");
        }
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.prem_info_button);
        if (imageView != null) {
            imageView.setOnClickListener(new HomeFragment$onViewCreated$2(this));
        }
        showSetup();
        Preference preference = Preference.INSTANCE;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
        Context applicationContext = activity.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "activity!!.applicationContext");
        preference.registerListener(applicationContext, this.listener);
        showNonEmptyAnnouncement();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Intrinsics.checkParameterIsNotNull(layoutInflater, "inflater");
        View inflate = layoutInflater.inflate(R.layout.fragment_home, viewGroup, false);
        Preference preference = Preference.INSTANCE;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
        Context applicationContext = activity.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "activity!!.applicationContext");
        preference.registerListener(applicationContext, this.listener);
        return inflate;
    }

    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        CardView cardView = (CardView) _$_findCachedViewById(R.id.share_card_view);
        if (cardView != null) {
            cardView.setOnClickListener(new HomeFragment$onActivityCreated$1(this));
        }
        LottieAnimationView lottieAnimationView = (LottieAnimationView) _$_findCachedViewById(R.id.animation_view);
        if (lottieAnimationView != null) {
            lottieAnimationView.setOnClickListener(new HomeFragment$onActivityCreated$2(this));
        }
        Button button = (Button) _$_findCachedViewById(R.id.btn_restart_app_setup);
        if (button != null) {
            button.setOnClickListener(new HomeFragment$onActivityCreated$3(this));
        }
        ImageButton imageButton = (ImageButton) _$_findCachedViewById(R.id.btn_announcement_close);
        if (imageButton != null) {
            imageButton.setOnClickListener(new HomeFragment$onActivityCreated$4(this));
        }
    }

    private final boolean isShowRestartSetup() {
        String str = "iv_location";
        String str2 = "iv_bluetooth";
        if (canRequestBatteryOptimizerExemption()) {
            ImageView imageView = (ImageView) _$_findCachedViewById(R.id.iv_bluetooth);
            Intrinsics.checkExpressionValueIsNotNull(imageView, str2);
            if (imageView.isSelected()) {
                ImageView imageView2 = (ImageView) _$_findCachedViewById(R.id.iv_location);
                Intrinsics.checkExpressionValueIsNotNull(imageView2, str);
                if (imageView2.isSelected()) {
                    return false;
                }
            }
        } else {
            ImageView imageView3 = (ImageView) _$_findCachedViewById(R.id.iv_bluetooth);
            Intrinsics.checkExpressionValueIsNotNull(imageView3, str2);
            if (imageView3.isSelected()) {
                ImageView imageView4 = (ImageView) _$_findCachedViewById(R.id.iv_location);
                Intrinsics.checkExpressionValueIsNotNull(imageView4, str);
                if (imageView4.isSelected()) {
                    return false;
                }
            }
        }
        return true;
    }

    private final boolean canRequestBatteryOptimizerExemption() {
        if (VERSION.SDK_INT >= 23) {
            Utils utils = Utils.INSTANCE;
            Utils utils2 = Utils.INSTANCE;
            String packageName = TracerApp.Companion.getAppContext().getPackageName();
            Intrinsics.checkExpressionValueIsNotNull(packageName, "TracerApp.AppContext.packageName");
            if (utils.canHandleIntent(utils2.getBatteryOptimizerExemptionIntent(packageName), TracerApp.Companion.getAppContext().getPackageManager())) {
                return true;
            }
        }
        return false;
    }

    public final void showSetup() {
        updatedPremLabels();
        LinearLayout linearLayout = (LinearLayout) _$_findCachedViewById(R.id.view_complete);
        if (linearLayout != null) {
            ViewKt.setVisible(linearLayout, true);
        }
    }

    public void onResume() {
        super.onResume();
        if (!this.mIsBroadcastListenerRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            FragmentActivity activity = getActivity();
            if (activity == null) {
                Intrinsics.throwNpe();
            }
            activity.registerReceiver(this.mBroadcastListener, intentFilter);
            this.mIsBroadcastListenerRegistered = true;
        }
        if (getView() != null) {
            String[] requiredPermissions = Utils.INSTANCE.getRequiredPermissions();
            ImageView imageView = (ImageView) _$_findCachedViewById(R.id.iv_location);
            Intrinsics.checkExpressionValueIsNotNull(imageView, "iv_location");
            FragmentActivity activity2 = getActivity();
            String str = "null cannot be cast to non-null type ca.albertahealthservices.contacttracing.MainActivity";
            if (activity2 != null) {
                imageView.setSelected(EasyPermissions.hasPermissions((MainActivity) activity2, (String[]) Arrays.copyOf(requiredPermissions, requiredPermissions.length)));
                ImageView imageView2 = (ImageView) _$_findCachedViewById(R.id.iv_push);
                Intrinsics.checkExpressionValueIsNotNull(imageView2, "iv_push");
                FragmentActivity activity3 = getActivity();
                if (activity3 != null) {
                    imageView2.setSelected(NotificationManagerCompat.from((MainActivity) activity3).areNotificationsEnabled());
                    BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
                    if (bluetoothAdapter != null) {
                        ImageView imageView3 = (ImageView) _$_findCachedViewById(R.id.iv_bluetooth);
                        Intrinsics.checkExpressionValueIsNotNull(imageView3, "iv_bluetooth");
                        imageView3.setSelected(!isDisabled(bluetoothAdapter));
                    }
                    FragmentActivity activity4 = getActivity();
                    if (activity4 != null) {
                        Object systemService = ((MainActivity) activity4).getSystemService("power");
                        if (systemService != null) {
                            PowerManager powerManager = (PowerManager) systemService;
                            FragmentActivity activity5 = getActivity();
                            if (activity5 != null) {
                                String packageName = ((MainActivity) activity5).getPackageName();
                                if (VERSION.SDK_INT >= 23) {
                                    if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                                        CentralLog.Companion.d(this.TAG, "Not on Battery Optimization whitelist");
                                    } else {
                                        CentralLog.Companion.d(this.TAG, "On Battery Optimization whitelist");
                                    }
                                }
                                showSetup();
                                return;
                            }
                            throw new TypeCastException(str);
                        }
                        throw new TypeCastException("null cannot be cast to non-null type android.os.PowerManager");
                    }
                    throw new TypeCastException(str);
                }
                throw new TypeCastException(str);
            }
            throw new TypeCastException(str);
        }
    }

    public void onPause() {
        super.onPause();
        if (this.mIsBroadcastListenerRegistered) {
            FragmentActivity activity = getActivity();
            if (activity == null) {
                Intrinsics.throwNpe();
            }
            activity.unregisterReceiver(this.mBroadcastListener);
            this.mIsBroadcastListenerRegistered = false;
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        Preference preference = Preference.INSTANCE;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
        Context applicationContext = activity.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "activity!!.applicationContext");
        preference.unregisterListener(applicationContext, this.listener);
        LiveData<StatusRecord> liveData = this.lastKnownScanningStarted;
        if (liveData == null) {
            Intrinsics.throwUninitializedPropertyAccessException("lastKnownScanningStarted");
        }
        liveData.removeObservers(getViewLifecycleOwner());
        _$_clearFindViewByIdCache();
    }

    /* access modifiers changed from: private */
    public final void shareThisApp() {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name));
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.share_message));
        sb.append(BuildConfig.SHARE_URL);
        intent.putExtra("android.intent.extra.TEXT", sb.toString());
        startActivity(Intent.createChooser(intent, "Choose one"));
    }

    private final boolean isDisabled(BluetoothAdapter bluetoothAdapter) {
        return !bluetoothAdapter.isEnabled();
    }

    private final void enableBluetooth() {
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter != null && isDisabled(bluetoothAdapter)) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 123);
        }
    }

    @AfterPermissionGranted(456)
    public final void setupPermissionsAndSettings() {
        if (VERSION.SDK_INT >= 23) {
            String[] requiredPermissions = Utils.INSTANCE.getRequiredPermissions();
            FragmentActivity activity = getActivity();
            if (activity == null) {
                throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.MainActivity");
            } else if (!EasyPermissions.hasPermissions((MainActivity) activity, (String[]) Arrays.copyOf(requiredPermissions, requiredPermissions.length))) {
                EasyPermissions.requestPermissions((Fragment) this, getString(R.string.permission_location_rationale), 456, (String[]) Arrays.copyOf(requiredPermissions, requiredPermissions.length));
            }
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 123) {
            ImageView imageView = (ImageView) _$_findCachedViewById(R.id.iv_bluetooth);
            Intrinsics.checkExpressionValueIsNotNull(imageView, "iv_bluetooth");
            imageView.setSelected(i2 == -1);
        }
        showSetup();
        super.onActivityResult(i, i2, intent);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "permissions");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        super.onRequestPermissionsResult(i, strArr, iArr);
        CentralLog.Companion companion = CentralLog.Companion;
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("[onRequestPermissionsResult]requestCode ");
        sb.append(i);
        companion.d(str, sb.toString());
        if (i == 456) {
            ImageView imageView = (ImageView) _$_findCachedViewById(R.id.iv_location);
            Intrinsics.checkExpressionValueIsNotNull(imageView, "iv_location");
            imageView.setSelected(!(strArr.length == 0));
        }
        showSetup();
    }

    /* access modifiers changed from: private */
    public final void clearAndHideAnnouncement() {
        ConstraintLayout constraintLayout = (ConstraintLayout) _$_findCachedViewById(R.id.view_announcement);
        Intrinsics.checkExpressionValueIsNotNull(constraintLayout, "view_announcement");
        constraintLayout.setVisibility(8);
        Preference preference = Preference.INSTANCE;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
        Context applicationContext = activity.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "activity!!.applicationContext");
        preference.putAnnouncement(applicationContext, "");
    }

    /* access modifiers changed from: private */
    public final void showNonEmptyAnnouncement() {
        Preference preference = Preference.INSTANCE;
        FragmentActivity activity = getActivity();
        if (activity == null) {
            Intrinsics.throwNpe();
        }
        Intrinsics.checkExpressionValueIsNotNull(activity, "activity!!");
        Context applicationContext = activity.getApplicationContext();
        Intrinsics.checkExpressionValueIsNotNull(applicationContext, "activity!!.applicationContext");
        String announcement = preference.getAnnouncement(applicationContext);
        if (!(announcement.length() == 0)) {
            CentralLog.Companion companion = CentralLog.Companion;
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("FCM Announcement Changed to ");
            sb.append(announcement);
            sb.append('!');
            companion.d(str, sb.toString());
            AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.tv_announcement);
            String str2 = "tv_announcement";
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, str2);
            appCompatTextView.setText(HtmlCompat.fromHtml(announcement, 63));
            AppCompatTextView appCompatTextView2 = (AppCompatTextView) _$_findCachedViewById(R.id.tv_announcement);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, str2);
            appCompatTextView2.setMovementMethod(new HomeFragment$showNonEmptyAnnouncement$1(this));
            ConstraintLayout constraintLayout = (ConstraintLayout) _$_findCachedViewById(R.id.view_announcement);
            Intrinsics.checkExpressionValueIsNotNull(constraintLayout, "view_announcement");
            constraintLayout.setVisibility(0);
        }
    }

    private final void updatedPremLabels() {
        ImageView imageView = (ImageView) _$_findCachedViewById(R.id.iv_location);
        Intrinsics.checkExpressionValueIsNotNull(imageView, "iv_location");
        String str = "Yes";
        String str2 = "No";
        String str3 = imageView.isSelected() ? str : str2;
        AppCompatTextView appCompatTextView = (AppCompatTextView) _$_findCachedViewById(R.id.tv_location);
        if (appCompatTextView != null) {
            appCompatTextView.setText(HtmlCompat.fromHtml(getString(R.string.location_on, str3), 0));
        }
        ImageView imageView2 = (ImageView) _$_findCachedViewById(R.id.iv_bluetooth);
        Intrinsics.checkExpressionValueIsNotNull(imageView2, "iv_bluetooth");
        String str4 = imageView2.isSelected() ? str : str2;
        AppCompatTextView appCompatTextView2 = (AppCompatTextView) _$_findCachedViewById(R.id.tv_bluetooth);
        if (appCompatTextView2 != null) {
            appCompatTextView2.setText(HtmlCompat.fromHtml(getString(R.string.bluetooth_on, str4), 0));
        }
        ImageView imageView3 = (ImageView) _$_findCachedViewById(R.id.iv_push);
        Intrinsics.checkExpressionValueIsNotNull(imageView3, "iv_push");
        if (!imageView3.isSelected()) {
            str = str2;
        }
        AppCompatTextView appCompatTextView3 = (AppCompatTextView) _$_findCachedViewById(R.id.tv_push);
        if (appCompatTextView3 != null) {
            appCompatTextView3.setText(HtmlCompat.fromHtml(getString(R.string.push_noti, str), 0));
        }
    }

    /* access modifiers changed from: private */
    public final void alertDialog(String str) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            Builder builder = new Builder((MainActivity) activity);
            builder.setMessage(str).setCancelable(false).setPositiveButton(getString(R.string.ok), HomeFragment$alertDialog$1.INSTANCE);
            builder.create().show();
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.MainActivity");
    }
}

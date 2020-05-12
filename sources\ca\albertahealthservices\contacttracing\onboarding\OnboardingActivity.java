package ca.albertahealthservices.contacttracing.onboarding;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import ca.albertahealthservices.contacttracing.Preference;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.api.auth.SmsCodeChallengeHandler;
import ca.albertahealthservices.contacttracing.idmanager.TempIDManager;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.logging.CentralLog.Companion;
import ca.albertahealthservices.contacttracing.onboarding.SetupFragment.OnFragmentInteractionListener;
import ca.albertahealthservices.contacttracing.services.BluetoothMonitoringService;
import ca.albertahealthservices.contacttracing.view.CustomViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import kotlin.Lazy;
import kotlin.LazyKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.PropertyReference1Impl;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KProperty;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.Job;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\u0002\n\u0002\b\n\n\u0002\u0010\b\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0000\n\u0002\u0010\u0015\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u000f\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u00052\u00020\u00062\u00020\u0007:\u0001QB\u0005¢\u0006\u0002\u0010\bJ\u0012\u0010\u001e\u001a\u00020\u001f2\b\u0010 \u001a\u0004\u0018\u00010\nH\u0002J\u0006\u0010!\u001a\u00020\u001fJ\b\u0010\"\u001a\u00020\u001fH\u0002J\u0006\u0010#\u001a\u00020\u001fJ\b\u0010$\u001a\u00020\u001fH\u0002J\b\u0010%\u001a\u00020\u001fH\u0002J\b\u0010&\u001a\u00020\u001fH\u0002J\b\u0010'\u001a\u00020\u001fH\u0002J\u0010\u0010(\u001a\u00020\u001f2\u0006\u0010)\u001a\u00020*H\u0002J\u0006\u0010+\u001a\u00020\u001fJ\u0006\u0010,\u001a\u00020\u001fJ\"\u0010-\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020*2\u0006\u0010/\u001a\u00020*2\b\u00100\u001a\u0004\u0018\u000101H\u0014J\b\u00102\u001a\u00020\u001fH\u0016J\u0012\u00103\u001a\u00020\u001f2\b\u00104\u001a\u0004\u0018\u000105H\u0014J\b\u00106\u001a\u00020\u001fH\u0014J\u0010\u00107\u001a\u00020\u001f2\u0006\u00108\u001a\u000209H\u0016J-\u0010:\u001a\u00020\u001f2\u0006\u0010.\u001a\u00020*2\u000e\u0010;\u001a\n\u0012\u0006\b\u0001\u0012\u00020\n0<2\u0006\u0010=\u001a\u00020>H\u0016¢\u0006\u0002\u0010?J\b\u0010@\u001a\u00020\u001fH\u0014J\b\u0010A\u001a\u00020\u001fH\u0002J\u0018\u0010B\u001a\u00020C2\u0006\u0010D\u001a\u00020\n2\b\b\u0002\u0010E\u001a\u00020\fJ\b\u0010F\u001a\u00020\u001fH\u0002J\u0010\u0010G\u001a\u00020\u001f2\b\b\u0002\u0010H\u001a\u00020\fJ\b\u0010I\u001a\u00020\u001fH\u0007J\u0010\u0010J\u001a\u00020\u001f2\u0006\u0010K\u001a\u00020\nH\u0002J\u000e\u0010L\u001a\u00020\u001f2\u0006\u0010M\u001a\u00020\nJ\u000e\u0010N\u001a\u00020\u001f2\u0006\u0010K\u001a\u00020\nJ\u000e\u0010O\u001a\u00020\u001f2\u0006\u0010P\u001a\u00020\nR\u000e\u0010\t\u001a\u00020\nX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u001d\u0010\r\u001a\u0004\u0018\u00010\u000e8BX\u0002¢\u0006\f\n\u0004\b\u0011\u0010\u0012\u001a\u0004\b\u000f\u0010\u0010R\u0012\u0010\u0013\u001a\u00020\u0014X\u0005¢\u0006\u0006\u001a\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0017\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\u0019\u001a\b\u0018\u00010\u001aR\u00020\u0000X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\u001b\u001a\u00020\fX\u000e¢\u0006\u0002\n\u0000R\u0018\u0010\u001c\u001a\u00020\f*\u00020\u000e8BX\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001d¨\u0006R"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/OnboardingActivity;", "Landroidx/fragment/app/FragmentActivity;", "Lkotlinx/coroutines/CoroutineScope;", "Lca/albertahealthservices/contacttracing/onboarding/SetupFragment$OnFragmentInteractionListener;", "Lca/albertahealthservices/contacttracing/onboarding/SetupCompleteFragment$OnFragmentInteractionListener;", "Lca/albertahealthservices/contacttracing/onboarding/RegisterNumberFragment$OnFragmentInteractionListener;", "Lca/albertahealthservices/contacttracing/onboarding/OTPFragment$OnFragmentInteractionListener;", "Lca/albertahealthservices/contacttracing/onboarding/TOUFragment$OnFragmentInteractionListener;", "()V", "TAG", "", "bleSupported", "", "bluetoothAdapter", "Landroid/bluetooth/BluetoothAdapter;", "getBluetoothAdapter", "()Landroid/bluetooth/BluetoothAdapter;", "bluetoothAdapter$delegate", "Lkotlin/Lazy;", "coroutineContext", "Lkotlin/coroutines/CoroutineContext;", "getCoroutineContext", "()Lkotlin/coroutines/CoroutineContext;", "mIsOpenSetting", "mIsResetup", "pagerAdapter", "Lca/albertahealthservices/contacttracing/onboarding/OnboardingActivity$ScreenSlidePagerAdapter;", "resendingCode", "isDisabled", "(Landroid/bluetooth/BluetoothAdapter;)Z", "alertDialog", "", "desc", "cancelChallengeAndGoBack", "checkBLESupport", "enableBluetooth", "enableFragmentbutton", "excludeFromBatteryOptimization", "getTemporaryID", "initBluetooth", "navigateTo", "page", "", "navigateToNextPage", "navigateToPreviousPage", "onActivityResult", "requestCode", "resultCode", "data", "Landroid/content/Intent;", "onBackPressed", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onDestroy", "onFragmentInteraction", "uri", "Landroid/net/Uri;", "onRequestPermissionsResult", "permissions", "", "grantResults", "", "(I[Ljava/lang/String;[I)V", "onResume", "registerForSmsCodeChallengeBroadcasts", "requestForOTP", "Lkotlinx/coroutines/Job;", "phoneNumber", "skipRegister", "requestTempIdsIfNeeded", "resendCode", "skipCancelChallenge", "setupPermissionsAndSettings", "updateOTPError", "error", "updatePhoneNumber", "num", "updatePhoneNumberError", "validateOTP", "otp", "ScreenSlidePagerAdapter", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: OnboardingActivity.kt */
public final class OnboardingActivity extends FragmentActivity implements CoroutineScope, OnFragmentInteractionListener, SetupCompleteFragment.OnFragmentInteractionListener, RegisterNumberFragment.OnFragmentInteractionListener, OTPFragment.OnFragmentInteractionListener, TOUFragment.OnFragmentInteractionListener {
    static final /* synthetic */ KProperty[] $$delegatedProperties = {Reflection.property1(new PropertyReference1Impl(Reflection.getOrCreateKotlinClass(OnboardingActivity.class), "bluetoothAdapter", "getBluetoothAdapter()Landroid/bluetooth/BluetoothAdapter;"))};
    private final /* synthetic */ CoroutineScope $$delegate_0 = CoroutineScopeKt.MainScope();
    /* access modifiers changed from: private */
    public String TAG = "OnboardingActivity";
    private HashMap _$_findViewCache;
    private boolean bleSupported;
    private final Lazy bluetoothAdapter$delegate = LazyKt.lazy(LazyThreadSafetyMode.NONE, (Function0) new OnboardingActivity$bluetoothAdapter$2(this));
    /* access modifiers changed from: private */
    public boolean mIsOpenSetting;
    private boolean mIsResetup;
    /* access modifiers changed from: private */
    public ScreenSlidePagerAdapter pagerAdapter;
    /* access modifiers changed from: private */
    public boolean resendingCode;

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\u0010\b\n\u0002\u0018\u0002\n\u0002\b\b\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004J\u0010\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\u0007H\u0002J\b\u0010\r\u001a\u00020\u0007H\u0016J\u0010\u0010\u000e\u001a\u00020\b2\u0006\u0010\u000f\u001a\u00020\u0007H\u0016R\u001d\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\b0\u0006¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n¨\u0006\u0010"}, d2 = {"Lca/albertahealthservices/contacttracing/onboarding/OnboardingActivity$ScreenSlidePagerAdapter;", "Landroidx/fragment/app/FragmentStatePagerAdapter;", "fm", "Landroidx/fragment/app/FragmentManager;", "(Lca/albertahealthservices/contacttracing/onboarding/OnboardingActivity;Landroidx/fragment/app/FragmentManager;)V", "fragmentMap", "", "", "Lca/albertahealthservices/contacttracing/onboarding/OnboardingFragmentInterface;", "getFragmentMap", "()Ljava/util/Map;", "createFragAtIndex", "index", "getCount", "getItem", "position", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: OnboardingActivity.kt */
    private final class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        private final Map<Integer, OnboardingFragmentInterface> fragmentMap = new HashMap();
        final /* synthetic */ OnboardingActivity this$0;

        public int getCount() {
            return 5;
        }

        public ScreenSlidePagerAdapter(OnboardingActivity onboardingActivity, FragmentManager fragmentManager) {
            Intrinsics.checkParameterIsNotNull(fragmentManager, "fm");
            this.this$0 = onboardingActivity;
            super(fragmentManager);
        }

        public final Map<Integer, OnboardingFragmentInterface> getFragmentMap() {
            return this.fragmentMap;
        }

        public OnboardingFragmentInterface getItem(int i) {
            Map<Integer, OnboardingFragmentInterface> map = this.fragmentMap;
            Integer valueOf = Integer.valueOf(i);
            Object obj = map.get(valueOf);
            if (obj == null) {
                obj = createFragAtIndex(i);
                map.put(valueOf, obj);
            }
            return (OnboardingFragmentInterface) obj;
        }

        private final OnboardingFragmentInterface createFragAtIndex(int i) {
            if (i == 0) {
                return new TOUFragment();
            }
            if (i == 1) {
                return new RegisterNumberFragment();
            }
            if (i == 2) {
                return new OTPFragment();
            }
            if (i == 3) {
                return new SetupFragment();
            }
            if (i != 4) {
                return new TOUFragment();
            }
            return new SetupCompleteFragment();
        }
    }

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
        if (view != null) {
            return view;
        }
        View findViewById = findViewById(i);
        this._$_findViewCache.put(Integer.valueOf(i), findViewById);
        return findViewById;
    }

    public CoroutineContext getCoroutineContext() {
        return this.$$delegate_0.getCoroutineContext();
    }

    /* access modifiers changed from: private */
    public final void enableFragmentbutton() {
        OnboardingFragmentInterface onboardingFragmentInterface;
        ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
        if (screenSlidePagerAdapter != null) {
            CustomViewPager customViewPager = (CustomViewPager) _$_findCachedViewById(R.id.pager);
            Intrinsics.checkExpressionValueIsNotNull(customViewPager, "pager");
            onboardingFragmentInterface = screenSlidePagerAdapter.getItem(customViewPager.getCurrentItem());
        } else {
            onboardingFragmentInterface = null;
        }
        if (onboardingFragmentInterface != null) {
            onboardingFragmentInterface.enableButton();
        }
    }

    private final void alertDialog(String str) {
        Builder builder = new Builder(this);
        builder.setMessage(str).setCancelable(false).setPositiveButton(getString(R.string.ok), OnboardingActivity$alertDialog$1.INSTANCE);
        builder.create().show();
    }

    /* access modifiers changed from: private */
    public final void requestTempIdsIfNeeded() {
        CentralLog.Companion.d(this.TAG, "requestTempIdsIfNeeded");
        if (BluetoothMonitoringService.Companion.getBroadcastMessage() != null) {
            TempIDManager tempIDManager = TempIDManager.INSTANCE;
            Context applicationContext = getApplicationContext();
            Intrinsics.checkExpressionValueIsNotNull(applicationContext, "applicationContext");
            if (!tempIDManager.needToUpdate(applicationContext)) {
                return;
            }
        }
        getTemporaryID();
    }

    private final void getTemporaryID() {
        TempIDManager.INSTANCE.getTemporaryIDs(this);
        CentralLog.Companion.d(this.TAG, "Retrieved Temporary ID successfully");
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_onboarding);
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        Intrinsics.checkExpressionValueIsNotNull(supportFragmentManager, "supportFragmentManager");
        this.pagerAdapter = new ScreenSlidePagerAdapter(this, supportFragmentManager);
        CustomViewPager customViewPager = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        String str = "pager";
        Intrinsics.checkExpressionValueIsNotNull(customViewPager, str);
        customViewPager.setAdapter(this.pagerAdapter);
        ((TabLayout) _$_findCachedViewById(R.id.tabDots)).setupWithViewPager((CustomViewPager) _$_findCachedViewById(R.id.pager), true);
        ((CustomViewPager) _$_findCachedViewById(R.id.pager)).addOnPageChangeListener(new OnboardingActivity$onCreate$1(this));
        ((CustomViewPager) _$_findCachedViewById(R.id.pager)).setPagingEnabled(false);
        CustomViewPager customViewPager2 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        Intrinsics.checkExpressionValueIsNotNull(customViewPager2, str);
        customViewPager2.setOffscreenPageLimit(5);
        Intent intent = getIntent();
        Intrinsics.checkExpressionValueIsNotNull(intent, "intent");
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.mIsResetup = true;
            navigateTo(extras.getInt("page", 0));
        } else {
            navigateTo(Preference.INSTANCE.getCheckpoint(this));
        }
        registerForSmsCodeChallengeBroadcasts();
    }

    private final void registerForSmsCodeChallengeBroadcasts() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this);
        Intrinsics.checkExpressionValueIsNotNull(instance, "LocalBroadcastManager.getInstance(this)");
        instance.registerReceiver(new OnboardingActivity$registerForSmsCodeChallengeBroadcasts$1(this), new IntentFilter(SmsCodeChallengeHandler.ACTION_VERIFY_SMS_CODE_REQUIRED));
        instance.registerReceiver(new OnboardingActivity$registerForSmsCodeChallengeBroadcasts$2(this), new IntentFilter(SmsCodeChallengeHandler.ACTION_VERIFY_SMS_CODE_SUCCESS));
        instance.registerReceiver(new OnboardingActivity$registerForSmsCodeChallengeBroadcasts$3(this), new IntentFilter(SmsCodeChallengeHandler.ACTION_VERIFY_SMS_CODE_FAIL));
        instance.registerReceiver(new OnboardingActivity$registerForSmsCodeChallengeBroadcasts$4(this), new IntentFilter(SmsCodeChallengeHandler.ACTION_CHALLENGE_CANCELLED));
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.mIsOpenSetting) {
            new Handler().postDelayed(new OnboardingActivity$onResume$1(this), 1000);
        }
    }

    public void onBackPressed() {
        CustomViewPager customViewPager = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        String str = "pager";
        Intrinsics.checkExpressionValueIsNotNull(customViewPager, str);
        if (customViewPager.getCurrentItem() > 1) {
            CustomViewPager customViewPager2 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
            Intrinsics.checkExpressionValueIsNotNull(customViewPager2, str);
            if (customViewPager2.getCurrentItem() != 0) {
                CustomViewPager customViewPager3 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
                Intrinsics.checkExpressionValueIsNotNull(customViewPager3, str);
                if (customViewPager3.getCurrentItem() == 2) {
                    cancelChallengeAndGoBack();
                } else {
                    navigateToPreviousPage();
                }
                return;
            }
        }
        super.onBackPressed();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        CoroutineScopeKt.cancel$default(this, null, 1, null);
        super.onDestroy();
    }

    public final void cancelChallengeAndGoBack() {
        Intent intent = new Intent();
        intent.setAction(SmsCodeChallengeHandler.ACTION_CANCEL_CHALLENGE);
        intent.putExtra(SmsCodeChallengeHandler.EXTRA_SKIP_CANCELLED_BROADCAST, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        navigateToPreviousPage();
    }

    private final boolean isDisabled(BluetoothAdapter bluetoothAdapter) {
        return !bluetoothAdapter.isEnabled();
    }

    public final void enableBluetooth() {
        CentralLog.Companion.d(this.TAG, "[enableBluetooth]");
        BluetoothAdapter bluetoothAdapter = getBluetoothAdapter();
        if (bluetoothAdapter == null) {
            return;
        }
        if (isDisabled(bluetoothAdapter)) {
            startActivityForResult(new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE"), 123);
        } else {
            setupPermissionsAndSettings();
        }
    }

    @AfterPermissionGranted(456)
    public final void setupPermissionsAndSettings() {
        CentralLog.Companion.d(this.TAG, "[setupPermissionsAndSettings]");
        if (VERSION.SDK_INT >= 23) {
            String[] requiredPermissions = Utils.INSTANCE.getRequiredPermissions();
            if (EasyPermissions.hasPermissions(this, (String[]) Arrays.copyOf(requiredPermissions, requiredPermissions.length))) {
                initBluetooth();
                excludeFromBatteryOptimization();
                return;
            }
            EasyPermissions.requestPermissions((Activity) this, getString(R.string.permission_location_rationale), 456, (String[]) Arrays.copyOf(requiredPermissions, requiredPermissions.length));
            return;
        }
        initBluetooth();
        navigateToNextPage();
    }

    private final void initBluetooth() {
        checkBLESupport();
    }

    private final void checkBLESupport() {
        CentralLog.Companion.d(this.TAG, "[checkBLESupport] ");
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        Intrinsics.checkExpressionValueIsNotNull(defaultAdapter, "BluetoothAdapter.getDefaultAdapter()");
        if (!defaultAdapter.isMultipleAdvertisementSupported()) {
            this.bleSupported = false;
            Utils.INSTANCE.stopBluetoothMonitoringService(this);
            return;
        }
        this.bleSupported = true;
    }

    private final void excludeFromBatteryOptimization() {
        CentralLog.Companion.d(this.TAG, "[excludeFromBatteryOptimization] ");
        Object systemService = getSystemService("power");
        if (systemService != null) {
            PowerManager powerManager = (PowerManager) systemService;
            String packageName = getPackageName();
            if (VERSION.SDK_INT >= 23) {
                Utils utils = Utils.INSTANCE;
                Intrinsics.checkExpressionValueIsNotNull(packageName, "packageName");
                Intent batteryOptimizerExemptionIntent = utils.getBatteryOptimizerExemptionIntent(packageName);
                if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
                    CentralLog.Companion.d(this.TAG, "Not on Battery Optimization whitelist");
                    if (Utils.INSTANCE.canHandleIntent(batteryOptimizerExemptionIntent, getPackageManager())) {
                        startActivityForResult(batteryOptimizerExemptionIntent, 789);
                    } else {
                        navigateToNextPage();
                    }
                } else {
                    CentralLog.Companion.d(this.TAG, "On Battery Optimization whitelist");
                    navigateToNextPage();
                }
            }
        } else {
            throw new TypeCastException("null cannot be cast to non-null type android.os.PowerManager");
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        Companion companion = CentralLog.Companion;
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("requestCode ");
        sb.append(i);
        sb.append(" resultCode ");
        sb.append(i2);
        companion.d(str, sb.toString());
        if (i == 123) {
            if (i2 == 0) {
                finish();
                return;
            }
            setupPermissionsAndSettings();
        } else if (i == 789 && i2 != 0) {
            new Handler().postDelayed(new OnboardingActivity$onActivityResult$1(this), 1000);
        }
        super.onActivityResult(i, i2, intent);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        Intrinsics.checkParameterIsNotNull(strArr, "permissions");
        Intrinsics.checkParameterIsNotNull(iArr, "grantResults");
        super.onRequestPermissionsResult(i, strArr, iArr);
        Companion companion = CentralLog.Companion;
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("[onRequestPermissionsResult] requestCode ");
        sb.append(i);
        companion.d(str, sb.toString());
        if (i == 456) {
            int length = strArr.length;
            for (int i2 = 0; i2 < length; i2++) {
                String str2 = strArr[i2];
                if (iArr[i2] == -1) {
                    if (VERSION.SDK_INT >= 23) {
                        if (!shouldShowRequestPermissionRationale(str2)) {
                            Builder builder = new Builder(this);
                            builder.setMessage(getString(R.string.open_location_setting)).setCancelable(false).setPositiveButton(getString(R.string.ok), new OnboardingActivity$onRequestPermissionsResult$1(this)).setNegativeButton(getString(R.string.cancel), OnboardingActivity$onRequestPermissionsResult$2.INSTANCE);
                            builder.create().show();
                        } else if ("android.permission.WRITE_CONTACTS".equals(str2)) {
                            CentralLog.Companion.d(this.TAG, "user did not CHECKED never ask again");
                        } else {
                            excludeFromBatteryOptimization();
                        }
                    }
                } else if (iArr[i2] == 0) {
                    excludeFromBatteryOptimization();
                }
            }
        }
    }

    public final void navigateToNextPage() {
        CentralLog.Companion.d(this.TAG, "Navigating to next page");
        CustomViewPager customViewPager = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        String str = "pager";
        Intrinsics.checkExpressionValueIsNotNull(customViewPager, str);
        CustomViewPager customViewPager2 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        Intrinsics.checkExpressionValueIsNotNull(customViewPager2, str);
        customViewPager.setCurrentItem(customViewPager2.getCurrentItem() + 1);
        ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
        if (screenSlidePagerAdapter == null) {
            Intrinsics.throwNpe();
        }
        screenSlidePagerAdapter.notifyDataSetChanged();
    }

    public final void navigateToPreviousPage() {
        CentralLog.Companion.d(this.TAG, "Navigating to previous page");
        String str = "pager";
        if (this.mIsResetup) {
            CustomViewPager customViewPager = (CustomViewPager) _$_findCachedViewById(R.id.pager);
            Intrinsics.checkExpressionValueIsNotNull(customViewPager, str);
            if (customViewPager.getCurrentItem() >= 4) {
                CustomViewPager customViewPager2 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
                Intrinsics.checkExpressionValueIsNotNull(customViewPager2, str);
                CustomViewPager customViewPager3 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
                Intrinsics.checkExpressionValueIsNotNull(customViewPager3, str);
                customViewPager2.setCurrentItem(customViewPager3.getCurrentItem() - 1);
                ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
                if (screenSlidePagerAdapter == null) {
                    Intrinsics.throwNpe();
                }
                screenSlidePagerAdapter.notifyDataSetChanged();
                return;
            }
            finish();
            return;
        }
        CustomViewPager customViewPager4 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        Intrinsics.checkExpressionValueIsNotNull(customViewPager4, str);
        CustomViewPager customViewPager5 = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        Intrinsics.checkExpressionValueIsNotNull(customViewPager5, str);
        customViewPager4.setCurrentItem(customViewPager5.getCurrentItem() - 1);
        ScreenSlidePagerAdapter screenSlidePagerAdapter2 = this.pagerAdapter;
        if (screenSlidePagerAdapter2 == null) {
            Intrinsics.throwNpe();
        }
        screenSlidePagerAdapter2.notifyDataSetChanged();
    }

    private final void navigateTo(int i) {
        CentralLog.Companion.d(this.TAG, "Navigating to page");
        CustomViewPager customViewPager = (CustomViewPager) _$_findCachedViewById(R.id.pager);
        Intrinsics.checkExpressionValueIsNotNull(customViewPager, "pager");
        customViewPager.setCurrentItem(i);
        ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
        if (screenSlidePagerAdapter == null) {
            Intrinsics.throwNpe();
        }
        screenSlidePagerAdapter.notifyDataSetChanged();
    }

    public static /* synthetic */ Job requestForOTP$default(OnboardingActivity onboardingActivity, String str, boolean z, int i, Object obj) {
        if ((i & 2) != 0) {
            z = false;
        }
        return onboardingActivity.requestForOTP(str, z);
    }

    public final Job requestForOTP(String str, boolean z) {
        Intrinsics.checkParameterIsNotNull(str, "phoneNumber");
        return BuildersKt__Builders_commonKt.launch$default(this, null, null, new OnboardingActivity$requestForOTP$1(this, z, str, null), 3, null);
    }

    public final void validateOTP(String str) {
        String str2 = "otp";
        Intrinsics.checkParameterIsNotNull(str, str2);
        if (TextUtils.isEmpty(str) || str.length() < 6) {
            String string = getString(R.string.must_be_six_digit);
            Intrinsics.checkExpressionValueIsNotNull(string, "getString(R.string.must_be_six_digit)");
            updateOTPError(string);
            return;
        }
        Intent intent = new Intent();
        intent.setAction(SmsCodeChallengeHandler.ACTION_VERIFY_SMS_CODE);
        intent.putExtra(str2, str);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    public static /* synthetic */ void resendCode$default(OnboardingActivity onboardingActivity, boolean z, int i, Object obj) {
        if ((i & 1) != 0) {
            z = false;
        }
        onboardingActivity.resendCode(z);
    }

    public final void resendCode(boolean z) {
        this.resendingCode = true;
        if (!z) {
            Intent intent = new Intent();
            intent.setAction(SmsCodeChallengeHandler.ACTION_CANCEL_CHALLENGE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return;
        }
        requestForOTP("", true);
    }

    public final void updatePhoneNumber(String str) {
        Intrinsics.checkParameterIsNotNull(str, "num");
        ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
        if (screenSlidePagerAdapter == null) {
            Intrinsics.throwNpe();
        }
        screenSlidePagerAdapter.getItem(2).onUpdatePhoneNumber(str);
    }

    public final void updatePhoneNumberError(String str) {
        Intrinsics.checkParameterIsNotNull(str, "error");
        ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
        if (screenSlidePagerAdapter == null) {
            Intrinsics.throwNpe();
        }
        screenSlidePagerAdapter.getItem(1).onError(str);
    }

    /* access modifiers changed from: private */
    public final void updateOTPError(String str) {
        ScreenSlidePagerAdapter screenSlidePagerAdapter = this.pagerAdapter;
        if (screenSlidePagerAdapter == null) {
            Intrinsics.throwNpe();
        }
        screenSlidePagerAdapter.getItem(2).onError(str);
        alertDialog(str);
    }

    public void onFragmentInteraction(Uri uri) {
        Intrinsics.checkParameterIsNotNull(uri, "uri");
        Companion companion = CentralLog.Companion;
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("########## fragment interaction: ");
        sb.append(uri);
        companion.d(str, sb.toString());
    }
}

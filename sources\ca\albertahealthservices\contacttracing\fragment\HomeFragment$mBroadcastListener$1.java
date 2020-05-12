package ca.albertahealthservices.contacttracing.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001d\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J\u0018\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u0007H\u0016¨\u0006\b"}, d2 = {"ca/albertahealthservices/contacttracing/fragment/HomeFragment$mBroadcastListener$1", "Landroid/content/BroadcastReceiver;", "onReceive", "", "context", "Landroid/content/Context;", "intent", "Landroid/content/Intent;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: HomeFragment.kt */
public final class HomeFragment$mBroadcastListener$1 extends BroadcastReceiver {
    final /* synthetic */ HomeFragment this$0;

    HomeFragment$mBroadcastListener$1(HomeFragment homeFragment) {
        this.this$0 = homeFragment;
    }

    public void onReceive(Context context, Intent intent) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        Intrinsics.checkParameterIsNotNull(intent, "intent");
        CentralLog.Companion.d(this.this$0.TAG, String.valueOf(intent.getAction()));
        if (Intrinsics.areEqual((Object) intent.getAction(), (Object) "android.bluetooth.adapter.action.STATE_CHANGED")) {
            int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
            String str = "iv_bluetooth";
            if (intExtra == 10) {
                ImageView imageView = (ImageView) this.this$0._$_findCachedViewById(R.id.iv_bluetooth);
                Intrinsics.checkExpressionValueIsNotNull(imageView, str);
                imageView.setSelected(false);
            } else if (intExtra == 13) {
                ImageView imageView2 = (ImageView) this.this$0._$_findCachedViewById(R.id.iv_bluetooth);
                Intrinsics.checkExpressionValueIsNotNull(imageView2, str);
                imageView2.setSelected(false);
            } else if (intExtra == 12) {
                ImageView imageView3 = (ImageView) this.this$0._$_findCachedViewById(R.id.iv_bluetooth);
                Intrinsics.checkExpressionValueIsNotNull(imageView3, str);
                imageView3.setSelected(true);
            }
            this.this$0.showSetup();
        }
    }
}

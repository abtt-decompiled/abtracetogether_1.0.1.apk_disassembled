package ca.albertahealthservices.contacttracing.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u00032\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u00010\u00060\u0006H\n¢\u0006\u0002\b\u0007"}, d2 = {"<anonymous>", "", "<anonymous parameter 0>", "Landroid/content/SharedPreferences;", "kotlin.jvm.PlatformType", "key", "", "onSharedPreferenceChanged"}, k = 3, mv = {1, 1, 16})
/* compiled from: HomeFragment.kt */
final class HomeFragment$listener$1 implements OnSharedPreferenceChangeListener {
    final /* synthetic */ HomeFragment this$0;

    HomeFragment$listener$1(HomeFragment homeFragment) {
        this.this$0 = homeFragment;
    }

    public final void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        if (str != null && str.hashCode() == -1820904121 && str.equals("ANNOUNCEMENT")) {
            this.this$0.showNonEmptyAnnouncement();
        }
    }
}

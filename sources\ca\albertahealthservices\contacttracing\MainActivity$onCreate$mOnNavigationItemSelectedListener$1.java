package ca.albertahealthservices.contacttracing;

import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import ca.albertahealthservices.contacttracing.fragment.ForUseByOTCFragment;
import ca.albertahealthservices.contacttracing.fragment.HelpFragment;
import ca.albertahealthservices.contacttracing.fragment.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "item", "Landroid/view/MenuItem;", "onNavigationItemSelected"}, k = 3, mv = {1, 1, 16})
/* compiled from: MainActivity.kt */
final class MainActivity$onCreate$mOnNavigationItemSelectedListener$1 implements OnNavigationItemSelectedListener {
    final /* synthetic */ MainActivity this$0;

    MainActivity$onCreate$mOnNavigationItemSelectedListener$1(MainActivity mainActivity) {
        this.this$0 = mainActivity;
    }

    public final boolean onNavigationItemSelected(MenuItem menuItem) {
        Intrinsics.checkParameterIsNotNull(menuItem, "item");
        switch (menuItem.getItemId()) {
            case R.id.navigation_help /*2131296486*/:
                if (this.this$0.selected != R.id.navigation_help) {
                    MainActivity mainActivity = this.this$0;
                    int layout_main_id = mainActivity.getLAYOUT_MAIN_ID();
                    Fragment helpFragment = new HelpFragment();
                    String name = HelpFragment.class.getName();
                    Intrinsics.checkExpressionValueIsNotNull(name, "HelpFragment::class.java.name");
                    mainActivity.openFragment(layout_main_id, helpFragment, name, 0);
                }
                this.this$0.selected = R.id.navigation_help;
                return true;
            case R.id.navigation_home /*2131296487*/:
                if (this.this$0.selected != R.id.navigation_home) {
                    MainActivity mainActivity2 = this.this$0;
                    int layout_main_id2 = mainActivity2.getLAYOUT_MAIN_ID();
                    Fragment homeFragment = new HomeFragment();
                    String name2 = HomeFragment.class.getName();
                    Intrinsics.checkExpressionValueIsNotNull(name2, "HomeFragment::class.java.name");
                    mainActivity2.openFragment(layout_main_id2, homeFragment, name2, 0);
                }
                this.this$0.selected = R.id.navigation_home;
                return true;
            case R.id.navigation_upload /*2131296488*/:
                if (this.this$0.selected != R.id.navigation_upload) {
                    MainActivity mainActivity3 = this.this$0;
                    int layout_main_id3 = mainActivity3.getLAYOUT_MAIN_ID();
                    Fragment forUseByOTCFragment = new ForUseByOTCFragment();
                    String name3 = ForUseByOTCFragment.class.getName();
                    Intrinsics.checkExpressionValueIsNotNull(name3, "ForUseByOTCFragment::class.java.name");
                    mainActivity3.openFragment(layout_main_id3, forUseByOTCFragment, name3, 0);
                }
                this.this$0.selected = R.id.navigation_upload;
                return true;
            default:
                return false;
        }
    }
}

package ca.albertahealthservices.contacttracing.fragment;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import ca.albertahealthservices.contacttracing.R;
import ca.albertahealthservices.contacttracing.Utils;
import ca.albertahealthservices.contacttracing.logging.CentralLog;
import ca.albertahealthservices.contacttracing.status.persistence.StatusRecord;
import com.airbnb.lottie.LottieAnimationView;
import kotlin.Metadata;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u000e\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\b\u0010\u0002\u001a\u0004\u0018\u00010\u0003H\n¢\u0006\u0002\b\u0004"}, d2 = {"<anonymous>", "", "record", "Lca/albertahealthservices/contacttracing/status/persistence/StatusRecord;", "onChanged"}, k = 3, mv = {1, 1, 16})
/* compiled from: HomeFragment.kt */
final class HomeFragment$onViewCreated$1<T> implements Observer<StatusRecord> {
    final /* synthetic */ HomeFragment this$0;

    HomeFragment$onViewCreated$1(HomeFragment homeFragment) {
        this.this$0 = homeFragment;
    }

    public final void onChanged(StatusRecord statusRecord) {
        if (statusRecord != null) {
            AppCompatTextView appCompatTextView = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.tv_last_update);
            if (appCompatTextView != null) {
                appCompatTextView.setVisibility(0);
            }
            AppCompatTextView appCompatTextView2 = (AppCompatTextView) this.this$0._$_findCachedViewById(R.id.tv_last_update);
            if (appCompatTextView2 != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Last updated: ");
                sb.append(Utils.INSTANCE.getTime(statusRecord.getTimestamp()));
                appCompatTextView2.setText(sb.toString());
            }
            long currentTimeMillis = System.currentTimeMillis();
            CentralLog.Companion.d(this.this$0.TAG, String.valueOf(statusRecord.getTimestamp()));
            CentralLog.Companion.d(this.this$0.TAG, String.valueOf(currentTimeMillis));
            if (currentTimeMillis - ((long) this.this$0.animationWindow) >= statusRecord.getTimestamp() && statusRecord.getTimestamp() <= currentTimeMillis + ((long) this.this$0.animationWindow)) {
                CentralLog.Companion.d(this.this$0.TAG, "Start animation");
                LottieAnimationView lottieAnimationView = (LottieAnimationView) this.this$0._$_findCachedViewById(R.id.animation_view);
                if (lottieAnimationView != null) {
                    lottieAnimationView.playAnimation();
                }
            }
        }
    }
}

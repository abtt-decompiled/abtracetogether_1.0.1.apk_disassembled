package ca.albertahealthservices.contacttracing.fragment;

import android.text.Layout;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.MotionEvent;
import android.widget.TextView;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J&\u0010\u0002\u001a\u00020\u00032\b\u0010\u0004\u001a\u0004\u0018\u00010\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00072\b\u0010\b\u001a\u0004\u0018\u00010\tH\u0016¨\u0006\n"}, d2 = {"ca/albertahealthservices/contacttracing/fragment/HomeFragment$showNonEmptyAnnouncement$1", "Landroid/text/method/LinkMovementMethod;", "onTouchEvent", "", "widget", "Landroid/widget/TextView;", "buffer", "Landroid/text/Spannable;", "event", "Landroid/view/MotionEvent;", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: HomeFragment.kt */
public final class HomeFragment$showNonEmptyAnnouncement$1 extends LinkMovementMethod {
    final /* synthetic */ HomeFragment this$0;

    HomeFragment$showNonEmptyAnnouncement$1(HomeFragment homeFragment) {
        this.this$0 = homeFragment;
    }

    public boolean onTouchEvent(TextView textView, Spannable spannable, MotionEvent motionEvent) {
        if (!(motionEvent == null || motionEvent.getAction() != 1 || textView == null || spannable == null)) {
            float x = (motionEvent.getX() - ((float) textView.getTotalPaddingLeft())) + ((float) textView.getScrollX());
            float y = (motionEvent.getY() - ((float) textView.getTotalPaddingTop())) + ((float) textView.getScrollY());
            Layout layout = textView.getLayout();
            int offsetForHorizontal = layout.getOffsetForHorizontal(layout.getLineForVertical((int) y), x);
            Object[] spans = spannable.getSpans(offsetForHorizontal, offsetForHorizontal, URLSpan.class);
            Intrinsics.checkExpressionValueIsNotNull(spans, "buffer.getSpans(off, off, URLSpan::class.java)");
            if (!(((URLSpan[]) spans).length == 0)) {
                this.this$0.clearAndHideAnnouncement();
            }
        }
        return super.onTouchEvent(textView, spannable, motionEvent);
    }
}

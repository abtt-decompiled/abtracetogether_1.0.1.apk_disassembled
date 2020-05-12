package ca.albertahealthservices.contacttracing;

import android.view.View;
import android.view.View.OnClickListener;
import ca.albertahealthservices.contacttracing.RecordListAdapter.MODE;
import ca.albertahealthservices.contacttracing.streetpass.view.StreetPassRecordViewModel;
import kotlin.Metadata;
import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "it", "Landroid/view/View;", "kotlin.jvm.PlatformType", "onClick"}, k = 3, mv = {1, 1, 16})
/* compiled from: RecordListAdapter.kt */
final class RecordListAdapter$onBindViewHolder$1 implements OnClickListener {
    final /* synthetic */ RecordListAdapter this$0;

    RecordListAdapter$onBindViewHolder$1(RecordListAdapter recordListAdapter) {
        this.this$0 = recordListAdapter;
    }

    public final void onClick(View view) {
        Intrinsics.checkExpressionValueIsNotNull(view, "it");
        Object tag = view.getTag();
        if (tag != null) {
            this.this$0.setMode(MODE.MODEL_P, (StreetPassRecordViewModel) tag);
            return;
        }
        throw new TypeCastException("null cannot be cast to non-null type ca.albertahealthservices.contacttracing.streetpass.view.StreetPassRecordViewModel");
    }
}

package ca.albertahealthservices.contacttracing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord;
import ca.albertahealthservices.contacttracing.streetpass.view.StreetPassRecordViewModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.NoWhenBranchMatchedException;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\n\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0002%&B\u000f\b\u0000\u0012\u0006\u0010\u0003\u001a\u00020\u0004¢\u0006\u0002\u0010\u0005J\u0018\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\u0010\u0010\u001a\u0004\u0018\u00010\fH\u0002J&\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\u0010\u0012\u001a\u0004\u0018\u00010\f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bH\u0002J&\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\b\u0010\u0012\u001a\u0004\u0018\u00010\f2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bH\u0002J\b\u0010\u0015\u001a\u00020\u0016H\u0016J\u001c\u0010\u0017\u001a\u00020\u00182\n\u0010\u0019\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001a\u001a\u00020\u0016H\u0016J\u001c\u0010\u001b\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u0016H\u0016J\u001c\u0010\u001f\u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bH\u0002J\u001c\u0010 \u001a\b\u0012\u0004\u0012\u00020\f0\u000b2\f\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bH\u0002J\u000e\u0010!\u001a\u00020\u00182\u0006\u0010\b\u001a\u00020\tJ\u001a\u0010!\u001a\u00020\u00182\u0006\u0010\b\u001a\u00020\t2\b\u0010\u0012\u001a\u0004\u0018\u00010\fH\u0002J\u0016\u0010\"\u001a\u00020\u00182\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bH\u0002J\u001b\u0010#\u001a\u00020\u00182\f\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bH\u0000¢\u0006\u0002\b$R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u000e¢\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000e0\u000bX\u000e¢\u0006\u0002\n\u0000¨\u0006'"}, d2 = {"Lca/albertahealthservices/contacttracing/RecordListAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lca/albertahealthservices/contacttracing/RecordListAdapter$RecordViewHolder;", "context", "Landroid/content/Context;", "(Landroid/content/Context;)V", "inflater", "Landroid/view/LayoutInflater;", "mode", "Lca/albertahealthservices/contacttracing/RecordListAdapter$MODE;", "records", "", "Lca/albertahealthservices/contacttracing/streetpass/view/StreetPassRecordViewModel;", "sourceData", "Lca/albertahealthservices/contacttracing/streetpass/persistence/StreetPassRecord;", "filter", "sample", "filterByModelC", "model", "words", "filterByModelP", "getItemCount", "", "onBindViewHolder", "", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "prepareCollapsedData", "prepareViewData", "setMode", "setRecords", "setSourceData", "setSourceData$app_release", "MODE", "RecordViewHolder", "app_release"}, k = 1, mv = {1, 1, 16})
/* compiled from: RecordListAdapter.kt */
public final class RecordListAdapter extends Adapter<RecordViewHolder> {
    private final LayoutInflater inflater;
    private MODE mode = MODE.ALL;
    private List<StreetPassRecordViewModel> records = CollectionsKt.emptyList();
    private List<StreetPassRecord> sourceData = CollectionsKt.emptyList();

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006¨\u0006\u0007"}, d2 = {"Lca/albertahealthservices/contacttracing/RecordListAdapter$MODE;", "", "(Ljava/lang/String;I)V", "ALL", "COLLAPSE", "MODEL_P", "MODEL_C", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: RecordListAdapter.kt */
    public enum MODE {
        ALL,
        COLLAPSE,
        MODEL_P,
        MODEL_C
    }

    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0013\b\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\u0006\u0010\u0007R\u0011\u0010\b\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\u0007R\u0011\u0010\n\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\f\u0010\rR\u0011\u0010\u000e\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\rR\u0011\u0010\u0010\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\rR\u0011\u0010\u0012\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\rR\u0011\u0010\u0014\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\rR\u0011\u0010\u0016\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\rR\u0011\u0010\u0018\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u0019\u0010\rR\u0011\u0010\u001a\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\rR\u0011\u0010\u001c\u001a\u00020\u000b¢\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\r¨\u0006\u001e"}, d2 = {"Lca/albertahealthservices/contacttracing/RecordListAdapter$RecordViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "itemView", "Landroid/view/View;", "(Lca/albertahealthservices/contacttracing/RecordListAdapter;Landroid/view/View;)V", "filterModelC", "getFilterModelC", "()Landroid/view/View;", "filterModelP", "getFilterModelP", "findsView", "Landroid/widget/TextView;", "getFindsView", "()Landroid/widget/TextView;", "modelCView", "getModelCView", "modelPView", "getModelPView", "msgView", "getMsgView", "org", "getOrg", "signalStrengthView", "getSignalStrengthView", "timestampView", "getTimestampView", "txpowerView", "getTxpowerView", "version", "getVersion", "app_release"}, k = 1, mv = {1, 1, 16})
    /* compiled from: RecordListAdapter.kt */
    public final class RecordViewHolder extends ViewHolder {
        private final View filterModelC;
        private final View filterModelP;
        private final TextView findsView;
        private final TextView modelCView;
        private final TextView modelPView;
        private final TextView msgView;

        /* renamed from: org reason: collision with root package name */
        private final TextView f4org;
        private final TextView signalStrengthView;
        final /* synthetic */ RecordListAdapter this$0;
        private final TextView timestampView;
        private final TextView txpowerView;
        private final TextView version;

        public RecordViewHolder(RecordListAdapter recordListAdapter, View view) {
            Intrinsics.checkParameterIsNotNull(view, "itemView");
            this.this$0 = recordListAdapter;
            super(view);
            AppCompatTextView appCompatTextView = (AppCompatTextView) view.findViewById(R.id.modelc);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView, "itemView.modelc");
            this.modelCView = appCompatTextView;
            AppCompatTextView appCompatTextView2 = (AppCompatTextView) view.findViewById(R.id.modelp);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView2, "itemView.modelp");
            this.modelPView = appCompatTextView2;
            AppCompatTextView appCompatTextView3 = (AppCompatTextView) view.findViewById(R.id.timestamp);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView3, "itemView.timestamp");
            this.timestampView = appCompatTextView3;
            AppCompatTextView appCompatTextView4 = (AppCompatTextView) view.findViewById(R.id.finds);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView4, "itemView.finds");
            this.findsView = appCompatTextView4;
            AppCompatTextView appCompatTextView5 = (AppCompatTextView) view.findViewById(R.id.txpower);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView5, "itemView.txpower");
            this.txpowerView = appCompatTextView5;
            AppCompatTextView appCompatTextView6 = (AppCompatTextView) view.findViewById(R.id.signal_strength);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView6, "itemView.signal_strength");
            this.signalStrengthView = appCompatTextView6;
            Button button = (Button) view.findViewById(R.id.filter_by_modelp);
            Intrinsics.checkExpressionValueIsNotNull(button, "itemView.filter_by_modelp");
            this.filterModelP = button;
            Button button2 = (Button) view.findViewById(R.id.filter_by_modelc);
            Intrinsics.checkExpressionValueIsNotNull(button2, "itemView.filter_by_modelc");
            this.filterModelC = button2;
            AppCompatTextView appCompatTextView7 = (AppCompatTextView) view.findViewById(R.id.msg);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView7, "itemView.msg");
            this.msgView = appCompatTextView7;
            AppCompatTextView appCompatTextView8 = (AppCompatTextView) view.findViewById(R.id.version);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView8, "itemView.version");
            this.version = appCompatTextView8;
            AppCompatTextView appCompatTextView9 = (AppCompatTextView) view.findViewById(R.id.f3org);
            Intrinsics.checkExpressionValueIsNotNull(appCompatTextView9, "itemView.org");
            this.f4org = appCompatTextView9;
        }

        public final TextView getModelCView() {
            return this.modelCView;
        }

        public final TextView getModelPView() {
            return this.modelPView;
        }

        public final TextView getTimestampView() {
            return this.timestampView;
        }

        public final TextView getFindsView() {
            return this.findsView;
        }

        public final TextView getTxpowerView() {
            return this.txpowerView;
        }

        public final TextView getSignalStrengthView() {
            return this.signalStrengthView;
        }

        public final View getFilterModelP() {
            return this.filterModelP;
        }

        public final View getFilterModelC() {
            return this.filterModelC;
        }

        public final TextView getMsgView() {
            return this.msgView;
        }

        public final TextView getVersion() {
            return this.version;
        }

        public final TextView getOrg() {
            return this.f4org;
        }
    }

    @Metadata(bv = {1, 0, 3}, k = 3, mv = {1, 1, 16})
    public final /* synthetic */ class WhenMappings {
        public static final /* synthetic */ int[] $EnumSwitchMapping$0;

        static {
            int[] iArr = new int[MODE.values().length];
            $EnumSwitchMapping$0 = iArr;
            iArr[MODE.COLLAPSE.ordinal()] = 1;
            $EnumSwitchMapping$0[MODE.ALL.ordinal()] = 2;
            $EnumSwitchMapping$0[MODE.MODEL_P.ordinal()] = 3;
            $EnumSwitchMapping$0[MODE.MODEL_C.ordinal()] = 4;
        }
    }

    public RecordListAdapter(Context context) {
        Intrinsics.checkParameterIsNotNull(context, "context");
        LayoutInflater from = LayoutInflater.from(context);
        Intrinsics.checkExpressionValueIsNotNull(from, "LayoutInflater.from(context)");
        this.inflater = from;
    }

    public RecordViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Intrinsics.checkParameterIsNotNull(viewGroup, "parent");
        View inflate = this.inflater.inflate(R.layout.recycler_view_item, viewGroup, false);
        Intrinsics.checkExpressionValueIsNotNull(inflate, "itemView");
        return new RecordViewHolder(this, inflate);
    }

    public void onBindViewHolder(RecordViewHolder recordViewHolder, int i) {
        Intrinsics.checkParameterIsNotNull(recordViewHolder, "holder");
        StreetPassRecordViewModel streetPassRecordViewModel = (StreetPassRecordViewModel) this.records.get(i);
        recordViewHolder.getMsgView().setText(streetPassRecordViewModel.getMsg());
        recordViewHolder.getModelCView().setText(streetPassRecordViewModel.getModelC());
        recordViewHolder.getModelPView().setText(streetPassRecordViewModel.getModelP());
        TextView findsView = recordViewHolder.getFindsView();
        StringBuilder sb = new StringBuilder();
        sb.append("Detections: ");
        sb.append(streetPassRecordViewModel.getNumber());
        findsView.setText(sb.toString());
        recordViewHolder.getTimestampView().setText(Utils.INSTANCE.getDate(streetPassRecordViewModel.getTimeStamp()));
        TextView version = recordViewHolder.getVersion();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("v: ");
        sb2.append(streetPassRecordViewModel.getVersion());
        version.setText(sb2.toString());
        TextView org2 = recordViewHolder.getOrg();
        StringBuilder sb3 = new StringBuilder();
        sb3.append("ORG: ");
        sb3.append(streetPassRecordViewModel.getOrg());
        org2.setText(sb3.toString());
        recordViewHolder.getFilterModelP().setTag(streetPassRecordViewModel);
        recordViewHolder.getFilterModelC().setTag(streetPassRecordViewModel);
        TextView signalStrengthView = recordViewHolder.getSignalStrengthView();
        StringBuilder sb4 = new StringBuilder();
        sb4.append("Signal Strength: ");
        sb4.append(streetPassRecordViewModel.getRssi());
        signalStrengthView.setText(sb4.toString());
        TextView txpowerView = recordViewHolder.getTxpowerView();
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Tx Power: ");
        sb5.append(streetPassRecordViewModel.getTransmissionPower());
        txpowerView.setText(sb5.toString());
        recordViewHolder.getFilterModelP().setOnClickListener(new RecordListAdapter$onBindViewHolder$1(this));
        recordViewHolder.getFilterModelC().setOnClickListener(new RecordListAdapter$onBindViewHolder$2(this));
    }

    private final List<StreetPassRecordViewModel> filter(StreetPassRecordViewModel streetPassRecordViewModel) {
        int i = WhenMappings.$EnumSwitchMapping$0[this.mode.ordinal()];
        if (i == 1) {
            return prepareCollapsedData(this.sourceData);
        }
        if (i == 2) {
            return prepareViewData(this.sourceData);
        }
        if (i == 3) {
            return filterByModelP(streetPassRecordViewModel, this.sourceData);
        }
        if (i == 4) {
            return filterByModelC(streetPassRecordViewModel, this.sourceData);
        }
        throw new NoWhenBranchMatchedException();
    }

    private final List<StreetPassRecordViewModel> filterByModelC(StreetPassRecordViewModel streetPassRecordViewModel, List<StreetPassRecord> list) {
        if (streetPassRecordViewModel == null) {
            return prepareViewData(list);
        }
        Iterable iterable = list;
        Collection arrayList = new ArrayList();
        for (Object next : iterable) {
            if (Intrinsics.areEqual((Object) ((StreetPassRecord) next).getModelC(), (Object) streetPassRecordViewModel.getModelC())) {
                arrayList.add(next);
            }
        }
        return prepareViewData((List) arrayList);
    }

    private final List<StreetPassRecordViewModel> filterByModelP(StreetPassRecordViewModel streetPassRecordViewModel, List<StreetPassRecord> list) {
        if (streetPassRecordViewModel == null) {
            return prepareViewData(list);
        }
        Iterable iterable = list;
        Collection arrayList = new ArrayList();
        for (Object next : iterable) {
            if (Intrinsics.areEqual((Object) ((StreetPassRecord) next).getModelP(), (Object) streetPassRecordViewModel.getModelP())) {
                arrayList.add(next);
            }
        }
        return prepareViewData((List) arrayList);
    }

    /* JADX WARNING: type inference failed for: r4v0 */
    /* JADX WARNING: type inference failed for: r4v1, types: [ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord] */
    /* JADX WARNING: type inference failed for: r4v3 */
    /* JADX WARNING: type inference failed for: r4v4, types: [ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord] */
    /* JADX WARNING: type inference failed for: r4v5, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r6v2 */
    /* JADX WARNING: type inference failed for: r4v6 */
    /* JADX WARNING: type inference failed for: r8v0, types: [java.lang.Object] */
    /* JADX WARNING: type inference failed for: r9v0 */
    /* JADX WARNING: type inference failed for: r4v7 */
    /* JADX WARNING: type inference failed for: r4v8 */
    /* JADX WARNING: type inference failed for: r4v17 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v0
  assigns: [?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.lang.Object, ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord, ?[OBJECT, ARRAY]]
  uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord, ?[OBJECT, ARRAY]]
  mth insns count: 84
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(Unknown Source)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    private final List<StreetPassRecordViewModel> prepareCollapsedData(List<StreetPassRecord> list) {
        StreetPassRecordViewModel streetPassRecordViewModel;
        Iterable iterable = list;
        Map linkedHashMap = new LinkedHashMap();
        for (Object next : iterable) {
            String modelC = ((StreetPassRecord) next).getModelC();
            Object obj = linkedHashMap.get(modelC);
            if (obj == null) {
                obj = new ArrayList();
                linkedHashMap.put(modelC, obj);
            }
            ((List) obj).add(next);
        }
        HashSet hashSet = new HashSet();
        ArrayList arrayList = new ArrayList();
        for (Object next2 : iterable) {
            if (hashSet.add(((StreetPassRecord) next2).getModelC())) {
                arrayList.add(next2);
            }
        }
        Iterable<StreetPassRecord> iterable2 = arrayList;
        Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
        for (StreetPassRecord streetPassRecord : iterable2) {
            List list2 = (List) linkedHashMap.get(streetPassRecord.getModelC());
            ? r4 = 0;
            Number valueOf = list2 != null ? Integer.valueOf(list2.size()) : null;
            if (valueOf != null) {
                int intValue = valueOf.intValue();
                List list3 = (List) linkedHashMap.get(streetPassRecord.getModelC());
                if (list3 != null) {
                    Iterator it = list3.iterator();
                    if (it.hasNext()) {
                        r4 = it.next();
                        if (it.hasNext()) {
                            long timestamp = ((StreetPassRecord) r4).getTimestamp();
                            do {
                                ? next3 = it.next();
                                long timestamp2 = ((StreetPassRecord) next3).getTimestamp();
                                if (timestamp < timestamp2) {
                                    r4 = next3;
                                    timestamp = timestamp2;
                                }
                            } while (it.hasNext());
                        }
                    }
                    r4 = (StreetPassRecord) r4;
                }
                if (r4 != 0) {
                    streetPassRecordViewModel = new StreetPassRecordViewModel(r4, intValue);
                } else {
                    streetPassRecordViewModel = new StreetPassRecordViewModel(streetPassRecord, intValue);
                }
            } else {
                streetPassRecordViewModel = new StreetPassRecordViewModel(streetPassRecord);
            }
            arrayList2.add(streetPassRecordViewModel);
        }
        return (List) arrayList2;
    }

    private final List<StreetPassRecordViewModel> prepareViewData(List<StreetPassRecord> list) {
        Iterable<StreetPassRecord> reversed = CollectionsKt.reversed(list);
        Collection arrayList = new ArrayList(CollectionsKt.collectionSizeOrDefault(reversed, 10));
        for (StreetPassRecord streetPassRecordViewModel : reversed) {
            arrayList.add(new StreetPassRecordViewModel(streetPassRecordViewModel));
        }
        return (List) arrayList;
    }

    public final void setMode(MODE mode2) {
        Intrinsics.checkParameterIsNotNull(mode2, "mode");
        setMode(mode2, null);
    }

    /* access modifiers changed from: private */
    public final void setMode(MODE mode2, StreetPassRecordViewModel streetPassRecordViewModel) {
        this.mode = mode2;
        setRecords(filter(streetPassRecordViewModel));
    }

    private final void setRecords(List<StreetPassRecordViewModel> list) {
        this.records = list;
        notifyDataSetChanged();
    }

    public final void setSourceData$app_release(List<StreetPassRecord> list) {
        Intrinsics.checkParameterIsNotNull(list, "records");
        this.sourceData = list;
        setMode(this.mode);
    }

    public int getItemCount() {
        return this.records.size();
    }
}

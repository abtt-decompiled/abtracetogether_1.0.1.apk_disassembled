package ca.albertahealthservices.contacttracing;

import android.util.Log;
import android.webkit.WebView;
import ca.albertahealthservices.contacttracing.fragment.ExportData;
import ca.albertahealthservices.contacttracing.streetpass.persistence.StreetPassRecord;
import io.reactivex.functions.Consumer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.text.StringsKt;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0010\u0000\u001a\u00020\u00012\u000e\u0010\u0002\u001a\n \u0004*\u0004\u0018\u00010\u00030\u0003H\n¢\u0006\u0002\b\u0005"}, d2 = {"<anonymous>", "", "exportedData", "Lca/albertahealthservices/contacttracing/fragment/ExportData;", "kotlin.jvm.PlatformType", "accept"}, k = 3, mv = {1, 1, 16})
/* compiled from: PlotActivity.kt */
final class PlotActivity$onCreate$zipResult$2<T> implements Consumer<ExportData> {
    final /* synthetic */ int $displayTimePeriod;
    final /* synthetic */ PlotActivity this$0;

    PlotActivity$onCreate$zipResult$2(PlotActivity plotActivity, int i) {
        this.this$0 = plotActivity;
        this.$displayTimePeriod = i;
    }

    public final void accept(ExportData exportData) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        String str6;
        String str7;
        String str8;
        Iterator it;
        String str9;
        Set set;
        Map map;
        String str10;
        String str11;
        String str12;
        String str13;
        Map map2;
        String str14;
        if (!exportData.getRecordList().isEmpty()) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long j = (long) 1000;
            long timestamp = (((StreetPassRecord) CollectionsKt.sortedWith(exportData.getRecordList(), new PlotActivity$onCreate$zipResult$2$$special$$inlined$sortedByDescending$1()).get(0)).getTimestamp() / j) + ((long) 60);
            String format = simpleDateFormat.format(new Date(timestamp * j));
            long j2 = timestamp - ((long) (this.$displayTimePeriod * 3600));
            String format2 = simpleDateFormat.format(new Date(j2 * j));
            Iterable recordList = exportData.getRecordList();
            Collection arrayList = new ArrayList();
            Iterator it2 = recordList.iterator();
            while (true) {
                boolean z = true;
                if (!it2.hasNext()) {
                    break;
                }
                Object next = it2.next();
                StreetPassRecord streetPassRecord = (StreetPassRecord) next;
                if (streetPassRecord.getTimestamp() / j < j2 || streetPassRecord.getTimestamp() / j > timestamp) {
                    z = false;
                }
                if (z) {
                    arrayList.add(next);
                }
            }
            List list = (List) arrayList;
            String str15 = "UTF-8";
            String str16 = "text/html";
            if (!list.isEmpty()) {
                Iterable iterable = list;
                Map linkedHashMap = new LinkedHashMap();
                for (Object next2 : iterable) {
                    String modelC = ((StreetPassRecord) next2).getModelC();
                    Object obj = linkedHashMap.get(modelC);
                    if (obj == null) {
                        obj = new ArrayList();
                        linkedHashMap.put(modelC, obj);
                    }
                    ((List) obj).add(next2);
                }
                Map linkedHashMap2 = new LinkedHashMap();
                for (Object next3 : iterable) {
                    String modelP = ((StreetPassRecord) next3).getModelP();
                    Object obj2 = linkedHashMap2.get(modelP);
                    if (obj2 == null) {
                        obj2 = new ArrayList();
                        linkedHashMap2.put(modelP, obj2);
                    }
                    ((List) obj2).add(next3);
                }
                Set union = CollectionsKt.union(linkedHashMap.keySet(), CollectionsKt.toList(linkedHashMap2.keySet()));
                String access$getTAG$p = this.this$0.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("allModels: ");
                sb.append(union);
                Log.d(access$getTAG$p, sb.toString());
                List sortedWith = CollectionsKt.sortedWith(union, new PlotActivity$onCreate$zipResult$2$sortedModelList$1(linkedHashMap, linkedHashMap2));
                Iterable<String> iterable2 = sortedWith;
                Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                Iterator it3 = iterable2.iterator();
                while (true) {
                    str = "";
                    if (!it3.hasNext()) {
                        break;
                    }
                    String str17 = (String) it3.next();
                    int indexOf = sortedWith.indexOf(str17) + 1;
                    boolean containsKey = linkedHashMap.containsKey(str17);
                    boolean containsKey2 = linkedHashMap2.containsKey(str17);
                    List list2 = (List) linkedHashMap.get(str17);
                    String str18 = "\"]";
                    String str19 = "[\"";
                    String str20 = "\", \"";
                    String str21 = null;
                    if (list2 != null) {
                        Iterable iterable3 = list2;
                        it = it3;
                        str8 = str15;
                        str7 = str16;
                        Collection arrayList3 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable3, 10));
                        Iterator it4 = iterable3.iterator();
                        while (it4.hasNext()) {
                            Iterator it5 = it4;
                            arrayList3.add(simpleDateFormat.format(new Date(((StreetPassRecord) it4.next()).getTimestamp())));
                            it4 = it5;
                        }
                        str9 = CollectionsKt.joinToString$default((List) arrayList3, str20, str19, str18, 0, null, null, 56, null);
                    } else {
                        str8 = str15;
                        str7 = str16;
                        it = it3;
                        str9 = null;
                    }
                    List list3 = (List) linkedHashMap.get(str17);
                    String str22 = "]";
                    String str23 = "[";
                    String str24 = ", ";
                    if (list3 != null) {
                        Iterable<StreetPassRecord> iterable4 = list3;
                        map = linkedHashMap;
                        set = union;
                        Collection arrayList4 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable4, 10));
                        for (StreetPassRecord rssi : iterable4) {
                            arrayList4.add(Integer.valueOf(rssi.getRssi()));
                        }
                        str10 = CollectionsKt.joinToString$default((List) arrayList4, str24, str23, str22, 0, null, null, 56, null);
                    } else {
                        map = linkedHashMap;
                        set = union;
                        str10 = null;
                    }
                    List list4 = (List) linkedHashMap2.get(str17);
                    if (list4 != null) {
                        Iterable iterable5 = list4;
                        str11 = format;
                        Collection arrayList5 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable5, 10));
                        Iterator it6 = iterable5.iterator();
                        while (it6.hasNext()) {
                            Iterator it7 = it6;
                            arrayList5.add(simpleDateFormat.format(new Date(((StreetPassRecord) it6.next()).getTimestamp())));
                            it6 = it7;
                        }
                        str12 = CollectionsKt.joinToString$default((List) arrayList5, str20, str19, str18, 0, null, null, 56, null);
                    } else {
                        str11 = format;
                        str12 = null;
                    }
                    List list5 = (List) linkedHashMap2.get(str17);
                    if (list5 != null) {
                        Iterable<StreetPassRecord> iterable6 = list5;
                        Collection arrayList6 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable6, 10));
                        for (StreetPassRecord rssi2 : iterable6) {
                            arrayList6.add(Integer.valueOf(rssi2.getRssi()));
                        }
                        str21 = CollectionsKt.joinToString$default((List) arrayList6, str24, str23, str22, 0, null, null, 56, null);
                    }
                    String str25 = str21;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("var data");
                    sb2.append(indexOf);
                    sb2.append(" = [];");
                    String sb3 = sb2.toString();
                    String str26 = " = data";
                    String str27 = "',\n                              yaxis: 'y";
                    String str28 = ",\n                              xaxis: 'x";
                    String str29 = ",\n                              y: ";
                    SimpleDateFormat simpleDateFormat2 = simpleDateFormat;
                    String str30 = "\n                            var data";
                    if (!containsKey) {
                        map2 = linkedHashMap2;
                        str13 = format2;
                        str14 = str;
                    } else {
                        map2 = linkedHashMap2;
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append(str30);
                        sb4.append(indexOf);
                        str13 = format2;
                        sb4.append("a = {\n                              name: 'central',\n                              x: ");
                        sb4.append(str9);
                        sb4.append(str29);
                        sb4.append(str10);
                        sb4.append(str28);
                        sb4.append(indexOf);
                        sb4.append(str27);
                        sb4.append(indexOf);
                        sb4.append("',\n                              mode: 'markers',\n                              type: 'scatter',\n                              line: {color: 'blue'}\n                            };\n                            data");
                        sb4.append(indexOf);
                        sb4.append(str26);
                        sb4.append(indexOf);
                        sb4.append(".concat(data");
                        sb4.append(indexOf);
                        sb4.append("a);\n                        ");
                        str14 = StringsKt.trimIndent(sb4.toString());
                    }
                    if (containsKey2) {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str30);
                        sb5.append(indexOf);
                        sb5.append("b = {\n                              name: 'peripheral',\n                              x: ");
                        sb5.append(str12);
                        sb5.append(str29);
                        sb5.append(str25);
                        sb5.append(str28);
                        sb5.append(indexOf);
                        sb5.append(str27);
                        sb5.append(indexOf);
                        sb5.append("',\n                              mode: 'markers',\n                              type: 'scatter',\n                              line: {color: 'red'}\n                            };\n                            data");
                        sb5.append(indexOf);
                        sb5.append(str26);
                        sb5.append(indexOf);
                        sb5.append(".concat(data");
                        sb5.append(indexOf);
                        sb5.append("b);\n                        ");
                        str = StringsKt.trimIndent(sb5.toString());
                    }
                    String str31 = str;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append(sb3);
                    sb6.append(str14);
                    sb6.append(str31);
                    arrayList2.add(sb6.toString());
                    linkedHashMap2 = map2;
                    it3 = it;
                    simpleDateFormat = simpleDateFormat2;
                    format2 = str13;
                    str15 = str8;
                    str16 = str7;
                    linkedHashMap = map;
                    union = set;
                    format = str11;
                }
                String str32 = format;
                String str33 = str15;
                String str34 = str16;
                Set set2 = union;
                String str35 = format2;
                String str36 = "\n";
                String joinToString$default = CollectionsKt.joinToString$default((List) arrayList2, str36, null, null, 0, null, null, 62, null);
                Collection arrayList7 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                for (String indexOf2 : iterable2) {
                    int indexOf3 = sortedWith.indexOf(indexOf2) + 1;
                    if (indexOf3 < 20) {
                        StringBuilder sb7 = new StringBuilder();
                        sb7.append("\n                            data = data.concat(data");
                        sb7.append(indexOf3);
                        sb7.append(");\n                        ");
                        str6 = StringsKt.trimIndent(sb7.toString());
                    } else {
                        str6 = str;
                    }
                    arrayList7.add(str6);
                }
                String joinToString$default2 = CollectionsKt.joinToString$default((List) arrayList7, str36, null, null, 0, null, null, 62, null);
                Collection arrayList8 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                for (String indexOf4 : iterable2) {
                    int indexOf5 = sortedWith.indexOf(indexOf4) + 1;
                    if (indexOf5 < 20) {
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append("\n                                  xaxis");
                        sb8.append(indexOf5);
                        sb8.append(": {\n                                    type: 'date',\n                                    tickformat: '%H:%M:%S',\n                                    range: ['");
                        str5 = str35;
                        sb8.append(str5);
                        sb8.append("', '");
                        str3 = str32;
                        sb8.append(str3);
                        sb8.append("'],\n                                    dtick: ");
                        sb8.append(this.$displayTimePeriod * 5);
                        sb8.append(" * 60 * 1000\n                                  }\n                        ");
                        str4 = StringsKt.trimIndent(sb8.toString());
                    } else {
                        str5 = str35;
                        str3 = str32;
                        str4 = str;
                    }
                    arrayList8.add(str4);
                    str35 = str5;
                    str32 = str3;
                }
                String str37 = str35;
                String str38 = str32;
                String str39 = ",\n";
                String joinToString$default3 = CollectionsKt.joinToString$default((List) arrayList8, str39, null, null, 0, null, null, 62, null);
                Collection arrayList9 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable2, 10));
                for (String str40 : iterable2) {
                    int indexOf6 = sortedWith.indexOf(str40) + 1;
                    if (indexOf6 < 20) {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("\n                                  yaxis");
                        sb9.append(indexOf6);
                        sb9.append(": {\n                                    range: [-100, -30],\n                                    ticks: 'outside',\n                                    dtick: 10,\n                                    title: {\n                                      text: \"");
                        sb9.append(str40);
                        sb9.append("\"\n                                    }\n                                  }\n                        ");
                        str2 = StringsKt.trimIndent(sb9.toString());
                    } else {
                        str2 = str;
                    }
                    arrayList9.add(str2);
                }
                String joinToString$default4 = CollectionsKt.joinToString$default((List) arrayList9, str39, null, null, 0, null, null, 62, null);
                StringBuilder sb10 = new StringBuilder();
                sb10.append("\n                        <head>\n                            <script src='https://cdn.plot.ly/plotly-latest.min.js'></script>\n                        </head>\n                        <body>\n                            <div id='myDiv'></div>\n                            <script>\n                                ");
                sb10.append(joinToString$default);
                sb10.append("\n                                \n                                var data = [];\n                                ");
                sb10.append(joinToString$default2);
                sb10.append("\n                                \n                                var layout = {\n                                  title: 'Activities from <b>");
                Intrinsics.checkExpressionValueIsNotNull(str37, "startTimeString");
                sb10.append(StringsKt.substring(str37, new IntRange(11, 15)));
                sb10.append("</b> to <b>");
                Intrinsics.checkExpressionValueIsNotNull(str38, "endTimeString");
                sb10.append(StringsKt.substring(str38, new IntRange(11, 15)));
                sb10.append("</b>   <span style=\"color:blue\">central</span> <span style=\"color:red\">peripheral</span>',\n                                  height: 135 * ");
                sb10.append(set2.size());
                sb10.append(",\n                                  showlegend: false,\n                                  grid: {rows: ");
                sb10.append(set2.size());
                sb10.append(", columns: 1, pattern: 'independent'},\n                                  margin: {\n                                    t: 30,\n                                    r: 30,\n                                    b: 20,\n                                    l: 50,\n                                    pad: 0\n                                  },\n                                  ");
                sb10.append(joinToString$default3);
                sb10.append(",\n                                  ");
                sb10.append(joinToString$default4);
                sb10.append("\n                                };\n                                \n                                var config = {\n                                    responsive: true, \n                                    displayModeBar: false, \n                                    displaylogo: false, \n                                    modeBarButtonsToRemove: ['toImage', 'sendDataToCloud', 'editInChartStudio', 'zoom2d', 'select2d', 'pan2d', 'lasso2d', 'autoScale2d', 'resetScale2d', 'zoomIn2d', 'zoomOut2d', 'hoverClosestCartesian', 'hoverCompareCartesian', 'toggleHover', 'toggleSpikelines']\n                                }\n                                \n                                Plotly.newPlot('myDiv', data, layout, config);\n                            </script>\n                        </body>\n                    ");
                String trimIndent = StringsKt.trimIndent(sb10.toString());
                String access$getTAG$p2 = this.this$0.TAG;
                StringBuilder sb11 = new StringBuilder();
                sb11.append("customHtml: ");
                sb11.append(trimIndent);
                Log.d(access$getTAG$p2, sb11.toString());
                ((WebView) this.this$0._$_findCachedViewById(R.id.webView)).loadData(trimIndent, str34, str33);
            } else {
                String str41 = str15;
                String str42 = str16;
                WebView webView = (WebView) this.this$0._$_findCachedViewById(R.id.webView);
                StringBuilder sb12 = new StringBuilder();
                sb12.append("No data received in the last ");
                sb12.append(this.$displayTimePeriod);
                sb12.append(" hour(s) or more.");
                webView.loadData(sb12.toString(), str42, str41);
            }
        }
    }
}

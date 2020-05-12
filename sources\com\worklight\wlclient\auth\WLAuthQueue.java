package com.worklight.wlclient.auth;

import com.worklight.common.Logger;
import com.worklight.wlclient.api.WLConstants;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WLAuthQueue {
    private Map<String, ArrayList<WLResponseListener>> queue = new HashMap();

    protected WLAuthQueue() {
    }

    /* access modifiers changed from: protected */
    public void releaseQueueOnSuccess(String str, WLResponse wLResponse) {
        String str2 = "releaseQueueOnSuccess";
        Logger.enter(getClass().getSimpleName(), str2);
        ArrayList arrayList = new ArrayList();
        for (String str3 : this.queue.keySet()) {
            if (isKeysEqual(str3, str)) {
                Iterator it = ((ArrayList) this.queue.get(str3)).iterator();
                while (it.hasNext()) {
                    ((WLResponseListener) it.next()).onSuccess(wLResponse);
                }
                arrayList.add(str3);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.queue.remove((String) it2.next());
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: protected */
    public void releaseQueueOnFailure(String str, WLFailResponse wLFailResponse) {
        String str2 = "releaseQueueOnFailure";
        Logger.enter(getClass().getSimpleName(), str2);
        ArrayList arrayList = new ArrayList();
        for (String str3 : this.queue.keySet()) {
            if (isKeysEqual(str, str3)) {
                Iterator it = ((ArrayList) this.queue.get(str3)).iterator();
                while (it.hasNext()) {
                    ((WLResponseListener) it.next()).onFailure(wLFailResponse);
                }
                arrayList.add(str3);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            this.queue.remove((String) it2.next());
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: protected */
    public void addToAuthorizationQueue(String str, WLResponseListener wLResponseListener) {
        String str2 = "addToAuthorizationQueue";
        Logger.enter(getClass().getSimpleName(), str2);
        if (str == null) {
            str = WLConstants.DEFAULT_SCOPE;
        }
        ArrayList arrayList = (ArrayList) this.queue.get(str);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.queue.put(str, arrayList);
        }
        arrayList.add(wLResponseListener);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    /* access modifiers changed from: protected */
    public void abortAuthorization(WLFailResponse wLFailResponse) {
        String str = "abortAuthorization";
        Logger.enter(getClass().getSimpleName(), str);
        for (String str2 : this.queue.keySet()) {
            Iterator it = ((ArrayList) this.queue.get(str2)).iterator();
            while (it.hasNext()) {
                ((WLResponseListener) it.next()).onFailure(wLFailResponse);
            }
        }
        this.queue.clear();
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: protected */
    public boolean isEmpty() {
        String str = "isEmpty";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.queue.isEmpty();
    }

    /* access modifiers changed from: protected */
    public String getNextScopeToObtain() {
        String str = "getNextScopeToObtain";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        Iterator it = this.queue.keySet().iterator();
        return it.hasNext() ? (String) it.next() : "";
    }

    /* access modifiers changed from: protected */
    public ArrayList<WLResponseListener> get(String str) {
        String str2 = "get";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return (ArrayList) this.queue.get(str);
    }

    private boolean isKeysEqual(String str, String str2) {
        String str3 = "isKeysEqual";
        Logger.enter(getClass().getSimpleName(), str3);
        String str4 = " ";
        List asList = Arrays.asList(str.split(str4));
        List asList2 = Arrays.asList(str2.split(str4));
        Logger.exit(getClass().getSimpleName(), str3);
        return asList.containsAll(asList2) && asList2.containsAll(asList);
    }
}

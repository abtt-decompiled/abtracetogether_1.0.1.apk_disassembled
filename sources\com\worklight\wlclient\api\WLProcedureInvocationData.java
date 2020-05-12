package com.worklight.wlclient.api;

import com.worklight.common.Logger;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;

public class WLProcedureInvocationData {
    private String adapter;
    private boolean compressResponse = false;
    private boolean isCacheableRequest = false;
    private Object[] parameters;
    private String procedure;

    public WLProcedureInvocationData(String str, String str2) {
        this.adapter = str;
        this.procedure = str2;
    }

    public WLProcedureInvocationData(String str, String str2, boolean z) {
        this.adapter = str;
        this.procedure = str2;
        this.compressResponse = z;
    }

    public void setParameters(Object[] objArr) {
        String str = "setParameters";
        Logger.enter(getClass().getSimpleName(), str);
        this.parameters = objArr;
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* access modifiers changed from: 0000 */
    public Map<String, String> getInvocationDataMap() {
        HashMap hashMap = new HashMap();
        JSONArray jSONArray = new JSONArray();
        if (this.parameters != null) {
            int i = 0;
            while (true) {
                Object[] objArr = this.parameters;
                if (i >= objArr.length) {
                    break;
                }
                try {
                    jSONArray.put(i, objArr[i]);
                    i++;
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        hashMap.put("adapter", this.adapter);
        hashMap.put("procedure", this.procedure);
        hashMap.put("parameters", jSONArray.toString());
        hashMap.put("compressResponse", Boolean.toString(this.compressResponse));
        return hashMap;
    }

    public void setCompressResponse(boolean z) {
        String str = "setCompressResponse";
        Logger.enter(getClass().getSimpleName(), str);
        this.compressResponse = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void setCacheableRequest(boolean z) {
        String str = "setCacheableRequest";
        Logger.enter(getClass().getSimpleName(), str);
        this.isCacheableRequest = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isCacheableRequest() {
        String str = "isCacheableRequest";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.isCacheableRequest;
    }

    public String getAdapter() {
        String str = "getAdapter";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.adapter;
    }

    public String getProcedure() {
        String str = "getProcedure";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.procedure;
    }

    public Object[] getParameters() {
        String str = "getParameters";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.parameters;
    }

    public boolean isCompressResponse() {
        String str = "isCompressResponse";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.compressResponse;
    }
}

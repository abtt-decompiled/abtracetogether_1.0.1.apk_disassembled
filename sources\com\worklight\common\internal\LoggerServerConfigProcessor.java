package com.worklight.common.internal;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.worklight.common.Logger;
import com.worklight.common.Logger.LEVEL;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class LoggerServerConfigProcessor {
    private static final String CLIENT_LOG_PROFILES = "clientLogProfiles";
    private static final String LEVEL = "level";
    private static final String PKG = "name";

    public static void processLoggerServerConfig(JSONObject jSONObject, Context context) throws JSONException {
        JSONArray jSONArray = (JSONArray) jSONObject.get(CLIENT_LOG_PROFILES);
        if (jSONArray == null || jSONArray.length() == 0) {
            removeServerLoggerConfigOverrides(context);
            return;
        }
        JSONObject jSONObject2 = new JSONObject();
        for (int i = 0; i < jSONArray.length(); i++) {
            JSONObject jSONObject3 = jSONArray.getJSONObject(i);
            String str = PKG;
            String str2 = "level";
            if ((!jSONObject3.has(str) || jSONObject3.isNull(str)) && jSONObject3.has(str2)) {
                context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0).edit().putString(Logger.SHARED_PREF_KEY_level_from_server, jSONObject3.getString(str2)).commit();
            } else if (jSONObject3.has(str) && jSONObject3.has(str2)) {
                jSONObject2.put(jSONObject3.getString(str), jSONObject3.getString(str2));
            }
        }
        int length = jSONObject2.length();
        String str3 = Logger.SHARED_PREF_KEY_filters_from_server;
        if (length > 0) {
            context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0).edit().putString(str3, jSONObject2.toString()).commit();
        } else {
            context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0).edit().putString(str3, "{}").commit();
        }
    }

    public static void removeServerLoggerConfigOverrides(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0);
        Editor edit = sharedPreferences.edit();
        edit.remove(Logger.SHARED_PREF_KEY_logPersistence_from_server);
        edit.remove(Logger.SHARED_PREF_KEY_level_from_server);
        edit.remove(Logger.SHARED_PREF_KEY_filters_from_server);
        edit.commit();
        Logger.setLevel(LEVEL.fromString(sharedPreferences.getString("level", Logger.getLevelDefault().toString())));
        Logger.setCapture(sharedPreferences.getBoolean(Logger.SHARED_PREF_KEY_logPersistence, true));
        try {
            Logger.setFilters(Logger.JSONObjectToHashMap(new JSONObject(sharedPreferences.getString(Logger.SHARED_PREF_KEY_filters, "{}"))));
        } catch (JSONException unused) {
        }
    }
}

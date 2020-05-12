package com.worklight.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.XmlResourceParser;
import android.provider.Settings.Secure;
import com.worklight.common.security.WLDeviceAuthManager;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.utils.AESStringEncryption;
import com.worklight.wlclient.api.SecurityUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import org.json.JSONException;
import org.json.JSONObject;

public class WLConfig {
    private static final String ANDROID_ASSET = "/android_asset";
    public static final String APP_ID = "id";
    private static final String APP_ID_PREF_KEY = "appIdPref";
    private static final String APP_INSTALL_TIME_KEY = "appInstallTime";
    private static final String CLEAR_CACHE_NEXT_LOAD = "com.worklight.clearCacheNextLoad";
    public static final String CLIENT_PLATFORM = "clientPlatform";
    public static final String ENABLE_LEGACY_HTTP = "legacy_http";
    private static final String IGNORED_FILE_EXTENSIONS = "ignoredFileExtensions";
    private static final String IN_PROGRESS_CHECKSUM = "inProgressChecksum";
    private static final String LANGUAGE_PREFS = "languagePreferences";
    private static final String MFP_PLATFORM_VERSION = "8.0.0.00.2015-12-11T23:31:24Z";
    public static final String SDK_PROTOCOL_VERSION_KEY = "sdk_protocol_version";
    private static final String SECURITY_PREFS = "SecurityPrefs";
    private static final String SECURITY_TOKEN_PREFS = "SecurityTokenPrefs";
    private static final String USE_CUSTOM_SERVER_URL = "useCustomServerUrl";
    public static final String VERSION = "version";
    public static final String WL_APP_ID = "wlAppId";
    public static final String WL_APP_VERSION = "wlAppVersion";
    public static final String WL_CLIENT_PROPS_NAME = "mfpclient.properties";
    private static final String WL_DEFAULT_SERVER_URL = "WLDefaultServerURL";
    private static final String WL_DEVICE_ID_STRENGTH = "wlDeviceIdStrength";
    private static final String WL_DIRECT_UPDATE_PUBLIC_KEY = "wlSecureDirectUpdatePublicKey";
    public static final String WL_DIRECT_UPDATE_TEMP_FOLDER = "wlDirectUppdateTempFolder";
    public static final String WL_ENABLE_REFRESH_TOKEN = "wlEnableRefreshToken";
    private static final String WL_ENVIRONMENT = "wlEnvironment";
    public static final String WL_GCM_SENDER = "GcmSenderId";
    private static final String WL_IS_ENCRYPTED = "WLIsEncrypted";
    private static final String WL_IS_EXTERNAL_WEBRESOURCES = "WLIsExternalWebResources";
    public static final String WL_PLATFORM_VERSION = "wlPlatformVersion";
    private static final String WL_PREFS = "WLPrefs";
    private static final String WL_RESOURCES_CHECKSUM_PREF_KEY = "wlResourcesChecksum";
    public static final String WL_SERVER_CONTEXT = "wlServerContext";
    public static final String WL_SERVER_HOST = "wlServerHost";
    public static final String WL_SERVER_PORT = "wlServerPort";
    public static final String WL_SERVER_PROTOCOL = "wlServerProtocol";
    public static final String WL_SERVER_URI = "serverUri";
    private static final String WL_SERVER_URL = "WLServerURL";
    public static final String WL_SHARE_COOKIES = "wlShareCookies";
    public static final String WL_SHARE_USER_CERT = "wlShareUserCert";
    private static final String WL_SHOW_ALL_NOTIFICATIONS_IN_TRAY = "showAllNotificationsInTray";
    private static final String WL_TEST_WEB_RESOURCES_CHECKSUM = "testWebResourcesChecksum";
    public static final String WL_WEB_RESOURCES_UNPACKD_SIZE = "webResourcesSize";
    public static final String WL_X_PLATFORM_VERSION = "x-wl-platform-version";
    public static final String WL_X_VERSION_HEADER = "x-wl-app-version";
    private static WLConfig singleton;
    private String absolutePathToExternalAppFiles;
    private Context con;
    private AESStringEncryption encrypter;
    private boolean isApplicationForeground = false;
    private boolean isInitComplete = false;
    private String mainFile;
    private String oldAbsolutePathToExternalAppFiles;
    private SharedPreferences prefs = null;
    private long serverRelativeTime;
    private String uuid;
    private Properties wlProperties = new Properties();

    /* JADX WARNING: Can't wrap try/catch for region: R(3:21|22|23) */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x012b, code lost:
        throw new java.lang.RuntimeException("The URI provided in the serverUri property is not valid. Provide a valid URI");
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:21:0x0124 */
    private WLConfig(Context context) {
        String str = "/mfp/";
        String str2 = WL_SERVER_URI;
        String str3 = "";
        if (!isBOMPresent(context)) {
            try {
                this.wlProperties.load(context.getAssets().open(WL_CLIENT_PROPS_NAME));
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                this.wlProperties.setProperty(WL_APP_ID, packageInfo.packageName);
                Properties properties = this.wlProperties;
                String str4 = WL_APP_VERSION;
                StringBuilder sb = new StringBuilder();
                sb.append(str3);
                sb.append(packageInfo.versionName);
                properties.setProperty(str4, sb.toString());
                this.wlProperties.setProperty(WL_PLATFORM_VERSION, MFP_PLATFORM_VERSION);
                this.prefs = context.getSharedPreferences("WLPrefs", 0);
                this.con = context;
                this.mainFile = str3;
                this.oldAbsolutePathToExternalAppFiles = context.getFilesDir().getAbsolutePath();
                this.absolutePathToExternalAppFiles = WLUtils.getNoBackupFilesDir(context).getAbsolutePath();
                this.uuid = Secure.getString(context.getContentResolver(), "android_id");
                this.encrypter = new AESStringEncryption(this.uuid);
                String property = this.wlProperties.getProperty(str2);
                if (property != null && !property.isEmpty()) {
                    URL url = new URL(property);
                    String protocol = url.getProtocol();
                    String host = url.getHost();
                    int port = url.getPort();
                    if (port == -1) {
                        port = protocol.equals("https") ? 443 : 80;
                    }
                    String path = url.getPath();
                    if (path == null || path.isEmpty()) {
                        throw new RuntimeException("The serverUri property does not contain an instanceID. Provide a valid instanceID");
                    }
                    String substring = path.substring(1, path.length());
                    this.wlProperties.setProperty(WL_SERVER_PROTOCOL, protocol);
                    this.wlProperties.setProperty(WL_SERVER_HOST, host);
                    this.wlProperties.setProperty(WL_SERVER_PORT, String.valueOf(port));
                    this.wlProperties.setProperty(WL_SERVER_CONTEXT, str);
                    property = String.format("%s://%s:%d/%s%s", new Object[]{protocol, host, Integer.valueOf(port), substring, str});
                    this.wlProperties.setProperty(str2, property);
                }
                if (property != null) {
                    return;
                }
                if (getDefaultHost() == null || getDefaultHost().isEmpty()) {
                    throw new RuntimeException("You must specify the server host (wlServerHost or serverUri) in the client configuration file (mfpclient.properties).");
                }
            } catch (IOException unused) {
                throw new RuntimeException("Client configuration file mfpclient.properties not found in application assets. Use the MFP CLI command 'mfpdev app register' to create the file.");
            } catch (NameNotFoundException e) {
                throw new Error(e);
            }
        } else {
            throw new RuntimeException("Client configuration file mfpclient.properties contains a BOM (Byte Order Mark). Save the file without a BOM");
        }
    }

    public static synchronized void createInstance(Context context) {
        synchronized (WLConfig.class) {
            if (singleton == null) {
                singleton = new WLConfig(context.getApplicationContext());
            }
        }
    }

    private boolean isBOMPresent(Context context) {
        String str = "isBOMPresent";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(WL_CLIENT_PROPS_NAME), "UTF8"));
            bufferedReader.mark(4);
            if (65279 == bufferedReader.read()) {
                Logger.exit(getClass().getSimpleName(), str);
                return true;
            }
            bufferedReader.reset();
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static WLConfig getInstance() {
        WLConfig wLConfig = singleton;
        if (wLConfig != null) {
            return wLConfig;
        }
        throw new IllegalStateException("getInstance can't be called before createInstance");
    }

    public void writePref(String str, String str2, String str3) {
        String str4 = "writePref";
        Logger.enter(getClass().getSimpleName(), str4);
        Editor edit = this.con.getSharedPreferences(str, 0).edit();
        edit.putString(str2, str3);
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str4);
    }

    public void writePrefEncrypted(String str, String str2, String str3) {
        String str4 = "writePrefEncrypted";
        Logger.enter(getClass().getSimpleName(), str4);
        if (str3 != null) {
            str3 = this.encrypter.encrypt(str3);
        }
        writePref(str, str2, str3);
        Logger.exit(getClass().getSimpleName(), str4);
    }

    public void writeWLPref(String str, String str2) {
        String str3 = "writeWLPref";
        Logger.enter(getClass().getSimpleName(), str3);
        writePref("WLPrefs", str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public void writeWLPrefEncrypted(String str, String str2) {
        String str3 = "writeWLPrefEncrypted";
        Logger.enter(getClass().getSimpleName(), str3);
        writePrefEncrypted("WLPrefs", str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public void writeSecurityPref(String str, String str2) {
        String str3 = "writeSecurityPref";
        Logger.enter(getClass().getSimpleName(), str3);
        writePrefEncrypted(SECURITY_PREFS, str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public void writeSecurityTokenPref(String str, String str2) {
        String str3 = "writeSecurityTokenPref";
        Logger.enter(getClass().getSimpleName(), str3);
        writePrefEncrypted(SECURITY_TOKEN_PREFS, str, str2);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    public String readPref(String str, String str2) {
        String str3 = "readPref";
        Logger.enter(getClass().getSimpleName(), str3);
        SharedPreferences sharedPreferences = this.con.getSharedPreferences(str, 0);
        Logger.exit(getClass().getSimpleName(), str3);
        return sharedPreferences.getString(str2, null);
    }

    public String readPrefEncrypted(String str, String str2) {
        String str3 = "readPrefEncrypted";
        Logger.enter(getClass().getSimpleName(), str3);
        String readPref = readPref(str, str2);
        if (readPref != null) {
            Logger.exit(getClass().getSimpleName(), str3);
            return this.encrypter.decrypt(readPref);
        }
        Logger.exit(getClass().getSimpleName(), str3);
        return readPref;
    }

    public String readWLPref(String str) {
        String str2 = "readWLPref";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return readPref("WLPrefs", str);
    }

    public String readWLPrefEncrypted(String str) {
        String str2 = "readWLPrefEncrypted";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return readPrefEncrypted("WLPrefs", str);
    }

    public String readSecurityPref(String str) {
        String str2 = "readSecurityPref";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return readPrefEncrypted(SECURITY_PREFS, str);
    }

    public String readSecurityTokenPref(String str) {
        String str2 = "readSecurityTokenPref";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return readPrefEncrypted(SECURITY_TOKEN_PREFS, str);
    }

    public void writeLongWLPref(String str, long j) {
        String str2 = "writeLongWLPref";
        Logger.enter(getClass().getSimpleName(), str2);
        Editor edit = this.prefs.edit();
        edit.putLong(str, j);
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public long readLongWLPref(String str) {
        String str2 = "readLongWLPref";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return this.prefs.getLong(str, 0);
    }

    public void clearWLPref() {
        String str = "clearWLPref";
        Logger.enter(getClass().getSimpleName(), str);
        Editor edit = this.prefs.edit();
        edit.clear();
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void clearSecurityTokenPref() {
        String str = "clearSecurityTokenPref";
        Logger.enter(getClass().getSimpleName(), str);
        Editor edit = this.con.getSharedPreferences(SECURITY_TOKEN_PREFS, 0).edit();
        edit.clear();
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public void removeSecurityTokenPref(String str) {
        String str2 = "removeSecurityTokenPref";
        Logger.enter(getClass().getSimpleName(), str2);
        Editor edit = this.con.getSharedPreferences(SECURITY_TOKEN_PREFS, 0).edit();
        edit.remove(str);
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void setExternalWebResourcesPref(boolean z) {
        String str = "setExternalWebResourcesPref";
        Logger.enter(getClass().getSimpleName(), str);
        writeWLPref(WL_IS_EXTERNAL_WEBRESOURCES, Boolean.toString(z));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isShareUserCert() {
        String str = "isShareUserCert";
        Logger.enter(getClass().getSimpleName(), str);
        String property = this.wlProperties.getProperty(WL_SHARE_USER_CERT);
        Logger.exit(getClass().getSimpleName(), str);
        return Boolean.valueOf(property).booleanValue();
    }

    public List<String> getShareCookies() {
        String str = "getShareCookies";
        Logger.enter(getClass().getSimpleName(), str);
        String property = this.wlProperties.getProperty(WL_SHARE_COOKIES);
        if (property == null) {
            property = "";
        }
        Logger.exit(getClass().getSimpleName(), str);
        return Arrays.asList(property.split(","));
    }

    public boolean isExternalWebResources() {
        String str = "isExternalWebResources";
        Logger.enter(getClass().getSimpleName(), str);
        String readWLPref = readWLPref(WL_IS_EXTERNAL_WEBRESOURCES);
        Logger.exit(getClass().getSimpleName(), str);
        return Boolean.valueOf(readWLPref).booleanValue();
    }

    public void setClearCacheNextLoadPref(boolean z) {
        String str = "setClearCacheNextLoadPref";
        Logger.enter(getClass().getSimpleName(), str);
        Editor edit = this.prefs.edit();
        edit.putBoolean(CLEAR_CACHE_NEXT_LOAD, z);
        edit.commit();
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isClearCacheNextLoad() {
        String str = "isClearCacheNextLoad";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.prefs.getBoolean(CLEAR_CACHE_NEXT_LOAD, false);
    }

    public String getResourceChecksumPref() {
        String str = "getResourceChecksumPref";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return readWLPref(WL_RESOURCES_CHECKSUM_PREF_KEY);
    }

    public void setResourceChecksumPref(String str) {
        String str2 = "setResourceChecksumPref";
        Logger.enter(getClass().getSimpleName(), str2);
        writeWLPref(WL_RESOURCES_CHECKSUM_PREF_KEY, str);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void setServerUrl(String str) {
        String str2 = "setServerUrl";
        Logger.enter(getClass().getSimpleName(), str2);
        useCustomServerUrl(true);
        writeWLPref(WL_SERVER_URL, str);
        Logger.exit(getClass().getSimpleName(), str2);
    }

    public void useCustomServerUrl(boolean z) {
        String simpleName = getClass().getSimpleName();
        String str = USE_CUSTOM_SERVER_URL;
        Logger.enter(simpleName, str);
        writeWLPref(str, Boolean.toString(z));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean shouldUseCustomServerUrl() {
        String str = "shouldUseCustomServerUrl";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return Boolean.valueOf(this.prefs.getString(USE_CUSTOM_SERVER_URL, null)).booleanValue();
    }

    public URL getAppURL() {
        String str = "getAppURL";
        Logger.enter(getClass().getSimpleName(), str);
        try {
            Logger.exit(getClass().getSimpleName(), str);
            StringBuilder sb = new StringBuilder();
            sb.append(getRootURL());
            sb.append("/apps/services/api/");
            sb.append(getAppId());
            sb.append("/android/");
            return new URL(sb.toString());
        } catch (MalformedURLException e) {
            Logger.exit(getClass().getSimpleName(), str);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Could not parse URL; check assets/mfpclient.properties. ");
            sb2.append(e.getMessage());
            throw new RuntimeException(sb2.toString(), e);
        }
    }

    public String getAppId() {
        String str = "getAppId";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return getPropertyOrPref(WL_APP_ID, APP_ID_PREF_KEY);
    }

    public String getDirectUpdatePublicKey() {
        String str = "getDirectUpdatePublicKey";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_DIRECT_UPDATE_PUBLIC_KEY, "");
    }

    public String getApplicationVersion() {
        String str = "getApplicationVersion";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return getPropertyOrPref(WL_APP_VERSION, APP_ID_PREF_KEY);
    }

    public void setDefaultRootUrlPref() {
        String str = "setDefaultRootUrlPref";
        Logger.enter(getClass().getSimpleName(), str);
        writeWLPref(WL_DEFAULT_SERVER_URL, getDefaultRootUrl());
        Logger.exit(getClass().getSimpleName(), str);
    }

    /* JADX WARNING: Removed duplicated region for block: B:24:0x00b9  */
    private String getDefaultRootUrl() {
        boolean z;
        String str;
        String str2;
        String format;
        String str3 = "getDefaultRootUrl";
        Logger.enter(getClass().getSimpleName(), str3);
        String property = this.wlProperties.getProperty(WL_SERVER_URI);
        if (property == null || property.isEmpty()) {
            if ("https".equalsIgnoreCase(getDefaultProtocol())) {
                if ("443".equals(getDefaultPort())) {
                    z = true;
                    String str4 = "";
                    if (!WLUtils.isStringEmpty(getDefaultPort()) || z) {
                        str = str4;
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(":");
                        sb.append(getDefaultPort());
                        str = sb.toString();
                    }
                    str2 = "/";
                    if (!WLUtils.isStringEmpty(getDefaultProtocol()) && !getDefaultServerContext().equals(str2)) {
                        str4 = getDefaultServerContext();
                    }
                    format = String.format("%s://%s%s%sapi/", new Object[]{getDefaultProtocol(), getDefaultHost(), str, str4});
                    if (format.endsWith(str2)) {
                        format = format.substring(0, format.length() - 1);
                    }
                    Logger.exit(getClass().getSimpleName(), str3);
                    return format;
                }
            }
            z = false;
            String str42 = "";
            if (!WLUtils.isStringEmpty(getDefaultPort())) {
            }
            str = str42;
            str2 = "/";
            str42 = getDefaultServerContext();
            format = String.format("%s://%s%s%sapi/", new Object[]{getDefaultProtocol(), getDefaultHost(), str, str42});
            if (format.endsWith(str2)) {
            }
            Logger.exit(getClass().getSimpleName(), str3);
            return format;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(property);
        sb2.append("api");
        String sb3 = sb2.toString();
        Logger.exit(getClass().getSimpleName(), str3);
        return sb3;
    }

    public String getRootURL() {
        String str = "getRootURL";
        Logger.enter(getClass().getSimpleName(), str);
        if (!shouldUseCustomServerUrl()) {
            Logger.exit(getClass().getSimpleName(), str);
            return getDefaultRootUrl();
        }
        String string = this.prefs.getString(WL_SERVER_URL, null);
        if (string == null) {
            Logger.exit(getClass().getSimpleName(), str);
            return getDefaultRootUrl();
        }
        Logger.exit(getClass().getSimpleName(), str);
        return string;
    }

    public String[] getMediaExtensions() {
        String str = "getMediaExtensions";
        Logger.enter(getClass().getSimpleName(), str);
        String property = this.wlProperties.getProperty(IGNORED_FILE_EXTENSIONS);
        if (property != null) {
            String[] split = property.replaceAll(" ", "").split(",");
            Logger.exit(getClass().getSimpleName(), str);
            return split;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return null;
    }

    public String getWlGenerateDeviceIdStrong() {
        String str = "getWlGenerateDeviceIdStrong";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_DEVICE_ID_STRENGTH);
    }

    public String getTestWebResourcesChecksumFlag() {
        String str = "getTestWebResourcesChecksumFlag";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_TEST_WEB_RESOURCES_CHECKSUM);
    }

    public String getProtocol() {
        String str = "getProtocol";
        Logger.enter(getClass().getSimpleName(), str);
        String rootURL = getRootURL();
        String str2 = WL_SERVER_PROTOCOL;
        if (rootURL != null) {
            try {
                URL url = new URL(rootURL);
                Logger.exit(getClass().getSimpleName(), str);
                return url.getProtocol();
            } catch (MalformedURLException unused) {
                Logger.exit(getClass().getSimpleName(), str);
                return this.wlProperties.getProperty(str2).trim();
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str);
            return this.wlProperties.getProperty(str2).trim();
        }
    }

    private String getDefaultProtocol() {
        String str = "getDefaultProtocol";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_SERVER_PROTOCOL).trim();
    }

    public String getHost() {
        String str = "getHost";
        Logger.enter(getClass().getSimpleName(), str);
        String rootURL = getRootURL();
        String str2 = WL_SERVER_HOST;
        if (rootURL != null) {
            try {
                URL url = new URL(rootURL);
                Logger.exit(getClass().getSimpleName(), str);
                return url.getHost();
            } catch (MalformedURLException unused) {
                Logger.exit(getClass().getSimpleName(), str);
                return this.wlProperties.getProperty(str2).trim();
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str);
            return this.wlProperties.getProperty(str2).trim();
        }
    }

    public boolean isRefreshTokenEnabled() {
        String str = "isRefreshTokenEnabled";
        Logger.enter(getClass().getSimpleName(), str);
        String property = this.wlProperties.getProperty(WL_ENABLE_REFRESH_TOKEN);
        if (property == null || !property.trim().equalsIgnoreCase("true")) {
            Logger.exit(getClass().getSimpleName(), str);
            return false;
        }
        Logger.exit(getClass().getSimpleName(), str);
        return true;
    }

    private String getDefaultHost() {
        String str = "getDefaultHost";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_SERVER_HOST).trim();
    }

    public String getPort() {
        String str = "getPort";
        Logger.enter(getClass().getSimpleName(), str);
        String rootURL = getRootURL();
        String str2 = WL_SERVER_PORT;
        if (rootURL != null) {
            try {
                URL url = new URL(rootURL);
                Logger.exit(getClass().getSimpleName(), str);
                StringBuilder sb = new StringBuilder();
                sb.append("");
                sb.append(url.getPort());
                return sb.toString();
            } catch (MalformedURLException unused) {
                Logger.exit(getClass().getSimpleName(), str);
                return this.wlProperties.getProperty(str2).trim();
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str);
            return this.wlProperties.getProperty(str2).trim();
        }
    }

    private String getDefaultPort() {
        String str = "getDefaultPort";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_SERVER_PORT).trim();
    }

    public String getServerContext() {
        String str = "getServerContext";
        Logger.enter(getClass().getSimpleName(), str);
        String rootURL = getRootURL();
        String property = this.wlProperties.getProperty(WL_SERVER_URI);
        String str2 = WL_SERVER_CONTEXT;
        if (property != null && !property.isEmpty()) {
            String property2 = this.wlProperties.getProperty(str2);
            Logger.exit(getClass().getSimpleName(), str);
            StringBuilder sb = new StringBuilder();
            sb.append(property2);
            sb.append("api");
            return sb.toString();
        } else if (rootURL != null) {
            try {
                URL url = new URL(rootURL);
                Logger.exit(getClass().getSimpleName(), str);
                return url.getPath();
            } catch (MalformedURLException unused) {
                Logger.exit(getClass().getSimpleName(), str);
                return this.wlProperties.getProperty(str2).trim();
            }
        } else {
            Logger.exit(getClass().getSimpleName(), str);
            return this.wlProperties.getProperty(str2).trim();
        }
    }

    private String getDefaultServerContext() {
        String str = "getDefaultServerContext";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_SERVER_CONTEXT).trim();
    }

    private String getPropertyOrPref(String str, String str2) {
        String str3 = "getPropertyOrPref";
        Logger.enter(getClass().getSimpleName(), str3);
        String string = this.prefs.getString(str2, null);
        if (string == null) {
            string = (String) this.wlProperties.get(str);
        }
        Logger.exit(getClass().getSimpleName(), str3);
        return string;
    }

    public String getMainFilePath() {
        String str = "getMainFilePath";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append(getAppId());
        sb.append(".html");
        return sb.toString();
    }

    public String getWebResourcesUnpackedSize() {
        String str = "getWebResourcesUnpackedSize";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_WEB_RESOURCES_UNPACKD_SIZE);
    }

    public String getPlatformVersion() {
        String str = "getPlatformVersion";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(WL_PLATFORM_VERSION);
    }

    public String getMainFileFromDescriptor() {
        String str = "getMainFileFromDescriptor";
        Logger.enter(getClass().getSimpleName(), str);
        String str2 = "";
        if (this.mainFile.equals(str2)) {
            try {
                XmlResourceParser xml = this.con.getResources().getXml(this.con.getResources().getIdentifier("config", "xml", this.con.getPackageName()));
                xml.next();
                for (int eventType = xml.getEventType(); eventType != 1; eventType = xml.next()) {
                    if (eventType == 2 && xml.getName().equals("content")) {
                        this.mainFile = xml.getAttributeValue(null, SecurityUtils.ENCRYPTION_SOURCE_LABEL);
                    }
                }
                if (this.mainFile.equals(str2)) {
                    this.mainFile = "index.html";
                }
            } catch (Exception e) {
                e.getMessage();
                e.printStackTrace();
            }
        }
        Logger.exit(getClass().getSimpleName(), str);
        return this.mainFile;
    }

    public String getLanguagePreferences() {
        String str = "getLanguagePreferences";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.wlProperties.getProperty(LANGUAGE_PREFS);
    }

    public boolean isShouldTestWebResourcesChecksum() {
        String str = "isShouldTestWebResourcesChecksum";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return getTestWebResourcesChecksumFlag().equals("true");
    }

    public long getApplicationInstallTime() {
        String str = "getApplicationInstallTime";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return readLongWLPref(APP_INSTALL_TIME_KEY);
    }

    public void setApplicationInstallTime(long j) {
        String str = "setApplicationInstallTime";
        Logger.enter(getClass().getSimpleName(), str);
        writeLongWLPref(APP_INSTALL_TIME_KEY, j);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public String getApplicationAbsolutePathToExternalAppFiles() {
        String str = "getApplicationAbsolutePathToExternalAppFiles";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.absolutePathToExternalAppFiles;
    }

    public String getApplicationAbsolutePathToExternalWWWFiles() {
        String str = "getApplicationAbsolutePathToExternalWWWFiles";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append(this.absolutePathToExternalAppFiles);
        sb.append("/www");
        return sb.toString();
    }

    public String getApplicationOldAbsolutePathToExternalWWWFiles() {
        String str = "getApplicationOldAbsolutePathToExternalWWWFiles";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append(this.oldAbsolutePathToExternalAppFiles);
        sb.append("/www");
        return sb.toString();
    }

    public String getLocalStorageRoot() {
        String str = "getLocalStorageRoot";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return isExternalWebResources() ? getApplicationAbsolutePathToExternalAppFiles() : ANDROID_ASSET;
    }

    public String getWebResourcesUrl() {
        String str = "getWebResourcesUrl";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append(getLocalStorageRoot());
        sb.append("/www");
        return sb.toString();
    }

    public String getWebUrl() {
        String str = "getWebUrl";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append("file://");
        sb.append(getWebResourcesUrl());
        return sb.toString();
    }

    public boolean isHybridActivityInForeground() {
        String str = "isHybridActivityInForeground";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.isApplicationForeground;
    }

    public void setHybridActivityInForeground(boolean z) {
        String str = "setHybridActivityInForeground";
        Logger.enter(getClass().getSimpleName(), str);
        this.isApplicationForeground = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isInitComplete() {
        String str = "isInitComplete";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.isInitComplete;
    }

    public void setInitComplete(boolean z) {
        String str = "setInitComplete";
        Logger.enter(getClass().getSimpleName(), str);
        this.isInitComplete = z;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public long getInProgressChecksumPref() {
        String str = "getInProgressChecksumPref";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return readLongWLPref(IN_PROGRESS_CHECKSUM);
    }

    public void setInProgressChecksumPref(long j) {
        String str = "setInProgressChecksumPref";
        Logger.enter(getClass().getSimpleName(), str);
        writeLongWLPref(IN_PROGRESS_CHECKSUM, j);
        Logger.exit(getClass().getSimpleName(), str);
    }

    public String getPackageName() {
        String str = "getPackageName";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return this.con.getPackageName();
    }

    public String getAppWebUrl() {
        String str = "getAppWebUrl";
        Logger.enter(getClass().getSimpleName(), str);
        String mainFileFromDescriptor = getMainFileFromDescriptor();
        if (mainFileFromDescriptor.startsWith("http:") || mainFileFromDescriptor.startsWith("https:")) {
            Logger.exit(getClass().getSimpleName(), str);
            return mainFileFromDescriptor;
        }
        Logger.exit(getClass().getSimpleName(), str);
        StringBuilder sb = new StringBuilder();
        sb.append(getWebUrl());
        sb.append("/");
        sb.append(mainFileFromDescriptor);
        return sb.toString();
    }

    public boolean isShowAllNotificationsInTray() {
        String str = "isShowAllNotificationsInTray";
        Logger.enter(getClass().getSimpleName(), str);
        String property = this.wlProperties.getProperty(WL_SHOW_ALL_NOTIFICATIONS_IN_TRAY, "false");
        Logger.exit(getClass().getSimpleName(), str);
        return Boolean.valueOf(property).booleanValue();
    }

    public JSONObject getApplicationData() throws JSONException {
        String str = "getApplicationData";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(APP_ID, singleton.getPackageName());
        jSONObject.put(CLIENT_PLATFORM, "android");
        jSONObject.put(VERSION, singleton.getApplicationVersion());
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject;
    }

    public JSONObject getClientData() throws JSONException {
        String str = "getClientData";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject deviceData = WLDeviceAuthManager.getInstance().getDeviceData();
        deviceData.put(APP_ID, singleton.getPackageName());
        deviceData.put(CLIENT_PLATFORM, "android");
        deviceData.put(VERSION, singleton.getApplicationVersion());
        deviceData.put(SDK_PROTOCOL_VERSION_KEY, getSDKProtocolVersion());
        Logger.exit(getClass().getSimpleName(), str);
        return deviceData;
    }

    public JSONObject getRegistrationAttributesData() throws JSONException {
        String str = "getRegistrationAttributesData";
        Logger.enter(getClass().getSimpleName(), str);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put(SDK_PROTOCOL_VERSION_KEY, getSDKProtocolVersion());
        Logger.exit(getClass().getSimpleName(), str);
        return jSONObject;
    }

    public void setIsEncrypted(boolean z) {
        String str = "setIsEncrypted";
        Logger.enter(getClass().getSimpleName(), str);
        writeWLPref(WL_IS_ENCRYPTED, Boolean.toString(z));
        Logger.exit(getClass().getSimpleName(), str);
    }

    public boolean isEncrypted() {
        String str = "isEncrypted";
        Logger.enter(getClass().getSimpleName(), str);
        String readWLPref = readWLPref(WL_IS_ENCRYPTED);
        Logger.exit(getClass().getSimpleName(), str);
        return Boolean.valueOf(readWLPref).booleanValue();
    }

    public void setServerRelativeTime(long j) {
        String str = "setServerRelativeTime";
        Logger.enter(getClass().getSimpleName(), str);
        this.serverRelativeTime = j;
        Logger.exit(getClass().getSimpleName(), str);
    }

    public long getCurrentWithRelativeTime() {
        String str = "getCurrentWithRelativeTime";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return System.currentTimeMillis() + this.serverRelativeTime;
    }

    private int getSDKProtocolVersion() {
        String str = "getSDKProtocolVersion";
        Logger.enter(getClass().getSimpleName(), str);
        Logger.exit(getClass().getSimpleName(), str);
        return 1;
    }
}

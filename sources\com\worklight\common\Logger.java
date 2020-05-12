package com.worklight.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.ItemTouchHelper.Callback;
import ca.albertahealthservices.contacttracing.BuildConfig;
import com.google.common.net.HttpHeaders;
import com.worklight.common.internal.JULHandler;
import com.worklight.common.internal.LoggerServerConfigProcessor;
import com.worklight.nativeandroid.common.WLUtils;
import com.worklight.wlclient.RequestMethod;
import com.worklight.wlclient.WLRequest;
import com.worklight.wlclient.WLRequest.RequestPaths;
import com.worklight.wlclient.WLRequestListener;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLErrorCode;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLRequestOptions;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.auth.WLAuthorizationManagerInternal;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class Logger {
    public static final String ANALYTICS_FILENAME = "analytics.log";
    private static final String CONTEXT_NULL_MSG;
    public static final boolean DEFAULT_analyticsCapture = true;
    public static final boolean DEFAULT_capture = true;
    protected static final int DEFAULT_logFileMaxSize = 100000;
    public static final boolean DEFAULT_showLogsInConsole = true;
    public static final String FILENAME = "wl.log";
    private static final String FUNCTION = "function";
    /* access modifiers changed from: private */
    public static final String LOG_TAG;
    public static final int MAX_NUM_LOG_FILES = 2;
    private static final String NETWORK = "network";
    public static final String SHARED_PREF_KEY;
    public static final String SHARED_PREF_KEY_CRASH_DETECTED = "crashDetected";
    protected static final String SHARED_PREF_KEY_autoSendLogs = "autoSendLogs";
    public static final String SHARED_PREF_KEY_filters = "filters";
    public static final String SHARED_PREF_KEY_filters_from_server = "filtersFromServer";
    public static final String SHARED_PREF_KEY_level = "level";
    public static final String SHARED_PREF_KEY_level_from_server = "levelFromServer";
    protected static final String SHARED_PREF_KEY_logFileMaxSize = "logFileMaxSize";
    public static final String SHARED_PREF_KEY_logPersistence = "logPersistence";
    public static final String SHARED_PREF_KEY_logPersistence_from_server = "logPersistenceFromServer";
    protected static final String SHARED_PREF_KEY_timerAutoSendLogs = "timerAutoSendLogs";
    private static final ThreadPoolExecutor ThreadPoolWorkQueue;
    public static final Object WAIT_LOCK = new Object();
    private static Boolean analyticsCapture = null;
    /* access modifiers changed from: private */
    public static Boolean autoSendLogs;
    private static Timer autoTriggerTimer;
    private static Boolean capture = null;
    /* access modifiers changed from: private */
    public static Context context;
    private static boolean context_null_msg_already_printed = false;
    private static boolean entered = false;
    private static FileLoggerInterface fileLoggerInstance;
    private static HashMap<String, LEVEL> filters = new HashMap<>();
    private static WeakHashMap<String, Logger> instances = new WeakHashMap<>();
    private static int intervalSeconds = 60;
    private static boolean isStart = true;
    private static JULHandler julHandler = new JULHandler();
    private static LEVEL level = null;
    private static final Object lockObject = new Object();
    private static Integer logFileMaxSize = null;
    private static StringBuilder message = new StringBuilder();
    /* access modifiers changed from: private */
    public static boolean mfpTrace = false;
    /* access modifiers changed from: private */
    public static boolean mfpTraceAnalytics = false;
    /* access modifiers changed from: private */
    public static boolean mfpTraceNetwork = false;
    private static StringBuilder networkJson = new StringBuilder();
    private static StringBuilder networkJsonBuffer = new StringBuilder();
    private static Logger networkTraceLogger = getInstance("MFP_TRACE_NETWORK");
    private static int numberOfEntries = 0;
    private static int numberOfIndents = 0;
    private static long priorAutoTriggerTime = 0;
    private static boolean sendingAnalyticsLogs = false;
    private static boolean sendingLogs = false;
    private static Boolean showLogsInConsole = null;
    private static StringBuilder stringJson = new StringBuilder();
    private static StringBuilder stringJsonBuffer = new StringBuilder();
    private static Timer timer;
    private static Logger traceLogger = getInstance("MFP_TRACE");
    private static UncaughtExceptionHandler uncaughtExceptionHandler = null;
    /* access modifiers changed from: private */
    public final String tag;

    /* renamed from: com.worklight.common.Logger$9 reason: invalid class name */
    static /* synthetic */ class AnonymousClass9 {
        static final /* synthetic */ int[] $SwitchMap$com$worklight$common$Logger$LEVEL;

        /* JADX WARNING: Can't wrap try/catch for region: R(14:0|1|2|3|4|5|6|7|8|9|10|11|12|(3:13|14|16)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(16:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|16) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x003e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0049 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0012 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001d */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x0028 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0033 */
        static {
            int[] iArr = new int[LEVEL.values().length];
            $SwitchMap$com$worklight$common$Logger$LEVEL = iArr;
            iArr[LEVEL.FATAL.ordinal()] = 1;
            $SwitchMap$com$worklight$common$Logger$LEVEL[LEVEL.ERROR.ordinal()] = 2;
            $SwitchMap$com$worklight$common$Logger$LEVEL[LEVEL.WARN.ordinal()] = 3;
            $SwitchMap$com$worklight$common$Logger$LEVEL[LEVEL.INFO.ordinal()] = 4;
            $SwitchMap$com$worklight$common$Logger$LEVEL[LEVEL.LOG.ordinal()] = 5;
            $SwitchMap$com$worklight$common$Logger$LEVEL[LEVEL.DEBUG.ordinal()] = 6;
            try {
                $SwitchMap$com$worklight$common$Logger$LEVEL[LEVEL.TRACE.ordinal()] = 7;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private static class DoLogRunnable implements Runnable {
        private JSONObject additionalMetadata;
        private LEVEL calledLevel;
        private Logger logger;
        private String message;
        private Throwable t;
        private long timestamp;

        public DoLogRunnable(LEVEL level, String str, long j, JSONObject jSONObject, Throwable th, Logger logger2) {
            this.calledLevel = level;
            this.message = str;
            this.timestamp = j;
            this.additionalMetadata = jSONObject;
            this.t = th;
            this.logger = logger2;
        }

        private String removeMetatData(String str) {
            String str2 = ">";
            String str3 = "<";
            String str4 = str.contains(str2) ? str.substring(str.indexOf(str2), str.length()) : str.contains(str3) ? str.substring(str.indexOf(str3), str.length()) : str;
            return (str.contains(str2) || str.contains(str3)) ? str4 : str;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:11:0x003b, code lost:
            if (r0.isLoggable() != false) goto L_0x003f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:7:0x0030, code lost:
            if (r9.calledLevel.getLevelValue() <= ((com.worklight.common.Logger.LEVEL) r0.get(com.worklight.common.Logger.access$1000(r9.logger))).getLevelValue()) goto L_0x003f;
         */
        /* JADX WARNING: Removed duplicated region for block: B:19:0x0065  */
        /* JADX WARNING: Removed duplicated region for block: B:22:0x0077  */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x0132 A[SYNTHETIC] */
        public void run() {
            String str;
            HashMap filters = Logger.getFilters();
            boolean z = true;
            boolean z2 = false;
            if (filters == null || filters.size() <= 0) {
                LEVEL level = this.calledLevel;
                if (level != null) {
                }
            } else {
                if (filters.containsKey(this.logger.tag)) {
                }
                if (z2 || this.calledLevel == LEVEL.ANALYTICS) {
                    Logger.captureToFile(Logger.createJSONObject(this.calledLevel, this.logger.tag, this.message, this.timestamp, this.additionalMetadata, this.t), this.calledLevel);
                    str = this.message;
                    if (str == null) {
                        str = "(null)";
                    }
                    this.message = str;
                    this.message = Logger.prependMetadata(str, this.additionalMetadata);
                    if (Logger.getShowLogsInConsole()) {
                        switch (AnonymousClass9.$SwitchMap$com$worklight$common$Logger$LEVEL[this.calledLevel.ordinal()]) {
                            case 1:
                            case 2:
                                if (this.t != null) {
                                    Log.e(this.logger.tag, this.message, this.t);
                                    break;
                                } else {
                                    Log.e(this.logger.tag, this.message);
                                    break;
                                }
                            case 3:
                                if (this.t != null) {
                                    Log.w(this.logger.tag, this.message, this.t);
                                    break;
                                } else {
                                    Log.w(this.logger.tag, this.message);
                                    break;
                                }
                            case 4:
                                if (this.t != null) {
                                    Log.i(this.logger.tag, this.message, this.t);
                                    break;
                                } else {
                                    Log.i(this.logger.tag, this.message);
                                    break;
                                }
                            case 5:
                                if (this.t != null) {
                                    Log.v(this.logger.tag, this.message, this.t);
                                    break;
                                } else {
                                    Log.v(this.logger.tag, this.message);
                                    break;
                                }
                            case 6:
                            case 7:
                                if (this.logger.tag == "MFP_TRACE") {
                                    this.message = removeMetatData(this.message);
                                }
                                if (this.t != null) {
                                    Log.d(this.logger.tag, this.message, this.t);
                                    break;
                                } else {
                                    Log.d(this.logger.tag, this.message);
                                    break;
                                }
                        }
                    }
                }
                synchronized (this.logger) {
                    this.logger.notifyAll();
                }
            }
            z = false;
            z2 = z;
            Logger.captureToFile(Logger.createJSONObject(this.calledLevel, this.logger.tag, this.message, this.timestamp, this.additionalMetadata, this.t), this.calledLevel);
            str = this.message;
            if (str == null) {
            }
            this.message = str;
            this.message = Logger.prependMetadata(str, this.additionalMetadata);
            if (Logger.getShowLogsInConsole()) {
            }
            synchronized (this.logger) {
            }
        }
    }

    private static class FileLogger extends FileLoggerInterface {
        private static String filePath;
        private static ClientLogFormatter formatter;
        private static FileLogger noopSingleton = new FileLogger(null, null);
        private static FileLogger singleton;

        private static class ClientLogFormatter extends Formatter {
            private ClientLogFormatter() {
            }

            public String format(LogRecord logRecord) {
                return logRecord.getMessage();
            }
        }

        private FileLogger(String str, String str2) {
            super(str, str2);
        }

        public static FileLogger getInstance() {
            if (singleton != null || Logger.context == null) {
                return noopSingleton;
            }
            singleton = new FileLogger(null, null);
            StringBuilder sb = new StringBuilder();
            sb.append(WLUtils.getNoBackupFilesDir(Logger.context));
            sb.append(System.getProperty("file.separator"));
            sb.append(Logger.FILENAME);
            filePath = sb.toString();
            formatter = new ClientLogFormatter();
            singleton.setLevel(Level.ALL);
            return singleton;
        }

        public synchronized void log(JSONObject jSONObject, String str) throws SecurityException, IOException {
            if (singleton != null) {
                StringBuilder sb = new StringBuilder();
                sb.append(WLUtils.getNoBackupFilesDir(Logger.context));
                sb.append(System.getProperty("file.separator"));
                sb.append(str);
                filePath = sb.toString();
                FileHandler fileHandler = new FileHandler(filePath, Logger.getMaxFileSize(), 2, true);
                fileHandler.setFormatter(formatter);
                singleton.addHandler(fileHandler);
                FileLogger fileLogger = singleton;
                Level level = Level.FINEST;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(jSONObject.toString());
                sb2.append(",");
                fileLogger.log(level, sb2.toString());
                singleton.getHandlers()[0].close();
                singleton.removeHandler(fileHandler);
            }
        }

        public byte[] getFileContentsAsByteArray(File file) throws UnsupportedEncodingException {
            return getByteArrayFromFile(file.getName());
        }

        private byte[] getByteArrayFromFile(String str) throws UnsupportedEncodingException {
            File file = new File(WLUtils.getNoBackupFilesDir(Logger.context), str);
            if (file.exists()) {
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) file.length());
                    copyStream(fileInputStream, byteArrayOutputStream);
                    return byteArrayOutputStream.toByteArray();
                } catch (IOException e) {
                    String access$300 = Logger.LOG_TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("problem reading file ");
                    sb.append(file.toString());
                    Log.e(access$300, sb.toString(), e);
                }
            }
            return "".getBytes("UTF-8");
        }

        private static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
            byte[] bArr = new byte[1024];
            while (true) {
                int read = inputStream.read(bArr, 0, 1024);
                if (read == -1) {
                    inputStream.close();
                    return;
                }
                outputStream.write(bArr, 0, read);
            }
        }
    }

    protected static abstract class FileLoggerInterface extends java.util.logging.Logger {
        public abstract byte[] getFileContentsAsByteArray(File file) throws UnsupportedEncodingException;

        public abstract void log(JSONObject jSONObject, String str) throws SecurityException, IOException;

        protected FileLoggerInterface(String str, String str2) {
            super(str, str2);
        }
    }

    public enum LEVEL {
        ANALYTICS {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 25;
            }
        },
        FATAL {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 50;
            }
        },
        ERROR {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 100;
            }
        },
        WARN {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return Callback.DEFAULT_DRAG_ANIMATION_DURATION;
            }
        },
        INFO {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 300;
            }
        },
        LOG {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 400;
            }
        },
        DEBUG {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 500;
            }
        },
        TRACE {
            /* access modifiers changed from: protected */
            public int getLevelValue() {
                return 600;
            }
        };

        /* access modifiers changed from: protected */
        public abstract int getLevelValue();

        /* access modifiers changed from: protected */
        public boolean isLoggable() {
            LEVEL level = Logger.getLevel();
            return level != null && level.getLevelValue() >= getLevelValue();
        }

        public static LEVEL fromString(String str) {
            try {
                return valueOf(str.toUpperCase());
            } catch (Exception unused) {
                return null;
            }
        }
    }

    static class SendLogsRequestListener implements WLRequestListener {
        private static final Logger logger = Logger.getInstance(SendLogsRequestListener.class.getName());
        private final File file;
        private boolean isAnalyticsRequest = false;
        private WLRequestListener userDefinedListener;

        public SendLogsRequestListener(File file2, WLRequestListener wLRequestListener, boolean z) {
            this.file = file2;
            this.userDefinedListener = wLRequestListener;
            this.isAnalyticsRequest = z;
        }

        public SendLogsRequestListener(WLRequestListener wLRequestListener) {
            this.userDefinedListener = wLRequestListener;
            this.file = null;
        }

        public void onSuccess(WLResponse wLResponse) {
            if (Logger.mfpTraceAnalytics) {
                if (wLResponse.getStatus() == 201) {
                    Logger logger2 = logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Successfully POSTed log data to URL loguploader.  HTTP response code: ");
                    sb.append(wLResponse.getStatus());
                    logger2.trace(sb.toString());
                    WLRequestListener wLRequestListener = this.userDefinedListener;
                    if (wLRequestListener != null) {
                        wLRequestListener.onSuccess(wLResponse);
                    }
                } else {
                    Logger logger3 = logger;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Failed to POST data due to: HTTP response code: ");
                    sb2.append(wLResponse.getStatus());
                    logger3.error(sb2.toString());
                    WLRequestListener wLRequestListener2 = this.userDefinedListener;
                    if (wLRequestListener2 == null) {
                        wLRequestListener2.onFailure(new WLFailResponse(wLResponse));
                    }
                }
                Logger.context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0).edit().putBoolean(Logger.SHARED_PREF_KEY_CRASH_DETECTED, false).commit();
                return;
            }
            try {
                this.file.delete();
                if (wLResponse.getStatus() == 201) {
                    Logger logger4 = logger;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Successfully POSTed log data from file ");
                    sb3.append(this.file);
                    sb3.append(" to URL ");
                    sb3.append(RequestPaths.UPLOAD_LOGS);
                    sb3.append(".  HTTP response code: ");
                    sb3.append(wLResponse.getStatus());
                    logger4.trace(sb3.toString());
                    if (this.userDefinedListener != null) {
                        this.userDefinedListener.onSuccess(wLResponse);
                    }
                } else {
                    Logger logger5 = logger;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("Failed to POST data from file ");
                    sb4.append(this.file);
                    sb4.append(" due to: HTTP response code: ");
                    sb4.append(wLResponse.getStatus());
                    logger5.error(sb4.toString());
                    if (this.userDefinedListener == null) {
                        this.userDefinedListener.onFailure(new WLFailResponse(wLResponse));
                    }
                }
                Logger.context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0).edit().putBoolean(Logger.SHARED_PREF_KEY_CRASH_DETECTED, false).commit();
                Logger.unlockMutex(this.isAnalyticsRequest);
                synchronized (Logger.WAIT_LOCK) {
                    Logger.WAIT_LOCK.notifyAll();
                }
            } catch (Throwable th) {
                Logger.unlockMutex(this.isAnalyticsRequest);
                synchronized (Logger.WAIT_LOCK) {
                    Logger.WAIT_LOCK.notifyAll();
                    throw th;
                }
            }
        }

        public void onFailure(WLFailResponse wLFailResponse) {
            WLRequestListener wLRequestListener = this.userDefinedListener;
            if (wLRequestListener != null) {
                wLRequestListener.onFailure(wLFailResponse);
            }
            if (!Logger.mfpTraceAnalytics) {
                Logger.unlockMutex(this.isAnalyticsRequest);
                synchronized (Logger.WAIT_LOCK) {
                    Logger.WAIT_LOCK.notifyAll();
                }
            }
        }
    }

    private static class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {
        private final java.lang.Thread.UncaughtExceptionHandler defaultUEH;

        private UncaughtExceptionHandler() {
            this.defaultUEH = Thread.getDefaultUncaughtExceptionHandler();
        }

        public final void uncaughtException(Thread thread, Throwable th) {
            if (Logger.context != null) {
                Logger.context.getSharedPreferences(Logger.SHARED_PREF_KEY, 0).edit().putBoolean(Logger.SHARED_PREF_KEY_CRASH_DETECTED, true).commit();
            }
            Logger.getInstance(getClass().getName()).fatal("Uncaught Exception", new JSONObject(), th);
            WLAnalytics.logAppCrash(th);
            this.defaultUEH.uncaughtException(thread, th);
        }
    }

    static {
        Class<Logger> cls = Logger.class;
        LOG_TAG = cls.getName();
        StringBuilder sb = new StringBuilder();
        sb.append(cls.getName());
        sb.append(".setContext(Context) must be called to fully enable debug log capture.  Currently, the 'capture' flag is set but the 'context' field is not.  This warning will only be printed once.");
        CONTEXT_NULL_MSG = sb.toString();
        SHARED_PREF_KEY = cls.getName();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue(1000));
        ThreadPoolWorkQueue = threadPoolExecutor;
        threadPoolExecutor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                try {
                    threadPoolExecutor.getQueue().put(runnable);
                } catch (InterruptedException unused) {
                }
            }
        });
    }

    private Logger(String str) {
        if (fileLoggerInstance == null) {
            fileLoggerInstance = FileLogger.getInstance();
        }
        this.tag = (str == null || str.trim().equals("")) ? "null" : str.trim();
    }

    protected static void unsetContext() {
        instances.clear();
        context = null;
        capture = null;
        analyticsCapture = null;
        showLogsInConsole = null;
        logFileMaxSize = null;
        level = null;
        filters = null;
        uncaughtExceptionHandler = null;
        fileLoggerInstance = null;
        autoSendLogs = null;
        LogManager.getLogManager().getLogger("").removeHandler(julHandler);
    }

    public static Logger getInstance(String str) {
        Logger logger;
        synchronized (lockObject) {
            logger = (Logger) instances.get(str);
            if (logger == null) {
                logger = new Logger(str);
                instances.put(str, logger);
            }
        }
        return logger;
    }

    public static void enter(String str, String str2) {
        if (!entered) {
            entered = true;
            StringBuilder sb = new StringBuilder();
            numberOfIndents++;
            for (int i = 0; i < numberOfIndents; i++) {
                sb.append(">");
            }
            String str3 = " ";
            sb.append(str3);
            sb.append(str);
            sb.append(".");
            sb.append(str2);
            if (mfpTraceAnalytics) {
                StringBuilder sb2 = message;
                sb2.append(str3);
                sb2.append(sb.toString());
                message = sb2;
                numberOfEntries++;
            }
            if (!mfpTrace) {
                entered = false;
                return;
            }
            traceLogger.trace(sb.toString());
            entered = false;
        }
    }

    public static void exit(String str, String str2) {
        int i;
        if (!entered) {
            entered = true;
            StringBuilder sb = new StringBuilder();
            if (numberOfIndents < 1) {
                numberOfIndents = 1;
            }
            int i2 = 0;
            while (true) {
                i = numberOfIndents;
                if (i2 >= i) {
                    break;
                }
                sb.append("<");
                i2++;
            }
            numberOfIndents = i - 1;
            String str3 = " ";
            sb.append(str3);
            sb.append(str);
            sb.append(".");
            sb.append(str2);
            if (mfpTraceAnalytics) {
                StringBuilder sb2 = message;
                sb2.append(str3);
                sb2.append(sb.toString());
                message = sb2;
                int i3 = numberOfEntries + 1;
                numberOfEntries = i3;
                if (i3 >= 500) {
                    numberOfEntries = 0;
                    try {
                        long longValue = Long.valueOf(String.valueOf(System.currentTimeMillis())).longValue();
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put(NotificationCompat.CATEGORY_MESSAGE, message);
                        message = new StringBuilder();
                        jSONObject.put("timestamp", longValue);
                        jSONObject.put(SHARED_PREF_KEY_level, "TRACE");
                        jSONObject.put("pkg", "MFP_TRACE");
                        StringBuilder sb3 = stringJson;
                        sb3.append(jSONObject.toString());
                        stringJson = sb3;
                        stringJsonBuffer = new StringBuilder(stringJson);
                        stringJson = new StringBuilder();
                        sendTraceLogs(new WLRequestListener() {
                            public void onSuccess(WLResponse wLResponse) {
                                PrintStream printStream = System.out;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Success ");
                                sb.append(wLResponse.toString());
                                printStream.println(sb.toString());
                            }

                            public void onFailure(WLFailResponse wLFailResponse) {
                                PrintStream printStream = System.out;
                                StringBuilder sb = new StringBuilder();
                                sb.append("Failure ");
                                sb.append(wLFailResponse.toString());
                                printStream.println(sb.toString());
                            }
                        }, FUNCTION);
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }
            }
            if (!mfpTrace) {
                entered = false;
                return;
            }
            traceLogger.trace(sb.toString());
            entered = false;
        }
    }

    public static void logNetworkData(String str) {
        if (mfpTraceNetwork) {
            try {
                long longValue = Long.valueOf(String.valueOf(System.currentTimeMillis())).longValue();
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(NotificationCompat.CATEGORY_MESSAGE, str);
                jSONObject.put("timestamp", longValue);
                jSONObject.put(SHARED_PREF_KEY_level, "TRACE");
                jSONObject.put("pkg", "MFP_TRACE_NETWORK");
                StringBuilder sb = networkJson;
                sb.append(jSONObject.toString());
                networkJson = sb;
                networkJsonBuffer = new StringBuilder(networkJson);
                networkJson = new StringBuilder();
                sendTraceLogs(new WLRequestListener() {
                    public void onSuccess(WLResponse wLResponse) {
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Success ");
                        sb.append(wLResponse.toString());
                        printStream.println(sb.toString());
                    }

                    public void onFailure(WLFailResponse wLFailResponse) {
                        PrintStream printStream = System.out;
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failure ");
                        sb.append(wLFailResponse.toString());
                        printStream.println(sb.toString());
                    }
                }, NETWORK);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    public static void setContext(Context context2) {
        String str;
        if (context == null) {
            int i = 0;
            while (true) {
                str = "";
                if (i >= 3) {
                    break;
                }
                try {
                    LogManager.getLogManager().getLogger(str).addHandler(julHandler);
                    break;
                } catch (ConcurrentModificationException e) {
                    Logger instance = getInstance(LOG_TAG);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Logger failed to add handler: ");
                    sb.append(e);
                    instance.error(sb.toString());
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e2) {
                        Logger instance2 = getInstance(LOG_TAG);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Logger failed to sleep: ");
                        sb2.append(e2);
                        instance2.error(sb2.toString());
                    }
                    i++;
                }
            }
            java.util.logging.Logger.getLogger(str).setLevel(Level.ALL);
            context = context2.getApplicationContext();
            FileLoggerInterface fileLoggerInterface = fileLoggerInstance;
            if (fileLoggerInterface == null || (fileLoggerInterface instanceof FileLogger)) {
                fileLoggerInstance = FileLogger.getInstance();
            }
            try {
                WLClient.getInstance();
            } catch (RuntimeException unused) {
                WLClient.createInstance(context);
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
            LEVEL level2 = level;
            if (level2 != null) {
                setLevel(level2);
            } else {
                setLevel(LEVEL.fromString(sharedPreferences.getString(SHARED_PREF_KEY_level, getLevelDefault().toString())));
            }
            Integer num = logFileMaxSize;
            if (num != null) {
                setMaxFileSize(num.intValue());
            } else {
                setMaxFileSize(sharedPreferences.getInt(SHARED_PREF_KEY_logFileMaxSize, DEFAULT_logFileMaxSize));
            }
            Boolean bool = capture;
            String str2 = SHARED_PREF_KEY_logPersistence;
            if (bool != null) {
                setCapture(bool.booleanValue());
            } else {
                setCapture(sharedPreferences.getBoolean(str2, true));
            }
            Boolean bool2 = showLogsInConsole;
            if (bool2 != null) {
                setShowLogsInConsole(bool2.booleanValue());
            } else {
                setShowLogsInConsole(sharedPreferences.getBoolean(str2, true));
            }
            Boolean bool3 = autoSendLogs;
            if (bool3 != null) {
                setAutoSendLogsSync(bool3.booleanValue(), intervalSeconds);
            } else {
                setAutoSendLogsSync(sharedPreferences.getBoolean(SHARED_PREF_KEY_autoSendLogs, true), sharedPreferences.getInt(SHARED_PREF_KEY_timerAutoSendLogs, intervalSeconds));
            }
            HashMap<String, LEVEL> hashMap = filters;
            if (hashMap != null) {
                setFilters(hashMap);
            } else {
                try {
                    setFilters(JSONObjectToHashMap(new JSONObject(sharedPreferences.getString(SHARED_PREF_KEY_filters, "{}"))));
                } catch (JSONException unused2) {
                }
            }
            UncaughtExceptionHandler uncaughtExceptionHandler2 = new UncaughtExceptionHandler();
            uncaughtExceptionHandler = uncaughtExceptionHandler2;
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler2);
        }
    }

    public static void setLevel(LEVEL level2) {
        if (level2 != null) {
            level = level2;
            Context context2 = context;
            if (context2 != null) {
                SharedPreferences sharedPreferences = context2.getSharedPreferences(SHARED_PREF_KEY, 0);
                sharedPreferences.edit().putString(SHARED_PREF_KEY_level, level.toString()).commit();
                level = LEVEL.fromString(sharedPreferences.getString(SHARED_PREF_KEY_level_from_server, level.toString()));
            }
        }
    }

    public static LEVEL getLevel() {
        LEVEL level2 = level;
        if (level2 == null) {
            level2 = getLevelDefault();
        }
        Context context2 = context;
        if (context2 == null) {
            return level2;
        }
        return LEVEL.fromString(context2.getSharedPreferences(SHARED_PREF_KEY, 0).getString(SHARED_PREF_KEY_level_from_server, level2.toString()));
    }

    public static void setCapture(boolean z) {
        capture = Boolean.valueOf(z);
        Context context2 = context;
        if (context2 != null) {
            SharedPreferences sharedPreferences = context2.getSharedPreferences(SHARED_PREF_KEY, 0);
            sharedPreferences.edit().putBoolean(SHARED_PREF_KEY_logPersistence, capture.booleanValue()).commit();
            capture = Boolean.valueOf(sharedPreferences.getBoolean(SHARED_PREF_KEY_logPersistence_from_server, capture.booleanValue()));
        }
    }

    public static void setShowLogsInConsole(boolean z) {
        showLogsInConsole = Boolean.valueOf(z);
        Context context2 = context;
        if (context2 != null) {
            SharedPreferences sharedPreferences = context2.getSharedPreferences(SHARED_PREF_KEY, 0);
            sharedPreferences.edit().putBoolean(SHARED_PREF_KEY_logPersistence, showLogsInConsole.booleanValue()).commit();
            showLogsInConsole = Boolean.valueOf(sharedPreferences.getBoolean(SHARED_PREF_KEY_logPersistence_from_server, showLogsInConsole.booleanValue()));
        }
    }

    public static boolean getShowLogsInConsole() {
        Boolean bool = showLogsInConsole;
        boolean booleanValue = bool == null ? true : bool.booleanValue();
        Context context2 = context;
        return context2 != null ? context2.getSharedPreferences(SHARED_PREF_KEY, 0).getBoolean(SHARED_PREF_KEY_logPersistence_from_server, booleanValue) : booleanValue;
    }

    public static void setAutoSendLogs(final boolean z) {
        ThreadPoolWorkQueue.execute(new Runnable() {
            public void run() {
                Logger.setAutoSendLogsSync(z, 60);
                synchronized (Logger.WAIT_LOCK) {
                    Logger.WAIT_LOCK.notifyAll();
                }
            }
        });
    }

    public static void setAutoSendLogs(final boolean z, final int i) {
        ThreadPoolWorkQueue.execute(new Runnable() {
            public void run() {
                Logger.setAutoSendLogsSync(z, i);
                synchronized (Logger.WAIT_LOCK) {
                    Logger.WAIT_LOCK.notifyAll();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public static void setAutoSendLogsSync(boolean z, int i) {
        autoSendLogs = Boolean.valueOf(z);
        intervalSeconds = i;
        if (context != null) {
            synchronized (WAIT_LOCK) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_KEY, 0);
                sharedPreferences.edit().putBoolean(SHARED_PREF_KEY_autoSendLogs, autoSendLogs.booleanValue()).commit();
                sharedPreferences.edit().putInt(SHARED_PREF_KEY_timerAutoSendLogs, intervalSeconds).commit();
            }
            Timer timer2 = autoTriggerTimer;
            if (timer2 != null) {
                timer2.cancel();
                autoTriggerTimer = null;
            }
            if (autoSendLogs.booleanValue()) {
                triggerAutoSendLog();
            }
        }
    }

    public static boolean getCapture() {
        Boolean bool = capture;
        boolean booleanValue = bool == null ? true : bool.booleanValue();
        Context context2 = context;
        return context2 != null ? context2.getSharedPreferences(SHARED_PREF_KEY, 0).getBoolean(SHARED_PREF_KEY_logPersistence_from_server, booleanValue) : booleanValue;
    }

    public static void setAnalyticsCapture(boolean z) {
        analyticsCapture = Boolean.valueOf(z);
    }

    public static boolean getAnalyticsCapture() {
        Boolean bool = analyticsCapture;
        if (bool == null) {
            return true;
        }
        return bool.booleanValue();
    }

    public static void setFilters(HashMap<String, LEVEL> hashMap) {
        filters = hashMap;
        Context context2 = context;
        if (context2 != null) {
            SharedPreferences sharedPreferences = context2.getSharedPreferences(SHARED_PREF_KEY, 0);
            sharedPreferences.edit().putString(SHARED_PREF_KEY_filters, HashMapToJSONObject(filters).toString()).commit();
            try {
                filters = JSONObjectToHashMap(new JSONObject(sharedPreferences.getString(SHARED_PREF_KEY_filters_from_server, HashMapToJSONObject(filters).toString())));
            } catch (JSONException unused) {
            }
        }
    }

    public static HashMap<String, LEVEL> getFilters() {
        HashMap<String, LEVEL> hashMap = filters;
        Context context2 = context;
        if (context2 == null) {
            return hashMap;
        }
        try {
            return JSONObjectToHashMap(new JSONObject(context2.getSharedPreferences(SHARED_PREF_KEY, 0).getString(SHARED_PREF_KEY_filters_from_server, HashMapToJSONObject(hashMap).toString())));
        } catch (JSONException unused) {
            return hashMap;
        }
    }

    public static void setMaxFileSize(int i) {
        if (i >= 10000) {
            logFileMaxSize = Integer.valueOf(i);
        }
        Context context2 = context;
        if (context2 != null) {
            context2.getSharedPreferences(SHARED_PREF_KEY, 0).edit().putInt(SHARED_PREF_KEY_logFileMaxSize, logFileMaxSize.intValue()).commit();
        }
    }

    public static int getMaxFileSize() {
        Integer num = logFileMaxSize;
        return num == null ? DEFAULT_logFileMaxSize : num.intValue();
    }

    public static void send() {
        send(null);
    }

    public static void send(WLRequestListener wLRequestListener) {
        if (!sendingLogs) {
            sendingLogs = true;
            sendFiles(FILENAME, wLRequestListener);
        }
    }

    public static void sendAnalytics(WLRequestListener wLRequestListener) {
        if (!sendingAnalyticsLogs) {
            sendingAnalyticsLogs = true;
            sendFiles(ANALYTICS_FILENAME, wLRequestListener);
        }
    }

    public static synchronized void updateConfigFromServer() {
        synchronized (Logger.class) {
            WLRequest wLRequest = new WLRequest(new WLRequestListener() {
                public void onSuccess(WLResponse wLResponse) {
                    JSONObject responseJSON;
                    try {
                        if (Logger.context == null) {
                            synchronized (Logger.WAIT_LOCK) {
                                Logger.WAIT_LOCK.notifyAll();
                            }
                            return;
                        }
                        if (wLResponse.getStatus() == 200) {
                            responseJSON = wLResponse.getResponseJSON();
                            if (responseJSON == null) {
                                Logger.getInstance(Logger.LOG_TAG).error("Server replied with 200 but had no JSON payload.  Ignoring this reply.");
                                synchronized (Logger.WAIT_LOCK) {
                                    Logger.WAIT_LOCK.notifyAll();
                                }
                                return;
                            }
                            Logger instance = Logger.getInstance(Logger.LOG_TAG);
                            StringBuilder sb = new StringBuilder();
                            sb.append("Processing server reply 200 OK.  Payload: ");
                            sb.append(responseJSON.toString());
                            instance.trace(sb.toString());
                            if (responseJSON.has("clientLogProfileConfig")) {
                                JSONObject jSONObject = responseJSON.getJSONObject("clientLogProfileConfig");
                                JSONArray jSONArray = (JSONArray) jSONObject.get("clientLogProfiles");
                                if (jSONArray.length() > 0) {
                                    for (int i = 0; i < jSONArray.length(); i++) {
                                        JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                        if (jSONObject2.has("name") && jSONObject2.has(Logger.SHARED_PREF_KEY_level)) {
                                            if (jSONObject2.getString("name").equals("MFP_TRACE") && jSONObject2.getString(Logger.SHARED_PREF_KEY_level).equals("TRACE")) {
                                                Logger.mfpTrace = true;
                                                Logger.mfpTraceAnalytics = true;
                                            }
                                            if (jSONObject2.getString("name").equals("MFP_TRACE_NETWORK") && jSONObject2.getString(Logger.SHARED_PREF_KEY_level).equals("TRACE")) {
                                                Logger.mfpTraceNetwork = true;
                                            }
                                        }
                                    }
                                }
                                LoggerServerConfigProcessor.processLoggerServerConfig(jSONObject, Logger.context);
                            } else {
                                LoggerServerConfigProcessor.removeServerLoggerConfigOverrides(Logger.context);
                            }
                        } else if (wLResponse.getStatus() == 204) {
                            Logger.getInstance(Logger.LOG_TAG).trace("Processing server reply 204 NO_CONTENT.  No matching client configuration profiles were found at the MobileFirst Platform server, so removing any server-directed configuration overrides.");
                            LoggerServerConfigProcessor.removeServerLoggerConfigOverrides(Logger.context);
                        }
                        synchronized (Logger.WAIT_LOCK) {
                            Logger.WAIT_LOCK.notifyAll();
                        }
                    } catch (JSONException e) {
                        Logger instance2 = Logger.getInstance(Logger.LOG_TAG);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Failed to parse response from server.  Payload: ");
                        sb2.append(responseJSON.toString());
                        instance2.error(sb2.toString(), (Throwable) e);
                    } catch (Throwable th) {
                        synchronized (Logger.WAIT_LOCK) {
                            Logger.WAIT_LOCK.notifyAll();
                            throw th;
                        }
                    }
                }

                public void onFailure(WLFailResponse wLFailResponse) {
                    Logger instance = Logger.getInstance(Logger.LOG_TAG);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Server replied with HTTP response code ");
                    sb.append(wLFailResponse.getStatus());
                    sb.append(": ");
                    sb.append(wLFailResponse.getErrorMsg());
                    instance.trace(sb.toString());
                    synchronized (Logger.WAIT_LOCK) {
                        Logger.WAIT_LOCK.notifyAll();
                    }
                }
            }, new WLRequestOptions(), WLConfig.getInstance(), context);
            wLRequest.setMethod(RequestMethod.GET);
            StringBuilder sb = new StringBuilder();
            sb.append("clientLogProfile/");
            sb.append(WLConfig.getInstance().getAppId());
            sb.append("/android/");
            sb.append(WLConfig.getInstance().getApplicationVersion());
            wLRequest.makeRequest(sb.toString(), true);
        }
    }

    @Deprecated
    public static synchronized void sendIfUnCaughtExceptionDetected(Context context2) {
        synchronized (Logger.class) {
            setContext(context2);
            boolean z = false;
            if (context2 != null) {
                z = context2.getSharedPreferences(SHARED_PREF_KEY, 0).getBoolean(SHARED_PREF_KEY_CRASH_DETECTED, false);
            }
            if (z) {
                send();
            }
        }
    }

    public static boolean isUnCaughtExceptionDetected() {
        Context context2 = context;
        if (context2 != null) {
            return context2.getSharedPreferences(SHARED_PREF_KEY, 0).getBoolean(SHARED_PREF_KEY_CRASH_DETECTED, false);
        }
        printOneTimeContextNullMessage();
        return false;
    }

    private static void printOneTimeContextNullMessage() {
        if (!context_null_msg_already_printed) {
            Log.w(LOG_TAG, CONTEXT_NULL_MSG);
            context_null_msg_already_printed = true;
        }
    }

    /* access modifiers changed from: protected */
    public void analytics(String str, JSONObject jSONObject, Throwable th) {
        doLog(LEVEL.ANALYTICS, str, new Date().getTime(), jSONObject, th);
    }

    public void fatal(String str, JSONObject jSONObject, Throwable th) {
        doLog(LEVEL.FATAL, str, new Date().getTime(), jSONObject, th);
    }

    public void error(String str) {
        error(str, null, null);
    }

    public void error(String str, JSONObject jSONObject) {
        error(str, jSONObject, null);
    }

    public void error(String str, Throwable th) {
        error(str, null, th);
    }

    public void error(String str, JSONObject jSONObject, Throwable th) {
        doLog(LEVEL.ERROR, str, new Date().getTime(), jSONObject, th);
    }

    public void warn(String str) {
        warn(str, null, null);
    }

    public void warn(String str, JSONObject jSONObject) {
        warn(str, jSONObject, null);
    }

    public void warn(String str, Throwable th) {
        warn(str, null, th);
    }

    public void warn(String str, JSONObject jSONObject, Throwable th) {
        doLog(LEVEL.WARN, str, new Date().getTime(), jSONObject, th);
    }

    public void info(String str) {
        info(str, null);
    }

    public void info(String str, JSONObject jSONObject) {
        doLog(LEVEL.INFO, str, new Date().getTime(), jSONObject, null);
    }

    public void log(String str) {
        log(str, null);
    }

    public void log(String str, JSONObject jSONObject) {
        doLog(LEVEL.LOG, str, new Date().getTime(), jSONObject, null);
    }

    public void debug(String str) {
        debug(str, null, null);
    }

    public void debug(String str, JSONObject jSONObject) {
        debug(str, jSONObject, null);
    }

    public void debug(String str, Throwable th) {
        debug(str, null, th);
    }

    public void debug(String str, JSONObject jSONObject, Throwable th) {
        doLog(LEVEL.DEBUG, str, new Date().getTime(), jSONObject, th);
    }

    public void trace(String str) {
        trace(str, null);
    }

    public void trace(String str, JSONObject jSONObject) {
        doLog(LEVEL.TRACE, str, new Date().getTime(), jSONObject, null);
    }

    public void doLog(LEVEL level2, String str, long j, JSONObject jSONObject, Throwable th) {
        JSONObject appendStackMetadata = appendStackMetadata(jSONObject);
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolWorkQueue;
        DoLogRunnable doLogRunnable = new DoLogRunnable(level2, str, j, appendStackMetadata, th, this);
        threadPoolExecutor.execute(doLogRunnable);
    }

    /* access modifiers changed from: private */
    public static String prependMetadata(String str, JSONObject jSONObject) {
        String str2;
        String str3 = "$line";
        String str4 = "$file";
        String str5 = "$method";
        String str6 = "$class";
        if (jSONObject == null) {
            return str;
        }
        try {
            String str7 = "";
            if (jSONObject.has(str6)) {
                String string = jSONObject.getString(str6);
                str2 = string.substring(string.lastIndexOf(46) + 1, string.length());
            } else {
                str2 = str7;
            }
            String string2 = jSONObject.has(str5) ? jSONObject.getString(str5) : str7;
            String string3 = jSONObject.has(str4) ? jSONObject.getString(str4) : str7;
            String string4 = jSONObject.has(str3) ? jSONObject.getString(str3) : str7;
            StringBuilder sb = new StringBuilder();
            sb.append(str2);
            sb.append(string2);
            sb.append(string3);
            sb.append(string4);
            if (sb.toString().equals(str7)) {
                return str;
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str2);
            sb2.append(".");
            sb2.append(string2);
            sb2.append(" in ");
            sb2.append(string3);
            sb2.append(":");
            sb2.append(string4);
            sb2.append(" :: ");
            sb2.append(str);
            return sb2.toString();
        } catch (Exception unused) {
            return str;
        }
    }

    public static HashMap<String, LEVEL> JSONObjectToHashMap(JSONObject jSONObject) {
        HashMap<String, LEVEL> hashMap = new HashMap<>();
        Iterator keys = jSONObject.keys();
        while (keys.hasNext()) {
            String str = (String) keys.next();
            try {
                hashMap.put(str, LEVEL.valueOf(jSONObject.getString(str).toUpperCase()));
            } catch (JSONException unused) {
            }
        }
        return hashMap;
    }

    public static JSONObject HashMapToJSONObject(HashMap<String, LEVEL> hashMap) {
        if (hashMap == null) {
            return new JSONObject();
        }
        Set<String> keySet = hashMap.keySet();
        JSONObject jSONObject = new JSONObject();
        for (String str : keySet) {
            try {
                jSONObject.put(str, ((LEVEL) hashMap.get(str)).toString());
            } catch (JSONException unused) {
            }
        }
        return jSONObject;
    }

    public static LEVEL getLevelDefault() {
        if (context == null) {
            printOneTimeContextNullMessage();
        }
        return LEVEL.FATAL;
    }

    public static synchronized void processAutomaticTrigger() {
        synchronized (Logger.class) {
            if (Long.valueOf(new Date().getTime()).longValue() - priorAutoTriggerTime >= BuildConfig.MAX_SCAN_INTERVAL) {
                if (timer != null) {
                    timer.cancel();
                }
                Timer timer2 = new Timer("trigger timer");
                timer = timer2;
                timer2.schedule(new TimerTask() {
                    public void run() {
                        if (Logger.autoSendLogs == null || Logger.autoSendLogs.booleanValue()) {
                            Logger.send();
                            Logger.sendAnalytics(null);
                        }
                    }
                }, 50);
                priorAutoTriggerTime = new Date().getTime();
            }
        }
    }

    private static void triggerAutoSendLog() {
        Timer timer2 = autoTriggerTimer;
        if (timer2 != null) {
            timer2.cancel();
        }
        Timer timer3 = new Timer("trigger autoSendLog timer");
        autoTriggerTimer = timer3;
        timer3.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (Logger.autoSendLogs == null || Logger.autoSendLogs.booleanValue()) {
                    Logger.sendAnalytics(new WLRequestListener() {
                        public void onSuccess(WLResponse wLResponse) {
                            Log.i(Logger.LOG_TAG, "Logger logs Successfully send to server");
                        }

                        public void onFailure(WLFailResponse wLFailResponse) {
                            Log.i(Logger.LOG_TAG, "Logger logs failed to send to server");
                        }
                    });
                    Logger.send(new WLRequestListener() {
                        public void onSuccess(WLResponse wLResponse) {
                            Log.i(Logger.LOG_TAG, "Analytics logs Successfully send to server");
                        }

                        public void onFailure(WLFailResponse wLFailResponse) {
                            Log.i(Logger.LOG_TAG, "Analytics logs failed to send to server");
                        }
                    });
                }
            }
        }, new Date(), (long) (intervalSeconds * 1000));
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(21:0|(5:2|3|(2:5|(1:7))|8|(3:10|11|(1:15)))|16|17|(2:20|18)|58|(3:21|(2:27|(2:29|63)(1:59))|52)|(1:31)|32|(1:34)|35|(1:37)|38|(1:40)|41|(1:43)|44|(1:46)|47|(2:49|(1:51))|57) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0038 */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0052 A[Catch:{ Exception -> 0x0103 }, LOOP:0: B:18:0x0042->B:20:0x0052, LOOP_END] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x009b A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x009e A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00aa A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00b9 A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00c8 A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00d7 A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00e6 A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00f1 A[Catch:{ Exception -> 0x0103 }] */
    /* JADX WARNING: Removed duplicated region for block: B:59:0x009c A[EDGE_INSN: B:59:0x009c->B:30:0x009c ?: BREAK  
EDGE_INSN: B:59:0x009c->B:30:0x009c ?: BREAK  , SYNTHETIC] */
    private JSONObject appendStackMetadata(JSONObject jSONObject) {
        String str = "$line";
        String str2 = "$method";
        String str3 = "$file";
        String str4 = "$class";
        Class<Logger> cls = Logger.class;
        String str5 = "$src";
        String str6 = "$clientId";
        if (jSONObject != null) {
            try {
                if (!jSONObject.has(str6)) {
                    String clientId = WLAuthorizationManagerInternal.getInstance().getClientId();
                    if (clientId != null) {
                        jSONObject.put(str6, clientId);
                    }
                }
                if (jSONObject.has(str5)) {
                    String string = jSONObject.getString(str5);
                    if (string != null && string.equals("js")) {
                        return jSONObject;
                    }
                }
            } catch (Exception e) {
                if (!entered) {
                    Log.e(LOG_TAG, "Could not generate jsonMetadata object.", e);
                }
            }
        }
        StackTraceElement[] stackTrace = new Exception().getStackTrace();
        int i = 0;
        while (!stackTrace[i].getClassName().equals(cls.getName())) {
            i++;
        }
        while (true) {
            if (!stackTrace[i].getClassName().equals(cls.getName()) && !stackTrace[i].getClassName().startsWith(JULHandler.class.getName()) && !stackTrace[i].getClassName().startsWith(java.util.logging.Logger.class.getName())) {
                if (stackTrace[i].getClassName().startsWith(WLAnalytics.class.getName())) {
                    break;
                }
            }
            i++;
        }
        if (jSONObject == null) {
            jSONObject = new JSONObject();
        }
        if (!jSONObject.has(str4)) {
            jSONObject.put(str4, stackTrace[i].getClassName());
        }
        if (!jSONObject.has(str3)) {
            jSONObject.put(str3, stackTrace[i].getFileName());
        }
        if (!jSONObject.has(str2)) {
            jSONObject.put(str2, stackTrace[i].getMethodName());
        }
        if (!jSONObject.has(str)) {
            jSONObject.put(str, stackTrace[i].getLineNumber());
        }
        if (!jSONObject.has(str5)) {
            jSONObject.put(str5, "java");
        }
        if (!jSONObject.has(str6)) {
            String clientId2 = WLAuthorizationManagerInternal.getInstance().getClientId();
            if (clientId2 != null) {
                jSONObject.put(str6, clientId2);
            }
        }
        return jSONObject;
    }

    /* access modifiers changed from: private */
    public static JSONObject createJSONObject(LEVEL level2, String str, String str2, long j, JSONObject jSONObject, Throwable th) {
        JSONObject jSONObject2 = new JSONObject();
        try {
            jSONObject2.put("timestamp", j);
            jSONObject2.put(SHARED_PREF_KEY_level, level2.toString());
            jSONObject2.put("pkg", str);
            jSONObject2.put(NotificationCompat.CATEGORY_MESSAGE, str2);
            jSONObject2.put("threadid", Thread.currentThread().getId());
            String str3 = "metadata";
            if (jSONObject != null) {
                jSONObject2.put(str3, jSONObject);
            }
            if (th != null) {
                jSONObject2.put(str3, appendFullStackTrace(jSONObject, th));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error adding JSONObject key/value pairs", e);
        }
        return jSONObject2;
    }

    private static JSONObject appendFullStackTrace(JSONObject jSONObject, Throwable th) {
        String str;
        JSONArray jSONArray = new JSONArray();
        boolean z = true;
        Throwable th2 = th;
        while (th2 != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(z ? "Exception " : "Caused by: ");
            sb.append(th2.getClass().getName());
            if (th2.getMessage() != null) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(": ");
                sb2.append(th2.getMessage());
                str = sb2.toString();
            } else {
                str = "";
            }
            sb.append(str);
            jSONArray.put(sb.toString());
            StackTraceElement[] stackTrace = th2.getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                jSONArray.put(stackTraceElement.toString());
            }
            th2 = th2.getCause();
            z = false;
        }
        if (jSONObject == null) {
            try {
                jSONObject = new JSONObject();
            } catch (JSONException unused) {
            }
        }
        jSONObject.put("$stacktrace", jSONArray);
        jSONObject.put("$exceptionMessage", th.getLocalizedMessage());
        jSONObject.put("$exceptionClass", th.getClass().getName());
        return jSONObject;
    }

    /* access modifiers changed from: private */
    public static synchronized void captureToFile(JSONObject jSONObject, LEVEL level2) {
        synchronized (Logger.class) {
            boolean capture2 = getCapture();
            boolean analyticsCapture2 = getAnalyticsCapture();
            if (context == null) {
                printOneTimeContextNullMessage();
                return;
            } else if (jSONObject.length() != 0) {
                if (analyticsCapture2) {
                    try {
                        if (level2.equals(LEVEL.ANALYTICS)) {
                            fileLoggerInstance.log(jSONObject, ANALYTICS_FILENAME);
                        }
                    } catch (Exception e) {
                        Log.e(LOG_TAG, "An error occurred capturing data to file.", e);
                    }
                }
                if (capture2) {
                    fileLoggerInstance.log(jSONObject, FILENAME);
                }
            } else {
                return;
            }
        }
        return;
    }

    public static byte[] getByteArrayFromFile(File file) throws UnsupportedEncodingException {
        FileLoggerInterface fileLoggerInterface = fileLoggerInstance;
        return fileLoggerInterface == null ? new byte[0] : fileLoggerInterface.getFileContentsAsByteArray(file);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0017, code lost:
        return;
     */
    private static synchronized void sendTraceLogs(WLRequestListener wLRequestListener, String str) {
        synchronized (Logger.class) {
            if (context != null) {
                if (str == NETWORK) {
                    try {
                        if (networkJsonBuffer.length() > 0) {
                            SendLogsRequestListener sendLogsRequestListener = new SendLogsRequestListener(wLRequestListener);
                            WLRequestOptions wLRequestOptions = new WLRequestOptions();
                            wLRequestOptions.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
                            wLRequestOptions.addParameter("__logdata", networkJsonBuffer.toString());
                            wLRequestOptions.setTextContentType(true);
                            networkJsonBuffer = new StringBuilder();
                            new WLRequest(sendLogsRequestListener, wLRequestOptions, WLConfig.getInstance(), context).makeRequest(RequestPaths.UPLOAD_LOGS, true);
                        }
                    } catch (Exception e) {
                        getInstance(LOG_TAG).error("Failed to send logs due to exception.", (Throwable) e);
                        if (wLRequestListener != null) {
                            wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, "Failed to send logs due to exception.", null));
                        }
                        return;
                    }
                }
                if (str == FUNCTION && stringJsonBuffer.length() > 0) {
                    SendLogsRequestListener sendLogsRequestListener2 = new SendLogsRequestListener(wLRequestListener);
                    WLRequestOptions wLRequestOptions2 = new WLRequestOptions();
                    wLRequestOptions2.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
                    wLRequestOptions2.addParameter("__logdata", stringJsonBuffer.toString());
                    wLRequestOptions2.setTextContentType(true);
                    stringJsonBuffer = new StringBuilder();
                    new WLRequest(sendLogsRequestListener2, wLRequestOptions2, WLConfig.getInstance(), context).makeRequest(RequestPaths.UPLOAD_LOGS, true);
                }
            } else if (wLRequestListener != null) {
                wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, CONTEXT_NULL_MSG, null));
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:47:0x011e, code lost:
        return;
     */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00de  */
    private static synchronized void sendFiles(String str, WLRequestListener wLRequestListener) {
        boolean z;
        Throwable e;
        synchronized (Logger.class) {
            boolean equalsIgnoreCase = str.equalsIgnoreCase(ANALYTICS_FILENAME);
            if (context == null) {
                if (wLRequestListener != null) {
                    wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.ILLEGAL_ARGUMENT_EXCEPTION, CONTEXT_NULL_MSG, null));
                }
                unlockMutex(equalsIgnoreCase);
                return;
            }
            boolean z2 = false;
            for (int i = 1; i > -1; i--) {
                File noBackupFilesDir = WLUtils.getNoBackupFilesDir(context);
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(".");
                sb.append(i);
                File file = new File(noBackupFilesDir, sb.toString());
                if (file.length() > 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(file);
                    sb2.append(".send");
                    File file2 = new File(sb2.toString());
                    if (!file2.exists()) {
                        Logger instance = getInstance(LOG_TAG);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Moving ");
                        sb3.append(file);
                        sb3.append(" to ");
                        sb3.append(file2);
                        instance.trace(sb3.toString());
                        file.renameTo(file2);
                    }
                    SendLogsRequestListener sendLogsRequestListener = new SendLogsRequestListener(file2, wLRequestListener, equalsIgnoreCase);
                    WLRequestOptions wLRequestOptions = new WLRequestOptions();
                    wLRequestOptions.addHeader(HttpHeaders.CONTENT_TYPE, "text/plain");
                    try {
                        byte[] byteArrayFromFile = getByteArrayFromFile(file2);
                        if (byteArrayFromFile.length != 0) {
                            wLRequestOptions.addParameter("__logdata", new String(byteArrayFromFile, "UTF-8"));
                            wLRequestOptions.setTextContentType(true);
                            try {
                                new WLRequest(sendLogsRequestListener, wLRequestOptions, WLConfig.getInstance(), context).makeRequest(RequestPaths.UPLOAD_LOGS, true);
                                z2 = true;
                            } catch (IOException e2) {
                                e = e2;
                                z = true;
                                getInstance(LOG_TAG).error("Failed to send logs due to exception.", e);
                                if (wLRequestListener != null) {
                                    wLRequestListener.onFailure(new WLFailResponse(WLErrorCode.UNEXPECTED_ERROR, "Failed to send logs due to exception.", null));
                                }
                                z2 = z;
                            }
                        }
                    } catch (IOException e3) {
                        Throwable th = e3;
                        z = z2;
                        e = th;
                        getInstance(LOG_TAG).error("Failed to send logs due to exception.", e);
                        if (wLRequestListener != null) {
                        }
                        z2 = z;
                    }
                }
            }
            if (!z2) {
                unlockMutex(equalsIgnoreCase);
                if (wLRequestListener != null) {
                    String str2 = equalsIgnoreCase ? "analytics" : "logs";
                    WLErrorCode wLErrorCode = WLErrorCode.UNEXPECTED_ERROR;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("No ");
                    sb4.append(str2);
                    sb4.append(" were sent because nothing has been logged.");
                    wLRequestListener.onFailure(new WLFailResponse(wLErrorCode, sb4.toString(), null));
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static void unlockMutex(boolean z) {
        if (z) {
            sendingAnalyticsLogs = false;
        } else {
            sendingLogs = false;
        }
    }
}

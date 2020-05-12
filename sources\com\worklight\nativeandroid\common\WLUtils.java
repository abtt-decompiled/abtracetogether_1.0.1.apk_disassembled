package com.worklight.nativeandroid.common;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import com.worklight.common.Logger;
import com.worklight.common.NoSuchResourceException;
import com.worklight.common.WLConfig;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.zip.CRC32;
import java.util.zip.Checksum;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import okhttp3.HttpUrl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WLUtils {
    public static final int ANDROID_BUFFER_8K = 8192;
    public static final String BUNDLE_BASENAME = "com.worklight.wlclient.messages";
    public static final String BUNDLE_RESOURCE = "/com/worklight/wlclient/messages";
    private static HashSet<String> LOADED_LIBS = new HashSet<>();
    public static final String LOG_CAT = "WL";
    public static final int LOLLIPOP_MR1 = 22;
    private static int MAX_PERMITTED_DATA_SIZE = 10485760;
    private static final String MOBILE_IRON = "com.mobileiron.wrapped";
    public static final String WL_CHALLENGE_DATA = "WL-Challenge-Data";
    public static final String WL_CHALLENGE_RESPONSE_DATA = "WL-Challenge-Response-Data";
    public static final String WL_INSTANCE_AUTH_ID = "WL-Instance-Id";
    public static final String WL_PREFS = "WLPrefs";
    public static final String WWW = "www";
    private static ResourceBundle bundle;
    private static final Logger logger = Logger.getInstance(WLUtils.class.getName());

    public static boolean checkMaxResponseThreshold() {
        return false;
    }

    public static Drawable scaleImage(Drawable drawable, float f, float f2) {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(f, f2);
        return new BitmapDrawable(Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true));
    }

    public static int getResourceId(Context context, String str, String str2) throws NoSuchResourceException {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(".R");
            Class[] declaredClasses = Class.forName(sb.toString()).getDeclaredClasses();
            for (int i = 0; i < declaredClasses.length; i++) {
                if (declaredClasses[i].getSimpleName().equals(str)) {
                    return declaredClasses[i].getField(str2).getInt(null);
                }
            }
            return -1;
        } catch (Exception e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to find resource R.");
            sb2.append(str);
            sb2.append(".");
            sb2.append(str2);
            throw new NoSuchResourceException(sb2.toString(), e);
        }
    }

    public static String getResourceString(String str, Context context) {
        return getResourceString(str, null, context);
    }

    public static String getResourceString(String str, String str2, Context context) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(context.getPackageName());
            sb.append(".R$string");
            Integer num = (Integer) Class.forName(sb.toString()).getDeclaredField(str).get(null);
            if (str2 == null) {
                return context.getResources().getString(num.intValue());
            }
            return context.getResources().getString(num.intValue(), new Object[]{str2});
        } catch (Exception e) {
            logger.error(e.getMessage(), (Throwable) e);
            return "";
        }
    }

    public static long getFreeSpaceOnDevice() {
        StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
        return ((long) statFs.getBlockSize()) * ((long) statFs.getAvailableBlocks());
    }

    public static void copyDirectory(File file, File file2) throws IOException {
        if (file != null && file2 != null) {
            if (file.isDirectory()) {
                if (!file2.exists()) {
                    file2.mkdir();
                }
                String[] list = file.list();
                for (int i = 0; i < file.listFiles().length; i++) {
                    copyDirectory(new File(file, list[i]), new File(file2, list[i]));
                }
                return;
            }
            copyFile(file, file2);
        }
    }

    public static void copyFile(File file, File file2) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);
        if (!file2.exists()) {
            if (file.isDirectory()) {
                file2.mkdirs();
            } else {
                File file3 = new File(file2.getParent());
                if (!file3.exists()) {
                    file3.mkdirs();
                }
                file2.createNewFile();
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file2);
        try {
            byte[] bArr = new byte[8192];
            while (true) {
                int read = fileInputStream.read(bArr);
                if (read != -1) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    fileInputStream.close();
                    fileOutputStream.close();
                    return;
                }
            }
        } catch (IOException e) {
            throw e;
        } catch (Throwable th) {
            fileInputStream.close();
            fileOutputStream.close();
            throw th;
        }
    }

    public static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                outputStream.flush();
                return;
            }
        }
    }

    public static boolean deleteDirectory(File file) {
        if (file.isDirectory()) {
            for (File deleteDirectory : file.listFiles()) {
                deleteDirectory(deleteDirectory);
            }
        }
        return file.delete();
    }

    public static void calculateCheckSum(InputStream inputStream, Checksum checksum) {
        String str = "Problem while trying to close InputStream";
        byte[] bArr = new byte[8192];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read == -1) {
                    break;
                }
                checksum.update(bArr, 0, read);
            } catch (IOException e) {
                String str2 = "An error occurred while trying to read checksum from assets folder";
                logger.error(str2, (Throwable) e);
                throw new RuntimeException(str2);
            } catch (Throwable th) {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        logger.debug(str, (Throwable) e2);
                    }
                }
                throw th;
            }
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e3) {
                logger.debug(str, (Throwable) e3);
            }
        }
    }

    public static long computeChecksumOnExternal(String str) {
        List<File> tree = getTree(new File(str));
        Collections.sort(tree);
        CRC32 crc32 = new CRC32();
        for (File file : tree) {
            try {
                calculateCheckSum(new FileInputStream(file), crc32);
            } catch (IOException e) {
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("Application failed to load, because checksum was not calculated for file ");
                sb.append(file.getName());
                sb.append(" with ");
                sb.append(e.getMessage());
                logger2.error(sb.toString(), (Throwable) e);
            }
        }
        return crc32.getValue();
    }

    public static String convertStreamToString(InputStream inputStream) {
        String str = "Failed to close input stream because ";
        String str2 = "Data size exceeds maximum permitted value of 10Mb";
        try {
            if (inputStream.available() <= MAX_PERMITTED_DATA_SIZE) {
                String str3 = "UTF-8";
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(str3)));
                StringBuilder sb = new StringBuilder();
                int i = 0;
                while (true) {
                    try {
                        String readLine = bufferedReader.readLine();
                        if (readLine == null) {
                            break;
                        }
                        i += readLine.getBytes(str3).length;
                        if (i > MAX_PERMITTED_DATA_SIZE) {
                            logger.warn(str2);
                            break;
                        }
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(readLine);
                        sb2.append("\n");
                        sb.append(sb2.toString());
                    } catch (Throwable th) {
                        try {
                            inputStream.close();
                        } catch (IOException e) {
                            Logger logger2 = logger;
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append(str);
                            sb3.append(e.getMessage());
                            logger2.debug(sb3.toString(), (Throwable) e);
                        }
                        throw th;
                    }
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                try {
                    inputStream.close();
                } catch (IOException e2) {
                    Logger logger3 = logger;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str);
                    sb4.append(e2.getMessage());
                    logger3.debug(sb4.toString(), (Throwable) e2);
                }
                if (i <= MAX_PERMITTED_DATA_SIZE) {
                    return sb.toString();
                }
                throw new RuntimeException(str2);
            }
            logger.warn(str2);
            throw new RuntimeException(str2);
        } catch (IOException e3) {
            logger.debug("Calling available is not supported", (Throwable) e3);
        }
    }

    public static String convertGZIPStreamToString(InputStream inputStream) {
        try {
            return convertStreamToString(new GZIPInputStream(inputStream));
        } catch (IOException e) {
            StringBuilder sb = new StringBuilder();
            sb.append("Error reading input stream (");
            sb.append(inputStream);
            sb.append(").");
            throw new RuntimeException(sb.toString(), e);
        }
    }

    public static void unpack(InputStream inputStream, File file) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        while (true) {
            ZipEntry nextEntry = zipInputStream.getNextEntry();
            if (nextEntry != null) {
                String name = nextEntry.getName();
                if (name.startsWith("/") || name.startsWith("\\")) {
                    name = name.substring(1);
                }
                StringBuilder sb = new StringBuilder();
                sb.append(file.getPath());
                sb.append(File.separator);
                sb.append(name);
                File file2 = new File(sb.toString());
                if (!nextEntry.isDirectory()) {
                    File parentFile = file2.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
                    copyFile((InputStream) zipInputStream, (OutputStream) bufferedOutputStream);
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                } else if (!file2.exists()) {
                    file2.mkdirs();
                }
            } else {
                return;
            }
        }
    }

    public static List<File> getTree(File file) {
        return getTree(file, new ArrayList());
    }

    private static List<File> getTree(File file, List<File> list) {
        File[] listFiles;
        try {
            for (File file2 : file.listFiles()) {
                if (file2.isDirectory()) {
                    getTree(file2, list);
                } else if (!isMediaFile(file2.getAbsolutePath(), WLConfig.getInstance().getMediaExtensions())) {
                    list.add(file2);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x0026 A[SYNTHETIC, Splitter:B:18:0x0026] */
    public static byte[] read(File file) throws IOException {
        byte[] bArr = new byte[((int) file.length())];
        InputStream inputStream = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                if (fileInputStream.read(bArr) != -1) {
                    try {
                        fileInputStream.close();
                    } catch (IOException unused) {
                    }
                    return bArr;
                }
                throw new IOException("EOF reached while trying to read the whole file");
            } catch (Throwable th) {
                th = th;
                inputStream = fileInputStream;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            if (inputStream != null) {
            }
            throw th;
        }
    }

    public static String getFullAppName(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append(context.getPackageName());
        sb.append(".");
        sb.append(context.getString(getResourceId(context, "string", "app_name")));
        return sb.toString();
    }

    public static final JSONObject convertStringToJSON(String str) throws JSONException {
        if (str.isEmpty()) {
            logger.debug("Input string is empty");
            return null;
        } else if (isContainBrackets(str)) {
            return new JSONObject(str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Input string does not contain brackets, or input string is invalid. The string is: ");
            sb.append(str);
            String sb2 = sb.toString();
            logger.debug(sb2);
            throw new JSONException(sb2);
        }
    }

    public static boolean isContainBrackets(String str) {
        int indexOf = str.indexOf("{");
        int lastIndexOf = str.lastIndexOf("}");
        return (indexOf == -1 || lastIndexOf == -1 || indexOf > lastIndexOf + 1) ? false : true;
    }

    public static final List<String> convertJSONArrayToList(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        int i = 0;
        while (i < jSONArray.length()) {
            try {
                arrayList.add((String) jSONArray.get(i));
                i++;
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return arrayList;
    }

    public static final boolean isStringEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static final byte[] hexStringToByteArray(String str) {
        int length = str.length();
        byte[] bArr = new byte[(length / 2)];
        for (int i = 0; i < length; i += 2) {
            bArr[i / 2] = (byte) ((Character.digit(str.charAt(i), 16) << 4) + Character.digit(str.charAt(i + 1), 16));
        }
        return bArr;
    }

    public static final File getNoBackupFilesDir(Context context) {
        if (VERSION.SDK_INT <= 22) {
            return context.getFilesDir();
        }
        return context.getNoBackupFilesDir();
    }

    public static final String byteArrayToHexString(byte[] bArr) {
        StringBuilder sb = new StringBuilder(bArr.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte valueOf : bArr) {
            formatter.format("%02x", new Object[]{Byte.valueOf(valueOf)});
        }
        formatter.close();
        return sb.toString();
    }

    public static final synchronized void loadLib(Context context, String str) {
        String str2;
        String str3;
        synchronized (WLUtils.class) {
            if (!LOADED_LIBS.contains(str)) {
                boolean parseBoolean = Boolean.parseBoolean(System.getProperty(MOBILE_IRON, "false"));
                String property = System.getProperty("os.arch");
                Logger logger2 = logger;
                StringBuilder sb = new StringBuilder();
                sb.append("os.arch: ");
                sb.append(property);
                logger2.debug(sb.toString());
                if (property == null || (!property.toLowerCase().startsWith("i") && !property.toLowerCase().startsWith("x86"))) {
                    str3 = "armeabi";
                    str2 = "x86";
                } else {
                    str3 = "x86";
                    str2 = "armeabi";
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("featurelibs");
                sb2.append(File.separator);
                sb2.append(str3);
                sb2.append(File.separator);
                sb2.append(str);
                String sb3 = sb2.toString();
                File noBackupFilesDir = getNoBackupFilesDir(context);
                StringBuilder sb4 = new StringBuilder();
                sb4.append(sb3);
                sb4.append(".zip");
                new File(noBackupFilesDir, sb4.toString());
                File noBackupFilesDir2 = getNoBackupFilesDir(context);
                StringBuilder sb5 = new StringBuilder();
                sb5.append("featurelibs");
                sb5.append(File.separator);
                sb5.append(str2);
                deleteDirectory(new File(noBackupFilesDir2, sb5.toString()));
                File file = new File(getNoBackupFilesDir(context), str);
                file.delete();
                Logger logger3 = logger;
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Extracting zip file: ");
                sb6.append(sb3);
                logger3.debug(sb6.toString());
                try {
                    AssetManager assets = context.getAssets();
                    StringBuilder sb7 = new StringBuilder();
                    sb7.append(sb3);
                    sb7.append(".zip");
                    unpack(assets.open(sb7.toString()), file.getParentFile());
                } catch (IOException e) {
                    e.printStackTrace();
                    Logger logger4 = logger;
                    StringBuilder sb8 = new StringBuilder();
                    sb8.append("Error extracting zip file: ");
                    sb8.append(e.getMessage());
                    logger4.debug(sb8.toString());
                }
                Logger logger5 = logger;
                StringBuilder sb9 = new StringBuilder();
                sb9.append("Loading library using System.load: ");
                sb9.append(file.getAbsolutePath());
                logger5.debug(sb9.toString());
                Logger logger6 = logger;
                StringBuilder sb10 = new StringBuilder();
                sb10.append("com.mobileiron.wrapped is set to ");
                sb10.append(parseBoolean);
                logger6.debug(sb10.toString());
                if (parseBoolean) {
                    logger.debug("Loading library using System.loadLibrary: /system/lib/libcrypto.so");
                    System.loadLibrary("crypto");
                } else {
                    Logger logger7 = logger;
                    StringBuilder sb11 = new StringBuilder();
                    sb11.append("Loading library using System.load: ");
                    sb11.append(file.getAbsolutePath());
                    logger7.debug(sb11.toString());
                    System.load(file.getAbsolutePath());
                }
                LOADED_LIBS.add(str);
            }
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(10:8|9|10|(3:12|13|(2:15|(3:17|18|19)(18:20|21|(1:23)(1:24)|25|(2:28|26)|76|29|(4:32|(2:34|(3:77|36|37)(1:79))(1:78)|38|30)|39|40|(2:42|(3:44|(2:46|(3:80|48|49)(1:82))(1:81)|50))|51|52|(4:54|(1:56)(1:57)|58|(3:83|60|61)(1:62))|84|63|64|65)))|66|67|(1:69)|70|71|72) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:66:0x00e4 */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x00e8  */
    public static synchronized ResourceBundle getMessagesBundle() {
        String str;
        String str2;
        synchronized (WLUtils.class) {
            if (bundle != null) {
                ResourceBundle resourceBundle = bundle;
                return resourceBundle;
            }
            String languagePreferences = WLConfig.getInstance().getLanguagePreferences();
            if (languagePreferences != null) {
                if (languagePreferences.length() > 0) {
                    Locale locale = Locale.getDefault();
                    String language = locale.getLanguage();
                    String country = locale.getCountry();
                    if (language.isEmpty()) {
                        ResourceBundle bundle2 = ResourceBundle.getBundle(BUNDLE_BASENAME, Locale.getDefault());
                        bundle = bundle2;
                        return bundle2;
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append(language);
                    if (country.isEmpty()) {
                        str = "";
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("-");
                        sb2.append(country);
                        str = sb2.toString();
                    }
                    sb.append(str);
                    String sb3 = sb.toString();
                    String[] split = languagePreferences.split(",");
                    for (int i = 0; i < split.length; i++) {
                        split[i] = split[i].trim();
                    }
                    for (String equals : split) {
                        if (sb3.equals(equals)) {
                            ResourceBundle bundle3 = getBundle(language, country, true);
                            bundle = bundle3;
                            if (bundle3 != null) {
                                return bundle3;
                            }
                        }
                    }
                    if (!country.isEmpty()) {
                        for (String equals2 : split) {
                            if (language.equals(equals2)) {
                                ResourceBundle bundle4 = getBundle(language, null, true);
                                bundle = bundle4;
                                if (bundle4 != null) {
                                    return bundle4;
                                }
                            }
                        }
                    }
                    for (String str3 : split) {
                        if (str3.length() > 2) {
                            String substring = str3.substring(0, 2);
                            str2 = str3.substring(3, 2);
                            str3 = substring;
                        } else {
                            str2 = null;
                        }
                        ResourceBundle bundle5 = getBundle(str3, str2, true);
                        bundle = bundle5;
                        if (bundle5 != null) {
                            return bundle5;
                        }
                    }
                    bundle = getBundle("en", null, true);
                }
            }
            if (bundle == null) {
                bundle = getBundle(null, null, false);
            }
            ResourceBundle resourceBundle2 = bundle;
            return resourceBundle2;
        }
    }

    private static ResourceBundle getBundle(String str, String str2, boolean z) {
        Class<WLUtils> cls = WLUtils.class;
        String str3 = BUNDLE_BASENAME;
        if (str == null) {
            return ResourceBundle.getBundle(str3, Locale.getDefault());
        }
        String str4 = ".properties";
        String str5 = "/com/worklight/wlclient/messages_";
        if (str2 == null) {
            if (z) {
                if ("en".equals(str)) {
                    return ResourceBundle.getBundle(str3, new Locale(""));
                }
                StringBuilder sb = new StringBuilder();
                sb.append(str5);
                sb.append(str);
                sb.append(str4);
                if (cls.getResource(sb.toString()) == null) {
                    return null;
                }
            }
            return ResourceBundle.getBundle(str3, new Locale(str));
        }
        if (z) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str5);
            sb2.append(str);
            sb2.append("_");
            sb2.append(str2);
            sb2.append(str4);
            if (cls.getResource(sb2.toString()) == null) {
                return null;
            }
        }
        return ResourceBundle.getBundle(str3, new Locale(str, str2));
    }

    public static void copyStream(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                outputStream.write(bArr, 0, read);
            } else {
                outputStream.flush();
                inputStream.close();
                return;
            }
        }
    }

    public static byte[] readStreamToByteArray(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        copyStream(inputStream, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();
        return byteArray;
    }

    public static void clearState() {
        bundle = null;
    }

    public static int getSDKVersion() {
        return VERSION.SDK_INT;
    }

    public static TreeMap<String, String> extractParametersFromURL(String str) {
        TreeMap<String, String> treeMap = new TreeMap<>();
        HttpUrl parse = HttpUrl.parse(str.toString());
        for (String str2 : parse.queryParameterNames()) {
            treeMap.put(str2, parse.queryParameter(str2));
        }
        return treeMap;
    }

    public static boolean isMediaFile(String str, String[] strArr) {
        if (strArr == null) {
            return false;
        }
        String substring = str.substring(str.lastIndexOf(".") + 1, str.length());
        for (String equals : strArr) {
            if (equals.equals(substring)) {
                return true;
            }
        }
        return false;
    }
}

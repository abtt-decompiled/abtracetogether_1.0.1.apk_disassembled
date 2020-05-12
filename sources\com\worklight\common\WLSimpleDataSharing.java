package com.worklight.common;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import com.worklight.nativeandroid.common.WLUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class WLSimpleDataSharing {
    private static final Logger logger = Logger.getInstance(WLSimpleDataSharing.class.getName());
    private static boolean shouldPersist = false;
    private final Context context;

    public WLSimpleDataSharing(Context context2) {
        this.context = context2;
        try {
            boolean z = false;
            String str = context2.getPackageManager().getPackageInfo(context2.getPackageName(), 0).sharedUserId;
            if (str != null && !str.isEmpty()) {
                z = true;
            }
            shouldPersist = z;
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:30|(2:38|39)|40|41) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:40:0x0114 */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x010b A[SYNTHETIC, Splitter:B:35:0x010b] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0111 A[SYNTHETIC, Splitter:B:38:0x0111] */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x0130 A[SYNTHETIC] */
    public void setSharedToken(String str, byte[] bArr) {
        byte[] bArr2;
        String str2 = str;
        String str3 = " to file ";
        String str4 = "Failed to share '";
        String str5 = "setSharedToken";
        Logger.enter(getClass().getSimpleName(), str5);
        if (str2 != null) {
            String str6 = "";
            if (!str.trim().equals(str6)) {
                String[] packagesForUid = this.context.getPackageManager().getPackagesForUid(this.context.getApplicationInfo().uid);
                int length = packagesForUid.length;
                int i = 0;
                int i2 = 0;
                while (i2 < length) {
                    String str7 = packagesForUid[i2];
                    Logger logger2 = logger;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Attempt to share '");
                    sb.append(str2);
                    String str8 = "' with ";
                    sb.append(str8);
                    sb.append(str7);
                    logger2.debug(sb.toString());
                    try {
                        String sanitizedFileName = getSanitizedFileName(str);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(this.context.createPackageContext(str7, i).getFilesDir().getAbsolutePath());
                        sb2.append("/");
                        sb2.append(sanitizedFileName);
                        File file = new File(sb2.toString());
                        if ((!file.exists() || file.isFile()) && sanitizedFileName.length() > 0 && shouldPersist) {
                            FileOutputStream fileOutputStream = null;
                            try {
                                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                                if (bArr == null) {
                                    try {
                                        bArr2 = str6.getBytes();
                                    } catch (IOException e) {
                                        e = e;
                                        fileOutputStream = fileOutputStream2;
                                        try {
                                            Logger logger3 = logger;
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append(str4);
                                            sb3.append(str2);
                                            sb3.append(str8);
                                            sb3.append(str7);
                                            sb3.append(str3);
                                            sb3.append(file.getPath());
                                            logger3.error(sb3.toString(), (Throwable) e);
                                            if (fileOutputStream == null) {
                                            }
                                            i2++;
                                            i = 0;
                                        } catch (Throwable th) {
                                            th = th;
                                            if (fileOutputStream != null) {
                                                fileOutputStream.close();
                                            }
                                            throw th;
                                        }
                                    } catch (Throwable th2) {
                                        th = th2;
                                        fileOutputStream = fileOutputStream2;
                                        if (fileOutputStream != null) {
                                        }
                                        throw th;
                                    }
                                } else {
                                    bArr2 = bArr;
                                }
                                fileOutputStream2.write(bArr2);
                                fileOutputStream2.close();
                                Logger logger4 = logger;
                                StringBuilder sb4 = new StringBuilder();
                                sb4.append("Successfully shared '");
                                sb4.append(str2);
                                sb4.append(str8);
                                sb4.append(str7);
                                sb4.append(str3);
                                sb4.append(file.getPath());
                                logger4.debug(sb4.toString());
                                try {
                                    fileOutputStream2.close();
                                } catch (IOException unused) {
                                }
                            } catch (IOException e2) {
                                e = e2;
                                Logger logger32 = logger;
                                StringBuilder sb32 = new StringBuilder();
                                sb32.append(str4);
                                sb32.append(str2);
                                sb32.append(str8);
                                sb32.append(str7);
                                sb32.append(str3);
                                sb32.append(file.getPath());
                                logger32.error(sb32.toString(), (Throwable) e);
                                if (fileOutputStream == null) {
                                    fileOutputStream.close();
                                }
                                i2++;
                                i = 0;
                            }
                        }
                    } catch (NameNotFoundException e3) {
                        Logger logger5 = logger;
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str4);
                        sb5.append(str2);
                        sb5.append(str8);
                        sb5.append(str7);
                        logger5.error(sb5.toString(), (Throwable) e3);
                    }
                    i2++;
                    i = 0;
                }
                Logger.exit(getClass().getSimpleName(), str5);
                return;
            }
        }
        logger.error("Cannot share a key/value pair where key is empty");
        Logger.exit(getClass().getSimpleName(), str5);
    }

    public void setSharedToken(String str, String str2) {
        byte[] bArr;
        String str3 = "setSharedToken";
        Logger.enter(getClass().getSimpleName(), str3);
        if (str2 == null) {
            bArr = null;
        } else {
            bArr = str2.getBytes();
        }
        setSharedToken(str, bArr);
        Logger.exit(getClass().getSimpleName(), str3);
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:26|(2:34|35)|36|37) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:36:0x00ec */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00e3 A[SYNTHETIC, Splitter:B:31:0x00e3] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00e9 A[SYNTHETIC, Splitter:B:34:0x00e9] */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x010a A[SYNTHETIC] */
    public String getSharedToken(String str) {
        FileInputStream fileInputStream;
        String str2 = str;
        String str3 = " in file ";
        String str4 = "' from ";
        String str5 = "Failed to retrieve '";
        String str6 = "getSharedToken";
        Logger.enter(getClass().getSimpleName(), str6);
        String[] packagesForUid = this.context.getPackageManager().getPackagesForUid(this.context.getApplicationInfo().uid);
        int length = packagesForUid.length;
        int i = 0;
        while (true) {
            FileInputStream fileInputStream2 = null;
            if (i < length) {
                String str7 = packagesForUid[i];
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.context.createPackageContext(str7, 0).getFilesDir().getAbsolutePath());
                    sb.append("/");
                    sb.append(getSanitizedFileName(str));
                    File file = new File(sb.toString());
                    if (file.exists() && file.isFile() && shouldPersist) {
                        try {
                            Logger logger2 = logger;
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("Attempt to retrieve '");
                            sb2.append(str2);
                            sb2.append(str4);
                            sb2.append(str7);
                            sb2.append(str3);
                            sb2.append(file.getPath());
                            logger2.debug(sb2.toString());
                            fileInputStream = new FileInputStream(file);
                        } catch (IOException e) {
                            e = e;
                            try {
                                Logger logger3 = logger;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append(str5);
                                sb3.append(str2);
                                sb3.append(str4);
                                sb3.append(str7);
                                sb3.append(str3);
                                sb3.append(file.getPath());
                                logger3.error(sb3.toString(), (Throwable) e);
                                if (fileInputStream2 == null) {
                                    try {
                                        fileInputStream2.close();
                                    } catch (IOException unused) {
                                    }
                                }
                                i++;
                            } catch (Throwable th) {
                                th = th;
                                if (fileInputStream2 != null) {
                                    fileInputStream2.close();
                                }
                                throw th;
                            }
                        }
                        try {
                            String convertStreamToString = WLUtils.convertStreamToString(fileInputStream);
                            setSharedToken(str2, convertStreamToString);
                            Logger.exit(getClass().getSimpleName(), str6);
                            if (convertStreamToString != null) {
                                convertStreamToString = convertStreamToString.trim();
                            }
                            try {
                                fileInputStream.close();
                            } catch (IOException unused2) {
                            }
                            return convertStreamToString;
                        } catch (IOException e2) {
                            e = e2;
                            fileInputStream2 = fileInputStream;
                            Logger logger32 = logger;
                            StringBuilder sb32 = new StringBuilder();
                            sb32.append(str5);
                            sb32.append(str2);
                            sb32.append(str4);
                            sb32.append(str7);
                            sb32.append(str3);
                            sb32.append(file.getPath());
                            logger32.error(sb32.toString(), (Throwable) e);
                            if (fileInputStream2 == null) {
                            }
                            i++;
                        } catch (Throwable th2) {
                            th = th2;
                            fileInputStream2 = fileInputStream;
                            if (fileInputStream2 != null) {
                            }
                            throw th;
                        }
                    }
                } catch (NameNotFoundException e3) {
                    Logger logger4 = logger;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(str5);
                    sb4.append(str2);
                    sb4.append("' with ");
                    sb4.append(str7);
                    logger4.error(sb4.toString(), (Throwable) e3);
                }
                i++;
            } else {
                Logger.exit(getClass().getSimpleName(), str6);
                return null;
            }
        }
    }

    public void clearSharedToken(String str) {
        String[] packagesForUid;
        String str2 = "clearSharedToken";
        Logger.enter(getClass().getSimpleName(), str2);
        for (String str3 : this.context.getPackageManager().getPackagesForUid(this.context.getApplicationInfo().uid)) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(this.context.createPackageContext(str3, 0).getFilesDir().getAbsolutePath());
                sb.append("/");
                sb.append(getSanitizedFileName(str));
                File file = new File(sb.toString());
                if (file.exists() && file.isFile() && shouldPersist) {
                    Logger logger2 = logger;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Attempt to delete file '");
                    sb2.append(file.getPath());
                    sb2.append("' from ");
                    sb2.append(str3);
                    logger2.debug(sb2.toString());
                    file.delete();
                }
            } catch (NameNotFoundException e) {
                Logger logger3 = logger;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Failed to find '");
                sb3.append(str);
                sb3.append("' with ");
                sb3.append(str3);
                logger3.error(sb3.toString(), (Throwable) e);
            }
        }
        Logger.exit(getClass().getSimpleName(), str2);
    }

    private String getSanitizedFileName(String str) {
        String str2 = "getSanitizedFileName";
        Logger.enter(getClass().getSimpleName(), str2);
        Logger.exit(getClass().getSimpleName(), str2);
        return str.replaceAll("[^a-zA-Z0-9\\._-]+", "_");
    }
}

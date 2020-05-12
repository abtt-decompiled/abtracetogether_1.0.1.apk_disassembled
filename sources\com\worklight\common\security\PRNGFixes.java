package com.worklight.common.security;

import android.os.Build;
import android.os.Build.VERSION;
import android.os.Process;
import com.worklight.common.Logger;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.SecureRandomSpi;
import java.security.Security;

public final class PRNGFixes {
    private static final byte[] BUILD_FINGERPRINT_AND_DEVICE_SERIAL = getBuildFingerprintAndDeviceSerial();
    private static final int VERSION_CODE_JELLY_BEAN = 16;
    private static final int VERSION_CODE_JELLY_BEAN_MR2 = 18;
    /* access modifiers changed from: private */
    public static final Logger logger = Logger.getInstance(PRNGFixes.class.getName());

    public static class LinuxPRNGSecureRandom extends SecureRandomSpi {
        private static final File URANDOM_FILE = new File("/dev/urandom");
        private static final Object sLock = new Object();
        private static DataInputStream sUrandomIn;
        private static OutputStream sUrandomOut;
        private boolean mSeeded;

        /* access modifiers changed from: protected */
        /* JADX WARNING: Missing exception handler attribute for start block: B:16:0x0024 */
        public void engineSetSeed(byte[] bArr) {
            OutputStream urandomOutputStream;
            Logger.enter(getClass().getSimpleName(), "engineSetSeed");
            synchronized (sLock) {
                try {
                    urandomOutputStream = getUrandomOutputStream();
                } catch (IOException ) {
                    try {
                    } catch (Throwable th) {
                        this.mSeeded = true;
                        throw th;
                    }
                } catch (Throwable th2) {
                    while (true) {
                        throw th2;
                    }
                }
            }
            urandomOutputStream.write(bArr);
            urandomOutputStream.flush();
            this.mSeeded = true;
            Logger.exit(getClass().getSimpleName(), "engineSetSeed");
            Logger access$000 = PRNGFixes.logger;
            StringBuilder sb = new StringBuilder();
            sb.append("Failed to mix seed into ");
            sb.append(URANDOM_FILE);
            access$000.warn(sb.toString());
            this.mSeeded = true;
            Logger.exit(getClass().getSimpleName(), "engineSetSeed");
        }

        /* access modifiers changed from: protected */
        public void engineNextBytes(byte[] bArr) {
            DataInputStream urandomInputStream;
            Logger.enter(getClass().getSimpleName(), "engineNextBytes");
            if (!this.mSeeded) {
                engineSetSeed(PRNGFixes.generateSeed());
            }
            try {
                synchronized (sLock) {
                    urandomInputStream = getUrandomInputStream();
                }
                synchronized (urandomInputStream) {
                    urandomInputStream.readFully(bArr);
                }
                Logger.exit(getClass().getSimpleName(), "engineNextBytes");
            } catch (IOException e) {
                Logger.exit(getClass().getSimpleName(), "engineNextBytes");
                StringBuilder sb = new StringBuilder();
                sb.append("Failed to read from ");
                sb.append(URANDOM_FILE);
                throw new SecurityException(sb.toString(), e);
            }
        }

        /* access modifiers changed from: protected */
        public byte[] engineGenerateSeed(int i) {
            String str = "engineGenerateSeed";
            Logger.enter(getClass().getSimpleName(), str);
            byte[] bArr = new byte[i];
            engineNextBytes(bArr);
            Logger.exit(getClass().getSimpleName(), str);
            return bArr;
        }

        private DataInputStream getUrandomInputStream() {
            DataInputStream dataInputStream;
            Logger.enter(getClass().getSimpleName(), "getUrandomInputStream");
            synchronized (sLock) {
                if (sUrandomIn == null) {
                    try {
                        sUrandomIn = new DataInputStream(new FileInputStream(URANDOM_FILE));
                    } catch (IOException e) {
                        Logger.exit(getClass().getSimpleName(), "getUrandomInputStream");
                        StringBuilder sb = new StringBuilder();
                        sb.append("Failed to open ");
                        sb.append(URANDOM_FILE);
                        sb.append(" for reading");
                        throw new SecurityException(sb.toString(), e);
                    }
                }
                Logger.exit(getClass().getSimpleName(), "getUrandomInputStream");
                dataInputStream = sUrandomIn;
            }
            return dataInputStream;
        }

        private OutputStream getUrandomOutputStream() throws IOException {
            OutputStream outputStream;
            Logger.enter(getClass().getSimpleName(), "getUrandomOutputStream");
            synchronized (sLock) {
                if (sUrandomOut == null) {
                    sUrandomOut = new FileOutputStream(URANDOM_FILE);
                }
                Logger.exit(getClass().getSimpleName(), "getUrandomOutputStream");
                outputStream = sUrandomOut;
            }
            return outputStream;
        }
    }

    private static class LinuxPRNGSecureRandomProvider extends Provider {
        public LinuxPRNGSecureRandomProvider() {
            super("LinuxPRNG", 1.0d, "A Linux-specific random number provider that uses /dev/urandom");
            put("SecureRandom.SHA1PRNG", LinuxPRNGSecureRandom.class.getName());
            put("SecureRandom.SHA1PRNG ImplementedIn", "Software");
        }
    }

    private PRNGFixes() {
    }

    public static void apply() {
        applyOpenSSLFix();
        installLinuxPRNGSecureRandom();
    }

    private static void applyOpenSSLFix() throws SecurityException {
        String str = "org.apache.harmony.xnet.provider.jsse.NativeCrypto";
        if (VERSION.SDK_INT >= 16 && VERSION.SDK_INT <= 18) {
            try {
                Class.forName(str).getMethod("RAND_seed", new Class[]{byte[].class}).invoke(null, new Object[]{generateSeed()});
                int intValue = ((Integer) Class.forName(str).getMethod("RAND_load_file", new Class[]{String.class, Long.TYPE}).invoke(null, new Object[]{"/dev/urandom", Integer.valueOf(1024)})).intValue();
                if (intValue != 1024) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unexpected number of bytes read from Linux PRNG: ");
                    sb.append(intValue);
                    throw new IOException(sb.toString());
                }
            } catch (Exception e) {
                throw new SecurityException("Failed to seed OpenSSL PRNG", e);
            }
        }
    }

    private static void installLinuxPRNGSecureRandom() throws SecurityException {
        if (VERSION.SDK_INT <= 18) {
            Provider[] providers = Security.getProviders("SecureRandom.SHA1PRNG");
            if (providers == null || providers.length < 1 || !LinuxPRNGSecureRandomProvider.class.equals(providers[0].getClass())) {
                Security.insertProviderAt(new LinuxPRNGSecureRandomProvider(), 1);
            }
            SecureRandom secureRandom = new SecureRandom();
            if (LinuxPRNGSecureRandomProvider.class.equals(secureRandom.getProvider().getClass())) {
                try {
                    SecureRandom instance = SecureRandom.getInstance("SHA1PRNG");
                    if (!LinuxPRNGSecureRandomProvider.class.equals(instance.getProvider().getClass())) {
                        StringBuilder sb = new StringBuilder();
                        sb.append("SecureRandom.getInstance(\"SHA1PRNG\") backed by wrong Provider: ");
                        sb.append(instance.getProvider().getClass());
                        throw new SecurityException(sb.toString());
                    }
                } catch (NoSuchAlgorithmException e) {
                    throw new SecurityException("SHA1PRNG not available", e);
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("new SecureRandom() backed by wrong Provider: ");
                sb2.append(secureRandom.getProvider().getClass());
                throw new SecurityException(sb2.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public static byte[] generateSeed() {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeLong(System.currentTimeMillis());
            dataOutputStream.writeLong(System.nanoTime());
            dataOutputStream.writeInt(Process.myPid());
            dataOutputStream.writeInt(Process.myUid());
            dataOutputStream.write(BUILD_FINGERPRINT_AND_DEVICE_SERIAL);
            dataOutputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SecurityException("Failed to generate seed", e);
        }
    }

    private static String getDeviceSerialNumber() {
        try {
            return (String) Build.class.getField("SERIAL").get(null);
        } catch (Exception unused) {
            return null;
        }
    }

    private static byte[] getBuildFingerprintAndDeviceSerial() {
        StringBuilder sb = new StringBuilder();
        String str = Build.FINGERPRINT;
        if (str != null) {
            sb.append(str);
        }
        String deviceSerialNumber = getDeviceSerialNumber();
        if (deviceSerialNumber != null) {
            sb.append(deviceSerialNumber);
        }
        try {
            return sb.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException unused) {
            throw new RuntimeException("UTF-8 encoding not supported");
        }
    }
}

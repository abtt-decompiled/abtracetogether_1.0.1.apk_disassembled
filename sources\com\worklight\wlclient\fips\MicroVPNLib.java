package com.worklight.wlclient.fips;

public class MicroVPNLib {
    public native int FIPSInit();

    public native int checkStatus(long j);

    public native int closeMicroVPNConnection(long j);

    public native int closeSocket(String str);

    public native int curlCleanup(long j);

    public native byte[] curlExecute(long j, long j2, String str, String str2, String str3, String[] strArr, String[] strArr2);

    public native long curlInit();

    public native long openMicroVPNConnection(String str, String str2, String str3, String str4);

    public native int openSocket(String str, String str2, String str3);

    public native int resetCookies();

    public native String testWLServer(long j, String str, String str2);

    static {
        System.loadLibrary("uvpn");
    }
}

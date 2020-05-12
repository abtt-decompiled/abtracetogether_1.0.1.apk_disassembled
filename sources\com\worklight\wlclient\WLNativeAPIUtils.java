package com.worklight.wlclient;

import android.os.Build;
import android.os.Build.VERSION;
import com.google.common.net.HttpHeaders;
import okhttp3.Request;

public class WLNativeAPIUtils {
    public static Request setUserAgentHeader(Request request) {
        String str = HttpHeaders.USER_AGENT;
        String header = request.header(str);
        StringBuilder sb = new StringBuilder();
        String str2 = "WLNativeAPI(";
        sb.append(str2);
        sb.append(Build.DEVICE);
        String str3 = "; ";
        sb.append(str3);
        sb.append(Build.DISPLAY);
        sb.append(str3);
        sb.append(Build.MODEL);
        sb.append("; SDK ");
        sb.append(VERSION.SDK);
        sb.append("; Android ");
        sb.append(VERSION.RELEASE);
        sb.append(")");
        String sb2 = sb.toString();
        if (header != null && header.indexOf(str2) < 0) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(header);
            sb3.append(" ");
            sb3.append(sb2);
            header = sb3.toString();
        } else if (header == null) {
            header = sb2;
        }
        return request.newBuilder().removeHeader(str).addHeader(str, header).build();
    }
}

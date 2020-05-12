package com.worklight.utils;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.Locale;
import java.util.Vector;

public class MultifileAssetStream extends SequenceInputStream {
    public MultifileAssetStream(String str, AssetManager assetManager) throws IOException {
        super(getAssetFileStreams(str, assetManager).elements());
    }

    private static InputStream getFileInputStream(String str, AssetManager assetManager) {
        try {
            return assetManager.open(str);
        } catch (IOException unused) {
            return null;
        }
    }

    private static Vector<InputStream> getAssetFileStreams(String str, AssetManager assetManager) throws IOException {
        Vector<InputStream> vector = new Vector<>();
        boolean z = true;
        int i = 1;
        while (z) {
            InputStream fileInputStream = getFileInputStream(getAssetFileName(str, i), assetManager);
            if (i == 0 && fileInputStream == null) {
                fileInputStream = getFileInputStream(str, assetManager);
            }
            if (fileInputStream != null) {
                vector.add(fileInputStream);
                i++;
            } else {
                z = false;
            }
        }
        if (vector.size() != 0) {
            return vector;
        }
        throw new IOException("Unable to open any files with that base name");
    }

    private static String getAssetFileName(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(".");
        sb.append(String.format(Locale.US, "%03d", new Object[]{Integer.valueOf(i)}));
        return sb.toString();
    }
}

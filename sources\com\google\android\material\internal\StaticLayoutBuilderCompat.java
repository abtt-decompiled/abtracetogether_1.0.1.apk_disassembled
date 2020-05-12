package com.google.android.material.internal;

import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils.TruncateAt;
import androidx.core.util.Preconditions;
import java.lang.reflect.Constructor;

final class StaticLayoutBuilderCompat {
    private static final String TEXT_DIRS_CLASS = "android.text.TextDirectionHeuristics";
    private static final String TEXT_DIR_CLASS = "android.text.TextDirectionHeuristic";
    private static final String TEXT_DIR_FIRSTSTRONG_LTR = "FIRSTSTRONG_LTR";
    private static Constructor<StaticLayout> constructor;
    private static boolean initialized;
    private static Object textDirection;
    private Alignment alignment;
    private TruncateAt ellipsize;
    private int end;
    private boolean includePad;
    private int maxLines;
    private final TextPaint paint;
    private final CharSequence source;
    private int start = 0;
    private final int width;

    static class StaticLayoutBuilderCompatException extends Exception {
        StaticLayoutBuilderCompatException(Throwable th) {
            super("Error thrown initializing StaticLayout", th);
        }
    }

    private StaticLayoutBuilderCompat(CharSequence charSequence, TextPaint textPaint, int i) {
        this.source = charSequence;
        this.paint = textPaint;
        this.width = i;
        this.end = charSequence.length();
        this.alignment = Alignment.ALIGN_NORMAL;
        this.maxLines = Integer.MAX_VALUE;
        this.includePad = true;
        this.ellipsize = null;
    }

    public static StaticLayoutBuilderCompat obtain(CharSequence charSequence, TextPaint textPaint, int i) {
        return new StaticLayoutBuilderCompat(charSequence, textPaint, i);
    }

    public StaticLayoutBuilderCompat setAlignment(Alignment alignment2) {
        this.alignment = alignment2;
        return this;
    }

    public StaticLayoutBuilderCompat setIncludePad(boolean z) {
        this.includePad = z;
        return this;
    }

    public StaticLayoutBuilderCompat setStart(int i) {
        this.start = i;
        return this;
    }

    public StaticLayoutBuilderCompat setEnd(int i) {
        this.end = i;
        return this;
    }

    public StaticLayoutBuilderCompat setMaxLines(int i) {
        this.maxLines = i;
        return this;
    }

    public StaticLayoutBuilderCompat setEllipsize(TruncateAt truncateAt) {
        this.ellipsize = truncateAt;
        return this;
    }

    public StaticLayout build() throws StaticLayoutBuilderCompatException {
        if (VERSION.SDK_INT >= 23) {
            Builder obtain = Builder.obtain(this.source, this.start, this.end, this.paint, this.width);
            obtain.setAlignment(this.alignment);
            obtain.setIncludePad(this.includePad);
            TruncateAt truncateAt = this.ellipsize;
            if (truncateAt != null) {
                obtain.setEllipsize(truncateAt);
            }
            obtain.setMaxLines(this.maxLines);
            return obtain.build();
        }
        createConstructorWithReflection();
        try {
            return (StaticLayout) ((Constructor) Preconditions.checkNotNull(constructor)).newInstance(new Object[]{this.source, Integer.valueOf(this.start), Integer.valueOf(this.end), this.paint, Integer.valueOf(this.width), this.alignment, Preconditions.checkNotNull(textDirection), Float.valueOf(1.0f), Float.valueOf(0.0f), Boolean.valueOf(this.includePad), this.ellipsize, Integer.valueOf(this.width), Integer.valueOf(this.maxLines)});
        } catch (Exception e) {
            throw new StaticLayoutBuilderCompatException(e);
        }
    }

    private static void createConstructorWithReflection() throws StaticLayoutBuilderCompatException {
        Class cls;
        if (!initialized) {
            try {
                if (VERSION.SDK_INT >= 18) {
                    cls = TextDirectionHeuristic.class;
                    textDirection = TextDirectionHeuristics.FIRSTSTRONG_LTR;
                } else {
                    ClassLoader classLoader = StaticLayoutBuilderCompat.class.getClassLoader();
                    Class loadClass = classLoader.loadClass(TEXT_DIR_CLASS);
                    Class loadClass2 = classLoader.loadClass(TEXT_DIRS_CLASS);
                    textDirection = loadClass2.getField(TEXT_DIR_FIRSTSTRONG_LTR).get(loadClass2);
                    cls = loadClass;
                }
                Constructor<StaticLayout> declaredConstructor = StaticLayout.class.getDeclaredConstructor(new Class[]{CharSequence.class, Integer.TYPE, Integer.TYPE, TextPaint.class, Integer.TYPE, Alignment.class, cls, Float.TYPE, Float.TYPE, Boolean.TYPE, TruncateAt.class, Integer.TYPE, Integer.TYPE});
                constructor = declaredConstructor;
                declaredConstructor.setAccessible(true);
                initialized = true;
            } catch (Exception e) {
                throw new StaticLayoutBuilderCompatException(e);
            }
        }
    }
}

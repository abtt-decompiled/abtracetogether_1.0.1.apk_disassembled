package com.google.android.material.slider;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build.VERSION;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import com.google.android.material.R;
import com.google.android.material.drawable.DrawableUtils;
import com.google.android.material.internal.DescendantOffsetUtils;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import com.google.android.material.tooltip.TooltipDrawable;
import com.google.common.primitives.Ints;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Slider extends View {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_Slider;
    private static final String EXCEPTION_ILLEGAL_DISCRETE_VALUE = "Value must be equal to valueFrom plus a multiple of stepSize when using stepSize";
    private static final String EXCEPTION_ILLEGAL_STEP_SIZE = "The stepSize must be 0, or a factor of the valueFrom-valueTo range";
    private static final String EXCEPTION_ILLEGAL_VALUE = "Slider value must be greater or equal to valueFrom, and lower or equal to valueTo";
    private static final String EXCEPTION_ILLEGAL_VALUE_FROM = "valueFrom must be smaller than valueTo";
    private static final String EXCEPTION_ILLEGAL_VALUE_TO = "valueTo must be greater than valueFrom";
    private static final int HALO_ALPHA = 63;
    public static final int LABEL_FLOATING = 0;
    public static final int LABEL_GONE = 2;
    public static final int LABEL_WITHIN_BOUNDS = 1;
    private static final String TAG = Slider.class.getSimpleName();
    private static final double THRESHOLD = 1.0E-4d;
    private final Paint activeTicksPaint;
    private final Paint activeTrackPaint;
    private List<OnChangeListener> changeListeners;
    private boolean forceDrawCompatHalo;
    private LabelFormatter formatter;
    private ColorStateList haloColor;
    private final Paint haloPaint;
    private int haloRadius;
    private final Paint inactiveTicksPaint;
    private final Paint inactiveTrackPaint;
    private TooltipDrawable label;
    private int labelBehavior;
    private int labelPadding;
    private final int scaledTouchSlop;
    private float stepSize;
    private final MaterialShapeDrawable thumbDrawable;
    private boolean thumbIsPressed;
    private final Paint thumbPaint;
    private int thumbRadius;
    private ColorStateList tickColorActive;
    private ColorStateList tickColorInactive;
    private float[] ticksCoordinates;
    private float touchDownX;
    private List<OnSliderTouchListener> touchListeners;
    private ColorStateList trackColorActive;
    private ColorStateList trackColorInactive;
    private int trackHeight;
    private int trackSidePadding;
    private int trackTop;
    private int trackWidth;
    private float value;
    private float valueFrom;
    private float valueTo;
    private int widgetHeight;

    public static final class BasicLabelFormatter implements LabelFormatter {
        private static final int BILLION = 1000000000;
        private static final int MILLION = 1000000;
        private static final int THOUSAND = 1000;
        private static final long TRILLION = 1000000000000L;

        public String getFormattedValue(float f) {
            if (f >= 1.0E12f) {
                return String.format(Locale.US, "%.1fT", new Object[]{Float.valueOf(f / 1.0E12f)});
            } else if (f >= 1.0E9f) {
                return String.format(Locale.US, "%.1fB", new Object[]{Float.valueOf(f / 1.0E9f)});
            } else if (f >= 1000000.0f) {
                return String.format(Locale.US, "%.1fM", new Object[]{Float.valueOf(f / 1000000.0f)});
            } else if (f >= 1000.0f) {
                return String.format(Locale.US, "%.1fK", new Object[]{Float.valueOf(f / 1000.0f)});
            } else {
                return String.format(Locale.US, "%.0f", new Object[]{Float.valueOf(f)});
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface LabelBehavior {
    }

    public interface LabelFormatter {
        String getFormattedValue(float f);
    }

    public interface OnChangeListener {
        void onValueChange(Slider slider, float f, boolean z);
    }

    public interface OnSliderTouchListener {
        void onStartTrackingTouch(Slider slider);

        void onStopTrackingTouch(Slider slider);
    }

    static class SliderState extends BaseSavedState {
        public static final Creator<SliderState> CREATOR = new Creator<SliderState>() {
            public SliderState createFromParcel(Parcel parcel) {
                return new SliderState(parcel);
            }

            public SliderState[] newArray(int i) {
                return new SliderState[i];
            }
        };
        boolean hasFocus;
        float stepSize;
        float value;
        float valueFrom;
        float valueTo;

        SliderState(Parcelable parcelable) {
            super(parcelable);
        }

        private SliderState(Parcel parcel) {
            super(parcel);
            this.valueFrom = parcel.readFloat();
            this.valueTo = parcel.readFloat();
            this.value = parcel.readFloat();
            this.stepSize = parcel.readFloat();
            this.hasFocus = parcel.createBooleanArray()[0];
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeFloat(this.valueFrom);
            parcel.writeFloat(this.valueTo);
            parcel.writeFloat(this.value);
            parcel.writeFloat(this.stepSize);
            parcel.writeBooleanArray(new boolean[]{this.hasFocus});
        }
    }

    public Slider(Context context) {
        this(context, null);
    }

    public Slider(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.sliderStyle);
    }

    public Slider(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, DEF_STYLE_RES), attributeSet, i);
        this.changeListeners = new ArrayList();
        this.touchListeners = new ArrayList();
        this.thumbIsPressed = false;
        this.stepSize = 0.0f;
        this.thumbDrawable = new MaterialShapeDrawable();
        Context context2 = getContext();
        Paint paint = new Paint();
        this.inactiveTrackPaint = paint;
        paint.setStyle(Style.STROKE);
        this.inactiveTrackPaint.setStrokeCap(Cap.ROUND);
        Paint paint2 = new Paint();
        this.activeTrackPaint = paint2;
        paint2.setStyle(Style.STROKE);
        this.activeTrackPaint.setStrokeCap(Cap.ROUND);
        Paint paint3 = new Paint(1);
        this.thumbPaint = paint3;
        paint3.setStyle(Style.FILL);
        this.thumbPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        Paint paint4 = new Paint(1);
        this.haloPaint = paint4;
        paint4.setStyle(Style.FILL);
        Paint paint5 = new Paint();
        this.inactiveTicksPaint = paint5;
        paint5.setStyle(Style.STROKE);
        this.inactiveTicksPaint.setStrokeCap(Cap.ROUND);
        Paint paint6 = new Paint();
        this.activeTicksPaint = paint6;
        paint6.setStyle(Style.STROKE);
        this.activeTicksPaint.setStrokeCap(Cap.ROUND);
        loadResources(context2.getResources());
        processAttributes(context2, attributeSet, i);
        setFocusable(true);
        this.thumbDrawable.setShadowCompatibilityMode(2);
        this.scaledTouchSlop = ViewConfiguration.get(context2).getScaledTouchSlop();
    }

    private void loadResources(Resources resources) {
        this.widgetHeight = resources.getDimensionPixelSize(R.dimen.mtrl_slider_widget_height);
        this.trackSidePadding = resources.getDimensionPixelOffset(R.dimen.mtrl_slider_track_side_padding);
        this.trackTop = resources.getDimensionPixelOffset(R.dimen.mtrl_slider_track_top);
        this.labelPadding = resources.getDimensionPixelSize(R.dimen.mtrl_slider_label_padding);
    }

    private void processAttributes(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context, attributeSet, R.styleable.Slider, i, DEF_STYLE_RES, new int[0]);
        this.valueFrom = obtainStyledAttributes.getFloat(R.styleable.Slider_android_valueFrom, 0.0f);
        this.valueTo = obtainStyledAttributes.getFloat(R.styleable.Slider_android_valueTo, 1.0f);
        setValue(obtainStyledAttributes.getFloat(R.styleable.Slider_android_value, this.valueFrom));
        this.stepSize = obtainStyledAttributes.getFloat(R.styleable.Slider_android_stepSize, 0.0f);
        boolean hasValue = obtainStyledAttributes.hasValue(R.styleable.Slider_trackColor);
        int i2 = hasValue ? R.styleable.Slider_trackColor : R.styleable.Slider_trackColorInactive;
        int i3 = hasValue ? R.styleable.Slider_trackColor : R.styleable.Slider_trackColorActive;
        ColorStateList colorStateList = MaterialResources.getColorStateList(context, obtainStyledAttributes, i2);
        if (colorStateList == null) {
            colorStateList = AppCompatResources.getColorStateList(context, R.color.material_slider_inactive_track_color);
        }
        setTrackColorInactive(colorStateList);
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(context, obtainStyledAttributes, i3);
        if (colorStateList2 == null) {
            colorStateList2 = AppCompatResources.getColorStateList(context, R.color.material_slider_active_track_color);
        }
        setTrackColorActive(colorStateList2);
        this.thumbDrawable.setFillColor(MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.Slider_thumbColor));
        ColorStateList colorStateList3 = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.Slider_haloColor);
        if (colorStateList3 == null) {
            colorStateList3 = AppCompatResources.getColorStateList(context, R.color.material_slider_halo_color);
        }
        setHaloColor(colorStateList3);
        boolean hasValue2 = obtainStyledAttributes.hasValue(R.styleable.Slider_tickColor);
        int i4 = hasValue2 ? R.styleable.Slider_tickColor : R.styleable.Slider_tickColorInactive;
        int i5 = hasValue2 ? R.styleable.Slider_tickColor : R.styleable.Slider_tickColorActive;
        ColorStateList colorStateList4 = MaterialResources.getColorStateList(context, obtainStyledAttributes, i4);
        if (colorStateList4 == null) {
            colorStateList4 = AppCompatResources.getColorStateList(context, R.color.material_slider_inactive_tick_marks_color);
        }
        setTickColorInactive(colorStateList4);
        ColorStateList colorStateList5 = MaterialResources.getColorStateList(context, obtainStyledAttributes, i5);
        if (colorStateList5 == null) {
            colorStateList5 = AppCompatResources.getColorStateList(context, R.color.material_slider_active_tick_marks_color);
        }
        setTickColorActive(colorStateList5);
        this.label = parseLabelDrawable(context, obtainStyledAttributes);
        setThumbRadius(obtainStyledAttributes.getDimensionPixelSize(R.styleable.Slider_thumbRadius, 0));
        setHaloRadius(obtainStyledAttributes.getDimensionPixelSize(R.styleable.Slider_haloRadius, 0));
        setThumbElevation(obtainStyledAttributes.getDimension(R.styleable.Slider_thumbElevation, 0.0f));
        setTrackHeight(obtainStyledAttributes.getDimensionPixelSize(R.styleable.Slider_trackHeight, 0));
        this.labelBehavior = obtainStyledAttributes.getInt(R.styleable.Slider_labelBehavior, 0);
        obtainStyledAttributes.recycle();
        validateValueFrom();
        validateValueTo();
        validateStepSize();
    }

    private static TooltipDrawable parseLabelDrawable(Context context, TypedArray typedArray) {
        return TooltipDrawable.createFromAttributes(context, null, 0, typedArray.getResourceId(R.styleable.Slider_labelStyle, R.style.Widget_MaterialComponents_Tooltip));
    }

    private void validateValueFrom() {
        if (this.valueFrom >= this.valueTo) {
            String str = TAG;
            String str2 = EXCEPTION_ILLEGAL_VALUE_FROM;
            Log.e(str, str2);
            throw new IllegalArgumentException(str2);
        }
    }

    private void validateValueTo() {
        if (this.valueTo <= this.valueFrom) {
            String str = TAG;
            String str2 = EXCEPTION_ILLEGAL_VALUE_TO;
            Log.e(str, str2);
            throw new IllegalArgumentException(str2);
        }
    }

    private void validateStepSize() {
        float f = this.stepSize;
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        String str = EXCEPTION_ILLEGAL_STEP_SIZE;
        if (i < 0) {
            Log.e(TAG, str);
            throw new IllegalArgumentException(str);
        } else if (f > 0.0f && ((double) (((this.valueTo - this.valueFrom) / f) % 1.0f)) > THRESHOLD) {
            Log.e(TAG, str);
            throw new IllegalArgumentException(str);
        }
    }

    public float getValueFrom() {
        return this.valueFrom;
    }

    public void setValueFrom(float f) {
        this.valueFrom = f;
        validateValueFrom();
    }

    public float getValueTo() {
        return this.valueTo;
    }

    public void setValueTo(float f) {
        this.valueTo = f;
        validateValueTo();
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float f) {
        if (isValueValid(f) && ((double) Math.abs(this.value - f)) >= THRESHOLD) {
            this.value = f;
            dispatchOnChanged(false);
            invalidate();
        }
    }

    private boolean isValueValid(float f) {
        float f2 = this.valueFrom;
        if (f < f2 || f > this.valueTo) {
            Log.e(TAG, EXCEPTION_ILLEGAL_VALUE);
            return false;
        }
        float f3 = this.stepSize;
        if (f3 <= 0.0f || ((double) (((f2 - f) / f3) % 1.0f)) <= THRESHOLD) {
            return true;
        }
        Log.e(TAG, EXCEPTION_ILLEGAL_DISCRETE_VALUE);
        return false;
    }

    public float getStepSize() {
        return this.stepSize;
    }

    public void setStepSize(float f) {
        if (this.stepSize != f) {
            this.stepSize = f;
            validateStepSize();
            if (this.trackWidth > 0) {
                calculateTicksCoordinates();
            }
            postInvalidate();
        }
    }

    public void addOnChangeListener(OnChangeListener onChangeListener) {
        this.changeListeners.add(onChangeListener);
    }

    public void removeOnChangeListener(OnChangeListener onChangeListener) {
        this.changeListeners.remove(onChangeListener);
    }

    public void clearOnChangeListeners() {
        this.changeListeners.clear();
    }

    public void addOnSliderTouchListener(OnSliderTouchListener onSliderTouchListener) {
        this.touchListeners.add(onSliderTouchListener);
    }

    public void removeOnSliderTouchListener(OnSliderTouchListener onSliderTouchListener) {
        this.touchListeners.remove(onSliderTouchListener);
    }

    public void clearOnSliderTouchListeners() {
        this.touchListeners.clear();
    }

    public boolean hasLabelFormatter() {
        return this.formatter != null;
    }

    public void setLabelFormatter(LabelFormatter labelFormatter) {
        this.formatter = labelFormatter;
    }

    public float getThumbElevation() {
        return this.thumbDrawable.getElevation();
    }

    public void setThumbElevation(float f) {
        this.thumbDrawable.setElevation(f);
    }

    public void setThumbElevationResource(int i) {
        setThumbElevation(getResources().getDimension(i));
    }

    public int getThumbRadius() {
        return this.thumbRadius;
    }

    public void setThumbRadius(int i) {
        if (i != this.thumbRadius) {
            this.thumbRadius = i;
            this.thumbDrawable.setShapeAppearanceModel(ShapeAppearanceModel.builder().setAllCorners(0, (float) this.thumbRadius).build());
            MaterialShapeDrawable materialShapeDrawable = this.thumbDrawable;
            int i2 = this.thumbRadius;
            materialShapeDrawable.setBounds(0, 0, i2 * 2, i2 * 2);
            postInvalidate();
        }
    }

    public void setThumbRadiusResource(int i) {
        setThumbRadius(getResources().getDimensionPixelSize(i));
    }

    public int getHaloRadius() {
        return this.haloRadius;
    }

    public void setHaloRadius(int i) {
        if (i != this.haloRadius) {
            this.haloRadius = i;
            if (!shouldDrawCompatHalo()) {
                Drawable background = getBackground();
                if (background instanceof RippleDrawable) {
                    DrawableUtils.setRippleDrawableRadius((RippleDrawable) background, this.haloRadius);
                }
            } else {
                postInvalidate();
            }
        }
    }

    public void setHaloRadiusResource(int i) {
        setHaloRadius(getResources().getDimensionPixelSize(i));
    }

    public int getLabelBehavior() {
        return this.labelBehavior;
    }

    public void setLabelBehavior(int i) {
        if (this.labelBehavior != i) {
            this.labelBehavior = i;
            requestLayout();
        }
    }

    public int getTrackSidePadding() {
        return this.trackSidePadding;
    }

    public int getTrackWidth() {
        return this.trackWidth;
    }

    public int getTrackHeight() {
        return this.trackHeight;
    }

    public void setTrackHeight(int i) {
        if (this.trackHeight != i) {
            this.trackHeight = i;
            invalidateTrack();
            postInvalidate();
        }
    }

    public ColorStateList getHaloColor() {
        return this.haloColor;
    }

    public void setHaloColor(ColorStateList colorStateList) {
        if (!colorStateList.equals(this.haloColor)) {
            this.haloColor = colorStateList;
            if (!shouldDrawCompatHalo()) {
                Drawable background = getBackground();
                if (background instanceof RippleDrawable) {
                    ((RippleDrawable) background).setColor(colorStateList);
                }
            } else {
                this.haloPaint.setColor(getColorForState(colorStateList));
                this.haloPaint.setAlpha(63);
                invalidate();
            }
        }
    }

    public ColorStateList getThumbColor() {
        return this.thumbDrawable.getFillColor();
    }

    public void setThumbColor(ColorStateList colorStateList) {
        this.thumbDrawable.setFillColor(colorStateList);
    }

    public ColorStateList getTickColor() {
        if (this.tickColorInactive.equals(this.tickColorActive)) {
            return this.tickColorActive;
        }
        throw new IllegalStateException("The inactive and active ticks are different colors. Use the getTickColorInactive() and getTickColorActive() methods instead.");
    }

    public void setTickColor(ColorStateList colorStateList) {
        setTickColorInactive(colorStateList);
        setTickColorActive(colorStateList);
    }

    public ColorStateList getTickColorActive() {
        return this.tickColorActive;
    }

    public void setTickColorActive(ColorStateList colorStateList) {
        if (!colorStateList.equals(this.tickColorActive)) {
            this.tickColorActive = colorStateList;
            this.activeTicksPaint.setColor(getColorForState(colorStateList));
            invalidate();
        }
    }

    public ColorStateList getTickColorInactive() {
        return this.tickColorInactive;
    }

    public void setTickColorInactive(ColorStateList colorStateList) {
        if (!colorStateList.equals(this.tickColorInactive)) {
            this.tickColorInactive = colorStateList;
            this.inactiveTicksPaint.setColor(getColorForState(colorStateList));
            invalidate();
        }
    }

    public ColorStateList getTrackColor() {
        if (this.trackColorInactive.equals(this.trackColorActive)) {
            return this.trackColorActive;
        }
        throw new IllegalStateException("The inactive and active parts of the track are different colors. Use the getInactiveTrackColor() and getActiveTrackColor() methods instead.");
    }

    public void setTrackColor(ColorStateList colorStateList) {
        setTrackColorInactive(colorStateList);
        setTrackColorActive(colorStateList);
    }

    public ColorStateList getTrackColorActive() {
        return this.trackColorActive;
    }

    public void setTrackColorActive(ColorStateList colorStateList) {
        if (!colorStateList.equals(this.trackColorActive)) {
            this.trackColorActive = colorStateList;
            this.activeTrackPaint.setColor(getColorForState(colorStateList));
            invalidate();
        }
    }

    public ColorStateList getTrackColorInactive() {
        return this.trackColorInactive;
    }

    public void setTrackColorInactive(ColorStateList colorStateList) {
        if (!colorStateList.equals(this.trackColorInactive)) {
            this.trackColorInactive = colorStateList;
            this.inactiveTrackPaint.setColor(getColorForState(colorStateList));
            invalidate();
        }
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        setLayerType(z ? 0 : 2, null);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.label.setRelativeToView(ViewUtils.getContentView(this));
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ViewUtils.getContentViewOverlay(this).remove(this.label);
        this.label.detachView(ViewUtils.getContentView(this));
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, MeasureSpec.makeMeasureSpec(this.widgetHeight + (this.labelBehavior == 1 ? this.label.getIntrinsicHeight() : 0), Ints.MAX_POWER_OF_TWO));
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.trackWidth = i - (this.trackSidePadding * 2);
        if (this.stepSize > 0.0f) {
            calculateTicksCoordinates();
        }
        updateHaloHotspot();
    }

    private void calculateTicksCoordinates() {
        int min = Math.min((int) (((this.valueTo - this.valueFrom) / this.stepSize) + 1.0f), (this.trackWidth / (this.trackHeight * 2)) + 1);
        float[] fArr = this.ticksCoordinates;
        if (fArr == null || fArr.length != min * 2) {
            this.ticksCoordinates = new float[(min * 2)];
        }
        float f = ((float) this.trackWidth) / ((float) (min - 1));
        for (int i = 0; i < min * 2; i += 2) {
            float[] fArr2 = this.ticksCoordinates;
            fArr2[i] = ((float) this.trackSidePadding) + (((float) (i / 2)) * f);
            fArr2[i + 1] = (float) calculateTop();
        }
    }

    private void updateHaloHotspot() {
        if (!shouldDrawCompatHalo() && getMeasuredWidth() > 0) {
            Drawable background = getBackground();
            if (background instanceof RippleDrawable) {
                int thumbPosition = (int) ((getThumbPosition() * ((float) this.trackWidth)) + ((float) this.trackSidePadding));
                int calculateTop = calculateTop();
                int i = this.haloRadius;
                DrawableCompat.setHotspotBounds(background, thumbPosition - i, calculateTop - i, thumbPosition + i, calculateTop + i);
            }
        }
    }

    private int calculateTop() {
        return this.trackTop + (this.labelBehavior == 1 ? this.label.getIntrinsicHeight() : 0);
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int calculateTop = calculateTop();
        drawInactiveTrack(canvas, this.trackWidth, calculateTop);
        if (getThumbPosition() > 0.0f) {
            drawActiveTrack(canvas, this.trackWidth, calculateTop);
        }
        if (this.stepSize > 0.0f) {
            drawTicks(canvas);
        }
        if ((this.thumbIsPressed || isFocused()) && isEnabled()) {
            maybeDrawHalo(canvas, this.trackWidth, calculateTop);
        }
        drawThumb(canvas, this.trackWidth, calculateTop);
    }

    private void drawInactiveTrack(Canvas canvas, int i, int i2) {
        float thumbPosition = ((float) this.trackSidePadding) + (getThumbPosition() * ((float) i));
        int i3 = this.trackSidePadding;
        if (thumbPosition < ((float) (i3 + i))) {
            float f = (float) i2;
            canvas.drawLine(thumbPosition, f, (float) (i3 + i), f, this.inactiveTrackPaint);
        }
    }

    private void drawActiveTrack(Canvas canvas, int i, int i2) {
        float f = (float) i2;
        Canvas canvas2 = canvas;
        float f2 = f;
        canvas2.drawLine((float) this.trackSidePadding, f2, ((float) this.trackSidePadding) + (getThumbPosition() * ((float) i)), f, this.activeTrackPaint);
    }

    private void drawTicks(Canvas canvas) {
        int pivotIndex = pivotIndex(this.ticksCoordinates, getThumbPosition()) * 2;
        canvas.drawPoints(this.ticksCoordinates, 0, pivotIndex, this.activeTicksPaint);
        float[] fArr = this.ticksCoordinates;
        canvas.drawPoints(fArr, pivotIndex, fArr.length - pivotIndex, this.inactiveTicksPaint);
    }

    private void drawThumb(Canvas canvas, int i, int i2) {
        if (!isEnabled()) {
            canvas.drawCircle(((float) this.trackSidePadding) + (getThumbPosition() * ((float) i)), (float) i2, (float) this.thumbRadius, this.thumbPaint);
        }
        canvas.save();
        int thumbPosition = this.trackSidePadding + ((int) (getThumbPosition() * ((float) i)));
        int i3 = this.thumbRadius;
        canvas.translate((float) (thumbPosition - i3), (float) (i2 - i3));
        this.thumbDrawable.draw(canvas);
        canvas.restore();
    }

    private void maybeDrawHalo(Canvas canvas, int i, int i2) {
        if (shouldDrawCompatHalo()) {
            int thumbPosition = (int) (((float) this.trackSidePadding) + (getThumbPosition() * ((float) i)));
            if (VERSION.SDK_INT < 28) {
                int i3 = this.haloRadius;
                canvas.clipRect((float) (thumbPosition - i3), (float) (i2 - i3), (float) (thumbPosition + i3), (float) (i3 + i2), Op.UNION);
            }
            canvas.drawCircle((float) thumbPosition, (float) i2, (float) this.haloRadius, this.haloPaint);
        }
    }

    private boolean shouldDrawCompatHalo() {
        return this.forceDrawCompatHalo || VERSION.SDK_INT < 21 || !(getBackground() instanceof RippleDrawable);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!isEnabled()) {
            return false;
        }
        float x = motionEvent.getX();
        float min = Math.min(1.0f, Math.max(0.0f, (x - ((float) this.trackSidePadding)) / ((float) this.trackWidth)));
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != 0) {
            if (actionMasked == 1) {
                this.thumbIsPressed = false;
                if (snapThumbPosition(min)) {
                    dispatchOnChanged(true);
                }
                ViewUtils.getContentViewOverlay(this).remove(this.label);
                onStopTrackingTouch();
                invalidate();
            } else if (actionMasked == 2) {
                if (!this.thumbIsPressed) {
                    if (Math.abs(x - this.touchDownX) < ((float) this.scaledTouchSlop)) {
                        return false;
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                    onStartTrackingTouch();
                }
                this.thumbIsPressed = true;
                if (snapThumbPosition(min)) {
                    dispatchOnChanged(true);
                }
                updateHaloHotspot();
                ensureLabel();
                updateLabelPosition();
                invalidate();
            }
        } else if (isInScrollingContainer()) {
            this.touchDownX = motionEvent.getX();
        } else {
            getParent().requestDisallowInterceptTouchEvent(true);
            requestFocus();
            this.thumbIsPressed = true;
            if (snapThumbPosition(min)) {
                dispatchOnChanged(true);
            }
            updateHaloHotspot();
            ensureLabel();
            updateLabelPosition();
            invalidate();
            onStartTrackingTouch();
        }
        setPressed(this.thumbIsPressed);
        return true;
    }

    private void ensureLabel() {
        float value2 = getValue();
        if (hasLabelFormatter()) {
            this.label.setText(this.formatter.getFormattedValue(value2));
        } else {
            this.label.setText(String.format(((float) ((int) value2)) == value2 ? "%.0f" : "%.2f", new Object[]{Float.valueOf(value2)}));
        }
    }

    private static int pivotIndex(float[] fArr, float f) {
        return Math.round(f * ((float) ((fArr.length / 2) - 1)));
    }

    private float getThumbPosition() {
        float f = this.value;
        float f2 = this.valueFrom;
        return (f - f2) / (this.valueTo - f2);
    }

    private boolean snapThumbPosition(float f) {
        float f2 = this.stepSize;
        if (f2 > 0.0f) {
            float f3 = (float) ((int) ((this.valueTo - this.valueFrom) / f2));
            f = ((float) Math.round(f * f3)) / f3;
        }
        if (f == getThumbPosition()) {
            return false;
        }
        float f4 = this.valueTo;
        float f5 = this.valueFrom;
        this.value = (f * (f4 - f5)) + f5;
        return true;
    }

    private void updateLabelPosition() {
        if (this.labelBehavior != 2) {
            int thumbPosition = (this.trackSidePadding + ((int) (getThumbPosition() * ((float) this.trackWidth)))) - (this.label.getIntrinsicWidth() / 2);
            int calculateTop = calculateTop() - (this.labelPadding + this.thumbRadius);
            TooltipDrawable tooltipDrawable = this.label;
            tooltipDrawable.setBounds(thumbPosition, calculateTop - tooltipDrawable.getIntrinsicHeight(), this.label.getIntrinsicWidth() + thumbPosition, calculateTop);
            Rect rect = new Rect(this.label.getBounds());
            DescendantOffsetUtils.offsetDescendantRect(ViewUtils.getContentView(this), this, rect);
            this.label.setBounds(rect);
            ViewUtils.getContentViewOverlay(this).add(this.label);
        }
    }

    private void invalidateTrack() {
        this.inactiveTrackPaint.setStrokeWidth((float) this.trackHeight);
        this.activeTrackPaint.setStrokeWidth((float) this.trackHeight);
        this.inactiveTicksPaint.setStrokeWidth(((float) this.trackHeight) / 2.0f);
        this.activeTicksPaint.setStrokeWidth(((float) this.trackHeight) / 2.0f);
    }

    private boolean isInScrollingContainer() {
        for (ViewParent parent = getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
            if (((ViewGroup) parent).shouldDelayChildPressedState()) {
                return true;
            }
        }
        return false;
    }

    private void dispatchOnChanged(boolean z) {
        float value2 = getValue();
        for (OnChangeListener onValueChange : this.changeListeners) {
            onValueChange.onValueChange(this, value2, z);
        }
    }

    private void onStartTrackingTouch() {
        for (OnSliderTouchListener onStartTrackingTouch : this.touchListeners) {
            onStartTrackingTouch.onStartTrackingTouch(this);
        }
    }

    private void onStopTrackingTouch() {
        for (OnSliderTouchListener onStopTrackingTouch : this.touchListeners) {
            onStopTrackingTouch.onStopTrackingTouch(this);
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        this.inactiveTrackPaint.setColor(getColorForState(this.trackColorInactive));
        this.activeTrackPaint.setColor(getColorForState(this.trackColorActive));
        this.inactiveTicksPaint.setColor(getColorForState(this.tickColorInactive));
        this.activeTicksPaint.setColor(getColorForState(this.tickColorActive));
        if (this.label.isStateful()) {
            this.label.setState(getDrawableState());
        }
        if (this.thumbDrawable.isStateful()) {
            this.thumbDrawable.setState(getDrawableState());
        }
        this.haloPaint.setColor(getColorForState(this.haloColor));
        this.haloPaint.setAlpha(63);
    }

    private int getColorForState(ColorStateList colorStateList) {
        return colorStateList.getColorForState(getDrawableState(), colorStateList.getDefaultColor());
    }

    /* access modifiers changed from: 0000 */
    public void forceDrawCompatHalo(boolean z) {
        this.forceDrawCompatHalo = z;
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        SliderState sliderState = new SliderState(super.onSaveInstanceState());
        sliderState.valueFrom = this.valueFrom;
        sliderState.valueTo = this.valueTo;
        sliderState.value = this.value;
        sliderState.stepSize = this.stepSize;
        sliderState.hasFocus = hasFocus();
        return sliderState;
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SliderState sliderState = (SliderState) parcelable;
        super.onRestoreInstanceState(sliderState.getSuperState());
        this.valueFrom = sliderState.valueFrom;
        this.valueTo = sliderState.valueTo;
        this.value = sliderState.value;
        this.stepSize = sliderState.stepSize;
        if (sliderState.hasFocus) {
            requestFocus();
        }
        dispatchOnChanged(false);
    }
}

package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import androidx.appcompat.R;
import androidx.core.view.GravityCompat;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import com.google.android.material.badge.BadgeDrawable;
import com.google.common.primitives.Ints;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class LinearLayoutCompat extends ViewGroup {
    private static final String ACCESSIBILITY_CLASS_NAME = "androidx.appcompat.widget.LinearLayoutCompat";
    public static final int HORIZONTAL = 0;
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private boolean mBaselineAligned;
    private int mBaselineAlignedChildIndex;
    private int mBaselineChildTop;
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    private int mGravity;
    private int[] mMaxAscent;
    private int[] mMaxDescent;
    private int mOrientation;
    private int mShowDividers;
    private int mTotalLength;
    private boolean mUseLargestChild;
    private float mWeightSum;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    public static class LayoutParams extends MarginLayoutParams {
        public int gravity;
        public float weight;

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            this.gravity = -1;
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.LinearLayoutCompat_Layout);
            this.weight = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_Layout_android_layout_weight, 0.0f);
            this.gravity = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_Layout_android_layout_gravity, -1);
            obtainStyledAttributes.recycle();
        }

        public LayoutParams(int i, int i2) {
            super(i, i2);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int i, int i2, float f) {
            super(i, i2);
            this.gravity = -1;
            this.weight = f;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams marginLayoutParams) {
            super(marginLayoutParams);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams layoutParams) {
            super(layoutParams);
            this.gravity = -1;
            this.weight = layoutParams.weight;
            this.gravity = layoutParams.gravity;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    /* access modifiers changed from: 0000 */
    public int getChildrenSkipCount(View view, int i) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int getNextLocationOffset(View view) {
        return 0;
    }

    /* access modifiers changed from: 0000 */
    public int measureNullChild(int i) {
        return 0;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public LinearLayoutCompat(Context context) {
        this(context, null);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LinearLayoutCompat(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = BadgeDrawable.TOP_START;
        TintTypedArray obtainStyledAttributes = TintTypedArray.obtainStyledAttributes(context, attributeSet, R.styleable.LinearLayoutCompat, i, 0);
        int i2 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_orientation, -1);
        if (i2 >= 0) {
            setOrientation(i2);
        }
        int i3 = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_gravity, -1);
        if (i3 >= 0) {
            setGravity(i3);
        }
        boolean z = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_android_baselineAligned, true);
        if (!z) {
            setBaselineAligned(z);
        }
        this.mWeightSum = obtainStyledAttributes.getFloat(R.styleable.LinearLayoutCompat_android_weightSum, -1.0f);
        this.mBaselineAlignedChildIndex = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_android_baselineAlignedChildIndex, -1);
        this.mUseLargestChild = obtainStyledAttributes.getBoolean(R.styleable.LinearLayoutCompat_measureWithLargestChild, false);
        setDividerDrawable(obtainStyledAttributes.getDrawable(R.styleable.LinearLayoutCompat_divider));
        this.mShowDividers = obtainStyledAttributes.getInt(R.styleable.LinearLayoutCompat_showDividers, 0);
        this.mDividerPadding = obtainStyledAttributes.getDimensionPixelSize(R.styleable.LinearLayoutCompat_dividerPadding, 0);
        obtainStyledAttributes.recycle();
    }

    public void setShowDividers(int i) {
        if (i != this.mShowDividers) {
            requestLayout();
        }
        this.mShowDividers = i;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable drawable) {
        if (drawable != this.mDivider) {
            this.mDivider = drawable;
            boolean z = false;
            if (drawable != null) {
                this.mDividerWidth = drawable.getIntrinsicWidth();
                this.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            if (drawable == null) {
                z = true;
            }
            setWillNotDraw(z);
            requestLayout();
        }
    }

    public void setDividerPadding(int i) {
        this.mDividerPadding = i;
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int i;
        int virtualChildCount = getVirtualChildCount();
        for (int i2 = 0; i2 < virtualChildCount; i2++) {
            View virtualChildAt = getVirtualChildAt(i2);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i2))) {
                drawHorizontalDivider(canvas, (virtualChildAt.getTop() - ((LayoutParams) virtualChildAt.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 == null) {
                i = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                i = virtualChildAt2.getBottom() + ((LayoutParams) virtualChildAt2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int i;
        int i2;
        int i3;
        int i4;
        int virtualChildCount = getVirtualChildCount();
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        for (int i5 = 0; i5 < virtualChildCount; i5++) {
            View virtualChildAt = getVirtualChildAt(i5);
            if (!(virtualChildAt == null || virtualChildAt.getVisibility() == 8 || !hasDividerBeforeChildAt(i5))) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (isLayoutRtl) {
                    i4 = virtualChildAt.getRight() + layoutParams.rightMargin;
                } else {
                    i4 = (virtualChildAt.getLeft() - layoutParams.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, i4);
            }
        }
        if (hasDividerBeforeChildAt(virtualChildCount)) {
            View virtualChildAt2 = getVirtualChildAt(virtualChildCount - 1);
            if (virtualChildAt2 != null) {
                LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                if (isLayoutRtl) {
                    i3 = virtualChildAt2.getLeft() - layoutParams2.leftMargin;
                    i2 = this.mDividerWidth;
                } else {
                    i = virtualChildAt2.getRight() + layoutParams2.rightMargin;
                    drawVerticalDivider(canvas, i);
                }
            } else if (isLayoutRtl) {
                i = getPaddingLeft();
                drawVerticalDivider(canvas, i);
            } else {
                i3 = getWidth() - getPaddingRight();
                i2 = this.mDividerWidth;
            }
            i = i3 - i2;
            drawVerticalDivider(canvas, i);
        }
    }

    /* access modifiers changed from: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, i, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + i);
        this.mDivider.draw(canvas);
    }

    /* access modifiers changed from: 0000 */
    public void drawVerticalDivider(Canvas canvas, int i) {
        this.mDivider.setBounds(i, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + i, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    public void setBaselineAligned(boolean z) {
        this.mBaselineAligned = z;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    public void setMeasureWithLargestChildEnabled(boolean z) {
        this.mUseLargestChild = z;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View childAt = getChildAt(i);
            int baseline = childAt.getBaseline();
            if (baseline != -1) {
                int i2 = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int i3 = this.mGravity & 112;
                    if (i3 != 48) {
                        if (i3 == 16) {
                            i2 += ((((getBottom() - getTop()) - getPaddingTop()) - getPaddingBottom()) - this.mTotalLength) / 2;
                        } else if (i3 == 80) {
                            i2 = ((getBottom() - getTop()) - getPaddingBottom()) - this.mTotalLength;
                        }
                    }
                }
                return i2 + ((LayoutParams) childAt.getLayoutParams()).topMargin + baseline;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        } else {
            throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
        }
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder sb = new StringBuilder();
            sb.append("base aligned child index out of range (0, ");
            sb.append(getChildCount());
            sb.append(")");
            throw new IllegalArgumentException(sb.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* access modifiers changed from: 0000 */
    public View getVirtualChildAt(int i) {
        return getChildAt(i);
    }

    /* access modifiers changed from: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    public void setWeightSum(float f) {
        this.mWeightSum = Math.max(0.0f, f);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.mOrientation == 1) {
            measureVertical(i, i2);
        } else {
            measureHorizontal(i, i2);
        }
    }

    /* access modifiers changed from: protected */
    public boolean hasDividerBeforeChildAt(int i) {
        boolean z = false;
        if (i == 0) {
            if ((this.mShowDividers & 1) != 0) {
                z = true;
            }
            return z;
        } else if (i == getChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        } else {
            if ((this.mShowDividers & 2) != 0) {
                int i2 = i - 1;
                while (true) {
                    if (i2 < 0) {
                        break;
                    } else if (getChildAt(i2).getVisibility() != 8) {
                        z = true;
                        break;
                    } else {
                        i2--;
                    }
                }
            }
            return z;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x032b  */
    public void measureVertical(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        boolean z;
        int i10;
        int i11;
        int i12;
        int i13;
        int i14;
        int i15;
        int i16;
        int i17;
        View view;
        int i18;
        boolean z2;
        int i19;
        int i20;
        int i21 = i;
        int i22 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        int i23 = this.mBaselineAlignedChildIndex;
        boolean z3 = this.mUseLargestChild;
        int i24 = 0;
        int i25 = 0;
        int i26 = 0;
        int i27 = 0;
        int i28 = 0;
        int i29 = 0;
        boolean z4 = false;
        boolean z5 = false;
        float f = 0.0f;
        boolean z6 = true;
        while (true) {
            int i30 = 8;
            int i31 = i27;
            if (i29 < virtualChildCount) {
                View virtualChildAt = getVirtualChildAt(i29);
                if (virtualChildAt == null) {
                    this.mTotalLength += measureNullChild(i29);
                    i12 = virtualChildCount;
                    i11 = mode2;
                    i27 = i31;
                } else {
                    int i32 = i24;
                    if (virtualChildAt.getVisibility() == 8) {
                        i29 += getChildrenSkipCount(virtualChildAt, i29);
                        i12 = virtualChildCount;
                        i27 = i31;
                        i24 = i32;
                        i11 = mode2;
                    } else {
                        if (hasDividerBeforeChildAt(i29)) {
                            this.mTotalLength += this.mDividerHeight;
                        }
                        LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                        float f2 = f + layoutParams.weight;
                        if (mode2 == 1073741824 && layoutParams.height == 0 && layoutParams.weight > 0.0f) {
                            int i33 = this.mTotalLength;
                            int i34 = i25;
                            this.mTotalLength = Math.max(i33, layoutParams.topMargin + i33 + layoutParams.bottomMargin);
                            i18 = i26;
                            view = virtualChildAt;
                            i13 = i28;
                            i12 = virtualChildCount;
                            z4 = true;
                            i17 = i32;
                            i14 = i34;
                            i16 = i29;
                            i11 = mode2;
                            i15 = i31;
                        } else {
                            int i35 = i25;
                            if (layoutParams.height != 0 || layoutParams.weight <= 0.0f) {
                                i20 = Integer.MIN_VALUE;
                            } else {
                                layoutParams.height = -2;
                                i20 = 0;
                            }
                            i17 = i32;
                            int i36 = i20;
                            i14 = i35;
                            int i37 = i26;
                            i11 = mode2;
                            i15 = i31;
                            View view2 = virtualChildAt;
                            i12 = virtualChildCount;
                            i13 = i28;
                            i16 = i29;
                            measureChildBeforeLayout(virtualChildAt, i29, i, 0, i2, f2 == 0.0f ? this.mTotalLength : 0);
                            int i38 = i36;
                            if (i38 != Integer.MIN_VALUE) {
                                layoutParams.height = i38;
                            }
                            int measuredHeight = view2.getMeasuredHeight();
                            int i39 = this.mTotalLength;
                            view = view2;
                            this.mTotalLength = Math.max(i39, i39 + measuredHeight + layoutParams.topMargin + layoutParams.bottomMargin + getNextLocationOffset(view));
                            i18 = z3 ? Math.max(measuredHeight, i37) : i37;
                        }
                        if (i23 >= 0 && i23 == i16 + 1) {
                            this.mBaselineChildTop = this.mTotalLength;
                        }
                        if (i16 >= i23 || layoutParams.weight <= 0.0f) {
                            if (mode == 1073741824 || layoutParams.width != -1) {
                                z2 = false;
                            } else {
                                z2 = true;
                                z5 = true;
                            }
                            int i40 = layoutParams.leftMargin + layoutParams.rightMargin;
                            int measuredWidth = view.getMeasuredWidth() + i40;
                            int max = Math.max(i14, measuredWidth);
                            int combineMeasuredStates = View.combineMeasuredStates(i17, view.getMeasuredState());
                            z6 = z6 && layoutParams.width == -1;
                            if (layoutParams.weight > 0.0f) {
                                if (!z2) {
                                    i40 = measuredWidth;
                                }
                                i27 = Math.max(i15, i40);
                                i19 = i13;
                            } else {
                                if (!z2) {
                                    i40 = measuredWidth;
                                }
                                i19 = Math.max(i13, i40);
                                i27 = i15;
                            }
                            i26 = i18;
                            f = f2;
                            int i41 = max;
                            i28 = i19;
                            i24 = combineMeasuredStates;
                            i29 = getChildrenSkipCount(view, i16) + i16;
                            i25 = i41;
                        } else {
                            throw new RuntimeException("A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.");
                        }
                    }
                }
                i29++;
                int i42 = i;
                int i43 = i2;
                virtualChildCount = i12;
                mode2 = i11;
            } else {
                int i44 = i24;
                int i45 = i26;
                int i46 = i28;
                int i47 = virtualChildCount;
                int i48 = mode2;
                int i49 = i31;
                int i50 = i25;
                if (this.mTotalLength > 0) {
                    i3 = i47;
                    if (hasDividerBeforeChildAt(i3)) {
                        this.mTotalLength += this.mDividerHeight;
                    }
                } else {
                    i3 = i47;
                }
                int i51 = i48;
                if (z3 && (i51 == Integer.MIN_VALUE || i51 == 0)) {
                    this.mTotalLength = 0;
                    int i52 = 0;
                    while (i52 < i3) {
                        View virtualChildAt2 = getVirtualChildAt(i52);
                        if (virtualChildAt2 == null) {
                            this.mTotalLength += measureNullChild(i52);
                        } else if (virtualChildAt2.getVisibility() == i30) {
                            i52 += getChildrenSkipCount(virtualChildAt2, i52);
                        } else {
                            LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                            int i53 = this.mTotalLength;
                            this.mTotalLength = Math.max(i53, i53 + i45 + layoutParams2.topMargin + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt2));
                        }
                        i52++;
                        i30 = 8;
                    }
                }
                int paddingTop = this.mTotalLength + getPaddingTop() + getPaddingBottom();
                this.mTotalLength = paddingTop;
                int i54 = i2;
                int i55 = i45;
                int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingTop, getSuggestedMinimumHeight()), i54, 0);
                int i56 = (16777215 & resolveSizeAndState) - this.mTotalLength;
                if (z4 || (i56 != 0 && f > 0.0f)) {
                    float f3 = this.mWeightSum;
                    if (f3 > 0.0f) {
                        f = f3;
                    }
                    this.mTotalLength = 0;
                    int i57 = i56;
                    int i58 = i46;
                    i5 = i44;
                    int i59 = 0;
                    while (i59 < i3) {
                        View virtualChildAt3 = getVirtualChildAt(i59);
                        if (virtualChildAt3.getVisibility() == 8) {
                            i7 = i57;
                            int i60 = i;
                        } else {
                            LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                            float f4 = layoutParams3.weight;
                            if (f4 > 0.0f) {
                                int i61 = (int) ((((float) i57) * f4) / f);
                                float f5 = f - f4;
                                i7 = i57 - i61;
                                int childMeasureSpec = getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight() + layoutParams3.leftMargin + layoutParams3.rightMargin, layoutParams3.width);
                                if (layoutParams3.height == 0) {
                                    i10 = Ints.MAX_POWER_OF_TWO;
                                    if (i51 == 1073741824) {
                                        if (i61 <= 0) {
                                            i61 = 0;
                                        }
                                        virtualChildAt3.measure(childMeasureSpec, MeasureSpec.makeMeasureSpec(i61, Ints.MAX_POWER_OF_TWO));
                                        i5 = View.combineMeasuredStates(i5, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                        f = f5;
                                    }
                                } else {
                                    i10 = Ints.MAX_POWER_OF_TWO;
                                }
                                int measuredHeight2 = virtualChildAt3.getMeasuredHeight() + i61;
                                if (measuredHeight2 < 0) {
                                    measuredHeight2 = 0;
                                }
                                virtualChildAt3.measure(childMeasureSpec, MeasureSpec.makeMeasureSpec(measuredHeight2, i10));
                                i5 = View.combineMeasuredStates(i5, virtualChildAt3.getMeasuredState() & InputDeviceCompat.SOURCE_ANY);
                                f = f5;
                            } else {
                                int i62 = i57;
                                int i63 = i;
                                i7 = i62;
                            }
                            int i64 = layoutParams3.leftMargin + layoutParams3.rightMargin;
                            int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i64;
                            i50 = Math.max(i50, measuredWidth2);
                            float f6 = f;
                            if (mode != 1073741824) {
                                i8 = i5;
                                i9 = -1;
                                if (layoutParams3.width == -1) {
                                    z = true;
                                    if (!z) {
                                        i64 = measuredWidth2;
                                    }
                                    int max2 = Math.max(i58, i64);
                                    boolean z7 = !z6 && layoutParams3.width == i9;
                                    int i65 = this.mTotalLength;
                                    this.mTotalLength = Math.max(i65, virtualChildAt3.getMeasuredHeight() + i65 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                                    z6 = z7;
                                    i5 = i8;
                                    i58 = max2;
                                    f = f6;
                                }
                            } else {
                                i8 = i5;
                                i9 = -1;
                            }
                            z = false;
                            if (!z) {
                            }
                            int max22 = Math.max(i58, i64);
                            if (!z6) {
                            }
                            int i652 = this.mTotalLength;
                            this.mTotalLength = Math.max(i652, virtualChildAt3.getMeasuredHeight() + i652 + layoutParams3.topMargin + layoutParams3.bottomMargin + getNextLocationOffset(virtualChildAt3));
                            z6 = z7;
                            i5 = i8;
                            i58 = max22;
                            f = f6;
                        }
                        i59++;
                        i57 = i7;
                    }
                    i4 = i;
                    this.mTotalLength += getPaddingTop() + getPaddingBottom();
                    i6 = i58;
                } else {
                    i6 = Math.max(i46, i49);
                    if (z3 && i51 != 1073741824) {
                        for (int i66 = 0; i66 < i3; i66++) {
                            View virtualChildAt4 = getVirtualChildAt(i66);
                            if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                                virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredWidth(), Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(i55, Ints.MAX_POWER_OF_TWO));
                            }
                        }
                    }
                    i4 = i;
                    i5 = i44;
                }
                if (z6 || mode == 1073741824) {
                    i6 = i50;
                }
                setMeasuredDimension(View.resolveSizeAndState(Math.max(i6 + getPaddingLeft() + getPaddingRight(), getSuggestedMinimumWidth()), i4, i5), resolveSizeAndState);
                if (z5) {
                    forceUniformWidth(i3, i54);
                    return;
                }
                return;
            }
        }
    }

    private void forceUniformWidth(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), Ints.MAX_POWER_OF_TWO);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.width == -1) {
                    int i4 = layoutParams.height;
                    layoutParams.height = virtualChildAt.getMeasuredHeight();
                    measureChildWithMargins(virtualChildAt, makeMeasureSpec, 0, i2, 0);
                    layoutParams.height = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:183:0x0450  */
    /* JADX WARNING: Removed duplicated region for block: B:61:0x0192  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x01d4  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01df  */
    public void measureHorizontal(int i, int i2) {
        int[] iArr;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        float f;
        int i12;
        boolean z;
        int i13;
        int i14;
        boolean z2;
        boolean z3;
        int i15;
        int i16;
        int i17;
        View view;
        boolean z4;
        char c;
        int i18;
        int i19 = i;
        int i20 = i2;
        this.mTotalLength = 0;
        int virtualChildCount = getVirtualChildCount();
        int mode = MeasureSpec.getMode(i);
        int mode2 = MeasureSpec.getMode(i2);
        if (this.mMaxAscent == null || this.mMaxDescent == null) {
            this.mMaxAscent = new int[4];
            this.mMaxDescent = new int[4];
        }
        int[] iArr2 = this.mMaxAscent;
        int[] iArr3 = this.mMaxDescent;
        iArr2[3] = -1;
        iArr2[2] = -1;
        iArr2[1] = -1;
        iArr2[0] = -1;
        iArr3[3] = -1;
        iArr3[2] = -1;
        iArr3[1] = -1;
        iArr3[0] = -1;
        boolean z5 = this.mBaselineAligned;
        boolean z6 = this.mUseLargestChild;
        int i21 = Ints.MAX_POWER_OF_TWO;
        boolean z7 = mode == 1073741824;
        int i22 = 0;
        int i23 = 0;
        int i24 = 0;
        int i25 = 0;
        int i26 = 0;
        boolean z8 = false;
        int i27 = 0;
        boolean z9 = false;
        boolean z10 = true;
        float f2 = 0.0f;
        while (true) {
            iArr = iArr3;
            if (i22 >= virtualChildCount) {
                break;
            }
            View virtualChildAt = getVirtualChildAt(i22);
            if (virtualChildAt == null) {
                this.mTotalLength += measureNullChild(i22);
            } else if (virtualChildAt.getVisibility() == 8) {
                i22 += getChildrenSkipCount(virtualChildAt, i22);
            } else {
                if (hasDividerBeforeChildAt(i22)) {
                    this.mTotalLength += this.mDividerWidth;
                }
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                float f3 = f2 + layoutParams.weight;
                if (mode == i21 && layoutParams.width == 0 && layoutParams.weight > 0.0f) {
                    if (z7) {
                        this.mTotalLength += layoutParams.leftMargin + layoutParams.rightMargin;
                    } else {
                        int i28 = this.mTotalLength;
                        this.mTotalLength = Math.max(i28, layoutParams.leftMargin + i28 + layoutParams.rightMargin);
                    }
                    if (z5) {
                        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(0, 0);
                        virtualChildAt.measure(makeMeasureSpec, makeMeasureSpec);
                        i17 = i22;
                        z3 = z6;
                        z2 = z5;
                        view = virtualChildAt;
                    } else {
                        i17 = i22;
                        z3 = z6;
                        z2 = z5;
                        view = virtualChildAt;
                        z8 = true;
                        i16 = Ints.MAX_POWER_OF_TWO;
                        if (mode2 == i16 && layoutParams.height == -1) {
                            z4 = true;
                            z9 = true;
                        } else {
                            z4 = false;
                        }
                        int i29 = layoutParams.topMargin + layoutParams.bottomMargin;
                        int measuredHeight = view.getMeasuredHeight() + i29;
                        i27 = View.combineMeasuredStates(i27, view.getMeasuredState());
                        if (z2) {
                            int baseline = view.getBaseline();
                            if (baseline != -1) {
                                int i30 = ((((layoutParams.gravity < 0 ? this.mGravity : layoutParams.gravity) & 112) >> 4) & -2) >> 1;
                                iArr2[i30] = Math.max(iArr2[i30], baseline);
                                iArr[i30] = Math.max(iArr[i30], measuredHeight - baseline);
                            }
                        }
                        i24 = Math.max(i24, measuredHeight);
                        z10 = !z10 && layoutParams.height == -1;
                        if (layoutParams.weight <= 0.0f) {
                            if (!z4) {
                                i29 = measuredHeight;
                            }
                            i26 = Math.max(i26, i29);
                        } else {
                            int i31 = i26;
                            if (!z4) {
                                i29 = measuredHeight;
                            }
                            i25 = Math.max(i25, i29);
                            i26 = i31;
                        }
                        int i32 = i17;
                        i15 = getChildrenSkipCount(view, i32) + i32;
                        f2 = f3;
                        int i33 = i2;
                        iArr3 = iArr;
                        z6 = z3;
                        z5 = z2;
                        int i34 = i15 + 1;
                        i21 = i16;
                        i22 = i34;
                    }
                } else {
                    if (layoutParams.width != 0 || layoutParams.weight <= 0.0f) {
                        c = 65534;
                        i18 = Integer.MIN_VALUE;
                    } else {
                        c = 65534;
                        layoutParams.width = -2;
                        i18 = 0;
                    }
                    i17 = i22;
                    int i35 = i18;
                    z3 = z6;
                    z2 = z5;
                    char c2 = c;
                    View view2 = virtualChildAt;
                    measureChildBeforeLayout(virtualChildAt, i17, i, f3 == 0.0f ? this.mTotalLength : 0, i2, 0);
                    int i36 = i35;
                    if (i36 != Integer.MIN_VALUE) {
                        layoutParams.width = i36;
                    }
                    int measuredWidth = view2.getMeasuredWidth();
                    if (z7) {
                        view = view2;
                        this.mTotalLength += layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin + getNextLocationOffset(view);
                    } else {
                        view = view2;
                        int i37 = this.mTotalLength;
                        this.mTotalLength = Math.max(i37, i37 + measuredWidth + layoutParams.leftMargin + layoutParams.rightMargin + getNextLocationOffset(view));
                    }
                    if (z3) {
                        i23 = Math.max(measuredWidth, i23);
                    }
                }
                i16 = Ints.MAX_POWER_OF_TWO;
                if (mode2 == i16) {
                }
                z4 = false;
                int i292 = layoutParams.topMargin + layoutParams.bottomMargin;
                int measuredHeight2 = view.getMeasuredHeight() + i292;
                i27 = View.combineMeasuredStates(i27, view.getMeasuredState());
                if (z2) {
                }
                i24 = Math.max(i24, measuredHeight2);
                if (!z10) {
                }
                if (layoutParams.weight <= 0.0f) {
                }
                int i322 = i17;
                i15 = getChildrenSkipCount(view, i322) + i322;
                f2 = f3;
                int i332 = i2;
                iArr3 = iArr;
                z6 = z3;
                z5 = z2;
                int i342 = i15 + 1;
                i21 = i16;
                i22 = i342;
            }
            z3 = z6;
            z2 = z5;
            int i38 = i21;
            i15 = i22;
            i16 = i38;
            int i3322 = i2;
            iArr3 = iArr;
            z6 = z3;
            z5 = z2;
            int i3422 = i15 + 1;
            i21 = i16;
            i22 = i3422;
        }
        int i39 = i21;
        boolean z11 = z6;
        boolean z12 = z5;
        int i40 = i24;
        int i41 = i25;
        int i42 = i26;
        int i43 = i27;
        if (this.mTotalLength > 0 && hasDividerBeforeChildAt(virtualChildCount)) {
            this.mTotalLength += this.mDividerWidth;
        }
        if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
            i4 = i40;
            i3 = i43;
        } else {
            i3 = i43;
            i4 = Math.max(i40, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
        }
        if (z11 && (mode == Integer.MIN_VALUE || mode == 0)) {
            this.mTotalLength = 0;
            int i44 = 0;
            while (i44 < virtualChildCount) {
                View virtualChildAt2 = getVirtualChildAt(i44);
                if (virtualChildAt2 == null) {
                    this.mTotalLength += measureNullChild(i44);
                } else if (virtualChildAt2.getVisibility() == 8) {
                    i44 += getChildrenSkipCount(virtualChildAt2, i44);
                } else {
                    LayoutParams layoutParams2 = (LayoutParams) virtualChildAt2.getLayoutParams();
                    if (z7) {
                        this.mTotalLength += layoutParams2.leftMargin + i23 + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2);
                    } else {
                        int i45 = this.mTotalLength;
                        i14 = i4;
                        this.mTotalLength = Math.max(i45, i45 + i23 + layoutParams2.leftMargin + layoutParams2.rightMargin + getNextLocationOffset(virtualChildAt2));
                        i44++;
                        i4 = i14;
                    }
                }
                i14 = i4;
                i44++;
                i4 = i14;
            }
        }
        int i46 = i4;
        int paddingLeft = this.mTotalLength + getPaddingLeft() + getPaddingRight();
        this.mTotalLength = paddingLeft;
        int resolveSizeAndState = View.resolveSizeAndState(Math.max(paddingLeft, getSuggestedMinimumWidth()), i19, 0);
        int i47 = (16777215 & resolveSizeAndState) - this.mTotalLength;
        if (z8 || (i47 != 0 && f2 > 0.0f)) {
            float f4 = this.mWeightSum;
            if (f4 > 0.0f) {
                f2 = f4;
            }
            iArr2[3] = -1;
            iArr2[2] = -1;
            iArr2[1] = -1;
            iArr2[0] = -1;
            iArr[3] = -1;
            iArr[2] = -1;
            iArr[1] = -1;
            iArr[0] = -1;
            this.mTotalLength = 0;
            int i48 = i41;
            int i49 = -1;
            int i50 = i3;
            int i51 = 0;
            while (i51 < virtualChildCount) {
                View virtualChildAt3 = getVirtualChildAt(i51);
                if (virtualChildAt3 == null || virtualChildAt3.getVisibility() == 8) {
                    i11 = i47;
                    i10 = virtualChildCount;
                    int i52 = i2;
                } else {
                    LayoutParams layoutParams3 = (LayoutParams) virtualChildAt3.getLayoutParams();
                    float f5 = layoutParams3.weight;
                    if (f5 > 0.0f) {
                        int i53 = (int) ((((float) i47) * f5) / f2);
                        float f6 = f2 - f5;
                        int i54 = i47 - i53;
                        i10 = virtualChildCount;
                        int childMeasureSpec = getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom() + layoutParams3.topMargin + layoutParams3.bottomMargin, layoutParams3.height);
                        if (layoutParams3.width == 0) {
                            i13 = Ints.MAX_POWER_OF_TWO;
                            if (mode == 1073741824) {
                                if (i53 <= 0) {
                                    i53 = 0;
                                }
                                virtualChildAt3.measure(MeasureSpec.makeMeasureSpec(i53, Ints.MAX_POWER_OF_TWO), childMeasureSpec);
                                i50 = View.combineMeasuredStates(i50, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                                f2 = f6;
                                i11 = i54;
                            }
                        } else {
                            i13 = Ints.MAX_POWER_OF_TWO;
                        }
                        int measuredWidth2 = virtualChildAt3.getMeasuredWidth() + i53;
                        if (measuredWidth2 < 0) {
                            measuredWidth2 = 0;
                        }
                        virtualChildAt3.measure(MeasureSpec.makeMeasureSpec(measuredWidth2, i13), childMeasureSpec);
                        i50 = View.combineMeasuredStates(i50, virtualChildAt3.getMeasuredState() & ViewCompat.MEASURED_STATE_MASK);
                        f2 = f6;
                        i11 = i54;
                    } else {
                        i11 = i47;
                        i10 = virtualChildCount;
                        int i55 = i2;
                    }
                    if (z7) {
                        this.mTotalLength += virtualChildAt3.getMeasuredWidth() + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3);
                        f = f2;
                    } else {
                        int i56 = this.mTotalLength;
                        f = f2;
                        this.mTotalLength = Math.max(i56, virtualChildAt3.getMeasuredWidth() + i56 + layoutParams3.leftMargin + layoutParams3.rightMargin + getNextLocationOffset(virtualChildAt3));
                    }
                    boolean z13 = mode2 != 1073741824 && layoutParams3.height == -1;
                    int i57 = layoutParams3.topMargin + layoutParams3.bottomMargin;
                    int measuredHeight3 = virtualChildAt3.getMeasuredHeight() + i57;
                    i49 = Math.max(i49, measuredHeight3);
                    if (!z13) {
                        i57 = measuredHeight3;
                    }
                    int max = Math.max(i48, i57);
                    if (z10) {
                        i12 = -1;
                        if (layoutParams3.height == -1) {
                            z = true;
                            if (z12) {
                                int baseline2 = virtualChildAt3.getBaseline();
                                if (baseline2 != i12) {
                                    int i58 = ((((layoutParams3.gravity < 0 ? this.mGravity : layoutParams3.gravity) & 112) >> 4) & -2) >> 1;
                                    iArr2[i58] = Math.max(iArr2[i58], baseline2);
                                    iArr[i58] = Math.max(iArr[i58], measuredHeight3 - baseline2);
                                }
                            }
                            z10 = z;
                            i48 = max;
                            f2 = f;
                        }
                    } else {
                        i12 = -1;
                    }
                    z = false;
                    if (z12) {
                    }
                    z10 = z;
                    i48 = max;
                    f2 = f;
                }
                i51++;
                int i59 = i;
                i47 = i11;
                virtualChildCount = i10;
            }
            i7 = i2;
            i5 = virtualChildCount;
            this.mTotalLength += getPaddingLeft() + getPaddingRight();
            if (iArr2[1] == -1 && iArr2[0] == -1 && iArr2[2] == -1 && iArr2[3] == -1) {
                i9 = i49;
            } else {
                i9 = Math.max(i49, Math.max(iArr2[3], Math.max(iArr2[0], Math.max(iArr2[1], iArr2[2]))) + Math.max(iArr[3], Math.max(iArr[0], Math.max(iArr[1], iArr[2]))));
            }
            i6 = i9;
            i8 = i48;
            i3 = i50;
        } else {
            i8 = Math.max(i41, i42);
            if (z11 && mode != 1073741824) {
                for (int i60 = 0; i60 < virtualChildCount; i60++) {
                    View virtualChildAt4 = getVirtualChildAt(i60);
                    if (!(virtualChildAt4 == null || virtualChildAt4.getVisibility() == 8 || ((LayoutParams) virtualChildAt4.getLayoutParams()).weight <= 0.0f)) {
                        virtualChildAt4.measure(MeasureSpec.makeMeasureSpec(i23, Ints.MAX_POWER_OF_TWO), MeasureSpec.makeMeasureSpec(virtualChildAt4.getMeasuredHeight(), Ints.MAX_POWER_OF_TWO));
                    }
                }
            }
            i7 = i2;
            i5 = virtualChildCount;
            i6 = i46;
        }
        if (z10 || mode2 == 1073741824) {
            i8 = i6;
        }
        setMeasuredDimension(resolveSizeAndState | (i3 & ViewCompat.MEASURED_STATE_MASK), View.resolveSizeAndState(Math.max(i8 + getPaddingTop() + getPaddingBottom(), getSuggestedMinimumHeight()), i7, i3 << 16));
        if (z9) {
            forceUniformHeight(i5, i);
        }
    }

    private void forceUniformHeight(int i, int i2) {
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), Ints.MAX_POWER_OF_TWO);
        for (int i3 = 0; i3 < i; i3++) {
            View virtualChildAt = getVirtualChildAt(i3);
            if (virtualChildAt.getVisibility() != 8) {
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                if (layoutParams.height == -1) {
                    int i4 = layoutParams.width;
                    layoutParams.width = virtualChildAt.getMeasuredWidth();
                    measureChildWithMargins(virtualChildAt, i2, 0, makeMeasureSpec, 0);
                    layoutParams.width = i4;
                }
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void measureChildBeforeLayout(View view, int i, int i2, int i3, int i4, int i5) {
        measureChildWithMargins(view, i2, i3, i4, i5);
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (this.mOrientation == 1) {
            layoutVertical(i, i2, i3, i4);
        } else {
            layoutHorizontal(i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x009f  */
    public void layoutVertical(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int paddingLeft = getPaddingLeft();
        int i10 = i3 - i;
        int paddingRight = i10 - getPaddingRight();
        int paddingRight2 = (i10 - paddingLeft) - getPaddingRight();
        int virtualChildCount = getVirtualChildCount();
        int i11 = this.mGravity;
        int i12 = i11 & 112;
        int i13 = i11 & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (i12 == 16) {
            i5 = getPaddingTop() + (((i4 - i2) - this.mTotalLength) / 2);
        } else if (i12 != 80) {
            i5 = getPaddingTop();
        } else {
            i5 = ((getPaddingTop() + i4) - i2) - this.mTotalLength;
        }
        int i14 = 0;
        while (i14 < virtualChildCount) {
            View virtualChildAt = getVirtualChildAt(i14);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i14);
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i15 = layoutParams.gravity;
                if (i15 < 0) {
                    i15 = i13;
                }
                int absoluteGravity = GravityCompat.getAbsoluteGravity(i15, ViewCompat.getLayoutDirection(this)) & 7;
                if (absoluteGravity == 1) {
                    i9 = ((paddingRight2 - measuredWidth) / 2) + paddingLeft + layoutParams.leftMargin;
                    i8 = layoutParams.rightMargin;
                } else if (absoluteGravity != 5) {
                    i7 = layoutParams.leftMargin + paddingLeft;
                    int i16 = i7;
                    if (hasDividerBeforeChildAt(i14)) {
                        i5 += this.mDividerHeight;
                    }
                    int i17 = i5 + layoutParams.topMargin;
                    LayoutParams layoutParams2 = layoutParams;
                    setChildFrame(virtualChildAt, i16, i17 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                    i14 += getChildrenSkipCount(virtualChildAt, i14);
                    i5 = i17 + measuredHeight + layoutParams2.bottomMargin + getNextLocationOffset(virtualChildAt);
                    i6 = 1;
                    i14 += i6;
                } else {
                    i9 = paddingRight - measuredWidth;
                    i8 = layoutParams.rightMargin;
                }
                i7 = i9 - i8;
                int i162 = i7;
                if (hasDividerBeforeChildAt(i14)) {
                }
                int i172 = i5 + layoutParams.topMargin;
                LayoutParams layoutParams22 = layoutParams;
                setChildFrame(virtualChildAt, i162, i172 + getLocationOffset(virtualChildAt), measuredWidth, measuredHeight);
                i14 += getChildrenSkipCount(virtualChildAt, i14);
                i5 = i172 + measuredHeight + layoutParams22.bottomMargin + getNextLocationOffset(virtualChildAt);
                i6 = 1;
                i14 += i6;
            }
            i6 = 1;
            i14 += i6;
        }
    }

    /* access modifiers changed from: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00af  */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x00b8  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00eb  */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ff  */
    public void layoutHorizontal(int i, int i2, int i3, int i4) {
        int i5;
        int i6;
        int i7;
        int i8;
        boolean z;
        int i9;
        int i10;
        int i11;
        boolean z2;
        int i12;
        int i13;
        int i14;
        int i15;
        boolean isLayoutRtl = ViewUtils.isLayoutRtl(this);
        int paddingTop = getPaddingTop();
        int i16 = i4 - i2;
        int paddingBottom = i16 - getPaddingBottom();
        int paddingBottom2 = (i16 - paddingTop) - getPaddingBottom();
        int virtualChildCount = getVirtualChildCount();
        int i17 = this.mGravity;
        int i18 = 8388615 & i17;
        int i19 = i17 & 112;
        boolean z3 = this.mBaselineAligned;
        int[] iArr = this.mMaxAscent;
        int[] iArr2 = this.mMaxDescent;
        int absoluteGravity = GravityCompat.getAbsoluteGravity(i18, ViewCompat.getLayoutDirection(this));
        boolean z4 = true;
        if (absoluteGravity == 1) {
            i5 = getPaddingLeft() + (((i3 - i) - this.mTotalLength) / 2);
        } else if (absoluteGravity != 5) {
            i5 = getPaddingLeft();
        } else {
            i5 = ((getPaddingLeft() + i3) - i) - this.mTotalLength;
        }
        if (isLayoutRtl) {
            i7 = virtualChildCount - 1;
            i6 = -1;
        } else {
            i7 = 0;
            i6 = 1;
        }
        int i20 = 0;
        while (i20 < virtualChildCount) {
            int i21 = i7 + (i6 * i20);
            View virtualChildAt = getVirtualChildAt(i21);
            if (virtualChildAt == null) {
                i5 += measureNullChild(i21);
                z2 = z4;
                i8 = paddingTop;
                i11 = virtualChildCount;
                i9 = i19;
            } else if (virtualChildAt.getVisibility() != 8) {
                int measuredWidth = virtualChildAt.getMeasuredWidth();
                int measuredHeight = virtualChildAt.getMeasuredHeight();
                LayoutParams layoutParams = (LayoutParams) virtualChildAt.getLayoutParams();
                int i22 = i20;
                if (z3) {
                    i10 = virtualChildCount;
                    if (layoutParams.height != -1) {
                        i12 = virtualChildAt.getBaseline();
                        i13 = layoutParams.gravity;
                        if (i13 < 0) {
                            i13 = i19;
                        }
                        i14 = i13 & 112;
                        i9 = i19;
                        if (i14 == 16) {
                            if (i14 == 48) {
                                i15 = layoutParams.topMargin + paddingTop;
                                if (i12 != -1) {
                                    z = true;
                                    i15 += iArr[1] - i12;
                                }
                            } else if (i14 != 80) {
                                i15 = paddingTop;
                            } else {
                                i15 = (paddingBottom - measuredHeight) - layoutParams.bottomMargin;
                                if (i12 != -1) {
                                    i15 -= iArr2[2] - (virtualChildAt.getMeasuredHeight() - i12);
                                }
                            }
                            z = true;
                        } else {
                            z = true;
                            i15 = ((((paddingBottom2 - measuredHeight) / 2) + paddingTop) + layoutParams.topMargin) - layoutParams.bottomMargin;
                        }
                        if (hasDividerBeforeChildAt(i21)) {
                            i5 += this.mDividerWidth;
                        }
                        int i23 = layoutParams.leftMargin + i5;
                        View view = virtualChildAt;
                        View view2 = view;
                        int i24 = i21;
                        int locationOffset = i23 + getLocationOffset(virtualChildAt);
                        i8 = paddingTop;
                        LayoutParams layoutParams2 = layoutParams;
                        setChildFrame(view, locationOffset, i15, measuredWidth, measuredHeight);
                        View view3 = view2;
                        i20 = i22 + getChildrenSkipCount(view3, i24);
                        i5 = i23 + measuredWidth + layoutParams2.rightMargin + getNextLocationOffset(view3);
                        i20++;
                        virtualChildCount = i10;
                        i19 = i9;
                        z4 = z;
                        paddingTop = i8;
                    }
                } else {
                    i10 = virtualChildCount;
                }
                i12 = -1;
                i13 = layoutParams.gravity;
                if (i13 < 0) {
                }
                i14 = i13 & 112;
                i9 = i19;
                if (i14 == 16) {
                }
                if (hasDividerBeforeChildAt(i21)) {
                }
                int i232 = layoutParams.leftMargin + i5;
                View view4 = virtualChildAt;
                View view22 = view4;
                int i242 = i21;
                int locationOffset2 = i232 + getLocationOffset(virtualChildAt);
                i8 = paddingTop;
                LayoutParams layoutParams22 = layoutParams;
                setChildFrame(view4, locationOffset2, i15, measuredWidth, measuredHeight);
                View view32 = view22;
                i20 = i22 + getChildrenSkipCount(view32, i242);
                i5 = i232 + measuredWidth + layoutParams22.rightMargin + getNextLocationOffset(view32);
                i20++;
                virtualChildCount = i10;
                i19 = i9;
                z4 = z;
                paddingTop = i8;
            } else {
                int i25 = i20;
                i8 = paddingTop;
                i11 = virtualChildCount;
                i9 = i19;
                z2 = true;
            }
            i20++;
            virtualChildCount = i10;
            i19 = i9;
            z4 = z;
            paddingTop = i8;
        }
    }

    private void setChildFrame(View view, int i, int i2, int i3, int i4) {
        view.layout(i, i2, i3 + i, i4 + i2);
    }

    public void setOrientation(int i) {
        if (this.mOrientation != i) {
            this.mOrientation = i;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    public void setGravity(int i) {
        if (this.mGravity != i) {
            if ((8388615 & i) == 0) {
                i |= GravityCompat.START;
            }
            if ((i & 112) == 0) {
                i |= 48;
            }
            this.mGravity = i;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalGravity(int i) {
        int i2 = i & GravityCompat.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i3 = this.mGravity;
        if ((8388615 & i3) != i2) {
            this.mGravity = i2 | (-8388616 & i3);
            requestLayout();
        }
    }

    public void setVerticalGravity(int i) {
        int i2 = i & 112;
        int i3 = this.mGravity;
        if ((i3 & 112) != i2) {
            this.mGravity = i2 | (i3 & -113);
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return new LayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(ACCESSIBILITY_CLASS_NAME);
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(ACCESSIBILITY_CLASS_NAME);
    }
}

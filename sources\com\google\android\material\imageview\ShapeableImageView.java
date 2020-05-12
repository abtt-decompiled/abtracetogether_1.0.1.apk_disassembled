package com.google.android.material.imageview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;
import com.google.android.material.R;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.ShapeAppearancePathProvider;
import com.google.android.material.shape.Shapeable;

public class ShapeableImageView extends AppCompatImageView implements Shapeable {
    private static final int DEF_STYLE_RES = R.style.Widget_MaterialComponents_ShapeableImageView;
    private static final String TAG = "ShapeableImageView";
    private Bitmap bitmap;
    private final Paint bitmapPaint;
    private BitmapShader bitmapShader;
    private final Paint borderPaint;
    /* access modifiers changed from: private */
    public final RectF destination;
    private final Matrix matrix;
    private final Path path;
    private final ShapeAppearancePathProvider pathProvider;
    /* access modifiers changed from: private */
    public ShapeAppearanceModel shapeAppearanceModel;
    private final RectF source;
    private ColorStateList strokeColor;
    private int strokeWidth;

    class OutlineProvider extends ViewOutlineProvider {
        private Rect rect = new Rect();

        OutlineProvider() {
        }

        public void getOutline(View view, Outline outline) {
            if (ShapeableImageView.this.shapeAppearanceModel != null && ShapeableImageView.this.shapeAppearanceModel.isRoundRect(ShapeableImageView.this.destination)) {
                ShapeableImageView.this.destination.round(this.rect);
                outline.setRoundRect(this.rect, ShapeableImageView.this.shapeAppearanceModel.getBottomLeftCornerSize().getCornerSize(ShapeableImageView.this.destination));
            }
        }
    }

    public ShapeableImageView(Context context) {
        this(context, null, 0);
    }

    public ShapeableImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ShapeableImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.pathProvider = new ShapeAppearancePathProvider();
        this.path = new Path();
        this.matrix = new Matrix();
        this.source = new RectF();
        this.destination = new RectF();
        Paint paint = new Paint();
        this.bitmapPaint = paint;
        paint.setAntiAlias(true);
        this.bitmapPaint.setFilterBitmap(true);
        this.bitmapPaint.setDither(true);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ShapeableImageView, i, DEF_STYLE_RES);
        this.strokeColor = MaterialResources.getColorStateList(context, obtainStyledAttributes, R.styleable.ShapeableImageView_strokeColor);
        this.strokeWidth = obtainStyledAttributes.getDimensionPixelSize(R.styleable.ShapeableImageView_strokeWidth, 0);
        Paint paint2 = new Paint();
        this.borderPaint = paint2;
        paint2.setStyle(Style.STROKE);
        this.borderPaint.setAntiAlias(true);
        this.shapeAppearanceModel = ShapeAppearanceModel.builder(context, attributeSet, i, DEF_STYLE_RES).build();
        if (VERSION.SDK_INT >= 21) {
            setOutlineProvider(new OutlineProvider());
        }
        updateShader();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        Bitmap bitmap2 = this.bitmap;
        if (bitmap2 != null) {
            this.source.set(0.0f, 0.0f, (float) bitmap2.getWidth(), (float) this.bitmap.getHeight());
            drawBitmap(canvas, this.source, this.destination);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.destination.set((float) getPaddingLeft(), (float) getPaddingTop(), (float) (getMeasuredWidth() - getPaddingRight()), (float) (getMeasuredHeight() - getPaddingBottom()));
        this.pathProvider.calculatePath(this.shapeAppearanceModel, 1.0f, this.destination, this.path);
    }

    private void drawBitmap(Canvas canvas, RectF rectF, RectF rectF2) {
        this.matrix.reset();
        this.matrix.setRectToRect(rectF, rectF2, ScaleToFit.FILL);
        this.bitmapShader.setLocalMatrix(this.matrix);
        this.bitmapPaint.setShader(this.bitmapShader);
        canvas.drawPath(this.path, this.bitmapPaint);
        this.borderPaint.setStrokeWidth((float) this.strokeWidth);
        int colorForState = this.strokeColor.getColorForState(getDrawableState(), this.strokeColor.getDefaultColor());
        if (this.strokeWidth > 0 && colorForState != 0) {
            this.borderPaint.setColor(colorForState);
            canvas.drawPath(this.path, this.borderPaint);
        }
    }

    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel2) {
        this.shapeAppearanceModel = shapeAppearanceModel2;
        requestLayout();
    }

    public ShapeAppearanceModel getShapeAppearanceModel() {
        return this.shapeAppearanceModel;
    }

    public void setStrokeColorResource(int i) {
        setStrokeColor(AppCompatResources.getColorStateList(getContext(), i));
    }

    public ColorStateList getStrokeColor() {
        return this.strokeColor;
    }

    public void setStrokeWidth(int i) {
        if (this.strokeWidth != i) {
            this.strokeWidth = i;
            invalidate();
        }
    }

    public void setStrokeWidthResource(int i) {
        setStrokeWidth(getResources().getDimensionPixelSize(i));
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public void setStrokeColor(ColorStateList colorStateList) {
        this.strokeColor = colorStateList;
        invalidate();
    }

    public void setImageBitmap(Bitmap bitmap2) {
        super.setImageBitmap(bitmap2);
        updateShader();
    }

    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateShader();
    }

    public void setImageResource(int i) {
        super.setImageResource(i);
        updateShader();
    }

    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        updateShader();
    }

    private void updateShader() {
        Bitmap createBitmap = createBitmap();
        this.bitmap = createBitmap;
        if (createBitmap != null) {
            this.bitmapShader = new BitmapShader(this.bitmap, TileMode.CLAMP, TileMode.CLAMP);
        }
    }

    private Bitmap createBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return null;
        }
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return createBitmap;
    }
}

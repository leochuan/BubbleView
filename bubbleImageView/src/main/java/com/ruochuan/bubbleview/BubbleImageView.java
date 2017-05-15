package com.ruochuan.bubbleview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import static com.ruochuan.bubbleview.BubbleDrawable.LEFT;


/**
 * Created by Dajavu on 04/05/2017.
 */

public class BubbleImageView extends ImageView {
    private int triangleHeight;
    private int triangleWidth;
    private int offset = 0;
    private int radius;
    private int orientation;
    private float borderWidth;
    private int borderColor;
    private boolean centerArrow;
    private BubbleDrawable bubbleDrawable;
    private Bitmap bitmap;
    private boolean preSetUp = true;
    private float[] matrixValues = new float[9];

    public BubbleImageView(Context context) {
        this(context, null);
    }

    public BubbleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        orientation = LEFT;
        radius = DensityUtils.dip2px(context, 5);
        triangleWidth = DensityUtils.dip2px(context, 12);
        triangleHeight = DensityUtils.dip2px(context, 8);
        offset = DensityUtils.dip2px(context, 8);
        borderColor = Color.BLACK;

        if (attrs == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.bubbleView);
        offset = (int) typedArray.getDimension(R.styleable.bubbleView_offset, offset);
        radius = (int) typedArray.getDimension(R.styleable.bubbleView_radius, radius);
        orientation = typedArray.getInt(R.styleable.bubbleView_orientation, orientation);
        triangleWidth = (int) typedArray.getDimension(R.styleable.bubbleView_triangleWidth, triangleWidth);
        triangleHeight = (int) typedArray.getDimension(R.styleable.bubbleView_triangleHeight, triangleHeight);
        borderColor = typedArray.getColor(R.styleable.bubbleView_borderColor, borderColor);
        borderWidth = typedArray.getDimension(R.styleable.bubbleView_borderWidth, 0);
        if (borderWidth != 0) {
            borderWidth = borderWidth / getResources().getDisplayMetrics().density;
        }
        typedArray.recycle();

        preSetUp = false;
        setImageDrawable(bubbleDrawable);
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;

            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if (bm == null || bitmap == bm) return;
        bitmap = bm;
        setUp();
        super.setImageDrawable(bubbleDrawable);
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        if (preSetUp || drawable == null) return;
        bitmap = getBitmapFromDrawable(drawable);
        setUp();
        super.setImageDrawable(bubbleDrawable);
    }

    @Override
    public void setImageResource(int resId) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getContext().getDrawable(resId);
        } else {
            drawable = getContext().getResources().getDrawable(resId);
        }
        setImageDrawable(drawable);
        super.setImageResource(resId);
    }

    public void setTriangleWidth(int triangleWidth) {
        this.triangleWidth = triangleWidth;
        if (bubbleDrawable != null)
            bubbleDrawable.setTriangleWidth(triangleWidth);
        invalidate();
    }

    public void setTriangleHeight(int triangleHeight) {
        this.triangleHeight = triangleHeight;
        if (bubbleDrawable != null)
            bubbleDrawable.setTriangleHeight(triangleHeight);
        invalidate();
    }

    public void setOffset(int offset) {
        this.offset = offset;
        if (bubbleDrawable != null) bubbleDrawable.setOffset(offset);
        invalidate();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        if (bubbleDrawable != null) bubbleDrawable.setOrientation(orientation);
        invalidate();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        if (bubbleDrawable != null) bubbleDrawable.setRadius(radius);
        invalidate();
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
        if (bubbleDrawable != null) bubbleDrawable.setBorderWidth(borderWidth);
        invalidate();
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        if (bubbleDrawable != null) bubbleDrawable.setBorderColor(borderColor);
        invalidate();
    }

    public void setCenterArrow(boolean centerArrow) {
        this.centerArrow = centerArrow;
        if (bubbleDrawable != null) bubbleDrawable.setCenterArrow(centerArrow);
        invalidate();
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public int getTriangleHeight() {
        return triangleHeight;
    }

    public int getTriangleWidth() {
        return triangleWidth;
    }

    public int getOffset() {
        return offset;
    }

    public int getRadius() {
        return radius;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isCenterArrow() {
        return centerArrow;
    }

    private void setUp() {
        if (bitmap == null) bitmap = getBitmapFromDrawable(getDrawable());
        if (bitmap == null) return;
        bubbleDrawable = new BubbleDrawable.Builder()
                .setBitmap(bitmap)
                .setOffset(offset)
                .setOrientation(orientation)
                .setRadius(radius)
                .setBorderColor(borderColor)
                .setBorderWidth(borderWidth)
                .setTriangleWidth(triangleWidth)
                .setTriangleHeight(triangleHeight)
                .build();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (bubbleDrawable == null) {
            return; // couldn't resolve the URI
        }

        if (bubbleDrawable.getIntrinsicHeight() == 0 || bubbleDrawable.getIntrinsicWidth() == 0) {
            return;     // nothing to draw (empty bounds)
        }

        final Matrix mDrawMatrix = getImageMatrix();
        final int mPaddingLeft = getPaddingLeft();
        final int mPaddingTop = getPaddingTop();

        if (mDrawMatrix == null && getPaddingTop() == 0 && getPaddingBottom() == 0) {
            bubbleDrawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            //crop to padding api above 16
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (getCropToPadding()) {
                    final int scrollX = getScrollX();
                    final int scrollY = getScrollY();
                    canvas.clipRect(scrollX + mPaddingLeft, scrollY + mPaddingTop,
                            scrollX + getRight() - getLeft() - getRight(),
                            scrollY + getBottom() - getTop() - getBottom());
                }
            }

            canvas.translate(mPaddingLeft, mPaddingTop);

            if (mDrawMatrix != null) {
                canvas.concat(mDrawMatrix);

                mDrawMatrix.getValues(matrixValues);
                final float scaleX = matrixValues[Matrix.MSCALE_X];
                final float scaleY = matrixValues[Matrix.MSCALE_Y];
                final float translateX = matrixValues[Matrix.MTRANS_X];
                final float translateY = matrixValues[Matrix.MTRANS_Y];

                final ScaleType scaleType = getScaleType();

                if (scaleType == ScaleType.CENTER) {
                    bubbleDrawable.setOffsetLeft(-translateX);
                    bubbleDrawable.setOffsetTop(-translateY);
                    bubbleDrawable.setOffsetBottom(-translateY);
                    bubbleDrawable.setOffsetRight(-translateX);
                } else if (scaleType == ScaleType.CENTER_CROP) {
                    float scale = scaleX > scaleY ? 1 / scaleY : 1 / scaleX;
                    bubbleDrawable.setOffsetLeft(-translateX * scale);
                    bubbleDrawable.setOffsetTop(-translateY * scale);
                    bubbleDrawable.setOffsetBottom(-translateY * scale);
                    bubbleDrawable.setOffsetRight(-translateX * scale);
                    bubbleDrawable.setScale(scale);
                } else {
                    bubbleDrawable.setScale(scaleX > scaleY ? 1 / scaleY : 1 / scaleX);
                }
            }

            bubbleDrawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }
}

package com.ruochuan.bubbleview;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by Dajavu on 05/05/2017.
 */

public final class BubbleDrawable extends Drawable {
    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int TOP = 2;
    public final static int BOTTOM = 3;

    private int triangleHeight;
    private int triangleWidth;
    private int offset = 0;
    private final Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int radius;
    private int orientation;
    private float borderWidth;

    private Bitmap bitmap;
    private RectF leftTopRadiusRect, rightTopRadiusRect, leftBottomRadiusRect, rightBottomRadiusRect;
    private Rect mDstRect = new Rect();
    private Path path = new Path();
    private final Matrix mShaderMatrix = new Matrix();
    private BitmapShader bitmapShader;
    private boolean dirtyDraw = true;
    private float scale = 1f;
    private float offsetLeft = 0f;
    private float offsetTop = 0f;
    private float offsetRight = 0f;
    private float offsetBottom = 0f;

    public BubbleDrawable(Builder builder) {
        triangleWidth = builder.triangleWidth;
        triangleHeight = builder.triangleHeight;
        offset = builder.offset;
        radius = builder.radius;
        orientation = builder.orientation;
        bitmap = builder.bitmap;
        borderWidth = builder.borderWidth;

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);
        borderPaint.setColor(builder.borderColor);

        leftTopRadiusRect = new RectF();
        rightBottomRadiusRect = new RectF();
        leftBottomRadiusRect = new RectF();
        rightTopRadiusRect = new RectF();

        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        bitmapPaint.setShader(bitmapShader);
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        dirtyDraw = true;
        updateShaderMatrix(bounds);
    }

    private void updateShaderMatrix(Rect bounds) {
        mShaderMatrix.set(null);

        final int mBitmapWidth = bitmap.getWidth();
        final int mBitmapHeight = bitmap.getHeight();

        float scaleX = (bounds.width() * 1f) / mBitmapWidth;
        float scaleY = (bounds.height() * 1f) / mBitmapHeight;

        mShaderMatrix.setScale(scaleX, scaleY);

        bitmapShader.setLocalMatrix(mShaderMatrix);
    }

    private void configureRadiusRect() {
        leftTopRadiusRect.set(getBorderLeft(), getBorderTop(), getBorderLeft() + radius * scale * 2, getBorderTop() + radius * scale * 2);
        rightTopRadiusRect.set(getBorderRight() - radius * scale * 2, getBorderTop(), getBorderRight(), getBorderTop() + radius * scale * 2);
        leftBottomRadiusRect.set(getBorderLeft(), getBorderBottom() - radius * scale * 2, getBorderLeft() + radius * scale * 2, getBorderBottom());
        rightBottomRadiusRect.set(getBorderRight() - radius * scale * 2, getBorderBottom() - radius * scale * 2, getBorderRight(), getBorderBottom());
        switch (orientation) {
            case LEFT:
                leftTopRadiusRect.offset(triangleHeight * scale, 0);
                leftBottomRadiusRect.offset(triangleHeight * scale, 0);
                break;
            case TOP:
                leftTopRadiusRect.offset(0, triangleHeight * scale);
                rightTopRadiusRect.offset(0, triangleHeight * scale);
                break;
            case RIGHT:
                rightTopRadiusRect.offset(-triangleHeight * scale, 0);
                rightBottomRadiusRect.offset(-triangleHeight * scale, 0);
                break;
            case BOTTOM:
                leftBottomRadiusRect.offset(0, -triangleHeight * scale);
                rightBottomRadiusRect.offset(0, -triangleHeight * scale);
                break;
        }
    }

    @Override
    public int getIntrinsicHeight() {
        if (bitmap != null) return bitmap.getHeight();
        return super.getIntrinsicHeight();
    }

    @Override
    public int getIntrinsicWidth() {
        if (bitmap != null) return bitmap.getWidth();
        return super.getIntrinsicWidth();
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap == null) {
            return;
        }

        updateDstRect();
        setUpPath();
        canvas.drawPath(path, bitmapPaint);
        if (borderWidth > 0) {
            borderPaint.setStrokeWidth(borderWidth * scale);
            canvas.drawPath(path, borderPaint);
        }
    }

    public int getOrientation() {
        return orientation;
    }

    private void updateDstRect() {
        if (dirtyDraw) {
            final Rect bounds = getBounds();
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                final int layoutDirection = getLayoutDirection();
                Gravity.apply(Gravity.FILL, bitmap.getWidth(), bitmap.getHeight(),
                        bounds, mDstRect, layoutDirection);
            } else {
                Gravity.apply(Gravity.FILL, bitmap.getWidth(), bitmap.getHeight(),
                        bounds, mDstRect);
            }

        }
        configureRadiusRect();
        dirtyDraw = false;
    }

    /* Package */ void setScale(float scale) {
        this.scale = scale;
    }

    /* Package */ void setOffsetLeft(float offsetLeft) {
        this.offsetLeft = offsetLeft;
    }

    /* Package */ void setOffsetTop(float offsetTop) {
        this.offsetTop = offsetTop;
    }

    /* Package */ void setOffsetRight(float offsetRight) {
        this.offsetRight = offsetRight;
    }

    /* Package */ void setOffsetBottom(float offsetBottom) {
        this.offsetBottom = offsetBottom;
    }

    /* Package */ void setTriangleHeight(int triangleHeight) {
        this.triangleHeight = triangleHeight;
    }

    /* Package */ void setTriangleWidth(int triangleWidth) {
        this.triangleWidth = triangleWidth;
    }

    /* Package */  void setOffset(int offset) {
        this.offset = offset;
    }

    /* Package */  void setRadius(int radius) {
        this.radius = radius;
    }

    /* Package */  void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    /* Package */ void setBorderColor(int color) {
        borderPaint.setColor(color);
    }

    /* Package */ void setBorderWidth(float width) {
        this.borderWidth = width;
    }

    public int getBorderColor() {
        return borderPaint.getColor();
    }

    public float getBorderWidth() {
        return borderWidth;
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

    @Override
    public void setAlpha(int alpha) {
        bitmapPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        bitmapPaint.setColorFilter(colorFilter);
    }

    @Override
    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    private void setUpPath() {
        path.reset();

        switch (orientation) {
            case LEFT:
                generatorLefPath();
                break;
            case RIGHT:
                generatorRightPath();
                break;
            case TOP:
                generatorTopPath();
                break;
            case BOTTOM:
                generatorBottomPath();
                break;
        }
    }

    private void generatorLefPath() {
        path.moveTo(getBorderLeft(), getBorderTop() + triangleWidth * scale / 2 + radius * scale + offset * scale);
        path.lineTo(getBorderLeft() + triangleHeight * scale, getBorderTop() + radius * scale + offset * scale);
        path.lineTo(getBorderLeft() + triangleHeight * scale, getBorderTop() + radius * scale);
        path.arcTo(leftTopRadiusRect, 180, 90, false);
        path.lineTo(getBorderRight() - radius, getBorderTop());
        path.arcTo(rightTopRadiusRect, 270, 90, false);
        path.lineTo(getBorderRight(), getBorderBottom() - radius * scale);
        path.arcTo(rightBottomRadiusRect, 0, 90, false);
        path.lineTo(getBorderLeft() + triangleHeight * scale + radius * scale, getBorderBottom());
        path.arcTo(leftBottomRadiusRect, 90, 90, false);
        path.lineTo(getBorderLeft() + triangleHeight * scale, getBorderTop() + radius * scale + offset * scale + triangleWidth * scale);
        path.close();
    }

    private void generatorRightPath() {
        path.moveTo(getBorderRight(), getBorderTop() + triangleWidth * scale / 2 + radius * scale + offset * scale);
        path.lineTo(getBorderRight() - triangleHeight * scale, getBorderTop() + radius * scale + offset * scale);
        path.lineTo(getBorderRight() - triangleHeight * scale, getBorderTop() + radius * scale);
        path.arcTo(rightTopRadiusRect, 0, -90, false);
        path.lineTo(getBorderLeft() + radius * scale, getBorderTop());
        path.arcTo(leftTopRadiusRect, 270, -90, false);
        path.lineTo(getBorderLeft(), getBorderBottom() - radius * scale);
        path.arcTo(leftBottomRadiusRect, 180, -90, false);
        path.lineTo(getBorderRight() - triangleHeight * scale - radius * scale, getBorderBottom());
        path.arcTo(rightBottomRadiusRect, 90, -90, false);
        path.lineTo(getBorderRight() - triangleHeight * scale, getBorderTop() + radius * scale + offset * scale + triangleWidth * scale);
        path.close();
    }

    private void generatorTopPath() {
        path.moveTo(getBorderLeft() + triangleWidth * scale / 2 + radius * scale + offset * scale, getBorderTop());
        path.lineTo(getBorderLeft() + triangleWidth * scale + radius * scale + offset * scale, getBorderTop() + triangleHeight * scale);
        path.lineTo(getBorderRight() - radius * scale, getBorderTop() + triangleHeight * scale);
        path.arcTo(rightTopRadiusRect, 270, 90, false);
        path.lineTo(getBorderRight(), getBorderBottom() - radius * scale);
        path.arcTo(rightBottomRadiusRect, 0, 90, false);
        path.lineTo(getBorderLeft() + radius * scale, getBorderBottom());
        path.arcTo(leftBottomRadiusRect, 90, 90, false);
        path.lineTo(getBorderLeft(), getBorderTop() + radius * scale + triangleHeight * scale);
        path.arcTo(leftTopRadiusRect, 180, 90, false);
        path.lineTo(getBorderLeft() + radius * scale + offset * scale, getBorderTop() + triangleHeight * scale);
        path.close();
    }

    private void generatorBottomPath() {
        path.moveTo(getBorderLeft() + triangleWidth * scale / 2 + radius * scale + offset * scale, getBorderBottom());
        path.lineTo(getBorderLeft() + triangleWidth * scale + radius * scale + offset * scale, getBorderBottom() - triangleHeight * scale);
        path.lineTo(getBorderRight() - radius * scale, getBorderBottom() - triangleHeight * scale);
        path.arcTo(rightBottomRadiusRect, 90, -90, false);
        path.lineTo(getBorderRight(), getBorderTop() + radius * scale);
        path.arcTo(rightTopRadiusRect, 0, -90, false);
        path.lineTo(getBorderLeft() + radius * scale, getBorderTop());
        path.arcTo(leftTopRadiusRect, 270, -90, false);
        path.lineTo(getBorderLeft(), getBorderBottom() - radius * scale - triangleHeight * scale);
        path.arcTo(leftBottomRadiusRect, 180, -90, false);
        path.lineTo(getBorderLeft() + radius * scale + offset * scale, getBorderBottom() - triangleHeight * scale);
        path.close();
    }

    private float getBorderLeft() {
        return mDstRect.left + offsetLeft + borderWidth * scale / 2f;
    }

    private float getBorderRight() {
        return mDstRect.right - offsetRight - borderWidth * scale / 2f;
    }

    private float getBorderTop() {
        return mDstRect.top + offsetTop + borderWidth * scale / 2f;
    }

    private float getBorderBottom() {
        return mDstRect.bottom - offsetBottom - borderWidth * scale / 2f;
    }

    public static class Builder {
        private int triangleHeight;
        private int triangleWidth;
        private int offset;
        private int radius;
        private int orientation;
        private int borderColor;
        private float borderWidth;
        private Bitmap bitmap;

        public Builder setTriangleHeight(int triangleHeight) {
            this.triangleHeight = triangleHeight;
            return this;
        }

        public Builder setTriangleWidth(int triangleWidth) {
            this.triangleWidth = triangleWidth;
            return this;
        }

        public Builder setOffset(int offset) {
            this.offset = offset;
            return this;
        }

        public Builder setRadius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder setOrientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
            return this;
        }

        public Builder setBorderColor(int borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setBorderWidth(float borderWidth) {
            this.borderWidth = borderWidth;
            return this;
        }

        public BubbleDrawable build() {
            return new BubbleDrawable(this);
        }
    }
}

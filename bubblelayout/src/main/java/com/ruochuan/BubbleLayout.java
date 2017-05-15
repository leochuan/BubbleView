package com.ruochuan;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.souche.apps.destiny.bubblelayout.R;

/**
 * Created by Dajavu on 28/02/2017.
 */

public class BubbleLayout extends ViewGroup {

    public final static int LEFT = 0;
    public final static int RIGHT = 1;
    public final static int TOP = 2;
    public final static int BOTTOM = 3;

    private int triangleHeight;
    private int triangleWidth;
    private int offset;
    private Paint backgroundPaint, borderPaint;
    private Path path;
    private int radius;
    private int borderWidth;
    private float borderPaintSize;
    private int orientation;
    private RectF leftTopRadiusRect, rightTopRadiusRect, leftBottomRadiusRect, rightBottomRadiusRect;
    private int backgroundColor;
    private int borderColor;
    private boolean clipToRadius;
    private boolean centerArrow = true;

    public BubbleLayout(Context context) {
        this(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        borderColor = Color.BLACK;
        backgroundColor = Color.WHITE;
        orientation = LEFT;
        borderWidth = DensityUtils.dip2px(context, 2);
        radius = DensityUtils.dip2px(context, 5);
        triangleWidth = DensityUtils.dip2px(context, 12);
        triangleHeight = DensityUtils.dip2px(context, 8);
        offset = DensityUtils.dip2px(context, 8);

        if (attrs == null) return;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.bubbleView);
        backgroundColor = typedArray.getColor(R.styleable.bubbleView_bgColor, backgroundColor);
        borderColor = typedArray.getColor(R.styleable.bubbleView_borderColor, borderColor);
        offset = (int) typedArray.getDimension(R.styleable.bubbleView_offset, offset);
        borderWidth = (int) typedArray.getDimension(R.styleable.bubbleView_borderWidth, borderWidth);
        radius = (int) typedArray.getDimension(R.styleable.bubbleView_radius, radius);
        orientation = typedArray.getInt(R.styleable.bubbleView_orientation, orientation);
        triangleWidth = (int) typedArray.getDimension(R.styleable.bubbleView_triangleWidth, triangleWidth);
        triangleHeight = (int) typedArray.getDimension(R.styleable.bubbleView_triangleHeight, triangleHeight);
        clipToRadius = typedArray.getBoolean(R.styleable.bubbleView_clipToRadius, clipToRadius);
        centerArrow = typedArray.getBoolean(R.styleable.bubbleView_centerArrow, centerArrow);
        typedArray.recycle();
    }

    private void init() {
        setWillNotDraw(false);

        backgroundPaint = new Paint();
        path = new Path();
        backgroundPaint.setAntiAlias(true);

        borderPaint = new Paint();
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeCap(Paint.Cap.ROUND);

        borderPaintSize = borderWidth / getResources().getDisplayMetrics().density;
        borderPaint.setStrokeWidth(borderPaintSize);

        backgroundPaint.setColor(backgroundColor);
        borderPaint.setColor(borderColor);

        leftTopRadiusRect = new RectF();
        rightBottomRadiusRect = new RectF();
        leftBottomRadiusRect = new RectF();
        rightTopRadiusRect = new RectF();
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        int reservedWidth = Math.round(borderPaintSize * 2);
        int reservedHeight = Math.round(borderPaintSize * 2);
        if (!clipToRadius) {
            reservedHeight += radius * 2;
            reservedWidth += radius * 2;
        }

        switch (orientation) {
            case LEFT:
            case RIGHT:
                reservedWidth += triangleHeight;
                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() != GONE) {
                        measureChildWithMargins(child, widthMeasureSpec, reservedWidth, heightMeasureSpec, reservedHeight);
                        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                        maxWidth = Math.max(maxWidth,
                                child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                        maxHeight = Math.max(maxHeight,
                                child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                        childState = combineMeasuredStates(childState, child.getMeasuredState());
                    }
                }
                maxWidth += getPaddingLeft() + getPaddingRight();
                maxHeight += getPaddingTop() + getPaddingBottom();

                maxWidth += Math.round(triangleHeight + borderPaintSize * 2);
                maxHeight = maxHeight > triangleWidth + offset ? maxHeight : triangleWidth + offset;
                maxHeight += Math.round(borderPaintSize * 2);
                if (!clipToRadius) {
                    maxHeight += radius * 2;
                    maxWidth += radius * 2;
                } else {
                    maxHeight = maxHeight > triangleWidth + offset + 2 * (radius + borderPaintSize) ?
                            maxHeight : Math.round(triangleWidth + offset + 2 * (radius + borderPaintSize));
                    maxWidth = maxWidth > triangleWidth + (borderPaintSize + radius) * 2 ?
                            maxWidth : Math.round(triangleWidth + (borderPaintSize + radius) * 2);
                }
                setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                        resolveSizeAndState(maxHeight, heightMeasureSpec,
                                childState << MEASURED_HEIGHT_STATE_SHIFT));
                break;
            case TOP:
            case BOTTOM:
                reservedHeight += triangleHeight;
                for (int i = 0; i < count; i++) {
                    final View child = getChildAt(i);
                    if (child.getVisibility() != GONE) {
                        measureChildWithMargins(child, widthMeasureSpec, reservedWidth, heightMeasureSpec, reservedHeight);
                        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                        maxWidth = Math.max(maxWidth,
                                child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                        maxHeight = Math.max(maxHeight,
                                child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                        childState = combineMeasuredStates(childState, child.getMeasuredState());
                    }
                }
                maxWidth += getPaddingLeft() + getPaddingRight();
                maxHeight += getPaddingTop() + getPaddingBottom();

                maxHeight += Math.round(triangleHeight + borderPaintSize * 2);
                maxWidth = maxWidth > triangleWidth + offset ? maxWidth : triangleWidth + offset;
                maxWidth += Math.round(borderPaintSize * 2);
                if (!clipToRadius) {
                    maxHeight += radius * 2;
                    maxWidth += radius * 2;
                } else {
                    maxWidth = maxWidth > triangleWidth + offset + 2 * (radius + borderPaintSize) ?
                            maxWidth : Math.round(triangleWidth + offset + 2 * (radius + borderPaintSize));
                    maxHeight = maxHeight > triangleWidth + (borderPaintSize + radius) * 2 ?
                            maxHeight : Math.round(triangleWidth + (borderPaintSize + radius) * 2);
                }
                setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                        resolveSizeAndState(maxHeight, heightMeasureSpec,
                                childState << MEASURED_HEIGHT_STATE_SHIFT));
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();

        final int parentLeft = getPaddingLeft();
        final int parentTop = getPaddingTop();

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childLeft = parentLeft;
                int childTop = parentTop;
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                switch (orientation) {
                    case LEFT:
                        childLeft += triangleHeight;
                        break;
                    case TOP:
                        childTop += triangleHeight;
                        break;
                    default:
                        break;
                }
                if (!clipToRadius) {
                    childLeft += radius;
                    childTop += radius;
                }
                childLeft += Math.round(borderPaintSize);
                childTop += Math.round(borderPaintSize);

                final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                childLeft += lp.leftMargin;
                childTop += lp.topMargin;

                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    public void setCenterArrow(boolean centerArrow) {
        this.centerArrow = centerArrow;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        borderPaint.setColor(borderColor);
        invalidate();
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        backgroundPaint.setColor(backgroundColor);
        invalidate();
    }

    @Override
    public void setBackground(Drawable background) {
        Log.e("BubbleView", "sorry not support this method for now and for-ever :)");
    }

    public void setOffset(int offset) {
        this.offset = offset;
        update();
    }

    public void setTriangleWidth(int length) {
        this.triangleWidth = length;
        update();
    }

    public void setTriangleHeight(int height) {
        triangleHeight = height;
        update();
    }

    public void setRadius(int radius) {
        this.radius = radius;
        update();
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
        update();
    }

    public void setBorderWidth(int borderWidth) {
        this.borderWidth = borderWidth;
        borderPaintSize = borderWidth / getResources().getDisplayMetrics().density;
        borderPaint.setStrokeWidth(borderPaintSize);
        update();
    }

    public int getTriangleWidth() {
        return triangleWidth;
    }

    public int getTriangleHeight() {
        return triangleHeight;
    }

    public int getOffset() {
        return offset;
    }

    public int getRadius() {
        return radius;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public int getOrientation() {
        return orientation;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public boolean isCenterArrow() {
        return centerArrow;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public boolean isClipToRadius() {
        return clipToRadius;
    }

    private void configureRadiusRect() {
        leftTopRadiusRect.set(getBorderLeft(), getBorderTop(), getBorderLeft() + radius * 2, getBorderTop() + radius * 2);
        rightTopRadiusRect.set(getBorderRight() - radius * 2, getBorderTop(), getBorderRight(), getBorderTop() + radius * 2);
        leftBottomRadiusRect.set(getBorderLeft(), getBorderBottom() - radius * 2, getBorderLeft() + radius * 2, getBorderBottom());
        rightBottomRadiusRect.set(getBorderRight() - radius * 2, getBorderBottom() - radius * 2, getBorderRight(), getBorderBottom());
        switch (orientation) {
            case LEFT:
                leftTopRadiusRect.offset(triangleHeight, 0);
                leftBottomRadiusRect.offset(triangleHeight, 0);
                break;
            case TOP:
                leftTopRadiusRect.offset(0, triangleHeight);
                rightTopRadiusRect.offset(0, triangleHeight);
                break;
            case RIGHT:
                rightTopRadiusRect.offset(-triangleHeight, 0);
                rightBottomRadiusRect.offset(-triangleHeight, 0);
                break;
            case BOTTOM:
                leftBottomRadiusRect.offset(0, -triangleHeight);
                rightBottomRadiusRect.offset(0, -triangleHeight);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w > 0 || h > 0) configureRadiusRect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setUpPath();
        canvas.drawPath(path, backgroundPaint);
        if (borderWidth > 0) canvas.drawPath(path, borderPaint);
    }

    private void setUpPath() {
        path.reset();

        switch (orientation) {
            case LEFT:
                centerYOffset();
                generatorLefPath();
                break;
            case RIGHT:
                centerYOffset();
                generatorRightPath();
                break;
            case TOP:
                centerXOffset();
                generatorTopPath();
                break;
            case BOTTOM:
                centerXOffset();
                generatorBottomPath();
                break;
        }
    }

    private void centerXOffset() {
        if (centerArrow) {
            offset = Math.round(getWidth() / 2f - radius - triangleHeight / 2f);
        }
    }

    private void centerYOffset() {
        if (centerArrow)
            offset = Math.round(getHeight() / 2f - radius - triangleWidth / 2f);
    }

    private void generatorLefPath() {
        path.moveTo(getBorderLeft(), getBorderTop() + triangleWidth / 2 + radius + offset);
        path.lineTo(getBorderLeft() + triangleHeight, getBorderTop() + radius + offset);
        path.lineTo(getBorderLeft() + triangleHeight, getBorderTop() + radius);
        path.arcTo(leftTopRadiusRect, 180, 90, false);
        path.lineTo(getBorderRight() - radius, getBorderTop());
        path.arcTo(rightTopRadiusRect, 270, 90, false);
        path.lineTo(getBorderRight(), getBorderBottom() - radius);
        path.arcTo(rightBottomRadiusRect, 0, 90, false);
        path.lineTo(getBorderLeft() + triangleHeight + radius, getBorderBottom());
        path.arcTo(leftBottomRadiusRect, 90, 90, false);
        path.lineTo(getBorderLeft() + triangleHeight, getBorderTop() + radius + offset + triangleWidth);
        path.close();
    }

    private void generatorRightPath() {
        path.moveTo(getBorderRight(), getBorderTop() + triangleWidth / 2 + radius + offset);
        path.lineTo(getBorderRight() - triangleHeight, getBorderTop() + radius + offset);
        path.lineTo(getBorderRight() - triangleHeight, getBorderTop() + radius);
        path.arcTo(rightTopRadiusRect, 0, -90, false);
        path.lineTo(getBorderLeft() + radius, getBorderTop());
        path.arcTo(leftTopRadiusRect, 270, -90, false);
        path.lineTo(getBorderLeft(), getBorderBottom() - radius);
        path.arcTo(leftBottomRadiusRect, 180, -90, false);
        path.lineTo(getBorderRight() - triangleHeight - radius, getBorderBottom());
        path.arcTo(rightBottomRadiusRect, 90, -90, false);
        path.lineTo(getBorderRight() - triangleHeight, getBorderTop() + radius + offset + triangleWidth);
        path.close();
    }

    private void generatorTopPath() {
        path.moveTo(getBorderLeft() + triangleWidth / 2 + radius + offset, getBorderTop());
        path.lineTo(getBorderLeft() + triangleWidth + radius + offset, getBorderTop() + triangleHeight);
        path.lineTo(getBorderRight() - radius, getBorderTop() + triangleHeight);
        path.arcTo(rightTopRadiusRect, 270, 90, false);
        path.lineTo(getBorderRight(), getBorderBottom() - radius);
        path.arcTo(rightBottomRadiusRect, 0, 90, false);
        path.lineTo(getBorderLeft() + radius, getBorderBottom());
        path.arcTo(leftBottomRadiusRect, 90, 90, false);
        path.lineTo(getBorderLeft(), getBorderTop() + radius + triangleHeight);
        path.arcTo(leftTopRadiusRect, 180, 90, false);
        path.lineTo(getBorderLeft() + radius + offset, getBorderTop() + triangleHeight);
        path.close();
    }

    private void generatorBottomPath() {
        path.moveTo(getBorderLeft() + triangleWidth / 2 + radius + offset, getBorderBottom());
        path.lineTo(getBorderLeft() + triangleWidth + radius + offset, getBorderBottom() - triangleHeight);
        path.lineTo(getBorderRight() - radius, getBorderBottom() - triangleHeight);
        path.arcTo(rightBottomRadiusRect, 90, -90, false);
        path.lineTo(getBorderRight(), getBorderTop() + radius);
        path.arcTo(rightTopRadiusRect, 0, -90, false);
        path.lineTo(getBorderLeft() + radius, getBorderTop());
        path.arcTo(leftTopRadiusRect, 270, -90, false);
        path.lineTo(getBorderLeft(), getBorderBottom() - radius - triangleHeight);
        path.arcTo(leftBottomRadiusRect, 180, -90, false);
        path.lineTo(getBorderLeft() + radius + offset, getBorderBottom() - triangleHeight);
        path.close();
    }

    private float getBorderLeft() {
        return borderPaintSize / 2f;
    }

    private float getBorderRight() {
        return getWidth() - borderPaintSize / 2f;
    }

    private float getBorderTop() {
        return borderPaintSize / 2f;
    }

    private float getBorderBottom() {
        return getHeight() - borderPaintSize / 2f;
    }

    private void update() {
        requestLayout();
        invalidate();
    }
}

package com.helper.widgets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.helper.widgets.R;

public class CustomTextView extends androidx.appcompat.widget.AppCompatTextView {

    /**
     * 自定义view的几个方法的执行顺序:
     * 构造方法->measure->onSizeChanged->onDraw
     */
    int width = 0;
    int height = 0;
    private Paint mPaint;
    private Rect mBounds = new Rect();

    private LinearGradient mLinearGradient; // 渐变渲染器
    private Matrix mGradientMatrix;
    private int mTranslate;
    Paint.FontMetricsInt fontMetricsInt;

    Boolean bRunText = false;
    Boolean bRemoveDefaultPadding = false;
    String fontPath = null;
    Boolean bGradient = false;
    int gradientStartColor;
    int gradientCenterColor;
    int gradientEndColor;
    Typeface typeface;

    public CustomTextView(@NonNull Context context) {
        super(context);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributes(context, attrs);
    }

    public CustomTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(context, attrs);
    }

    // java 代码块 ，先于构造函数执行
    {
        mPaint = getPaint(); // 获取TextView的画笔 获取画笔属性
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        option(canvas);
        drawText(canvas);
//        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        if (bRemoveDefaultPadding) {
            calculateTextParams();
            setMeasuredDimension(-mBounds.left + mBounds.right, -mBounds.top + mBounds.bottom); //设置view的宽高为text的宽高
        }
    }

    private void init() {
        mLinearGradient = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{gradientStartColor, gradientCenterColor, gradientEndColor},
                null, Shader.TileMode.MIRROR);
        mGradientMatrix = new Matrix();
    }

    //初始化属性
    private void initAttributes(Context context, AttributeSet attrs) {
        // 从xml的属性中获取到字体颜色与string
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        bRunText = ta.getBoolean(R.styleable.CustomTextView_runText, false);
        bGradient = ta.getBoolean(R.styleable.CustomTextView_gradient, false);
        bRemoveDefaultPadding = ta.getBoolean(R.styleable.CustomTextView_removeDefaultPadding, false);
        gradientStartColor = ta.getColor(R.styleable.CustomTextView_gradientStartColor, getCurrentTextColor());//默认为当前颜色
        gradientCenterColor = ta.getColor(R.styleable.CustomTextView_gradientCenterColor, getCurrentTextColor());//默认为当前颜色
        gradientEndColor = ta.getColor(R.styleable.CustomTextView_gradientEndColor, getCurrentTextColor());
        fontPath = ta.getString(R.styleable.CustomTextView_textFont);
        if (!TextUtils.isEmpty(fontPath)) {
            typeface = Typeface.createFromAsset(getResources().getAssets(), fontPath); //获取字体
            mPaint.setTypeface(typeface);
        }
        ta.recycle();
    }

    //选择效果 执行对应方法
    private void option(Canvas canvas) {
        if (bGradient) {
            mPaint.setShader(mLinearGradient);
        }
        if (bRunText) {
            runText();
        }
    }

    //文字颜色滚动
    public void runText() {
        if (mGradientMatrix != null) {
            mTranslate += width / 5;
            if (mTranslate > 2 * width) {
                mTranslate = -width;
            }
            mGradientMatrix.setTranslate(mTranslate, 0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            postInvalidateDelayed(100);
        }
    }


    //获取text宽高
    private String calculateTextParams() {
        final String text = getText().toString();
        final int textLength = text.length();
//        mPaint.setTextSize(getTextSize());
        mPaint.getTextBounds(text, 0, textLength, mBounds);
        if (textLength == 0) {
            mBounds.right = mBounds.left;
        }
        return text;
    }

    // 用drawText方法画text
    private void drawText(Canvas canvas) {
        final String text = calculateTextParams();
        final int left = mBounds.left;
        final int bottom = mBounds.bottom;
        mPaint.setAntiAlias(true);
        mPaint.setColor(getCurrentTextColor());
        if (bRemoveDefaultPadding || bRunText || bGradient) { // TODO 先这样之后再统一吧
            mBounds.offset(-mBounds.left, -mBounds.top);
            canvas.drawText(text, -left, mBounds.bottom - bottom, mPaint);
        } else {
            canvas.drawText(text, calcX(text), calcY(), mPaint);
        }
    }

    // TODO 没算 padding, 之后得空再加吧

    private float calcX(String text) {
        float x;
        int gravity = getGravity() & Gravity.HORIZONTAL_GRAVITY_MASK;
        switch (gravity) {
            case Gravity.LEFT:
                x = 0f;
                break;
            case Gravity.RIGHT:
                x = getWidth() - mPaint.measureText(text);
                break;
            case Gravity.CENTER_HORIZONTAL:
            default:
                x = (getWidth() - mPaint.measureText(text)) / 2;
                break;
        }
        return x;
    }

    private float calcY() {
        float y;
        int gravity = getGravity() & Gravity.VERTICAL_GRAVITY_MASK;
        Paint.FontMetrics fontMetrics = mPaint.getFontMetrics();
        switch (gravity) {
            case Gravity.TOP:
                y = (getHeight() - (fontMetrics.bottom - fontMetrics.ascent)) / 2;
                break;
            case Gravity.BOTTOM:
                y = getHeight() / 2 + (fontMetrics.bottom - fontMetrics.ascent);
                break;
            case Gravity.CENTER_VERTICAL:
            default:
                y = (getHeight() - (fontMetrics.ascent + fontMetrics.descent)) / 2;
                break;
        }
        return y;
    }


}

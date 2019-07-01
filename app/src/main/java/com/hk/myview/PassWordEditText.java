package com.hk.myview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;


/**
 * 固定密码长度输入框
 */

@SuppressLint("AppCompatCustomView")
public class PassWordEditText extends EditText {
    private Paint mBoundPaint;//外边框画笔
    private Paint mLinePaint;//分割线画笔
    private Paint mPsdPointPaint;//密码画笔

    private int boundPaintColor;
    private int partLineColor;
    private int psdPointColor;

    private int mPasswordTextLength;//输入密码的长度 

    private int mWidth;
    private int mHeight;

    private static final int PWD_LENGTH = 6;//密码长度
    private static final int PWD_POINT_RADIUS = 16;//小圆点半径

    private OnTextEndListener onTextEndListener;

    public void setOnTextEndListener(OnTextEndListener onTextEndListener) {
        this.onTextEndListener = onTextEndListener;
    }

    public PassWordEditText(Context context) {
        this(context, null);
    }

    public PassWordEditText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PassWordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PassWordEditText);
        boundPaintColor = typedArray.getColor(R.styleable.PassWordEditText_bound_line_color, Color.WHITE);
        partLineColor = typedArray.getColor(R.styleable.PassWordEditText_parting_line_color, Color.GRAY);
        psdPointColor = typedArray.getColor(R.styleable.PassWordEditText_point_color, Color.BLACK);
        typedArray.recycle();
        init();
    }

    private void init() {
        //设置获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //移除自带光标
        setCursorVisible(false);

        mBoundPaint = new Paint();
        mBoundPaint.setStrokeWidth(8);
        mBoundPaint.setAntiAlias(true);
        mBoundPaint.setColor(boundPaintColor);
        //实心矩形为了覆盖编辑框原有的字符串，有兴趣的同仁可以设置为空心矩形试一下
        // mBoundPaint.setStyle(Paint.Style.STROKE);
        mBoundPaint.setStyle(Paint.Style.FILL);

        mLinePaint = new Paint();
        mLinePaint.setStrokeWidth(4);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(partLineColor);

        mPsdPointPaint = new Paint();
        mPsdPointPaint.setStrokeWidth(12);
        mPsdPointPaint.setAntiAlias(true);
        mPsdPointPaint.setColor(psdPointColor);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
        drawRoundRect(canvas);
        drawPsdLine(canvas);
        drawPsdPoints(canvas);
    }

    /**
     * 画圆角边框
     *
     * @param canvas
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void drawRoundRect(Canvas canvas) {
        canvas.drawRoundRect(0, 0, mWidth, mHeight, 12, 12, mBoundPaint);
    }

    /**
     * 画密码分割线
     *
     * @param canvas
     */
    private void drawPsdLine(Canvas canvas) {
        for (int i = 1; i < PWD_LENGTH; i++) {
            float mX = mWidth * i / PWD_LENGTH;
            canvas.drawLine(mX, 12, mX, mHeight - 12, mLinePaint);
        }
    }

    /**
     * 绘画密码点
     *
     * @param canvas
     */
    private void drawPsdPoints(Canvas canvas) {
        float cx, cy = mHeight / 2f;
        float half = mWidth / PWD_LENGTH;
        for (int i = 0; i < mPasswordTextLength; i++) {
            cx = half / 2 + half * i;
            canvas.drawCircle(cx, cy, PWD_POINT_RADIUS, mPsdPointPaint);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        mPasswordTextLength = text.toString().length();
        if (mPasswordTextLength == PWD_LENGTH) {//这边可以做接口回调，或者用过eventBus的同仁们进行事件发布
            if (onTextEndListener != null) {
                onTextEndListener.onTextEndListener(text.toString());
            }
            return;
        }
        invalidate();
    }

    public interface OnTextEndListener {
        void onTextEndListener(String textString);
    }

}
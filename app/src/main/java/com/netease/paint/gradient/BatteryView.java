package com.netease.paint.gradient;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @Package: com.sybersense.myapplication
 * @Description: java类作用描述
 * @Author: chay
 * @CreateDate: 2020/7/20 17:46
 */
public class BatteryView extends View {
    private static final String TAG = "BatteryView";

    public static final int BATTERY_LOSS=1;
    public static final int BATTERY_CHARGE=2;
    public static final int BATTERY_WORK=3;
    private int mPower;
    private int width;
    private int height;
    private int mColor;
    private Bitmap mBitmap;
    /**
     * 根据电池的装填，绘制不同的样式
     * <enum name="battery_loss" value="1"/>
     * <enum name="battery_charge" value="2"/>
     * <enum name="battery_work" value="3"/>
     */
    private int mStatus;
    /**
     * 电池外边框的圆角半径
     */
    private float radius = 4;
    /**
     * 电池内外边框的间距
     */
    private float spaceBetween = 2;

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Battery);
        mColor = typedArray.getColor(R.styleable.Battery_batteryColor, 0xFFFFFFFF);
        mPower = typedArray.getInt(R.styleable.Battery_batteryPower, 100);
        mStatus = typedArray.getInt(R.styleable.Battery_batteryStatus, 3);

        mBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.battery_charge);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBattery(canvas);
    }

    /**
     * 绘平电池
     *
     * @param canvas
     */
    private void drawBattery(Canvas canvas) {
        //电池外边框
        Paint paint = new Paint();
        paint.setColor(mColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        //绘制外边框
        float strokeWidth = width / 20.f;
        float left = strokeWidth / 2;
        float top = strokeWidth / 2;
        float right = width - strokeWidth - left;
        float bottom = height - top;
        paint.setStrokeWidth(strokeWidth);
        RectF r1 = new RectF(left, top, right, bottom);
        if (mStatus == BATTERY_LOSS) {
            paint.setColor(mColor);
        } else if (mStatus == BATTERY_CHARGE || mStatus == BATTERY_WORK) {
            if (mPower < 30) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.WHITE);
            }
        }
        canvas.drawRoundRect(r1, radius, radius, paint);
        //绘制内边框
        paint.setStrokeWidth(0);
        paint.setStyle(Paint.Style.FILL);
        if (mStatus == BATTERY_LOSS) {
            //画电池内矩形电量
            mPower = 100;
            float right1 = (width - strokeWidth * 2 - spaceBetween * 2 - strokeWidth) * mPower / 100.f;
            RectF r2 = new RectF(strokeWidth + spaceBetween, strokeWidth + spaceBetween, right1 + strokeWidth + spaceBetween, height - strokeWidth - spaceBetween);
            paint.setColor(Color.WHITE);
            canvas.drawRect(r2, paint);
            //画出两条红色交叉线条
            paint.setColor(Color.RED);
            paint.setStrokeWidth(2);
            canvas.drawLine(strokeWidth, strokeWidth, right, bottom, paint);
            canvas.drawLine(strokeWidth, bottom, right, strokeWidth, paint);
        } else if (mStatus == BATTERY_CHARGE) {
            //画电池内矩形电量
            float offset = (width - strokeWidth * 2 - spaceBetween * 2 - strokeWidth) * mPower / 100.f;
            RectF r2 = new RectF(strokeWidth + spaceBetween, strokeWidth + spaceBetween, offset + strokeWidth + spaceBetween, height - strokeWidth - spaceBetween);
            //根据电池电量决定电池内矩形电量颜色
            if (mPower < 30) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.WHITE);
            }
            canvas.drawRect(r2, paint);
            canvas.drawBitmap(mBitmap, (width - mBitmap.getWidth()) / 2, (height - mBitmap.getHeight()) / 2, paint);
        } else if (mStatus == BATTERY_WORK) {
            //画电池内矩形电量
            float offset = (width - strokeWidth * 2 - spaceBetween * 2 - strokeWidth) * mPower / 100.f;
            RectF r2 = new RectF(strokeWidth + spaceBetween, strokeWidth + spaceBetween, offset + strokeWidth + 2, height - strokeWidth - 2);
            //根据电池电量决定电池内矩形电量颜色
            if (mPower < 30) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.WHITE);
            }
            canvas.drawRect(r2, paint);
        }
        //画电池头
        RectF r3 = new RectF(width - strokeWidth, height * 0.25f, width, height * 0.75f);
        if (mStatus == BATTERY_LOSS) {
            paint.setColor(mColor);
        } else if (mStatus == BATTERY_CHARGE || mStatus == BATTERY_WORK) {
            if (mPower < 30) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.WHITE);
            }
        }
        canvas.drawRect(r3, paint);
    }

    /**
     * 设置电池电量
     *
     * @param power
     */
    public void setPower(int power) {
        this.mPower = power;
        if (mPower >= 100) {
            mPower = 100;
        } else if (mPower <= 1) {
            mPower = 1;
        }
        invalidate();
    }

    public void setBatteryStatus(int status) {
        this.mStatus = status;
        invalidate();
    }

    /**
     * 设置电池颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    /**
     * 获取电池电量
     *
     * @return
     */
    public int getPower() {
        return mPower;
    }
}
